package ru.letitems.client.gui.screen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.GuiGlobal;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.gui.inventory.InventoryTab;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.LetItems;

@SideOnly(Side.CLIENT)
public final class GuiSkills extends GuiPreRenderScreen {
   private static final ResourceLocation bg = new ResourceLocation("letitems", "textures/gui/gui-skills.png");
   private int scale;
   private int mouseY = 0;
   private int guiMapY;
   private int isMouseButtonDown = 0;
   private int idHover = -1;
   private int idLevel = 0;

   public GuiSkills(EntityPlayer player) {
   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.idHover = -1;
      this.idLevel = 0;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(bg);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      int fsdfdsf = 38 * (-this.guiMapY / 43);
      fsdfdsf = MathHelper.clamp_int(fsdfdsf, 20, 185);
      boolean isScrollBox = xx >= 20 && xx < 300 && yy >= 38 && yy < 232;
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

      int i1;
      if (isScrollBox) {
         i1 = Mouse.getDWheel();
         if (i1 < 0) {
            this.guiMapY -= 8;
         } else if (i1 > 0) {
            this.guiMapY += 8;
         }
      }

      if (this.guiMapY > 0) {
         this.isMouseButtonDown = 0;
         --this.guiMapY;
      }

      if (this.guiMapY < -260) {
         this.isMouseButtonDown = 0;
         ++this.guiMapY;
      }

      GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft, this.guiTop + 42, 640, 185);
      String progress = "";

