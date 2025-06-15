package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import net.minecraft.client.network.NetHandlerPlayClient;
import ru.letitems.client.ClientParams;
import ru.letitems.client.EventHandlerClient;
import ru.letitems.client.gui.screen.GuiArch;
import ru.letitems.client.util.LoadAndRenderPic;

public final class PacketAchiewment extends AbstractPacket {
   private String text;
   private String image;
   private int id;
   private byte points;

   public PacketAchiewment() {
   }

   public PacketAchiewment(String text, int id, String image, byte points) {
      this.text = text;
      this.id = id;
      this.image = image;
      this.points = points;
   }

   public void fromBytes(ByteBuf buf) {
      this.text = ByteBufUtils.readUTF8String(buf);
      this.id = buf.readInt();
      this.image = ByteBufUtils.readUTF8String(buf);
      this.points = buf.readByte();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.text);
      buf.writeInt(this.id);
      ByteBufUtils.writeUTF8String(buf, this.image);
      buf.writeByte(this.points);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      EventHandlerClient.instance.achiewmentHandler.queueResearchInformation(this.text, this.image);
      ClientParams.userAchPoints += this.points;
      if (GuiArch.catList != null && !GuiArch.catList.isEmpty()) {
         Iterator var2 = GuiArch.catList.iterator();

         while(true) {
            while(var2.hasNext()) {
               GuiArch.Category category = (GuiArch.Category)var2.next();
               Iterator var4 = category.achievements.iterator();

               while(var4.hasNext()) {
                  GuiArch.Achievements achievements = (GuiArch.Achievements)var4.next();
                  if (achievements.id == this.id) {
                     LoadAndRenderPic.PictureData data = (LoadAndRenderPic.PictureData)LoadAndRenderPic.instance.bufferImages.get(achievements.getImage());
                     if (data != null) {
                        data.needUpload = true;
                     }
                     break;
                  }
               }
            }

            return;
         }
      }
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
