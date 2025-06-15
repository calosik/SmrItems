package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.handler.LootBoxManager;
import ru.letitems.common.util.LockPlayer;

public final class PacketLootBox extends AbstractPacket {
   private int id;
   private boolean count;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketLootBox() {
   }

   public PacketLootBox(int id, boolean count) {
      this.id = id;
      this.count = count;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
      this.count = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeBoolean(this.count);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   // $FF: synthetic method
   private void lambda$processServer$0(LootBoxManager.LootBox box, EntityPlayerMP player, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
