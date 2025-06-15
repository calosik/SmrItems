package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.NetHandlerPlayServer;

public class PacketSceneSender extends AbstractPacket {
   private int uid;
   private int part;
   private int parts;
   private byte[] bytes;

   public PacketSceneSender() {
   }

   public PacketSceneSender(int uid, int part, int parts, byte[] bytes) {
      this.uid = uid;
      this.part = part;
      this.parts = parts;
      this.bytes = bytes;
   }

   public void fromBytes(ByteBuf byteBuf) {
      this.uid = byteBuf.readInt();
      this.part = byteBuf.readInt();
      this.parts = byteBuf.readInt();
      this.bytes = this.readBytes(byteBuf);
   }

   public void toBytes(ByteBuf byteBuf) {
      byteBuf.writeInt(this.uid);
      byteBuf.writeInt(this.part);
      byteBuf.writeInt(this.parts);
      this.writeBytes(byteBuf, this.bytes);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   private void writeBytes(ByteBuf buf, byte[] bytes) {
      int length = bytes.length;
      buf.writeInt(length);
      if (length > 0) {
         buf.writeBytes(bytes);
      }

   }

   private byte[] readBytes(ByteBuf buf) {
      int length = buf.readInt();
      return length <= 0 ? new byte[0] : buf.readBytes(length).array();
   }

   // $FF: synthetic method
   private static void lambda$processServer$0(String playerName, byte[] bytes) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
