package ru.letitems.common.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerLetHelper {
   public static void addPlayerSlots(InventoryPlayer inventory, Container container, int x, int y) {
      addPlayerSlots(inventory, container, x, y, true, true);
   }

   public static void addPlayerSlots(InventoryPlayer inventory, Container container, int x, int y, boolean main, boolean hotbar) {
      int i;
      if (main) {
         for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
               container.addSlotToContainer(new Slot(inventory, j + (i + 1) * 9, x + j * 19, y + i * 19));
            }
         }
      }

      if (hotbar) {
         for(i = 0; i < 9; ++i) {
            container.addSlotToContainer(new Slot(inventory, i, x + i * 19, y + 59));
         }
      }

   }
}
