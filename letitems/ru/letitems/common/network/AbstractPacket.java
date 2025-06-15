package ru.letitems.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;

public abstract class AbstractPacket implements IMessage {
   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   public abstract static class AbstractHandler implements IMessageHandler<AbstractPacket, IMessage> {
      public IMessage onMessage(AbstractPacket message, MessageContext ctx) {
         if (ctx.side.isClient()) {
            message.processClient(ctx.getClientHandler());
         } else {
            message.processServer(ctx.getServerHandler());
         }

         return null;
      }
   }
}
