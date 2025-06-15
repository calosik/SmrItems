package ru.letitems.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.letitems.common.inventory.InventoryBag;
import ru.letitems.common.inventory.slot.SlotLocked;

public final class ContainerBag extends Container {
   private static final int COLUMNS = 9;
   public final InventoryBag inventory;
   private final int numRows;

   public ContainerBag(InventoryPlayer playerInv, ItemStack stack) {
      this.inventory = new InventoryBag(playerInv.player, stack);
      this.numRows = this.inventory.getSizeInventory() / 9;
      this.inventory.openInventory();
      int i = (this.numRows - 4) * 18;

      int col;
      int col;
      for(col = 0; col < this.numRows; ++col) {
         for(col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(this.inventory, col + col * 9, 8 + col * 18, 18 + col * 18));
         }
      }

      for(col = 0; col < 3; ++col) {
         for(col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInv, col + col * 9 + 9, 8 + col * 18, 103 + col * 18 + i));
         }
      }

      for(col = 0; col < 9; ++col) {
         ItemStack stackInSlot = playerInv.getStackInSlot(col);
         int x = 8 + col * 18;
         int y = 161 + i;
         if (this.inventory.isParentItemInventory(stackInSlot)) {
            this.addSlotToContainer(new SlotLocked(playerInv, col, x, y));
         } else {
            this.addSlotToContainer(new Slot(playerInv, col, x, y));
         }
      }

   }

   public boolean canInteractWith(EntityPlayer player) {
      return this.inventory.isUseableByPlayer(player);
   }

   public ItemStack slotClick(int slotIndex, int button, int modifier, EntityPlayer player) {
      if (!this.canInteractWith(player)) {
         return null;
      } else {
         if (modifier == 2 && button >= 0 && button < 9) {
            int hotbarSlotIndex = 27 + button;
            Slot hotbarSlot = this.getSlot(hotbarSlotIndex);
            if (hotbarSlot instanceof SlotLocked) {
               return null;
            }
         }

         return super.slotClick(slotIndex, button, modifier, player);
      }
   }

   public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
      if (!this.canInteractWith(player)) {
         return null;
      } else {
         ItemStack result = null;
         Slot slot = this.getSlot(slotNumber);
         if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            result = stack.copy();
            if (slotNumber < this.numRows * 9) {
               if (!this.mergeItemStack(stack, this.numRows * 9, this.inventorySlots.size(), true)) {
                  return null;
               }
            } else if (!this.mergeItemStack(stack, 0, this.numRows * 9, false)) {
               return null;
            }

            if (stack.stackSize == 0) {
               slot.putStack((ItemStack)null);
            } else {
               slot.onSlotChanged();
            }
         }

         return result;
      }
   }

   public void onContainerClosed(EntityPlayer player) {
      super.onContainerClosed(player);
      this.inventory.closeInventory();
   }
}
