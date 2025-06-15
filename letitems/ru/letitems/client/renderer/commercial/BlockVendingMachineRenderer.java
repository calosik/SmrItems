package ru.letitems.client.renderer.commercial;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import ru.letitems.common.tile.TileVendingMachine;

@SideOnly(Side.CLIENT)
public class BlockVendingMachineRenderer implements ISimpleBlockRenderingHandler {
   private void drawBlock(Block block, int meta, RenderBlocks renderer) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
      tessellator.draw();
   }

   public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
      this.drawBlock(block, meta, renderer);
      renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
      this.drawBlock(Blocks.stone, 0, renderer);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      renderer.renderStandardBlock(block, x, y, z);
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileVendingMachine) {
         ItemStack item = ((TileVendingMachine)tile).getRenderItem();
         Block blockRender = item != null ? Block.getBlockFromItem(item.getItem()) : Blocks.stone;
         Block glassPane = Blocks.glass_pane;
         ForgeDirection direction = ((TileVendingMachine)tile).getForgeDirection();
         if (direction != null) {
            switch(direction) {
            case DOWN:
               renderer.renderFaceYNeg(glassPane, (double)x, (double)y, (double)z, glassPane.getIcon(0, 0));
               break;
            case EAST:
               renderer.renderFaceXPos(glassPane, (double)x, (double)y, (double)z, glassPane.getIcon(0, 0));
               break;
            case NORTH:
               renderer.renderFaceZNeg(glassPane, (double)x, (double)y, (double)z, glassPane.getIcon(0, 0));
               break;
            case SOUTH:
               renderer.renderFaceZPos(glassPane, (double)x, (double)y, (double)z, glassPane.getIcon(0, 0));
               break;
            case UP:
               renderer.renderFaceYPos(glassPane, (double)x, (double)y, (double)z, glassPane.getIcon(0, 0));
               break;
            case WEST:
               renderer.renderFaceXNeg(glassPane, (double)x, (double)y, (double)z, glassPane.getIcon(0, 0));
            case UNKNOWN:
            }
         }

         renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
         renderer.renderStandardBlock(blockRender, x, y, z);
      }

      return false;
   }

   public boolean shouldRender3DInInventory(int var1) {
      return true;
   }

   public int getRenderId() {
      return RenderConstants.vendingRendererId;
   }
}
