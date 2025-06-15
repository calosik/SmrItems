package ru.letitems.common.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.letitems.client.ClientParams;

public final class PacketClientParams extends AbstractPacket {
   private static final List<Integer> listIds = Arrays.asList(168, 302, 300);
   private boolean cp1;
   private boolean cp2;
   private boolean cp3;
   private int cp7;
   private int cp8;
   private int cp9;
   private boolean cp10;
   private int cp11;
   private int cp12;

   public PacketClientParams() {
   }

   public PacketClientParams(boolean p1, boolean p2, boolean p3, int p5, int p6, int p7, boolean p8, int p9, int p10) {
      this.cp1 = p1;
      this.cp2 = p2;
      this.cp3 = p3;
      this.cp7 = p5;
      this.cp8 = p6;
      this.cp9 = p7;
      this.cp10 = p8;
      this.cp11 = p9;
      this.cp12 = p10;
   }

   public void fromBytes(ByteBuf buf) {
      this.cp1 = buf.readBoolean();
      this.cp2 = buf.readBoolean();
      this.cp3 = buf.readBoolean();
      this.cp7 = buf.readInt();
      this.cp8 = buf.readInt();
      this.cp9 = buf.readInt();
      this.cp10 = buf.readBoolean();
      this.cp11 = buf.readInt();
      this.cp12 = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.cp1);
      buf.writeBoolean(this.cp2);
      buf.writeBoolean(this.cp3);
      buf.writeInt(this.cp7);
      buf.writeInt(this.cp8);
      buf.writeInt(this.cp9);
      buf.writeBoolean(this.cp10);
      buf.writeInt(this.cp11);
      buf.writeInt(this.cp12);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      ClientParams.archUnlock.clear();
      ClientParams.archUnlock.put("ender_chest", this.cp1);
      ClientParams.archUnlock.put("store_wiz", this.cp2);
      ClientParams.archUnlock.put("attack_repair", this.cp10);
      ClientParams.isOpenedPrimeStore = this.cp3;
      ClientParams.userCoinsBox1 = this.cp7;
      ClientParams.userCoinsBox2 = this.cp8;
      ClientParams.userCoinsBox3 = this.cp9;
      ClientParams.userAchPoints = this.cp11;
      ClientParams.idKys = this.cp12;
   }

   public static void sendClientParams(EntityPlayer player) {
   }

   // $FF: synthetic method
   private static void lambda$sendClientParams$0(String name, EntityPlayerMP playerMP, EntityPlayer player) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
