package ru.letitems.common.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class MergedInventory implements IInventory {
   private final List<MergedInventory.SubInventory> inventories = new ArrayList();
   private int inventorySize;
   private int inventoryStackLimit = 64;

   public void addInventory(IInventory inventory) {
      if (inventory != null) {
         this.inventories.add(new MergedInventory.SubInventory(inventory, this.inventories.size(), this.inventorySize));
         this.inventorySize += inventory.getSizeInventory();
         this.inventoryStackLimit = Math.min(this.inventoryStackLimit, inventory.getInventoryStackLimit());
      }

   }

   public List<MergedInventory.SubInventory> getInventories() {
      return Collections.unmodifiableList(this.inventories);
   }

   private MergedInventory.SubInventory getInventoryBySlot(int slot) {
      if (slot >= 0) {
         Iterator var2 = this.inventories.iterator();

         while(var2.hasNext()) {
            MergedInventory.SubInventory inventory = (MergedInventory.SubInventory)var2.next();
            if (inventory.firstSlot <= slot && slot < inventory.firstSlot + inventory.inventory.getSizeInventory()) {
               return inventory;
            }
         }
      }

      return null;
   }

   public MergedInventory.ItemInfo findItem(Item item, int meta) {
      if (item != null) {
         if (meta < 0) {
            meta = 32767;
         }

         Iterator var3 = this.inventories.iterator();

         while(var3.hasNext()) {
            MergedInventory.SubInventory inventory = (MergedInventory.SubInventory)var3.next();
            int subSize = inventory.inventory.getSizeInventory();

            for(int subSlot = 0; subSlot < subSize; ++subSlot) {
               ItemStack stack = inventory.inventory.getStackInSlot(subSlot);
               if (stack != null && stack.stackSize > 0 && stack.getItem() == item && (meta == 32767 || meta == stack.getItemDamage())) {
                  return new MergedInventory.ItemInfo(inventory, subSlot, inventory.firstSlot + subSlot);
               }
            }
         }
      }

      return null;
   }

   public int getSizeInventory() {
      return this.inventorySize;
   }

   public ItemStack getStackInSlot(int slot) {
      MergedInventory.SubInventory inventory = this.getInventoryBySlot(slot);
      return inventory == null ? null : inventory.inventory.getStackInSlot(slot - inventory.firstSlot);
   }

   public ItemStack decrStackSize(int slot, int amount) {
      MergedInventory.SubInventory inventory = this.getInventoryBySlot(slot);
      return inventory == null ? null : inventory.inventory.decrStackSize(slot - inventory.firstSlot, amount);
   }

   public ItemStack getStackInSlotOnClosing(int slot) {
      MergedInventory.SubInventory inventory = this.getInventoryBySlot(slot);
      return inventory == null ? null : inventory.inventory.getStackInSlotOnClosing(slot - inventory.firstSlot);
   }

   public void setInventorySlotContents(int slot, ItemStack stack) {
      MergedInventory.SubInventory inventory = this.getInventoryBySlot(slot);
      if (inventory != null) {
         inventory.inventory.setInventorySlotContents(slot - inventory.firstSlot, stack);
      }

   }

   public String getInventoryName() {
      return null;
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public int getInventoryStackLimit() {
      return this.inventoryStackLimit;
   }

   public void markDirty() {
      Iterator var1 = this.inventories.iterator();

      while(var1.hasNext()) {
         MergedInventory.SubInventory inventory = (MergedInventory.SubInventory)var1.next();
         inventory.inventory.markDirty();
      }

   }

   public boolean isUseableByPlayer(EntityPlayer player) {
      Iterator var2 = this.inventories.iterator();

      MergedInventory.SubInventory inventory;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         inventory = (MergedInventory.SubInventory)var2.next();
      } while(inventory.inventory.isUseableByPlayer(player));

      return false;
   }

   public void openInventory() {
      Iterator var1 = this.inventories.iterator();

      while(var1.hasNext()) {
         MergedInventory.SubInventory inventory = (MergedInventory.SubInventory)var1.next();
         inventory.inventory.openInventory();
      }

   }

   public void closeInventory() {
      Iterator var1 = this.inventories.iterator();

      while(var1.hasNext()) {
         MergedInventory.SubInventory inventory = (MergedInventory.SubInventory)var1.next();
         inventory.inventory.closeInventory();
      }

   }

   public boolean isItemValidForSlot(int slot, ItemStack stack) {
      MergedInventory.SubInventory inventory = this.getInventoryBySlot(slot);
      return inventory != null && inventory.inventory.isItemValidForSlot(slot - inventory.firstSlot, stack);
   }

   public static final class ItemInfo {
      public final MergedInventory.SubInventory subInventory;
      public final int subSlot;
      public final int slot;

      public ItemInfo(MergedInventory.SubInventory subInventory, int subSlot, int slot) {
         this.subInventory = subInventory;
         this.subSlot = subSlot;
         this.slot = slot;
      }

      public ItemStack getItemStack() {
         return this.subInventory.inventory.getStackInSlot(this.subSlot);
      }

      public void setItemStack(ItemStack stack) {
         this.subInventory.inventory.setInventorySlotContents(this.subSlot, stack);
      }
   }

   public static final class SubInventory {
      public final IInventory inventory;
      public final int index;
      public final int firstSlot;

      public SubInventory(IInventory inventory, int index, int firstSlot) {
         this.inventory = inventory;
         this.index = index;
         this.firstSlot = firstSlot;
      }
   }
}
