package ru.letitems.client.gui.screen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientParams;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.handler.LootBoxManager;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketLootBox;
import ru.letitems.common.util.GeneralUtils;

@SideOnly(Side.CLIENT)
public final class GuiLootBox extends GuiPreRenderScreen {
   private static final ResourceLocation bg = new ResourceLocation("letitems", "textures/gui/gui-lootbox.png");
   private static final ResourceLocation materialLootBox = new ResourceLocation("letitems", "textures/gui/box-panel-3.png");
   private static String[] decFormat = new String[]{"у", "ы", ""};
   private static String[] decFormatCount = new String[]{"а", "ы", ""};
   private int sendId = -1;
   private boolean countId = false;
   public static boolean isActive = false;
   public static int pageBox = -1;
   public static List<ItemStack> finItemStack;
   public static int rCoins;
   private int scale;
   private int mouseY = 0;
   private int guiMapY;
   private int isMouseButtonDown = 0;
   private int maxHeight = 0;

   public GuiLootBox() {
      isActive = false;
      pageBox = -1;
      finItemStack = null;
      rCoins = 0;
   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      this.sendId = -1;
      this.countId = false;
      int picIndex;
      int countThisBoxCoins;
      int drawnY;
      int i;
      int i;
      int boxLeft;
      int boxRight;
      int l2;
      int countCoinReal;
      boolean nonCoins;
      int color1;
      if (isActive && pageBox != -1) {
         GeneralClientUtils.bind("letitems:textures/gui/gui-lootbox-f1.png");
         func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
         LootBoxManager.LootBox box = (LootBoxManager.LootBox)LootBoxManager.listLootBox.get(pageBox);
         if (!box.isActive()) {
            return;
         }

         boolean isScrollBox;
         boolean isFullHover;
         if (finItemStack != null && finItemStack.size() > 0) {
            this.sendId = -1;
            isScrollBox = finItemStack.size() > 1;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GeneralClientUtils.bind(String.format("letitems:textures/gui/gui-lootbox-f%s.png", isScrollBox ? 3 : 2));
            func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
            ItemStack hoverItemLoot = null;
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            float scaleBox = isScrollBox ? 1.75F : 2.5F;
            GL11.glEnable(32826);
            GL11.glScalef(scaleBox, scaleBox, scaleBox);
            countThisBoxCoins = isScrollBox ? 85 : 139;
            drawnY = isScrollBox ? 91 : 87;

            for(i = 0; i < (isScrollBox ? 5 : 1); ++i) {
               ItemStack itemStack = (ItemStack)finItemStack.get(i);
               boxLeft = 30 * i + countThisBoxCoins;
               itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStack, (int)((float)(boxLeft + this.guiLeft) / scaleBox), (int)((float)(drawnY + this.guiTop) / scaleBox));
               if (xx > boxLeft && xx < boxLeft + 29 && yy > 95 && yy < 125) {
                  hoverItemLoot = itemStack;
               }
            }

            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
            isFullHover = xx > 42 && xx < 275 && yy > 149 && yy < 182;
            if (isScrollBox) {
               this.drawStringWhenSize("Забрать предметы", 1.25F, 103, 161, isFullHover ? 16777215 : 13290186);
            } else {
               this.drawStringWhenSize("Забрать предмет", 1.25F, 106, 161, isFullHover ? 16777215 : 13290186);
            }

            if (rCoins > 0) {
               this.drawStringWhenSize("Получено монет для магазина - " + rCoins, 1.0F, 69, 191, 8158332);
            }

            if (isFullHover) {
               this.drawGradientRect(this.guiLeft + 45 + 229, this.guiTop + 151 + 30, this.guiLeft + 45, this.guiTop + 151, 1163504061, 1163504061);
               this.sendId = 2;
            } else if (hoverItemLoot != null) {
               ArrayList<String> listText = new ArrayList();
               listText.add("§6" + hoverItemLoot.getDisplayName());
               if (hoverItemLoot.stackSize > 1) {
                  listText.add("§7" + GeneralUtils.declinationCombine(hoverItemLoot.stackSize, decFormatCount, "единиц"));
               }

               this.drawHoveringText(listText, x + 10, y + 10);
            }

            return;
         }

         isScrollBox = xx >= 8 && xx < 293 && yy >= 100 && yy < 213;
         if (Mouse.isButtonDown(0)) {
            if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && isScrollBox) {
               if (this.isMouseButtonDown == 0) {
                  this.isMouseButtonDown = 1;
               } else {
                  this.guiMapY += y - this.mouseY;
               }

               this.mouseY = y;
            }
         } else {
            this.isMouseButtonDown = 0;
         }

