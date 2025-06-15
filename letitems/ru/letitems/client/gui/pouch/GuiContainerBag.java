package ru.letitems.client.gui.pouch;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.inventory.container.ContainerBag;

@SideOnly(Side.CLIENT)
public final class GuiContainerBag extends GuiContainer {
   private static final ResourceLocation RESOURCE = new ResourceLocation("textures/gui/container/generic_54.png");
   private final int rows;

   public GuiContainerBag(InventoryPlayer inventoryPlayer, ItemStack stack) {
      super(new ContainerBag(inventoryPlayer, stack));
      this.allowUserInput = false;
      this.rows = ((ContainerBag)this.inventorySlots).inventory.getSizeInventory() / 9;
      this.ySize = 114 + this.rows * 18;
   }

   protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.rows * 18 + 17);
      this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.rows * 18 + 17, 0, 126, this.xSize, 96);
   }

   protected boolean checkHotbarKeys(int key) {
      return false;
   }
}
