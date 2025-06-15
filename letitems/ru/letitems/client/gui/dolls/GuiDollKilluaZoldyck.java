package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollKilluaZoldyck extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-killua.png");
   private final EntityPlayer player;
   private static final ArrayList<String> listText = new ArrayList(3);
   private final BlockPos blockPos;
   private boolean take;
   public static int timeEnd;
   public static String killName;

   public GuiDollKilluaZoldyck(EntityPlayer player, BlockPos blockPos) {
      this.player = player;
      this.blockPos = blockPos;
      this.take = false;
      killName = null;
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      int xx = x - this.posX;
      int yy = y - this.posY;
      this.drawStringWhenSize("Киллуа Золдик", 1.15F, 158, 150, 4351639);
      this.fontRendererObj.drawString("Устранение", this.posX + 170, this.posY + 161, 9408399);
      if (GuiDollKilluaZoldyck.timeEnd == -1) {
         this.drawStringWhenSize("Нет доступа", 1.25F, 41, 82, 7105644);
         this.drawStringWhenSize("Используйте игровой предмет", 0.75F, 21, 94, 10395294);
         this.drawStringWhenSize("Игла Киллуа на кукле,", 0.75F, 37, 101, 10395294);
         this.drawStringWhenSize("чтобы получить доступ", 0.75F, 34, 108, 10395294);
         this.drawStringWhenSize("к особым заданиям.", 0.75F, 43, 115, 10395294);
      } else if (TimeTuTick.instance.get(TimeTuTick.TypeTime.KILLUA) > 0L) {
         this.drawStringWhenSize("Нет целей", 1.25F, 48, 82, 16777215);
         float timeEnd = (float)TimeTuTick.instance.get(TimeTuTick.TypeTime.KILLUA);
         String text = String.format("До новой %.0f ч %.0f м", Math.floor((double)(timeEnd / 3600.0F)), Math.floor((double)(timeEnd % 3600.0F / 60.0F)));
         this.fontRendererObj.drawString(text, this.posX + 82 - this.fontRendererObj.getStringWidth(text) / 2, this.posY + 95, 14013909);
      } else {
         if (killName == null && !this.take) {
            this.take = true;
            NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.KILLUA_ZOLDYCK, this.blockPos, "-1"));
            return;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawGradientRect(this.posX + 20 + 118, this.posY + 65 + 30, this.posX + 20, this.posY + 65, 815175318, 815175318);
         int titleWidth = this.fontRendererObj.getStringWidth(killName);
         float scale = Math.min(108.0F / (float)titleWidth, 1.25F);
         this.drawStringWhenSize(killName, scale, (int)(80.0F - (float)(titleWidth / 2) * scale), 76, 13816530);
         this.drawStringWhenSize("Уничтожение цели", 1.0F, 32, 105, 16645629);
         this.drawStringWhenSize("Убейте это существо", 0.75F, 38, 117, 14803425);
      }

      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(listText, x + 4, y + 4);
      }

   }

   static {
      listText.add("§6Кукла Киллуа Золдик");
      listText.add("§7Даёт награды за устранение");
      listText.add("§7случайных целей");
      timeEnd = -1;
      killName = null;
   }
}
