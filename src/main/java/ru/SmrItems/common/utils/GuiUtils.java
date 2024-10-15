package ru.SmrItems.common.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public final class GuiUtils {
   public static void drawFullScreen(int screenWidth, int screenHeight, ResourceLocation resourceLocation) {
      if (resourceLocation != null) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
         drawFullScreen(screenWidth, screenHeight);
      }

   }

   public static void drawFullScreen(int screenWidth, int screenHeight) {
      drawImage(0, 0, screenWidth, screenHeight);
   }

   public static void drawImage(int x, int y, int width, int height, ResourceLocation resourceLocation) {
      if (resourceLocation != null) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
         drawImage(x, y, width, height);
      }

   }

   public static void drawImage(int x, int y, int width, int height) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)x, (double)(y + height), 0.0D, 0.0D, 1.0D);
      tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, 1.0D, 1.0D);
      tessellator.addVertexWithUV((double)(x + width), (double)y, 0.0D, 1.0D, 0.0D);
      tessellator.addVertexWithUV((double)x, (double)y, 0.0D, 0.0D, 0.0D);
      tessellator.draw();
   }
}
