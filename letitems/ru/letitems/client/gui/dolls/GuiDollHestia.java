package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.LoadAndRenderPic;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.util.BlockPos;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;

@SideOnly(Side.CLIENT)
public final class GuiDollHestia extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-hestia.png");
   private String args;
   public static int openPage = 0;
   private static final ArrayList<String> listText = new ArrayList(3);
   private final String username;
   private final BlockPos blockPos;

   public GuiDollHestia(EntityPlayer player, BlockPos blockPos) {
      this.username = player.getDisplayName();
      this.blockPos = blockPos;
      this.args = null;
      LoadAndRenderPic.instance.queueResearchInformation("254.png", "R254.png");
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      boolean isFullHover1 = xx > -34 && xx < -1 && yy > -2 && yy < 28 || openPage == 0;
      if (isFullHover1) {
         openPage = 0;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover1 ? 1.0F : 0.25F);
      this.drawTexturedModalRect(this.posX - 32, this.posY, 0, 190, 30, 28);
      this.drawTexturedModalRect(this.posX - 25, this.posY + 6, 30, 190, 16, 16);
      boolean isFullHover2 = xx > -34 && xx < -1 && yy > 28 && yy < 60 || openPage == 1;
      if (isFullHover2) {
         openPage = 1;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover2 ? 1.0F : 0.25F);
      this.drawTexturedModalRect(this.posX - 32, this.posY + 30, 0, 190, 30, 28);
      this.drawTexturedModalRect(this.posX - 25, this.posY + 37, 46, 190, 16, 16);
      GL11.glPopMatrix();
      this.drawDollName("Гестия", 162, 150, 7049897);
      this.fontRendererObj.drawString("Помощь Богини", this.posX + 164, this.posY + 161, 9408399);
      if (openPage == 0) {
         LoadAndRenderPic.instance.renderPic("R254.png", 1.25F, this.posX + 56, this.posY + 19, 1.0F);
         this.fontRendererObj.drawString("Великий Маг", this.posX + 49, this.posY + 65, 7110345);
         this.drawStringWhenSize("Достижение ThaumCraft", 0.75F, 34, 75, 6517163);
         this.drawStringWhenSize("Режим исследований", 1.1F, 22, 95, 13290186);
         this.drawStringWhenSize("Изменение сложности", 0.75F, 38, 107, 9408399);
         this.drawStringWhenSize("исследований в ThaumCraft", 0.75F, 27, 115, 9408399);
         boolean isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
         if (isHover) {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
            this.args = "1";
         } else {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
         }

         if (Thaumcraft.proxy.playerKnowledge.getDefficulty(this.username) == 0) {
            this.drawStringWhenSize("Обычный режим", 1.0F, 42, 155, isHover ? -1 : 9408399);
         } else {
            this.drawStringWhenSize("Лёгкий режим", 1.0F, 46, 155, isHover ? -1 : 9408399);
         }

         if (isHover) {
            ArrayList<String> listText = new ArrayList();
            listText.add("§7Переключить режим сложности");
            listText.add("§8Требуется достижение Великий Маг");
            this.drawHoveringText(listText, x + 4, y + 4);
         }
      } else {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.enableGUIStandardItemLighting();
         AspectList aspects = Thaumcraft.proxy.getPlayerKnowledge().getAspectsDiscovered(this.username);
         if (aspects != null) {
            Aspect aspectSelect = null;
            int drawn = 0;
            int kfkfkd = 0;
            int fdsfdsf = 0;
            Aspect[] var13 = aspects.getAspects();
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               Aspect aspect = var13[var15];
               if (aspect != null && PacketDollsMech.aspectList.contains(aspect.getName().toLowerCase()) && aspects.getAmount(aspect) > 0) {
                  int l2 = 19 * kfkfkd + 14;
                  int i1 = fdsfdsf + 14;
                  if ((drawn + 1) % 7 == 0) {
                     fdsfdsf += 18;
                     kfkfkd = 0;
                  } else {
                     ++kfkfkd;
                  }

                  float alpha = 0.7F;
                  if (xx > l2 && xx < l2 + 18 && yy > i1 && yy < i1 + 18) {
                     aspectSelect = aspect;
                     alpha = 1.0F;
                  }

                  UtilsFX.drawTag(this.posX + l2, this.posY + i1, aspect, (float)aspects.getAmount(aspect), 0, (double)this.zLevel, 771, alpha);
                  ++drawn;
               }
            }

            if (drawn == 0) {
               this.fontRendererObj.drawString("Аспектов нет", this.posX + 44, this.posY + 47, 9408399);
            } else if (aspectSelect != null) {
               ArrayList<String> listText = new ArrayList();
               listText.add("§6Аспект " + aspectSelect.getName() + " §7(" + aspects.getAmount(aspectSelect) + ")");
               listText.add("§8Отправить на склад аукциона");
               this.drawHoveringText(listText, x + 4, y + 4);
               this.args = String.format("2#%s", aspectSelect.getName());
            }
         }

         RenderHelper.disableStandardItemLighting();
         this.drawStringWhenSize("Аукцион на сайте", 1.1F, 30, 105, 13290186);
         this.drawStringWhenSize("Отправка аспектов на склад", 0.75F, 24, 117, 9408399);
         this.drawStringWhenSize("для продажи на сайте", 0.75F, 37, 125, 9408399);
         boolean isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
         if (isHover) {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
            this.args = "3";
         } else {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
         }

         this.drawStringWhenSize("Забрать со склада", 0.75F, 44, 156, isHover ? -1 : 9408399);
         if (isHover) {
            this.drawHoveringText(Collections.singletonList("§7Забрать все аспекты с аукциона на сервер"), x + 4, y + 4);
         }
      }

      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(GuiDollHestia.listText, x + 4, y + 4);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.args != null) {
         NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.HESTIA, this.blockPos, this.args));
      }

   }

   static {
      listText.add("§6Кукла Гестия");
      listText.add("§7Позволяет переключить режим исследований");
      listText.add("§7и отправить аспекты на склад аукциона");
   }
}
