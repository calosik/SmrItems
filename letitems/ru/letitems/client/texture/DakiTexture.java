package ru.letitems.client.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

@SideOnly(Side.CLIENT)
public class DakiTexture extends AbstractTexture {
   private static long lastLoad;
   private BufferedImage bufferedImageFull;

   public boolean isLoaded() {
      if (this.glTextureId == -1) {
         if (lastLoad + 125L < System.currentTimeMillis()) {
            if (this.load()) {
               lastLoad = System.currentTimeMillis();
            } else if (this.bufferedImageFull != null) {
               this.load();
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void setBufferedImageFull(BufferedImage bufferedImageFull) {
      this.bufferedImageFull = bufferedImageFull;
   }

   protected void finalize() throws Throwable {
      this.deleteGlTexture();
      super.finalize();
   }

   private boolean load() {
      if (this.bufferedImageFull == null) {
         return false;
      } else {
         this.deleteGlTexture();

         try {
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), this.bufferedImageFull, false, false);
            return true;
         } catch (Exception var2) {
            var2.printStackTrace();
            return false;
         }
      }
   }

   public void loadTexture(IResourceManager resourceManager) {
   }
}
