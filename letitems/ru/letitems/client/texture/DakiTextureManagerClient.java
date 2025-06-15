package ru.letitems.client.texture;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import ru.letitems.common.dakimakura.Daki;
import ru.letitems.common.dakimakura.DakiImageData;

@SideOnly(Side.CLIENT)
public class DakiTextureManagerClient {
   private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("Daki texture loader %d").setPriority(1).build());
   private final Map<Daki, DakiTexture> textureCache = new HashMap();

   public DakiTexture getTextureForDaki(final Daki daki) {
      DakiTexture dakiTexture = (DakiTexture)this.textureCache.get(daki);
      if (dakiTexture == null) {
         dakiTexture = new DakiTexture();
         this.textureCache.put(daki, dakiTexture);

         try {
            DakiImageData imageData = new DakiImageData(daki);
            ListenableFutureTask<DakiImageData> task = ListenableFutureTask.create(imageData);
            Futures.addCallback(task, new FutureCallback<DakiImageData>() {
               public void onSuccess(@Nullable DakiImageData result) {
                  if (result != null) {
                     BufferedImage bufferedImage = result.getBufferedImageFull();
                     if (bufferedImage != null) {
                        DakiTexture texture = (DakiTexture)DakiTextureManagerClient.this.textureCache.get(daki);
                        if (texture != null) {
                           texture.setBufferedImageFull(bufferedImage);
                        }
                     }

                  }
               }

               public void onFailure(Throwable t) {
                  t.printStackTrace();
               }
            });
            EXECUTOR_SERVICE.submit(task);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return dakiTexture;
   }

   public void clear() {
      this.textureCache.clear();
   }
}
