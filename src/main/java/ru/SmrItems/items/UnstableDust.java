package ru.SmrItems.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import ru.SmrItems.SmMain;

public class UnstableDust extends Item {
   public UnstableDust(String name, String texture, int maxStackSize) {
      this.setUnlocalizedName(name);
      this.setTextureName("SmrItems:" + texture);
      this.setCreativeTab(SmMain.tabsmritems);
      this.maxStackSize = maxStackSize;
      GameRegistry.registerItem(this, name);
   }
}
