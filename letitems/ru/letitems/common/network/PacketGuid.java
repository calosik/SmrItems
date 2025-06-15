package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.client.gui.inventory.GuiAttackRepair;
import ru.letitems.common.util.LockPlayer;

public final class PacketGuid extends AbstractPacket {
   private byte gui;
   private int id;
   private static LockPlayer lockPlayer = new LockPlayer();

   public PacketGuid() {
   }

   public PacketGuid(byte gui, int id) {
      this.gui = gui;
      this.id = id;
   }

   public void fromBytes(ByteBuf buf) {
      this.gui = buf.readByte();
      this.id = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.gui);
      buf.writeInt(this.id);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$0(EntityPlayerMP player, ItemStack item, GuiAttackRepair.ContainerAttackRepair containerAttackRepair, UUID uuid) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
