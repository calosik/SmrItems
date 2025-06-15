package ru.SmrItems.modules.main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.SmrItems.util.GuiUtils;

@SideOnly(Side.CLIENT)
public final class GuiImageButton extends GuiButton {
   private final ResourceLocation imageLocation;
   private boolean field_146123_n;

   public GuiImageButton(int buttonId, int x, int y, ResourceLocation imageLocation) {
      super(buttonId, x, y, "");
      this.imageLocation = imageLocation;
   }

   public GuiImageButton(int buttonId, int x, int y, int width, int height, ResourceLocation imageLocation) {
      super(buttonId, x, y, width, height, "");
      this.imageLocation = imageLocation;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
         this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         mc.getTextureManager().bindTexture(this.imageLocation);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, this.field_146123_n ? 1.0F : 0.8F);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GuiUtils.drawImage(this.xPosition, this.yPosition, this.width, this.height);
         this.mouseDragged(mc, mouseX, mouseY);
      }

   }
}
