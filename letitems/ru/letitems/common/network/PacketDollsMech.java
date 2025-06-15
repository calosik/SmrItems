package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.NetHandlerPlayServer;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.util.BlockPos;
import ru.letitems.common.util.LockPlayer;
import thaumcraft.api.aspects.Aspect;

public final class PacketDollsMech extends AbstractPacket {
   private static LockPlayer lockPlayer = new LockPlayer();
   public static final List<String> aspectList = Arrays.asList("aer", "aqua", "ignis", "ordo", "terra", "perditio", "gelum", "lux", "motus", "permutatio", "potentia", "tempestas", "vacuos", "venenum", "victus", "vitreus", "bestia", "fames", "herba", "iter", "limus", "metallum", "mortuus", "praecantatio", "sano", "tenebrae", "vinculum", "volatus");
   private BlockDoll.DollType dollType;
   private String act;
   private int x;
   private int y;
   private int z;

   public PacketDollsMech() {
   }

   public PacketDollsMech(BlockDoll.DollType dollType, BlockPos blockPos) {
      this(dollType, blockPos, "");
   }

   public PacketDollsMech(BlockDoll.DollType dollType, BlockPos blockPos, String act) {
      this.dollType = dollType;
      this.act = act;
      this.x = blockPos.x;
      this.y = blockPos.y;
      this.z = blockPos.z;
   }

   public void fromBytes(ByteBuf buf) {
      this.dollType = BlockDoll.DollType.getTypeFromIndex(buf.readByte());
      this.act = ByteBufUtils.readUTF8String(buf);
      this.x = buf.readInt();
      this.y = buf.readInt();
      this.z = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.dollType.ordinal());
      ByteBufUtils.writeUTF8String(buf, this.act);
      buf.writeInt(this.x);
      buf.writeInt(this.y);
      buf.writeInt(this.z);
   }

   public void processServer(NetHandlerPlayServer netHandler) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$8(EntityPlayerMP player, UUID uuid) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$7(EntityPlayerMP player, String aspectName, int aspectCount, Aspect aspect, UUID uuid) {
   }

   // $FF: synthetic method
   private static void lambda$processServer$6(EntityPlayerMP player, String kysTag, String name, int blocksNeed, Item fromBlock, UUID uuid) {
   }

   // $FF: synthetic method
   private void lambda$processServer$5(EntityPlayerMP player, int idGetRelic, List relics, UUID uuid) {
   }

   // $FF: synthetic method
   private void lambda$processServer$4(EntityPlayerMP player, int itemCoin, List items, int idGetItem, UUID uuid) {
   }

   // $FF: synthetic method
   private void lambda$processServer$3(EntityPlayerMP player, int relicCoin, int idGetRelic, List relics, UUID uuid) {
   }

   // $FF: synthetic method
   private void lambda$processServer$2(EntityPlayerMP player) {
   }

   // $FF: synthetic method
   private void lambda$processServer$1(EntityPlayerMP player, UUID uuid) {
   }

   // $FF: synthetic method
   private void lambda$processServer$0(EntityPlayerMP player) {
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
