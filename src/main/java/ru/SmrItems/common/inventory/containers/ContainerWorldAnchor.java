package ru.SmrItems.common.inventory.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.SmrItems.common.tileentity.TileWorldAnchor;

public class ContainerWorldAnchor extends Container {
   private TileWorldAnchor te;

   public ContainerWorldAnchor(TileWorldAnchor te, EntityPlayer player) {
      this.te = te;
      this.addSlotToContainer(new Slot(te, 0, 25, 36));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 20 + j * 18, 67 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(player.inventory, i, 20 + i * 18, 125));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer player, int index) {
      ItemStack itemStack = null;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemStackInSlot = slot.getStack();
         itemStack = itemStackInSlot.copy();
         if (index < 27) {
            if (!this.mergeItemStack(itemStackInSlot, 27, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemStackInSlot, 0, 27, false)) {
            return null;
         }

         if (itemStackInSlot.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemStack;
   }

   public boolean canInteractWith(EntityPlayer player) {
      return this.te.isUseableByPlayer(player);
   }
}
