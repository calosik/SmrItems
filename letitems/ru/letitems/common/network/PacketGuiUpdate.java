package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import ru.letitems.client.gui.inventory.GuiGlobal;

public final class PacketGuiUpdate extends AbstractPacket {
   private int slave;
   private int slaves;
   private String skills;
   private String quest;

   public PacketGuiUpdate() {
   }

   public PacketGuiUpdate(int slave, int slaves, String skills, String quest) {
      this.slave = slave;
      this.slaves = slaves;
      this.skills = skills;
      this.quest = quest;
   }

   public void fromBytes(ByteBuf buf) {
      this.slave = buf.readInt();
      this.slaves = buf.readInt();
      this.skills = ByteBufUtils.readUTF8String(buf);
      this.quest = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slave);
      buf.writeInt(this.slaves);
      ByteBufUtils.writeUTF8String(buf, this.skills);
      ByteBufUtils.writeUTF8String(buf, this.quest);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      GuiGlobal.slaveSt = this.slave;
      GuiGlobal.slaveStId = this.slaves;
      int var4;
      if (!GuiGlobal.skillsList.isEmpty() && this.skills != null && !this.skills.equals("")) {
         int i = 0;
         String[] var3 = this.skills.split("@");
         var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String info = var3[var5];
            String[] sk = info.split(",");
            GuiGlobal.Skills skills = (GuiGlobal.Skills)GuiGlobal.skillsList.get(i);
            skills.setLevel(Integer.parseInt(sk[0]));
            skills.setPercent(sk[1]);
            ++i;
         }
      }

      GuiGlobal.questList.clear();
      if (this.quest != null && !this.quest.equals("")) {
         String[] var9 = this.quest.split("%");
         int var10 = var9.length;

         for(var4 = 0; var4 < var10; ++var4) {
            String info = var9[var4];
            String[] qk = info.split("@");
            GuiGlobal.questList.add(new GuiGlobal.Quest(qk[0], Integer.parseInt(qk[1])));
         }
      }

   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
