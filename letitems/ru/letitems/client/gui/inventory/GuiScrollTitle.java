package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.inventory.ExtendedPlayer;
import ru.letitems.common.inventory.container.ContainerLetHelper;
import ru.letitems.common.inventory.slot.Slots;
import ru.letitems.common.items.ItemScrollTitle;
import ru.letitems.common.registry.RegItems;

@SideOnly(Side.CLIENT)
public class GuiScrollTitle extends GuiPreRenderContainer {
   private int sendId = -1;
   private final Slot slot;

   public GuiScrollTitle(EntityPlayer player) {
      super(new GuiScrollTitle.ContainerScrollTitle(player));
      this.allowUserInput = false;
      this.slot = (Slot)this.inventorySlots.inventorySlots.get(0);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GeneralClientUtils.bind("letitems:textures/gui/bg-inv-scroll-title.png");
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      this.sendId = -1;
      int xx = mouseX - this.guiLeft;
      int yy = mouseY - this.guiTop;
      if (!this.slot.getHasStack()) {
         this.drawStringWhenSize("Нет данных", 1.75F, 42, 45, 11619147);
         this.fontRendererObj.drawString("Поместите Древний пергамент", this.guiLeft + 41, this.guiTop + 60, 9408399);
      } else {
         ItemStack itemStack = this.slot.getStack();
         String title = ItemScrollTitle.getTitle(itemStack);
         this.drawStringWhenSize("Титул персонажа", 1.75F, 42, 45, 11629643);
         if (title.equals("")) {
            this.fontRendererObj.drawString("Установите на сайте", this.guiLeft + 41, this.guiTop + 60, 9408399);
         } else {
            int titleWidth = this.fontRendererObj.getStringWidth(title);
            this.drawStringWhenSize(title, (float)Math.min(160 / titleWidth, 1), 42, 60, 9408399);
         }
      }

      this.drawStringWhenSize("Назад в инвентарь", 0.75F, 25, 8, xx > 21 && xx < 105 && yy > 2 && yy < 15 ? -1 : 4802889);
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      if (this.sendId != -1) {
         ArrayList<String> list = new ArrayList();
         switch(this.sendId) {
         case 1:
            list.add("Количество запчастей для починки.");
            list.add("§7Можно получить, ломая предметы.");
         default:
            this.drawHoveringText(list, x + 10, y + 10);
         }
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      x -= this.guiLeft;
      y -= this.guiTop;
      if ((!this.slot.getHasStack() || this.sendId < 2) && x > 21 && x < 105 && y > 2 && y < 15) {
         InventoryTab.inventoryTab.openInventoryId(1, false);
      }

   }

   public static class ContainerScrollTitle extends Container {
      public ContainerScrollTitle(EntityPlayer player) {
         ExtendedPlayer extendedPlayer = ExtendedPlayer.getExtendedPlayer(player);
         this.addSlotToContainer(new Slots(extendedPlayer, 3, RegItems.itemScrollTitle, 240, 48));
         ContainerLetHelper.addPlayerSlots(player.inventory, this, 76, 163);
      }

      public boolean canInteractWith(EntityPlayer player) {
         return true;
      }

      public ItemStack transferStackInSlot(EntityPlayer player, int slotIn) {
         ItemStack itemstack = null;
         Slot slot = (Slot)this.inventorySlots.get(slotIn);
         if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int numRows = 27;
            if (slotIn < numRows) {
               if (!this.mergeItemStack(itemstack1, numRows, this.inventorySlots.size(), true)) {
                  return null;
               }
            } else if (!this.mergeItemStack(itemstack1, 0, numRows, false)) {
               return null;
            }

            if (itemstack1.stackSize == 0) {
               slot.putStack((ItemStack)null);
            } else {
               slot.onSlotChanged();
            }
         }

         return itemstack;
      }
   }
}
