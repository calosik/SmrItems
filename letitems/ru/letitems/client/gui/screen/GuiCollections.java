package ru.letitems.client.gui.screen;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketCollections;

@SideOnly(Side.CLIENT)
public final class GuiCollections extends GuiPreRenderScreen {
   private int sendId = -1;
   private int openItem = -1;
   public static List<GuiCollections.Category> catList = new ArrayList();
   private static int selectCategory = 0;
   private int scale;
   private GuiTextField searchField;
   private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
   private static Type type = (new TypeToken<List<GuiCollections.Category>>() {
   }).getType();

   public GuiCollections(EntityPlayer player) {
      this.maxHeight = 0;
      this.scrollMouse = true;
      this.scrollWheel = true;
      selectCategory = 0;
      if (catList.isEmpty()) {
         EXECUTOR_SERVICE.submit(new GuiCollections.AchievementsLoadTask(player.getDisplayName()));
      }

   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
      this.searchField = new GuiTextField(this.fontRendererObj, this.guiLeft + 213, this.guiTop + 26, 85, 13);
      this.searchField.setTextColor(-1);
      this.searchField.setDisabledTextColour(-1);
      this.searchField.setEnableBackgroundDrawing(false);
      this.searchField.setMaxStringLength(18);
      Keyboard.enableRepeatEvents(true);
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      this.sendId = -1;
      GeneralClientUtils.bind("letitems:textures/gui/gui-collection.png");
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      if (catList != null && !catList.isEmpty()) {
         this.drawStringWhenNSSize("Коллекция", 1.5F, 24, 20, 13290186);
         String nameCat = "Набор ";
         Iterator var7 = catList.iterator();

         while(var7.hasNext()) {
            GuiCollections.Category category = (GuiCollections.Category)var7.next();
            if (category.id - 1 == selectCategory) {
               nameCat = nameCat + category.name;
               break;
            }
         }

         this.fontRendererObj.drawString(nameCat, this.guiLeft + 23, this.guiTop + 32, 9408399);

         int size;
         for(int i = 0; i < 5; ++i) {
            size = 23 + this.guiLeft;
            int posY = 43 + this.guiTop + 37 * i;
            boolean hover = x > size && x < size + 34 && y > posY && y < posY + 34 && this.openItem == -1;
            if (hover) {
               this.sendId = i;
               GL11.glPushMatrix();
               GL11.glRotatef(0.05F, 0.0F, 0.0F, 1.0F);
            }

            this.drawGradientRect(size + 34, posY + 34, size, posY, hover ? 1076110370 : 93426065, hover ? 1076110370 : 93426065);
            if (hover) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
               GL11.glEnable(3042);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
            }

            this.mc.getTextureManager().bindTexture(resource);
            this.drawTexturedModalRect(size + 4, posY + 6, 131 + 25 * i, 117, 25, 25);
            if (hover) {
               GL11.glPopMatrix();
            }
         }

         int boxLeft;
         int boxRight;
         GuiCollections.Collections itemHover;
         int i;
         if (this.openItem != -1) {
            this.sendId = -1;
            List<GuiCollections.Collections> itemsList = ((GuiCollections.Category)catList.get(selectCategory)).items;
            GuiCollections.Collections item = null;
            Iterator var24 = itemsList.iterator();

            while(var24.hasNext()) {
               itemHover = (GuiCollections.Collections)var24.next();
               if (itemHover.id == this.openItem) {
                  item = itemHover;
                  break;
               }
            }

            if (item != null && !item.isDateLock()) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               RenderHelper.enableGUIStandardItemLighting();
               GL11.glPushMatrix();
               GL11.glEnable(2896);
               GL11.glEnable(32826);
               GL11.glTranslated((double)(this.guiLeft + 158), (double)(this.guiTop + 72 + 15), 0.0D);
               GL11.glScalef(2.5F, 2.5F, 2.5F);
               itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), item.itemStack, 0, 0);
               GL11.glDisable(2896);
               GL11.glPopMatrix();
               String itemName = item.itemStack.getRarity().rarityColor + "" + item.itemStack.getDisplayName();
               itemName = itemName.replace("Кукла ", "");
               itemName = itemName.replace("Дакимакура ", "");
               int titleWidth = this.fontRendererObj.getStringWidth(itemName);
               float scale = Math.min(160.0F / (float)titleWidth, 1.75F);
               GL11.glPushMatrix();
               GL11.glTranslated((double)(this.guiLeft + 180 - (int)((float)(titleWidth / 2) * scale)), (double)(this.guiTop + 120 + 18), 0.0D);
               GL11.glScalef(scale, scale, scale);
               this.fontRendererObj.drawString(itemName, 0, 0, 13816530);
               GL11.glPopMatrix();
               i = 9408399;
               boxLeft = 7708585;
               if (xx > 74 && xx < 285 && yy > 159 && yy < 188) {
                  this.drawGradientRect(this.guiLeft + 75 + 209, this.guiTop + 160 + 28, this.guiLeft + 75, this.guiTop + 160, 369098751, 369098751);
                  boxLeft = 12763842;
                  i = 12763842;
                  this.sendId = this.openItem;
               } else {
                  this.drawGradientRect(this.guiLeft + 75 + 209, this.guiTop + 160 + 28, this.guiLeft + 75, this.guiTop + 160, 285212671, 285212671);
               }

