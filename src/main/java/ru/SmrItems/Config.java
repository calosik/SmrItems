package ru.SmrItems;

public final class Config {
   public static String fuelItem = "FuelUnstable";
   public static int fuelTime = 28800;
   public static int multiplier = 2;
   public static boolean enabledChunkLoader = true;

   public static void init() {
   }

   static {
      init();
   }
}
