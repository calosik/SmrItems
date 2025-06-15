package ru.letitems.common.integration.waila;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaRegistrar;
import ru.letitems.common.block.BlockDakimakura;
import ru.letitems.common.block.BlockFlowersEvent;
import ru.letitems.common.block.BlockPlayerDetector;
import ru.letitems.common.block.BlockVendingMachine;
import ru.letitems.common.tile.TileDakimakura;
import ru.letitems.common.tile.TileEntityFlowersEvent;
import ru.letitems.common.tile.TilePlayerDetector;
import ru.letitems.common.tile.TileVendingMachine;

public class CompatWaila {
   public static void init(FMLInitializationEvent event) {
      if (Loader.isModLoaded("Waila")) {
         FMLInterModComms.sendMessage("Waila", "register", "ru.letitems.common.integration.waila.CompatWaila.registerCallback");
      }
   }

   public static void registerCallback(IWailaRegistrar registrar) {
      registrar.registerBodyProvider(new TileEntityFlowersEvent(), BlockFlowersEvent.class);
      registrar.registerStackProvider(new TileDakimakura(), BlockDakimakura.class);
      registrar.registerBodyProvider(new TileVendingMachine(), BlockVendingMachine.class);
      registrar.registerBodyProvider(new TilePlayerDetector(), BlockPlayerDetector.class);
   }
}
