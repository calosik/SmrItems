package ru.letitems.common.tile;

import cpw.mods.fml.common.Optional.Method;
import java.util.List;
import java.util.UUID;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import ru.letitems.client.ClientParams;
import ru.letitems.common.integration.waila.IWailaBodyProvider;
import ru.letitems.common.util.LockPlayer;

public final class TileEntityFlowersEvent extends TileBase implements IWailaBodyProvider {
   public static final int TIME_TU_GATES = 36000;
   private static LockPlayer lockPlayer = new LockPlayer();
   private long tick = 0L;

   public TileEntityFlowersEvent() {
      super(true);
   }

   public void updateEntityServer() {
   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      return false;
   }

   public boolean canUpdate() {
      return false;
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
      super.readCustomFromNBT(nbt);
      this.tick = nbt.getLong("tick");
   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
      super.writeCustomToNBT(nbt);
      nbt.setLong("tick", this.tick);
   }

   @Method(
      modid = "Waila"
   )
   public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      if (ClientParams.idKys == 3) {
         TileEntity tileEntity = accessor.getTileEntity();
         if (tileEntity instanceof TileEntityFlowersEvent) {
            TileEntityFlowersEvent tileEntityFlowersEvent = (TileEntityFlowersEvent)tileEntity;
            int i = Math.max(0, (int)(tileEntityFlowersEvent.tick - tileEntityFlowersEvent.worldObj.getTotalWorldTime()));
            if (i <= 0) {
               currenttip.add(String.format("%sМожно получить награды", EnumChatFormatting.YELLOW));
            } else {
               float sec = (float)(i / 20);
               String textTime = sec > 60.0F && sec < 3600.0F ? String.format("%s%s", Math.round(sec / 60.0F), " мин.") : String.format("%s%s", Math.round(sec), " сек.");
               currenttip.add(String.format("%sДо цветения остаётся %s", EnumChatFormatting.DARK_AQUA, textTime));
            }
         }
      }

      return currenttip;
   }

   public int getTimeUntil() {
      return Math.max(0, (int)(this.tick - this.worldObj.getTotalWorldTime()));
   }

   // $FF: synthetic method
   private void lambda$onBlockActivated$0(EntityPlayerMP playerMP, EntityPlayer player, UUID uuid) {
   }
}
