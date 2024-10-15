package ru.SmrItems.common.handlers;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.SmrItems.client.gui.GuiWorldAnchor;
import ru.SmrItems.common.inventory.containers.ContainerWorldAnchor;
import ru.SmrItems.common.tileentity.TileWorldAnchor;

public class GuiHandler implements IGuiHandler {
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      TileEntity te = world.getTileEntity(x, y, z);
      return te != null && ID == 0 ? new ContainerWorldAnchor((TileWorldAnchor)te, player) : null;
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      TileEntity te = world.getTileEntity(x, y, z);
      return te != null && ID == 0 ? new GuiWorldAnchor((TileWorldAnchor)te, player) : null;
   }
}
