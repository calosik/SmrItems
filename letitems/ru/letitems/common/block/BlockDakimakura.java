package ru.letitems.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ru.letitems.common.LetItems;
import ru.letitems.common.items.blocks.ItemBlockDakimakura;
import ru.letitems.common.tile.TileDakimakura;
import ru.letitems.common.util.registry.IHasItemBlock;

public class BlockDakimakura extends BlockBaseContainer<TileDakimakura> implements IHasItemBlock {
   private static final int META_BIT_STANDING = 0;
   private static final int META_BIT_POS_NEG = 1;
   private static final int META_BIT_X_Z = 2;
   private static final int META_BIT_TOP_BOT = 3;

   public BlockDakimakura() {
      super(Material.cloth, "dakimakura");
      this.setHardness(0.35F);
      this.setStepSound(soundTypeCloth);
      this.setCreativeTab(LetItems.tabLetItemsDakies);
   }

   @SideOnly(Side.CLIENT)
   public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
      return true;
   }

   public int quantityDropped(Random random) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      this.setBlockBoundsBasedOnState(world, x, y, z);
      return super.getCollisionBoundingBoxFromPool(world, x, y, z);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
      int meta = blockAccess.getBlockMetadata(x, y, z);
      ForgeDirection rot = getRotation(meta);
      boolean standing = isStanding(meta);
      boolean topPart = isTopPart(meta);
      float w1 = 0.2F;
      float w2 = 0.8F;
      float h1 = 0.01F;
      float h2 = 0.28F;
      float d1 = 0.1F;
      float d2 = 1.8F;
      float x1 = 0.2F;
      float x2 = 0.8F;
      float y1 = 0.01F;
      float y2 = 0.28F;
      float z1 = 0.1F;
      float z2 = 1.8F;
      if (!standing) {
         switch(rot) {
         case NORTH:
            z1 = -0.79999995F;
            z2 = 0.9F;
            break;
         case EAST:
            x1 = 0.1F;
            x2 = 1.8F;
            z1 = 0.2F;
            z2 = 0.8F;
            break;
         case WEST:
            x1 = -0.79999995F;
            x2 = 0.9F;
            z1 = 0.19999999F;
            z2 = 0.8F;
         }

         if (!topPart) {
            this.setBlockBounds(x1, y1, z1, x2, y2, z2);
         } else {
            this.setBlockBounds(x1 - 1.0F * (float)rot.offsetX, y1 - 1.0F * (float)rot.offsetY, z1 - 1.0F * (float)rot.offsetZ, x2 - 1.0F * (float)rot.offsetX, y2 - 1.0F * (float)rot.offsetY, z2 - 1.0F * (float)rot.offsetZ);
         }
      } else {
         y1 = 0.1F;
         y2 = 1.8F;
         switch(rot) {
         case NORTH:
            z1 = 0.01F;
            z2 = 0.28F;
            break;
         case EAST:
            x1 = 0.72F;
            x2 = 0.99F;
            z1 = 0.2F;
            z2 = 0.8F;
            break;
         case WEST:
            x1 = 0.01F;
            x2 = 0.28F;
            z1 = 0.2F;
            z2 = 0.8F;
            break;
         case SOUTH:
            z1 = 0.72F;
            z2 = 0.99F;
         }

         if (!topPart) {
            this.setBlockBounds(x1, y1, z1, x2, y2, z2);
         } else {
            this.setBlockBounds(x1, y1 - 1.0F, z1, x2, y2 - 1.0F, z2);
         }
      }

   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      int meta = world.getBlockMetadata(x, y, z);
      ForgeDirection dir = getRotation(meta);
      boolean topPart = isTopPart(meta);
      boolean standing = isStanding(meta);
      if (!standing) {
         if (!topPart) {
            if (world.getBlock(x + dir.offsetX, y, z + dir.offsetZ) != this) {
               world.setBlockToAir(x, y, z);
            }
         } else if (world.getBlock(x - dir.offsetX, y, z - dir.offsetZ) != this) {
            world.setBlockToAir(x, y, z);
         }
      } else if (!topPart) {
         if (world.getBlock(x, y + 1, z) != this) {
            world.setBlockToAir(x, y, z);
         }
      } else if (world.getBlock(x, y - 1, z) != this) {
         world.setBlockToAir(x, y, z);
      }

   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileDakimakura();
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int getRenderType() {
      return -1;
   }

   public Class<TileDakimakura> getTileEntityClass() {
      return TileDakimakura.class;
   }

   public Class<? extends ItemBlock> getItemBlockClass() {
      return ItemBlockDakimakura.class;
   }

   public static int setRotationOnMeta(int meta, ForgeDirection rot) {
      if (rot == ForgeDirection.NORTH | rot == ForgeDirection.WEST) {
         meta = setBit(meta, 1, false);
      } else if (rot == ForgeDirection.SOUTH | rot == ForgeDirection.EAST) {
         meta = setBit(meta, 1, true);
      }

      if (rot == ForgeDirection.EAST | rot == ForgeDirection.WEST) {
         meta = setBit(meta, 2, true);
      } else if (rot == ForgeDirection.NORTH | rot == ForgeDirection.SOUTH) {
         meta = setBit(meta, 2, false);
      }

      return meta;
   }

   public static ForgeDirection getRotation(int meta) {
      boolean xz = getBit(meta, 2) == 1;
      if (getBit(meta, 1) == 1) {
         return xz ? ForgeDirection.EAST : ForgeDirection.SOUTH;
      } else {
         return xz ? ForgeDirection.WEST : ForgeDirection.NORTH;
      }
   }

   public static int setStandingOnMeta(int meta, boolean standing) {
      return setBit(meta, 0, standing);
   }

   public static boolean isStanding(int meta) {
      return getBit(meta, 0) == 1;
   }

   public static int setTopPartOnMeta(int meta, boolean topPart) {
      return setBit(meta, 3, topPart);
   }

   public static boolean isTopPart(int meta) {
      return getBit(meta, 3) == 1;
   }

   private static int getBit(int value, int index) {
      return value >> index & 1;
   }

   private static int setBit(int value, int index, boolean on) {
      return on ? value | 1 << index : value & ~(1 << index);
   }
}
