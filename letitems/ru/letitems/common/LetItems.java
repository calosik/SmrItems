package ru.letitems.common;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Sql2o;
import ru.letitems.common.block.BlockMinePortal;
import ru.letitems.common.event.BagEventHandler;
import ru.letitems.common.event.EventHandlerServer;
import ru.letitems.common.lib.creativetab.DakimakuraTab;
import ru.letitems.common.lib.creativetab.PrimaryTab;
import ru.letitems.common.registry.ItemRegistry;
import ru.letitems.common.util.module.IModule;

@Mod(
   modid = "letitems",
   name = "LetItems",
   version = "2.7",
   dependencies = "after:IC2"
)
public final class LetItems implements LoadingCallback {
   public static final Logger LOGGER = LogManager.getLogger("LetItems");
   public static final List<IModule> MODULES = new ArrayList();
   @Instance
   public static LetItems instance;
   public static CreativeTabs tabLetItems = new PrimaryTab();
   public static CreativeTabs tabLetItemsDakies = new DakimakuraTab();
   @SidedProxy(
      clientSide = "ru.letitems.client.ClientProxy",
      serverSide = "ru.letitems.common.CommonProxy"
   )
   public static CommonProxy proxy;
   public static boolean loadIC2;
   public static boolean loadAE2;
   public static boolean loadThaumCraft;
   public static boolean loadWaila;
   public static boolean loadNEI;
   public static boolean loadBotania;
   public static boolean loadGC;
   public static boolean loadForestry;
   public static boolean loadTF;
   public static boolean loadBaubles;
   public static boolean loadTConstruct;
   public static boolean loadWitchery;
   public static boolean loadEP;
   @SideOnly(Side.SERVER)
   public static Sql2o sql2o;

   @EventHandler
   public void construct(FMLConstructionEvent event) {
      proxy.initModules(MODULES);
   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      loadIC2 = Loader.isModLoaded("IC2");
      loadAE2 = Loader.isModLoaded("appliedenergistics2");
      loadThaumCraft = Loader.isModLoaded("Thaumcraft");
      loadWaila = Loader.isModLoaded("waila");
      loadNEI = Loader.isModLoaded("nei");
      loadBotania = Loader.isModLoaded("Botania");
      loadGC = Loader.isModLoaded("GalacticraftCore");
      loadForestry = Loader.isModLoaded("Forestry");
      loadTF = Loader.isModLoaded("TwilightForest");
      loadBaubles = Loader.isModLoaded("Baubles");
      loadTConstruct = Loader.isModLoaded("TConstruct");
      loadWitchery = Loader.isModLoaded("witchery");
      loadEP = Loader.isModLoaded("Enchanting Plus");
      ItemRegistry.init();
      BagEventHandler.init();
      EventHandlerServer.init();
      proxy.preInit(event);
      ForgeChunkManager.setForcedChunkLoadingCallback(instance, this);
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
      proxy.init(event);
      BlockMinePortal.init();
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit(event);
   }

   @EventHandler
   public void serverStart(FMLServerStartingEvent event) {
   }

   public static boolean isPexPlayer(EntityPlayer player) {
      return false;
   }

   public static boolean isPexPlayer(EntityPlayer player, String pex) {
      return false;
   }

   public static boolean isAFK(EntityPlayer player) {
      return false;
   }

   public void ticketsLoaded(List<Ticket> tickets, World world) {
   }
}
