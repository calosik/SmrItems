package ru.letitems.client.gui.screen;

import com.emoniph.witchery.common.ExtendedPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collections;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.mods.PacketWitchery;

@SideOnly(Side.CLIENT)
public final class GuiWitchery extends GuiPreRenderScreen {
   private static final ResourceLocation bg = new ResourceLocation("letitems", "textures/gui/gui-witchery.png");
   private final ExtendedPlayer playerEx;
   private static int page = 0;
   private static int selectLevel = -1;
   private int sendId = -1;
   private int sendLvl = -1;
   private GuiWitchery.GuiWitcheryPayerInfo guiWitcheryPayerInfo = null;
   private static GuiWitchery.GuiWitcheryPayerInfo guiWitcheryPayerInfoGlobal = null;
   private static String[][] witcheryQuests = new String[2][9];

   public GuiWitchery(EntityPlayer player) {
      this.playerEx = ExtendedPlayer.get(player);
      selectLevel = -1;
      guiWitcheryPayerInfoGlobal = null;
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(bg);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      this.guiWitcheryPayerInfo = null;
      this.sendId = -1;
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      this.drawGradientRect(this.guiLeft + 18 + 284, this.guiTop + 28 + 200, this.guiLeft + 18, this.guiTop + 28, 279817645, 279817645);
      this.drawGradientRect(this.guiLeft + 18 + 284, this.guiTop + 26 + 1, this.guiLeft + 18, this.guiTop + 26, 548253101, 548253101);
      if (page == 0) {
         this.drawGradientRect(this.guiLeft + 72, this.guiTop + 27, this.guiLeft + 22, this.guiTop + 11, 548253101, 548253101);
      }

      this.fontRendererObj.drawString("Вампир", this.guiLeft + 29, this.guiTop + 15, page == 0 ? 13290186 : 10000536);
      if (page == 1) {
         this.drawGradientRect(this.guiLeft + 139, this.guiTop + 27, this.guiLeft + 73, this.guiTop + 11, 548253101, 548253101);
      }

      this.fontRendererObj.drawString("Оборотень", this.guiLeft + 80, this.guiTop + 15, page == 1 ? 13290186 : 10000536);
      if (page == 2) {
         this.drawGradientRect(this.guiLeft + 200, this.guiTop + 27, this.guiLeft + 140, this.guiTop + 11, 548253101, 548253101);
      }

      this.fontRendererObj.drawString("Магазин", this.guiLeft + 150, this.guiTop + 15, page == 2 ? 13290186 : 10000536);
      if (guiWitcheryPayerInfoGlobal != null) {
         this.drawStringWhenNSSize("Оплата услуги", 2.0F, 86, 85, 13290186);
         String textPayInfo = guiWitcheryPayerInfoGlobal.text;
         this.fontRendererObj.drawString(textPayInfo, this.guiLeft + 159 - this.fontRendererObj.getStringWidth(textPayInfo) / 2, this.guiTop + 108, 10000536);
         int yyy = 20;
         boolean hover = xx > 68 && xx < 248 && yy > 110 + yyy && yy < 138 + yyy;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
         GL11.glEnable(3042);
         this.mc.getTextureManager().bindTexture(resource);
         this.drawTexturedModalRect(this.guiLeft + 68, this.guiTop + 110 + yyy, 0, 142, 184, 28);
         GL11.glDisable(3042);
         this.fontRendererObj.drawString("Оплатить за " + guiWitcheryPayerInfoGlobal.coin + " эссенции", this.guiLeft + 160 - this.fontRendererObj.getStringWidth("Оплатить за " + guiWitcheryPayerInfoGlobal.coin + " эссенции") / 2, this.guiTop + 120 + yyy, 11974326);
         if (hover) {
            this.sendId = guiWitcheryPayerInfoGlobal.id;
         }

      } else {
         int countDownY;
         boolean hover;
         if (page == 0) {
            this.drawStringWhenNSSize("Вампир", 2.0F, 123, 35, 13290186);
            if (this.playerEx.isVampire()) {
               countDownY = this.renderGlobalInfo(this.playerEx.getVampireLevel(), xx, yy, true);
               this.drawStringWhenSize("Зелье Багровых Магов", 1.5F, 73, 198 + countDownY, 13290186);
               if (this.playerEx.isNoSunDeath()) {
                  this.fontRendererObj.drawString("Имеется в этом вайпе", this.guiLeft + 102, this.guiTop + 217 + countDownY, 9408399);
               } else {
                  this.fontRendererObj.drawString("Защита от смерти на солнце отсутствует", this.guiLeft + 50, this.guiTop + 213 + countDownY, 9408399);
               }
            } else {
               this.fontRendererObj.drawString("Вы не являетесь Вампиром.", this.guiLeft + 90, this.guiTop + 58, 7829367);
               this.fontRendererObj.drawString("Одолейте Лилит, договоритесь с", this.guiLeft + 73, this.guiTop + 70, 9408399);
               this.fontRendererObj.drawString("игроком вампиром или станьте", this.guiLeft + 77, this.guiTop + 80, 9408399);
               this.fontRendererObj.drawString("вампиром 1-го уровня за 60 эссенции.", this.guiLeft + 56, this.guiTop + 90, 9408399);
               hover = xx > 65 && xx < 245 && yy > 110 && yy < 138;
               GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
               GL11.glEnable(3042);
               this.mc.getTextureManager().bindTexture(resource);
               this.drawTexturedModalRect(this.guiLeft + 65, this.guiTop + 110, 0, 142, 184, 28);
               GL11.glDisable(3042);
               this.fontRendererObj.drawString("Купить первый уровень", this.guiLeft + 95, this.guiTop + 120, 11974326);
               if (hover) {
                  this.guiWitcheryPayerInfo = new GuiWitchery.GuiWitcheryPayerInfo(1, "Купить первый уровень Вампира", 60, 0);
               }
            }
         } else if (page == 1) {
            this.drawStringWhenNSSize("Оборотень", 2.0F, 105, 35, 13290186);
            if (this.playerEx.getWerewolfLevel() > 0) {
               countDownY = this.renderGlobalInfo(this.playerEx.getWerewolfLevel(), xx, yy, false);
               this.drawStringWhenSize("Природня броня", 1.5F, 102, 198 + countDownY, 13290186);
               if (this.playerEx.getWerewolfArmor() == 0) {
                  this.fontRendererObj.drawString("Брони нет...", this.guiLeft + 130, this.guiTop + 212 + countDownY, 9408399);
                  this.fontRendererObj.drawString("Употребляйте Сырую баранину", this.guiLeft + 80, this.guiTop + 223 + countDownY, 9408399);
               } else {
                  this.fontRendererObj.drawString("Сырой баранины - " + this.playerEx.getWerewolfArmor(), this.guiLeft + 112, this.guiTop + 224 - 12 + countDownY, 9408399);
                  this.fontRendererObj.drawString("Природня броня поглощает " + this.playerEx.getWerewolfFinalArmor() * 5 + "% урона", this.guiLeft + 25 + 40, this.guiTop + 235 - 12 + countDownY, 9408399);
               }
            } else {
               this.fontRendererObj.drawString("Вы не являетесь Оборотнем.", this.guiLeft + 90, this.guiTop + 58, 7829367);
               this.fontRendererObj.drawString("Проведите ритуал, заразитесь или", this.guiLeft + 63, this.guiTop + 70, 9408399);
               this.fontRendererObj.drawString("станьте оборотнем 1-го.", this.guiLeft + 92, this.guiTop + 80, 9408399);
               this.fontRendererObj.drawString("уровня за 60 эссенции.", this.guiLeft + 96, this.guiTop + 90, 9408399);
               hover = xx > 65 && xx < 245 && yy > 110 && yy < 138;
               GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
               GL11.glEnable(3042);
               this.mc.getTextureManager().bindTexture(resource);
               this.drawTexturedModalRect(this.guiLeft + 65, this.guiTop + 110, 0, 142, 184, 28);
               GL11.glDisable(3042);
               this.fontRendererObj.drawString("Купить первый уровень", this.guiLeft + 95, this.guiTop + 120, 11974326);
               if (hover) {
                  this.guiWitcheryPayerInfo = new GuiWitchery.GuiWitcheryPayerInfo(2, "Купить первый уровень Оборотня", 60, 1);
               }
            }
         } else {
            this.drawStringWhenNSSize("Магазин", 2.0F, 120, 37, 13290186);
            hover = this.playerEx.isNoSunDeath();
            boolean isSunHover = xx > 44 && xx < 274 && yy > 59 && yy < 109 || hover;
            this.drawGradientRect(this.guiLeft + 45 + 230, this.guiTop + 60 + 50, this.guiLeft + 45, this.guiTop + 60, isSunHover ? 1085124013 : 279817645, isSunHover ? 1085124013 : 279817645);
            this.drawStringWhenNSSize("Зелье Багровых Магов", 1.5F, 60, 72, 13290186);
            if (hover) {
               this.fontRendererObj.drawString("Имеется в этом вайпе", this.guiLeft + 60, this.guiTop + 88, 9408399);
            } else {
               this.fontRendererObj.drawString("Купить за 300 эссенции на весь вайп", this.guiLeft + 60, this.guiTop + 88, 9408399);
               if (isSunHover) {
                  this.guiWitcheryPayerInfo = new GuiWitchery.GuiWitcheryPayerInfo(5, "Купить Зелье Багровых Магов", 300, 2);
               }
            }

            int yuy = 58;
            boolean isBarHover = xx > 44 && xx < 274 && yy > 59 + yuy && yy < 109 + yuy && this.playerEx.getWerewolfArmor() != 64;
            this.drawGradientRect(this.guiLeft + 45 + 230, this.guiTop + 60 + 50 + yuy, this.guiLeft + 45, this.guiTop + 60 + yuy, isBarHover ? 1085124013 : 279817645, isBarHover ? 1085124013 : 279817645);
            this.drawStringWhenNSSize("Природная броня", 1.5F, 60, 72 + yuy, 13290186);
            this.fontRendererObj.drawString(this.playerEx.getWerewolfArmor() == 64 ? "Имеется полный заряд брони" : "Купить набор сырой баранины", this.guiLeft + 60, this.guiTop + 88 + yuy, 9408399);
            if (isBarHover) {
               this.guiWitcheryPayerInfo = new GuiWitchery.GuiWitcheryPayerInfo(6, "Купить все заряды природной брони", 150, 2);
            }
         }

      }
   }

