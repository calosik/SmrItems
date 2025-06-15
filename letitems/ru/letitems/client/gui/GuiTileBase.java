package ru.letitems.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.inventory.container.ContainerTileBase;
import ru.letitems.common.tile.TileBase;

@SideOnly(Side.CLIENT)
public abstract class GuiTileBase<T extends TileBase & IInventory, C extends ContainerTileBase<T>> extends GuiContainer implements INEIShow {
   public final C container;
   public final T tile;
   private final ResourceLocation texture;

   public GuiTileBase(C container, ResourceLocation texture) {
      super(container);
      this.container = container;
      this.tile = container.tile;
      this.texture = texture;
   }

   protected void drawGuiContainerBackgroundLayer(float renderTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.bindTexture(this.texture);
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
   }

   public void bindTexture(ResourceLocation texture) {
      this.mc.getTextureManager().bindTexture(texture);
   }
}
