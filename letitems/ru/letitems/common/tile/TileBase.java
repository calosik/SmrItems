package ru.letitems.common.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileBase extends TileEntity {
   private final boolean needSync;
   private boolean noMarkDirty;

   public TileBase(boolean needSync) {
      this.needSync = needSync;
   }

   public void onBlockClicked(EntityPlayer player) {
   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      return !player.isSneaking() && this.openGui(player);
   }

   public boolean openGui(EntityPlayer player) {
      return false;
   }

   public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
   }

   public ArrayList<ItemStack> getDrops(Block block, int meta, int fortune, boolean onBreak) {
      ArrayList<ItemStack> drops = new ArrayList(1);
      drops.add(new ItemStack(block, 1, meta));
      return drops;
   }

   public void onBlockRemoval(Block block, int meta) {
   }

   public final Packet getDescriptionPacket() {
      return null;
   }

   public final void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
      if (this.needSync && packet.func_148853_f() == 0) {
         NBTTagCompound nbt = packet.func_148857_g();
         this.readCustomFromNBT(nbt);
         this.onDataPacket(nbt);
      }

   }

   protected void onDataPacket(NBTTagCompound nbt) {
   }

   public final void updateEntity() {
      super.updateEntity();
      if (this.worldObj.isRemote) {
         this.updateEntityClient();
      }

   }

   @SideOnly(Side.CLIENT)
   public void updateEntityClient() {
   }

   public void updateEntityServer() {
   }

   public final void writeToNBT(NBTTagCompound nbt) {
      super.writeToNBT(nbt);
      this.writeCustomToNBT(nbt);
   }

   public final void readFromNBT(NBTTagCompound nbt) {
      this.noMarkDirty = true;

      try {
         super.readFromNBT(nbt);
         this.readCustomFromNBT(nbt);
      } finally {
         this.noMarkDirty = false;
      }

   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
   }

   public void markDirty() {
      if (!this.noMarkDirty) {
         super.markDirty();
         if (this.needSync) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      }

   }
}
