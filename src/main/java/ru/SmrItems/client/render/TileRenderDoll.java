package ru.SmrItems.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import ru.SmrItems.client.model.ModelDoll;
import ru.SmrItems.common.tileentity.TileEntityDoll;

@SideOnly(Side.CLIENT)
public final class TileRenderDoll extends TileEntitySpecialRenderer {
   public static final ModelDoll MODEL_DOLL = new ModelDoll();

   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
      this.renderModel((TileEntityDoll)tile, x, y, z);
   }

   private void renderModel(TileEntityDoll tile, double x, double y, double z) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
      int direction = tile.direction;
      GL11.glRotatef(direction == 0 ? 180.0F : (float)(direction == 1 ? 90 : (direction == 2 ? 360 : 270)), 0.0F, 1.0F, 0.0F);
      GL11.glScalef(1.0F, -1.0F, -1.0F);
      this.bindTexture(tile.getType().getTextureLocation());
      MODEL_DOLL.renderModel(0.0625F);
      GL11.glPopMatrix();
   }
}
