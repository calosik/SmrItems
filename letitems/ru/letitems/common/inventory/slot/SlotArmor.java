package ru.letitems.common.inventory.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public final class SlotArmor extends Slot {
   private final EntityPlayer player;
   private final int armorNumber;

   public SlotArmor(IInventory inventory, int slotIndex, int x, int y, EntityPlayer player, int armorNumber) {
      super(inventory, slotIndex, x, y);
      this.player = player;
      this.armorNumber = armorNumber;
   }

   public int getSlotStackLimit() {
      return 1;
   }

   public boolean isItemValid(ItemStack stack) {
      return stack != null && stack.stackSize > 0 && stack.getItem().isValidArmor(stack, this.armorNumber, this.player);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getBackgroundIconIndex() {
      return ItemArmor.func_94602_b(this.armorNumber);
   }
}
