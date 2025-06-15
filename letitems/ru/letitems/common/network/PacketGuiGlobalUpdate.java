package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.NetHandlerPlayServer;

public final class PacketGuiGlobalUpdate extends AbstractPacket {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
