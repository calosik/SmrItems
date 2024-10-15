package ru.SmrItems.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.MathHelper;
import ru.SmrItems.common.blocks.BlockDoll;
import ru.SmrItems.common.utils.BlockPos;

public final class TileEntityDoll extends TileBase {
   public int direction;
   public int waifuGob;
   private int ticks = 45;
   private BlockPos blockPos = null;

   public TileEntityDoll() {
      super(true);
   }
   @Override
   public S35PacketUpdateTileEntity getDescriptionPacket() {
      NBTTagCompound nbtTagCompound = new NBTTagCompound();
      this.writeToNBT(nbtTagCompound);
      return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
   }
   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      this.readFromNBT(pkt.func_148857_g());
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public BlockDoll.DollType getType() {
      Block block = this.getBlockType();
      return block instanceof BlockDoll ? BlockDoll.DollType.getTypeFromIndex(((BlockDoll) block).startTypeIndex + this.getBlockMetadata()) : BlockDoll.DollType.getTypeFromIndex(0);
   }

   @Override
   public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
      super.onBlockPlacedBy(placer, stack);
      if (placer != null) {
         this.direction = MathHelper.floor_float(placer.rotationYaw * 4.0F / 360.0F + 0.5F) & 3;
         this.markDirty();
         if (this.worldObj != null) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      }
   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      BlockDoll.DollType type = this.getType();
      if (type.isGui()) {
         // Здесь можно добавить логику для открытия GUI
      }

      return true;
   }

   public boolean canUpdate() {
      return false;
   }

   public void updateEntityServer() {
      super.updateEntityServer();
      if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) != this.getBlockType()) {
         this.invalidate();
      } else {
         if (this.ticks++ > 20) {
            this.ticks = 0;
            this.markDirty();
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setInteger("direction", this.direction);
   }

   @Override
   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.direction = compound.getInteger("direction");
      if (this.worldObj != null) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
   }
}
