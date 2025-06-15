package ru.letitems.client.gui.screen;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketStoreItems;

@SideOnly(Side.CLIENT)
public final class GuiStoreItems extends GuiPreRenderScreen {
   private int sendId = -1;
   private int sendItemId = 0;
   private int sendItemCount = 0;
   public static List<GuiStoreItems.ItemStoreItem> itemsList = new ArrayList();
   private static boolean isFinish = false;
   private static boolean isOpenItem = false;
   private int scale;
   private int mouseY = 0;
   private int guiMapY;
   private int isMouseButtonDown = 0;
   private int maxHeight = 0;
   private float currentScroll;
   private boolean isScrolling;
   private boolean wasClicking;

   public GuiStoreItems() {
      isFinish = false;
      isOpenItem = false;
      this.isScrolling = false;
      this.currentScroll = 0.0F;
   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GeneralClientUtils.bind("letitems:textures/gui/gui-storeitems.png");
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      this.sendId = -1;
      this.drawStringWhenSize("Склад вещей", 1.5F, 21, 29, 13290186);
      boolean hoverFin;
      if (isOpenItem) {
         hoverFin = xx > 19 && xx < 102 && yy > 39 && yy < 52;
         this.fontRendererObj.drawString("Назад на склад", this.guiLeft + 21, this.guiTop + 41, hoverFin ? 13290186 : 6381921);
         if (hoverFin) {
            this.sendId = -5;
         }
      } else {
         this.fontRendererObj.drawString("letragon.ru/store", this.guiLeft + 21, this.guiTop + 41, 6381921);
      }

      if (!isFinish) {
         this.fontRendererObj.drawString("Загрузка списка предметов...", this.guiLeft + 80, this.guiTop + 135, 9408399);
      } else if (itemsList == null || itemsList.size() <= 0) {
         this.drawStringWhenSize("Склад пуст...", 2.0F, 100, 118, 9408399);
         this.fontRendererObj.drawString("letragon.ru/store", this.guiLeft + 114, this.guiTop + 138, 9408399);
         this.drawGradientRect(this.guiLeft + 273 + 25, this.guiTop + 27 + 25, this.guiLeft + 273, this.guiTop + 27, -1072689136, -1072689136);
         return;
      }

      if (TimeTuTick.instance.get(TimeTuTick.TypeTime.STORE) > 0L || isOpenItem) {
         this.drawGradientRect(this.guiLeft + 273 + 25, this.guiTop + 27 + 25, this.guiLeft + 273, this.guiTop + 27, -1072689136, -1072689136);
      }

      boolean isFullHover;
      int boxRight;
      int size;
      GuiStoreItems.ItemStoreItem itemHover;
      if (isOpenItem && itemsList != null) {
         GuiStoreItems.ItemStoreItem selectItem = null;
         Iterator var18 = itemsList.iterator();

         while(var18.hasNext()) {
            itemHover = (GuiStoreItems.ItemStoreItem)var18.next();
            if (itemHover.getId() == this.sendItemId) {
               selectItem = itemHover;
               break;
            }
         }

         if (selectItem == null) {
            isOpenItem = false;
            this.isScrolling = false;
            this.currentScroll = 0.0F;
         } else {
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            GL11.glEnable(2896);
            GL11.glEnable(3042);
            float scaleBox = 2.5F;
            GL11.glEnable(32826);
            GL11.glScalef(scaleBox, scaleBox, scaleBox);
            GL11.glPushAttrib(1048575);
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), selectItem.getItemStack(), (int)((float)(140 + this.guiLeft) / scaleBox), (int)((float)(70 + this.guiTop) / scaleBox));
            GL11.glPopAttrib();
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDisable(2896);
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
            float scaleText = selectItem.getItemStack().getDisplayName().length() > 20 ? 1.25F : 1.5F;
            this.drawStringWhenSize(selectItem.getItemStack().getRarity().rarityColor + "" + selectItem.getItemStack().getDisplayName(), scaleText, 160 - (int)((float)(this.fontRendererObj.getStringWidth(selectItem.getItemStack().getDisplayName()) / 2) * scaleText), 115, 11629643);
            this.fontRendererObj.drawString("Выберите нужное количество", this.guiLeft + 82, this.guiTop + 129, 9408399);
            isFullHover = xx > 136 && xx < 267 && yy > 27 && yy < 52;
            this.drawStringWhenSize("Получить всё", 1.25F, 157, 36, isFullHover ? 16777215 : 13290186);
            int maxSizeScroll = true;
            this.drawGradientRect(this.guiLeft + 50 + 219, this.guiTop + 155 + 4, this.guiLeft + 50, this.guiTop + 155, -1072689136, -1072689136);
            boolean flag = Mouse.isButtonDown(0);
            if (!this.wasClicking && flag && xx > 45 && xx < 278 && yy > 150 && yy < 160) {
               this.isScrolling = true;
            }

            if (!flag) {
               this.isScrolling = false;
            }

            this.wasClicking = flag;
            if (this.isScrolling) {
               this.currentScroll = ((float)(xx - 50) - 5.0F) / 209.0F;
               this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            }

            boxRight = this.guiLeft + 50;
            size = boxRight + 219;
            this.sendItemCount = (int)((float)selectItem.getItemCount() * this.currentScroll);
            this.drawGradientRect(boxRight + (int)((float)(size - boxRight - 1) * this.currentScroll), this.guiTop + 155 + 4, boxRight + (int)((float)(size - boxRight - 1) * this.currentScroll) - 5, this.guiTop + 155, -1, -1);
            String textCount = String.format((this.sendItemCount == 0 ? "§4" : "") + "%s§r / %s", this.sendItemCount, selectItem.getItemCount());
            this.fontRendererObj.drawString(textCount, this.guiLeft + 158 - this.fontRendererObj.getStringWidth(textCount) / 2, this.guiTop + 168, 9408399);
            if (this.sendItemCount > 0 && this.sendItemCount <= selectItem.getItemCount()) {
               boolean hover = xx > 132 && xx < 183 && yy > 178 && yy < 191;
               this.fontRendererObj.drawString("Забрать", this.guiLeft + 139, this.guiTop + 182, hover ? 13290186 : 9408399);
               if (hover) {
                  this.sendId = 1;
               }
            }

            if (isFullHover) {
               this.drawHoveringText(Collections.singletonList("§7Забрать всё количество этого предмета"), x + 15, y + 15);
               this.sendId = 1;
               this.sendItemCount = selectItem.getItemCount();
            }

         }
      } else {
         hoverFin = isFinish && xx > 136 && xx < 267 && yy > 27 && yy < 52;
         if (isFinish) {
            this.drawStringWhenSize("Получить всё", 1.25F, 157, 36, hoverFin ? 16777215 : 13290186);
         }

         boolean isScrollBox = xx >= 8 && xx < 293 && yy >= 60 && yy < 217;
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

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.enableGUIStandardItemLighting();
         GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 59, 340, 163);
         itemHover = null;
         int boxLeft = 0;
         boxRight = 0;
         size = itemsList.size();
         if (size <= 28) {
            this.guiMapY = 0;
         }

         for(int i = 0; i < size; ++i) {
            int l2 = 36 + 38 * boxLeft;
            int i1 = 71 + boxRight + this.guiMapY;
            if ((i + 1) % 7 == 0) {
               boxRight += 32;
               boxLeft = 0;
            } else {
               ++boxLeft;
            }

            if (i1 > this.maxHeight) {
               this.maxHeight = i1 - this.guiMapY - 194;
            }

            if (i1 <= 225 && i1 >= 39) {
               isFullHover = xx > l2 - 7 && xx < l2 + 23 && yy > i1 - 4 && yy < i1 + 21;
               if (yy > 224 || yy < 60) {
                  isFullHover = false;
               }

               GL11.glDisable(2896);
               GL11.glEnable(3042);
               GuiStoreItems.ItemStoreItem item = (GuiStoreItems.ItemStoreItem)itemsList.get(i);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover ? 1.0F : 0.4F);
               this.mc.getTextureManager().bindTexture(resource);
               this.drawTexturedModalRect(l2 + this.guiLeft - 7, i1 + this.guiTop - 6, 120, 170, 30, 28);
               GL11.glEnable(2896);
               GL11.glEnable(32826);
               GL11.glPushAttrib(1048575);
               itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), item.getItemStack(), l2 + this.guiLeft, i1 + this.guiTop);
               GL11.glPopAttrib();
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glDisable(2896);
               if (isFullHover) {
                  itemHover = item;
               }
            }
         }

         GeneralClientUtils.ScissorHelper.endScissor();
         RenderHelper.disableStandardItemLighting();
         if (itemHover != null) {
            ArrayList<String> listText = new ArrayList();
            listText.add("§7[" + itemHover.getItemCount() + "]§r " + itemHover.getItemStack().getRarity().rarityColor + "" + itemHover.getItemStack().getDisplayName());
            listText.add("§8Нажмите для получения");
            if (itemHover.getItemCount() != 1) {
               this.sendId = -3;
            } else {
               this.sendId = 1;
            }

            this.sendItemId = itemHover.getId();
            this.drawHoveringText(listText, x + 15, y + 15);
         }

         if (xx > 273 && xx < 298 && yy > 27 && yy < 52) {
            if (TimeTuTick.instance.get(TimeTuTick.TypeTime.STORE) > 0L) {
               this.drawHoveringText(Collections.singletonList("§8Можно обновить через " + TimeTuTick.instance.get(TimeTuTick.TypeTime.STORE) + " сек."), x + 15, y + 15);
            } else {
               this.drawHoveringText(Collections.singletonList("§7Обновить данные"), x + 15, y + 15);
               this.sendId = -2;
            }
         } else if (hoverFin) {
            this.drawHoveringText(Collections.singletonList("§7Вы заберёте все предметы на этот сервер"), x + 15, y + 15);
            this.sendId = -4;
         }

      }
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.sendId == -2) {
         itemsList.clear();
         isFinish = false;
         NetworkManager.sendToServer(new PacketStoreItems());
      } else if (this.sendId == -3) {
         isOpenItem = true;
      } else if (this.sendId == -4) {
         NetworkManager.sendToServer(new PacketStoreItems(0, 0, (byte)2));
      } else if (this.sendId == -5) {
         isOpenItem = false;
         this.isScrolling = false;
         this.currentScroll = 0.0F;
      } else if (this.sendId == 1 && this.sendItemId > 0) {
         NetworkManager.sendToServer(new PacketStoreItems(this.sendItemId, this.sendItemCount == 0 ? 1 : this.sendItemCount, (byte)1));
      }

   }

   public void onGuiClosed() {
      super.onGuiClosed();
      isFinish = false;
      isOpenItem = false;
      itemsList.clear();
   }

   public static void buildItemsCollections(String items) {
      itemsList.clear();
      if (items != null && !items.equals("")) {
         String[] var1 = items.split(",");
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String info = var1[var3];
            String[] qk = info.split("@");
            String[] itemString = qk[1].split(":");
            ItemStack stack = GameRegistry.findItemStack(itemString[0], itemString[1], 1);
            if (stack != null) {
               stack.setItemDamage(itemString.length > 2 ? Integer.parseInt(itemString[2]) : 0);
               itemsList.add(new GuiStoreItems.ItemStoreItem(Integer.parseInt(qk[0]), stack, Integer.parseInt(qk[2])));
            }
         }
      }

      TimeTuTick.instance.register(TimeTuTick.TypeTime.STORE, 15L);
      isFinish = true;
   }

   public static class ItemStoreItem {
      private final int id;
      private final ItemStack itemStack;
      private final int itemCount;

      public ItemStoreItem(int id, ItemStack itemStack, int dateLock) {
         this.id = id;
         this.itemStack = itemStack;
         this.itemCount = dateLock;
      }

      public int getId() {
         return this.id;
      }

      public ItemStack getItemStack() {
         return this.itemStack;
      }

      public int getItemCount() {
         return this.itemCount;
      }
   }
}
