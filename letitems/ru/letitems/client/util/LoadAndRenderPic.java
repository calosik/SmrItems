package ru.letitems.client.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class LoadAndRenderPic {
   public static final LoadAndRenderPic instance = new LoadAndRenderPic();
   private final File cacheImageCategory;
   public final Map<String, LoadAndRenderPic.PictureData> bufferImages;
   public final TObjectIntMap<String> textureIds;
   private final ExecutorService EXECUTOR_SERVICE;

   public LoadAndRenderPic() {
      this.cacheImageCategory = new File("..", "Assets" + File.separator + "pack" + File.separator + "arch" + File.separator + "img");
      this.bufferImages = new HashMap();
      this.textureIds = new TObjectIntHashMap();
      this.EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
   }

   public void queueResearchInformation(String image, String id) {
      if (id != null && !this.bufferImages.containsKey(id)) {
         try {
            this.EXECUTOR_SERVICE.submit(new LoadAndRenderPic.LoadAwardTask(image, id)).get();
         } catch (ExecutionException | InterruptedException var4) {
            var4.printStackTrace();
         }

      }
   }

   public void renderPic(String t, float scale, int x, int xx, float opacityDraw) {
      this.renderPic((ColorConvertOp)null, "", t, 28, 28, scale, x, xx, opacityDraw);
   }

   public void renderPic(ColorConvertOp op, String type, String t, float scale, int x, int xx, float opacityDraw) {
      this.renderPic(op, type, t, 28, 28, scale, x, xx, opacityDraw);
   }

   public void renderPic(ColorConvertOp op, String type, String t, int sizeX, int sizeY, float scale, int x, int xx, float opacityDraw) {
      if (opacityDraw != 0.0F) {
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, opacityDraw);
         GL11.glTranslatef(1.0F + (float)x + 5.0F, 1.0F + (float)xx + 5.0F, 1.0F);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GL11.glBlendFunc(770, 771);
         Tessellator tessellator = Tessellator.instance;
         this.glBindTexture(t, type, op);
         GL11.glTranslatef(0.5F, 0.58461535F, 0.0F);
         GL11.glDisable(2896);
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV((double)(0.0F * scale), (double)((float)sizeY * scale), 5.0D, 0.0D, 1.0D);
         tessellator.addVertexWithUV((double)((float)sizeX * scale), (double)((float)sizeY * scale), 5.0D, 1.0D, 1.0D);
         tessellator.addVertexWithUV((double)((float)sizeX * scale), (double)(0.0F * scale), 5.0D, 1.0D, 0.0D);
         tessellator.addVertexWithUV((double)(0.0F * scale), (double)(0.0F * scale), 5.0D, 0.0D, 0.0D);
         tessellator.draw();
         GL11.glDisable(2896);
         GL11.glPopMatrix();
      }
   }

   private void glBindTexture(String id, String type, ColorConvertOp op) {
      if (this.textureIds.containsKey(id)) {
         int textureId = this.textureIds.get(id);
         LoadAndRenderPic.PictureData data = (LoadAndRenderPic.PictureData)this.bufferImages.get(id);
         if (data != null && data.needUpload) {
            BufferedImage image = data.image;
            if (op != null && type.equals("gui")) {
               image = op.filter(image, (BufferedImage)null);
            }

            TextureUtil.uploadTextureImageAllocate(textureId, image, false, false);
            data.needUpload = false;
         }

         GL11.glBindTexture(3553, textureId);
      }

   }

   @SideOnly(Side.CLIENT)
   public static final class PictureData {
      public BufferedImage image;
      public boolean needUpload;

      public PictureData(BufferedImage image, boolean needUpload) {
         this.image = image;
         this.needUpload = needUpload;
      }
   }

   @SideOnly(Side.CLIENT)
   public static final class LoadAwardTask implements Runnable {
      private final String image;
      private final String id;

      public LoadAwardTask(String image, String id) {
         this.image = image;
         this.id = id;
         if (!LoadAndRenderPic.instance.textureIds.containsKey(id)) {
            LoadAndRenderPic.instance.textureIds.put(id, TextureUtil.glGenTextures());
         }

      }

      public void run() {
         try {
            File cacheFile = new File(LoadAndRenderPic.instance.cacheImageCategory, this.image);
            BufferedImage bm = null;
            if (cacheFile.exists()) {
               bm = ImageIO.read(cacheFile);
            } else {
               try {
                  bm = ImageIO.read(new URL("http://letragon.ru/assets/visual/awards/" + this.image));
                  ImageIO.write(bm, this.image.substring(this.image.length() - 3), cacheFile);
               } catch (Throwable var4) {
               }
            }

            if (bm != null && LoadAndRenderPic.instance.bufferImages.get(this.id) == null) {
               LoadAndRenderPic.instance.bufferImages.put(this.id, new LoadAndRenderPic.PictureData(bm, true));
            }
         } catch (Throwable var5) {
         }

      }
   }
}
