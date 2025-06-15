package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;

@SideOnly(Side.CLIENT)
public class GuiNewCreative extends GuiPreRenderContainer {
   private static final ResourceLocation bgInvCreative = new ResourceLocation("letitems", "textures/gui/bg-inv-creat.png");
   private static final int COUNT_SLOT_INV = 70;
   private static InventoryBasic basicInventory = new InventoryBasic("tmp", true, 70);
   private static int selectedTabIndex;
   private float currentScroll;
   private CreativeCrafting listener;
   private int guiMapY;
   private int sendIdTab;

   public GuiNewCreative(EntityPlayer player) {
      super(new GuiNewCreative.ContainerCreative(player));
      player.openContainer = this.inventorySlots;
      this.allowUserInput = false;
      this.sendIdTab = -1;
   }

   public void updateScreen() {
      if (!this.mc.playerController.isInCreativeMode()) {
         this.inventoryTab.openInventoryId(1, false);
      }

   }

   protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, int type) {
      boolean flag = type == 1;
      type = slotId == -999 && type == 0 ? 4 : type;
      InventoryPlayer inventoryplayer;
      ItemStack itemstack5;
      if (slotIn == null && type != 5) {
         inventoryplayer = this.mc.thePlayer.inventory;
         if (inventoryplayer.getItemStack() != null) {
            if (mouseButton == 0) {
               this.mc.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
               this.mc.playerController.sendPacketDropItem(inventoryplayer.getItemStack());
               inventoryplayer.setItemStack((ItemStack)null);
            }

            if (mouseButton == 1) {
               itemstack5 = inventoryplayer.getItemStack().splitStack(1);
               this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack5, true);
               this.mc.playerController.sendPacketDropItem(itemstack5);
               if (inventoryplayer.getItemStack().stackSize == 0) {
                  inventoryplayer.setItemStack((ItemStack)null);
               }
            }
         }
      } else if (type != 5 && slotIn.inventory == basicInventory) {
         inventoryplayer = this.mc.thePlayer.inventory;
         itemstack5 = inventoryplayer.getItemStack();
         ItemStack itemstack7 = slotIn.getStack();
         ItemStack itemstack8;
         if (type == 2) {
            if (itemstack7 != null && mouseButton >= 0 && mouseButton < 9) {
               itemstack8 = itemstack7.copy();
               itemstack8.stackSize = itemstack8.getMaxStackSize();
               this.mc.thePlayer.inventory.setInventorySlotContents(mouseButton, itemstack8);
               this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
            }

            return;
         }

         if (type == 3) {
            if (inventoryplayer.getItemStack() == null && slotIn.getHasStack()) {
               itemstack8 = slotIn.getStack().copy();
               itemstack8.stackSize = itemstack8.getMaxStackSize();
               inventoryplayer.setItemStack(itemstack8);
            }

            return;
         }

         if (type == 4) {
            if (itemstack7 != null) {
               itemstack8 = itemstack7.copy();
               itemstack8.stackSize = mouseButton == 0 ? 1 : itemstack8.getMaxStackSize();
               this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack8, true);
               this.mc.playerController.sendPacketDropItem(itemstack8);
            }

            return;
         }

         if (itemstack5 != null && itemstack7 != null && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7)) {
            if (mouseButton == 0) {
               if (flag) {
                  itemstack5.stackSize = itemstack5.getMaxStackSize();
               } else if (itemstack5.stackSize < itemstack5.getMaxStackSize()) {
                  ++itemstack5.stackSize;
               }
            } else if (itemstack5.stackSize <= 1) {
               inventoryplayer.setItemStack((ItemStack)null);
            } else {
               --itemstack5.stackSize;
            }
         } else if (itemstack7 != null && itemstack5 == null) {
            inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack7));
            itemstack5 = inventoryplayer.getItemStack();
            if (flag) {
               itemstack5.stackSize = itemstack5.getMaxStackSize();
            }
         } else {
            inventoryplayer.setItemStack((ItemStack)null);
         }
      } else {
         this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.mc.thePlayer);
         if (Container.func_94532_c(mouseButton) == 2) {
            for(int i = 0; i < 9; ++i) {
               Slot slotItem = this.inventorySlots.getSlot(70 + i);
               this.mc.playerController.sendSlotPacket(slotItem == null ? null : slotItem.getStack(), 34 + i);
            }
         } else if (slotIn != null) {
            Slot slotItem = this.inventorySlots.getSlot(slotIn.slotNumber);
            if (slotItem != null) {
               this.mc.playerController.sendSlotPacket(slotItem.getStack(), slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36 - 2);
            }
         }
      }

   }

   public void initGui() {
      if (this.mc.playerController.isInCreativeMode()) {
         super.initGui();
         CreativeTabs.creativeTabArray[5] = null;
         CreativeTabs.creativeTabArray[11] = null;
         int i = selectedTabIndex;
         selectedTabIndex = -1;
         this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
         this.listener = new CreativeCrafting(this.mc);
         this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.listener);
      } else {
         this.inventoryTab.openInventoryId(1, false);
      }

   }

   public void onGuiClosed() {
      super.onGuiClosed();
      if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
         this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.listener);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) {
      if (!this.checkHotbarKeys(keyCode)) {
         super.keyTyped(typedChar, keyCode);
      }

   }

   protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
      if (which == 0 && this.sendIdTab >= 0) {
         CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
         this.setCurrentCreativeTab(acreativetabs[this.sendIdTab]);
      }

      super.mouseMovedOrUp(mouseX, mouseY, which);
   }

   private boolean needsScrollBars() {
      if (CreativeTabs.creativeTabArray[selectedTabIndex] == null) {
         return false;
      } else {
         return CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((GuiNewCreative.ContainerCreative)this.inventorySlots).func_148328_e();
      }
   }

   private void setCurrentCreativeTab(CreativeTabs tab) {
      if (tab != null) {
         selectedTabIndex = tab.getTabIndex();
         GuiNewCreative.ContainerCreative slots = (GuiNewCreative.ContainerCreative)this.inventorySlots;
         this.field_147008_s.clear();
         slots.itemList.clear();
         tab.displayAllReleventItems(slots.itemList);
         this.currentScroll = 0.0F;
         slots.scrollTo(0.0F);
      }
   }

   public void handleMouseInput() {
      super.handleMouseInput();
      int i = Mouse.getEventDWheel();
      if (i != 0 && this.needsScrollBars() && this.sendIdTab == -2) {
         int j = ((GuiNewCreative.ContainerCreative)this.inventorySlots).itemList.size() / 14 - 5 + 1;
         if (i > 0) {
            i = 1;
         }

         if (i < 0) {
            i = -1;
         }

         this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
         this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
         ((GuiNewCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      int xx = mouseX - this.guiLeft;
      int yy = mouseY - this.guiTop;
      if (this.sendIdTab != -1) {
         CreativeTabs creativetabs = CreativeTabs.creativeTabArray[this.sendIdTab];
         if (creativetabs != null) {
            this.drawHoveringText(Collections.singletonList(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0])), mouseX, mouseY);
         }
      } else if (xx >= 0 && xx < 320 && yy >= 0 && yy < 115) {
         this.sendIdTab = -2;
      }

   }

   protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int x, int y) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(bgInvCreative);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      RenderHelper.enableGUIStandardItemLighting();
      GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.guiLeft, this.guiTop + 115, 340, 85);
      this.sendIdTab = -1;
      CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
      int kfkfkd = 0;
      int fdsfdsf = 0;
      int sizeTabs = acreativetabs.length;
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      if (xx >= 0 && xx < 320 && yy >= 114 && yy < 205) {
         int kj = Mouse.getDWheel();
         if (kj < 0) {
            this.guiMapY -= 12;
         } else if (kj > 0) {
            this.guiMapY += 12;
         }
      }

      int l = 0;

      int l2;
      int i1;
      for(int u = 0; l < sizeTabs; ++l) {
         if (l != 5 && l != 11) {
            CreativeTabs creativetabs1 = acreativetabs[l];
            if (creativetabs1 != null) {
               l2 = 25 + 25 * kfkfkd;
               i1 = 115 + fdsfdsf + this.guiMapY;
               if ((u + 1) % 10 == 0) {
                  fdsfdsf += 26;
                  kfkfkd = 0;
               } else {
                  ++kfkfkd;
               }

               ++u;
               l2 += 12;
               i1 += 9;
               if (i1 <= 200 && i1 >= 102) {
                  boolean isFullHover = xx > l2 - 8 && xx < l2 + 24 && yy > i1 - 5 && yy < i1 + 17;
                  if (yy > 200 || yy < 113) {
                     isFullHover = false;
                  }

                  GL11.glEnable(32826);
                  ItemStack itemstack = creativetabs1.getIconItemStack();
                  itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, this.guiLeft + l2, this.guiTop + i1);
                  if (isFullHover) {
                     this.sendIdTab = l;
                  }
               }
            }
         }
      }

      GeneralClientUtils.ScissorHelper.endScissor();
      RenderHelper.disableStandardItemLighting();
      int maxSizeScroll = true;
      l2 = this.guiTop + 17;
      i1 = l2 + 95;
      if (CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && this.needsScrollBars()) {
         this.drawGradientRect(this.guiLeft + 297 + 3, this.guiTop + 17 + 95, this.guiLeft + 297, this.guiTop + 17, -1072689136, -1072689136);
         this.drawGradientRect(this.guiLeft + 297 + 3, l2 + (int)((float)(i1 - l2 - 1) * this.currentScroll), this.guiLeft + 297, l2 + (int)((float)(i1 - l2 - 1) * this.currentScroll) - 5, -1, -1);
      }

   }

   public int func_147056_g() {
      return selectedTabIndex;
   }

   static {
      selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
   }

   @SideOnly(Side.CLIENT)
   static class ContainerCreative extends Container {
      public List<ItemStack> itemList = new ArrayList();

      public ContainerCreative(EntityPlayer player) {
         InventoryPlayer inventoryplayer = player.inventory;

         int i;
         for(i = 0; i < 5; ++i) {
            for(int j = 0; j < 14; ++j) {
               this.addSlotToContainer(new Slot(GuiNewCreative.basicInventory, i * 14 + j, 28 + j * 19, 18 + i * 19));
            }
         }

         for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryplayer, i, 76 + i * 19, 208));
         }

         this.scrollTo(0.0F);
      }

      public boolean canInteractWith(EntityPlayer player) {
         return true;
      }

      public void scrollTo(float v) {
         int sizeItemList = this.itemList.size();
         int i = sizeItemList / 14 - 5 + 1;
         int j = (int)((double)(v * (float)i) + 0.5D);
         if (j < 0) {
            j = 0;
         }

         for(int k = 0; k < 5; ++k) {
            for(int l = 0; l < 14; ++l) {
               int i1 = l + (k + j) * 14;
               if (i1 >= 0 && i1 < sizeItemList) {
                  GuiNewCreative.basicInventory.setInventorySlotContents(l + k * 14, (ItemStack)this.itemList.get(i1));
               } else {
                  GuiNewCreative.basicInventory.setInventorySlotContents(l + k * 14, (ItemStack)null);
               }
            }
         }

      }

      public boolean func_148328_e() {
         return this.itemList.size() > 70;
      }

      protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {
      }

      public ItemStack transferStackInSlot(EntityPlayer player, int i) {
         if (i >= this.inventorySlots.size() - 9 && i < this.inventorySlots.size()) {
            Slot slot = (Slot)this.inventorySlots.get(i);
            if (slot != null && slot.getHasStack()) {
               slot.putStack((ItemStack)null);
            }
         }

         return null;
      }

      public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
         return p_94530_2_.yDisplayPosition > 90;
      }

      public boolean canDragIntoSlot(Slot slot) {
         return slot.inventory instanceof InventoryPlayer || slot.yDisplayPosition > 90 && slot.xDisplayPosition <= 162;
      }
   }
}