   private int renderGlobalInfo(int level, int xx, int yy, boolean type) {
      this.fontRendererObj.drawString(level + " Уровень", this.guiLeft + 131, this.guiTop + 52, 9408399);
      String textHover = null;
      int countDownY = 0;
      if (level != 10) {
         int l2 = 58;
         int i1 = 32;
         this.sendLvl = -1;
         if (selectLevel == -1) {
            selectLevel = level;
         }

         int xPos = 0;
         boolean selectLevelFor = true;

         int i;
         boolean hover;
         for(i = 1; i < 10; ++i) {
            hover = xx > l2 + 40 + xPos && xx < l2 + 49 + xPos && yy > i1 + 34 && yy < i1 + 41;
            int colorNeed = hover ? 1351717265 : 814846353;
            if (i == selectLevel) {
               colorNeed = -1716859692;
               selectLevelFor = level != i;
            } else if (i < level) {
               colorNeed = 1974127828;
            }

            if (hover) {
               this.drawGradientRect(l2 + this.guiLeft + 43 + 4 + xPos, i1 + this.guiTop + 4 + 35, l2 + this.guiLeft + 44 + xPos, i1 + this.guiTop + 36, colorNeed, colorNeed);
               textHover = "Переход на " + (i + 1) + " уровень";
               this.sendLvl = i;
            }

            this.drawGradientRect(l2 + this.guiLeft + 43 + 5 + xPos, i1 + this.guiTop + 5 + 35, l2 + this.guiLeft + 43 + xPos, i1 + this.guiTop + 35, colorNeed, colorNeed);
            xPos += 10;
         }

         i = this.renderTextQuest(type ? 0 : 1, selectLevel);
         if (type && selectLevel >= 3 && selectLevel < this.playerEx.getVampireLevelCap()) {
            this.fontRendererObj.drawString("Мало записей в Наблюдениях бессмертного.", this.guiLeft + 45, this.guiTop + 80 + 10 * i++, 10315623);
         }

         if (!selectLevelFor) {
            if (i >= 2) {
               countDownY = 4 * i;
            }

            hover = xx > 65 && xx < 245 && yy > 110 + countDownY && yy < 138 + countDownY;
            if (type) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
            } else {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
            }

            GL11.glEnable(3042);
            this.mc.getTextureManager().bindTexture(resource);
            this.drawTexturedModalRect(this.guiLeft + 65, this.guiTop + 110 + countDownY, 0, 142, 184, 28);
            GL11.glDisable(3042);
            this.fontRendererObj.drawString("Платный переход", this.guiLeft + 115, this.guiTop + 120 + countDownY, 11974326);
            if (hover) {
               this.guiWitcheryPayerInfo = new GuiWitchery.GuiWitcheryPayerInfo(type ? 1 : 2, "Переход на " + (level + 1) + " уровень " + (type ? "Вампира" : "Оборотня"), 80, type ? 0 : 1);
            }
         } else {
            countDownY = -10;
         }
      } else {
         countDownY = -60;
      }

