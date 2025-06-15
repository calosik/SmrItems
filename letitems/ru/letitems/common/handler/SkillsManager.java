package ru.letitems.common.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public final class SkillsManager {
   public Map<String, Map<SkillsManager.Skills, SkillsManager.SkillsTree>> playersSkills = new HashMap(100);
   private final ExecutorService executor = Executors.newFixedThreadPool(5);
   private static final SkillsManager.Skills[] skills = SkillsManager.Skills.values();

   public Map<SkillsManager.Skills, SkillsManager.SkillsTree> getSkillsPlayer(EntityPlayer player) {
      return (Map)this.playersSkills.get(player.getDisplayName());
   }

   public boolean isSkillPlayer(EntityPlayer player) {
      return this.playersSkills != null && this.getSkillsPlayer(player) != null;
   }

   public SkillsManager.SkillsTree getSkillPlayer(EntityPlayer player, SkillsManager.Skills sk) {
      return (SkillsManager.SkillsTree)this.getSkillsPlayer(player).get(sk);
   }

   public void loadPlayerSkill(EntityPlayer player) {
   }

   public void savePlayerSkill(EntityPlayer player, boolean del) {
   }

   public static int getSizeSkills() {
      return skills.length;
   }

   public static SkillsManager.Skills getSkillFromIndex(int index) {
      return skills[MathHelper.clamp_int(index, 0, skills.length - 1)];
   }

   // $FF: synthetic method
   private void lambda$savePlayerSkill$1(EntityPlayer player, boolean del) {
   }

   // $FF: synthetic method
   private void lambda$loadPlayerSkill$0(EntityPlayer player) {
   }

   public static enum Skills {
      MINE_ARTEFE("Добытчик Артефэ", 0.0F, "294:Добытчик"),
      FISH("Рыболов", 0.2F, "295:Рыбак!"),
      PROTECTION("Защита", 0.08F, "297:Любитель боли"),
      FIRE("Огнеупорность", 0.35F, "298:Создан из лавы"),
      MINER("Шахтёр", 0.0F, "299:Угольный мастер"),
      DAMAGE("Атака", 0.05F, "300:Мастер битв"),
      POTIONS("Колдовство", 0.2F, "301:Колдун и Маг"),
      ENCHANT("Зачарование", 0.5F, "302:Мастер зачарований"),
      FOOD("Питание", 0.2F, "303:Настоящий гурман"),
      FARMING("Фермерство", 0.1F, "367:Большой урожай"),
      ADVENTURER("Путешественник", 0.0F, "407:Время странствий");

      private final String name;
      private final String id;
      private final float v;

      private Skills(String name, float v, String id) {
         this.name = name;
         this.v = v;
         this.id = id;
      }

      public String getName() {
         return this.name;
      }

      public float getV() {
         return this.v;
      }

      public String getId() {
         return this.id;
      }
   }

   public static class SkillsTree {
      private SkillsManager.Skills skill;
      private int exp;
      private int level;
      private long lastUse;

      public SkillsTree(SkillsManager.Skills skill, String info) {
      }

      public int getExpSkill() {
         return this.exp;
      }

      public int getLevelSkill() {
         return this.level;
      }

      public void addExpSkill(EntityPlayer player, int exp) {
      }

      public String getFormate() {
         return String.format("%s,%s", this.level, this.exp);
      }

      public boolean isLock() {
         return this.lastUse > System.currentTimeMillis();
      }

      public void setLastUse() {
         this.setLastUse(40);
      }

      public void setLastUse(int time) {
      }
   }
}
