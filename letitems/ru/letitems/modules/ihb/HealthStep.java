package ru.letitems.modules.ihb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum HealthStep {
   VERY_LOW(0.1F, -1124073217),
   LOW(0.2F, -201326337),
   VERY_DAMAGED(0.35F, -193462017),
   DAMAGED(0.5F, -188940033),
   OKAY(0.6F, -303351553),
   GOOD(1.0F, -1812709633),
   CREATIVE(-1.0F, -1288838145);

   private final float healthLimit;
   private final int color;

   private HealthStep(float limit, int argb) {
      this.healthLimit = limit;
      this.color = argb;
   }

   private float getLimit() {
      return this.healthLimit;
   }

   public final void glColor() {
      float red = (float)(this.color >> 24 & 255) / 255.0F;
      float green = (float)(this.color >> 16 & 255) / 255.0F;
      float blue = (float)(this.color >> 8 & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, 1.0F);
   }

   public static HealthStep getStep(Minecraft mc, EntityLivingBase entity, float time) {
      if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode) {
         return CREATIVE;
      } else {
         float value = Core.instance.getHealth(mc, entity, time) / Core.instance.getMaxHealth(entity);

         HealthStep step;
         for(step = first(); value > step.getLimit() && step.ordinal() + 1 < values().length; step = next(step)) {
         }

         return step;
      }
   }

   private static HealthStep first() {
      return values()[0];
   }

   private static HealthStep next(HealthStep step) {
      return values()[step.ordinal() + 1];
   }
}
