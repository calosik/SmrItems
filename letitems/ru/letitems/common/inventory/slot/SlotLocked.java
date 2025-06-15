package ru.letitems.common.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public final class SlotLocked extends Slot {
   public SlotLocked(IInventory inventory, int slotIndex, int xPos, int yPos) {
      super(inventory, slotIndex, xPos, yPos);
   }

   public void putStack(ItemStack itemStack) {
   }

   public boolean canTakeStack(EntityPlayer stack) {
      return false;
   }

   public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
   }

   public boolean isItemValid(ItemStack par1ItemStack) {
      return false;
   }

   public boolean getHasStack() {
      return false;
   }

   public ItemStack decrStackSize(int i) {
      return null;
   }
}
