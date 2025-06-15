package ru.letitems.modules.fpsreducer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import org.lwjgl.opengl.Display;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.util.module.AbstractModule;

@SideOnly(Side.CLIENT)
public final class FPSReducer extends AbstractModule {
   public static final FPSReducer instance = new FPSReducer();
   public static boolean reduceFPS = false;
   public static boolean reduceFPSEnable = false;
   private final Minecraft mc = Minecraft.getMinecraft();
   public boolean fpsReducerDisplay;
   public boolean fpsReducerSound;
   private int currentGameSettingFPS;
   private int currentGameSettingParticle;
   private float currentGameSettingVolume;
   private boolean stopSound = false;
   private boolean prevWindowStatus = true;

   private FPSReducer() {
   }

   public void preInitClient(FMLPreInitializationEvent event) {
      super.preInitClient(event);
      this.switchEnable();
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.START) {
         if (this.mc.theWorld == null) {
            this.recoverFPS();
            return;
         }

         if (this.fpsReducerDisplay || this.fpsReducerSound) {
            boolean curWindowStatus = Display.isActive();
            if (!curWindowStatus) {
               if (this.prevWindowStatus) {
                  this.prevWindowStatus = false;
                  if (this.fpsReducerDisplay) {
                     this.reduceFPS();
                  }

                  if (this.fpsReducerSound) {
                     this.stopSound();
                  }
               }

               if (this.fpsReducerDisplay) {
                  return;
               }
            } else if (!this.prevWindowStatus) {
               this.prevWindowStatus = true;
               this.recoverSound();
            }
         }

         this.recoverFPS();
      }

   }

   private void reduceFPS() {
      if (!reduceFPS) {
         this.currentGameSettingFPS = this.mc.gameSettings.limitFramerate;
         this.currentGameSettingParticle = this.mc.gameSettings.particleSetting;
         this.mc.gameSettings.limitFramerate = 5;
         this.mc.gameSettings.particleSetting = 2;
         reduceFPS = true;
      }

   }

   private void recoverFPS() {
      if (reduceFPS) {
         this.mc.gameSettings.limitFramerate = this.currentGameSettingFPS;
         this.mc.gameSettings.particleSetting = this.currentGameSettingParticle;
         reduceFPS = false;
      }

   }

   private void stopSound() {
      if (!this.stopSound) {
         this.currentGameSettingVolume = this.mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
         float newVolume = this.currentGameSettingVolume * 10.0F / 100.0F;
         this.mc.gameSettings.setSoundLevel(SoundCategory.MASTER, newVolume);
         this.stopSound = true;
      }

   }

   private void recoverSound() {
      if (this.stopSound) {
         this.mc.gameSettings.setSoundLevel(SoundCategory.MASTER, this.currentGameSettingVolume);
         this.stopSound = false;
      }

   }

   public void switchEnable() {
      this.fpsReducerDisplay = ((ClientProxy)LetItems.proxy).getModSettings().fpsReducerDisplay;
      this.fpsReducerSound = ((ClientProxy)LetItems.proxy).getModSettings().fpsReducerSound;
      if (!this.fpsReducerDisplay && !this.fpsReducerSound) {
         FMLCommonHandler.instance().bus().unregister(instance);
         reduceFPSEnable = false;
      } else {
         if (!reduceFPSEnable) {
            FMLCommonHandler.instance().bus().register(instance);
         }

         reduceFPSEnable = true;
      }

   }
}
