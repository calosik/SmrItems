package ru.letitems.modules.main;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.main.ServerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class GuiNewMainMenu extends GuiScreen {
   private static final ResourceLocation RESOURCE_LOGO = new ResourceLocation("letitems", "textures/gui/menu/logo.png");
   private static final ResourceLocation RESOURCE_BTN_SERVER = new ResourceLocation("letitems", "textures/gui/menu/btnServer.png");
   private static final ResourceLocation RESOURCE_BTN_HOME = new ResourceLocation("letitems", "textures/gui/menu/btnHome.png");
   private static final ResourceLocation RESOURCE_BTN_SPAWN = new ResourceLocation("letitems", "textures/gui/menu/btnSpawn.png");
   private static final ResourceLocation RESOURCE_BTN_SINGLE = new ResourceLocation("letitems", "textures/gui/menu/btnSingle.png");
   private static final ResourceLocation RESOURCE_BTN_OPTIONS = new ResourceLocation("letitems", "textures/gui/menu/btnOptions.png");
   private static final ResourceLocation RESOURCE_BTN_EXIT = new ResourceLocation("letitems", "textures/gui/menu/btnExit.png");
   private static ResourceLocation background;
   public static Object backgroundLocation;
   public static Object backgroundLocationHd;

   public GuiNewMainMenu() {
      ScaledResolution.isDefaultRescale = false;
      FMLClientHandler.instance().setupServerList();
   }

   public void initGui() {
      ScaledResolution.isDefaultRescale = false;
      ScaleGui.update();
      this.width = this.mc.displayWidth;
      this.height = this.mc.displayHeight;
      if (backgroundLocation instanceof BufferedImage) {
         background = this.mc.getTextureManager().getDynamicTextureLocation("imgBg", new DynamicTexture((BufferedImage)backgroundLocation));
      }

      int btnWidth = true;
      int btnHeight = true;
      int btnWidth2 = true;
      int btnHeight2 = true;
      this.buttonList.clear();
      this.buttonList.add(new GuiNewMainMenu.GuiImageButton(0, 959, 455, 714, 110, RESOURCE_BTN_SERVER));
      this.buttonList.add(new GuiNewMainMenu.GuiImageButton(1, 959, 571, 714, 110, RESOURCE_BTN_SINGLE));
      this.buttonList.add(new GuiNewMainMenu.GuiImageButton(2, 959, 687, 714, 110, RESOURCE_BTN_OPTIONS));
      this.buttonList.add(new GuiNewMainMenu.GuiImageButton(3, 1362, 195, 156, 46, RESOURCE_BTN_EXIT));
      this.buttonList.add(new GuiNewMainMenu.GuiImageButton(4, 872, 778, 156, 46, RESOURCE_BTN_HOME));
      this.buttonList.add(new GuiNewMainMenu.GuiImageButton(5, 1046, 778, 156, 46, RESOURCE_BTN_SPAWN));
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
      case 4:
      case 5:
         if (button.id != 0) {
            MenuMod.instance.teleportToNewLoc = button.id == 4 ? 2 : 1;
         }

         FMLClientHandler.instance().connectToServer(this, new ServerData("MinecraftServer", ServerInfo.serverInfo));
         ScaledResolution.isDefaultRescale = true;
         break;
      case 1:
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
         break;
      case 2:
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
         break;
      case 3:
         this.mc.shutdown();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTick) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GuiUtils.drawFullScreen(this.width, this.height, background);
      GuiUtils.drawCenterCentered(RESOURCE_LOGO, 959.0F, 300.0F, 958.0F, 139.0F, 255);
      super.drawScreen(mouseX, mouseY, partialTick);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char key, int keyId) {
   }

   public void onGuiClosed() {
      ScaledResolution.isDefaultRescale = true;
   }

   @SideOnly(Side.CLIENT)
   private static final class GuiImageButton extends GuiButton {
      private final ResourceLocation imageLocation;

      public GuiImageButton(int buttonId, int x, int y, int width, int height, ResourceLocation imageLocation) {
         super(buttonId, x, y, width, height, "");
         this.imageLocation = imageLocation;
      }

      public void drawButton(Minecraft mc, int mouseX, int mouseY) {
         if (this.visible) {
            int widthSize = (int)ScaleGui.get((float)this.width);
            int heightSize = (int)ScaleGui.get((float)this.height);
            int sx = (int)(ScaleGui.getCenterX((float)this.xPosition) - (float)widthSize / 2.0F);
            int sy = (int)(ScaleGui.getCenterY((float)this.yPosition) - (float)heightSize / 2.0F);
            this.field_146123_n = mouseX >= sx && mouseY >= sy && mouseX < sx + widthSize && mouseY < sy + heightSize;
            GuiUtils.drawCenterCentered(this.imageLocation, (float)this.xPosition, (float)this.yPosition, (float)this.width, (float)this.height, this.field_146123_n ? 255 : 150);
            this.mouseDragged(mc, mouseX, mouseY);
         }

      }

      public boolean mousePressed(Minecraft mc, int x, int y) {
         return this.enabled && this.field_146123_n;
      }

      public void func_146113_a(SoundHandler soundHandler) {
         soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("letitems:nav.but"), 1.25F));
      }
   }
}
