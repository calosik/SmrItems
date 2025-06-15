package ru.letitems.common.util.module;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.Configuration;

public abstract class AbstractModule implements IModule {
   public final void preInit(FMLPreInitializationEvent event) {
      this.preInitCommon(event);
      if (FMLCommonHandler.instance().getSide().isClient()) {
         this.preInitClient(event);
      }

   }

   @SideOnly(Side.CLIENT)
   public void preInitClient(FMLPreInitializationEvent event) {
   }

   public void preInitCommon(FMLPreInitializationEvent event) {
   }

   public final void init(FMLInitializationEvent event) {
      this.initCommon(event);
      if (FMLCommonHandler.instance().getSide().isClient()) {
         this.initClient(event);
      }

   }

   @SideOnly(Side.CLIENT)
   public void initClient(FMLInitializationEvent event) {
   }

   public void initCommon(FMLInitializationEvent event) {
   }

   public final void postInit(FMLPostInitializationEvent event) {
      this.postInitCommon(event);
      if (FMLCommonHandler.instance().getSide().isClient()) {
         this.postInitClient(event);
      }

   }

   @SideOnly(Side.CLIENT)
   public void postInitClient(FMLPostInitializationEvent event) {
   }

   public void postInitCommon(FMLPostInitializationEvent event) {
   }

   public void readConfig(Configuration cfg) {
   }
}
