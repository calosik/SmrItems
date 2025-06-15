package ru.letitems.client.renderer.dakimakura;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.model.ModelDakimakura;
import ru.letitems.common.block.BlockDakimakura;
import ru.letitems.common.tile.TileDakimakura;

@SideOnly(Side.CLIENT)
public class RenderBlockDakimakura extends TileEntitySpecialRenderer {
   private final ModelDakimakura modelDakimakura;

   public RenderBlockDakimakura(ModelDakimakura modelDakimakura) {
      this.modelDakimakura = modelDakimakura;
   }

   public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTickTime) {
      this.renderTileEntityAt((TileDakimakura)tileEntity, x, y, z);
   }

   private void renderTileEntityAt(TileDakimakura tileEntity, double x, double y, double z) {
      int meta = tileEntity.getBlockMetadata();
      if (!BlockDakimakura.isTopPart(meta)) {
         Minecraft mc = Minecraft.getMinecraft();
         mc.mcProfiler.startSection("lt-daki-block");
         ForgeDirection rot = BlockDakimakura.getRotation(meta);
         GL11.glPushMatrix();
         GL11.glTranslated(x, y, z);
         GL11.glTranslatef(0.5F, 0.125F, 0.5F);
         if (rot == ForgeDirection.WEST) {
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         } else if (rot == ForgeDirection.NORTH) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         } else if (rot == ForgeDirection.EAST) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         }

         GL11.glTranslatef(0.0F, 0.0F, 0.25F);
         if (!BlockDakimakura.isStanding(meta)) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         } else {
            GL11.glTranslatef(0.0F, 0.625F, 0.125F);
         }

         if (tileEntity.isFlipped()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         this.modelDakimakura.render(tileEntity.getDaki(), (double)tileEntity.xCoord, (double)tileEntity.yCoord, (double)tileEntity.zCoord);
         GL11.glPopMatrix();
         mc.mcProfiler.endSection();
      }
   }
}
