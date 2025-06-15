package ru.letitems.client.gui.screen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientParams;
import ru.letitems.client.gui.inventory.GuiGlobal;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.client.util.LoadAndRenderPic;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.items.ItemArtefeUp;
import ru.letitems.common.items.ItemMagicShard;
import ru.letitems.common.items.ItemSD;
import ru.letitems.common.items.ItemSiteCrafting;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.wizstore.PacketWizStore;
import ru.letitems.common.util.wizsotre.WizItems;

@SideOnly(Side.CLIENT)
public final class GuiWizStore extends GuiPreRenderScreen {
   private static final ResourceLocation bg = new ResourceLocation("letitems", "textures/gui/gui-wiz-store.png");
   private int sendId = -1;
   private int selectId = -1;
   private int[] countItems;
   private final boolean isUnlock;
   private int scale;
   public static boolean isLoaded = false;
   public static boolean isActive = false;
   public static List<ItemStack> finStacks;
   public static boolean dropLegendary = false;
   public static int dropCoins = 0;

   public GuiWizStore(EntityPlayer player) {
      this.xSize = 480;
      this.ySize = 245;
      this.scrollMouse = true;
      this.scrollWheel = true;
      isLoaded = false;
      this.isUnlock = ClientParams.isUnlockAch("store_wiz");
      if (this.isUnlock) {
         this.countItems = new int[4];
         ItemStack[] var2 = player.inventory.mainInventory;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack stack = var2[var4];
            if (stack != null) {
               Item item = stack.getItem();
               int[] var10000;
               if (item instanceof ItemMagicShard) {
                  var10000 = this.countItems;
                  var10000[1] += stack.stackSize;
               } else if (item instanceof ItemArtefeUp) {
                  var10000 = this.countItems;
                  var10000[2] += stack.stackSize;
               } else if (item instanceof ItemSiteCrafting && stack.getItemDamage() == 4) {
                  var10000 = this.countItems;
                  var10000[0] += stack.stackSize;
               } else if (item instanceof ItemSD) {
                  var10000 = this.countItems;
                  var10000[3] += stack.stackSize;
               }
            }
         }
      }

