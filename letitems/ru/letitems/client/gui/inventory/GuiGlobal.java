package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientParams;
import ru.letitems.client.EventHandlerClient;
import ru.letitems.client.gui.screen.GuiArch;
import ru.letitems.client.gui.screen.GuiSkills;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.LetItems;
import ru.letitems.common.handler.SkillsManager;
import ru.letitems.common.inventory.ExtendedPlayer;
import ru.letitems.common.items.ItemHairBand;
import ru.letitems.common.items.ItemScrollTitle;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.util.GuiType;

@SideOnly(Side.CLIENT)
public class GuiGlobal extends GuiPreRenderContainer {
   private static final ResourceLocation bgInvGlobal = new ResourceLocation("letitems", "textures/gui/bg-inv-first.png");
   private final EntityPlayer player;
   private final ExtendedPlayer extendedPlayer;
   private final ItemStack slotScrollTitle;
   private final int idGui;
   public static List<GuiGlobal.Skills> skillsList = new ArrayList(SkillsManager.getSizeSkills());
   public static List<GuiGlobal.Quest> questList = new ArrayList();
   public static int slaveSt = 0;
   public static int slaveStId = 0;
   public static Object renderUserAvatar = null;
   public static Object renderUserCover = null;
   private boolean hairChange;
   private int lastMetaHair;
   private int scale;
   private int scalePlayer;
   private int widthTitle;

   public GuiGlobal(EntityPlayer player) {
      this(player.inventoryContainer, player, 0);
   }

   public GuiGlobal(Container container, EntityPlayer player, int id) {
      super(container);
      this.hairChange = false;
      this.scalePlayer = 40;
      this.widthTitle = 0;
      this.hairChange = false;
      this.allowUserInput = false;
      this.player = player;
      this.idGui = id;
      this.extendedPlayer = ExtendedPlayer.getExtendedPlayer(player);
      this.slotScrollTitle = this.extendedPlayer.getStackInSlot(3);
   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
      this.buttonList.clear();
      int hj1 = this.guiLeft + 233;
      int hj2 = this.guiTop + 61;
      this.buttonList.add(new GuiGlobal.GuiPanelButton(0, "G", hj1, hj2, 12));
      if (LetItems.loadBaubles) {
         hj1 -= 14;
         this.buttonList.add(new GuiGlobal.GuiPanelButton(1, "B", hj1, hj2, 12));
      }

      if (LetItems.loadTConstruct) {
         hj1 -= 20;
         this.buttonList.add(new GuiGlobal.GuiPanelButton(2, "TC", hj1, hj2, 18));
      }

      if (LetItems.loadGC) {
         hj1 -= 20;
         this.buttonList.add(new GuiGlobal.GuiPanelButton(3, "GC", hj1, hj2, 18));
      }

      if (this.buttonList.size() == 1) {
         this.buttonList.clear();
      }

      if (renderUserAvatar instanceof BufferedImage) {
         renderUserAvatar = this.mc.getTextureManager().getDynamicTextureLocation("imgAvatar", new DynamicTexture((BufferedImage)renderUserAvatar));
      }

      if (renderUserCover instanceof BufferedImage) {
         renderUserCover = this.mc.getTextureManager().getDynamicTextureLocation("imgCover", new DynamicTexture((BufferedImage)renderUserCover));
      }

   }

