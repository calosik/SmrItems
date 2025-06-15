package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.inventory.container.ContainerPrimeInv;

@SideOnly(Side.CLIENT)
public final class GuiPrimeInventory extends GuiPreRenderContainer {
   public GuiPrimeInventory(EntityPlayer player) {
      super(new ContainerPrimeInv(player));
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GeneralClientUtils.bind("letitems:textures/gui/bg-prime-inventory.png");
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
   }
}
