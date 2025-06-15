package ru.letitems.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockMinePortal;

@SideOnly(Side.CLIENT)
public final class TileRenderWorldPortal extends TileEntitySpecialRenderer {
   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
      this.renderModel((BlockMinePortal.TileEntityMinePortal)tile, x, y, z);
   }

   private void renderModel(BlockMinePortal.TileEntityMinePortal tile, double x, double y, double z) {
      if ((TimeTuTick.instance.get(TimeTuTick.TypeTime.DIM180) <= 0L || tile.active) && !(x * x + y * y + z * z > 468.0D)) {
         RenderManager renderManager = RenderManager.instance;
         if (renderManager != null) {
            FontRenderer fontrenderer = renderManager.getFontRenderer();
            if (fontrenderer != null) {
               GL11.glPushMatrix();
               GL11.glTranslated(x - 0.2D, y + 2.5D, z - 0.2D);
               GL11.glNormal3f(0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(renderManager.playerViewX * (float)(Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1 : 1), 1.0F, 0.0F, 0.0F);
               GL11.glScalef(-0.025F, -0.025F, 0.025F);
               GL11.glDepthMask(false);
               GL11.glDisable(2896);
               String name = this.buildTimeEnd();
               fontrenderer.drawStringWithShadow(name, -(fontrenderer.getStringWidth(name) / 2), 1, -1);
               GL11.glScalef(0.6F, 0.6F, 0.6F);
               fontrenderer.drawStringWithShadow("Шахтёрский мир", -41, 17, 8684676);
               GL11.glNormal3f(0.0F, -1.0F, 0.0F);
               GL11.glEnable(2896);
               GL11.glPopMatrix();
            }
         }

      }
   }

   private String buildTimeEnd() {
      float timeEnd = (float)TimeTuTick.instance.get(TimeTuTick.TypeTime.DIM180);
      return String.format("%.0f:%.0f:%.0f", Math.floor((double)(timeEnd / 3600.0F)), Math.floor((double)(timeEnd % 3600.0F / 60.0F)), timeEnd % 60.0F);
   }
}
