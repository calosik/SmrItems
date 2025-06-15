package ru.letitems.client.renderer.doll;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.block.BlockDoll;

@SideOnly(Side.CLIENT)
public final class ItemRenderDoll implements IItemRenderer {
   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      return true;
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
      return true;
   }

   public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(BlockDoll.DollType.getTypeFromStack(item).getTextureLocation());
      if (type == ItemRenderType.ENTITY) {
         GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
      } else if ((type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) && data[1] instanceof EntityPlayer) {
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         GL11.glRotatef(220.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glScalef(1.0F, -1.0F, -1.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      if (type == ItemRenderType.INVENTORY) {
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
      }

      TileRenderDoll.MODEL_DOLL.renderModel(0.0625F);
      TileRenderDoll.renderAsc();
      if (type == ItemRenderType.INVENTORY) {
         GL11.glDisable(3042);
      }

      GL11.glPopMatrix();
   }
}
