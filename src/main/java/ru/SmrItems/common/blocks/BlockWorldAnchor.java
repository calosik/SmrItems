package ru.SmrItems.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.SmrItems.SmMain;
import ru.SmrItems.common.tileentity.TileWorldAnchor;
import ru.SmrItems.util.Logger;
import ru.SmrItems.util.enums.FieldType;
import ru.SmrItems.util.enums.LoadingMode;

public class BlockWorldAnchor extends BlockContainer {
   @SideOnly(Side.CLIENT)
   private IIcon icon_head;
   @SideOnly(Side.CLIENT)
   private IIcon icon_side;

   public BlockWorldAnchor() {
      super(Material.rock);
      this.setBlockName("SmrItems:blockWorldAnchor");
      this.setCreativeTab(SmMain.tabsmritems);
      this.setHardness(2.0F);
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileWorldAnchor();
   }

   public Item getItemDropped(int meta, Random random, int fortune) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icon_head = iconRegister.registerIcon("smritems:blockWorldAnchor.head");
      this.icon_side = iconRegister.registerIcon("smritems:blockWorldAnchor.side");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      return side == 1 ? this.icon_head : this.icon_side;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(BlockWorldAnchor block, IIconRegister iconRegister) {
      String blockTexture = block.getTextureName() + '.';
      this.icon_head = iconRegister.registerIcon(blockTexture + "head");
      this.icon_side = iconRegister.registerIcon(blockTexture + "side");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side == 1 ? this.icon_head : this.icon_side;
   }

   public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
      if (!worldIn.isRemote) {
         player.openGui(SmMain.INSTANCE, 0, worldIn, x, y, z);
      }

      return true;
   }

   public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
      if (!worldIn.isRemote) {
         System.out.println("[Anchors] Placed (" + x + ", " + y + ", " + z + ") world anchor by " + placer.getCommandSenderName());

         try {
            Logger.log("Placed (" + x + ", " + y + ", " + z + ") world anchor by " + placer.getCommandSenderName());
            Logger.Active.add(placer.getCommandSenderName(), x, y, z);
         } catch (IOException var11) {
            var11.printStackTrace();
         }

         NBTTagCompound nbtTagCompound = itemIn.getTagCompound();
         if (nbtTagCompound != null) {
            int chunkLoadingTime = nbtTagCompound.hasKey("chunkLoadingTime") ? nbtTagCompound.getInteger("chunkLoadingTime") : 0;
            LoadingMode mode = LoadingMode.fromInteger(nbtTagCompound.hasKey("mode") ? nbtTagCompound.getShort("mode") : 0);
            TileWorldAnchor te = (TileWorldAnchor)worldIn.getTileEntity(x, y, z);
            te.setField(FieldType.CHUNKLOADINGTIME, chunkLoadingTime);
            te.setField(FieldType.MODE, mode);
         }
      }

   }

   public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
      if (!worldIn.isRemote) {
         System.out.println("[Anchors] Broken (" + x + ", " + y + ", " + z + ") world anchor");

         try {
            Logger.log("Broken (" + x + ", " + y + ", " + z + ") world anchor");
            Logger.Active.remove(x, y, z);
         } catch (IOException var19) {
            var19.printStackTrace();
         }

         TileWorldAnchor te = (TileWorldAnchor)worldIn.getTileEntity(x, y, z);
         Random rand = new Random();
         float randX = rand.nextFloat() * 0.8F + 0.1F;
         float randY = rand.nextFloat() * 0.8F + 0.1F;
         float randZ = rand.nextFloat() * 0.8F + 0.1F;
         ItemStack item = new ItemStack(Item.getItemFromBlock(this), 1, 0);
         EntityItem entityItem = new EntityItem(worldIn, (double)((float)x + randX), (double)((float)y + randY), (double)((float)z + randZ), item.copy());
         int time = (Integer)te.getField(FieldType.CHUNKLOADINGTIME);
         LoadingMode mode = (LoadingMode)te.getField(FieldType.MODE);
         ItemStack itemStackInFirstSlot = (ItemStack)te.getField(FieldType.FIRSTSLOT);
         if (time != 0) {
            NBTTagCompound nbtTagCompound = entityItem.getEntityItem().getTagCompound();
            if (nbtTagCompound == null) {
               nbtTagCompound = new NBTTagCompound();
            }

            nbtTagCompound.setInteger("chunkLoadingTime", time);
            nbtTagCompound.setShort("mode", (short)mode.ordinal());
            entityItem.getEntityItem().setTagCompound(nbtTagCompound);
         }

         float factor = 0.05F;
         entityItem.motionX = rand.nextGaussian() * (double)factor;
         entityItem.motionY = 0.0D;
         entityItem.motionZ = rand.nextGaussian() * (double)factor;
         if (itemStackInFirstSlot != null) {
            EntityItem entityFuelItem = new EntityItem(worldIn, (double)((float)x + randX), (double)((float)y + randY), (double)((float)z + randZ), itemStackInFirstSlot);
            entityFuelItem.motionX = entityItem.motionX;
            entityFuelItem.motionY = 0.0D;
            entityFuelItem.motionZ = entityItem.motionZ;
            worldIn.spawnEntityInWorld(entityFuelItem);
         }

         worldIn.spawnEntityInWorld(entityItem);
      }

      super.breakBlock(worldIn, x, y, z, blockBroken, meta);
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World worldIn, int x, int y, int z) {
      return Item.getItemFromBlock(this);
   }
}
