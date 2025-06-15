package ru.letitems.common.lib.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ru.letitems.common.registry.RegItems;

public class PrimaryTab extends CreativeTabs {
   public PrimaryTab() {
      super("letitems");
   }

   @SideOnly(Side.CLIENT)
   public Item getTabIconItem() {
      return RegItems.ItemArtefe;
   }
}
