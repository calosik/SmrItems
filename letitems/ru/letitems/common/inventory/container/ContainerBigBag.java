package ru.letitems.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.letitems.common.inventory.InventoryBag;
import ru.letitems.common.inventory.slot.SlotLocked;

public final class ContainerBigBag extends Container {
   private static final int COLUMNS = 12;
   public static final int SIZE_X = 238;
   public static final int SIZE_Y = 256;
   public final InventoryBag inventory;
   private final int numRows;

   public ContainerBigBag(InventoryPlayer playerInv, ItemStack stack) {
      this.inventory = new InventoryBag(playerInv.player, stack);
      this.numRows = this.inventory.getSizeInventory() / 12;
      this.inventory.openInventory();

      int col;
      for(int row = 0; row < this.numRows; ++row) {
         for(col = 0; col < 12; ++col) {
            this.addSlotToContainer(new Slot(this.inventory, col + row * 12, 12 + col * 18, 8 + row * 18));
         }
      }

      int leftCol = true;

      for(col = 0; col < 3; ++col) {
         for(int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInv, col + col * 9 + 9, 39 + col * 18, 256 - (4 - col) * 18 - 10));
         }
      }

      for(col = 0; col < 9; ++col) {
         ItemStack stackInSlot = playerInv.getStackInSlot(col);
         int x = 39 + col * 18;
         int y = true;
         if (this.inventory.isParentItemInventory(stackInSlot)) {
            this.addSlotToContainer(new SlotLocked(playerInv, col, x, 232));
         } else {
            this.addSlotToContainer(new Slot(playerInv, col, x, 232));
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
            if (slotNumber < this.numRows * 12) {
               if (!this.mergeItemStack(stack, this.numRows * 12, this.inventorySlots.size(), true)) {
                  return null;
               }
            } else if (!this.mergeItemStack(stack, 0, this.numRows * 12, false)) {
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
