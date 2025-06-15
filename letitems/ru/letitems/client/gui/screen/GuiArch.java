package ru.letitems.client.gui.screen;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.LetSettings;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.gui.inventory.InventoryTab;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.client.util.LoadAndRenderPic;

@SideOnly(Side.CLIENT)
public final class GuiArch extends GuiPreRenderScreen {
   private static final ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(1003), (RenderingHints)null);
   public static List<GuiArch.Category> catList = new ArrayList();
   private static int selectCategory = -1;
   private int scale;
   private int sendId;
   private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
   private static Type type = (new TypeToken<List<GuiArch.Category>>() {
   }).getType();

   public GuiArch(EntityPlayer player) {
      this.xSize = 480;
      this.ySize = 245;
      this.scrollMouse = true;
      this.scrollWheel = true;
      this.sendId = -1;
      this.maxHeight = 0;
      selectCategory = -1;
      EXECUTOR_SERVICE.submit(new GuiArch.AchievementsLoadTask(player.getDisplayName()));
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
      GeneralClientUtils.bind("letitems:textures/gui/gui-arch.png");
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      if (catList != null && catList.size() > 0) {
         this.drawStringWhenNSSize("Достижения", 1.5F, 24, 20, 13290186);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         String hoverText = null;
         GuiArch.Achievements itemHover = null;
         int pan;
         int j;
         int posX;
         int posY;
         int i1;
         if (selectCategory == -1) {
            boolean hoverBack = xx > 19 && xx < 58 && yy > 30 && yy < 41;
            this.fontRendererObj.drawString("Назад", this.guiLeft + 24, this.guiTop + 32, hoverBack ? -1 : 6842472);
            if (hoverBack) {
               this.sendId = -3;
            }

            this.fontRendererObj.drawString(" / ", this.guiLeft + 53, this.guiTop + 32, 6842472);
            this.fontRendererObj.drawString("Категории достижений сервера", this.guiLeft + 68, this.guiTop + 32, 9408399);
            GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 50, 450, 177);
            int sizeCategory = catList.size();
            if (sizeCategory <= 6) {
               this.guiMapY = 0;
            }

            int boxLeft = 0;
            pan = 0;

            for(j = 0; j < sizeCategory; ++j) {
               posX = 38 + this.guiLeft + 139 * boxLeft;
               posY = 60 + this.guiTop + pan + this.guiMapY;
               if ((j + 1) % 3 == 0) {
                  pan += 76;
                  boxLeft = 0;
               } else {
                  ++boxLeft;
               }

               if (posY > this.maxHeight) {
                  this.maxHeight = posY - this.guiMapY - 195;
               }

               boolean hover = x > posX && x < posX + 127 && y > posY && y < posY + 66;
               if (yy > 227 || yy < 45) {
                  hover = false;
               }

               if (hover) {
                  this.sendId = j;
               }

               GuiArch.Category cat = (GuiArch.Category)catList.get(j);
               if (!hover) {
                  this.drawGradientRect(posX + 125, posY + 64, posX + 1, posY + 1, 695695223, 695695223);
               }

               this.drawGradientRect(posX + 126, posY + 65, posX, posY, hover ? -1995238637 : 1343427347, hover ? -1995238637 : 1343427347);
               i1 = this.fontRendererObj.getStringWidth(cat.name);
               this.drawStringWhenNSSize(cat.name, Math.min(100.0F / (float)i1, 1.25F), posX - this.guiLeft + 12, posY - this.guiTop + 23, 10461087);
               this.fontRendererObj.drawString("Прогресс " + (int)((float)cat.unlocks / (float)cat.achievements.size() * 100.0F) + "%", posX + 12, posY + 37, 9013641);
            }
         } else {
            GuiArch.Category cat = (GuiArch.Category)catList.get(selectCategory);
            boolean hover = xx > 19 && xx < 130 && yy > 30 && yy < 41;
            this.fontRendererObj.drawString("К списку категорий", this.guiLeft + 24, this.guiTop + 32, hover ? -1 : 6842472);
            if (hover) {
               this.sendId = -2;
            }

            this.fontRendererObj.drawString(" / ", this.guiLeft + 126, this.guiTop + 32, 6842472);
            this.fontRendererObj.drawString(cat.name, this.guiLeft + 140, this.guiTop + 32, 9408399);
            float v = 0.75F;

            for(pan = 1; pan <= 2; ++pan) {
               j = pan == 2 ? 444 : 426;
               boolean hoverPan = xx > j - 2 && xx < j + 14 && yy > 23 && yy < 38;
               if (!hoverPan && this.modSettings.otherArchType + 1 != pan) {
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.25F);
               } else {
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
                  if (hoverPan) {
                     this.sendId = pan == 1 ? -4 : -5;
                  }
               }

               GeneralClientUtils.bind("letitems:textures/gui/utils/ach-pan-" + pan + ".png");
               GL11.glPushMatrix();
               GL11.glTranslated((double)(this.guiLeft + j), (double)(this.guiTop + 25), 0.0D);
               GL11.glScalef(0.75F, 0.75F, 0.75F);
               func_146110_a(0, 1, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
               GL11.glPopMatrix();
            }

            GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 50, 450, 175);
            List<GuiArch.Achievements> itemsList = cat.achievements;
            posX = itemsList.size();
            boolean isFullHover;
            int i;
            if (this.modSettings.otherArchType == 0) {
               if (posX <= 4) {
                  this.guiMapY = 0;
               }

               posY = 0;

               for(i = 0; i < posX; ++i) {
                  int l2 = 40;
                  i1 = 60 + 45 * i + this.guiMapY + posY;
                  if (i1 > this.maxHeight) {
                     this.maxHeight = i1 - this.guiMapY - 165 - posY + 8;
                  }

                  if (i1 <= 225 && i1 >= 11) {
                     GuiArch.Achievements item = (GuiArch.Achievements)itemsList.get(i);
                     int syzeW = 0;
                     if (!item.isUnlock() && !item.progress.isEmpty()) {
                        syzeW += 8;
                        posY += 8;
                     }

                     isFullHover = xx > l2 - 3 && xx < l2 + 401 && yy > i1 - 4 && yy < i1 + 41 + syzeW;
                     if (yy > 224 || yy < 45) {
                        isFullHover = false;
                     }

                     GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover ? 1.0F : 0.75F);
                     if (!isFullHover) {
                        this.drawGradientRect(l2 + this.guiLeft + 400 - 1, i1 + this.guiTop + 40 - 1 + syzeW, l2 + this.guiLeft + 1, i1 + this.guiTop + 1, 695695223, 695695223);
                     }

                     this.drawGradientRect(l2 + this.guiLeft + 400, i1 + this.guiTop + 40 + syzeW, l2 + this.guiLeft, i1 + this.guiTop, isFullHover ? -1726803181 : 1343427347, isFullHover ? -1726803181 : 1343427347);
                     item.loadPic();
                     LoadAndRenderPic.instance.renderPic(item.isUnlock() ? null : op, "gui", item.getImage(), 1.0F, l2 + this.guiLeft + 1, i1 + this.guiTop, isFullHover ? 1.0F : 0.75F);
                     int titleWidth = this.fontRendererObj.getStringWidth(item.name);
                     this.drawStringWhenNSSize(item.name, Math.min(120.0F / (float)titleWidth, 1.25F), l2 + 43, i1 + 13, 13684944);
                     int titleWidth2 = this.fontRendererObj.getStringWidth(item.details);
                     this.drawStringWhenNSSize(item.details, Math.min(345.0F / (float)titleWidth2, 1.0F), l2 + 43, i1 + 23, 9013641);
                     if (item.isUnlock()) {
                        GeneralClientUtils.bind("letitems:textures/gui/ach-flep.png");
                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
                        GL11.glDisable(3008);
                        GL11.glEnable(3042);
                        GL11.glDisable(2884);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                        GL11.glTranslated((double)(l2 + this.guiLeft + 400 - 68), (double)(i1 + this.guiTop), 0.0D);
                        func_146110_a(0, 1, 0.0F, 0.0F, 67, 38, 67.0F, 38.0F);
                        GL11.glDisable(3042);
                        GL11.glEnable(3008);
                        GL11.glPopMatrix();
                     }

                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     GeneralClientUtils.bind("letitems:textures/gui/ach-border.png");
                     GL11.glPushMatrix();
                     GL11.glTranslated((double)(l2 + this.guiLeft + 355), (double)(i1 + this.guiTop + 8), 0.0D);
                     float scale = 0.38F;
                     GL11.glScalef(0.38F, 0.38F, 0.38F);
                     func_146110_a(0, 1, 0.5F, 0.5F, 26, 28, 26.0F, 28.0F);
                     GL11.glPopMatrix();
                     this.drawStringWhenNSSize(item.points + "", 0.75F, l2 + 368, i1 + 12, 6842472);
                     if (!item.reward.isEmpty()) {
                        boolean hoverReward = xx > l2 + 341 && xx < l2 + 353 && yy > i1 + 2 && yy < i1 + 22;
                        GL11.glPushMatrix();
                        GL11.glTranslated((double)(l2 + this.guiLeft + 348), (double)(i1 + this.guiTop + 8), 0.0D);
                        GL11.glRotatef(90.0F, 1.0F, 1.0F, 1.0F);
                        this.drawGradientRect(8, 15, 0, 0, hoverReward ? -2139654281 : 1081571191, hoverReward ? -2139654281 : 1081571191);
                        GL11.glPopMatrix();
                        if (hoverReward) {
                           hoverText = "Награда: " + item.reward;
                        }
                     }

                     if (syzeW > 0) {
                        String[] progress = item.progress.split(":");
                        if (NumberUtils.isNumber(progress[0])) {
                           int percent = (int)(Float.parseFloat(progress[1]) / Float.parseFloat(progress[0]) * 334.0F);
                           this.drawGradientRect(l2 + this.guiLeft + 43 + 334, i1 + this.guiTop + 2 + 35, l2 + this.guiLeft + 43, i1 + this.guiTop + 35, 546410897, 546410897);
                           this.drawGradientRect(l2 + this.guiLeft + 43 + percent, i1 + this.guiTop + 2 + 35, l2 + this.guiLeft + 43, i1 + this.guiTop + 35, 818662347, 818662347);
                           if (xx > l2 + 40 && xx < l2 + 336 + 40 && yy > i1 + 31 && yy < i1 + 39) {
                              hoverText = String.format("Выполнено на %s/%s", progress[1], progress[0]);
                           }
                        } else {
                           String[] listNeed = progress[0].split(",");
                           String[] listPr = progress[1].split("");
                           int bos = 0;

                           for(int j = 0; j < listNeed.length; ++j) {
                              boolean hover2 = xx > l2 + 40 + bos && xx < l2 + 49 + bos && yy > i1 + 34 && yy < i1 + 41;
                              int colorNeed = hover2 ? 1351717265 : 546410897;
                              if (listPr.length > 1 && listPr[j].equals("1")) {
                                 colorNeed = 1972356493;
                              }

                              if (hover2) {
                                 this.drawGradientRect(l2 + this.guiLeft + 43 + 4 + bos, i1 + this.guiTop + 4 + 35, l2 + this.guiLeft + 44 + bos, i1 + this.guiTop + 36, colorNeed, colorNeed);
                                 hoverText = listNeed[j];
                              }

                              this.drawGradientRect(l2 + this.guiLeft + 43 + 5 + bos, i1 + this.guiTop + 5 + 35, l2 + this.guiLeft + 43 + bos, i1 + this.guiTop + 35, colorNeed, colorNeed);
                              bos += 10;
                           }
                        }
                     }
                  }
               }
            } else {
               if (posX <= 24) {
                  this.guiMapY = 0;
               }

               posY = 0;
               i = 0;

               for(int i = 0; i < posX; ++i) {
                  i1 = 40 + 51 * posY;
                  int i1 = 60 + this.guiMapY + i;
                  if ((i + 1) % 8 == 0) {
                     i += 50;
                     posY = 0;
                  } else {
                     ++posY;
                  }

                  if (i1 > this.maxHeight) {
                     this.maxHeight = i1 - this.guiMapY - 165;
                  }

                  if (i1 <= 225 && i1 >= 11) {
                     GuiArch.Achievements item = (GuiArch.Achievements)itemsList.get(i);
                     isFullHover = xx > i1 - 3 && xx < i1 + 43 && yy > i1 - 4 && yy < i1 + 41;
                     if (yy > 224 || yy < 45) {
                        isFullHover = false;
                     }

                     GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover ? 1.0F : 0.75F);
                     if (!isFullHover) {
                        this.drawGradientRect(i1 + this.guiLeft + 44 - 1, i1 + this.guiTop + 40 - 1, i1 + this.guiLeft + 1, i1 + this.guiTop + 1, 695695223, 695695223);
                     }

                     this.drawGradientRect(i1 + this.guiLeft + 44, i1 + this.guiTop + 40, i1 + this.guiLeft, i1 + this.guiTop, isFullHover ? -1726803181 : 1343427347, isFullHover ? -1726803181 : 1343427347);
                     item.loadPic();
                     LoadAndRenderPic.instance.renderPic(item.isUnlock() ? null : op, "gui", item.getImage(), 1.0F, i1 + this.guiLeft + 1, i1 + this.guiTop, isFullHover ? 1.0F : 0.75F);
                     if (isFullHover) {
                        itemHover = item;
                     }
                  }
               }
            }
         }

         GeneralClientUtils.ScissorHelper.endScissor();
         if (itemHover != null) {
            ArrayList<String> listText = new ArrayList();
            listText.add("§6" + itemHover.name);
            String[] listNeed;
            String[] listPr;
            if (itemHover.details.length() > 38) {
               String s = itemHover.details.replaceAll("(.{40})", "$1|");
               listNeed = s.split("\\|");
               listPr = listNeed;
               j = listNeed.length;

               for(posX = 0; posX < j; ++posX) {
                  String s1 = listPr[posX];
                  listText.add("§7" + s1);
               }
            } else {
               listText.add("§7" + itemHover.details);
            }

            listText.add("");
            if (!itemHover.isUnlock() && !itemHover.progress.isEmpty()) {
               String[] progress = itemHover.progress.split(":");
               if (NumberUtils.isNumber(progress[0])) {
                  listText.add("§8Прогресс выполнения: §7" + progress[1] + "§8 / §7" + progress[0]);
               } else {
                  listText.add("§6Прогресс:");
                  listNeed = progress[0].split(",");
                  listPr = progress[1].split("");
                  j = 0;

                  while(true) {
                     if (j >= listNeed.length) {
                        listText.add("");
                        break;
                     }

                     if (listPr.length > 1 && listPr[j].equals("1")) {
                        listText.add("§6- §2" + listNeed[j]);
                     } else {
                        listText.add("§6- §7" + listNeed[j]);
                     }

                     ++j;
                  }
               }
            }

            if (!itemHover.reward.isEmpty()) {
               listText.add("§8Награда: §7" + itemHover.reward);
            }

            listText.add("§8Очков активности: §7" + itemHover.points);
            this.drawHoveringText(listText, x + 4, y + 4);
         }

         if (hoverText != null) {
            this.drawHoveringText(Collections.singletonList(hoverText), x + 12, y + 12);
         }

      } else {
         this.fontRendererObj.drawString("Загрузка данных...", this.guiLeft + 195, this.guiTop + 135, 9408399);
      }
   }

   protected boolean isScrollBox(int x, int y) {
      x -= this.guiLeft;
      y -= this.guiTop;
      return x >= 20 && x < 450 && y >= 45 && y < 240;
   }

   public void mouseMovedOrUp(int mouseX, int mouseY, int which) {
      if (which == 0 && !this.mouseMoving) {
         if (this.sendId == -3) {
            InventoryTab.inventoryTab.openInventoryId(1, false);
         } else if (this.sendId != -4 && this.sendId != -5) {
            if (this.sendId == -2) {
               selectCategory = -1;
               this.maxHeight = 0;
               this.guiMapY = 0;
               this.mouseY = mouseY;
            } else if (this.sendId != -1) {
               if (selectCategory == this.sendId) {
                  return;
               }

               this.maxHeight = 0;
               this.guiMapY = 0;
               this.mouseY = mouseY;
               selectCategory = this.sendId;
            }
         } else {
            this.modSettings.setOptionValue(LetSettings.Options.OTHER_ARCH_TYPE, this.sendId == -4 ? 0 : 1);
            this.pushSound();
         }
      }

      super.mouseMovedOrUp(mouseX, mouseY, which);
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
               URL url = new URL(String.format("http://letragon.ru/bin/api/qpockcvjjiqwe.php?username=%s", this.username));
               String json = Resources.asCharSource(url, Charsets.UTF_8).read();
               if (!StringUtils.isNullOrEmpty(json)) {
                  GuiArch.catList = (List)(new Gson()).fromJson(json, GuiArch.type);
               }
            } catch (IOException var6) {
               var6.printStackTrace();
            } finally {
               isLoad = false;
            }

         }
      }
   }

   public static class Achievements {
      public int id;
      public String name;
      public String details;
      public String type;
      public int points;
      public String reward = "";
      public String progress = "";
      public int sorted;
      private boolean view = false;

      public boolean isUnlock() {
         return this.sorted > 0;
      }

      public void loadPic() {
         if (!this.view) {
            this.view = true;
            LoadAndRenderPic.instance.queueResearchInformation(this.getImage(), this.getImage());
         }

      }

      public String getImage() {
         return this.id + "." + this.type;
      }
   }

   public static class Category {
      public String name;
      public List<GuiArch.Achievements> achievements;
      public int unlocks;
   }
}