         if (isScrollBox) {
            int k = Mouse.getDWheel();
            if (k < 0) {
               this.guiMapY -= 8;
            } else if (k > 0) {
               this.guiMapY += 8;
            }
         }

         if (this.guiMapY > 0) {
            this.isMouseButtonDown = 0;
            --this.guiMapY;
         }

         if (this.guiMapY < -this.maxHeight) {
            this.isMouseButtonDown = 0;
            ++this.guiMapY;
         }

         GL11.glPushMatrix();
         GL11.glEnable(32826);
         this.mc.getTextureManager().bindTexture(materialLootBox);
         float sizeItems = 1.25F;
         GL11.glScalef(1.25F, 1.25F, 1.25F);
         picIndex = box.getIndexPic();
         this.drawTexturedModalRect((int)((float)(this.guiLeft + 33) / 1.25F), (int)((float)(this.guiTop + 49) / 1.25F), 32 * picIndex, 32 * (picIndex / 8), 32, 32);
         GL11.glPopMatrix();
         countThisBoxCoins = box.getCount();
         this.drawStringWhenSize(box.getName(), 1.5F, 84, 57, 13290186);
         this.fontRendererObj.drawString("Сундук стоит " + GeneralUtils.declinationCombine(countThisBoxCoins, decFormat, "монет"), this.guiLeft + 84, this.guiTop + 72, 9408399);
         this.fontRendererObj.drawString("Список предметов", this.guiLeft + 28, this.guiTop + 104, 9408399);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.enableGUIStandardItemLighting();
         ItemStack itemHover = null;
         GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 115, 340, 109);
         boxLeft = 0;
         boxRight = 0;
         WeightedRandomChestContent[] itemsBox = box.getWizItemsMapPush();
         if (itemsBox.length <= 24) {
            this.guiMapY = 0;
         }

         for(i = 0; i < itemsBox.length; ++i) {
            l2 = 35 + 32 * boxLeft;
            countCoinReal = 123 + boxRight + this.guiMapY;
            if ((i + 1) % 8 == 0) {
               boxRight += 30;
               boxLeft = 0;
            } else {
               ++boxLeft;
            }

            if (countCoinReal > this.maxHeight) {
               this.maxHeight = countCoinReal - this.guiMapY - 197;
            }

            if (countCoinReal <= 225 && countCoinReal >= 97) {
               isFullHover = xx > l2 - 4 && xx < l2 + 23 && yy > countCoinReal - 4 && yy < countCoinReal + 21;
               if (yy > 224 || yy < 113) {
                  isFullHover = false;
               }

               GL11.glDisable(2896);
               GL11.glEnable(3042);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover ? 1.0F : 0.5F);
               this.mc.getTextureManager().bindTexture(resource);
               this.drawTexturedModalRect(l2 + this.guiLeft - 7, countCoinReal + this.guiTop - 6, 120, 170, 30, 28);
               GL11.glEnable(2896);
               GL11.glEnable(32826);
               GL11.glPushAttrib(1048575);
               ItemStack itemStack = itemsBox[i].theItemId;
               itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStack, l2 + this.guiLeft, countCoinReal + this.guiTop);
               GL11.glPopAttrib();
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glDisable(2896);
               if (isFullHover) {
                  itemHover = itemStack;
               }
            }
         }

         GeneralClientUtils.ScissorHelper.endScissor();
         RenderHelper.disableStandardItemLighting();
         if (itemHover != null) {
            List list = itemHover.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

            for(countCoinReal = 0; countCoinReal < list.size(); ++countCoinReal) {
               if (countCoinReal == 0) {
                  list.set(0, itemHover.getRarity().rarityColor + "" + list.get(0));
               } else {
                  list.set(countCoinReal, "§7" + list.get(countCoinReal));
               }
            }

            this.drawHoveringText(list, x + 10, y + 10);
         }

         boolean hover = xx > 20 && xx < 70 && yy > 18 && yy < 38;
         this.drawGradientRect(this.guiLeft + 21 + 47, this.guiTop + 20 + 15, this.guiLeft + 21, this.guiTop + 20, -1072689136, -1072689136);
         this.fontRendererObj.drawString("Назад", this.guiLeft + 30, this.guiTop + 24, hover ? 13290186 : 6710886);
         if (hover) {
            this.sendId = -2;
         }

         countCoinReal = box.getType() == 1 ? ClientParams.userCoinsBox1 : (box.getType() == 2 ? ClientParams.userCoinsBox2 : ClientParams.userCoinsBox3);
         String countCoin = String.valueOf(countCoinReal);
         this.drawGradientRect(this.guiLeft + 270 + 28, this.guiTop + 20 + 15, this.guiLeft + 270, this.guiTop + 20, -757575111, -757575111);
         this.fontRendererObj.drawString(countCoin, this.guiLeft + 284 - this.fontRendererObj.getStringWidth(countCoin) / 2, this.guiTop + 24, 13290186);
         if (xx > 269 && xx < 299 && yy > 18 && yy < 38) {
            ArrayList<String> listText = new ArrayList();
            listText.add("§6" + (box.getType() == 1 ? "Вечное желание" : (box.getType() == 2 ? "Камень Арте" : "Ванильный сундук")));
            this.drawHoveringText(listText, x + 10, y + 10);
         }

         boolean hoverSend = false;
         boolean hoverFiveSend = false;
         nonCoins = countCoinReal != 0 && countCoinReal / countThisBoxCoins >= 5;
         if (nonCoins) {
            hoverSend = xx > 216 && xx < 251 && yy > 53 && yy < 85;
            hoverFiveSend = xx > 256 && xx < 290 && yy > 53 && yy < 85;
         } else if (countCoinReal >= countThisBoxCoins) {
            hoverSend = xx > 216 && xx < 291 && yy > 53 && yy < 85;
         }

         color1 = hoverSend ? -267386864 : -1072689136;
         if (nonCoins) {
            this.drawGradientRect(this.guiLeft + 217 + 34, this.guiTop + 54 + 30, this.guiLeft + 217, this.guiTop + 54, color1, color1);
            this.drawStringWhenNSSize("1", 1.25F, 231, 65, hoverSend ? 16777215 : 13290186);
            this.drawGradientRect(this.guiLeft + 257 + 34, this.guiTop + 54 + 30, this.guiLeft + 257, this.guiTop + 54, hoverFiveSend ? -267386864 : -1072689136, hoverFiveSend ? -267386864 : -1072689136);
            this.drawStringWhenNSSize("5", 1.25F, 271, 65, hoverFiveSend ? 16777215 : 13290186);
         } else if (countCoinReal >= countThisBoxCoins) {
            this.drawGradientRect(this.guiLeft + 217 + 74, this.guiTop + 54 + 30, this.guiLeft + 217, this.guiTop + 54, color1, color1);
            this.drawStringWhenNSSize("Открыть", 1.25F, 228, 65, hoverSend ? 16777215 : 13290186);
         }

         if (hoverSend || hoverFiveSend) {
            ArrayList<String> listText = new ArrayList();
            if (hoverFiveSend) {
               countThisBoxCoins *= 5;
            }

            if (countCoinReal >= countThisBoxCoins) {
               this.sendId = 1;
               this.countId = hoverFiveSend;
               listText.add("§8Открыть сундук с предметами");
            } else {
               listText.add("§8Cундук с предметами");
            }

            listText.add("§7Для открытия требуется §3" + (box.getType() == 1 ? "Вечное желание" : (box.getType() == 2 ? "Камень Арте" : "Ванильный сундук")));
            listText.add((!hoverFiveSend ? "§7Этот сундук стоит " : "§7Сундуки обойдутся в ") + GeneralUtils.declinationCombine(countThisBoxCoins, decFormat, "монет"));
            this.drawHoveringText(listText, x + 10, y + 10);
         }
      } else {
         this.mc.getTextureManager().bindTexture(bg);
         func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
         this.drawStringWhenSize("Сундуки с предметами", 1.5F, 25, 12, 13290186);
         this.fontRendererObj.drawString("letragon.ru/cabinet", this.guiLeft + 25, this.guiTop + 26, 9408399);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         int sizeBox = LootBoxManager.listLootBox.size();
         float sizeItems = 0.55F;
         int hh = false;

         for(picIndex = 0; picIndex < 2; ++picIndex) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            countThisBoxCoins = 4;
            drawnY = 0;
            this.mc.getTextureManager().bindTexture(materialLootBox);
            if (picIndex == 0) {
               this.drawTexturedModalRect(20 + this.guiLeft, this.guiTop + 38 + 0, 15, 240, 15, 15);
               this.drawStringWhenNSSize("§7" + ClientParams.userCoinsBox1 + "§r Вечное желание, §7" + ClientParams.userCoinsBox3 + "§r Стандартный сундук", 1.0F, 38, 43, 5066061);
            } else {
               this.drawTexturedModalRect(21 + this.guiLeft, this.guiTop + 92 + 0, 0, 240, 15, 15);
               this.drawStringWhenNSSize("§7" + ClientParams.userCoinsBox2 + "§r Камень Арте", 1.0F, 37, 97, 5066061);
               drawnY = countThisBoxCoins;
               countThisBoxCoins = sizeBox;
            }

            i = drawnY;
            i = 0;
            boxLeft = 0;

            for(boxRight = 0; i < countThisBoxCoins; ++i) {
               LootBoxManager.LootBox box = (LootBoxManager.LootBox)LootBoxManager.listLootBox.get(i);
               if (box.isActive()) {
                  this.mc.getTextureManager().bindTexture(materialLootBox);
                  l2 = 27 + 33 * boxLeft;
                  countCoinReal = 56 + boxRight + 56 * picIndex + 0;
                  boolean isHover = xx > l2 - 1 && xx < l2 + 28 && yy > countCoinReal + 2 && yy < countCoinReal + 29;
                  int countThisBoxCoins = box.getCount();
                  int countCoinReal = box.getType() == 1 ? ClientParams.userCoinsBox1 : (box.getType() == 2 ? ClientParams.userCoinsBox2 : ClientParams.userCoinsBox3);
                  nonCoins = countCoinReal != 0 && countCoinReal / countThisBoxCoins > 0;
                  GL11.glPushMatrix();
                  GL11.glEnable(3042);
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, isHover ? 1.0F : (nonCoins ? 0.5F : 0.15F));
                  GL11.glScalef(0.55F, 0.55F, 0.55F);
                  GL11.glEnable(32826);
                  color1 = (int)((float)(l2 + 5 + this.guiLeft) / 0.55F);
                  int i11 = (int)((float)(countCoinReal + 8 + this.guiTop) / 0.55F);
                  int picIndex = box.getIndexPic();
                  this.drawTexturedModalRect(color1, i11, 32 * picIndex, 32 * (picIndex / 8), 32, 32);
                  GL11.glPopMatrix();
                  int colorDraw = isHover ? 1351651472 : 167772159;
                  this.drawGradientRect(l2 + this.guiLeft + 28, countCoinReal + this.guiTop + 29, l2 + this.guiLeft, countCoinReal + this.guiTop + 3, colorDraw, colorDraw);
                  if (nonCoins) {
                     this.drawStringWhenNSSize("" + countCoinReal / countThisBoxCoins, 0.5F, l2 + 22, countCoinReal + 22, isHover ? 15987699 : 6447714);
                  }

                  if (isHover) {
                     this.sendId = i;
                  }

                  if ((i + 1) % 7 == 0) {
                     boxRight += 30;
                     boxLeft = 0;
                  } else {
                     ++boxLeft;
                  }

                  ++i;
               }
            }
         }

         if (this.sendId != -1) {
            LootBoxManager.LootBox box = (LootBoxManager.LootBox)LootBoxManager.listLootBox.get(this.sendId);
            ArrayList<String> listText = new ArrayList();
            listText.add("§6" + box.getName());
            listText.add("§7Стоит " + GeneralUtils.declinationCombine(box.getCount(), decFormat, "монет"));
            this.drawHoveringText(listText, x + 10, y + 10);
         }
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (finItemStack != null && this.sendId == 2) {
         finItemStack = null;
      } else if (this.sendId == 1 && pageBox != -1) {
         NetworkManager.sendToServer(new PacketLootBox(pageBox, this.countId));
      } else if (this.sendId == -2) {
         this.maxHeight = 0;
         isActive = false;
         pageBox = -1;
      } else if (!isActive && this.sendId >= 0) {
         isActive = true;
         pageBox = this.sendId;
         this.guiMapY = 0;
         this.mouseY = y;
      }

   }
}
