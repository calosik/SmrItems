package ru.letitems.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.letitems.common.tile.TileBase;

public class ContainerTileEntity<T extends TileBase & IInventory> extends ContainerTileBase<T> {
   private int playerSlotsCount;

   public ContainerTileEntity(IInventory playerInv, T tile, int startX, int startY) {
      super(tile);

      int l;
      for(l = 0; l < 3; ++l) {
         for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInv, j1 + l * 9 + 9, startX + j1 * 18, startY + l * 18));
         }
      }

      for(l = 0; l < 9; ++l) {
         this.addSlot(new Slot(playerInv, l, startX + l * 18, startY + 142 - 84));
      }

      this.playerSlotsCount = this.inventorySlots.size();
   }

   public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
      ItemStack prevSlotStack = null;
      Slot slot = (Slot)this.inventorySlots.get(slotNumber);
      if (slot != null && slot.getHasStack()) {
         ItemStack slotStack = slot.getStack();
         prevSlotStack = slotStack.copy();
         if (slotNumber < this.playerSlotsCount) {
            if (!this.mergeItemStack(slotStack, this.playerSlotsCount, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(slotStack, 0, this.playerSlotsCount, false)) {
            return null;
         }

         if (slotStack.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }
      }

      return prevSlotStack;
   }
}
