package ru.letitems.common.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.letitems.common.registry.RegItems;

public final class BlockOreArtefor extends BlockBase {
   private final Random rand = new Random();

   public BlockOreArtefor(String name) {
      super(Material.rock, name);
      this.setHardness(2.5F);
      this.setStepSound(Block.soundTypeStone);
   }

   protected boolean canSilkHarvest() {
      return false;
   }

   public Item getItemDropped(int metadata, Random random, int par3) {
      return RegItems.ItemArtefe;
   }

   public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
      return MathHelper.getRandomIntegerInRange(this.rand, 0, 4);
   }

   public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
      super.onBlockHarvested(world, x, y, z, meta, player);
   }
}
