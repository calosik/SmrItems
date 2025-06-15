package ru.letitems.client.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class GeneralClientUtils {
   private static HashMap<String, ResourceLocation> resources = new HashMap();

   public static ResourceLocation put(String textureName) {
      ResourceLocation res = (ResourceLocation)resources.get(textureName);
      if (res == null) {
         resources.put(textureName, res = new ResourceLocation(textureName));
      }

      return res;
   }

   public static void bind(String textureName) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(put(textureName));
   }

   @SideOnly(Side.CLIENT)
   public static class ScissorHelper {
      public static void startScissor(Minecraft minecraft, int x, int y, int w, int h) {
         ScaledResolution scaledRes = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
         startScissor(minecraft, scaledRes.getScaleFactor(), x, y, w, h);
      }

      public static void startScissor(Minecraft minecraft, int scale, int x, int y, int w, int h) {
         int scissorWidth = w * scale;
         int scissorHeight = h * scale;
         int scissorX = x * scale;
         int scissorY = minecraft.displayHeight - scissorHeight - y * scale;
         GL11.glEnable(3089);
         GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
      }

      public static void endScissor() {
         GL11.glDisable(3089);
      }
   }
}
