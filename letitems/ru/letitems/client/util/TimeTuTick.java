package ru.letitems.client.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Iterator;

@SideOnly(Side.CLIENT)
public final class TimeTuTick {
   public static TimeTuTick instance = new TimeTuTick();
   private HashMap<TimeTuTick.TypeTime, Long> timeTick = new HashMap(4);

   public void register(TimeTuTick.TypeTime key, long time) {
      if (!this.timeTick.containsKey(key) || (Long)this.timeTick.get(key) < time) {
         this.timeTick.put(key, time);
      }

   }

   public void delete(TimeTuTick.TypeTime key) {
      this.timeTick.remove(key);
   }

   public void update() {
      if (this.timeTick != null && !this.timeTick.isEmpty()) {
         TimeTuTick.TypeTime delete = null;
         Iterator var2 = this.timeTick.keySet().iterator();

         while(var2.hasNext()) {
            TimeTuTick.TypeTime key = (TimeTuTick.TypeTime)var2.next();
            long time = (Long)this.timeTick.get(key);
            if (time > 0L) {
               this.timeTick.put(key, time - 1L);
            } else {
               delete = key;
            }
         }

         if (delete != null) {
            this.delete(delete);
         }

      }
   }

   public long get(TimeTuTick.TypeTime key) {
      return this.timeTick.containsKey(key) ? (Long)this.timeTick.get(key) : 0L;
   }

   public static enum TypeTime {
      DIM180,
      WIZ_STORE,
      STORE,
      OTHER,
      KILLUA;
   }
}
