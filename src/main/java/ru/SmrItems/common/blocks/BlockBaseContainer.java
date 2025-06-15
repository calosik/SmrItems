package ru.SmrItems.common.blocks;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.SmrItems.SmMain;
import ru.SmrItems.common.tileentity.TileBase;
import ru.SmrItems.util.registry.IHasName;
import ru.SmrItems.util.registry.IHasTileEntity;

public abstract class BlockBaseContainer<T extends TileBase> extends BlockContainer implements IHasName, IHasTileEntity<T> {
   private final String name;

   public BlockBaseContainer(Material material, String name) {
      super(material);
      this.name = name;
      this.setBlockName(name);
      this.setBlockTextureName("SmrItems:" + name);
      this.setCreativeTab(SmMain.tabsmritems);
   }

   public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileBase) {
         ((TileBase)tile).onBlockClicked(player);
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset) {
      TileEntity tile = world.getTileEntity(x, y, z);
      return tile instanceof TileBase && ((TileBase)tile).onBlockActivated(player, side);
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
      super.onBlockPlacedBy(world, x, y, z, placer, stack);
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileBase) {
         ((TileBase)tile).onBlockPlacedBy(placer, stack);
      }

   }

   public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
      return this.getDrops(world, x, y, z, meta, fortune, true);
   }

   protected ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune, boolean onBreak) {
      TileEntity tile = world.getTileEntity(x, y, z);
      return tile instanceof TileBase ? ((TileBase)tile).getDrops(this, meta, fortune, onBreak) : super.getDrops(world, x, y, z, meta, fortune);
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
      return (ItemStack)this.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0, false).get(0);
   }

   public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int meta) {
      return false;
   }

   protected boolean canSilkHarvest() {
      return false;
   }

   public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileBase) {
         ((TileBase)tile).onBlockRemoval(block, meta);
      }

      super.breakBlock(world, x, y, z, block, meta);
   }

   public String getName() {
      return this.name;
   }
}
