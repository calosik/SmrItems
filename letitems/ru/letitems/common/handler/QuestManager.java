package ru.letitems.common.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.entity.player.EntityPlayer;

public final class QuestManager {
   public Map<String, Map<Integer, QuestManager.QuestCluster>> playersQuest = new HashMap(100);
   private final ExecutorService executor = Executors.newSingleThreadExecutor();

   public Map<Integer, QuestManager.QuestCluster> getQuestsPlayer(EntityPlayer player) {
      return (Map)this.playersQuest.get(player.getDisplayName());
   }

   public void loadPlayerQuest(EntityPlayer player) {
   }

   public void removePlayerQuests(EntityPlayer player) {
   }

   public void rebuildPlayerQuests(EntityPlayer player, boolean clear) {
   }

   private void savePlayerQuest(EntityPlayer player, int i, int count) {
   }

   public void sendQuestChange(EntityPlayer player, int id, String args) {
   }

   // $FF: synthetic method
   private void lambda$savePlayerQuest$1(EntityPlayer player, int i, int count) {
   }

   // $FF: synthetic method
   private void lambda$loadPlayerQuest$0(EntityPlayer player) {
   }

   public static class QuestCluster {
      private int id;
      private int type;
      private int need;
      private String args;
      private String text;

      public QuestCluster(int id, int type, int need, String args, String text) {
      }

      public int getId() {
         return this.id;
      }

      public int getNeed() {
         return this.need;
      }

      public void setNeed(int count) {
      }

      public String getArgs() {
         return this.args;
      }

      public int getType() {
         return this.type;
      }

      public String getText() {
         return this.text;
      }
   }
}
