package ru.SmrItems.util.registry;

import net.minecraft.item.ItemBlock;

public interface IHasItemBlock {
   Class<? extends ItemBlock> getItemBlockClass();
}
