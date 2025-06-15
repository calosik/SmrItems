package ru.letitems.common.items;

import net.minecraft.item.Item;
import ru.letitems.common.LetItems;
import ru.letitems.common.util.registry.IHasName;

public class ItemBase extends Item implements IHasName {
   private final String name;

   public ItemBase(String name) {
      this.name = name;
      this.setUnlocalizedName(name);
      this.setTextureName("letitems:" + name);
      this.setCreativeTab(LetItems.tabLetItems);
   }

   public String getName() {
      return this.name;
   }
}
