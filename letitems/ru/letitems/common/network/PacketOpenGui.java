package ru.letitems.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import ru.letitems.common.util.GuiType;

public final class PacketOpenGui extends AbstractPacket {
   private GuiType guiType;

   public PacketOpenGui() {
   }

   public PacketOpenGui(GuiType guiType) {
      this.guiType = guiType;
   }

   public void fromBytes(ByteBuf buf) {
      this.guiType = GuiType.getGuiType(buf.readByte());
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.guiType.ordinal());
   }

   public void processServer(NetHandlerPlayServer netHandler) {
      if (this.guiType != null && this.guiType.isAllowPacket()) {
         EntityPlayerMP player = netHandler.playerEntity;
         this.guiType.openGui(player, (World)player.worldObj);
      }
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