   public void updateScreen() {
      super.updateScreen();
      ItemStack hairStack = this.extendedPlayer.getStackInSlot(0);
      if (hairStack != null) {
         if (hairStack.getItemDamage() != this.lastMetaHair) {
            this.lastMetaHair = hairStack.getItemDamage();
            ItemHairBand.HairBandType hairBandType = ItemHairBand.getType(hairStack);
            this.hairChange = hairBandType != null && hairBandType.getParts() > 0;
         }
      } else {
         this.hairChange = false;
         this.lastMetaHair = -1;
      }

   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int x = this.guiLeft;
      int y = this.guiTop;
      this.mc.getTextureManager().bindTexture(bgInvGlobal);
      func_146110_a(x, y, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      GL11.glPushMatrix();
      int xx = mouseX - x;
      int yy = mouseY - y;
      String title = "Нет титула";
      String slaveText;
      if (this.slotScrollTitle != null) {
         slaveText = ItemScrollTitle.getTitle(this.slotScrollTitle);
         if (!StringUtils.isNullOrEmpty(slaveText)) {
            title = slaveText;
         }
      }

      if (this.widthTitle == 0) {
         this.widthTitle = this.fontRendererObj.getStringWidth(title);
      }

      this.drawStringWhenSize(this.player.getDisplayName(), 1.5F, 60, 20, 9013641);
      this.drawStringWhenSize(title, 0.75F, 60, 33, xx > 58 && xx < 58 + this.widthTitle && yy > 30 && yy < 39 ? 9539985 : 6842472);
      slaveText = "3нет";
      if (slaveStId == 3) {
         slaveText = "5предельная";
      } else if (slaveStId == 2) {
         slaveText = "4средняя";
      } else if (slaveStId == 1) {
         slaveText = "2маленькая";
      }

      this.drawStringWhenSize("Усталость: §" + slaveText, 0.75F, 60, 41, 6842472);
      this.drawStringWhenSize("Задания", 1.25F, 13, 84, 12763842);
      this.drawStringWhenSize("Активных " + (questList.isEmpty() ? 0 : questList.size()), 0.75F, 16, 97, 9539985);
      String countAchPoints = String.valueOf(ClientParams.userAchPoints);
      boolean isHoverAch = xx > 250 && xx < 313 && yy > 164 && yy < 239;
      if (isHoverAch) {
         GeneralClientUtils.bind("letitems:textures/gui/ach-border.png");
         func_146110_a(x + 269, y + 182, 0.0F, 0.0F, 26, 28, 26.0F, 28.0F);
      }

      this.drawGradientRect(x + 262 + slaveSt, y + 148 + 2, x + 262, y + 148, 1084531876, 1084531876);
      String textSlave = (int)((float)slaveSt / 40.0F * 100.0F) + "%";
      this.drawStringWhenSize(textSlave, 0.5F, 282 - (int)((float)(this.fontRendererObj.getStringWidth(textSlave) / 2) * 0.5F), 153, 6842472);
      this.fontRendererObj.drawString(countAchPoints, x + 282 - this.fontRendererObj.getStringWidth(countAchPoints) / 2, y + (!isHoverAch ? 216 : 215), isHoverAch ? 9070128 : 6052956);
      if (this.modSettings.invAvatar) {
         this.renderImage((ResourceLocation)renderUserAvatar, 28, 28, 1.45F, x + 5, y + 5, 0.85F, false, true);
      } else {
         GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft + 8, this.guiTop, 80, 56);
         this.renderPlayer(47.0F, this.guiLeft - 85, this.guiTop - 32);
         GeneralClientUtils.ScissorHelper.endScissor();
      }

      this.renderImage((ResourceLocation)renderUserCover, 170, 48, 1.0F, x + 68, y + 1, this.modSettings.invCoverAlpha, true, false);
      this.drawGradientRect(this.guiLeft + 75 + 170, this.guiTop + 61 + 98, this.guiLeft + 75, this.guiTop + 61, 130730698, 130730698);
      this.fontRendererObj.drawString("Броня", x + 85, y + 75, 6052956);
      if (this.idGui == 0) {
         this.fontRendererObj.drawString("Украшения", x + 85, y + 113, 6052956);
      } else if (this.idGui == 1) {
         this.fontRendererObj.drawString("Кольца", x + 175, y + 75, 6052956);
         this.fontRendererObj.drawString("Украшения", x + 85, y + 113, 6052956);
      } else if (this.idGui == 2) {
         this.fontRendererObj.drawString("Банки", x + 175, y + 75, 6052956);
         this.fontRendererObj.drawString("Снаряжение", x + 85, y + 113, 6052956);
      } else if (this.idGui == 3) {
         this.fontRendererObj.drawString("Кислород", x + 175, y + 75, 6052956);
         this.fontRendererObj.drawString("Термозащита", x + 85, y + 113, 6052956);
         this.fontRendererObj.drawString("Устройства", x + 175, y + 113, 6052956);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int i;
      int xSlot;
      if (this.idGui == 3) {
         for(i = 36; i < 50; ++i) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
            int xSlot = x + slot.xDisplayPosition - 2;
            xSlot = y + slot.yDisplayPosition - 2;
            this.mc.getTextureManager().bindTexture(resource);
            GL11.glEnable(3042);
            this.drawTexturedModalRect(xSlot + 1, xSlot + 1, 153, 180, 18, 18);
            this.mc.getTextureManager().bindTexture(this.resource2);
            if (i >= 40 && !slot.getHasStack()) {
               this.voidSlot(xSlot + 1, xSlot + 1, 54, 18 * (i - 40), 18);
            }
         }
      } else {
         i = this.inventorySlots.inventorySlots.size() - 36;

         for(int i = 0; i < i; ++i) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
            xSlot = x + slot.xDisplayPosition - 2;
            int ySlot = y + slot.yDisplayPosition - 2;
            this.mc.getTextureManager().bindTexture(resource);
            GL11.glEnable(3042);
            this.drawTexturedModalRect(xSlot + 1, ySlot + 1, 153, 180, 18, 18);
            this.mc.getTextureManager().bindTexture(this.resource2);
            if (this.idGui == 0) {
               if (i <= 2 && !slot.getHasStack()) {
                  this.voidSlot(xSlot + 1, ySlot, 18, i * 18, 18);
               }
            } else if (this.idGui == 1) {
               if (i > 3 && !slot.getHasStack()) {
                  int jj = i - 4;
                  this.voidSlot(xSlot + 2, ySlot + 1, 37, -2 + (jj > 1 ? jj - 1 : jj) * 16, 16);
               }
            } else if (this.idGui == 2 && !slot.getHasStack()) {
               this.voidSlot(xSlot + 1, ySlot + 1, 72, 18 * (i == 2 ? 3 : (i == 3 ? 2 : i)), 18);
            }
         }
      }

