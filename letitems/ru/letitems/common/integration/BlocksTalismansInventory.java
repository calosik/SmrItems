package ru.letitems.common.integration;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class BlocksTalismansInventory implements IInventory {
   private static final String TAG_BLOCK_NAME = "blockName";
   private static final String TAG_BLOCK_META = "blockMeta";
   private static final String TAG_BLOCK_COUNT = "blockCount";
   private final IInventory parentInventory;
   private final int parentInventorySlot;
   private final ItemStack stack;

   public BlocksTalismansInventory(IInventory parentInventory, int parentInventorySlot, ItemStack stack) {
      this.parentInventory = parentInventory;
      this.parentInventorySlot = parentInventorySlot;
      this.stack = stack;
   }

   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int slot) {
      int stackSize = this.getInt(this.stack, "blockCount");
      if (stackSize <= 0) {
         return null;
      } else {
         Block block = Block.getBlockFromName(this.getString(this.stack));
         if (block != null && block != Blocks.air) {
            Item item = Item.getItemFromBlock(block);
            return item == null ? null : new ItemStack(item, stackSize, this.getInt(this.stack, "blockMeta"));
         } else {
            return null;
         }
      }
   }

   public ItemStack decrStackSize(int slot, int amount) {
      ItemStack stack = this.getStackInSlot(slot);
      if (stack == null) {
         return null;
      } else if (stack.stackSize <= amount) {
         this.setInventorySlotContents(slot, (ItemStack)null);
         return stack;
      } else {
         ItemStack resultStack = stack.splitStack(amount);
         if (stack.stackSize <= 0) {
            this.setInventorySlotContents(slot, (ItemStack)null);
         } else {
            this.setInventorySlotContents(slot, stack);
         }

         return resultStack;
      }
   }

   public ItemStack getStackInSlotOnClosing(int slot) {
      ItemStack stack = this.getStackInSlot(slot);
      if (stack == null) {
         return null;
      } else {
         this.setInventorySlotContents(slot, (ItemStack)null);
         return stack;
      }
   }

   public void setInventorySlotContents(int slot, ItemStack stack) {
      Block block = stack != null && stack.stackSize > 0 ? Block.getBlockFromItem(stack.getItem()) : null;
      if (block == Blocks.air) {
         block = null;
      }

      if (block == null) {
         if (this.stack.hasTagCompound()) {
            NBTTagCompound nbt = this.stack.getTagCompound();
            nbt.removeTag("blockName");
            nbt.removeTag("blockMeta");
            nbt.removeTag("blockCount");
         }
      } else {
         this.setString(this.stack, Block.blockRegistry.getNameForObject(block));
         this.setInt(this.stack, "blockMeta", stack.getItemDamage());
         this.setInt(this.stack, "blockCount", stack.stackSize);
      }

   }

   public String getInventoryName() {
      return null;
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public int getInventoryStackLimit() {
      return 2147483547;
   }

   public void markDirty() {
      this.parentInventory.setInventorySlotContents(this.parentInventorySlot, this.stack);
   }

   public boolean isUseableByPlayer(EntityPlayer player) {
      return true;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int slot, ItemStack stack) {
      Block block = stack != null && stack.stackSize > 0 ? Block.getBlockFromItem(stack.getItem()) : null;
      return block != null && block != Blocks.air;
   }

   private NBTTagCompound getNBT(ItemStack stack) {
      if (stack == null) {
         return null;
      } else {
         if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
         }

         return stack.getTagCompound();
      }
   }

   private void setString(ItemStack stack, String s) {
      this.getNBT(stack).setString("blockName", s);
   }

   private void setInt(ItemStack stack, String tag, int i) {
      this.getNBT(stack).setInteger(tag, i);
   }

   private boolean verifyExistance(ItemStack stack, String tag) {
      return stack != null && this.getNBT(stack).hasKey(tag);
   }

   private String getString(ItemStack stack) {
      return this.verifyExistance(stack, "blockName") ? this.getNBT(stack).getString("blockName") : "";
   }

   private int getInt(ItemStack stack, String tag) {
      return this.verifyExistance(stack, tag) ? this.getNBT(stack).getInteger(tag) : 0;
   }
}
