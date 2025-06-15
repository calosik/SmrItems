package ru.letitems.common.network.mods;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.network.AbstractPacket;
import ru.letitems.common.util.LockPlayer;
import twilightforest.TFExtendedPlayer;

public final class PacketTwilightForest extends AbstractPacket {
   private int id;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketTwilightForest() {
   }

   public PacketTwilightForest(int id) {
      this.id = id;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   private void sendSync(EntityPlayerMP player, TFExtendedPlayer playerEx) {
   }

   // $FF: synthetic method
   private void lambda$processServer$0(String name, EntityPlayerMP player, TFExtendedPlayer playerEx, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
