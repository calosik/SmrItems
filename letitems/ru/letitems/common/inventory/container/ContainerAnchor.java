package ru.letitems.common.inventory.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.letitems.common.inventory.slot.SlotStackFilter;
import ru.letitems.common.tile.TileAnchor;
import ru.letitems.common.util.IStackFilter;

public final class ContainerAnchor extends ContainerTileBase<TileAnchor> {
   public short minutesRemaining;
   private short prevMinutesRemaining;

   public ContainerAnchor(InventoryPlayer inventoryPlayer, TileAnchor tile) {
      super(tile);
      this.addSlot(new SlotStackFilter(new IStackFilter() {
         public boolean matches(ItemStack stack) {
            return ((TileAnchor)ContainerAnchor.this.tile).getAnchorType().getFuelForStackInHours(stack) > 0.0F;
         }
      }, tile, 0, 25, 36));

      int column;
      for(column = 0; column < 3; ++column) {
         for(int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(inventoryPlayer, column + column * 9 + 9, 20 + column * 18, 67 + column * 18));
         }
      }

      for(column = 0; column < 9; ++column) {
         this.addSlot(new Slot(inventoryPlayer, column, 20 + column * 18, 125));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
      Slot slot = this.getSlot(slotNumber);
      if (slot != null && slot.getHasStack()) {
         ItemStack stack = slot.getStack();
         ItemStack prevStack = stack.copy();
         if (slotNumber == 0) {
            if (!this.mergeItemStack(stack, 1, 37, true)) {
               return null;
            }
         } else if (!this.mergeItemStack(stack, 0, 1, false)) {
            return null;
         }

         if (stack.stackSize <= 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }

         if (stack.stackSize == prevStack.stackSize) {
            return null;
         } else {
            slot.onPickupFromSlot(player, stack);
            return prevStack;
         }
      } else {
         return null;
      }
   }

   public void addCraftingToCrafters(ICrafting crafter) {
      super.addCraftingToCrafters(crafter);
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();
   }

   @SideOnly(Side.CLIENT)
   public void updateProgressBar(int id, int value) {
      if (id == 0) {
         this.minutesRemaining = (short)value;
      }

   }

   private static short getMinutesRemaining(int fuel) {
      return 0;
   }
}
