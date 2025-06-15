package ru.letitems.common.network.mods;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import ru.letitems.common.LetItems;
import ru.letitems.common.network.AbstractPacket;
import twilightforest.TFExtendedPlayer;

public final class PacketTwilightForestSync extends AbstractPacket {
   private String skills;
   private String boss;
   private int takenAwards;

   public PacketTwilightForestSync() {
   }

   public PacketTwilightForestSync(String skills, String boss, int takenAwards) {
      this.skills = skills;
      this.boss = boss;
      this.takenAwards = takenAwards;
   }

   public void fromBytes(ByteBuf buf) {
      this.skills = ByteBufUtils.readUTF8String(buf);
      this.boss = ByteBufUtils.readUTF8String(buf);
      this.takenAwards = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.skills);
      ByteBufUtils.writeUTF8String(buf, this.boss);
      buf.writeInt(this.takenAwards);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      if (LetItems.loadTF) {
         EntityPlayer player = Minecraft.getMinecraft().thePlayer;
         TFExtendedPlayer extendedPlayer = TFExtendedPlayer.get(player);
         int s = 0;
         String[] var5;
         int var6;
         int var7;
         String i;
         if (!StringUtils.isNullOrEmpty(this.skills)) {
            var5 = this.skills.split("@");
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               i = var5[var7];
               extendedPlayer.skillsLevel.put(TFExtendedPlayer.getSkillFromIndex(s++), Integer.parseInt(i));
            }
         }

         if (!StringUtils.isNullOrEmpty(this.boss)) {
            s = 0;
            var5 = this.boss.split("%");
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               i = var5[var7];
               extendedPlayer.boss.put(TFExtendedPlayer.bosses[s++], Integer.parseInt(i));
            }
         }

         extendedPlayer.setTakeAwards(this.takenAwards);
      }
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
