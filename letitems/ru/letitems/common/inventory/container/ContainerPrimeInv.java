package ru.letitems.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.letitems.common.inventory.ExtendedPlayer;

public final class ContainerPrimeInv extends Container {
   public ContainerPrimeInv(EntityPlayer player) {
      ExtendedPlayer extendedPlayer = ExtendedPlayer.getExtendedPlayer(player);

      for(int row = 0; row < 4; ++row) {
         for(int col = 0; col < 11; ++col) {
            this.addSlotToContainer(new Slot(extendedPlayer, 4 + col + row * 11, 57 + col * 19, 73 + row * 19));
         }
      }

      ContainerLetHelper.addPlayerSlots(player.inventory, this, 76, 163);
   }

   public boolean canInteractWith(EntityPlayer player) {
      return true;
   }

   public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
      ItemStack stack = null;
      Slot slot = this.getSlot(slotNumber);
      if (slot != null && slot.getHasStack()) {
         ItemStack slotStack = slot.getStack();
         stack = slotStack.copy();
         int numRows = true;
         if (slotNumber < 44) {
            if (!this.mergeItemStack(slotStack, 44, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(slotStack, 0, 44, false)) {
            return null;
         }

         if (slotStack.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }
      }

      return stack;
   }
}
