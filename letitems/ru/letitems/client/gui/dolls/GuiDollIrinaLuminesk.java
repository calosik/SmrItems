package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.integration.BotaniaCoins;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.util.BlockPos;
import ru.letitems.common.util.GeneralUtils;

@SideOnly(Side.CLIENT)
public final class GuiDollIrinaLuminesk extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-alice-f1.png");
   private static String[] decFormat = new String[]{"у", "ы", ""};
   private String args;
   private static final ArrayList<String> listText = new ArrayList(3);
   public static int openPage = 0;
   public static int openPageRelic = -1;
   public static int gaiaCoins = 0;
   public static boolean lockTakeRelic = false;
   private final BlockPos blockPos;
   private int scale;
   private int guiMapY;
   private int maxHeight;

   public GuiDollIrinaLuminesk(EntityPlayer player, BlockPos blockPos) {
      this.blockPos = blockPos;
      this.maxHeight = 0;
      this.args = null;
   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      int k;
      if (openPageRelic != -1) {
         List<BotaniaCoins.ItemStoreALice> listItems = openPage == 0 ? BotaniaCoins.botaniaCoins.getListAliceRelics() : BotaniaCoins.botaniaCoins.getListAliceItems();
         GeneralClientUtils.bind("letitems:textures/gui/dolls/gui-doll-alice-f2.png");
         this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
         BotaniaCoins.ItemStoreALice stack = (BotaniaCoins.ItemStoreALice)listItems.get(openPageRelic);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.enableGUIStandardItemLighting();
         GL11.glPushMatrix();
         GL11.glEnable(2896);
         GL11.glEnable(32826);
         GL11.glTranslated((double)(this.posX + 109), (double)(this.posY + 60), 0.0D);
         GL11.glScalef(2.5F, 2.5F, 2.5F);
         itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack.stack, 0, 0);
         GL11.glDisable(2896);
         GL11.glPopMatrix();
         String itemName = stack.stack.getRarity().rarityColor + "" + stack.stack.getDisplayName();
         k = this.fontRendererObj.getStringWidth(itemName);
         this.drawStringWhenSize(itemName, Math.min(120.0F / (float)k, 2.5F), 72, 110, 13816530);
         int colorText;
         if (stack.lock) {
            if (lockTakeRelic) {
               this.fontRendererObj.drawString("Сейчас нельзя получить реликвии", this.posX + 38, this.posY + 127, 12541291);
            } else {
               colorText = 9408399;
               if (xx > 56 && xx < 185 && yy > 123 && yy < 138 && gaiaCoins >= 12) {
                  this.drawGradientRect(this.posX + 52 + 161, this.posY + 127 + 11, this.posX + 52 - 4, this.posY + 126 - 3, 369098751, 369098751);
                  colorText = 12763842;
                  this.args = String.format("2#%s", openPageRelic);
               }

               this.fontRendererObj.drawString("Получить еще раз за 12 монет", this.posX + 55, this.posY + 127, colorText);
            }
         } else {
            colorText = 9408399;
            if (xx > 56 && xx < 185 && yy > 123 && yy < 138 && gaiaCoins >= stack.coins) {
               this.drawGradientRect(this.posX + 70 + 126, this.posY + 127 + 11, this.posX + 70 - 4, this.posY + 127 - 3, 369098751, 369098751);
               colorText = 12763842;
               this.args = String.format("%s#%s", openPage, openPageRelic);
            }

            this.fontRendererObj.drawString(String.format("Купить за %s", GeneralUtils.declinationCombine(stack.coins, decFormat, "монет")), this.posX + 80, this.posY + 127, colorText);
         }

         colorText = 7960953;
         if (xx > 110 && xx < 153 && yy > 138 && yy < 149) {
            colorText = 12763842;
            this.args = "@-1";
         }

         this.fontRendererObj.drawString("Назад", this.posX + 115, this.posY + 137 + 4, colorText);
         RenderHelper.disableStandardItemLighting();
      } else {
         this.mc.getTextureManager().bindTexture(RESOURCE);
         this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
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
         this.drawTexturedModalRect(this.posX - 25, this.posY + 39, 47, 190, 16, 12);
         GL11.glPopMatrix();
         boolean isHoverCoins = xx >= 170 && xx <= 242 && yy >= 121 && yy <= 147;
         this.drawStringWhenSize("" + gaiaCoins, 1.75F, (int)(205.0F - (float)(this.fontRendererObj.getStringWidth("" + gaiaCoins) / 2) * 1.75F), 128, 14007091);
         this.drawDollName("Алиса", 169, 150, 7049897);
         this.fontRendererObj.drawString("Лавка вещей", this.posX + 160 + 13, this.posY + 161, 9408399);
         this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 14209712);
         k = Mouse.getDWheel();
         if (k < 0) {
            this.guiMapY -= 8;
         } else if (k > 0) {
            this.guiMapY += 8;
         }

         if (this.guiMapY > 0) {
            --this.guiMapY;
         }

         if (this.guiMapY < -this.maxHeight) {
            ++this.guiMapY;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.enableGUIStandardItemLighting();
         GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.posX, this.posY + 14, 340, 163);
         int stackHover = -1;
         int l2 = 18;
         String textNameInfo;
         List listItems;
         if (openPage == 0) {
            textNameInfo = "Реликвия";
            listItems = BotaniaCoins.botaniaCoins.getListAliceRelics();
         } else {
            textNameInfo = "Предмет";
            listItems = BotaniaCoins.botaniaCoins.getListAliceItems();
         }

         int sizeList = listItems.size();
         if (sizeList <= 6) {
            this.guiMapY = 0;
         }

         for(int i = 0; i < sizeList; ++i) {
            int i1 = 18 + 27 * i + this.guiMapY;
            if (i1 > this.maxHeight) {
               this.maxHeight = i1 - this.guiMapY - 156;
            }

            if (i1 <= 180 && i1 >= 1) {
               boolean hoverRelic = xx > l2 && xx < l2 + 140 && yy > i1 - 6 && yy < i1 + 19;
               if (yy > 180 || yy < 10) {
                  hoverRelic = false;
               }

               GL11.glEnable(2896);
               GL11.glEnable(32826);
               BotaniaCoins.ItemStoreALice stack = (BotaniaCoins.ItemStoreALice)listItems.get(i);
               itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack.stack, l2 + this.posX, i1 + this.posY);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glDisable(2896);
               int colorDraw = hoverRelic ? 1083216016 : 285212671;
               this.drawGradientRect(this.posX + l2 + 138, this.posY + i1 + 20, this.posX + l2 - 4, this.posY + i1 - 4, colorDraw, colorDraw);
               String itemName = stack.stack.getRarity().rarityColor + "" + stack.stack.getDisplayName();
               int titleWidth = this.fontRendererObj.getStringWidth(itemName);
               this.drawStringWhenSize(itemName, Math.min(112.0F / (float)titleWidth, 1.0F), l2 + 20, i1 + ((float)this.scale != 1.0F ? 2 : 1), 13816530);
               if (stack.lock) {
                  this.drawStringWhenSize(textNameInfo + " уже открыта", 0.75F, l2 + 20, i1 + 10, 7305877);
               } else {
                  this.drawStringWhenSize(String.format(textNameInfo + " стоит %s", GeneralUtils.declinationCombine(stack.coins, decFormat, "монет")), 0.75F, l2 + 20, i1 + 10, 11513775);
               }

               if (hoverRelic) {
                  stackHover = i;
               }
            }
         }

         GeneralClientUtils.ScissorHelper.endScissor();
         RenderHelper.disableStandardItemLighting();
         if (stackHover != -1) {
            BotaniaCoins.ItemStoreALice relstoreALicec = (BotaniaCoins.ItemStoreALice)listItems.get(stackHover);
            ArrayList<String> listText = new ArrayList();
            listText.add("§6" + relstoreALicec.stack.getDisplayName());
            listText.add("§8Нажмите для " + (relstoreALicec.lock ? "подробностей" : "покупки"));
            this.drawHoveringText(listText, x + 4, y + 4);
            this.args = String.format("@%s", stackHover);
         } else if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
            this.drawHoveringText(GuiDollIrinaLuminesk.listText, x + 4, y + 4);
         } else if (isHoverCoins) {
            this.drawHoveringText(Collections.singletonList("§7Монеты Гайи"), x + 4, y + 4);
         }

      }
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.args != null) {
         if (this.args.startsWith("@")) {
            openPageRelic = Integer.parseInt(this.args.replace("@", ""));
            this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "letitems:nav.but", 0.08F, 1.25F, false);
         } else {
            NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.ALICE_KISARAGI, this.blockPos, this.args));
         }
      }

   }

   public void onGuiClosed() {
      openPageRelic = -1;
   }

   static {
      listText.add("§6Кукла 123");
      listText.add("§7Позволяет купить реликвии и");
      listText.add("§7и другие предметы за монеты Гайи");
   }
}
