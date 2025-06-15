package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.util.LockPlayer;

public final class PacketUpgradeScrollTitle extends AbstractPacket {
   private static LockPlayer lockPlayer = new LockPlayer();
   private String text;

   public PacketUpgradeScrollTitle() {
   }

   public PacketUpgradeScrollTitle(String text) {
      this.text = text;
   }

   public void fromBytes(ByteBuf buf) {
      this.text = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.text);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
