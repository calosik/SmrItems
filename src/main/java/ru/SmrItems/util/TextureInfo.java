package ru.SmrItems.util;

import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public final class TextureInfo {
   public final ResourceLocation resourceLocation;
   public final int width;
   public final int height;
   public final ResourceLocation backgroundLocation;

   public TextureInfo(BufferedImage image, String name, BufferedImage backgroundImage) {
      this(image, name, backgroundImage == null ? null : Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(name + "Background", new DynamicTexture(backgroundImage)));
   }

   public TextureInfo(BufferedImage image, String name, ResourceLocation backgroundLocation) {
      this(image == null ? null : Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(name, new DynamicTexture(image)), image == null ? 0 : image.getWidth(), image == null ? 0 : image.getHeight(), backgroundLocation);
   }

   public TextureInfo(ResourceLocation resourceLocation, int width, int height, ResourceLocation backgroundLocation) {
      this.resourceLocation = resourceLocation;
      this.width = width;
      this.height = height;
      this.backgroundLocation = backgroundLocation;
   }
}
