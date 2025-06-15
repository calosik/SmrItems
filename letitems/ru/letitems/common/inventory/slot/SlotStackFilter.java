package ru.letitems.common.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.letitems.common.util.IStackFilter;

public final class SlotStackFilter extends Slot {
   private final IStackFilter filter;

   public SlotStackFilter(IStackFilter filter, IInventory inventory, int slotIndex, int x, int y) {
      super(inventory, slotIndex, x, y);
      this.filter = filter;
   }

   public boolean isItemValid(ItemStack stack) {
      return this.filter.matches(stack);
   }
}
