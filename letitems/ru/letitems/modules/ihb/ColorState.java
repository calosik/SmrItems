package ru.letitems.modules.ihb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum ColorState {
   INNOCENT(-1812709633),
   VIOLENT(-191168257),
   KILLER(-1124073217),
   INVALID(-1953788929),
   GAMEMASTER(8639989);

   private final int color;

   private ColorState(int argb) {
      this.color = argb;
   }

   public static ColorState getColorState(Minecraft mc, Entity entity, float time) {
      if (entity instanceof EntityPlayer) {
         return ((EntityPlayer)entity).getGameProfile().getName().equals("Kirishima") ? GAMEMASTER : INNOCENT;
      } else if (entity instanceof EntityLiving) {
         return ((EntityLiving)entity).getAttackTarget() instanceof EntityPlayer ? KILLER : getState(mc, (EntityLiving)entity, time);
      } else {
         return INVALID;
      }
   }

   private static ColorState getState(Minecraft mc, EntityLiving entity, float time) {
      if (entity instanceof EntityWolf && ((EntityWolf)entity).isAngry()) {
         return KILLER;
      } else if (entity instanceof EntityTameable && ((EntityTameable)entity).isTamed()) {
         return ((EntityTameable)entity).getOwner() != mc.thePlayer ? getColorState(mc, ((EntityTameable)entity).getOwner(), time) : INNOCENT;
      } else if (entity instanceof IMob) {
         return KILLER;
      } else if (entity instanceof IAnimals) {
         return INNOCENT;
      } else {
         return entity instanceof IEntityOwnable ? VIOLENT : INVALID;
      }
   }

   public final void glColor() {
      float red = (float)(this.color >> 24 & 255) / 255.0F;
      float green = (float)(this.color >> 16 & 255) / 255.0F;
      float blue = (float)(this.color >> 8 & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, 1.0F);
   }
}
