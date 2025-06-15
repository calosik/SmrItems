package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.inventory.container.ContainerLetHelper;

@SideOnly(Side.CLIENT)
public class GuiNewCrafting extends GuiPreRenderContainer {
   private final ResourceLocation bgInvCrafting = new ResourceLocation("letitems", "textures/gui/bg-inv-crafting.png");

   public GuiNewCrafting(EntityPlayer player) {
      super(new GuiNewCrafting.ContainerWorkbench(player));
      this.allowUserInput = false;
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(this.bgInvCrafting);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
   }

   public static class ContainerWorkbench extends Container {
      public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
      public IInventory craftResult = new InventoryCraftResult();
      private final World worldObj;

      public ContainerWorkbench(EntityPlayer player) {
         this.worldObj = player.worldObj;
         this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 186, 115));

         for(int l = 0; l < 3; ++l) {
            for(int i1 = 0; i1 < 3; ++i1) {
               this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 109 + i1 * 19, 96 + l * 19));
            }
         }

         ContainerLetHelper.addPlayerSlots(player.inventory, this, 76, 163);
         this.onCraftMatrixChanged(this.craftMatrix);
      }

      public void onCraftMatrixChanged(IInventory inventory) {
         this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
      }

      public void onContainerClosed(EntityPlayer player) {
         super.onContainerClosed(player);
         if (!this.worldObj.isRemote) {
            for(int i = 0; i < 9; ++i) {
               ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
               if (itemstack != null) {
                  player.dropPlayerItemWithRandomChoice(itemstack, false);
               }
            }
         }

      }

      public boolean canInteractWith(EntityPlayer player) {
         return player instanceof EntityPlayerMP;
      }

      public ItemStack transferStackInSlot(EntityPlayer player, int slotIn) {
         ItemStack itemstack = null;
         Slot slot = (Slot)this.inventorySlots.get(slotIn);
         if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotIn == 0) {
               if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                  return null;
               }

               slot.onSlotChange(itemstack1, itemstack);
            } else if (slotIn >= 10 && slotIn < 37) {
               if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
                  return null;
               }
            } else if (slotIn >= 37 && slotIn < 46) {
               if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
                  return null;
               }
            } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
               return null;
            }

            if (itemstack1.stackSize == 0) {
               slot.putStack((ItemStack)null);
            } else {
               slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
               return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
         }

         return itemstack;
      }

      public boolean func_94530_a(ItemStack stack, Slot slot) {
         return slot.inventory != this.craftResult && super.func_94530_a(stack, slot);
      }
   }
}
