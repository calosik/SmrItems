package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import net.minecraft.client.network.NetHandlerPlayClient;
import ru.letitems.client.gui.screen.GuiCollections;
import ru.letitems.client.gui.screen.GuiEnderBonus;
import ru.letitems.client.gui.screen.GuiStoreItems;
import ru.letitems.client.gui.screen.GuiWitchery;

public final class PacketGuisSync extends AbstractPacket {
   public static final int WITCHERY = 0;
   public static final int COLLECTIONS = 1;
   public static final int STORE = 2;
   public static final int END_BONUS = 3;
   private int gui;
   private int argInt;
   private String argStr;

   public PacketGuisSync() {
   }

   public PacketGuisSync(int gui) {
      this.gui = gui;
   }

   public PacketGuisSync(int gui, int argInt) {
      this.gui = gui;
      this.argInt = argInt;
   }

   public PacketGuisSync(int gui, String argStr) {
      this.gui = gui;
      this.argStr = argStr;
   }

   public void fromBytes(ByteBuf buf) {
      this.gui = buf.readInt();
      if (this.gui == 1 || this.gui == 3) {
         this.argInt = buf.readInt();
      }

      if (this.gui == 2) {
         this.argStr = ByteBufUtils.readUTF8String(buf);
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.gui);
      if (this.gui == 1 || this.gui == 3) {
         buf.writeInt(this.argInt);
      }

      if (this.gui == 2) {
         ByteBufUtils.writeUTF8String(buf, this.argStr);
      }

   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      switch(this.gui) {
      case 0:
         GuiWitchery.dropInfo();
         break;
      case 1:
         if (this.argInt > 0) {
            Iterator var2 = GuiCollections.catList.iterator();

            while(var2.hasNext()) {
               GuiCollections.Category category = (GuiCollections.Category)var2.next();
               Iterator var4 = category.items.iterator();

               while(var4.hasNext()) {
                  GuiCollections.Collections collections = (GuiCollections.Collections)var4.next();
                  if (collections.id == this.argInt) {
                     collections.setDateLock();
                  }
               }
            }
         }
         break;
      case 2:
         GuiStoreItems.buildItemsCollections(this.argStr);
         break;
      case 3:
         GuiEnderBonus.endSelect = this.argInt;
      }

   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
