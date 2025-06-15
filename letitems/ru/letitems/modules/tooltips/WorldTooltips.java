package ru.letitems.modules.tooltips;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.util.module.AbstractModule;

@SideOnly(Side.CLIENT)
public final class WorldTooltips extends AbstractModule {
   public static final WorldTooltips instance = new WorldTooltips();
   public static int maxDistance;
   public static float alpha;

   private WorldTooltips() {
   }

   public void preInitClient(FMLPreInitializationEvent event) {
      super.preInitClient(event);
      this.switchEnable();
   }

   public void initClient(FMLInitializationEvent event) {
      super.initClient(event);
      Tooltip.init();
   }

   public void switchEnable() {
      if (((ClientProxy)LetItems.proxy).getModSettings().tplEnabled) {
         MinecraftForge.EVENT_BUS.register(RenderEventHandler.INSTANCE);
      } else {
         MinecraftForge.EVENT_BUS.unregister(RenderEventHandler.INSTANCE);
      }

      this.syncSettings();
   }

   public void syncSettings() {
      maxDistance = ((ClientProxy)LetItems.proxy).getModSettings().tplDistance;
      alpha = ((ClientProxy)LetItems.proxy).getModSettings().tplAlpha;
   }
}
