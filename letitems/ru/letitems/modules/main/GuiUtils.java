package ru.letitems.modules.main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class GuiUtils {
   private static final Tessellator tessellator;

   public static void drawFullScreen(int screenWidth, int screenHeight, ResourceLocation resourceLocation) {
      if (resourceLocation != null) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
         drawImage(0, 0, screenWidth, screenHeight, 255);
      }

   }

   public static void drawImage(int x, int y, int width, int height, int alpha) {
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA(255, 255, 255, alpha);
      tessellator.addVertexWithUV((double)x, (double)(y + height), 0.0D, 0.0D, 1.0D);
      tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, 1.0D, 1.0D);
      tessellator.addVertexWithUV((double)(x + width), (double)y, 0.0D, 1.0D, 0.0D);
      tessellator.addVertexWithUV((double)x, (double)y, 0.0D, 0.0D, 0.0D);
      tessellator.draw();
   }

   public static void drawCenterCentered(ResourceLocation textureLocation, float x, float y, float width, float height, int alpha) {
      float size = 0.5F;
      width = ScaleGui.get(width, 0.5F);
      height = ScaleGui.get(height, 0.5F);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      drawRect(textureLocation, (double)(ScaleGui.getCenterX(x, 0.5F) - width / 2.0F), (double)(ScaleGui.getCenterY(y, 0.5F) - height / 2.0F), (double)width, (double)height, alpha);
   }

   public static void drawRect(ResourceLocation textureLocation, double x, double y, double width, double height, int alpha) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(textureLocation);
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA(255, 255, 255, alpha);
      tessellator.addVertexWithUV(x, y + height, 0.0D, 0.0D, 1.0D);
      tessellator.addVertexWithUV(x + width, y + height, 0.0D, 1.0D, 1.0D);
      tessellator.addVertexWithUV(x + width, y, 0.0D, 1.0D, 0.0D);
      tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
      tessellator.draw();
   }

   static {
      tessellator = Tessellator.instance;
   }
}
