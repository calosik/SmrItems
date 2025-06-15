package ru.letitems.client.gui;

import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.guihook.GuiContainerManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.inventory.container.ContainerVendingMachine;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketVending;
import ru.letitems.common.tile.TileVendingMachine;

@SideOnly(Side.CLIENT)
public class GuiVendingMachine extends GuiContainer {
   private final TileVendingMachine machine;
   private int count;
   private final boolean hisOwner;
   private final String owners;
   private GuiTextField nameField;

   public GuiVendingMachine(EntityPlayer player, TileVendingMachine machine) {
      super(new ContainerVendingMachine(player.inventory, machine));
      this.machine = machine;
      this.count = machine.getCountSale();
      this.hisOwner = machine.hisPermission(player);
      this.owners = machine.getOwners();
      this.xSize = 222;
      this.ySize = 228;
   }

   public void initGui() {
      super.initGui();
      if (this.hisOwner) {
         this.nameField = new GuiTextField(this.fontRendererObj, this.guiLeft + 23, this.guiTop + 132, 94, 15);
         this.nameField.setText(this.owners);
         this.nameField.setEnableBackgroundDrawing(false);
         this.nameField.setMaxStringLength(33);
         this.nameField.setTextColor(9408399);
         this.nameField.setDisabledTextColour(-1);
         Keyboard.enableRepeatEvents(true);
      }
   }

   public void onGuiClosed() {
      if (this.hisOwner) {
         Keyboard.enableRepeatEvents(false);
      }

   }

   public void updateScreen() {
      super.updateScreen();
      if (this.hisOwner) {
         this.nameField.updateCursorCounter();
      }

   }

   protected void keyTyped(char c, int i) {
      if (i == this.mc.gameSettings.keyBindInventory.getKeyCode() && this.nameField.isFocused()) {
         i = -1;
      }

      super.keyTyped(c, i);
      if (this.hisOwner && this.nameField.isFocused()) {
         this.nameField.textboxKeyTyped(c, i);
      }

      if (this.hisOwner && i == 37) {
         LayoutManager layoutManager = LayoutManager.instance();
         if (layoutManager != null && LayoutManager.itemPanel != null && !NEIClientConfig.isHidden()) {
            ItemStack stack = GuiContainerManager.getOverdirve(this);
            if (stack != null) {
               Item item = stack.getItem();
               if (item instanceof ItemBlock) {
                  if (this.machine.getRenderItem() != null && item == this.machine.getRenderItem().getItem() && stack.getItemDamage() == this.machine.getRenderItem().getItemDamage()) {
                     return;
                  }

                  NetworkManager.sendToServer(new PacketVending(3, stack, this.machine.xCoord, this.machine.yCoord, this.machine.zCoord));
               }
            }
         }
      }

   }

   protected void drawGuiContainerForegroundLayer(int a, int b) {
      this.fontRendererObj.drawString("Продажа", 24, 43, 8351671);
      this.fontRendererObj.drawString("Покупка", 76, 43, 8351671);
      this.fontRendererObj.drawString("Блок основы", 47, 68, 8351671);
      this.fontRendererObj.drawString("Продажи", 24, 92, 8351671);
      GL11.glPushMatrix();
      GL11.glScalef(0.75F, 0.75F, 0.75F);
      this.fontRendererObj.drawString(this.count > 0 ? "Всего " + this.count : "Данных нет", 32, 134, 6707605);
      GL11.glPopMatrix();
   }

