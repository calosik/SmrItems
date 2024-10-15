package ru.SmrItems.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public final class ModelDoll extends ModelBase {
   private final ModelRenderer head;
   private final ModelRenderer headaccessory;
   private final ModelRenderer hair;
   private final ModelRenderer upperbody;
   private final ModelRenderer lowerbody;
   private final ModelRenderer rightchest;
   private final ModelRenderer leftchest;
   private final ModelRenderer rightarm;
   private final ModelRenderer leftarm;
   private final ModelRenderer waist;
   private final ModelRenderer rightleg;
   private final ModelRenderer leftleg;

   public ModelDoll() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6);
      this.head.setRotationPoint(0.0F, 15.0F, 2.0F);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0872665F, 0.0F, 0.0F);
      this.headaccessory = new ModelRenderer(this, 36, 0);
      this.headaccessory.addBox(-3.5F, -6.5F, -3.5F, 7, 7, 7);
      this.headaccessory.setRotationPoint(0.0F, 15.0F, 2.0F);
      this.headaccessory.mirror = true;
      this.setRotation(this.headaccessory, 0.0872665F, 0.0F, 0.0F);
      this.hair = new ModelRenderer(this, 36, 14);
      this.hair.addBox(-3.5F, -4.0F, 0.5F, 7, 9, 3);
      this.hair.setRotationPoint(0.0F, 15.0F, 2.0F);
      this.hair.mirror = true;
      this.setRotation(this.hair, 0.0872665F, 0.0F, 0.0F);
      this.upperbody = new ModelRenderer(this, 0, 12);
      this.upperbody.addBox(-2.5F, -4.0F, -1.5F, 5, 4, 3);
      this.upperbody.setRotationPoint(0.0F, 19.0F, 2.0F);
      this.upperbody.mirror = true;
      this.setRotation(this.upperbody, 0.0F, 0.0F, 0.0F);
      this.lowerbody = new ModelRenderer(this, 0, 19);
      this.lowerbody.addBox(-2.5F, 0.0F, -1.5F, 5, 4, 3);
      this.lowerbody.setRotationPoint(0.0F, 19.0F, 2.0F);
      this.lowerbody.mirror = true;
      this.setRotation(this.lowerbody, -0.1745329F, 0.0F, 0.0F);
      this.rightchest = new ModelRenderer(this, 0, 26);
      this.rightchest.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
      this.rightchest.setRotationPoint(-1.3F, 17.0F, 0.5F);
      this.rightchest.mirror = true;
      this.setRotation(this.rightchest, 0.7853982F, 0.1745329F, 0.0872665F);
      this.leftchest = new ModelRenderer(this, 8, 26);
      this.leftchest.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
      this.leftchest.setRotationPoint(1.3F, 17.0F, 0.5F);
      this.leftchest.mirror = true;
      this.setRotation(this.leftchest, 0.7853982F, -0.1745329F, -0.0872665F);
      this.rightarm = new ModelRenderer(this, 16, 12);
      this.rightarm.addBox(-2.0F, -1.0F, -1.0F, 2, 7, 2);
      this.rightarm.setRotationPoint(-2.5F, 16.5F, 2.0F);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.1745329F);
      this.leftarm = new ModelRenderer(this, 16, 12);
      this.leftarm.addBox(0.0F, -1.0F, -1.0F, 2, 7, 2);
      this.leftarm.setRotationPoint(2.5F, 16.5F, 2.0F);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, -0.1745329F);
      this.waist = new ModelRenderer(this, 36, 26);
      this.waist.addBox(-3.0F, 4.0F, -2.0F, 6, 2, 4);
      this.waist.setRotationPoint(0.0F, 18.0F, 2.0F);
      this.waist.mirror = true;
      this.setRotation(this.waist, -0.1745329F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 24, 12);
      this.rightleg.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
      this.rightleg.setRotationPoint(-1.5F, 23.0F, 2.0F);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, -1.570796F, 0.1745329F, 0.0F);
      this.leftleg = new ModelRenderer(this, 24, 12);
      this.leftleg.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
      this.leftleg.setRotationPoint(1.5F, 23.0F, 2.0F);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, -1.570796F, -0.1745329F, 0.0F);
   }

   public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      super.render(entity, par2, par3, par4, par5, par6, par7);
      this.renderModel(par7);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void renderModel(float scale) {
      this.head.render(scale);
      this.headaccessory.render(scale);
      this.hair.render(scale);
      this.upperbody.render(scale);
      this.lowerbody.render(scale);
      this.rightchest.render(scale);
      this.leftchest.render(scale);
      this.rightarm.render(scale);
      this.leftarm.render(scale);
      this.waist.render(scale);
      this.rightleg.render(scale);
      this.leftleg.render(scale);
   }
}
