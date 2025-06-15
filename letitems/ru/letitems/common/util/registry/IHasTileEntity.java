package ru.letitems.common.util.registry;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<T extends TileEntity> {
   Class<T> getTileEntityClass();
}
