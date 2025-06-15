package ru.letitems.common.lib.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ru.letitems.common.registry.RegItems;

public class DakimakuraTab extends CreativeTabs {
   public DakimakuraTab() {
      super("letitems.daki");
   }

   @SideOnly(Side.CLIENT)
   public Item getTabIconItem() {
      return Item.getItemFromBlock(RegItems.blockDakimakura);
   }
}
