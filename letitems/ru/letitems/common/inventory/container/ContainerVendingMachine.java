package ru.letitems.common.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.letitems.common.inventory.slot.SlotLocked;
import ru.letitems.common.tile.TileVendingMachine;

public class ContainerVendingMachine extends ContainerTileEntity<TileVendingMachine> {
   public ContainerVendingMachine(IInventory playerInv, TileVendingMachine machine) {
      super(playerInv, machine, 20, 146);
      this.addSlot(new Slot(machine, 9, 36, 20));
      this.addSlot(new Slot(machine, 10, 88, 20));
      this.addSlot(new SlotLocked(machine, 11, 25, 64));
   }
}
