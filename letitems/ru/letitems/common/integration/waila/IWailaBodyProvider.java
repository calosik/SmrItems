package ru.letitems.common.integration.waila;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Interface(
   modid = "Waila",
   iface = "mcp.mobius.waila.api.IWailaDataProvider"
)
public interface IWailaBodyProvider extends IWailaDataProvider {
   @Method(
      modid = "Waila"
   )
   default ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return accessor.getStack();
   }

   @Method(
      modid = "Waila"
   )
   default List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   @Method(
      modid = "Waila"
   )
   default List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   @Method(
      modid = "Waila"
   )
   default NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
      if (te != null) {
         te.writeToNBT(tag);
      }

      return tag;
   }
}
