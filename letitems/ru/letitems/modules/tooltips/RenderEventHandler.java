package ru.letitems.modules.tooltips;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

@SideOnly(Side.CLIENT)
public final class RenderEventHandler {
   public static final RenderEventHandler INSTANCE = new RenderEventHandler();
   private final Minecraft mc = Minecraft.getMinecraft();
   private Tooltip cache;

   private RenderEventHandler() {
   }

   @SubscribeEvent
   public void render(RenderWorldLastEvent event) {
      EntityItem entity = getMouseOver(this.mc, event.partialTicks);
      if (entity != null) {
         if (this.cache == null || this.cache.getEntity() != entity) {
            this.cache = new Tooltip(this.mc.thePlayer, entity);
         }

         this.cache.renderTooltip3D(this.mc, (double)event.partialTicks);
      }

   }

   private static EntityItem getMouseOver(Minecraft mc, float partialTicks) {
      EntityLivingBase viewer = mc.renderViewEntity;
      mc.mcProfiler.startSection("world-tooltips");
      int distanceLook = WorldTooltips.maxDistance;
      Vec3 eyes = viewer.getPosition(partialTicks);
      Vec3 look = viewer.getLook(partialTicks);
      Vec3 eyesLook = eyes.addVector(look.xCoord * (double)distanceLook, look.yCoord * (double)distanceLook, look.zCoord * (double)distanceLook);
      float distanceMax = 1.0F;
      List<? extends EntityItem> entityList = mc.theWorld.getEntitiesWithinAABB(EntityItem.class, viewer.boundingBox.addCoord(look.xCoord * (double)distanceLook, look.yCoord * (double)distanceLook, look.zCoord * (double)distanceLook).expand(1.0D, 1.0D, 1.0D));
      double targetDistanceSq = 0.0D;
      EntityItem target = null;
      Iterator var12 = entityList.iterator();

      while(true) {
         while(true) {
            EntityItem entity;
            MovingObjectPosition mop;
            do {
               do {
                  do {
                     if (!var12.hasNext()) {
                        mc.mcProfiler.endSection();
                        return target;
                     }

                     entity = (EntityItem)var12.next();
                  } while(entity == null);
               } while(entity.boundingBox == null);

               mop = mc.theWorld.func_147447_a(Vec3.createVectorHelper(viewer.posX, viewer.posY + (double)viewer.getEyeHeight(), viewer.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), false, true, false);
            } while(mop != null);

            float boundSize = 0.15F;
            AxisAlignedBB expandedAABB = entity.boundingBox.copy().offset(0.0D, 0.25D, 0.0D).expand(0.15D, 0.1D, 0.15D).expand(0.15000000596046448D, 0.15000000596046448D, 0.15000000596046448D);
            MovingObjectPosition objectInVector = expandedAABB.calculateIntercept(eyes, eyesLook);
            if (expandedAABB.isVecInside(eyes)) {
               if (0.0D <= targetDistanceSq) {
                  target = entity;
                  targetDistanceSq = 0.0D;
               }
            } else if (objectInVector != null) {
               double distance = eyes.squareDistanceTo(objectInVector.hitVec);
               if (distance < targetDistanceSq || targetDistanceSq == 0.0D) {
                  target = entity;
                  targetDistanceSq = distance;
               }
            }
         }
      }
   }
}
