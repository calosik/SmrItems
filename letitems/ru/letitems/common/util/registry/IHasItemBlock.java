package ru.letitems.common.util.registry;

import net.minecraft.item.ItemBlock;

public interface IHasItemBlock {
   Class<? extends ItemBlock> getItemBlockClass();
}
