package ru.SmrItems.util.regisrty;

import net.minecraft.item.ItemBlock;

public interface IHasItemBlock {
   Class<? extends ItemBlock> getItemBlockClass();
}
