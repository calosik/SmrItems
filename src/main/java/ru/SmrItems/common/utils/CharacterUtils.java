package ru.SmrItems.common.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class CharacterUtils {
   private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
   private static TextureInfo backCharacter;
   private static BufferedImage tempBackCharacterImage;
   private static Object tempBackground;
   private static boolean texturesLoaded;

   public static void loadCharacterInfo() {
      if (backCharacter == null) {
         EXECUTOR_SERVICE.submit(new CharacterLoadTask(Minecraft.getMinecraft().getSession().getUsername()));
      }

   }

   public static TextureInfo getBackCharacterInfo() {
      if (texturesLoaded) {
         try {
            if (tempBackground instanceof ResourceLocation) {
               backCharacter = new TextureInfo(tempBackCharacterImage, "characterBack", (ResourceLocation)tempBackground);
            } else if (tempBackground instanceof BufferedImage || tempBackCharacterImage != null) {
               backCharacter = new TextureInfo(tempBackCharacterImage, "characterBack", (BufferedImage)tempBackground);
            }
         } catch (Throwable var1) {
            var1.printStackTrace();
         }

         tempBackCharacterImage = null;
         tempBackground = null;
         texturesLoaded = false;
      }

      return backCharacter;
   }

   private static final class CharacterLoadTask implements Runnable {
      private final String username;

      public CharacterLoadTask(String username) {
         this.username = username;
      }

      public void run() {
         try {
            URL url = new URL(String.format("", this.username));
            String json = Resources.asCharSource(url, Charsets.UTF_8).read();
            JsonResponse response = (JsonResponse)(new Gson()).fromJson(json, JsonResponse.class);
            if (!Strings.isNullOrEmpty(response.backgroundUrl)) {
               CharacterUtils.tempBackground = downloadImage(response.backgroundUrl);
            } else if (!Strings.isNullOrEmpty(response.characterUrl)) {
               CharacterUtils.tempBackCharacterImage = downloadImage(response.characterUrl);
            }

            CharacterUtils.texturesLoaded = true;
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

      }

      private static BufferedImage downloadImage(String url) {
         try {
            URL input = new URL(url);
            return ImageIO.read(input);
         } catch (Throwable var2) {
            return null;
         }
      }

      private static final class JsonResponse {
         @SerializedName("char")
         public String characterUrl;
         public String backgroundUrl;
      }
   }
}
