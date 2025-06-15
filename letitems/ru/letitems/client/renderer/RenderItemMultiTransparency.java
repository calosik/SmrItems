package ru.letitems.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.items.ItemBuildersWand;

@SideOnly(Side.CLIENT)
public final class RenderItemMultiTransparency implements IItemRenderer {
   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      return true;
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
      return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
   }

   public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
      if (item.getItem() instanceof ItemBuildersWand) {
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         ItemBuildersWand itemTrans = (ItemBuildersWand)item.getItem();
         if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(16.0F, 16.0F, 1.0F);
         } else if (type == ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5F, -0.25F, 0.0F);
            GL11.glDisable(2884);
         }

         Tessellator tessellator = Tessellator.instance;

         for(int i = 0; i < 2; ++i) {
            IIcon icon = itemTrans.getIconForTransparentRender(item, i);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glEnable(3008);
            if (i == 1) {
               GL11.glBlendFunc(770, 771);
               GL11.glEnable(3042);
               GL11.glDisable(3008);
               GL11.glShadeModel(7425);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
               if (type != ItemRenderType.INVENTORY) {
                  GL11.glEnable(32826);
                  ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
                  GL11.glDisable(32826);
               } else {
                  tessellator.startDrawingQuads();
                  tessellator.addVertexWithUV(0.0D, 0.0D, 0.03125D, (double)icon.getMinU(), (double)icon.getMinV());
                  tessellator.addVertexWithUV(0.0D, 1.0D, 0.03125D, (double)icon.getMinU(), (double)icon.getMaxV());
                  tessellator.addVertexWithUV(1.0D, 1.0D, 0.03125D, (double)icon.getMaxU(), (double)icon.getMaxV());
                  tessellator.addVertexWithUV(1.0D, 0.0D, 0.03125D, (double)icon.getMaxU(), (double)icon.getMinV());
                  tessellator.draw();
               }

               GL11.glShadeModel(7424);
               GL11.glEnable(3008);
               GL11.glDisable(3042);
            } else if (type != ItemRenderType.INVENTORY) {
               GL11.glEnable(32826);
               ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
               GL11.glDisable(32826);
            } else {
               tessellator.startDrawingQuads();
               tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMinV());
               tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMaxV());
               tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMaxV());
               tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMinV());
               tessellator.draw();
            }
         }

         if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(0.0625F, 0.0625F, 1.0F);
         } else if (type == ItemRenderType.ENTITY) {
            GL11.glTranslatef(0.5F, 0.25F, 0.0F);
            GL11.glEnable(2884);
         }
      }

   }
}
