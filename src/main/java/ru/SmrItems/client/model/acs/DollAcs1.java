package ru.SmrItems.client.model.acs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class DollAcs1 extends ModelBase {
   public ModelRenderer v1;
   public ModelRenderer v2;
   public ModelRenderer v3;
   public ModelRenderer v4;
   public ModelRenderer v5;
   public ModelRenderer v6;

   public DollAcs1() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.v2 = new ModelRenderer(this, 0, 23);
      this.v2.setRotationPoint(-3.0F, 3.0F, -2.0F);
      this.v2.addBox(0.0F, 2.0F, 0.0F, 6, 2, 6, 0.0F);
      this.setRotateAngle(this.v2, 0.05235988F, 0.0F, 0.0F);
      this.v5 = new ModelRenderer(this, 0, 24);
      this.v5.setRotationPoint(-0.5F, 2.0F, 0.7F);
      this.v5.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
      this.setRotateAngle(this.v5, -0.13962634F, 0.0F, 0.0F);
      this.v1 = new ModelRenderer(this, 0, 0);
      this.v1.setRotationPoint(-3.0F, 7.0F, -3.0F);
      this.v1.addBox(-1.0F, 0.0F, 0.0F, 8, 2, 8, 0.0F);
      this.setRotateAngle(this.v1, 0.08726646F, 0.0F, 0.0F);
      this.v3 = new ModelRenderer(this, 0, 22);
      this.v3.setRotationPoint(-2.0F, 4.0F, -1.0F);
      this.v3.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);
      this.setRotateAngle(this.v3, -0.017453292F, 0.0F, 0.0F);
      this.v6 = new ModelRenderer(this, 0, 0);
      this.v6.setRotationPoint(-0.7F, 0.7F, 0.1F);
      this.v6.addBox(0.0F, 0.0F, 1.0F, 2, 2, 2, 0.1F);
      this.setRotateAngle(this.v6, -0.13962634F, 0.0F, 0.0F);
      this.v4 = new ModelRenderer(this, 24, 23);
      this.v4.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.v4.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
      this.setRotateAngle(this.v4, -0.034906585F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.renderModel(f5);
   }

   public void renderModel(float scale) {
      this.v2.render(scale);
      GL11.glPushMatrix();
      GL11.glTranslatef(this.v5.offsetX, this.v5.offsetY, this.v5.offsetZ);
      GL11.glTranslatef(this.v5.rotationPointX * scale, this.v5.rotationPointY * scale, this.v5.rotationPointZ * scale);
      GL11.glScaled(0.5D, 0.4D, 0.4D);
      GL11.glTranslatef(-this.v5.offsetX, -this.v5.offsetY, -this.v5.offsetZ);
      GL11.glTranslatef(-this.v5.rotationPointX * scale, -this.v5.rotationPointY * scale, -this.v5.rotationPointZ * scale);
      this.v5.render(scale);
      GL11.glPopMatrix();
      this.v1.render(scale);
      this.v3.render(scale);
      GL11.glPushMatrix();
      GL11.glTranslatef(this.v6.offsetX, this.v6.offsetY, this.v6.offsetZ);
      GL11.glTranslatef(this.v6.rotationPointX * scale, this.v6.rotationPointY * scale, this.v6.rotationPointZ * scale);
      GL11.glScaled(0.7D, 0.6D, 0.6D);
      GL11.glTranslatef(-this.v6.offsetX, -this.v6.offsetY, -this.v6.offsetZ);
      GL11.glTranslatef(-this.v6.rotationPointX * scale, -this.v6.rotationPointY * scale, -this.v6.rotationPointZ * scale);
      this.v6.render(scale);
      GL11.glPopMatrix();
      this.v4.render(scale);
   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }
}
