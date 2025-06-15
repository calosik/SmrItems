package ru.letitems.common.inventory;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.letitems.common.items.ItemBag;

public final class InventoryBag implements IInventory {
   private static final String KEY_SLOTS = "Slots";
   private static final String KEY_UID = "UID";
   private static final String KEY_UPDATE = "UPDATE";
   private final EntityPlayer player;
   private final ItemStack stack;
   private final ItemStack[] inventoryStacks;
   private int update;

   public InventoryBag(EntityPlayer player, ItemStack stack) {
      Preconditions.checkNotNull(stack, "stack must not be null");
      Item item = stack.getItem();
      Preconditions.checkArgument(item instanceof ItemBag, "stack.getItem() must be ItemBag");
      this.player = player;
      this.stack = stack;
      this.inventoryStacks = new ItemStack[((ItemBag)item).getInventorySize()];
      this.setUID();
      this.readFromNBT(stack.getTagCompound());
   }

   public ItemStack decrStackSize(int i, int j) {
      ItemStack stack = this.getStackInSlot(i);
      if (stack == null) {
         return null;
      } else if (stack.stackSize <= j) {
         this.setInventorySlotContents(i, (ItemStack)null);
         return stack;
      } else {
         ItemStack product = stack.splitStack(j);
         this.setInventorySlotContents(i, stack);
         return product;
      }
   }

   public void setInventorySlotContents(int i, ItemStack stack) {
      if (stack != null && stack.stackSize <= 0) {
         stack = null;
      }

      this.inventoryStacks[i] = stack;
      ItemStack parent = this.getParent();
      NBTTagCompound nbt = parent.getTagCompound();
      if (nbt == null) {
         nbt = new NBTTagCompound();
         parent.setTagCompound(nbt);
      }

      NBTTagCompound slotNbt;
      if (!nbt.hasKey("Slots", 10)) {
         slotNbt = new NBTTagCompound();
         nbt.setTag("Slots", slotNbt);
      } else {
         slotNbt = nbt.getCompoundTag("Slots");
      }

      String slotKey = getSlotNBTKey(i);
      if (stack == null) {
         slotNbt.removeTag(slotKey);
      } else {
         NBTTagCompound itemNbt = new NBTTagCompound();
         stack.writeToNBT(itemNbt);
         slotNbt.setTag(slotKey, itemNbt);
      }

   }

   public ItemStack getStackInSlot(int i) {
      return this.inventoryStacks[i];
   }

   public int getSizeInventory() {
      return this.inventoryStacks.length;
   }

   public String getInventoryName() {
      return "Bag";
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public void markDirty() {
      ++this.update;
      this.writeToParentNBT();
   }

   public boolean isUseableByPlayer(EntityPlayer player) {
      if (!this.isParentItemInventory(player.getCurrentEquippedItem())) {
         return false;
      } else {
         NBTTagCompound nbt = this.getParent().getTagCompound();
         return nbt != null && this.update == nbt.getInteger("UPDATE");
      }
   }

   public boolean hasCustomInventoryName() {
      return true;
   }

   public boolean isItemValidForSlot(int slotIndex, ItemStack stack) {
      return true;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public ItemStack getStackInSlotOnClosing(int slot) {
      ItemStack toReturn = this.getStackInSlot(slot);
      if (toReturn != null) {
         this.setInventorySlotContents(slot, (ItemStack)null);
      }

      return toReturn;
   }

   public boolean isParentItemInventory(ItemStack stack) {
      ItemStack parent = this.getParent();
      return isSameItemInventory(parent, stack);
   }

   private void setUID() {
      ItemStack parent = this.getParent();
      if (parent.getTagCompound() == null) {
         parent.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbt = parent.getTagCompound();
      if (!nbt.hasKey("UID")) {
         nbt.setString("UID", UUID.randomUUID().toString());
      }

   }

   public ItemStack getParent() {
      ItemStack equipped = this.player.getCurrentEquippedItem();
      return isSameItemInventory(equipped, this.stack) ? equipped : this.stack;
   }

   private void readFromNBT(NBTTagCompound nbt) {
      if (nbt != null) {
         if (nbt.hasKey("Slots")) {
            NBTTagCompound nbtSlots = nbt.getCompoundTag("Slots");

            for(int i = 0; i < this.inventoryStacks.length; ++i) {
               String slotKey = getSlotNBTKey(i);
               if (nbtSlots.hasKey(slotKey)) {
                  NBTTagCompound itemNbt = nbtSlots.getCompoundTag(slotKey);
                  ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemNbt);
                  this.inventoryStacks[i] = itemStack;
               } else {
                  this.inventoryStacks[i] = null;
               }
            }
         }

         if (nbt.hasKey("UPDATE")) {
            this.update = nbt.getInteger("UPDATE");
         } else {
            nbt.setInteger("UPDATE", 0);
         }

      }
   }

   public void writeToParentNBT() {
      ItemStack parent = this.getParent();
      if (parent != null) {
         NBTTagCompound nbt = parent.getTagCompound();
         NBTTagCompound slotsNbt = new NBTTagCompound();

         for(int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemStack = this.getStackInSlot(i);
            if (itemStack != null) {
               String slotKey = getSlotNBTKey(i);
               NBTTagCompound itemNbt = new NBTTagCompound();
               itemStack.writeToNBT(itemNbt);
               slotsNbt.setTag(slotKey, itemNbt);
            }
         }

         nbt.setTag("Slots", slotsNbt);
         nbt.setInteger("UPDATE", this.update);
      }
   }

   private static boolean isSameItemInventory(ItemStack base, ItemStack comparison) {
      if (base != null && comparison != null) {
         if (base.getItem() != comparison.getItem()) {
            return false;
         } else if (base.hasTagCompound() && comparison.hasTagCompound()) {
            String baseUID = base.getTagCompound().getString("UID");
            String comparisonUID = comparison.getTagCompound().getString("UID");
            return baseUID != null && baseUID.equals(comparisonUID);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static String getSlotNBTKey(int i) {
      return Integer.toString(i, 36);
   }
}