   protected void drawGuiContainerBackgroundLayer(float f, int a, int b) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GeneralClientUtils.bind("letitems:textures/gui/vending-gui.png");
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      if (this.hisOwner) {
         if (this.count > 0) {
            if (a - this.guiLeft <= 103 || a - this.guiLeft >= 116 || b - this.guiTop <= 92 || b - this.guiTop >= 105) {
               GL11.glEnable(3042);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }

            this.drawTexturedModalRect(this.guiLeft + 104, this.guiTop + 93, 0, 228, 12, 12);
         }

         GL11.glPushMatrix();
         if (a - this.guiLeft > 192 && a - this.guiLeft < 215 && b - this.guiTop > 90 && b - this.guiTop < 120) {
            this.drawGradientRect(this.guiLeft + 199 + 15, this.guiTop + 102 + 15, this.guiLeft + 199, this.guiTop + 102, -1, -1);
         }

         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         this.drawTexturedModalRect(this.guiLeft + 199, this.guiTop + 102, 13, 228, 15, 15);
         GL11.glPopMatrix();
         if (this.nameField != null && this.nameField.getText() != null) {
            GL11.glPushMatrix();
            GL11.glScalef(0.85F, 0.85F, 0.85F);
            int p = (int)((float)(this.guiTop + 120) / 0.85F);
            int kk = (int)((float)(this.guiLeft + 19) / 0.85F);
            this.fontRendererObj.drawString("Укажите ники игроков", kk, p, 6707605);
            this.fontRendererObj.drawString("Сохранить", (int)((float)(this.guiLeft + 135) / 0.85F), p, a - this.guiLeft > 134 && a - this.guiLeft < 182 && b - this.guiTop > 118 && b - this.guiTop < 128 ? 6707605 : 5194869);
            int p1 = (int)((float)(this.guiTop + 130) / 0.85F);
            this.drawGradientRect(kk + 116, p1 + 15, kk, p1, 1082688157, 1082688157);
            if (this.nameField != null && this.nameField.getText().isEmpty() && !this.nameField.isFocused()) {
               this.fontRendererObj.drawString("Укажите ники...", kk + 5, p1 + 4, 9408399);
            }

            GL11.glPopMatrix();
            if (this.nameField != null) {
               this.nameField.drawTextBox();
            }
         }
      }

   }

   public void drawScreen(int x, int y, float p_73863_3_) {
      super.drawScreen(x, y, p_73863_3_);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      if (this.hisOwner) {
         if (this.count > 0 && xx > 103 && xx < 116 && yy > 92 && yy < 105) {
            this.drawHoveringText(Collections.singletonList("Сбросить статистику"), x, y, this.fontRendererObj);
         }

         if (xx > 17 && xx < 120 && yy > 115 && yy < 128) {
            this.drawHoveringText(Arrays.asList("§7Укажите ники игроков, которые смогут", "§7редактировать аппарат.", "§6Два игрока, через запятую."), x, y, this.fontRendererObj);
         } else if (xx > 19 && xx < 122 && yy > 60 && yy < 84) {
            this.drawHoveringText(Arrays.asList("§7Можно установить крышку блока, для", "§7этого наведитесь в NEI или инвентаре", "§7на блок и нажмите на клавишу §4K§7."), x, y, this.fontRendererObj);
         } else if (xx > 192 && xx < 215 && yy > 90 && yy < 120 && this.machine.getForgeDirection() != null) {
            this.drawHoveringText(Collections.singletonList("§7Сторона хранилища: §6" + StatCollector.translateToLocal("tooltip.tile.side." + this.machine.getForgeDirection().name().toLowerCase(Locale.US))), x, y, this.fontRendererObj);
         }
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      if (this.mc.thePlayer != null && this.hisOwner) {
         if (this.count > 0 && xx > 103 && xx < 116 && yy > 92 && yy < 105) {
            NetworkManager.sendToServer(new PacketVending(1, "", this.machine.xCoord, this.machine.yCoord, this.machine.zCoord));
            this.count = 0;
            return;
         }

         if (xx > 192 && xx < 215 && yy > 90 && yy < 120) {
            NetworkManager.sendToServer(new PacketVending(4, "", this.machine.xCoord, this.machine.yCoord, this.machine.zCoord));
         }

         if (!this.nameField.getText().equals(this.owners) && xx > 134 && xx < 182 && yy > 118 && yy < 128) {
            NetworkManager.sendToServer(new PacketVending(2, this.nameField.getText(), this.machine.xCoord, this.machine.yCoord, this.machine.zCoord));
         }

         this.nameField.mouseClicked(x, y, button);
      }

   }
}