      if (xx > 7 && xx < 69 && yy > 127 && yy < 239) {
         if (this.hairChange) {
            EventHandlerClient.instance.enchHair = true;
         }

         this.drawGradientRect(this.guiLeft + 7 + 62, this.guiTop + 127 + 112, this.guiLeft + 7, this.guiTop + 127, 147507914, 147507914);
         if (this.scalePlayer > 35) {
            --this.scalePlayer;
         }
      } else if (this.scalePlayer != 40) {
         ++this.scalePlayer;
      }

      GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.guiLeft + 7, this.guiTop + 127, 62, 112);
      this.renderPlayer((float)this.scalePlayer, this.guiLeft - 78, this.guiTop + 95);
      GeneralClientUtils.ScissorHelper.endScissor();
      GL11.glPopMatrix();
   }

   private void renderImage(ResourceLocation resourceLocation, int sizeX, int sizeY, float scale, int x, int xx, float opacityDraw, boolean hardAlpha, boolean smootch) {
      if (resourceLocation != null && opacityDraw != 0.0F) {
         GL11.glPushMatrix();
         if (hardAlpha) {
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, opacityDraw);
         GL11.glTranslatef(1.0F + (float)x + 5.0F, 1.0F + (float)xx + 5.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(resourceLocation);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GL11.glBlendFunc(770, 771);
         if (smootch) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
         }

         Tessellator tessellator = Tessellator.instance;
         GL11.glTranslatef(0.5F, 0.58461535F, 0.0F);
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV((double)(0.0F * scale), (double)((float)sizeY * scale), 5.0D, 0.0D, 1.0D);
         tessellator.addVertexWithUV((double)((float)sizeX * scale), (double)((float)sizeY * scale), 5.0D, 1.0D, 1.0D);
         tessellator.addVertexWithUV((double)((float)sizeX * scale), (double)(0.0F * scale), 5.0D, 1.0D, 0.0D);
         tessellator.addVertexWithUV((double)(0.0F * scale), (double)(0.0F * scale), 5.0D, 0.0D, 0.0D);
         tessellator.draw();
         if (hardAlpha) {
            GL11.glDisable(3042);
            GL11.glEnable(3008);
         }

         GL11.glPopMatrix();
      }
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      EventHandlerClient.instance.enchHair = false;
      if ((xx <= 57 || xx >= 130 || yy <= 40 || yy >= 49) && (xx <= 250 || xx >= 310 || yy <= 143 || yy >= 159)) {
         if (xx > 15 && xx < 70 && yy > 60 && yy < 124) {
            this.drawHoveringText(this.getTooltipQuest(), x + 10, y + 10);
         } else if (xx <= 58 || xx >= 58 + this.widthTitle || yy <= 30 || yy >= 39) {
            if (xx > 7 && xx < 69 && yy > 127 && yy < 239) {
               if (this.hairChange) {
                  this.drawHoveringText(Collections.singletonList("Сменить стиль косички"), x + 5, y + 10);
                  EventHandlerClient.instance.enchHair = true;
               }
            } else if (xx > 250 && xx < 313 && yy > 164 && yy < 239) {
               this.drawHoveringText(Collections.singletonList("Количество очков достижений"), x + 5, y + 10);
            } else if (!skillsList.isEmpty() && xx > 250 && xx < 310 && yy > 16 && yy < 145) {
               for(int i = 0; i < skillsList.size(); ++i) {
                  GuiGlobal.Skills sk = (GuiGlobal.Skills)skillsList.get(i);
                  if (xx > sk.getX() && xx < sk.getX() + 18 && yy > sk.getY() && yy < sk.getY() + 18) {
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     this.mc.getTextureManager().bindTexture(this.resource2);
                     this.drawTexturedModalRect(sk.getX() + this.guiLeft, sk.getY() + this.guiTop, 0, 0, 18, 18);
                     this.drawHoveringText(this.getTooltip(sk, i), x + 10, y + 10);
                     break;
                  }
               }
            }
         }
      } else {
         this.drawHoveringText(Collections.singletonList("Получаемый опыт снижен на " + EnumChatFormatting.GOLD + (slaveStId == 0 ? 0 : slaveStId * 25 + 25) + "%"), x + 10, y + 10);
      }

   }

   public void drawGuiContainerForegroundLayer(int x, int y) {
      super.drawGuiContainerForegroundLayer(x, y);
      x -= this.guiLeft;
      y -= this.guiTop;
      if (this.idGui == 0) {
         if (x > 84 && x < 102 && y > 127 && y < 145) {
            this.renderSlot(RegItems.hairBand);
         } else if ((x > 106 && x < 124 || x > 128 && x < 146) && y > 127 && y < 145) {
            this.renderSlot(RegItems.itemCosmetics);
         }
      }

   }

   public void mouseMovedOrUp(int mouseX, int mouseY, int which) {
      super.mouseMovedOrUp(mouseX, mouseY, which);
      if (which == 0) {
         mouseX -= this.guiLeft;
         mouseY -= this.guiTop;
         if (mouseX > 7 && mouseX < 69 && mouseY > 127 && mouseY < 239 && this.hairChange) {
            GuiType.UPGRADE_HAIRS.openGui(this.player, this.player.worldObj, 0, 0, 0);
         } else if (mouseX <= 58 || mouseX >= 58 + this.widthTitle || mouseY <= 30 || mouseY >= 39) {
            if (mouseX > 250 && mouseX < 314 && mouseY > 8 && mouseY < 160) {
               this.mc.displayGuiScreen(new GuiSkills(this.mc.thePlayer));
            } else if (mouseX > 250 && mouseX < 313 && mouseY > 164 && mouseY < 239) {
               this.mc.displayGuiScreen(new GuiArch(this.player));
            }
         }

      }
   }

   public void onGuiClosed() {
      super.onGuiClosed();
      EventHandlerClient.instance.enchHair = false;
   }

   private void renderSlot(Item item) {
      if (this.modSettings.invHighlightingItems) {
         for(int i1 = 7; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
            if (!slot.getHasStack() || slot.getStack().getItem() != item) {
               int j1 = slot.xDisplayPosition;
               int k1 = slot.yDisplayPosition;
               GL11.glDisable(2896);
               GL11.glDisable(2929);
               this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -1070781139, -802345683);
               GL11.glEnable(2896);
               GL11.glEnable(2929);
            }
         }

      }
   }

   private List<String> getTooltip(GuiGlobal.Skills skill, int i) {
      ArrayList<String> list = new ArrayList();
      list.add(EnumChatFormatting.BOLD + skill.getName());
      int skillLevel = skill.getLevel();
      if (skillLevel >= 50) {
         list.add("§6Максимальный уровень умения");
      } else {
         list.add("§7" + skillLevel + " уровень умения");
         if (skill.getPercent() != null) {
            list.add("§7До нового уровня остаётся §b" + skill.getPercent() + "%");
         }
      }

      String skillBonus = this.getSkillRealyInfo(skill.getName(), skill.getLevel());
      if (!skillBonus.isEmpty()) {
         list.add((Object)null);
         list.add("§8Бонусы:");
         String[] var6 = skillBonus.split("U");
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String s = var6[var8];
            list.add(" §8- §7" + s);
         }
      }

      return list;
   }

   private String getSkillRealyInfo(String name, int level) {
      StringBuilder buildSkills = new StringBuilder();
      byte var5 = -1;
      switch(name.hashCode()) {
      case -1943741055:
         if (name.equals("Добытчик Артефэ")) {
            var5 = 0;
         }
         break;
      case -1755083478:
         if (name.equals("Путешественник")) {
            var5 = 10;
         }
         break;
      case -970918249:
         if (name.equals("Рыболов")) {
            var5 = 1;
         }
         break;
      case -260891873:
         if (name.equals("Огнеупорность")) {
            var5 = 3;
         }
         break;
      case 731365861:
         if (name.equals("Фермерство")) {
            var5 = 9;
         }
         break;
      case 924833758:
         if (name.equals("Колдовство")) {
            var5 = 6;
         }
         break;
      case 933718006:
         if (name.equals("Защита")) {
            var5 = 2;
         }
         break;
      case 993998836:
         if (name.equals("Атака")) {
            var5 = 5;
         }
         break;
      case 1420304500:
         if (name.equals("Шахтёр")) {
            var5 = 4;
         }
         break;
      case 1665439740:
         if (name.equals("Зачарование")) {
            var5 = 7;
         }
         break;
      case 1907876019:
         if (name.equals("Питание")) {
            var5 = 8;
         }
      }

      switch(var5) {
      case 0:
         buildSkills.append("Шанс получения осколка Артефэ §4").append((float)level / 5.0F).append("%UШанс получения кристалла Артефэ §4").append((float)level / 20.0F).append("%U§6[Max] §7Гарантированное получение осколка Артефэ");
         break;
      case 1:
         buildSkills.append("Шанс получения осколка Артефэ §4").append((float)level * 0.4F).append("%UШанс получения кристалла Артефэ §4").append((float)level * 0.1F).append("%UШанс получения дополнительных ресурсов §4").append(level < 5 ? 0.0F : (float)level * 0.1F).append("%U§6[Max] §7+3 уровня к зачарованиям").append("U§6[Max] §7Удочка не теряет прочности");
         if (LetItems.loadThaumCraft) {
            buildSkills.append("U§5[ThaumCraft] §7Шанс получения аспекта §4").append((float)level * 0.2F).append("%");
         }
         break;
      case 2:
         buildSkills.append("Снижение урона от монстров на §4").append((float)level * 0.5F).append("%UСнижение урона от игроков и стрел на §4").append((float)level * 0.3F).append("%UСнижение урона от магических атак на §4").append((float)level * 0.16F).append("%U§6[Max] §7С шансом §420%§7 урон может быть поглощён").append("U§6[Max] §7Спасёт от смерти в битве с монстрами");
         break;
      case 3:
         buildSkills.append("Снижение урона от горения и огня на §4").append((float)level * 0.6F).append("%UСнижение урона от лавы на §4").append((float)level * 0.4F).append("%U§6[Max] §7С шансом §420%§7 урон может быть поглощён").append("U§6[Max] §7Полное игнорирование урона от лавы");
         break;
      case 4:
         buildSkills.append("Шанс получения дополнительных ресурсов §4").append((float)level * 0.3F).append("%UШанс получения удвоенного дропа §4").append((float)level * 0.1F).append("%U§6[Max] §7Добыча руды восстанавливает голод");
         if (LetItems.loadThaumCraft) {
            buildSkills.append("U§5[ThaumCraft] §7Шанс получения аспекта §4").append((float)level / 25.0F).append("%");
         }
         break;
      case 5:
         buildSkills.append("Урон по монстрам увеличен на §4").append((float)level * 0.4F).append("%UУрон по боссам увеличен на §4").append((float)level * 0.3F).append("%UВосстановление §420%§7 HP от нанесенного урона с шансом §4").append(15 + level).append("%U§6[Max] §7Вампиризм срабатывает со 100% шансом").append("U§6[Max] §7Критический удар §5x1.8§7 по монстру с шансом §415%");
         break;
      case 6:
         buildSkills.append("Повышает время действия хороших эффектов на §4").append((float)level * 0.45F).append("%UУменьшает время действия плохих эффектов на §4").append((float)level * 0.8F).append("%U§6[Max] §7Иммунитет к некоторым плохим эффектам");
         break;
      case 7:
         buildSkills.append("Снижение опыта для Стола Зачарований на §4").append((float)level * 1.5F).append("%UСнижение опыта для Наковальни на §4").append((float)level * 0.8F).append("%UСкидка в Кузнице (letragon.ru/store/smithy) на §4").append(level / 5).append("%U§6[Max] §7Максимальный заряд без книжных полок").append("U§6[Max] §7Предмет восстанавливает прочность");
         if (LetItems.loadThaumCraft) {
            buildSkills.append("U§5[ThaumCraft] §7Шанс получения аспектов §4").append((float)level * 0.4F).append("%");
         }

         if (LetItems.loadEP) {
            buildSkills.append("U§5[Enchanting Plus] §7Снижение монет на §4").append((float)level * 0.8F).append("%");
         }
         break;
      case 8:
         buildSkills.append("Увеличение показателей ценности еды на §4").append((float)level * 3.0F).append("%UСнижение увеличения истощения на §4").append(level).append("%UСнижение увеличения усталости персонажа на §4").append((float)level * 0.3F).append("%");
         if (LetItems.loadThaumCraft) {
            buildSkills.append("U§5[ThaumCraft] §6[Max] §7Печенье убирает временное искажение");
         }
         break;
      case 9:
         buildSkills.append("Увеличение урожая на 1-3 с шансом §4").append((float)level * 0.5F).append("%UПолучение §61-").append(level / 5 + 1).append("§7 опыта при сборе урожая с шансом §4").append(level).append("%");
         if (LetItems.loadThaumCraft) {
            buildSkills.append("U§5[ThaumCraft] §7Шанс получения аспектов §4").append((float)level / 5.0F).append("%");
         }
         break;
      case 10:
         buildSkills.append("Увеличение получаемого опыта на §4").append((float)level * 0.9F).append("%");
      }

      return buildSkills.toString();
   }

   private List<String> getTooltipQuest() {
      ArrayList<String> list = new ArrayList();
      if (!questList.isEmpty()) {
         int sizeQuestList = questList.size();

         for(int i = 0; i < sizeQuestList; ++i) {
            GuiGlobal.Quest quest = (GuiGlobal.Quest)questList.get(i);
            if (!this.modSettings.invMinQuestInfo) {
               list.add(EnumChatFormatting.BOLD + "" + EnumChatFormatting.GOLD + "Задание");
               list.add(EnumChatFormatting.GRAY + " - " + quest.getDesc());
               list.add(EnumChatFormatting.DARK_GRAY + " - - Остаётся " + quest.getNeed());
               if (i + 1 != sizeQuestList) {
                  list.add((Object)null);
               }
            } else {
               list.add(EnumChatFormatting.GRAY + " - " + quest.getDesc() + EnumChatFormatting.DARK_GRAY + " (остаётся " + quest.getNeed() + ")");
            }
         }
      } else {
         list.add(EnumChatFormatting.GOLD + "Заданий нет.");
         list.add(EnumChatFormatting.GRAY + "Переподключитесь, если уже получили задания.");
      }

      return list;
   }

   protected void actionPerformed(GuiButton button) {
      if (button.enabled && button.id != this.idGui) {
         switch(button.id) {
         case 0:
            InventoryTab.inventoryTab.openInventoryId(1, false);
            break;
         case 1:
            InventoryTab.inventoryTab.openInventoryId(5, false);
            break;
         case 2:
            InventoryTab.inventoryTab.openInventoryId(6, false);
            break;
         case 3:
            InventoryTab.inventoryTab.openInventoryId(8, false);
         }
      }

   }

   static {
      skillsList.add(new GuiGlobal.Skills("Добытчик Артефэ", "добыче кристаллов#Артефэ, отправке кристаллов на сайт#и обмене у куклы", 261, 14));
      skillsList.add(new GuiGlobal.Skills("Рыболов", "вылавливании#рыбы, мусора или сокровища", 285, 14));
      skillsList.add(new GuiGlobal.Skills("Защита", "получении#различного урона", 261, 36));
      skillsList.add(new GuiGlobal.Skills("Огнеупорность", "получении#урона от лавы и огня", 285, 36));
      skillsList.add(new GuiGlobal.Skills("Шахтёр", "добыче руды в шахтах", 261, 58));
      skillsList.add(new GuiGlobal.Skills("Атака", "нанесении#урона монстрам", 285, 58));
      skillsList.add(new GuiGlobal.Skills("Колдовство", "получении#различных эффектов", 261, 80));
      skillsList.add(new GuiGlobal.Skills("Зачарование", "использовании#стола зачарования и наковальни", 285, 80));
      skillsList.add(new GuiGlobal.Skills("Питание", "употреблении еды", 261, 102));
      skillsList.add(new GuiGlobal.Skills("Фермерство", "сборе урожая и#использовании костной муки", 285, 102));
      skillsList.add(new GuiGlobal.Skills("Путешественник", "получении опыта персонажа", 261, 124));
   }

   @SideOnly(Side.CLIENT)
   public static final class GuiPanelButton extends GuiButton {
      private final String text;

      public GuiPanelButton(int buttonId, String text, int x, int y, int width) {
         super(buttonId, x, y, width, 12, "");
         this.text = text;
      }

      public void drawButton(Minecraft mc, int mouseX, int mouseY) {
         this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         int color = this.field_146123_n ? 550161098 : 147507914;
         this.drawGradientRect(this.xPosition + this.width, this.yPosition + this.height, this.xPosition, this.yPosition, color, color);
         GL11.glPushMatrix();
         GL11.glTranslated((double)(this.xPosition + (this.id != 2 && this.id != 3 ? 4 : 5)), (double)(this.yPosition + 4), 0.0D);
         GL11.glScalef(0.75F, 0.75F, 0.75F);
         mc.fontRenderer.drawString(this.text, 0, 0, this.field_146123_n ? 14935011 : 11974326);
         GL11.glPopMatrix();
         this.mouseDragged(mc, mouseX, mouseY);
      }
   }

   public static class Quest {
      private final String desc;
      private final int need;

      public Quest(String desc, int need) {
         this.desc = desc;
         this.need = need;
      }

      public String getDesc() {
         return this.desc;
      }

      public int getNeed() {
         return this.need;
      }
   }

   public static class Skills {
      private final String name;
      public final String progress;
      private final int x;
      private final int y;
      private int level = 0;
      private String percent = null;

      public Skills(String name, String progress, int x, int y) {
         this.name = name;
         this.progress = progress;
         this.x = x;
         this.y = y;
      }

      public String getName() {
         return this.name;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public String getPercent() {
         return this.percent;
      }

      public void setPercent(String percent) {
         this.percent = percent;
      }

      public int getLevel() {
         return this.level;
      }

      public void setLevel(int level) {
         this.level = level;
      }
   }
}
