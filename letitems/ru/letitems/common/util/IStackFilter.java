package ru.letitems.common.util;

import net.minecraft.item.ItemStack;

public interface IStackFilter {
   boolean matches(ItemStack var1);
}
