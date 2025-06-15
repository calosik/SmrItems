package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.util.LockPlayer;

public final class PacketAnchor extends AbstractPacket {
   private int id;
   private int x;
   private int y;
   private int z;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketAnchor() {
   }

   public PacketAnchor(int id, int x, int y, int z) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
      this.x = buf.readInt();
      this.y = buf.readInt();
      this.z = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeInt(this.x);
      buf.writeInt(this.y);
      buf.writeInt(this.z);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   // $FF: synthetic method
   private void lambda$processServer$0(EntityPlayerMP player, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