      if (!this.isUnlock) {
         LoadAndRenderPic.instance.queueResearchInformation("302.png", "R302.png");
      }

   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.sendId = -1;
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      int i1;
      if (!this.isUnlock) {
         GeneralClientUtils.bind("letitems:textures/gui/gui-wiz-store-panel.png");
         func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
         this.drawStringWhenNSSize("Лавочка Виз", 2.75F, 154, 96, 7231819);
         this.drawStringWhenNSSize("Нет достижения 'Мастер зачарований'", 1.0F, 143, 119, 7626825);
         if (GuiGlobal.skillsList != null && !GuiGlobal.skillsList.isEmpty()) {
            GuiGlobal.Skills sk = (GuiGlobal.Skills)GuiGlobal.skillsList.get(7);
            if (sk.getLevel() != 50) {
               this.drawGradientRect(this.guiLeft + 141 + 202, this.guiTop + 138 + 35 - 6, this.guiLeft + 141, this.guiTop + 138 - 6, 1348620090, 1348620090);
               LoadAndRenderPic.instance.renderPic("R302.png", 1.0F, this.guiLeft + 139, this.guiTop + 135 - 6, 1.0F);
               this.drawStringWhenNSSize("Мастер зачарований", 1.0F, 178, 140, 7626825);
               this.drawStringWhenNSSize(sk.getLevel() + " / 50 уровень", 0.75F, 178, 155, 7626825);
               float parse = sk.getPercent() != null ? Float.parseFloat(sk.getPercent()) : 100.0F;
               i1 = (int)((100.0F - parse) / 100.0F * 157.0F);
               this.drawGradientRect(this.guiLeft + 178 + 157, this.guiTop + 2 + 156 - 6, this.guiLeft + 178, this.guiTop + 156 - 6, 812538187, 812538187);
               this.drawGradientRect(this.guiLeft + 178 + i1, this.guiTop + 2 + 156 - 6, this.guiLeft + 178, this.guiTop + 156 - 6, -2140251829, -2140251829);
            }
         }
      } else if (!isLoaded) {
         GeneralClientUtils.bind("letitems:textures/gui/gui-wiz-store-panel.png");
         func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
         this.drawStringWhenNSSize("Лавочка Виз", 2.75F, 154, 114, 7231819);
         this.drawStringWhenNSSize("Загрузка", 1.0F, 210, 140, 7626825);
      } else if (xx > 460 && xx < 480 && yy > 0 && yy < 15) {
         GeneralClientUtils.bind("letitems:textures/gui/gui-wiz-store-panel.png");
         func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
         this.drawStringWhenNSSize("Информация", 1.75F, 70, 100, 7231819);
         this.fontRendererObj.drawString("Лавка Виз - это возможность получения различных вещей", this.guiLeft + 69, this.guiTop + 120, 6048833);
         this.fontRendererObj.drawString("путём обмена с торговкой Виз. Как только Виз выполнит заказ", this.guiLeft + 69, this.guiTop + 130, 6048833);
         this.fontRendererObj.drawString("можно будет вновь запросить обмен.", this.guiLeft + 69, this.guiTop + 140, 6048833);
      } else {
         int countReward;
         if (!isActive) {
            boolean isHover;
            boolean isHover;
            int l2;
            int i;
            if (finStacks != null && finStacks.size() > 0) {
               GeneralClientUtils.bind("letitems:textures/gui/gui-wiz-store-panel.png");
               func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
               GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 78, 450, 75);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               countReward = finStacks.size() + 1;
               l2 = 0;
               i1 = 0;
               GL11.glPushMatrix();

               for(i = 0; i < countReward; ++i) {
                  int l2 = 67 + this.guiLeft + 180 * l2;
                  int i1 = 91 + this.guiTop + i1 + this.guiMapY;
                  if ((i + 1) % 2 == 0) {
                     i1 += 33;
                     l2 = 0;
                  } else {
                     ++l2;
                  }

                  if (i1 > this.maxHeight) {
                     this.maxHeight = i1 - this.guiMapY - 225;
                  }

                  GL11.glEnable(3042);
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  if (i == 0) {
                     this.drawGradientRect(l2 + 175 - 6, i1 + 30 - 6, l2 - 6, i1 - 6, 1350664577, 1350664577);
                     this.drawStringWhenNSSize("Монеты лавочки", 1.0F, l2 + 5 - this.guiLeft, i1 + 2 - this.guiTop, 7168856);
                     this.drawStringWhenNSSize("Количество - " + dropCoins, 0.75F, l2 + 5 - this.guiLeft, i1 + 11 - this.guiTop, 7235422);
                  } else {
                     ItemStack stack = (ItemStack)finStacks.get(i - 1);
                     if (dropLegendary && i + 1 == countReward) {
                        this.drawGradientRect(l2 + 175 - 6, i1 + 30 - 6, l2 - 6, i1 - 6, 1623694960, 1623694960);
                     } else {
                        this.drawGradientRect(l2 + 175 - 6, i1 + 30 - 6, l2 - 6, i1 - 6, 543313722, 543313722);
                     }

                     GL11.glPushMatrix();
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     RenderHelper.enableGUIStandardItemLighting();
                     GL11.glEnable(2884);
                     itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack, l2 + 1, i1 + 1);
                     RenderHelper.disableStandardItemLighting();
                     GL11.glPopMatrix();
                     this.drawGradientRect(l2 + 30 - 6, i1 + 30 - 6, l2 - 6, i1 - 6, 543313722, 543313722);
                     String itemName = stack.getDisplayName();
                     int titleWidth = this.fontRendererObj.getStringWidth(itemName);
                     this.drawStringWhenNSSize(itemName, Math.min(130.0F / (float)titleWidth, 1.0F), l2 + 28 - this.guiLeft, i1 + 2 - this.guiTop, 7168856);
                     this.drawStringWhenNSSize("Количество - " + stack.stackSize, 0.75F, l2 + 28 - this.guiLeft, i1 + 11 - this.guiTop, 7235422);
                  }
               }

               GL11.glPopMatrix();
               GeneralClientUtils.ScissorHelper.endScissor();
               isHover = true;
               isHover = xx > 60 && xx < 416 && yy > 157 && yy < 180;
               this.drawGradientRect(this.guiLeft + 61 + 355, this.guiTop + 165 + 15, this.guiLeft + 61, this.guiTop + 165 - 8, 543313722, 543313722);
               if (isHover) {
                  this.drawGradientRect(this.guiLeft + 61 + 355, this.guiTop + 165 + 15, this.guiLeft + 61, this.guiTop + 165 - 8, 543313722, 543313722);
                  this.sendId = -3;
               }

               this.drawStringWhenNSSize("Готово", 1.25F, 215, 164, isHover ? 6115650 : 7693134);
            } else {
               if (this.selectId < 0) {
                  this.mc.getTextureManager().bindTexture(bg);
               } else {
                  GeneralClientUtils.bind("letitems:textures/gui/gui-wiz-store-items.png");
               }

               func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
               this.drawStringWhenNSSize("Лавочка Виз", 1.25F, 38, 34, 7231819);
               this.drawStringWhenNSSize("Выбор ресурсов", 0.75F, 176, 37, 10324348);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

               for(countReward = 0; countReward < 4; ++countReward) {
                  l2 = 86 + this.guiLeft;
                  i1 = 60 + this.guiTop + 39 * countReward;
                  isHover = x > l2 - 32 && x < l2 + 140 && y > i1 - 3 && y < i1 + 29;
                  isHover = isHover || this.selectId == countReward;
                  WizItems.WizBuildItems wizItem = (WizItems.WizBuildItems)WizItems.wizItemsMap.get(countReward);
                  int itemStack = wizItem.getStack().stackSize;
                  this.fontRendererObj.drawString(wizItem.getStack().getDisplayName(), l2, i1 + 6, isHover ? 13816530 : 8744285);
                  this.drawStringWhenNSSize("Требуется " + (this.countItems[countReward] < itemStack ? this.countItems[countReward] : (this.countItems[countReward] >= itemStack ? itemStack : 0)) + "/" + itemStack + ", время " + wizItem.getTime(), 0.75F, 86, i1 + 16 - this.guiTop, isHover ? 13816530 : (this.countItems[countReward] != 0 && this.countItems[countReward] >= itemStack ? 6442810 : 9138774));
                  if (isHover) {
                     this.sendId = this.countItems[countReward] >= itemStack ? countReward : -2;
                  }
               }

               if (this.selectId < 0) {
                  this.drawStringWhenNSSize("Лавочка Виз", 2.25F, 273, 118, 7231819);
                  if (this.selectId == -2) {
                     this.drawStringWhenNSSize("Недостаточно для обмена", 1.0F, 278, 138, 7626825);
                  } else {
                     this.drawStringWhenNSSize("Выберите предмет обмена", 1.0F, 278, 138, 7626825);
                  }
               } else {
                  WizItems.WizBuildItems wizItem = (WizItems.WizBuildItems)WizItems.wizItemsMap.get(this.selectId);
                  List<String> wizItemList = wizItem.getList();
                  this.drawStringWhenNSSize("Лавочка Виз", 2.25F, 273, 60, 7231819);
                  String itemName = wizItem.getStack().getDisplayName();
                  this.drawStringWhenNSSize(itemName, 1.0F, 343 - this.fontRendererObj.getStringWidth(itemName) / 2, 80, 7231819);
                  this.drawStringWhenNSSize((String)wizItemList.get(0), 3.0F, ((String)wizItemList.get(0)).length() == 1 ? 283 : 272, 118, 7626825);
                  this.drawStringWhenNSSize("Монеты", 0.75F, 277, 148, 7231819);
                  int infB = 6;
                  this.drawStringWhenNSSize("Предметы с данжей", 0.75F, 342, 95 + infB, 7231819);
                  this.drawStringWhenNSSize((String)wizItemList.get(1), 1.0F, 370, 101 + infB, 7626825);
                  i = infB + 30;
                  this.drawStringWhenNSSize("Уникальная награда", 0.75F, 341, 95 + i, 7231819);
                  this.drawStringWhenNSSize((String)wizItemList.get(2) + "%", 1.0F, ((String)wizItemList.get(2)).length() == 1 ? 376 : 370, 101 + i, 7626825);
                  i += 30;
                  this.drawStringWhenNSSize("Очков сил тьмы", 0.75F, 350, 95 + i, 7231819);
                  this.drawStringWhenNSSize((String)wizItemList.get(3) + " очков", 0.75F, 363, 102 + i, 7626825);
                  isHover = xx > 260 && xx < 428 && yy > 186 && yy < 208;
                  if (isHover) {
                     this.drawGradientRect(this.guiLeft + 261 + 166, this.guiTop + 188 + 19, this.guiLeft + 261, this.guiTop + 188, 543313722, 543313722);
                     this.sendId = 5;
                  }

                  this.drawStringWhenNSSize("Начать", 1.25F, 320, 192, isHover ? 6115650 : 7693134);
               }
            }
         } else if (TimeTuTick.instance.get(TimeTuTick.TypeTime.WIZ_STORE) >= 0L) {
            GeneralClientUtils.bind("letitems:textures/gui/gui-wiz-store-panel.png");
            func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
            if (TimeTuTick.instance.get(TimeTuTick.TypeTime.WIZ_STORE) == 0L) {
               this.drawStringWhenNSSize("Лавочка Виз", 2.75F, 154, 102, 7231819);
               this.drawStringWhenNSSize("Виз вернулась с товарами", 1.0F, 169, 125, 7626825);
               countReward = xx > 139 && xx < 339 && yy > 135 && yy < 164 ? 1969377082 : 1348620090;
               this.drawGradientRect(this.guiLeft + 141 + 197, this.guiTop + 138 + 26, this.guiLeft + 141, this.guiTop + 138, countReward, countReward);
               this.drawStringWhenNSSize("Забрать награду", 1.25F, 184, 146, 12758679);
               if (xx > 139 && xx < 339 && yy > 135 && yy < 164) {
                  this.sendId = -10;
               }
            } else {
               this.drawStringWhenNSSize("Лавочка Виз", 2.75F, 154, 112, 7231819);
               this.drawStringWhenNSSize("Дождитесь возвращения Виз", 1.25F, 147, 134, 7626825);
               float timeEnd = (float)TimeTuTick.instance.get(TimeTuTick.TypeTime.WIZ_STORE);
               String text = String.format("Осталось %.0f ч %.0f м %.0f с", Math.floor((double)(timeEnd / 3600.0F)), Math.floor((double)(timeEnd % 3600.0F / 60.0F)), timeEnd % 60.0F);
               this.fontRendererObj.drawString(text, this.guiLeft + 240 - this.fontRendererObj.getStringWidth(text) / 2, this.guiTop + 146, 7626825);
            }
         }
      }

      if (this.isUnlock) {
         this.fontRendererObj.drawString("?", this.guiLeft + this.xSize - 10, this.guiTop + 5, 7430486);
      }

   }

   protected boolean isScrollBox(int x, int y) {
      x -= this.guiLeft;
      y -= this.guiTop;
      return x >= 40 && x < 450 && y >= 45 && y < 240 && !isActive && finStacks != null;
   }

   public void mouseMovedOrUp(int mouseX, int mouseY, int which) {
      if (which == 0 && !this.mouseMoving) {
         if (this.sendId == -3) {
            isActive = false;
            TimeTuTick.instance.delete(TimeTuTick.TypeTime.WIZ_STORE);
            finStacks = null;
            dropLegendary = false;
            this.selectId = -1;
         } else if (this.sendId == -10) {
            NetworkManager.sendToServer(new PacketWizStore(0, (byte)2));
         } else if (!isActive && this.sendId != -1) {
            if (this.sendId == 5 && this.selectId != -1) {
               NetworkManager.sendToServer(new PacketWizStore(this.selectId, (byte)1));
            } else {
               this.selectId = this.sendId;
            }
         }
      }

      super.mouseMovedOrUp(mouseX, mouseY, which);
   }
}
