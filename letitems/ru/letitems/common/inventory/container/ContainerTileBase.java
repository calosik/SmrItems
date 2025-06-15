package ru.letitems.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.letitems.common.tile.TileBase;

public abstract class ContainerTileBase<T extends TileBase & IInventory> extends Container {
   public final T tile;

   public ContainerTileBase(T tile) {
      this.tile = tile;
   }

   public boolean canInteractWith(EntityPlayer player) {
      return ((IInventory)this.tile).isUseableByPlayer(player);
   }

   public <S extends Slot> void addSlot(S slot) {
      this.addSlotToContainer(slot);
   }
}
