package ru.letitems.common.dakimakura;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.pack.IDakiPack;

public class DakiImageData implements Callable<DakiImageData> {
   private final Daki daki;
   private BufferedImage bufferedImageFull;
   private byte[] textureFront;
   private byte[] textureBack;
   private static int dakiTexture = 1024;

   public DakiImageData(Daki daki) {
      this.daki = daki;
      this.load();
   }

   public void load() {
      IDakiPack dakiPack = LetItems.proxy.getDakimakuraManager().getDakiPack(this.daki.getDakiDirectoryName());
      this.textureFront = dakiPack.getResource("front.jpg");
      this.textureBack = dakiPack.getResource("back.jpg");
   }

   public Daki getDaki() {
      return this.daki;
   }

   public BufferedImage getBufferedImageFull() {
      return this.bufferedImageFull;
   }

   public DakiImageData call() {
      Object inputstream = null;

      BufferedImage bufferedimageBack;
      try {
         inputstream = this.textureFront != null ? new ByteArrayInputStream(this.textureFront) : this.getMissingTexture();
         BufferedImage bufferedimageFront = ImageIO.read((InputStream)inputstream);
         ((InputStream)inputstream).close();
         inputstream = this.textureBack != null ? new ByteArrayInputStream(this.textureBack) : this.getMissingTexture();
         bufferedimageBack = ImageIO.read((InputStream)inputstream);
         ((InputStream)inputstream).close();
         int heightF = bufferedimageFront.getHeight();
         int heightB = bufferedimageBack.getHeight();
         int maxTexture = Math.max(heightF, heightB);
         int textureSize = getMaxTextureSize();
         textureSize = Math.min(textureSize, getNextPowerOf2(maxTexture));
         bufferedimageFront = resize(bufferedimageFront, textureSize / 2, textureSize);
         bufferedimageBack = resize(bufferedimageBack, textureSize / 2, textureSize);
         AffineTransform tx = AffineTransform.getScaleInstance(-1.0D, 1.0D);
         tx.translate((double)(-bufferedimageBack.getWidth((ImageObserver)null)), 0.0D);
         AffineTransformOp op = new AffineTransformOp(tx, 1);
         bufferedimageBack = op.filter(bufferedimageBack, (BufferedImage)null);
         this.bufferedImageFull = new BufferedImage(textureSize, textureSize, 1);
         Graphics2D g2d = this.bufferedImageFull.createGraphics();
         g2d.drawImage(bufferedimageFront, 0, 0, (ImageObserver)null);
         g2d.drawImage(bufferedimageBack, textureSize / 2, 0, (ImageObserver)null);
         g2d.dispose();
         this.textureFront = null;
         this.textureBack = null;
         DakiImageData var11 = this;
         return var11;
      } catch (Exception var15) {
         var15.printStackTrace();
         bufferedimageBack = null;
      } finally {
         IOUtils.closeQuietly((InputStream)inputstream);
      }

      return bufferedimageBack;
   }

   private InputStream getMissingTexture() {
      return this.getClass().getClassLoader().getResourceAsStream("assets/letitems/textures/models/missing.png");
   }

   private static int getNextPowerOf2(int value) {
      return (int)Math.pow(2.0D, (double)(32 - Integer.numberOfLeadingZeros(value - 1)));
   }

   private static int getMaxTextureSize() {
      int maxGpuSize = ((ClientProxy)LetItems.proxy).getMaxGpuTextureSize();
      return Math.min(maxGpuSize, dakiTexture);
   }

   public static void syncSettings(int dt) {
      dakiTexture = 256 + dt * 256;
   }

   private static BufferedImage resize(BufferedImage img, int newW, int newH) {
      int hint = true;
      Image tmp = img.getScaledInstance(newW, newH, 4);
      BufferedImage dimg = new BufferedImage(newW, newH, 1);
      Graphics2D g2d = dimg.createGraphics();
      g2d.drawImage(tmp, 0, 0, (ImageObserver)null);
      g2d.dispose();
      return dimg;
   }
}
