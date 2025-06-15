package ru.letitems.common.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ru.letitems.common.LetItems;

public class Crafting {
   public static void init() {
      GameRegistry.addRecipe(new ItemStack(RegItems.itemBag1, 1), new Object[]{"FFY", "XNX", "XXX", 'F', Items.string, 'X', Items.leather, 'Y', new ItemStack(Items.dye, 1, 10), 'N', Blocks.chest});
      GameRegistry.addRecipe(new ItemStack(RegItems.itemBag2, 1), new Object[]{"FFY", "XNX", "XXX", 'F', Items.string, 'X', Items.leather, 'Y', new ItemStack(Items.dye, 1, 6), 'N', Blocks.chest});
      GameRegistry.addRecipe(new ItemStack(RegItems.itemBag3, 1), new Object[]{"FFY", "XNX", "XXX", 'F', Items.string, 'X', Items.leather, 'Y', new ItemStack(Items.dye, 1, 14), 'N', Blocks.chest});
      GameRegistry.addRecipe(new ItemStack(RegItems.itemBag4, 1), new Object[]{"FFY", "XIX", "KMJ", 'F', Items.string, 'X', Items.leather, 'Y', Items.fish, 'I', Items.diamond, 'K', RegItems.itemBag1, 'M', RegItems.itemBag2, 'J', RegItems.itemBag3});
      GameRegistry.addRecipe(new ItemStack(RegItems.itemBag5, 1), new Object[]{"XXX", "XOX", "KMJ", 'X', RegItems.ItemMagicShard, 'K', RegItems.itemBag1, 'M', RegItems.itemBag2, 'J', RegItems.itemBag3, 'O', RegItems.itemBag4});
      if (LetItems.loadIC2) {
         GameRegistry.addRecipe(new ItemStack(RegItems.itemArtifactRyota, 1), new Object[]{"BBB", "BXB", "AMA", 'M', IC2Items.getItem("advancedMachine"), 'X', IC2Items.getItem("evTransformer"), 'A', IC2Items.getItem("advancedCircuit"), 'B', IC2Items.getItem("carbonPlate")});
      }

      GameRegistry.addRecipe(new ItemStack(RegItems.blockVendingMachine, 6), new Object[]{"YYY", "YXY", "KKK", 'Y', Blocks.glass, 'K', Blocks.stone, 'X', RegItems.ItemMagicShard});
      GameRegistry.addRecipe(new ItemStack(RegItems.blockPlayerDetector), new Object[]{"KUK", "HYH", "HKH", 'Y', Blocks.obsidian, 'K', Items.redstone, 'H', Items.flint, 'U', new ItemStack(Items.dye, 1, 6)});
      GameRegistry.addRecipe(new ItemStack(RegItems.itemSiteCrafting), new Object[]{"DGD", "DSD", "DID", 'D', Items.diamond_sword, 'I', Items.iron_sword, 'G', Items.golden_sword, 'S', Items.nether_star});
      GameRegistry.addRecipe(new ItemStack(RegItems.itemSiteCrafting, 1, 2), new Object[]{"YYY", "YYY", "YKY", 'Y', Items.leather, 'K', Items.paper});
      GameRegistry.addSmelting(RegItems.ItemMagicShard, new ItemStack(RegItems.itemSiteCrafting, 4, 3), 25.0F);
      GameRegistry.addRecipe(new ItemStack(RegItems.itemRRPistol, 1), new Object[]{"GII", "IIP", "  P", 'I', Items.iron_ingot, 'P', Blocks.planks, 'G', Items.gunpowder});
      int[] cmdString = new int[]{1, 12, 10, 14};

      for(int i = 0; i < cmdString.length; ++i) {
         GameRegistry.addRecipe(new ItemStack(RegItems.ItemCmd, 32, i), new Object[]{"BR ", "ST ", "   ", 'S', new ItemStack(RegItems.itemSiteCrafting, 1, 2), 'B', Items.dye, 'T', Items.feather, 'R', new ItemStack(Items.dye, 1, cmdString[i])});
      }

      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stick, 16, 0), new Object[]{"I  ", "I  ", "   ", 'I', "logWood"}));
   }
}
