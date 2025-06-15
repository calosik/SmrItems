package ru.letitems.common.dakimakura.pack;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.Daki;

public class DakiPackZipFile extends AbstractDakiPack {
   @Nullable
   private final ZipFile zipFile;

   public DakiPackZipFile(@Nullable ZipFile zipFile, String packName) {
      super(packName);
      this.zipFile = zipFile;
   }

   public String getName() {
      return this.getResourceName();
   }

   public byte[] getResource(String path) {
      if (this.zipFile == null) {
         return null;
      } else {
         ZipEntry entry = this.getResourceEntry(path);
         if (entry == null) {
            return null;
         } else {
            InputStream inputStream = null;

            try {
               inputStream = this.zipFile.getInputStream(entry);
               byte[] var4 = IOUtils.toByteArray(inputStream);
               return var4;
            } catch (Exception var8) {
               var8.printStackTrace();
            } finally {
               IOUtils.closeQuietly(inputStream);
            }

            return null;
         }
      }
   }

   public void loadPack(String packName) {
      if (FMLCommonHandler.instance().getSide().isClient()) {
         if (this.zipFile == null) {
            LetItems.LOGGER.warn("Zip file not found");
         } else {
            ZipEntry entry = this.getResourceEntry("");
            if (entry == null) {
               LetItems.LOGGER.warn("Daki {} not found", new Object[]{packName});
            } else if (!entry.isDirectory()) {
               LetItems.LOGGER.warn("Daki entry {} is not directory", new Object[]{packName});
            }
         }
      }

      this.addDaki(new Daki(packName));
   }

   private ZipEntry getResourceEntry(String path) {
      return this.zipFile == null ? null : this.zipFile.getEntry(this.getName() + '/' + path);
   }
}
