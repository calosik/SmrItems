package ru.letitems.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.InventoryTab;
import ru.letitems.client.model.ModelHairBand;
import ru.letitems.common.inventory.ExtendedPlayer;
import ru.letitems.common.items.ItemHairBand;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketGuid;

@SideOnly(Side.CLIENT)
public final class GuiHairUpgrade extends GuiScreen {
   public static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/upgradeHairs.png");
   private final ModelHairBand[] HairRender;
   private ModelHairBand MainHairRender;
   private final EntityPlayer player;
   private final byte sizeHair;
   private float screenZoom = 1.0F;
   private double anchorx = 0.0D;
   private double anchory = 0.0D;
   private double rotx = 0.0D;
   private double roty = 0.0D;
   private boolean dragging = false;
   private int posX;
   private int posY;
   private byte colorPlayer = 0;
   private static final ArrayList<String> listText = new ArrayList(Arrays.asList("§6Выбор стиля косички", "§7Панель выбора различных вариаций косички", "§7Косичку можно отдалить и повернуть"));

   public GuiHairUpgrade(EntityPlayer player) {
      this.player = player;
      ItemStack itemHairBand = ExtendedPlayer.getExtendedPlayer(player).getHair();
      ItemHairBand.HairBandType hairBandType = ItemHairBand.getType(itemHairBand);
      if (hairBandType.getParts() == 0) {
         this.closeScreen();
      }

      if (itemHairBand != null && itemHairBand.hasTagCompound()) {
         this.colorPlayer = itemHairBand.getTagCompound().getByte("type");
      }

      this.sizeHair = hairBandType.getSize();
      this.HairRender = new ModelHairBand[hairBandType.getParts() + 1];

      for(byte i = 0; i <= this.HairRender.length - 1; ++i) {
         this.HairRender[i] = new ModelHairBand(hairBandType.ordinal(), i != 0 ? "_" + i : null);
      }

      this.MainHairRender = this.HairRender[this.colorPlayer];
      this.allowUserInput = false;
   }

   public void initGui() {
      super.initGui();
      this.buttonList.clear();
      this.posX = (this.width - 256) / 2;
      this.posY = (this.height - 190) / 2;
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      int k = Mouse.getDWheel();
      if (k < 0) {
         this.screenZoom += 0.25F;
      } else if (k > 0) {
         this.screenZoom -= 0.25F;
      }

      this.screenZoom = MathHelper.clamp_float(this.screenZoom, 1.0F, 1.75F);
      GL11.glPushMatrix();
      GL11.glScalef(1.1F, 1.1F, 1.1F);
      this.fontRendererObj.drawString("Повернуть", (int)((float)(this.posX + 100) / 1.1F), (int)((float)(this.posY + 115) / 1.1F), 7105644);
      GL11.glPopMatrix();
      if (this.dragging) {
         double mx = (double)(Mouse.getEventX() * this.width) / (double)this.mc.displayWidth;
         double my = (double)this.height - (double)(Mouse.getEventY() * this.height) / (double)this.mc.displayHeight - 1.0D;
         double dy = my - this.anchory;
         this.rotx = this.rotx + dy < -90.0D ? -90.0D : (this.rotx + dy > 90.0D ? 90.0D : this.rotx + dy);
         this.roty -= mx - this.anchorx;
         this.anchorx = mx;
         this.anchory = my;
      }

      this.fontRendererObj.drawString("Выберите вариацию косички", this.posX + 56, this.posY + 165, 6842472);
      int xx = x - this.posX;
      int yy = y - this.posY;
      this.fontRendererObj.drawString("Назад в инвентарь", this.posX + 82, this.posY + 175, xx > 60 && xx < 180 && yy > 172 && yy < 185 ? -1 : 4802889);
      int scaleX = 70 - this.sizeHair * 4;
      int scaleY = 15 + this.sizeHair * 20;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(this.posX + scaleX), (float)(this.posY + scaleY), -3.0F + this.zLevel);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glScalef(65.0F, 65.0F, 65.0F);
      GL11.glTranslatef(1.0F, 0.5F, 1.0F);
      GL11.glScalef(1.0F, 1.0F, -1.0F);
      GL11.glClear(256);
      GL11.glDisable(2884);
      GL11.glRotatef(55.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef((float)this.rotx, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef((float)this.roty, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(1.0F / this.screenZoom, 1.0F / this.screenZoom, 1.0F);
      this.MainHairRender.render(this.player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
      byte count = (byte)(this.HairRender.length - 1);

      for(byte i = 0; i <= count; ++i) {
         this.renderAllHairs(i, count);
      }

      super.drawScreen(x, y, f);
      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(listText, x + 4, y + 4, this.fontRendererObj);
      }

   }

   private void renderAllHairs(byte i, byte count) {
      int hairX = 85 - 17 * count;
      hairX += (i + 1) * 35;
      if (this.buttonList.size() <= count) {
         this.buttonList.add(new GuiHairUpgrade.GuiImageButton(i, this.posX - 5 + hairX, this.posY + 130, this.colorPlayer));
      }

      hairX -= this.sizeHair;
      int scaleY = 138 + this.sizeHair * 3;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(this.posX + hairX), (float)(this.posY + scaleY), -3.0F + this.zLevel);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glScalef(12.0F, 12.0F, 12.0F);
      GL11.glTranslatef(1.0F, 0.5F, 1.0F);
      GL11.glScalef(1.0F, 1.0F, -1.0F);
      GL11.glClear(256);
      GL11.glDisable(2884);
      GL11.glRotatef(55.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      this.HairRender[i].render(this.player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
   }

   protected void actionPerformed(GuiButton button) {
      if (button.enabled) {
         NetworkManager.sendToServer(new PacketGuid((byte)2, button.id));
         this.MainHairRender = this.HairRender[button.id];
         this.colorPlayer = (byte)button.id;
         this.buttonList.clear();
         ModelHairBand.hairPlayers.replace(this.player.getUniqueID(), (byte)button.id);
      }

   }

   protected void mouseMovedOrUp(int x, int y, int which) {
      this.dragging = false;
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      this.anchorx = (double)x;
      this.anchory = (double)y;
      x -= this.posX;
      y -= this.posY;
      if (x > 90 && x < 180 && y > 105 && y < 130) {
         this.dragging = true;
      } else if (x > 60 && x < 180 && y > 172 && y < 185) {
         InventoryTab.inventoryTab.openInventoryId(1, false);
      }

   }

   public void updateScreen() {
      if (this.player.isDead) {
         this.closeScreen();
      }

   }

   private void closeScreen() {
      this.mc.displayGuiScreen((GuiScreen)null);
      this.mc.setIngameFocus();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public static final class GuiImageButton extends GuiButton {
      private final byte active;

      public GuiImageButton(int buttonId, int x, int y, byte colorPlayer) {
         super(buttonId, x, y, 30, 30, "");
         this.active = colorPlayer;
      }

      public void drawButton(Minecraft mc, int mouseX, int mouseY) {
         if (this.visible) {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if (this.id == this.active) {
               GL11.glColor4f(0.5F, 1.0F, 0.2F, this.field_146123_n ? 1.0F : 0.6F);
            } else {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, this.field_146123_n ? 1.0F : 0.4F);
            }

            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            mc.getTextureManager().bindTexture(GuiHairUpgrade.RESOURCE);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 190, 30, 30);
            this.mouseDragged(mc, mouseX, mouseY);
         }

      }
   }
}
