package ru.letitems.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.items.ItemScrollTitle;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketUpgradeScrollTitle;

/** @deprecated */
@Deprecated
@SideOnly(Side.CLIENT)
public final class GuiScrollTitle extends GuiScreen {
   public static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/scrollTitel.png");
   private GuiTextField nameField;
   private final EntityPlayer player;
   private final String titleScroll;
   private int posX;
   private int posY;
   private static final String[] strings = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

   public GuiScrollTitle(EntityPlayer player, ItemStack itemStack) {
      this.player = player;
      this.titleScroll = ItemScrollTitle.scrollInfo(itemStack).getString("title");
   }

   public void initGui() {
      super.initGui();
      this.posX = (this.width - 256) / 2;
      this.posY = (this.height - 190) / 2;
      Keyboard.enableRepeatEvents(true);
      this.nameField = new GuiTextField(this.fontRendererObj, this.posX + 103, this.posY + 74, 144, 17);
      this.nameField.setText(this.titleScroll);
      this.nameField.setEnableBackgroundDrawing(false);
      this.nameField.setFocused(true);
      this.nameField.setMaxStringLength(40);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      super.updateScreen();
      this.nameField.updateCursorCounter();
   }

   protected void keyTyped(char c, int i) {
      if (i == 1) {
         this.mc.displayGuiScreen((GuiScreen)null);
      } else if (i == 28) {
         this.sendTitle();
      }

      this.nameField.textboxKeyTyped(c, i);
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.fontRendererObj.drawString("ГОТОВО", this.posX + 148, this.posY + 127, 6842472);
      if (this.nameField != null && this.nameField.getText() != null) {
         this.nameField.drawTextBox();
      }

      this.fontRendererObj.drawString("Установка титула стоит 85 эссенции, с", this.posX + 18, this.posY + 151, 6842472);
      this.fontRendererObj.drawString("талантом 'Титулованный' стоит 4 обелиска.", this.posX + 18, this.posY + 161, 6842472);
      this.fontRendererObj.drawString("Максимальная длина строки 40 символов.", this.posX + 18, this.posY + 172, 6842472);
      GL11.glPushMatrix();
      this.fontRendererObj.drawString(this.player.getCommandSenderName(), this.posX + 25, this.posY + 10, 6842472);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.drawCenteredString(this.fontRendererObj, this.nameField.getText(), (int)((float)(this.posX + 45) / 0.5F), (int)((float)(this.posY + 20) / 0.5F), 6842472);
      GL11.glPopMatrix();
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(this.posX + 52), (float)(this.posY + 136 - 75), 50.0F);
      GL11.glScalef(-55.0F, 55.0F, 55.0F);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(65.0F, 0.0F, 1.0F, 0.0F);
      float f2 = this.mc.thePlayer.renderYawOffset;
      float f3 = this.mc.thePlayer.rotationYaw;
      float f4 = this.mc.thePlayer.rotationPitch;
      float f5 = this.mc.thePlayer.prevRotationYawHead;
      float f6 = this.mc.thePlayer.rotationYawHead;
      RenderHelper.enableStandardItemLighting();
      this.mc.thePlayer.renderYawOffset = (float)Math.atan(0.75D) * 20.0F;
      this.mc.thePlayer.rotationYaw = (float)Math.atan(0.75D) * 40.0F;
      this.mc.thePlayer.rotationPitch = -((float)Math.atan(0.75D)) * 20.0F;
      this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw;
      this.mc.thePlayer.prevRotationYawHead = this.mc.thePlayer.rotationYaw;
      RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      this.mc.thePlayer.renderYawOffset = f2;
      this.mc.thePlayer.rotationYaw = f3;
      this.mc.thePlayer.rotationPitch = f4;
      this.mc.thePlayer.prevRotationYawHead = f5;
      this.mc.thePlayer.rotationYawHead = f6;
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
      super.drawScreen(x, y, f);
   }

   protected void mouseClicked(int x, int y, int par3) {
      super.mouseClicked(x, y, par3);
      x -= this.posX;
      y -= this.posY;
      if (x >= 111 && x <= 219) {
         if (y >= 127 && y <= 135) {
            this.sendTitle();
            return;
         }

         x -= 115;
         int index = x / 14;
         if (y >= 94 && y <= 104) {
            if (x % 14 <= 9) {
               this.nameField.setText(this.addTextMod("§" + strings[index], this.nameField.getText()));
            }
         } else if (y >= 108 && y <= 118) {
            index += 8;
            if (x % 14 <= 9) {
               this.nameField.setText(this.addTextMod("§" + strings[index], this.nameField.getText()));
            }
         }
      }

   }

   private String addTextMod(String text, String line) {
      return this.nameField.getCursorPosition() > 0 && this.nameField.getCursorPosition() <= line.length() ? line.substring(0, this.nameField.getCursorPosition()) + text + line.substring(this.nameField.getCursorPosition()) : text + line;
   }

   private void sendTitle() {
      NetworkManager.sendToServer(new PacketUpgradeScrollTitle(this.nameField.getText()));
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
