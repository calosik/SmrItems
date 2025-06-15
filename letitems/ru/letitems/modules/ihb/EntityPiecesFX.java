package ru.letitems.modules.ihb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class EntityPiecesFX extends EntityFX {
   private final float smokeParticleScale;

   public EntityPiecesFX(World world, double xCoord, double yCoord, double zCoord) {
      super(world, xCoord, yCoord, zCoord);
      this.motionX *= 0.2D;
      this.motionY *= 0.2D;
      this.motionZ *= 0.2D;
      this.particleRed = 0.5F;
      this.particleGreen = 0.88F;
      this.particleBlue = 0.9F;
      this.particleScale = 0.2F * this.rand.nextFloat() * 0.6F + 1.0F;
      this.smokeParticleScale = this.particleScale;
      this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
      this.particleMaxAge = (int)((float)this.particleMaxAge * 2.0F);
      this.noClip = false;
   }

   public void renderParticle(Tessellator tessellator, float time, float x, float y, float z, float f0, float f1) {
      GL11.glEnable(3042);
      GL11.glBlendFunc(1, 1);
      this.particleScale = this.smokeParticleScale * MathHelper.clamp_float(((float)this.particleAge + time) / (float)this.particleMaxAge * 32.0F, 0.0F, 1.65F);
      super.renderParticle(tessellator, time, x, y, z, f0, f1);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      this.setParticleTextureIndex(3 - this.particleAge * 8 / this.particleMaxAge);
      this.moveEntity(0.0D, 0.02D, 0.0D);
   }
}
