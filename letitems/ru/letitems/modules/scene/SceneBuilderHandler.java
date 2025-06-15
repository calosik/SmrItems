package ru.letitems.modules.scene;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SceneBuilderHandler {
   private final Minecraft mc = Minecraft.getMinecraft();
   private Framebuffer framebuffer;
   private final ExecutorService executorService = Executors.newSingleThreadExecutor();
   private IntBuffer pixelBuffer;
   private int[] pixelValues;
   private int width;
   private int height;

   public SceneBuilderHandler() {
      this.width = this.mc.displayWidth;
      this.height = this.mc.displayHeight;
   }

   public void buildScene() {
      EntityClientPlayerMP player = this.mc.thePlayer;
      if (player != null && player.onGround && !player.isInvisible() && !player.isDead && !player.isPlayerSleeping() && !player.isRiding() && !player.isBurning() && !player.isSprinting() && !player.isSneaking()) {
         this.width = this.mc.displayWidth;
         this.height = this.mc.displayHeight;
         if (this.width >= 400 && this.height >= 400) {
            this.framebuffer = new Framebuffer(this.width, this.height, true);
            this.framebuffer.framebufferClear();
            this.framebuffer.bindFramebuffer(true);
            ScaledResolution scaledResolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int scale = scaledResolution.getScaleFactor() > 1 ? scaledResolution.getScaleFactor() - 1 : 2;
            int width = scaledResolution.getScaledWidth() * scale;
            int height = scaledResolution.getScaledHeight() * scale;
            int size = this.mc.gameSettings.guiScale != 2 && scale != 1 ? 4 : 2;
            int i = 75 - (scale > 1 ? 13 * scale : 1);
            drawEntity((float)(width / size), (float)(height / size + i), (float)i, player);
            this.framebuffer.unbindFramebuffer();
         }
      }
   }

   public static void drawEntity(float x, float y, float scale, EntityLivingBase entity) {
      GL11.glPushMatrix();
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glTranslatef(x, y, 50.0F);
      GL11.glScalef(-scale, scale, scale);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      float f2 = entity.renderYawOffset;
      float f3 = entity.rotationYaw;
      float f4 = entity.rotationPitch;
      float f5 = entity.prevRotationYawHead;
      float f6 = entity.rotationYawHead;
      RenderHelper.enableStandardItemLighting();
      GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-((float)Math.atan(0.0D)) * 20.0F, 1.0F, 0.0F, 0.0F);
      entity.renderYawOffset = (float)Math.atan(0.75D) * 20.0F;
      entity.rotationYaw = (float)Math.atan(0.75D) * 40.0F;
      entity.rotationPitch = -((float)Math.atan(0.75D)) * 20.0F;
      entity.rotationYawHead = entity.rotationYaw;
      entity.prevRotationYawHead = entity.rotationYaw;
      GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
      RenderManager.instance.playerViewY = 180.0F;
      RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      entity.renderYawOffset = f2;
      entity.rotationYaw = f3;
      entity.rotationPitch = f4;
      entity.prevRotationYawHead = f5;
      entity.rotationYawHead = f6;
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GL11.glPopMatrix();
   }

   public Future<byte[]> getScreenshot() {
      if (this.mc.thePlayer == null) {
         return null;
      } else {
         int p_148259_2_ = this.width;
         int p_148259_3_ = this.height;
         if (this.framebuffer == null) {
            return null;
         } else {
            Framebuffer p_148259_4_ = this.framebuffer;

            try {
               if (OpenGlHelper.isFramebufferEnabled()) {
                  p_148259_2_ = p_148259_4_.framebufferTextureWidth;
                  p_148259_3_ = p_148259_4_.framebufferTextureHeight;
               }

               int k = p_148259_2_ * p_148259_3_;
               if (this.pixelBuffer == null || this.pixelBuffer.capacity() < k) {
                  this.pixelBuffer = BufferUtils.createIntBuffer(k);
                  this.pixelValues = new int[k];
               }

               GL11.glPixelStorei(3333, 1);
               GL11.glPixelStorei(3317, 1);
               this.pixelBuffer.clear();
               if (OpenGlHelper.isFramebufferEnabled()) {
                  GL11.glBindTexture(3553, p_148259_4_.framebufferTexture);
                  GL11.glGetTexImage(3553, 0, 32993, 33639, this.pixelBuffer);
               } else {
                  GL11.glReadPixels(0, 0, p_148259_2_, p_148259_3_, 32993, 33639, this.pixelBuffer);
               }

               return this.executorService.submit(() -> {
                  this.pixelBuffer.get(this.pixelValues);
                  TextureUtil.func_147953_a(this.pixelValues, this.width, this.height);
                  BufferedImage bufferedimage;
                  if (OpenGlHelper.isFramebufferEnabled()) {
                     bufferedimage = new BufferedImage(this.width, this.height, 2);
                     int l = p_148259_4_.framebufferTextureHeight - p_148259_4_.framebufferHeight;

                     int x;
                     for(int i1 = l; i1 < p_148259_4_.framebufferTextureHeight; ++i1) {
                        for(x = 0; x < p_148259_4_.framebufferWidth; ++x) {
                           bufferedimage.setRGB(x, i1 - l, this.pixelValues[i1 * p_148259_4_.framebufferTextureWidth + x]);
                        }
                     }

                     BufferedImage tempImage = new BufferedImage(400, 400, 2);
                     x = (bufferedimage.getWidth() - tempImage.getWidth()) / 2;
                     int y = (bufferedimage.getHeight() - tempImage.getHeight()) / 2;

                     for(int i = 0; i < bufferedimage.getWidth(); ++i) {
                        for(int j = 0; j < bufferedimage.getHeight(); ++j) {
                           if (i >= x && j >= y && i < x + tempImage.getWidth() && j < y + tempImage.getHeight()) {
                              tempImage.setRGB(i - x, j - y, bufferedimage.getRGB(i, j));
                           }
                        }
                     }

                     bufferedimage = tempImage;
                  } else {
                     bufferedimage = new BufferedImage(this.width, this.height, 2);
                     bufferedimage.setRGB(0, 0, this.width, this.height, this.pixelValues, 0, this.width);
                  }

                  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                  ImageIO.write(bufferedimage, "png", byteArrayOutputStream);
                  byte[] image = byteArrayOutputStream.toByteArray();
                  byteArrayOutputStream.close();
                  return image;
               });
            } catch (Exception var5) {
               return null;
            }
         }
      }
   }
}