      for(int i = 0; i < GuiGlobal.skillsList.size(); ++i) {
         GuiGlobal.Skills sk = (GuiGlobal.Skills)GuiGlobal.skillsList.get(i);
         int l2 = 35 + this.guiLeft;
         i1 = this.guiTop + 50 + 40 * i + this.guiMapY;
         if (i1 <= 372 && i1 >= 30) {
            boolean hover = x > l2 - 6 && x < l2 + 250 && y > i1 - 5 && y < i1 + 35;
            if (yy > 230 || yy < 39) {
               hover = false;
            }

            this.drawGradientRect(l2 + 248, i1 + 35, l2 - 4, i1 - 4, hover ? 634112971 : 277975441, hover ? 550226891 : 277975441);
            this.fontRendererObj.drawString(sk.getName(), l2 + 40, i1 + 5, 13816530);
            this.drawStringWhenNSSize(sk.getLevel() + " уровень", 0.5F, l2 - this.guiLeft + 40, i1 - this.guiTop + 23, 8618883);
            if (sk.getLevel() >= 50) {
               this.drawGradientRect(l2 + 40 + 202, i1 + 2 + 17, l2 + 40, i1 + 17, 1087097803, 1087097803);
            } else {
               float parse = sk.getPercent() != null ? Float.parseFloat(sk.getPercent()) : 100.0F;
               int percent = (int)((100.0F - parse) / 100.0F * 202.0F);
               this.drawGradientRect(l2 + 40 + 202, i1 + 2 + 17, l2 + 40, i1 + 17, 546410897, 546410897);
               this.drawGradientRect(l2 + 40 + percent, i1 + 2 + 17, l2 + 40, i1 + 17, 818662347, 818662347);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GeneralClientUtils.bind("letitems:textures/gui/gui-skills-mt.jpg");
            this.renderSkills(l2 + 2, i1, (float)(98 * i), i >= 6 ? 98.0F : 0.0F, 98, 98, 0.32F);
            if (i == 0) {
               this.renderSkillBonus(1, sk.getLevel(), x, y, l2, i1, 0.0F, 197.0F);
               this.renderSkillBonus(2, sk.getLevel(), x, y, l2 - 13, i1, 32.0F, 197.0F);
               this.renderSkillBonus(31, sk.getLevel(), x, y, l2 - 26, i1, 128.0F, 229.0F);
            } else {
               byte jj;
               int jj;
               int var10002;
               if (i == 1) {
                  jj = 0;
                  var10002 = sk.getLevel();
                  jj = jj + 1;
                  this.renderSkillBonus(3, var10002, x, y, l2 - jj * 13, i1, 0.0F, 197.0F);
                  this.renderSkillBonus(4, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 32.0F, 197.0F);
                  this.renderSkillBonus(5, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 512.0F, 197.0F);
                  if (LetItems.loadThaumCraft) {
                     this.renderSkillBonus(6, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 64.0F, 197.0F);
                  }

                  this.renderSkillBonus(32, sk.getLevel(), x, y, l2 - jj * 13, i1, 128.0F, 229.0F);
               } else if (i == 2) {
                  this.renderSkillBonus(7, sk.getLevel(), x, y, l2, i1, 128.0F, 197.0F);
                  this.renderSkillBonus(8, sk.getLevel(), x, y, l2 - 13, i1, 256.0F, 197.0F);
                  this.renderSkillBonus(9, sk.getLevel(), x, y, l2 - 26, i1, 288.0F, 197.0F);
                  this.renderSkillBonus(33, sk.getLevel(), x, y, l2 - 39, i1, 128.0F, 229.0F);
               } else if (i == 3) {
                  this.renderSkillBonus(10, sk.getLevel(), x, y, l2, i1, 160.0F, 197.0F);
                  this.renderSkillBonus(11, sk.getLevel(), x, y, l2 - 13, i1, 96.0F, 197.0F);
                  this.renderSkillBonus(34, sk.getLevel(), x, y, l2 - 26, i1, 128.0F, 229.0F);
               } else if (i == 4) {
                  jj = 0;
                  var10002 = sk.getLevel();
                  jj = jj + 1;
                  this.renderSkillBonus(12, var10002, x, y, l2 - jj * 13, i1, 192.0F, 197.0F);
                  this.renderSkillBonus(13, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 320.0F, 197.0F);
                  if (LetItems.loadThaumCraft) {
                     this.renderSkillBonus(14, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 64.0F, 197.0F);
                  }

                  this.renderSkillBonus(35, sk.getLevel(), x, y, l2 - jj * 13, i1, 128.0F, 229.0F);
               } else if (i == 5) {
                  this.renderSkillBonus(15, sk.getLevel(), x, y, l2, i1, 352.0F, 197.0F);
                  this.renderSkillBonus(16, sk.getLevel(), x, y, l2 - 13, i1, 224.0F, 197.0F);
                  this.renderSkillBonus(17, sk.getLevel(), x, y, l2 - 26, i1, 384.0F, 197.0F);
                  this.renderSkillBonus(36, sk.getLevel(), x, y, l2 - 39, i1, 128.0F, 229.0F);
               } else if (i == 6) {
                  this.renderSkillBonus(18, sk.getLevel(), x, y, l2, i1, 416.0F, 197.0F);
                  this.renderSkillBonus(19, sk.getLevel(), x, y, l2 - 13, i1, 448.0F, 197.0F);
                  this.renderSkillBonus(37, sk.getLevel(), x, y, l2 - 26, i1, 128.0F, 229.0F);
               } else if (i == 7) {
                  jj = 0;
                  var10002 = sk.getLevel();
                  jj = jj + 1;
                  this.renderSkillBonus(20, var10002, x, y, l2 - jj * 13, i1, 480.0F, 197.0F);
                  this.renderSkillBonus(21, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 0.0F, 229.0F);
                  this.renderSkillBonus(22, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 224.0F, 229.0F);
                  if (LetItems.loadThaumCraft) {
                     this.renderSkillBonus(23, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 64.0F, 197.0F);
                  }

                  if (LetItems.loadEP) {
                     this.renderSkillBonus(24, sk.getLevel(), x, y, l2 - jj++ * 13, i1, 32.0F, 228.0F);
                  }

                  this.renderSkillBonus(38, sk.getLevel(), x, y, l2 - jj * 13, i1, 128.0F, 229.0F);
               } else if (i == 8) {
                  this.renderSkillBonus(25, sk.getLevel(), x, y, l2, i1, 544.0F, 197.0F);
                  this.renderSkillBonus(26, sk.getLevel(), x, y, l2 - 13, i1, 160.0F, 229.0F);
                  this.renderSkillBonus(27, sk.getLevel(), x, y, l2 - 26, i1, 192.0F, 229.0F);
                  this.renderSkillBonus(39, sk.getLevel(), x, y, l2 - 39, i1, 128.0F, 229.0F);
               } else if (i == 9) {
                  this.renderSkillBonus(28, sk.getLevel(), x, y, l2, i1, 64.0F, 228.0F);
                  this.renderSkillBonus(29, sk.getLevel(), x, y, l2 - 13, i1, 96.0F, 228.0F);
                  if (LetItems.loadThaumCraft) {
                     this.renderSkillBonus(30, sk.getLevel(), x, y, l2 - 26, i1, 64.0F, 197.0F);
                  }
               } else if (i == 10) {
                  this.renderSkillBonus(40, sk.getLevel(), x, y, l2, i1, 96.0F, 228.0F);
               }
            }

            if (hover && x > l2 - 2 && x < l2 + 35 && y > i1 - 2 && y < i1 + 31) {
               progress = sk.progress;
            }
         }
      }

      GeneralClientUtils.ScissorHelper.endScissor();
      if (!progress.equals("")) {
         this.drawHoveringText("Прокачивается при " + progress, x, y);
      }

      this.bonusInfo(x, y);
      this.drawGradientRect(this.guiLeft + 289 + 3, this.guiTop + 42 + 185, this.guiLeft + 289, this.guiTop + 42, 550226891, 550226891);
      this.drawGradientRect(this.guiLeft + 289 + 3, this.guiTop + 42 + fsdfdsf, this.guiLeft + 289, this.guiTop + 42 + fsdfdsf - 20, 1623968715, 1623968715);
      this.drawStringWhenNSSize("Назад в инвентарь", 0.75F, 21, 27, xx > 18 && xx < 105 && yy > 24 && yy < 35 ? -1 : 4802889);
   }

