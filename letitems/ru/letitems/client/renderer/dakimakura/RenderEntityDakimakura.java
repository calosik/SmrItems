package ru.letitems.client.renderer.dakimakura;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.model.ModelDakimakura;
import ru.letitems.common.entity.EntityDakimakura;

@SideOnly(Side.CLIENT)
public class RenderEntityDakimakura extends Render {
   private final ModelDakimakura modelDakimakura;

   public RenderEntityDakimakura(ModelDakimakura modelDakimakura) {
      this.modelDakimakura = modelDakimakura;
   }

   public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.mcProfiler.startSection("lt-daki-entity");
      float scale = 0.0625F;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glTranslatef(0.5F, -0.3125F, 0.5F);
      ForgeDirection rot = ((EntityDakimakura)entity).getRotation();
      if (rot == ForgeDirection.WEST) {
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      } else if (rot == ForgeDirection.NORTH) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (rot == ForgeDirection.EAST) {
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, 0.0F, 0.25F);
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      if (((EntityDakimakura)entity).isFlipped()) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      }

      this.modelDakimakura.render(((EntityDakimakura)entity).getDaki(), entity.posX, entity.posY, entity.posZ);
      GL11.glPopMatrix();
      Minecraft.getMinecraft().mcProfiler.endSection();
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return null;
   }
}
