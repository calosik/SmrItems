package ru.letitems.client.gui.pouch;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.inventory.container.ContainerBigBag;

@SideOnly(Side.CLIENT)
public final class GuiContainerBigBag extends GuiContainer {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/bigBag.png");

   public GuiContainerBigBag(InventoryPlayer inventoryPlayer, ItemStack stack) {
      super(new ContainerBigBag(inventoryPlayer, stack));
      this.allowUserInput = false;
      this.xSize = 238;
      this.ySize = 256;
   }

   protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
   }

   protected boolean checkHotbarKeys(int key) {
      return false;
   }
}
