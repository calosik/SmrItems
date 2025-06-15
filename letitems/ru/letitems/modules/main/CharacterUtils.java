package ru.letitems.modules.main;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import ru.letitems.client.gui.inventory.GuiGlobal;

@SideOnly(Side.CLIENT)
public class CharacterUtils {
   private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

   public static void loadCharacterInfo(Minecraft mc) {
      EXECUTOR_SERVICE.submit(new CharacterUtils.CharacterLoadTask(mc.getSession().getUsername()));
   }

   private static final class CharacterLoadTask implements Runnable {
      private final String username;

      public CharacterLoadTask(String username) {
         this.username = username;
      }

      public void run() {
         try {
            URL url = new URL(String.format("http://letragon.ru/bin/api/lkdfksjvjioas.php?username=%s", this.username));
            String json = Resources.asCharSource(url, Charsets.UTF_8).read();
            CharacterUtils.CharacterLoadTask.JsonResponse response = (CharacterUtils.CharacterLoadTask.JsonResponse)(new Gson()).fromJson(json, CharacterUtils.CharacterLoadTask.JsonResponse.class);
            if (!Strings.isNullOrEmpty(response.backgroundId)) {
               GuiNewMainMenu.backgroundLocation = downloadImage("/uploads/bg-client/%s.%s", "jpg", "bg", response.backgroundId);
            }

            String avatar = "/assets/visual/default_photo.png";
            if (response.avatar != null) {
               avatar = response.avatar;
            }

            String[] arrayAvatar = avatar.replace(".thump", "").split("/");
            String[] arrayAvatarInfo = arrayAvatar[arrayAvatar.length - 1].split("\\.");
            GuiGlobal.renderUserAvatar = downloadImage(avatar, arrayAvatarInfo[1], "avatars", arrayAvatarInfo[0]);
            if (response.coverId != null) {
               GuiGlobal.renderUserCover = downloadImage("/assets/visual/covers/server/%s.%s", "png", "covers", response.coverId);
            }
         } catch (Throwable var7) {
            var7.printStackTrace();
         }

      }

      private static BufferedImage downloadImage(String url, String format, String cat, String id) {
         try {
            File cacheImageCategory = new File(MenuMod.instance.cacheDirectory, cat);
            cacheImageCategory.mkdir();
            File cacheFile = new File(cacheImageCategory, String.format("%s.%s", id, format));
            BufferedImage bm;
            if (cacheFile.exists()) {
               bm = ImageIO.read(cacheFile);
            } else {
               URL input = new URL(String.format("http://letragon.ru" + url, id, format));
               bm = ImageIO.read(input);
               ImageIO.write(bm, format, cacheFile);
            }

            return bm;
         } catch (Throwable var8) {
            var8.printStackTrace();
            return null;
         }
      }

      private static final class JsonResponse {
         public String backgroundId;
         public String avatar;
         public String coverId;
      }
   }
}
