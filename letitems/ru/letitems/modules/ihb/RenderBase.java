package ru.letitems.modules.ihb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderBase extends RenderPlayer {
   private final Minecraft mc = Minecraft.getMinecraft();
   private final Render parent;
   private static final ResourceLocation entities = new ResourceLocation("letitems", "textures/gui/inb.bar.png");

   public RenderBase(Render render) {
      this.parent = render;
   }

   public Render realRender() {
      return this.parent;
   }

   public void doRender(Entity entity, double x, double y, double z, float f0, float f1) {
      boolean dead = false;
      boolean deadStart = false;
      boolean deadExactly = false;
      if (entity instanceof EntityLivingBase) {
         EntityLivingBase living = (EntityLivingBase)entity;
         dead = Core.instance.getHealth(this.mc, living, -1.0F) <= 0.0F;
         deadStart = living.deathTime == 1;
         deadExactly = living.deathTime >= 18;
         if (deadStart) {
            ++living.deathTime;
         }
      }

      this.parent.doRender(entity, x, y, z, f0, f1);
      if (entity instanceof EntityLivingBase && !dead && !entity.isInvisibleToPlayer(this.mc.thePlayer)) {
         this.doRenderColorCursor(this.mc, entity, x, y, z);
         if (!entity.equals(this.mc.thePlayer)) {
            this.doRenderHealthBar(this.mc, entity, x, y, z);
         }
      }

      if (deadExactly) {
         this.doSpawnDeathParticles(this.mc, entity);
         entity.setDead();
      }

   }

   private void doRenderColorCursor(Minecraft mc, Entity entity, double x, double y, double z) {
      if (Core.GAUGE_ENABLED && entity.riddenByEntity == null && (entity instanceof IMob || Core.instance.getHealth(mc, entity, -1.0F) != Core.instance.getMaxHealth(entity) || entity instanceof EntityPlayer)) {
         double d3 = entity.getDistanceSqToEntity(super.renderManager.livingPlayer);
         if (d3 <= 4096.0D) {
            float sizeMult = ((EntityLivingBase)entity).isChild() && entity instanceof EntityMob ? 0.5F : 1.0F;
            float f1 = 0.0208F * sizeMult;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.0F, (float)y + sizeMult * entity.height + sizeMult * 0.8F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(2896);
            GL11.glEnable(2929);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            this.glBindTexture(mc);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            if (entity instanceof EntityLiving || entity instanceof EntityPlayer) {
               ColorState.getColorState(mc, entity, -1.0F).glColor();
            }

            double a = (double)(entity.worldObj.getTotalWorldTime() % 40L) / 20.0D * 3.141592653589793D;
            double cos = Math.cos(a);
            double sin = Math.sin(a);
            if (a > 1.5707963267948966D && a <= 4.71238898038469D) {
               tessellator.addVertexWithUV(15.0D * cos, -1.0D, 9.0D * sin, 0.125D, 0.25D);
               tessellator.addVertexWithUV(15.0D * cos, 17.0D, 9.0D * sin, 0.125D, 0.375D);
               tessellator.addVertexWithUV(-15.0D * cos, 17.0D, -9.0D * sin, 0.0D, 0.375D);
               tessellator.addVertexWithUV(-15.0D * cos, -1.0D, -9.0D * sin, 0.0D, 0.25D);
            } else {
               tessellator.addVertexWithUV(-5.0D * cos, -1.0D, -9.0D * sin, 0.0D, 0.25D);
               tessellator.addVertexWithUV(-5.0D * cos, 17.0D, -9.0D * sin, 0.0D, 0.375D);
               tessellator.addVertexWithUV(5.0D * cos, 17.0D, 9.0D * sin, 0.125D, 0.375D);
               tessellator.addVertexWithUV(5.0D * cos, -1.0D, 9.0D * sin, 0.125D, 0.25D);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            if (a < 3.141592653589793D) {
               tessellator.addVertexWithUV(-12.0D * sin, -1.0D, 9.0D * cos, 0.125D, 0.25D);
               tessellator.addVertexWithUV(-12.0D * sin, 17.0D, 9.0D * cos, 0.125D, 0.375D);
               tessellator.addVertexWithUV(12.0D * sin, 17.0D, -9.0D * cos, 0.0D, 0.375D);
               tessellator.addVertexWithUV(12.0D * sin, -1.0D, -9.0D * cos, 0.0D, 0.25D);
            } else {
               tessellator.addVertexWithUV(19.0D * sin, -1.0D, -9.0D * cos, 0.0D, 0.25D);
               tessellator.addVertexWithUV(19.0D * sin, 17.0D, -9.0D * cos, 0.0D, 0.375D);
               tessellator.addVertexWithUV(-19.0D * sin, 17.0D, 9.0D * cos, 0.125D, 0.375D);
               tessellator.addVertexWithUV(-19.0D * sin, -1.0D, 9.0D * cos, 0.125D, 0.25D);
            }

            tessellator.draw();
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(2896);
            GL11.glPopMatrix();
         }
      }

   }

   private void doRenderHealthBar(Minecraft mc, Entity entity, double x, double y, double z) {
      if (Core.HP_ENABLED && (entity.riddenByEntity == null || entity.riddenByEntity != mc.thePlayer) && (entity instanceof IMob || Core.instance.getHealth(mc, entity, -1.0F) != Core.instance.getMaxHealth(entity) || entity instanceof EntityPlayer)) {
         this.glBindTexture(mc);
         Tessellator tessellator = Tessellator.instance;
         GL11.glEnable(2929);
         GL11.glDisable(2884);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         int hitPoints = (int)(this.getHealthFactor(mc, entity) * 32.0F);
         if (entity instanceof EntityLivingBase) {
            HealthStep.getStep(mc, (EntityLivingBase)entity, -1.0F).glColor();
         } else {
            HealthStep.GOOD.glColor();
         }

         tessellator.startDrawing(5);
         float sizeMult = 1.0F;
         if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isChild() && entity instanceof EntityMob) {
            sizeMult = 0.5F;
         }

         int i;
         double value;
         double rad;
         double x0;
         double y0;
         double z0;
         for(i = 0; i <= hitPoints; ++i) {
            value = (double)(i + 32 - hitPoints) / 32.0D;
            rad = Math.toRadians((double)(super.renderManager.playerViewY - 60.0F)) + (value - 0.5D) * 3.141592653589793D * 0.3499999940395355D;
            x0 = x + (double)(sizeMult * entity.width) * 1.2D * Math.cos(rad);
            y0 = y + (double)(sizeMult * entity.height * 0.6F);
            z0 = z + (double)(sizeMult * entity.width) * 1.2D * Math.sin(rad);
            double uv_value = value - (double)(32 - hitPoints) / 32.0D;
            tessellator.addVertexWithUV(x0, y0 + 0.16D, z0, 1.0D - uv_value, 0.0D);
            tessellator.addVertexWithUV(x0, y0, z0, 1.0D - uv_value, 0.11D);
         }

         tessellator.draw();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         tessellator.startDrawing(5);

         for(i = 0; i <= 32; ++i) {
            value = (double)i / 32.0D;
            rad = Math.toRadians((double)(super.renderManager.playerViewY - 60.0F)) + (value - 0.5D) * 3.141592653589793D * 0.3499999940395355D;
            x0 = x + (double)(sizeMult * entity.width) * 1.2D * Math.cos(rad);
            y0 = y + (double)(sizeMult * entity.height * 0.6F);
            z0 = z + (double)(sizeMult * entity.width) * 1.2D * Math.sin(rad);
            tessellator.addVertexWithUV(x0, y0 + 0.16D, z0, 1.0D - value, 0.125D);
            tessellator.addVertexWithUV(x0, y0, z0, 1.0D - value, 0.24D);
         }

         tessellator.draw();
         GL11.glEnable(2884);
      }

   }

   private void doSpawnDeathParticles(Minecraft mc, Entity entity) {
      if (entity.worldObj != null) {
         for(int i = 0; i < 2; ++i) {
            double x0 = (double)entity.width * (Math.random() * 2.0D - 1.0D) * 0.75D;
            double y0 = (double)entity.height * Math.random();
            double z0 = (double)entity.width * (Math.random() * 2.0D - 1.0D) * 0.75D;
            mc.effectRenderer.addEffect(new EntityPiecesFX(entity.worldObj, entity.posX + x0, entity.posY + y0, entity.posZ + z0));
         }
      }

   }

   private float getHealthFactor(Minecraft mc, Entity entity) {
      float normalFactor = Core.instance.getHealth(mc, entity, -1.0F) / Core.instance.getMaxHealth(entity);
      float delta = 1.0F - normalFactor;
      return normalFactor + delta * delta / 2.0F * normalFactor;
   }

   private void glBindTexture(Minecraft mc) {
      if (mc != null) {
         mc.getTextureManager().bindTexture(entities);
      }

   }
}
