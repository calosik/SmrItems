package ru.letitems.modules.blur;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public final class ShaderResourcePack implements IResourcePack, IResourceManagerReloadListener {
   private final Map<ResourceLocation, String> loadedData = new HashMap();

   private boolean validPath(ResourceLocation location) {
      return location.getResourceDomain().equals("minecraft") && location.getResourcePath().startsWith("shaders/");
   }

   public InputStream getInputStream(ResourceLocation location) throws IOException {
      if (!this.validPath(location)) {
         throw new FileNotFoundException(location.toString());
      } else {
         String s = (String)this.loadedData.get(location);
         if (s == null) {
            InputStream in = Blur.class.getResourceAsStream('/' + location.getResourcePath());
            Throwable var5 = null;

            StringBuilder data;
            try {
               data = new StringBuilder();
               Scanner scan = new Scanner(in);
               Throwable var7 = null;

               try {
                  while(scan.hasNextLine()) {
                     data.append(scan.nextLine().replaceAll("@radius@", Integer.toString(Blur.instance.radius))).append('\n');
                  }
               } catch (Throwable var30) {
                  var7 = var30;
                  throw var30;
               } finally {
                  if (scan != null) {
                     if (var7 != null) {
                        try {
                           scan.close();
                        } catch (Throwable var29) {
                           var7.addSuppressed(var29);
                        }
                     } else {
                        scan.close();
                     }
                  }

               }
            } catch (Throwable var32) {
               var5 = var32;
               throw var32;
            } finally {
               if (in != null) {
                  if (var5 != null) {
                     try {
                        in.close();
                     } catch (Throwable var28) {
                        var5.addSuppressed(var28);
                     }
                  } else {
                     in.close();
                  }
               }

            }

            s = data.toString();
            this.loadedData.put(location, s);
         }

         return new ByteArrayInputStream(s.getBytes());
      }
   }

   public boolean resourceExists(ResourceLocation location) {
      return this.validPath(location) && Blur.class.getResource('/' + location.getResourcePath()) != null;
   }

   public Set<String> getResourceDomains() {
      return ImmutableSet.of("minecraft");
   }

   public IMetadataSection getPackMetadata(IMetadataSerializer serializer, String section) {
      return section.equals("pack") ? new PackMetadataSection(new ChatComponentText("Blur's default shaders"), 3) : null;
   }

   public BufferedImage getPackImage() throws IOException {
      throw new FileNotFoundException("pack.png");
   }

   public String getPackName() {
      return "Blur dummy resource pack";
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.loadedData.clear();
   }
}