      boolean hover = xx > 65 && xx < 245 && yy > 142 + countDownY && yy < 166 + countDownY;
      if (type) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
      } else {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
      }

      GL11.glEnable(3042);
      this.mc.getTextureManager().bindTexture(resource);
      this.drawTexturedModalRect(this.guiLeft + 65, this.guiTop + 142 + countDownY, 0, 142, 184, 28);
      GL11.glDisable(3042);
      this.fontRendererObj.drawString("Снять проклятье", this.guiLeft + 115, this.guiTop + 152 + countDownY, 11974326);
      if (hover) {
         this.guiWitcheryPayerInfo = new GuiWitchery.GuiWitcheryPayerInfo(type ? 3 : 4, "Снять проклятье " + (type ? "Вампира" : "Оборотня"), 40, type ? 0 : 1);
      }

      if (textHover != null) {
         this.drawHoveringText(Collections.singletonList(textHover), xx + this.guiLeft + 15, yy + this.guiTop + 15);
      }

      countDownY -= 20;
      return Math.min(countDownY, 0);
   }

   private int renderTextQuest(int id, int level) {
      this.drawStringWhenSize("Задание", 1.25F, 45, 63, 13290186);
      String[] textQuest = witcheryQuests[id][level - 1].split("#");

      int i;
      for(i = 0; i < textQuest.length; ++i) {
         this.fontRendererObj.drawString(textQuest[i], this.guiLeft + 45, this.guiTop + 80 + 10 * i, 9408399);
      }

      return i;
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      x -= this.guiLeft;
      y -= this.guiTop;
      if (x > 20 && x < 72 && y > 10 && y < 28) {
         page = 0;
         selectLevel = -1;
      } else if (x > 73 && x < 139 && y > 10 && y < 28) {
         page = 1;
         selectLevel = -1;
      } else if (x > 140 && x < 200 && y > 10 && y < 28) {
         page = 2;
      } else if (this.guiWitcheryPayerInfo != null) {
         page = -1;
         guiWitcheryPayerInfoGlobal = this.guiWitcheryPayerInfo;
      } else if (this.sendId != -1) {
         NetworkManager.sendToServer(new PacketWitchery(this.sendId));
      } else if (this.sendLvl != -1) {
         selectLevel = this.sendLvl;
      }

      if (page != -1) {
         this.guiWitcheryPayerInfo = null;
         guiWitcheryPayerInfoGlobal = null;
      }

   }

   public static void dropInfo() {
      if (guiWitcheryPayerInfoGlobal != null) {
         page = guiWitcheryPayerInfoGlobal.page;
         guiWitcheryPayerInfoGlobal = null;
      }

   }

   static {
      witcheryQuests[0] = new String[]{"Пополните запасы крови!", "Парализуйте пять жителей и выпейте#половину запаса их крови.", "Увеличьте свои познания в древних#рукописях §2Наблюдения бессмертного§r#и проживите 9 минут в ночное время суток.", "Победите Лилит, отдав ей §2Замороженное#сердце§r. Полученную кровь Лилит#принесите §2кукле Синобу Осино§r.", "Убейте 20 Ифритов.", "Победите Лилит, отдав ей §2Мак§r.", "Посетите 2 деревни Жителей.", "Постройте для жителей клетки#и выпейте их кровь.", "Превратить жителя в своего слугу."};
      witcheryQuests[1] = new String[]{"Алтарь Волка - принесите три §2Золотых слитка§r.", "Алтарь Волка - принесите#тридцать §2Сырой баранины§r.", "Алтарь Волка - принесите#десять §2Языков собаки§r.", "Победите Рогатого Охотника.", "Убить 10 свинозомби в облике#оборотня с прыжка.", "Повыть на луну в 16-ти разных местах.", "Приручит 6 волков.", "Убить 30 свинозомби в облике оборотня.", "Убить оборотня жителя или игрока."};
   }

   private static class GuiWitcheryPayerInfo {
      public final int id;
      public final int coin;
      public final int page;
      public final String text;

      public GuiWitcheryPayerInfo(int id, String text, int coin, int page) {
         this.id = id;
         this.text = text;
         this.coin = coin;
         this.page = page;
      }
   }
}
