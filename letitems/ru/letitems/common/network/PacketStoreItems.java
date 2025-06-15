package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.util.LockPlayer;

public final class PacketStoreItems extends AbstractPacket {
   private int id;
   private int count;
   private byte phase;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketStoreItems() {
      this.id = 0;
      this.count = 0;
      this.phase = 0;
   }

   public PacketStoreItems(int id, int count, byte phase) {
      this.id = id;
      this.count = count;
      this.phase = phase;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
      this.count = buf.readInt();
      this.phase = buf.readByte();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeInt(this.count);
      buf.writeByte(this.phase);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   private static void giveItem(EntityPlayerMP player, ItemStack stack, int count) {
   }

   // $FF: synthetic method
   private void lambda$processServer$2(String name, EntityPlayerMP player, UUID uuid) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$1(EntityPlayerMP player, String name, UUID uuid) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$0(EntityPlayerMP player, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
