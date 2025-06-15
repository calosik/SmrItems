package ru.letitems.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import ru.letitems.client.util.TimeTuTick;

@SideOnly(Side.CLIENT)
public final class ClientParams {
   private static ZoneId timeZone = null;
   private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
   public static final Map<String, Boolean> archUnlock = new HashMap(3);
   public static boolean isOpenedPrimeStore = false;
   public static int userCoinsBox1 = 0;
   public static int userCoinsBox2 = 0;
   public static int userCoinsBox3 = 0;
   public static int userAchPoints = 0;
   public static int idKys = -1;

   public static boolean isUnlockAch(String id) {
      return !archUnlock.isEmpty() && (Boolean)archUnlock.get(id);
   }

   public static void setEndTimeToDIM180() {
      if (timeZone == null) {
         timeZone = ZoneId.of("Europe/Moscow");
      }

      int endTimeToDIM180 = 0;
      LocalDateTime dateTime = LocalDateTime.now(timeZone).with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

      try {
         endTimeToDIM180 = (int)(format.parse(dateTime.toLocalDate().toString()).getTime() / 1000L);
         endTimeToDIM180 += 7200;
      } catch (ParseException var3) {
      }

      if (endTimeToDIM180 > 0) {
         endTimeToDIM180 = (int)((long)endTimeToDIM180 - System.currentTimeMillis() / 1000L);
      }

      TimeTuTick.instance.register(TimeTuTick.TypeTime.DIM180, (long)endTimeToDIM180);
   }
}
