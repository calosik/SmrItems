package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientParams;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.client.util.LoadAndRenderPic;
import ru.letitems.common.inventory.container.ContainerLetHelper;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketGuid;
import ru.letitems.common.util.GeneralUtils;

@SideOnly(Side.CLIENT)
public class GuiAttackRepair extends GuiPreRenderContainer {
   private int sendId = -1;
   private final GuiAttackRepair.ContainerAttackRepair container;

   public GuiAttackRepair(EntityPlayer player) {
      super(new GuiAttackRepair.ContainerAttackRepair(player.inventory, player));
      this.allowUserInput = false;
      this.container = (GuiAttackRepair.ContainerAttackRepair)this.inventorySlots;
      if (!ClientParams.isUnlockAch("attack_repair")) {
         LoadAndRenderPic.instance.queueResearchInformation("300.png", "R300.png");
      }

   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GeneralClientUtils.bind("letitems:textures/gui/bg-attack-repair.png");
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      this.sendId = -1;
      int xx = mouseX - this.guiLeft;
      int yy = mouseY - this.guiTop;
      if (xx > 302 && xx < 320 && yy > 3 && yy < 25) {
         this.drawGradientRect(this.guiLeft + 24 + 270, this.guiTop + 90 + 62, this.guiLeft + 24, this.guiTop + 90, -2144522962, -2144522962);
         this.drawStringWhenSize("Информация", 1.25F, 34, 98, 13290186);
         this.fontRendererObj.drawString("Это возможность починить и убрать проклятия", this.guiLeft + 34, this.guiTop + 78 + 33, 9408399);
         this.fontRendererObj.drawString("с предметов. Починка стоит запчастей, а", this.guiLeft + 34, this.guiTop + 88 + 33, 9408399);
         this.fontRendererObj.drawString("проклятия 2 эссенции. Стоимость починки", this.guiLeft + 34, this.guiTop + 98 + 33, 9408399);
         this.fontRendererObj.drawString("зависит от количества повреждений предмета.", this.guiLeft + 34, this.guiTop + 108 + 33, 9408399);
      } else if (!ClientParams.isUnlockAch("attack_repair")) {
         this.drawGradientRect(this.guiLeft + 24 + 270, this.guiTop + 90 + 62, this.guiLeft + 24, this.guiTop + 90, -2144522962, -2144522962);
         this.drawStringWhenSize("Ремонт снаряжения", 1.75F, 90, 105, 16777215);
         this.fontRendererObj.drawString("Нет достижения 'Мастер битв'", this.guiLeft + 90, this.guiTop + 60 + 60, 9408399);
         this.fontRendererObj.drawString("Нет доступа", this.guiLeft + 90, this.guiTop + 72 + 60, 8092539);
         LoadAndRenderPic.instance.renderPic("R300.png", 1.5F, this.guiLeft + 30, this.guiTop + 49 + 45, 1.0F);
      } else {
         ItemStack itemHand = this.container.slotARepair.getStackInSlot(0);
         String colorText;
         if (itemHand != null) {
            if (itemHand.isItemStackDamageable() && itemHand.getItem().isRepairable()) {
               int countRepairCost;
               if (itemHand.getItemDamage() <= 0) {
                  this.drawStringWhenSize("Не нуждается в починке", 1.75F, 51, 100, 11629643);
                  this.fontRendererObj.drawString("Этот предмет можно магически разложить", this.guiLeft + 51, this.guiTop + 118, 9408399);
               } else {
                  this.drawStringWhenSize("Нуждается в починке", 1.75F, 63, 100, 4954289);
                  colorText = "3";
                  if (xx > 144 && xx < 197 && yy > 120 && yy < 133) {
                     this.sendId = 3;
                     colorText = "6";
                  }

                  countRepairCost = MathHelper.clamp_int(itemHand.getItemDamage() - itemHand.getItemDamage() / itemHand.getMaxDamage(), 4, 80);
                  this.fontRendererObj.drawString("Этот предмет можно §" + colorText + "починить§r за §6" + countRepairCost + " запчастей", this.guiLeft + 39, this.guiTop + 118, 9408399);
               }

               colorText = "3";
               if (xx > 96 && xx < 132 && yy > 135 && yy < 146) {
                  this.sendId = 2;
                  colorText = "6";
               }

               countRepairCost = MathHelper.clamp_int(20 + itemHand.getMaxDamage() / 25, 21, 80);
               this.fontRendererObj.drawString("Нажмите §" + colorText + "сюда§r и получите §620-" + countRepairCost + " запчастей", this.guiLeft + 49, this.guiTop + 132, 9408399);
            } else {
               this.drawStringWhenSize("Не нуждается в починке", 1.75F, 51, 100, 11619147);
               this.fontRendererObj.drawString("Выберите более подходящий предмет", this.guiLeft + 63, this.guiTop + 118, 9408399);
            }

            if (GeneralUtils.isItemCurse(itemHand)) {
               this.drawStringWhenSize(itemHand.getRarity().rarityColor + "" + itemHand.getDisplayName(), 1.25F, 105, 31, 11629643);
               this.fontRendererObj.drawString("Предмет проклят и может", this.guiLeft + 105, this.guiTop + 44, 9408399);
               this.fontRendererObj.drawString("§4удалиться при смерти§r. Вы можете", this.guiLeft + 105, this.guiTop + 54, 9408399);
               colorText = "3";
               if (xx > 192 && xx < 254 && yy > 60 && yy < 80) {
                  this.sendId = 4;
                  colorText = "6";
               }

               this.fontRendererObj.drawString("очистить его за §" + colorText + "2 эссенции§r.", this.guiLeft + 105, this.guiTop + 64, 9408399);
            } else {
               this.drawStringWhenSize(itemHand.getRarity().rarityColor + "" + itemHand.getDisplayName(), 1.5F, 105, 41, 11629643);
               this.fontRendererObj.drawString("Предмет не проклят", this.guiLeft + 105, this.guiTop + 56, 9408399);
            }
         }

         if (xx > 30 && xx < 95 && yy > 62 && yy < 78) {
            this.sendId = 1;
         }

         colorText = (this.container.repairUserCost2 == 0 ? "§4" : "§7") + this.container.repairUserCost2 + "§7/1000";
         this.fontRendererObj.drawString(colorText, this.guiLeft + 62 - this.fontRendererObj.getStringWidth(colorText) / 2, this.guiTop + 66, 8092539);
      }
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      if (this.sendId != -1) {
         ArrayList<String> list = new ArrayList();
         switch(this.sendId) {
         case 1:
            list.add("Количество запчастей для починки.");
            list.add("§7Можно получить, ломая предметы.");
            break;
         case 2:
            list.add("Уничтожить '" + this.container.slotARepair.getStackInSlot(0).getDisplayName() + "'");
            list.add("§7Можно получить, ломая другие вещи.");
            break;
         case 3:
            list.add("§7Починить '" + this.container.slotARepair.getStackInSlot(0).getDisplayName() + "'");
            break;
         case 4:
            list.add("Снятие проклятия с предмета");
            list.add("§7Операция стоит 2 эссенции");
         }

         this.drawHoveringText(list, x + 10, y + 10);
      }

      this.fontRendererObj.drawString("?", this.guiLeft + this.xSize - 15, this.guiTop + 10, 9408399);
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.container.slotARepair.getStackInSlot(0) != null && this.sendId >= 2) {
         NetworkManager.sendToServer(new PacketGuid((byte)0, this.sendId - 1));
      }

   }

   public static class SlotARepair extends InventoryBasic {
      private final GuiAttackRepair.ContainerAttackRepair container;

      SlotARepair(GuiAttackRepair.ContainerAttackRepair attackRepair) {
         super("ARepair", true, 1);
         this.container = attackRepair;
      }

      public void markDirty() {
         super.markDirty();
         this.container.onCraftMatrixChanged(this);
      }
   }

   public static class ContainerAttackRepair extends Container {
      public final IInventory slotARepair = new GuiAttackRepair.SlotARepair(this);
      private final EntityPlayer player;
      public int repairUserCost2 = 0;
      private int repairUserCost3 = 0;

      public ContainerAttackRepair(IInventory inventory, EntityPlayer player) {
         this.player = player;
         this.addSlotToContainer(new Slot(this.slotARepair, 0, 54, 35));
         ContainerLetHelper.addPlayerSlots(player.inventory, this, 76, 163);
      }

      public boolean canInteractWith(EntityPlayer player) {
         return this.slotARepair.isUseableByPlayer(player) && !player.isDead;
      }

      public void onContainerClosed(EntityPlayer player) {
         super.onContainerClosed(player);
         ItemStack stack = this.slotARepair.getStackInSlot(0);
         if (stack != null && !player.inventory.addItemStackToInventory(stack)) {
            player.entityDropItem(stack, 0.2F);
         }

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

      public void addCraftingToCrafters(ICrafting crafter) {
         super.addCraftingToCrafters(crafter);
      }

      public void detectAndSendChanges() {
         super.detectAndSendChanges();
      }

      @SideOnly(Side.CLIENT)
      public void updateProgressBar(int id, int value) {
         if (id == 0) {
            this.repairUserCost2 = value;
         }

      }
   }
}
