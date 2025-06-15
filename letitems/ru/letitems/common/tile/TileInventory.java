package ru.letitems.common.tile;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.letitems.common.util.InventoryUtils;

public abstract class TileInventory extends TileBase implements IInventory {
   private final int size;
   private final ItemStack[] stacks;
   private final String itemsTagName;
   private final String slotTagName;
   private boolean dropContent;

   public TileInventory(int size) {
      this(size, "Items", "Slot");
   }

   public TileInventory(int size, String itemsTagName, String slotTagName) {
      super(true);
      this.dropContent = true;
      this.size = size;
      this.stacks = new ItemStack[size];
      this.itemsTagName = itemsTagName;
      this.slotTagName = slotTagName;
   }

   public boolean isDropContent() {
      return this.dropContent;
   }

   public void setDropContent(boolean dropContent) {
      this.dropContent = dropContent;
   }

   public ArrayList<ItemStack> getDrops(Block block, int meta, int fortune, boolean onBreak) {
      ArrayList<ItemStack> drops = super.getDrops(block, meta, fortune, onBreak);
      if (this.isDropContent()) {
         ItemStack[] var6 = this.stacks;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ItemStack stack = var6[var8];
            if (stack != null && stack.stackSize > 0) {
               drops.add(stack);
            }
         }
      }

      return drops;
   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
      super.writeCustomToNBT(nbt);
      InventoryUtils.writeInvToNbt(nbt, this.itemsTagName, this.slotTagName, this);
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
      super.readCustomFromNBT(nbt);
      Arrays.fill(this.stacks, (Object)null);
      InventoryUtils.readInvFromNbt(nbt, this.itemsTagName, this.slotTagName, this);
   }

   public int getSizeInventory() {
      return this.size;
   }

   public ItemStack getStackInSlot(int slot) {
      return this.stacks[slot];
   }

   public ItemStack decrStackSize(int slot, int amount) {
      ItemStack stack = this.stacks[slot];
      if (stack != null) {
         if (stack.stackSize <= amount) {
            this.stacks[slot] = null;
         } else {
            stack = stack.splitStack(amount);
            if (this.stacks[slot].stackSize <= 0) {
               this.stacks[slot] = null;
            }
         }

         this.markDirty();
         return stack.stackSize > 0 ? stack : null;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int slot) {
      ItemStack stack = this.stacks[slot];
      if (stack != null) {
         this.stacks[slot] = null;
         this.markDirty();
         return stack.stackSize > 0 ? stack : null;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int slot, ItemStack stack) {
      if (stack != null) {
         if (stack.stackSize <= 0) {
            stack = null;
         } else {
            int stackLimit = this.getInventoryStackLimit();
            if (stack.stackSize > stackLimit) {
               stack.stackSize = stackLimit;
            }
         }
      }

      this.stacks[slot] = stack;
      this.markDirty();
   }

   public String getInventoryName() {
      return "";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer player) {
      return this.getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord, (double)this.yCoord, (double)this.zCoord) <= 64.0D;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int slot, ItemStack stack) {
      return true;
   }
}
