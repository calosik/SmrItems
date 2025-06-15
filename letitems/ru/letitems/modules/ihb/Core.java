package ru.letitems.modules.ihb;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import ru.letitems.common.util.module.AbstractModule;

@SideOnly(Side.CLIENT)
public final class Core extends AbstractModule {
   public static final Core instance = new Core();
   private final Map<UUID, Float> healthSmooth = new HashMap();
   public static boolean GAUGE_ENABLED = true;
   public static boolean HP_ENABLED = true;

   public void initClient(FMLInitializationEvent event) {
      (new Core.RenderManagerUpdater(RenderManager.instance)).start();
   }

   public float getHealth(Minecraft mc, Entity entity, float time) {
      UUID uuid = entity.getUniqueID();
      float healthReal;
      if (entity instanceof EntityLivingBase) {
         healthReal = ((EntityLivingBase)entity).getHealth();
      } else {
         healthReal = entity.isDead ? 0.0F : 1.0F;
      }

      if (this.healthSmooth.containsKey(uuid)) {
         float healthValue = (Float)this.healthSmooth.get(uuid);
         if (healthReal <= 0.0F && entity instanceof EntityLivingBase) {
            float value = (float)(18 - ((EntityLivingBase)entity).deathTime) / 18.0F;
            if (value <= 0.0F) {
               this.healthSmooth.remove(uuid);
            }

            return healthValue * value;
         } else {
            if (Math.round(healthValue * 10.0F) != Math.round(healthReal * 10.0F)) {
               healthValue += (healthReal - healthValue) * this.gameTimeDelay(mc, time) * 0.075F;
            } else {
               healthValue = healthReal;
            }

            this.healthSmooth.put(uuid, healthValue);
            return healthValue;
         }
      } else {
         this.healthSmooth.put(uuid, healthReal);
         return healthReal;
      }
   }

   private float gameTimeDelay(Minecraft mc, float time) {
      return time >= 0.0F ? time : 36.0F / (float)mc.getLimitFramerate();
   }

   public float getMaxHealth(Entity entity) {
      return entity instanceof EntityLivingBase ? ((EntityLivingBase)entity).getMaxHealth() : 1.0F;
   }

   @SideOnly(Side.CLIENT)
   private static final class RenderManagerUpdater extends Thread {
      private final RenderManager manager;

      private RenderManagerUpdater(RenderManager manager) {
         this.manager = manager;
      }

      public void run() {
         for(; this.manager != null; sleep(10000L)) {
            try {
               Iterator var1 = this.manager.entityRenderMap.keySet().iterator();

               while(var1.hasNext()) {
                  Object key = var1.next();
                  if (key instanceof Class && EntityLivingBase.class.isAssignableFrom((Class)key)) {
                     Object value = this.manager.entityRenderMap.get(key);
                     if (value instanceof Render && !(value instanceof RenderBase)) {
                        RenderBase render = new RenderBase((Render)value);
                        this.manager.entityRenderMap.put(key, render);
                        render.setRenderManager(this.manager);
                     }
                  }
               }
            } catch (Exception var5) {
               sleep(1000L);
            }
         }

      }

      public static void sleep(long time) {
         try {
            Thread.sleep(time);
         } catch (InterruptedException var3) {
         }

      }

      // $FF: synthetic method
      RenderManagerUpdater(RenderManager x0, Object x1) {
         this(x0);
      }
   }
}
