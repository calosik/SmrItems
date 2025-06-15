package ru.letitems.client.renderer.doll;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Calendar;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.model.ModelDoll;
import ru.letitems.client.model.acs.DollAcs1;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.tile.TileEntityDoll;

@SideOnly(Side.CLIENT)
public final class TileRenderDoll extends TileEntitySpecialRenderer {
   private static DollAcs1 DOLL_ACS_1;
   private static int renderAsc = 0;
   public static final ModelDoll MODEL_DOLL;

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
      renderAsc();
      GL11.glPopMatrix();
   }

   public static void renderAsc() {
      if (renderAsc != 0) {
         GeneralClientUtils.bind("letitems:models/blocks/acs/" + renderAsc + ".png");
         if (renderAsc == 1) {
            GL11.glTranslatef(0.0F, 0.111F, 0.05F);
            GL11.glRotatef(-2.0F, 2.0F, 1.0F, 0.0F);
            DOLL_ACS_1.renderModel(0.0625F);
         }

      }
   }

   static {
      Calendar calendar = Calendar.getInstance();
      int month = calendar.get(2);
      int day = calendar.get(5);
      if (month + 1 == 12 && day >= 24) {
         renderAsc = 1;
         DOLL_ACS_1 = new DollAcs1();
      }

      MODEL_DOLL = new ModelDoll();
   }
}
