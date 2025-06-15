package ru.letitems.common.handler;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;

public final class DollMechKilluaZoldyck {
   public static final Class<? extends EntityLivingBase>[] CREATURE_CLASSES = new Class[]{EntityChicken.class, EntityPig.class, EntitySheep.class, EntityCow.class};
   private static final int CREATURE_CLASSES_LENGTH;

   public static String getEntityClassName(Class<? extends Entity> entityClass) {
      if (entityClass != null) {
         EntityRegistration entityRegistration = EntityRegistry.instance().lookupModSpawn(entityClass, true);
         if (entityRegistration != null) {
            return entityRegistration.getEntityName();
         }

         String name = (String)EntityList.classToStringMapping.get(entityClass);
         if (name != null) {
            return name;
         }
      }

      return "";
   }

   static {
      CREATURE_CLASSES_LENGTH = CREATURE_CLASSES.length;
   }

   public static class DollKilluaZoldyck {
      public int mobType;
      public long reloadTime;

      public void setMobType(Random rand) {
         this.mobType = rand.nextInt(DollMechKilluaZoldyck.CREATURE_CLASSES_LENGTH);
      }
   }
}
