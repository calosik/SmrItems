package ru.SmrItems.utils;

import java.util.concurrent.TimeUnit;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;

public class Utils {
   public static String getMode(int mode) {
      if (mode == 0) {
         return I18n.format("gui.loadingModeSmall.text", new Object[0]);
      } else {
         return mode == 1 ? I18n.format("gui.loadingModeNormal.text", new Object[0]) : I18n.format("gui.loadingModeLarge.text", new Object[0]);
      }
   }

   public static String getPaused(boolean paused) {
      return paused ? I18n.format("gui.pausedOFF.text", new Object[0]) : I18n.format("gui.pausedON.text", new Object[0]);
   }

   public static String getChunkLoadingTime(int ticks) {
      return ticks == 0 ? "0" + getI18n("day") + " 0" + getI18n("hour") + " 0" + getI18n("second") : convertSecToDHMS(ticks / 20);
   }

   public static String convertSecToDHMS(int sec) {
      int day = (int)TimeUnit.SECONDS.toDays((long)sec);
      long hours = TimeUnit.SECONDS.toHours((long)sec) - TimeUnit.DAYS.toHours((long)day);
      long minute = TimeUnit.SECONDS.toMinutes((long)sec) - TimeUnit.DAYS.toMinutes((long)day) - TimeUnit.HOURS.toMinutes(hours);
      long second = TimeUnit.SECONDS.toSeconds((long)sec) - TimeUnit.DAYS.toSeconds((long)day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);
      return day > 0 ? day + "" + getI18n("day") + " " + hours + "" + getI18n("hour") + " " + minute + getI18n("minute") : hours + "" + getI18n("hour") + " " + minute + "" + getI18n("minute") + " " + second + getI18n("second");
   }

   private static String getI18n(String s) {
      return I18n.format("gui." + s + "Short.text", new Object[0]);
   }

   public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
      double dx = x - x1;
      double dy = y - y1;
      double dz = z - z1;
      return (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
   }
}
