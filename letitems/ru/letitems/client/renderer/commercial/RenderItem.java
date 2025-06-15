package ru.letitems.client.renderer.commercial;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.block.BlockDakimakura;
import ru.letitems.common.block.BlockDoll;

@SideOnly(Side.CLIENT)
public class RenderItem extends Render {
   private final RenderBlocks renderBlocksRi = new RenderBlocks();
   private static RenderItem instance;

   public RenderItem() {
      this.shadowSize = 0.0F;
      this.shadowOpaque = 0.0F;
   }

   public void doRender(EntityItem item) {
      ItemStack itemstack = item.getEntityItem();
      Item itemstackItem = itemstack.getItem();
      if (itemstackItem != null) {
         this.bindEntityTexture(item);
         TextureUtil.func_152777_a(false, false, 1.0F);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 0.0F);
         GL11.glEnable(32826);
         if (!this.renderEntityItem(item, itemstack)) {
            float f6;
            int i;
            if (itemstack.getItemSpriteNumber() == 0 && itemstackItem instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstackItem).getRenderType())) {
               Block block = Block.getBlockFromItem(itemstackItem);
               f6 = 0.25F;
               i = block.getRenderType();
               if (i == 1 || i == 19 || i == 12 || i == 2) {
                  f6 = 0.5F;
               }

               if (block.getRenderBlockPass() > 0) {
                  GL11.glAlphaFunc(516, 0.1F);
                  GL11.glEnable(3042);
                  OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               }

               GL11.glTranslatef(0.0F, -0.1F, 0.0F);
               GL11.glScalef(f6, f6, f6);
               GL11.glPushMatrix();
               this.renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
               GL11.glPopMatrix();
               if (block.getRenderBlockPass() > 0) {
                  GL11.glDisable(3042);
               }
            } else {
               float f5;
               if (itemstackItem.requiresMultipleRenderPasses()) {
                  GL11.glScalef(0.5F, 0.5F, 0.5F);

                  for(int j = 0; j < itemstackItem.getRenderPasses(itemstack.getItemDamage()); ++j) {
                     int k = itemstackItem.getColorFromItemStack(itemstack, j);
                     f5 = (float)(k >> 16 & 255) / 255.0F;
                     f6 = (float)(k >> 8 & 255) / 255.0F;
                     float f7 = (float)(k & 255) / 255.0F;
                     GL11.glColor4f(f5, f6, f7, 1.0F);
                     this.renderDroppedItem(item, itemstackItem.getIcon(itemstack, j), f5, f6, f7);
                  }
               } else {
                  GL11.glScalef(0.45F, 0.45F, 0.45F);
                  i = itemstackItem.getColorFromItemStack(itemstack, 0);
                  float f4 = (float)(i >> 16 & 255) / 255.0F;
                  f5 = (float)(i >> 8 & 255) / 255.0F;
                  f6 = (float)(i & 255) / 255.0F;
                  this.renderDroppedItem(item, itemstack.getIconIndex(), f4, f5, f6);
               }
            }
         }

         GL11.glDisable(32826);
         GL11.glPopMatrix();
         this.bindEntityTexture(item);
         TextureUtil.func_147945_b();
      }

   }

   public boolean renderEntityItem(EntityItem entity, ItemStack item) {
      IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, ItemRenderType.ENTITY);
      if (customRenderer == null) {
         return false;
      } else {
         GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         this.renderManager.renderEngine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
         Block block = item.getItem() instanceof ItemBlock ? Block.getBlockFromItem(item.getItem()) : null;
         if (customRenderer.shouldUseRenderHelper(ItemRenderType.ENTITY, item, ItemRendererHelper.BLOCK_3D) || block != null && RenderBlocks.renderItemIn3d(block.getRenderType())) {
            int renderType = block != null ? block.getRenderType() : 1;
            float scale = renderType != 1 && renderType != 19 && renderType != 12 && renderType != 2 ? 0.25F : 0.3F;
            boolean blend = block != null && block.getRenderBlockPass() > 0;
            if (block instanceof BlockDoll) {
               GL11.glTranslatef(0.25F, 0.2F, 0.25F);
               scale = 0.5F;
            } else if (block instanceof BlockDakimakura) {
               GL11.glTranslatef(0.0F, -0.05F, 0.0F);
               scale = 0.08F;
            }

            if (blend) {
               GL11.glAlphaFunc(516, 0.1F);
               GL11.glEnable(3042);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }

            GL11.glScalef(scale, scale, scale);
            GL11.glPushMatrix();
            customRenderer.renderItem(ItemRenderType.ENTITY, item, new Object[]{this.field_147909_c, entity});
            GL11.glPopMatrix();
            if (blend) {
               GL11.glDisable(3042);
            }
         } else {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            customRenderer.renderItem(ItemRenderType.ENTITY, item, new Object[]{this.field_147909_c, entity});
         }

         return true;
      }
   }

   private ResourceLocation getEntityTexture(EntityItem p_110775_1_) {
      return this.renderManager.renderEngine.getResourceLocation(p_110775_1_.getEntityItem().getItemSpriteNumber());
   }

   private void renderDroppedItem(EntityItem entityItem, IIcon icon, float p_77020_5_, float p_77020_6_, float p_77020_7_) {
      Tessellator tessellator = Tessellator.instance;
      if (icon == null) {
         TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
         ResourceLocation resourcelocation = texturemanager.getResourceLocation(entityItem.getEntityItem().getItemSpriteNumber());
         icon = ((TextureMap)texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
      }

      float f14 = ((IIcon)icon).getMinU();
      float f15 = ((IIcon)icon).getMaxU();
      float f4 = ((IIcon)icon).getMinV();
      float f5 = ((IIcon)icon).getMaxV();
      if (this.renderManager.options.fancyGraphics) {
         GL11.glPushMatrix();
         GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.25F, -0.0421875F);
         GL11.glTranslatef(0.0F, 0.0F, 0.084375F);
         this.bindTexture(entityItem.getEntityItem().getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
         GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
         ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, ((IIcon)icon).getIconWidth(), ((IIcon)icon).getIconHeight(), 0.0625F);
         GL11.glPopMatrix();
      } else {
         GL11.glPushMatrix();
         GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
         tessellator.startDrawingQuads();
         tessellator.setNormal(0.0F, 1.0F, 0.0F);
         tessellator.addVertexWithUV(-0.5D, -0.25D, 0.0D, (double)f14, (double)f5);
         tessellator.addVertexWithUV(0.5D, -0.25D, 0.0D, (double)f15, (double)f5);
         tessellator.addVertexWithUV(0.5D, 0.75D, 0.0D, (double)f15, (double)f4);
         tessellator.addVertexWithUV(-0.5D, 0.75D, 0.0D, (double)f14, (double)f4);
         tessellator.draw();
         GL11.glPopMatrix();
      }

   }

   protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
      return this.getEntityTexture((EntityItem)p_110775_1_);
   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityItem)p_76986_1_);
   }

   public static RenderItem getInstance() {
      if (instance == null) {
         instance = new RenderItem();
      }

      return instance;
   }
}
