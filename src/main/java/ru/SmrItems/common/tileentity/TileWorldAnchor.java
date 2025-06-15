package ru.SmrItems.common.tileentity;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import ru.SmrItems.Config;
import ru.SmrItems.SmMain;
import ru.SmrItems.items.FuelUnstable;
import ru.SmrItems.util.AnchorsChunkManager;
import ru.SmrItems.util.BlockPos;
import ru.SmrItems.util.enums.FieldType;
import ru.SmrItems.util.enums.LoadingMode;

public class TileWorldAnchor extends TileEntity implements IInventory, ISidedInventory {
   private ItemStack itemsInFirstSlot;
   private int chunkLoadingTime;
   private Ticket ticket;
   private boolean isPaused = false;
   private LoadingMode mode;

   public TileWorldAnchor() {
      this.mode = LoadingMode.NORMAL;
   }

   public void setField(FieldType type, Object value) {
      if (type == FieldType.FIRSTSLOT) {
         if (value instanceof ItemStack) {
            this.itemsInFirstSlot = (ItemStack)value;
         }
      } else if (type == FieldType.CHUNKLOADINGTIME) {
         if (value instanceof Integer) {
            this.chunkLoadingTime = (Integer)value;
         }
      } else if (type == FieldType.ISPAUSED) {
         if (value instanceof Boolean) {
            this.isPaused = (Boolean)value;
         }
      } else if (type == FieldType.MODE && value instanceof LoadingMode) {
         this.mode = (LoadingMode)value;
      }

   }

   public Object getField(FieldType type) {
      if (type == FieldType.FIRSTSLOT) {
         return this.itemsInFirstSlot;
      } else if (type == FieldType.CHUNKLOADINGTIME) {
         return this.chunkLoadingTime;
      } else if (type == FieldType.ISPAUSED) {
         return this.isPaused;
      } else {
         return type == FieldType.MODE ? this.mode : null;
      }
   }

   public void forceChunkLoadingCallback(Ticket ticket) {
      if (Config.enabledChunkLoader) {
         if (this.ticket == null) {
            this.ticket = ticket;
         }

         Iterator var2 = AnchorsChunkManager.getLoadArea(this.mode, this.xCoord, this.zCoord).iterator();

         while(var2.hasNext()) {
            ChunkCoordIntPair coord = (ChunkCoordIntPair)var2.next();
            ForgeChunkManager.forceChunk(this.ticket, coord);
         }
      }

   }

   public void forceChunkLoading() {
      if (Config.enabledChunkLoader) {
         if (this.ticket == null) {
            this.ticket = ForgeChunkManager.requestTicket(SmMain.INSTANCE, this.worldObj, Type.NORMAL);
         }

         if (this.ticket == null) {
            System.out.println("[Anchors] Ticket could not be reserved [" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + "]");
         } else {
            this.ticket.getModData().setInteger("coreX", this.xCoord);
            this.ticket.getModData().setInteger("coreY", this.yCoord);
            this.ticket.getModData().setInteger("coreZ", this.zCoord);
            Iterator var1 = AnchorsChunkManager.getLoadArea(this.mode, this.xCoord, this.zCoord).iterator();

            while(var1.hasNext()) {
               ChunkCoordIntPair coord = (ChunkCoordIntPair)var1.next();
               ForgeChunkManager.forceChunk(this.ticket, coord);
            }
         }
      }

   }

