package ru.SmrItems.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.SmrItems.common.handlers.PacketHandler;
import ru.SmrItems.common.inventory.containers.ContainerWorldAnchor;
import ru.SmrItems.common.packets.PacketClickButton;
import ru.SmrItems.common.tileentity.TileWorldAnchor;
import ru.SmrItems.util.Utils;
import ru.SmrItems.util.enums.FieldType;

public class GuiWorldAnchor extends GuiContainer {
   public static final ResourceLocation RESOURCE_LOCATION_GUI = new ResourceLocation("smritems:textures/container/gui/guiChunkLoader.png");
   private final EntityPlayer player;
   private final TileWorldAnchor te;
   private GuiButton buttonPause;

   public GuiWorldAnchor(TileWorldAnchor te, EntityPlayer player) {
      super(new ContainerWorldAnchor(te, player));
      this.player = player;
      this.te = te;
   }

   public void initGui() {
      this.buttonList.clear();
      this.guiLeft = (this.width - this.xSize) / 2;
      this.guiTop = (this.height - this.ySize) / 2;
      this.buttonList.add(this.buttonPause = new GuiButton(2, this.width / 2 + 43, (this.height - this.ySize) / 2 + 45, 40, 20, Utils.getPaused((Boolean)this.te.getField(FieldType.ISPAUSED))));
      super.initGui();
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      this.mc.renderEngine.bindTexture(RESOURCE_LOCATION_GUI);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.xSize = 238;
      this.ySize = 164;
      int x = (this.width - this.xSize) / 2;
      int y = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawString(I18n.format(this.te.getInventoryName(), new Object[0]), 34, 15, 4737096);
      this.fontRendererObj.drawString(I18n.format("gui.workTime.text", new Object[0]), 65, 40, 10895809);
      GL11.glPushMatrix();
      GL11.glTranslated(65.0D, 51.0D, 0.0D);
      GL11.glScalef(0.75F, 0.75F, 0.75F);
      this.fontRendererObj.drawString(Utils.getChunkLoadingTime((Integer)this.te.getField(FieldType.CHUNKLOADINGTIME)), 0, 0, 6842472);
      GL11.glPopMatrix();
   }

   public void updateScreen() {
      super.updateScreen();
      this.guiLeft = (this.width - this.xSize) / 2;
      this.guiTop = (this.height - this.ySize) / 2;
      this.buttonPause.displayString = Utils.getPaused((Boolean)this.te.getField(FieldType.ISPAUSED));
      super.updateScreen();
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 1:
         PacketHandler.INSTANCE.sendToServer(new PacketClickButton(1, this.te.xCoord, this.te.yCoord, this.te.zCoord, this.player.dimension));
         break;
      case 2:
         PacketHandler.INSTANCE.sendToServer(new PacketClickButton(2, this.te.xCoord, this.te.yCoord, this.te.zCoord, this.player.dimension));
      }

   }
}