   private void renderSkills(int sizeX, int sizeY, float posX, float posY, int width, int height, float scale) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)sizeX, (float)sizeY, 1.0F);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      float f4 = 0.0016992353F;
      float f5 = 0.0038387715F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(0.0F * scale), (double)((float)height * scale), 0.0D, (double)(posX * 0.0016992353F), (double)((posY + (float)height) * 0.0038387715F));
      tessellator.addVertexWithUV((double)((float)width * scale), (double)((float)height * scale), 0.0D, (double)((posX + (float)width) * 0.0016992353F), (double)((posY + (float)height) * 0.0038387715F));
      tessellator.addVertexWithUV((double)((float)width * scale), (double)(0.0F * scale), 0.0D, (double)((posX + (float)width) * 0.0016992353F), (double)(posY * 0.0038387715F));
      tessellator.addVertexWithUV((double)(0.0F * scale), (double)(0.0F * scale), 0.0D, (double)(posX * 0.0016992353F), (double)(posY * 0.0038387715F));
      tessellator.draw();
      GL11.glPopMatrix();
   }

   private void renderSkillBonus(int id, int level, int x, int y, int l2, int i1, float posX, float posY) {
      l2 += 232;
      i1 += 4;
      boolean hover = x > l2 - 2 && x < l2 + 11 && y > i1 - 3 && y < i1 + 10;
      if (hover) {
         this.drawGradientRect(l2 + 11, i1 + 11, l2 - 1, i1 - 1, -2130706433, -2130706433);
         this.idHover = id;
         this.idLevel = level;
      }

      this.renderSkills(l2, i1, posX + 1.0F, posY, 32, 32, 0.32F);
   }

   private void bonusInfo(int x, int y) {
      if (this.idHover != -1 && y - this.guiTop <= 230 && y - this.guiTop >= 39) {
         int id = this.idHover;
         int level = this.idLevel;
         String list = "";
         if (id == 1) {
            list = "Шанс получения дополнительного#кристалла Артефэ §6" + (float)level / 20.0F + "%";
         } else if (id == 2) {
            list = "Шанс получения осколка Артефэ §6" + (float)level / 5.0F + "%";
         } else if (id == 3) {
            list = "Шанс получения кристалла Артефэ §6" + (float)level * 0.4F + "%";
         } else if (id == 4) {
            list = "Шанс получения осколка Артефэ §6" + (float)level * 0.1F + "%";
         } else if (id == 5) {
            list = "Шанс получения дополнительных#ресурсов §6" + (level < 5 ? 0.0F : (float)level * 0.1F) + "%";
         } else if (id == 6) {
            list = "§5[ThaumCraft] §rШанс получения#аспекта §6" + (float)level * 0.2F + "%";
         } else if (id == 7) {
            list = "Снижение урона от монстров на §6" + (float)level * 0.5F + "%";
         } else if (id == 8) {
            list = "Снижение урона от игроков#и стрел на §6" + (float)level * 0.3F + "%";
         } else if (id == 9) {
            list = "Снижение урона от магических#атак на §6" + (float)level * 0.16F + "%";
         } else if (id == 10) {
            list = "Снижение урона от горения#и огня на §6" + (float)level * 0.6F + "%";
         } else if (id == 11) {
            list = "Снижение урона от лавы на §6" + (float)level * 0.4F + "%";
         } else if (id == 12) {
            list = "Шанс получения дополнительных#ресурсов §6" + (float)level * 0.3F + "%";
         } else if (id == 13) {
            list = "Шанс получения удвоенного дропа §6" + (float)level * 0.1F + "%";
         } else if (id == 14) {
            list = "§5[ThaumCraft] §rШанс получения#аспекта §6" + (float)level / 25.0F + "%";
         } else if (id == 15) {
            list = "Урон по монстрам увеличен на §6" + (float)level * 0.4F + "%";
         } else if (id == 16) {
            list = "Урон по боссам увеличен на §6" + (float)level * 0.3F + "%";
         } else if (id == 17) {
            list = "Восстановление §420%§r HP#от нанесенного урона#с шансом §6" + (level + 15) + "%";
         } else if (id == 18) {
            list = "Повышает время действия#хороших эффектов на §6" + (float)level * 0.45F + "%";
         } else if (id == 19) {
            list = "Уменьшает время действия#плохих эффектов на §6" + (float)level * 0.8F + "%";
         } else if (id == 20) {
            list = "Снижение опыта для#Стола Зачарований на §6" + (float)level * 1.5F + "%";
         } else if (id == 21) {
            list = "Снижение опыта для#Наковальни на §6" + (float)level * 0.8F + "%";
         } else if (id == 22) {
            list = "Скидка в Кузнице#(letragon.ru/store/smithy) на §6" + level / 5 + "%";
         } else if (id == 23) {
            list = "§5[ThaumCraft] §rШанс получения#аспекта §6" + (float)level * 0.4F + "%";
         } else if (id == 24) {
            list = "§5[Enchanting Plus] §rСнижение уровней#и монет на §6" + (float)level * 0.8F + "%";
         } else if (id == 25) {
            list = "Увеличение показателей#ценности еды на §6" + (float)level * 3.0F + "%";
         } else if (id == 26) {
            list = "Снижение увеличения истощения на §6" + level + "%";
         } else if (id == 27) {
            list = "Снижение увеличения усталости#персонажа на §6" + (float)level * 0.3F + "%";
         } else if (id == 28) {
            list = "Увеличение урожая на 1-3#с шансом §6" + (float)level * 0.5F + "%";
         } else if (id == 29) {
            list = "Получение §61-" + (level / 5 + 1) + "§r опыта#при сборе урожая с шансом §6" + level + "%";
         } else if (id == 30) {
            list = "§5[ThaumCraft] §rШанс получения#аспекта §6" + (float)level / 5.0F + "%";
         } else if (id == 31) {
            list = "§6Бонусы на максимальном уровне§r#* Гарантированное получение осколка Артефэ";
         } else if (id == 32) {
            list = "§6Бонусы на максимальном уровне§r#* Удочка получает +3 уровня к зачарованиям#'Морская удача' и 'Приманка'#* Удочка больше не теряет прочность#при использовании поплавка";
         } else if (id == 33) {
            list = "§6Бонусы на максимальном уровне§r#* Шанс 20%, что урон может быть пропущен#* Не даёт умереть, при получении смертельного#урона от монстров. Может возникнуть раз в 40 минут";
         } else if (id == 34) {
            list = "§6Бонусы на максимальном уровне§r#* Игнорирование урона от лавы#* Шанс 20%, что урон может быть пропущен";
         } else if (id == 35) {
            list = "§6Бонусы на максимальном уровне§r#* Добыча руды восстанавливает запас сытости персонажа";
         } else if (id == 36) {
            list = "§6Бонусы на максимальном уровне§r#* Восстановление здоровья происходит со 100% шансом#* Атака может нанести критический удар с множителем §6x1.8";
         } else if (id == 37) {
            list = "§6Бонусы на максимальном уровне§r#* Иммунитет к эффектам - отравление, тошнота и иссушение";
         } else if (id == 38) {
            list = "§6Бонусы на максимальном уровне§r#* Максимальный заряд без использования книжных полок#* Позволяет чинить предмет при наложении чар";
         } else if (id == 39) {
            list = "§6Бонусы на максимальном уровне§r#* §5[ThaumCraft] §rПеченье убирает временное искажение";
         } else if (id == 40) {
            list = "Увеличение получаемого опыта на §6" + (float)level * 0.9F + "%";
         }

         this.drawHoveringText(list, x, y);
      }
   }

   private void drawHoveringText(String list, int x, int y) {
      if (!list.isEmpty()) {
         GL11.glDisable(2929);
         int k = 0;
         String[] lines = list.split("#");
         String[] var6 = lines;
         int k2 = lines.length;

         int i1;
         for(i1 = 0; i1 < k2; ++i1) {
            String s = var6[i1];
            int l = this.fontRendererObj.getStringWidth(s);
            if (l > k) {
               k = l;
            }
         }

         int j2 = x + 22;
         k2 = y - 2;
         i1 = 8;
         if (lines.length > 1) {
            i1 += 2 + (lines.length - 1) * 10;
         }

         if (j2 + k > this.width) {
            j2 -= 28 + k;
         }

         if (k2 + i1 + 6 > this.height) {
            k2 = this.height - i1 - 6;
         }

         int j1 = -1726803181;
         int radius = 7;
         this.drawGradientRect(j2 - radius - 1, k2 - radius - 1, j2 + k + radius + 1, k2 + i1 + radius + 1, 695695223, 695695223);
         this.drawGradientRect(j2 - radius, k2 - radius, j2 + k + radius, k2 + i1 + radius, j1, j1);
         String[] var11 = lines;
         int var12 = lines.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            String text = var11[var13];
            this.fontRendererObj.drawString(text, j2, k2, 15329769);
            k2 += 10;
         }
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      x -= this.guiLeft;
      y -= this.guiTop;
      if (x > 18 && x < 105 && y > 24 && y < 35) {
         InventoryTab.inventoryTab.openInventoryId(1, false);
      }

   }
}