   public void releaseTicket() {
      if (this.ticket != null) {
         ForgeChunkManager.releaseTicket(this.ticket);
         this.ticket = null;
      }

   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      NBTTagList items = new NBTTagList();
      if (this.itemsInFirstSlot != null) {
         NBTTagCompound tagCompound = new NBTTagCompound();
         tagCompound.setByte("Slot", (byte)0);
         this.itemsInFirstSlot.writeToNBT(tagCompound);
         items.appendTag(tagCompound);
      }

      compound.setInteger("chunkLoadingTime", this.chunkLoadingTime);
      compound.setShort("mode", (short)this.mode.ordinal());
      compound.setBoolean("isPaused", this.isPaused);
      compound.setTag("Items", items);
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.chunkLoadingTime = compound.getInteger("chunkLoadingTime");
      this.mode = LoadingMode.fromInteger(compound.getShort("mode"));
      this.isPaused = compound.getBoolean("isPaused");
      NBTTagList items = compound.getTagList("Items", 10);
      NBTTagCompound nbtTagCompound = items.getCompoundTagAt(0);
      this.itemsInFirstSlot = ItemStack.loadItemStackFromNBT(nbtTagCompound);
   }

   public void updateEntity() {
      if (this.chunkLoadingTime > 0 && !this.isPaused) {
         --this.chunkLoadingTime;
      }

      if (this.itemsInFirstSlot != null && this.itemsInFirstSlot.getItem() instanceof FuelUnstable && this.chunkLoadingTime == 0) {
         if (this.itemsInFirstSlot.stackSize > 1) {
            --this.itemsInFirstSlot.stackSize;
         } else {
            this.itemsInFirstSlot = null;
         }

         if (this.mode == LoadingMode.SMALL) {
            this.chunkLoadingTime = 20 * Config.fuelTime * Config.multiplier;
         } else if (this.mode == LoadingMode.NORMAL) {
            this.chunkLoadingTime = 20 * Config.fuelTime;
         } else {
            this.chunkLoadingTime = 20 * Config.fuelTime / Config.multiplier;
         }

         if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            if (!this.isPaused) {
               this.forceChunkLoading();
            }
         }
      }

      if (!this.worldObj.isRemote && this.chunkLoadingTime == 0) {
         this.releaseTicket();
      }

      this.markDirty();
   }

   public void invalidate() {
      super.invalidate();
      if (this.worldObj.isRemote) {
         AnchorsChunkManager.anchorsList.removeIf((obj) -> {
            return obj.getPosX() == this.xCoord && obj.getPosY() == this.yCoord && obj.getPosZ() == this.zCoord;
         });
      }

      if (!this.worldObj.isRemote) {
         this.releaseTicket();
      }

   }

   public void validate() {
      super.validate();
      if (this.worldObj.isRemote) {
         AnchorsChunkManager.anchorsList.add(new BlockPos(this.xCoord, this.yCoord, this.zCoord));
      }

   }

   public Packet getDescriptionPacket() {
      NBTTagCompound tagCompound = new NBTTagCompound();
      this.writeToNBT(tagCompound);
      return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      this.readFromNBT(pkt.func_148857_g());
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int slotIn) {
      return this.itemsInFirstSlot;
   }

   public ItemStack decrStackSize(int index, int count) {
      if (this.itemsInFirstSlot != null) {
         ItemStack itemStack;
         if (this.itemsInFirstSlot.stackSize == count) {
            itemStack = this.itemsInFirstSlot;
            this.itemsInFirstSlot = null;
         } else {
            itemStack = this.itemsInFirstSlot.splitStack(count);
            if (this.itemsInFirstSlot.stackSize == 0) {
               this.itemsInFirstSlot = null;
            }
         }

         this.markDirty();
         return itemStack;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int index) {
      if (this.itemsInFirstSlot != null) {
         ItemStack itemStack = this.itemsInFirstSlot;
         this.itemsInFirstSlot = null;
         return itemStack;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      this.itemsInFirstSlot = stack;
      if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
         stack.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public String getInventoryName() {
      return "inventory.worldAnchor.name";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer player) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return stack.getItem().getUnlocalizedName().equals(Config.fuelItem);
   }

   public int[] getAccessibleSlotsFromSide(int slot) {
      return new int[slot];
   }

   public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
      return this.isItemValidForSlot(slot, itemStack);
   }

   public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
      return false;
   }
}
