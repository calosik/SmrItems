package ru.SmrItems.common.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import ru.SmrItems.Config;
import ru.SmrItems.common.tileentity.TileWorldAnchor;
import ru.SmrItems.common.utils.enums.FieldType;
import ru.SmrItems.common.utils.enums.LoadingMode;

public class PacketClickButton implements IMessage {
   private int buttonId;
   private int posX;
   private int posY;
   private int posZ;
   private int dimensionId;

   public PacketClickButton() {
   }

   public PacketClickButton(int buttonId, int posX, int posY, int posZ, int dimensionId) {
      this.buttonId = buttonId;
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.dimensionId = dimensionId;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.buttonId);
      buf.writeInt(this.posX);
      buf.writeInt(this.posY);
      buf.writeInt(this.posZ);
      buf.writeInt(this.dimensionId);
   }

   public void fromBytes(ByteBuf buf) {
      this.buttonId = buf.readInt();
      this.posX = buf.readInt();
      this.posY = buf.readInt();
      this.posZ = buf.readInt();
      this.dimensionId = buf.readInt();
   }

   public static class Handler implements IMessageHandler<PacketClickButton, IMessage> {
      public IMessage onMessage(PacketClickButton message, MessageContext ctx) {
         EntityPlayer player = ctx.getServerHandler().playerEntity;
         TileWorldAnchor te = (TileWorldAnchor)player.worldObj.getTileEntity(message.posX, message.posY, message.posZ);
         switch(message.buttonId) {
         case 1:
            LoadingMode mode = (LoadingMode)te.getField(FieldType.MODE);
            int time = (Integer)te.getField(FieldType.CHUNKLOADINGTIME);
            if (mode == LoadingMode.NORMAL) {
               te.setField(FieldType.MODE, LoadingMode.NORMAL);
               te.setField(FieldType.CHUNKLOADINGTIME, time / Config.multiplier);
            }

            te.getWorldObj().markBlockForUpdate(message.posX, message.posY, message.posZ);
            break;
         case 2:
            boolean paused = (Boolean)te.getField(FieldType.ISPAUSED);
            if (paused) {
               te.forceChunkLoading();
               te.setField(FieldType.ISPAUSED, false);
            } else {
               te.releaseTicket();
               te.setField(FieldType.ISPAUSED, true);
            }

            te.getWorldObj().markBlockForUpdate(message.posX, message.posY, message.posZ);
         }

         return null;
      }
   }
}
