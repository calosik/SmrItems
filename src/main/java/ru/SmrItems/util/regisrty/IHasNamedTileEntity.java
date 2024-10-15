package ru.SmrItems.util.regisrty;

import net.minecraft.tileentity.TileEntity;

public interface IHasNamedTileEntity<T extends TileEntity> extends IHasTileEntity<T> {
   String getTileEntityName();
}
