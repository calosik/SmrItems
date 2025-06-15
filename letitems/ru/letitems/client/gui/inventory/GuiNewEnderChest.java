package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.screen.GuiEnderBonus;
import ru.letitems.common.inventory.container.ContainerLetHelper;

@SideOnly(Side.CLIENT)
public class GuiNewEnderChest extends GuiPreRenderContainer {
   private static final ResourceLocation bgInvEnderChest = new ResourceLocation("letitems", "textures/gui/bg-inv-ec.png");
   private final GuiNewEnderChest.ContainerEnderChest container;

   public GuiNewEnderChest(InventoryPlayer inventory, IInventory inventory1) {
      super(new GuiNewEnderChest.ContainerEnderChest(inventory, inventory1));
      this.allowUserInput = false;
      this.container = (GuiNewEnderChest.ContainerEnderChest)this.inventorySlots;
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int xx = mouseX - this.guiLeft;
      int yy = mouseY - this.guiTop;
      boolean hover = xx > 10 && xx < 102 && yy > 70 && yy < 231;
      this.mc.getTextureManager().bindTexture(bgInvEnderChest);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      this.drawStringWhenSize("Энд Бонус", 1.25F, 24, 197, 12763842);
      if (this.container.pubEnderBonus == 0) {
         this.drawStringWhenSize("Выберите бонус", 0.75F, 26, 210, 9539985);
      } else if (this.container.pubEnderBonus == 1) {
         this.drawStringWhenSize("Умения", 0.75F, 40, 210, 9539985);
      } else if (this.container.pubEnderBonus == 2) {
         this.drawStringWhenSize("Опыт", 0.75F, 48, 210, 9539985);
      } else if (this.container.pubEnderBonus == 3) {
         this.drawStringWhenSize("Урон", 0.75F, 48, 210, 9539985);
      }

      if (hover) {
         this.drawGradientRect(this.guiLeft + 12 + 88, this.guiTop + 71 + 159, this.guiLeft + 12, this.guiTop + 71, 277909648, 277909648);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      x -= this.guiLeft;
      y -= this.guiTop;
      if (x > 10 && x < 102 && y > 70 && y < 231) {
         this.mc.displayGuiScreen(new GuiEnderBonus(this.container.pubEnderBonus));
      }

   }

   public static class ContainerEnderChest extends Container {
      private final IInventory lowerChestInventory;
      private final EntityPlayer player;
      public int pubEnderBonus = 0;
      private int prEnderBonus = 0;

      public ContainerEnderChest(InventoryPlayer inventory, IInventory inventory1) {
         this.lowerChestInventory = inventory1;
         this.player = inventory.player;
         inventory1.openInventory();

         for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
               this.addSlotToContainer(new Slot(inventory1, k + j * 9, 125 + k * 19, 77 + j * 19));
            }
         }

         ContainerLetHelper.addPlayerSlots(inventory, this, 125, 154);
      }

      public boolean canInteractWith(EntityPlayer player) {
         return this.lowerChestInventory.isUseableByPlayer(player);
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

      public void onContainerClosed(EntityPlayer player) {
         super.onContainerClosed(player);
         this.lowerChestInventory.closeInventory();
      }

      public void addCraftingToCrafters(ICrafting crafter) {
         super.addCraftingToCrafters(crafter);
      }

      public void detectAndSendChanges() {
         super.detectAndSendChanges();
      }

      @SideOnly(Side.CLIENT)
      public void updateProgressBar(int id, int value) {
         if (id == 0) {
            this.pubEnderBonus = value;
         }

      }
   }
}
