package ru.letitems.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class InventoryWrapper implements IInventory {
   private final IInventory inventory;
   private final int size;

   public InventoryWrapper(IInventory inventory, int size) {
      this.inventory = inventory;
      this.size = Math.min(size, inventory.getSizeInventory());
   }

   public int getSizeInventory() {
      return this.size;
   }

   public ItemStack getStackInSlot(int slot) {
      return slot < this.getSizeInventory() ? this.inventory.getStackInSlot(slot) : null;
   }

   public ItemStack decrStackSize(int slot, int amount) {
      return slot < this.getSizeInventory() ? this.inventory.decrStackSize(slot, amount) : null;
   }

   public ItemStack getStackInSlotOnClosing(int slot) {
      return slot < this.getSizeInventory() ? this.inventory.getStackInSlotOnClosing(slot) : null;
   }

   public void setInventorySlotContents(int slot, ItemStack stack) {
      if (slot < this.getSizeInventory()) {
         this.inventory.setInventorySlotContents(slot, stack);
      }

   }

   public String getInventoryName() {
      return this.inventory.getInventoryName();
   }

   public boolean hasCustomInventoryName() {
      return this.inventory.hasCustomInventoryName();
   }

   public int getInventoryStackLimit() {
      return this.inventory.getInventoryStackLimit();
   }

   public void markDirty() {
      this.inventory.markDirty();
   }

   public boolean isUseableByPlayer(EntityPlayer player) {
      return this.inventory.isUseableByPlayer(player);
   }

   public void openInventory() {
      this.inventory.openInventory();
   }

   public void closeInventory() {
      this.inventory.closeInventory();
   }

   public boolean isItemValidForSlot(int slot, ItemStack stack) {
      return slot < this.getSizeInventory() && this.inventory.isItemValidForSlot(slot, stack);
   }
}
