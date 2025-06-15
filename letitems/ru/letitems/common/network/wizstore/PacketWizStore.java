package ru.letitems.common.network.wizstore;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.network.AbstractPacket;
import ru.letitems.common.util.LockPlayer;
import ru.letitems.common.util.wizsotre.WizItems;

public final class PacketWizStore extends AbstractPacket {
   private int id;
   private byte phase;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketWizStore() {
   }

   public PacketWizStore(int id, byte phase) {
      this.id = id;
      this.phase = phase;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
      this.phase = buf.readByte();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeByte(this.phase);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$1(EntityPlayerMP player, UUID uuid) {
   }

   // $FF: synthetic method
   private void lambda$processServer$0(EntityPlayerMP player, WizItems.WizBuildItems wizItem, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
