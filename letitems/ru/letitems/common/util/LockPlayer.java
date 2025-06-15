package ru.letitems.common.util;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.EntityPlayer;

public final class LockPlayer {
   public final ConcurrentHashMap<UUID, Boolean> locked = new ConcurrentHashMap(100);

   public boolean isLocked(UUID uuid) {
      return this.locked.get(uuid) != null;
   }

   public void tryLock(UUID uuid) {
      this.locked.put(uuid, true);
   }

   public void unlock(UUID uuid) {
      this.locked.remove(uuid);
   }

   public void sendMessage(EntityPlayer player) {
   }
}
