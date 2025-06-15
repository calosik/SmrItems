package ru.SmrItems.util.registry;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<T extends TileEntity> extends IHasName {
   Class<T> getTileEntityClass();
}
