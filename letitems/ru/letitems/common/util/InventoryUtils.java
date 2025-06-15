package ru.letitems.common.util;

import cpw.mods.fml.common.Optional.Method;
import java.util.Collection;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.letitems.common.LetItems;
import ru.letitems.common.integration.BlocksTalismansInventory;
import ru.letitems.common.inventory.MergedInventory;
import vazkii.botania.common.item.ItemBlackHoleTalisman;

public final class InventoryUtils {
   private static final Random RANDOM = new Random();
   public static final String TAG_ITEMS = "Items";
   public static final String TAG_SLOT = "Slot";

   public static MergedInventory getAllPlayerInventories(EntityPlayer player) {
      MergedInventory mergedInventory = new MergedInventory();
      mergedInventory.addInventory(player.inventory);
      if (LetItems.loadBotania) {
         findAllBlackHoleTalismans(mergedInventory, player.inventory);
      }

      if (LetItems.loadThaumCraft) {
         findAllBlockTalisman(mergedInventory, player.inventory);
      }

      return mergedInventory;
   }

   @Method(
      modid = "Botania"
   )
   private static void findAllBlackHoleTalismans(MergedInventory mergedInventory, IInventory inventory) {
      for(int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
         ItemStack stack = inventory.getStackInSlot(slot);
         if (stack != null && stack.stackSize > 0 && stack.getItem() instanceof ItemBlackHoleTalisman) {
            mergedInventory.addInventory(new BlocksTalismansInventory(inventory, slot, stack));
         }
      }

   }

   @Method(
      modid = "ThaumicTinkerer"
   )
   private static void findAllBlockTalisman(MergedInventory mergedInventory, IInventory inventory) {
      for(int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
         ItemStack stack = inventory.getStackInSlot(slot);
         if (stack != null && stack.stackSize > 0 && stack.getItem().getUnlocalizedName().equals("item.blockTalisman")) {
            mergedInventory.addInventory(new BlocksTalismansInventory(inventory, slot, stack));
         }
      }

   }

   public static void dropAllItems(IInventory inventory, EntityPlayer player, Collection<EntityItem> list) {
   }

   public static void dropAllItems(IInventory inventory, EntityPlayer player) {
   }

   public static void addItemStackToInventoryOrCollide(EntityPlayerMP player, ItemStack stack) {
   }

   public static void addItemToInventory(EntityPlayerMP player, ItemStack protoStack, int totalQuantity) {
   }

   public static int findCountItem(EntityPlayer player, int max, Item item, int meta) {
      int countItems = 0;
      return countItems;
   }

   public static void removeInventoryItem(EntityPlayer player, int count, Item item, int meta) {
   }

   public static void writeInvToNbt(NBTTagCompound nbt, String itemsTagName, IInventory inventory) {
      writeInvToNbt(nbt, itemsTagName, "Slot", inventory);
   }

   public static void readInvFromNbt(NBTTagCompound nbt, String itemsTagName, IInventory inventory) {
      readInvFromNbt(nbt, itemsTagName, "Slot", inventory);
   }

   public static void writeInvToNbt(NBTTagCompound nbt, String itemsTagName, String slotTagName, IInventory inventory) {
      NBTTagList list = new NBTTagList();
      int invSize = inventory.getSizeInventory();

      for(int slot = 0; slot < invSize; ++slot) {
         ItemStack stack = inventory.getStackInSlot(slot);
         if (stack != null && stack.stackSize > 0) {
            NBTTagCompound slotNbt = new NBTTagCompound();
            slotNbt.setByte(slotTagName, (byte)slot);
            stack.writeToNBT(slotNbt);
            list.appendTag(slotNbt);
         }
      }

      nbt.setTag(itemsTagName, list);
   }

   public static void readInvFromNbt(NBTTagCompound nbt, String itemsTagName, String slotTagName, IInventory inventory) {
      clearInv(inventory);
      NBTTagList list = nbt.getTagList(itemsTagName, 10);
      if (list.tagCount() > 0) {
         int invSize = inventory.getSizeInventory();

         for(int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound slotNbt = list.getCompoundTagAt(i);
            int slot = slotNbt.getByte(slotTagName) & 255;
            ItemStack stack = ItemStack.loadItemStackFromNBT(slotNbt);
            if (stack != null && stack.stackSize > 0 && slot < invSize) {
               inventory.setInventorySlotContents(slot, stack);
            }
         }
      }

   }

   public static boolean isEmpty(IInventory inventory) {
      return true;
   }

   private static void clearInv(IInventory inventory) {
   }

   public static boolean isItemEqual(ItemStack s1, ItemStack s2) {
      if (s1 == s2) {
         return true;
      } else if (s1 != null && s2 != null) {
         return s1.isItemEqual(s2) && ItemStack.areItemStackTagsEqual(s1, s2);
      } else {
         return false;
      }
   }

   public static boolean canAdd(Iterable<IInventory> inventories, ItemStack stack) {
      return false;
   }

   public static boolean add(Iterable<IInventory> inventories, ItemStack stack) {
      return false;
   }

   public static boolean canAdd(IInventory inventory, ItemStack stack) {
      return false;
   }

   public static boolean add(IInventory inventory, ItemStack stack) {
      return false;
   }

   public static boolean canRemove(IInventory inventory, ItemStack stack) {
      return false;
   }

   public static void remove(IInventory inventory, ItemStack stack) {
   }

   public static ItemStack copyWithSize(ItemStack stack, int size) {
      if (stack == null) {
         return null;
      } else {
         ItemStack clone = stack.copy();
         clone.stackSize = size;
         return clone;
      }
   }

   public static <T extends Item> T getItem(ItemStack stack, Class<T> itemClass) {
      if (stack == null) {
         return null;
      } else {
         Item item = stack.getItem();
         return itemClass.isInstance(item) ? item : null;
      }
   }
}
