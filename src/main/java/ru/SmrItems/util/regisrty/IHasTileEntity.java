package ru.SmrItems.util.regisrty;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<T extends TileEntity> {
   Class<T> getTileEntityClass();
}
