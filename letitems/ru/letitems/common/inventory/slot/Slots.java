package ru.letitems.common.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class Slots extends Slot {
   private Item item;

   public Slots(IInventory inventory, int slotIndex, Item item, int x, int y) {
      super(inventory, slotIndex, x, y);
      this.item = item;
   }

   public int getSlotStackLimit() {
      return 1;
   }

   public boolean isItemValid(ItemStack stack) {
      return stack != null && stack.stackSize > 0 && stack.getItem().equals(this.item);
   }
}
