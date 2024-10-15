package ru.SmrItems.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.ForgeChunkManager;
import ru.SmrItems.Config;
import ru.SmrItems.Registry;
import ru.SmrItems.SmMain;
import ru.SmrItems.common.handlers.ChunkLoadingHandler;
import ru.SmrItems.common.handlers.PacketHandler;

public class CommonProxy {
   public void preInit(FMLPreInitializationEvent event) {
      Config.init();
   }

   public void init(FMLInitializationEvent event) {
      Registry.Blocks.register();
      this.registerRenderers();
      Registry.TileEntity.register();
      Registry.Gui.register();
   }

   public void registerRenderers() {
   }

   public void postInit(FMLPostInitializationEvent event) {
      ForgeChunkManager.setForcedChunkLoadingCallback(SmMain.INSTANCE, new ChunkLoadingHandler());
      PacketHandler.init();
   }
}
