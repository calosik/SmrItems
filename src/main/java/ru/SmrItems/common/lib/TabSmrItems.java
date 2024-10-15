package ru.SmrItems.common.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ru.SmrItems.registry.RegItems;

public class TabSmrItems extends CreativeTabs {
   public TabSmrItems() {
      super("SmrItems");
   }

   public Item getTabIconItem() {
      return RegItems.unstabledrop;
   }
}
