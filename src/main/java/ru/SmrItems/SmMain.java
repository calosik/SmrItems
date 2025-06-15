package ru.SmrItems;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import ru.SmrItems.common.CommonProxy;
import ru.SmrItems.common.craft.Crafting;
import ru.SmrItems.common.lib.TabSmrItems;
import ru.SmrItems.registry.ItemRegistry;
import ru.SmrItems.world.GenOre;

@Mod(
   modid = "SmrItems",
   name = "SmrItems",
   version = "1.1B",
   dependencies = "after:IC2"
)
public class SmMain {
   @Instance("SmrItems")
   public static SmMain INSTANCE;
   public static GenOre genoresmr = new GenOre();
   public static final CreativeTabs tabsmritems = new TabSmrItems();
   @SidedProxy(
      clientSide = "ru.SmrItems.client.ClientProxy",
      serverSide = "ru.SmrItems.common.CommonProxy"
   )
   public static CommonProxy proxy;

   @EventHandler
   public void preLoad(FMLPreInitializationEvent evet) {
      GameRegistry.registerWorldGenerator(genoresmr, 0);
   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      proxy.preInit(event);
      ItemRegistry.init();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      Crafting.init();
      proxy.init(event);
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit(event);
   }
}
