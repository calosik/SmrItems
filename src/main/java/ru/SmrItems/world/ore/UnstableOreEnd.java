package ru.SmrItems.world.ore;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import ru.SmrItems.SmMain;
import ru.SmrItems.registry.RegItems;

public class UnstableOreEnd extends Block {
   private final int maxStackSize;

   public UnstableOreEnd(String name, String texture, int maxStackSize) {
      super(Material.rock);
      this.setBlockName("SmrItems:" + name);
      this.setBlockTextureName("SmrItems:" + texture);
      this.setCreativeTab(SmMain.tabsmritems);
      this.setHarvestLevel("pickaxe", 2);
      this.maxStackSize = maxStackSize;
      GameRegistry.registerBlock(this, name);
      this.setHardness(2.5F);
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return RegItems.unstabledrop;
   }

   protected boolean canSilkHarvest() {
      return false;
   }

   public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
      return MathHelper.getRandomIntegerInRange(new Random(), 0, 4);
   }
}
