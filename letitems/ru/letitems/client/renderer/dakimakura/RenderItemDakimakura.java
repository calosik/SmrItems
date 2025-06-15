package ru.letitems.client.renderer.dakimakura;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.model.ModelDakimakura;
import ru.letitems.common.dakimakura.Daki;
import ru.letitems.common.items.blocks.ItemBlockDakimakura;

@SideOnly(Side.CLIENT)
public class RenderItemDakimakura implements IItemRenderer {
   private final ModelDakimakura modelDakimakura;

   public RenderItemDakimakura(ModelDakimakura modelDakimakura) {
      this.modelDakimakura = modelDakimakura;
   }

   public boolean handleRenderType(ItemStack itemStack, ItemRenderType renderType) {
      return true;
   }

   public boolean shouldUseRenderHelper(ItemRenderType renderType, ItemStack itemStack, ItemRendererHelper rendererHelper) {
      return true;
   }

   public void renderItem(ItemRenderType renderType, ItemStack itemStack, Object... data) {
      Daki daki = ItemBlockDakimakura.getDaki(itemStack);
      int mod = 0;
      GL11.glPushMatrix();
      switch(renderType) {
      case INVENTORY:
         GL11.glTranslatef(0.0F, -0.21875F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(1.1F, 1.1F, 1.1F);
         if (ItemBlockDakimakura.isFlipped(itemStack)) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         mod = 2;
         break;
      case EQUIPPED_FIRST_PERSON:
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(0.0F, 0.75F, 0.0F);
         GL11.glTranslatef(-0.5F, 0.25F, -0.1875F);
         if (ItemBlockDakimakura.isFlipped(itemStack)) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }
         break;
      case EQUIPPED:
         GL11.glScalef(2.8F, 2.8F, 2.8F);
         GL11.glTranslatef(0.1875F, 0.0F, 0.25F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         if (ItemBlockDakimakura.isFlipped(itemStack)) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }
         break;
      case ENTITY:
         GL11.glTranslatef(0.0F, 2.0F, 0.0F);
         GL11.glScalef(4.0F, 4.0F, 4.0F);
      }

      this.modelDakimakura.render(daki, mod);
      GL11.glPopMatrix();
   }
}
