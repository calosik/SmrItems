package ru.letitems.common.util.registry;

import net.minecraft.tileentity.TileEntity;

public interface IHasNamedTileEntity<T extends TileEntity> extends IHasTileEntity<T> {
   String getTileEntityName();
}
