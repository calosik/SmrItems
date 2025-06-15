package ru.SmrItems.util.registry;

import net.minecraft.tileentity.TileEntity;

public interface IHasNamedTileEntity<T extends TileEntity> extends ru.SmrItems.util.registry.IHasTileEntity<T> {
   String getTileEntityName();
}
