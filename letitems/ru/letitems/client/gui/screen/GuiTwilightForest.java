package ru.letitems.client.gui.screen;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.mods.PacketTwilightForest;
import ru.letitems.common.util.GeneralUtils;
import twilightforest.TFExtendedPlayer;
import twilightforest.TFExtendedPlayer.Skills;

@SideOnly(Side.CLIENT)
public final class GuiTwilightForest extends GuiPreRenderScreen {
   private static final ResourceLocation bg = new ResourceLocation("letitems", "textures/gui/gui-tf.png");
   private static final ArrayListMultimap<Integer, Collection<String>> listBosses = ArrayListMultimap.create();
   private static final String[] bossesName;
   private static final String[] biomeName;
   private static String[] decFormat;
   private static final String[] pageList;
   private static int page;
   private TFExtendedPlayer playerTf;
   private int sendId = -1;

   public GuiTwilightForest(EntityPlayer player) {
      page = 0;
      this.playerTf = TFExtendedPlayer.get(player);
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(bg);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      this.sendId = -1;
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      if (this.playerTf != null) {
         this.drawStringWhenSize("Сумеречный лес", 2.0F, 15, 22, 13290186);
         int l2 = false;

         int skillIndex;
         int sumKills;
         int j;
         for(skillIndex = 0; skillIndex < 3; ++skillIndex) {
            int l2 = 15 + 48 * skillIndex + (skillIndex == 2 ? 12 : 0);
            int i1 = 47;
            boolean hoverRelic = xx > l2 - 4 && xx < l2 + 47 && yy > i1 - 6 && yy < i1 + 10;
            if (hoverRelic) {
               page = skillIndex;
            }

            sumKills = skillIndex == 1 ? 12 : 0;
            j = !hoverRelic && page != skillIndex ? 1346124860 : -2142417587;
            this.drawGradientRect(this.guiLeft + l2 + 40 + sumKills, this.guiTop + i1 + 10, this.guiLeft + l2 - 4, this.guiTop + i1 - 4, j, j);
            float scale = 0.75F;
            GL11.glPushMatrix();
            GL11.glTranslated((double)(l2 + this.guiLeft + (skillIndex == 2 ? 5 : (skillIndex == 1 ? 2 : 0))), (double)(i1 + this.guiTop), 0.0D);
            GL11.glScalef(0.75F, 0.75F, 0.75F);
            this.fontRendererObj.drawString(pageList[skillIndex], 0, 0, !hoverRelic && page != skillIndex ? 3947580 : 9803157);
            GL11.glPopMatrix();
         }

         int l2 = 15;
         int i1;
         int colorDraw;
         int bos;
         int i1;
         if (page == 0) {
            skillIndex = 0;
            i1 = 0;
            sumKills = 0;
            float scaleText = 0.75F;

            for(Iterator var31 = listBosses.entries().iterator(); var31.hasNext(); ++skillIndex) {
               Entry<Integer, Collection<String>> s = (Entry)var31.next();
               i1 = 65 + 32 * skillIndex;
               if (xx > l2 - 5 && xx < l2 + 300 && yy > i1 - 6 && yy < i1 + 24) {
                  this.drawGradientRect(this.guiLeft + l2 + 294, this.guiTop + i1 + 25, this.guiLeft + l2 - 4, this.guiTop + i1 - 4, 1082167194, 1082167194);
               }

               String keyName = biomeName[(Integer)s.getKey()];
               colorDraw = this.fontRendererObj.getStringWidth(keyName);
               float scale = Math.min(120.0F / (float)colorDraw, 1.25F);
               GL11.glPushMatrix();
               GL11.glTranslated((double)(l2 + this.guiLeft + 2), (double)(i1 + this.guiTop + (scale != 1.0F ? 1 : 0)), 0.0D);
               GL11.glScalef(scale, scale, scale);
               this.fontRendererObj.drawString(keyName, 0, 0, 13816530);
               GL11.glPopMatrix();
               bos = 0;

               for(Iterator var18 = ((Collection)s.getValue()).iterator(); var18.hasNext(); ++i1) {
                  String b = (String)var18.next();
                  boolean isUnlock = this.playerTf.getPlayerBossProgress(TFExtendedPlayer.bosses[i1]);
                  if (isUnlock) {
                     ++sumKills;
                  }

                  GL11.glPushMatrix();
                  GL11.glTranslated((double)(l2 + this.guiLeft + 2 + bos), (double)(i1 + this.guiTop + 14), 0.0D);
                  GL11.glScalef(scaleText, scaleText, scaleText);
                  this.fontRendererObj.drawString(b, 0, 0, isUnlock ? 9477817 : 14185074);
                  GL11.glPopMatrix();
                  bos += (int)((float)this.fontRendererObj.getStringWidth(b) * scaleText) + 10;
               }
            }

            if (sumKills != TFExtendedPlayer.bosses.length) {
               boolean hoverPay = xx > 12 && xx < 175 && yy > 221 && yy < 232;
               GL11.glPushMatrix();
               GL11.glTranslated((double)(this.guiLeft + 15), (double)(this.guiTop + 225), 0.0D);
               GL11.glScalef(scaleText, scaleText, scaleText);
               this.fontRendererObj.drawString("Купить прогресс Леса за 120 эссенции", 0, 0, hoverPay ? 10077658 : 5658198);
               GL11.glPopMatrix();
               if (hoverPay) {
                  ArrayList<String> listText = new ArrayList();
                  listText.add("§6Покупка прогресса");
                  listText.add("§8Вы потратите 120 эссенции");
                  this.drawHoveringText(listText, x + 4, y + 4);
                  this.sendId = 6;
               }
            }
         } else {
            int level;
            int i;
            if (page == 1) {
               skillIndex = 0;
               bos = 0;
               i1 = 0;
               sumKills = 0;
               j = 0;
               level = TFExtendedPlayer.bosses.length;

               for(i = 0; i < level; ++i) {
                  if (i != 2 && i != 4 && i != 5 && i != 9) {
                     int i1 = 65;
                     short l2;
                     if (j % 2 != 0) {
                        l2 = 165;
                        i1 = i1 + 32 * skillIndex++;
                     } else {
                        l2 = 15;
                        i1 = i1 + 32 * bos++;
                     }

                     boolean hover = xx > l2 && xx < l2 + 140 && yy > i1 - 6 && yy < i1 + 22;
                     if (yy > 230 || yy < 10) {
                        hover = false;
                     }

                     int countBossKills = this.playerTf.getPlayerBossProgressState(TFExtendedPlayer.bosses[i]);
                     colorDraw = hover ? -2139058278 : 546410897;
                     this.drawGradientRect(this.guiLeft + l2 + 140, this.guiTop + i1 + 23, this.guiLeft + l2 - 4, this.guiTop + i1 - 4, colorDraw, colorDraw);
                     int titleWidth = this.fontRendererObj.getStringWidth(bossesName[i]);
                     float scale = Math.min(85.0F / (float)titleWidth, 1.25F);
                     GL11.glPushMatrix();
                     GL11.glTranslated((double)(l2 + this.guiLeft + 2), (double)(i1 + this.guiTop + (scale != 1.0F ? 1 : 0)), 0.0D);
                     GL11.glScalef(scale, scale, scale);
                     this.fontRendererObj.drawString(bossesName[i], 0, 0, 13816530);
                     GL11.glPopMatrix();
                     GL11.glPushMatrix();
                     GL11.glTranslated((double)(l2 + this.guiLeft + 2), (double)(i1 + this.guiTop + 12), 0.0D);
                     GL11.glScalef(0.75F, 0.75F, 0.75F);
                     this.fontRendererObj.drawString(GeneralUtils.declinationCombine(countBossKills, decFormat, "побед"), 0, 0, 11513775);
                     GL11.glPopMatrix();
                     sumKills += countBossKills;
                     ++j;
                  }
               }

               l2 = 15;
               i1 += 32;
               this.drawGradientRect(this.guiLeft + l2 + 290, this.guiTop + i1 + 23, this.guiLeft + l2 - 4, this.guiTop + i1 - 4, 1351051625, 1351051625);
               i = this.fontRendererObj.getStringWidth("Всего побед над боссами");
               float scale = Math.min(120.0F / (float)i, 1.25F);
               GL11.glPushMatrix();
               GL11.glTranslated((double)(l2 + this.guiLeft + 2), (double)(i1 + this.guiTop + 1), 0.0D);
               GL11.glScalef(scale, scale, scale);
               this.fontRendererObj.drawString("Всего побед над боссами", 0, 0, 13816530);
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glTranslated((double)(l2 + this.guiLeft + 2), (double)(i1 + this.guiTop + 12), 0.0D);
               GL11.glScalef(0.75F, 0.75F, 0.75F);
               this.fontRendererObj.drawString(GeneralUtils.declinationCombine(sumKills, decFormat, "побед"), 0, 0, 11513775);
               GL11.glPopMatrix();
               i1 += 32;
               this.drawGradientRect(this.guiLeft + l2 + 290, this.guiTop + i1 + 42, this.guiLeft + l2 - 4, this.guiTop + i1 - 4, 635034073, 635034073);
               if (this.playerTf.getTakeAwards() >= 8) {
                  GL11.glPushMatrix();
                  GL11.glTranslated((double)(this.guiLeft + 79), (double)(this.guiTop + 201), 0.0D);
                  GL11.glScalef(1.5F, 1.5F, 1.5F);
                  this.fontRendererObj.drawString("Награды за убийство", 0, 0, 13816530);
                  GL11.glPopMatrix();
                  this.fontRendererObj.drawString("Были получены все награды", l2 + this.guiLeft + 73, i1 + this.guiTop + 21, 9342606);
               } else {
                  GL11.glPushMatrix();
                  GL11.glTranslated((double)(this.guiLeft + 22), (double)(this.guiTop + 198), 0.0D);
                  GL11.glScalef(1.5F, 1.5F, 1.5F);
                  this.fontRendererObj.drawString("Награды за убийство", 0, 0, 13816530);
                  GL11.glPopMatrix();
                  this.fontRendererObj.drawString("Каждые 20 убийств боссов награждается", this.guiLeft + 22, i1 + this.guiTop + 18, 9342606);
                  this.fontRendererObj.drawString("различными предметами, получено " + GeneralUtils.declinationCombine(this.playerTf.getTakeAwards(), decFormat, "наград") + ".", this.guiLeft + 22, i1 + this.guiTop + 27, 9342606);
               }
            } else {
               skillIndex = -1;

               for(bos = 0; bos < 5; ++bos) {
                  i1 = 65 + 34 * bos;
                  boolean hoverRelic = xx > l2 - 7 && xx < l2 + 300 && yy > i1 - 6 && yy < i1 + 27;
                  Skills skill = TFExtendedPlayer.getSkillFromIndex(bos);
                  level = this.playerTf.getPlayerSkillLevel(skill);
                  i = hoverRelic ? -2142087333 : 546410897;
                  this.drawGradientRect(this.guiLeft + l2 + 294, this.guiTop + i1 + 28, this.guiLeft + l2 - 4, this.guiTop + i1 - 4, i, i);
                  if (hoverRelic && level < 5) {
                     skillIndex = bos;
                  }

                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  this.mc.getTextureManager().bindTexture(resource);
                  this.drawTexturedModalRect(l2 + this.guiLeft - 3, i1 + this.guiTop, bos * 25, 117, 25, 25);
                  i1 = this.fontRendererObj.getStringWidth(skill.name);
                  float scale = Math.min(125.0F / (float)i1, 1.05F);
                  GL11.glPushMatrix();
                  GL11.glTranslated((double)(l2 + this.guiLeft + 4 + 20), (double)(i1 + this.guiTop + (scale != 1.0F ? 1 : 0)), 0.0D);
                  GL11.glScalef(scale, scale, scale);
                  this.fontRendererObj.drawString(skill.name, 0, 0, 13816530);
                  GL11.glPopMatrix();
                  GL11.glPushMatrix();
                  GL11.glTranslated((double)(l2 + this.guiLeft + 4 + 20), (double)(i1 + this.guiTop + 11), 0.0D);
                  GL11.glScalef(0.75F, 0.75F, 0.75F);
                  this.fontRendererObj.drawString(skill.desc + (skill.v > 0 ? level * skill.v + "%" : ""), 0, 0, 11513775);
                  this.fontRendererObj.drawString(level == 5 ? "Максимальный уровень" : level + " уровень", 0, 10, level == 5 ? 10257495 : 10526880);
                  GL11.glPopMatrix();
               }

               if (skillIndex != -1) {
                  Skills skill = TFExtendedPlayer.getSkillFromIndex(skillIndex);
                  i1 = this.playerTf.getPlayerSkillLevel(skill);
                  ArrayList<String> listText = new ArrayList();
                  byte[] skillNeedInfo = TFExtendedPlayer.getItemForLevel(i1 + 1);
                  listText.add("§6Умение " + skill.name);
                  listText.add(String.format("§8Требуется %s Улучшений умений %s", skillNeedInfo[1], skillNeedInfo[0] == 0 ? "" : skillNeedInfo[0] + 1 + " уровня"));
                  this.drawHoveringText(listText, x + 4, y + 4);
                  this.sendId = skillIndex + 1;
               }
            }
         }

      }
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.sendId != -1 && this.mc.thePlayer != null) {
         NetworkManager.sendToServer(new PacketTwilightForest(this.sendId));
      }

   }

   static {
      listBosses.put(0, ImmutableSet.of("Нага", "Сумеречный Лич"));
      listBosses.put(1, ImmutableSet.of("Лабиринт", "Гидра"));
      listBosses.put(2, ImmutableSet.of("Крепость рыцарей-фантомов", "Трофейный пьедестал", "Темная Башня"));
      listBosses.put(3, ImmutableSet.of("Берлога Йети", "Дворец Авроры"));
      listBosses.put(4, Collections.singletonList("Тролльи пещеры"));
      bossesName = new String[]{"Нага", "Сумеречный Лич", "Лабиринт", "Гидра", "Крепость рыцарей-фантомов", "Трофейный пьедестал", "Темная Башня", "Берлога Йети", "Дворец Авроры", "Тролльи пещеры"};
      biomeName = new String[]{"Начало приключений", "Приключения на болотах", "Темный лес", "Снежный лес и ледник", "Высокогорье"};
      decFormat = new String[]{"а", "ы", ""};
      pageList = new String[]{"Прогресс", "Статистика", "Умения"};
   }
}
