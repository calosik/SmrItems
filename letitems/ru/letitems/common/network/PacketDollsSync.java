package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import ru.letitems.client.gui.dolls.GuiDollAlice;
import ru.letitems.client.gui.dolls.GuiDollBellCranel;
import ru.letitems.client.gui.dolls.GuiDollKilluaZoldyck;
import ru.letitems.client.gui.dolls.GuiDollKoko;
import ru.letitems.client.gui.dolls.GuiDollKysygaki;
import ru.letitems.client.gui.dolls.GuiDollWiz;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.handler.DollMechKilluaZoldyck;
import ru.letitems.common.integration.BotaniaCoins;

public final class PacketDollsSync extends AbstractPacket {
   private BlockDoll.DollType dollType;
   private String args;

   public PacketDollsSync() {
   }

   public PacketDollsSync(BlockDoll.DollType dollType, String args) {
      this.dollType = dollType;
      this.args = args;
   }

   public void fromBytes(ByteBuf buf) {
      this.dollType = BlockDoll.DollType.getTypeFromIndex(buf.readByte());
      this.args = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.dollType.ordinal());
      ByteBufUtils.writeUTF8String(buf, this.args);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      if (this.dollType == BlockDoll.DollType.BELL_CRANEL) {
         GuiDollBellCranel.rebuild();
      } else if (this.dollType == BlockDoll.DollType.WIZ) {
         GuiDollWiz.buildItem(this.args);
      } else if (this.dollType == BlockDoll.DollType.KOKO) {
         GuiDollKoko.hairs.clear();
      } else if (!StringUtils.isNullOrEmpty(this.args)) {
         String[] dollArgs = this.args.split("#");
         if (this.dollType == BlockDoll.DollType.ALICE_KISARAGI) {
            GuiDollAlice.gaiaCoins = Integer.parseInt(dollArgs[0]);
            if (dollArgs.length == 1) {
               return;
            }

            String[] playerUnlockRelics = dollArgs[1].split("");
            if (!dollArgs[1].equals("r")) {
               for(int i = 0; i < playerUnlockRelics.length; ++i) {
                  ((BotaniaCoins.ItemStoreALice)BotaniaCoins.botaniaCoins.getListAliceRelics().get(i)).setLock(playerUnlockRelics[i].equals("1"));
               }
            }

            if (dollArgs.length == 2) {
               return;
            }

            GuiDollAlice.lockTakeRelic = dollArgs[2].equals("1");
         } else if (this.dollType == BlockDoll.DollType.KYSYGAKI) {
            GuiDollKysygaki.buildWands(Integer.parseInt(dollArgs[0]), Integer.parseInt(dollArgs[1]));
         } else if (this.dollType == BlockDoll.DollType.KILLUA_ZOLDYCK) {
            GuiDollKilluaZoldyck.timeEnd = Integer.parseInt(dollArgs[0]);
            TimeTuTick.instance.delete(TimeTuTick.TypeTime.KILLUA);
            if (GuiDollKilluaZoldyck.timeEnd > 0) {
               TimeTuTick.instance.register(TimeTuTick.TypeTime.KILLUA, (long)GuiDollKilluaZoldyck.timeEnd);
            }

            if (dollArgs.length == 1) {
               return;
            }

            GuiDollKilluaZoldyck.killName = StatCollector.translateToLocal("entity." + DollMechKilluaZoldyck.getEntityClassName(DollMechKilluaZoldyck.CREATURE_CLASSES[Integer.parseInt(dollArgs[1])]) + ".name");
         }
      }

   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
