package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;

public final class PacketVending extends AbstractPacket {
   private int id;
   private int x;
   private int y;
   private int z;
   private String args;
   private ItemStack stack;

   public PacketVending() {
   }

   public PacketVending(int id, String args, int x, int y, int z) {
      this.id = id;
      this.args = args;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public PacketVending(int id, ItemStack stack, int x, int y, int z) {
      this.id = id;
      this.stack = stack;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
      if (this.id == 3) {
         this.stack = ByteBufUtils.readItemStack(buf);
      } else {
         this.args = ByteBufUtils.readUTF8String(buf);
      }

      this.x = buf.readInt();
      this.y = buf.readInt();
      this.z = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
      if (this.id == 3) {
         ByteBufUtils.writeItemStack(buf, this.stack);
      } else {
         ByteBufUtils.writeUTF8String(buf, this.args);
      }

      buf.writeInt(this.x);
      buf.writeInt(this.y);
      buf.writeInt(this.z);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
