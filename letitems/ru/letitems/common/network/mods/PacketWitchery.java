package ru.letitems.common.network.mods;

import com.emoniph.witchery.common.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.network.AbstractPacket;
import ru.letitems.common.util.LockPlayer;

public final class PacketWitchery extends AbstractPacket {
   private int id;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketWitchery() {
   }

   public PacketWitchery(int id) {
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

   // $FF: synthetic method
   private void lambda$processServer$0(String name, EntityPlayerMP player, ExtendedPlayer playerEx, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
