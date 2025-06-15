package ru.letitems.modules.main;

import net.minecraft.client.Minecraft;

public class ScaleGui {
   private static final Minecraft mc = Minecraft.getMinecraft();
   public static float FULL_HD = 1.7777778F;
   public static float screenCenterX;
   public static float screenCenterY;
   public static int screenWidth;
   public static int screenHeight;
   public static float scaleValue;
   public static final float DEFAULT_WIDTH = 1920.0F;
   public static final float HALF_WIDTH = 960.0F;
   public static final float DEFAULT_HEIGHT = 1080.0F;
   public static final float HALF_HEIGHT = 540.0F;

   public static void update() {
      refresh(FULL_HD);
   }

   private static void refresh(float minAspect) {
      screenWidth = mc.displayWidth;
      screenHeight = mc.displayHeight;
      screenCenterX = (float)screenWidth / 2.0F;
      screenCenterY = (float)screenHeight / 2.0F;
      float ratio = (float)screenWidth / (float)screenHeight;
      scaleValue = ratio < minAspect ? (float)screenHeight / (1.0F + (minAspect - ratio)) : (float)screenHeight;
   }

   public static float get(float value) {
      return scaleValue / (1080.0F / value);
   }

   public static float get(float value, float size) {
      return scaleValue / (1080.0F / value) - get(size) / 2.0F;
   }

   public static float getCenterX(float value) {
      return screenCenterX + scaleValue / (1080.0F / (value - 960.0F));
   }

   public static float getCenterX(float value, float size) {
      return screenCenterX + scaleValue / (1080.0F / (value - 960.0F)) - get(size) / 2.0F;
   }

   public static float getCenterY(float value) {
      return screenCenterY + scaleValue / (1080.0F / (value - 540.0F));
   }

   public static float getCenterY(float value, float size) {
      return screenCenterY + scaleValue / (1080.0F / (value - 540.0F)) - get(size) / 2.0F;
   }
}
