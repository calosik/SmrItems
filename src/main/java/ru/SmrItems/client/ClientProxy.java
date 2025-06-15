package ru.SmrItems.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import ru.SmrItems.client.events.EventKeybinding;
import ru.SmrItems.client.events.EventVisualRenderer;
import ru.SmrItems.client.render.ItemRenderDoll;
import ru.SmrItems.client.render.TileRenderDoll;
import ru.SmrItems.common.CommonProxy;
import ru.SmrItems.common.tileentity.TileEntityDoll;
import ru.SmrItems.modules.main.ClientEventHandler;
import ru.SmrItems.registry.RegItems;
import ru.SmrItems.util.CharacterUtils;
import ru.SmrItems.util.CharacterUtils;

public class ClientProxy extends CommonProxy {
   public static final KeyBinding visualRenderKeybind = new KeyBinding("key.chunkloading.view", 68, "key.chunkloading.category");

   public void preInit(FMLPreInitializationEvent event) {
      Display.setTitle("Minecraft 1.7.10 - SmrProject");
      ClientEventHandler.init();
      CharacterUtils.loadCharacterInfo();
      MinecraftForge.EVENT_BUS.register(this);
      super.preInit(event);
   }

   public void init(FMLInitializationEvent event) {
      super.init(event);
      FMLCommonHandler.instance().bus().register(new EventKeybinding());
      MinecraftForge.EVENT_BUS.register(new EventVisualRenderer());
   }

   public void postInit(FMLPostInitializationEvent event) {
      super.postInit(event);
      ClientRegistry.registerKeyBinding(visualRenderKeybind);
   }

   public void registerRenderers() {
      super.registerRenderers();
      Block[] var2 = RegItems.DollBlocks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Block dollBlock = var2[var4];
         MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(dollBlock), new ItemRenderDoll());
         ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDoll.class, new TileRenderDoll());
      }

   }
}
