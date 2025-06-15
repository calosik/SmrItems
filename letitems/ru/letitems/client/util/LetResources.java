package ru.letitems.client.util;

import com.google.common.base.Charsets;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.apache.commons.io.FilenameUtils;

@SideOnly(Side.CLIENT)
public class LetResources extends DummyModContainer {
   public static final LetResources INSTANCE = new LetResources();
   private final String resourceDir = FilenameUtils.normalizeNoEndSeparator((new File(new File(".."), "Assets")).getAbsolutePath());
   private static ZipFile resourcePackZipFile;

   private LetResources() {
      super(new ModMetadata());
      ModMetadata meta = this.getMetadata();
      meta.modId = "letitems";
   }

   public void init() {
      FMLClientHandler.instance().addModAsResource(this);
      ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).reloadResourcePack(FMLClientHandler.instance().getResourcePackFor("letitems"));
   }

   public File getSource() {
      return new File(this.resourceDir, "letitems.zip");
   }

   public Class<?> getCustomResourcePackClass() {
      return LetResources.DynamicFileResourcePack.class;
   }

   public static class DynamicFileResourcePack extends FMLFileResourcePack {
      public DynamicFileResourcePack(ModContainer container) {
         super(container);
      }

      ZipFile getResourcePackZipFile() {
         if (LetResources.resourcePackZipFile == null) {
            try {
               LetResources.resourcePackZipFile = new ZipFile(LetResources.INSTANCE.getSource());
            } catch (Exception var2) {
            }
         }

         return LetResources.resourcePackZipFile;
      }

      protected InputStream getInputStreamByName(String resourceName) throws IOException {
         try {
            if ("pack.mcmeta".equals(resourceName)) {
               return new ByteArrayInputStream(("{\n \"pack\": {\n   \"description\": \"dummy FML pack for " + this.getPackName() + "\",\n   \"pack_format\": 1\n}\n}").getBytes(Charsets.UTF_8));
            } else {
               ZipFile zipfile = this.getResourcePackZipFile();
               ZipEntry zipentry = zipfile.getEntry(resourceName);
               return zipfile.getInputStream(zipentry);
            }
         } catch (IOException var4) {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, resourceName);
         }
      }

      public boolean hasResourceName(String resourceName) {
         return this.getResourcePackZipFile().getEntry(resourceName) != null;
      }
   }
}