               this.fontRendererObj.drawString("Забрать из коллекции на этот вайп", this.guiLeft + 84, this.guiTop + 165, i);
               this.drawStringWhenNSSize("Потратить 10 эссенции", 0.95F, 121, 175, boxLeft);
               boxRight = 7960953;
               if (xx > 160 && xx < 196 && yy > 190 && yy < 205) {
                  boxRight = 12763842;
                  this.sendId = -2;
               }

               this.fontRendererObj.drawString("Назад", this.guiLeft + 165, this.guiTop + 195, boxRight);
               RenderHelper.disableStandardItemLighting();
            } else {
               this.openItem = -1;
            }
         } else {
            List<GuiCollections.Collections> itemsList = ((GuiCollections.Category)catList.get(selectCategory)).items;
            if (itemsList != null && ((List)itemsList).size() > 0) {
               this.drawGradientRect(this.guiLeft + 208 + 87, this.guiTop + 23 + 15, this.guiLeft + 208, this.guiTop + 23, 1343690519, 1343690519);
               if (this.searchField != null && this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
                  this.fontRendererObj.drawString("Поиск...", this.guiLeft + 213, this.guiTop + 27, 9408399);
               }

               size = ((List)itemsList).size();
               List<GuiCollections.Collections> collectionsArrayList = new ArrayList();
               if (this.searchField != null && !this.searchField.getText().isEmpty()) {
                  String textSearch = this.searchField.getText();

                  for(int je = 0; je < size; ++je) {
                     GuiCollections.Collections item = (GuiCollections.Collections)((List)itemsList).get(je);
                     if (item.itemStack.getDisplayName().toLowerCase().contains(textSearch.toLowerCase())) {
                        collectionsArrayList.add(item);
                     }
                  }

                  if (collectionsArrayList.isEmpty()) {
                     this.fontRendererObj.drawString("Поиск не дал результата", this.guiLeft + 114, this.guiTop + 135, 9408399);
                     if (this.searchField != null) {
                        this.searchField.drawTextBox();
                     }

                     return;
                  }
               }

               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               RenderHelper.enableGUIStandardItemLighting();
               GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 45, 340, 178);
               itemHover = null;
               boxLeft = 0;
               boxRight = 0;
               if (!collectionsArrayList.isEmpty()) {
                  itemsList = collectionsArrayList;
                  size = collectionsArrayList.size();
               }

               if (size <= 35) {
                  this.guiMapY = 0;
               }

               int i1;
               for(i = 0; i < size; ++i) {
                  int l2 = 75 + 32 * boxLeft;
                  i1 = 55 + boxRight + this.guiMapY;
                  if ((i + 1) % 7 == 0) {
                     boxRight += 30;
                     boxLeft = 0;
                  } else {
                     ++boxLeft;
                  }

                  if (i1 > this.maxHeight) {
                     this.maxHeight = i1 - this.guiMapY - 195;
                  }

                  if (i1 <= 225 && i1 >= 20) {
                     boolean isFullHover = xx > l2 - 4 && xx < l2 + 23 && yy > i1 - 4 && yy < i1 + 21;
                     if (yy > 224 || yy < 45) {
                        isFullHover = false;
                     }

                     GuiCollections.Collections item = (GuiCollections.Collections)((List)itemsList).get(i);
                     GL11.glDisable(2896);
                     GL11.glEnable(3042);
                     if (item.isDateLock()) {
                        GL11.glColor4f(1.0F, 0.5F, 0.5F, isFullHover ? 1.0F : 0.6F);
                     } else {
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover ? 1.0F : 0.5F);
                     }

                     this.mc.getTextureManager().bindTexture(resource);
                     this.drawTexturedModalRect(l2 + this.guiLeft - 7, i1 + this.guiTop - 6, 120, 170, 30, 28);
                     GL11.glEnable(2896);
                     GL11.glEnable(32826);
                     GL11.glPushAttrib(1048575);
                     itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), item.itemStack, l2 + this.guiLeft, i1 + this.guiTop);
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
                  List list = itemHover.itemStack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

                  for(i1 = 0; i1 < list.size(); ++i1) {
                     if (i1 == 0) {
                        list.set(0, itemHover.itemStack.getRarity().rarityColor + "" + list.get(0));
                     } else {
                        list.set(i1, "§7" + list.get(i1));
                     }
                  }

                  if (itemHover.isDateLock()) {
                     list.add("§4Вы уже получили предмет в этом вайпе");
                  } else {
                     this.sendId = itemHover.id;
                  }

                  this.drawHoveringText(list, x + 12, y + 12);
               }

               if (this.searchField != null) {
                  this.searchField.drawTextBox();
               }

            } else {
               this.fontRendererObj.drawString("В коллекции нет ни одного предмета", this.guiLeft + 82, this.guiTop + 135, 9408399);
            }
         }
      } else {
         this.fontRendererObj.drawString("Загрузка списка предметов...", this.guiLeft + 101, this.guiTop + 135, 9408399);
      }
   }

   protected boolean isScrollBox(int x, int y) {
      x -= this.guiLeft;
      y -= this.guiTop;
      return x >= 60 && x < 300 && y >= 45 && y < 240 && this.openItem == -1;
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      this.searchField.mouseClicked(x, y, button);
   }

   public void mouseMovedOrUp(int mouseX, int mouseY, int which) {
      if (which == 0 && !this.mouseMoving) {
         if (this.sendId == -2) {
            this.guiMapY = 0;
            this.mouseY = mouseY;
            this.openItem = -1;
         } else if (this.sendId >= 0 && this.sendId < 5) {
            if (selectCategory == this.sendId) {
               return;
            }

            this.maxHeight = 0;
            this.guiMapY = 0;
            this.mouseY = mouseY;
            selectCategory = this.sendId;
            this.pushSound();
         } else if (this.sendId != -1) {
            if (this.openItem == -1) {
               this.openItem = this.sendId;
            } else {
               NetworkManager.sendToServer(new PacketCollections(this.sendId));
            }

            this.pushSound();
         }
      }

      super.mouseMovedOrUp(mouseX, mouseY, which);
   }

   public void keyTyped(char character, int key) {
      if (key == this.mc.gameSettings.keyBindInventory.getKeyCode() && this.searchField.isFocused()) {
         key = -1;
      }

      super.keyTyped(character, key);
      if (this.searchField != null && this.searchField.isFocused()) {
         this.searchField.textboxKeyTyped(character, key);
      }

   }

   public void updateScreen() {
      super.updateScreen();
      this.searchField.updateCursorCounter();
   }

   public void onGuiClosed() {
      super.onGuiClosed();
      Keyboard.enableRepeatEvents(false);
   }

   private static final class AchievementsLoadTask implements Runnable {
      private static boolean isLoad = false;
      private final String username;

      public AchievementsLoadTask(String username) {
         this.username = username;
      }

      public void run() {
         if (!isLoad) {
            isLoad = true;

            try {
               URL url = new URL(String.format("http://letragon.ru/bin/api/cjkdidasoif.php?username=%s", this.username));
               String json = Resources.asCharSource(url, Charsets.UTF_8).read();
               if (!StringUtils.isNullOrEmpty(json)) {
                  GuiCollections.catList = (List)(new Gson()).fromJson(json, GuiCollections.type);
                  Iterator var3 = GuiCollections.catList.iterator();

                  while(true) {
                     GuiCollections.Category category;
                     do {
                        if (!var3.hasNext()) {
                           ((GuiCollections.Category)GuiCollections.catList.get(2)).items.removeIf((collectionsx) -> {
                              return collectionsx.itemStack == null;
                           });
                           return;
                        }

                        category = (GuiCollections.Category)var3.next();
                     } while(category.items == null);

                     Iterator var5 = category.items.iterator();

                     while(var5.hasNext()) {
                        GuiCollections.Collections collections = (GuiCollections.Collections)var5.next();
                        String[] itemString = collections.item_data.split(":");
                        ItemStack stack = GameRegistry.findItemStack(itemString[0], itemString[1], 1);
                        if (stack != null) {
                           stack.setItemDamage(itemString.length > 2 ? Integer.parseInt(itemString[2]) : 0);
                           collections.itemStack = stack;
                        }
                     }
                  }
               }
            } catch (IOException var12) {
               var12.printStackTrace();
            } finally {
               isLoad = false;
            }

         }
      }
   }

   public static class Collections {
      public int id;
      public int counttake;
      public String item_data;
      public ItemStack itemStack;

      public boolean isDateLock() {
         return this.counttake == 1;
      }

      public void setDateLock() {
         this.counttake = 1;
      }
   }

   public static class Category {
      public String name;
      public int id;
      public List<GuiCollections.Collections> items;
   }
}
