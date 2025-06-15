package ru.letitems.common.handler;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public final class WizStoreManager {
   public Map<String, Integer> playersWizStore = new HashMap(100);

   public int getWizStoreProgress(EntityPlayer player) {
      return -1;
   }

   public void loadPlayerWizStore(EntityPlayerMP player, int storeWiz) {
   }

   public void removePlayerWizStore(EntityPlayer player) {
   }

   public void rebuildPlayerWizStore(EntityPlayer player, int i) {
   }
}
