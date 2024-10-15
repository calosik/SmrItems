package ru.SmrItems;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import ru.SmrItems.common.blocks.BlockWorldAnchor;
import ru.SmrItems.common.handlers.GuiHandler;
import ru.SmrItems.common.tileentity.TileWorldAnchor;
import ru.SmrItems.items.ItemBlockWorldAnchor;

public class Registry {
   public static class Blocks {
      public static final Block blockWorldAnchor = new BlockWorldAnchor();

      public static void register() {
         GameRegistry.registerBlock(blockWorldAnchor, ItemBlockWorldAnchor.class, blockWorldAnchor.getUnlocalizedName());
      }
   }

   public static class TileEntity {
      public static void register() {
         GameRegistry.registerTileEntity(TileWorldAnchor.class, "tileWorldAnchor");
      }
   }

   public static class Gui {
      public static void register() {
         NetworkRegistry.INSTANCE.registerGuiHandler(SmMain.INSTANCE, new GuiHandler());
      }
   }
}
