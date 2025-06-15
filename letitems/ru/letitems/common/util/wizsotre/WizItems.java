package ru.letitems.common.util.wizsotre;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.WeightedRandomChestContent;
import ru.letitems.common.registry.RegItems;

public class WizItems {
   public static List<WizItems.WizBuildItems> wizItemsMap = new ArrayList(4);
   public static WeightedRandomChestContent[] wizItemsMapPush;
   public static final Gson GSON = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().registerTypeHierarchyAdapter(WeightedRandomChestContent.class, new JsonWeightedRandomChestContent()).registerTypeHierarchyAdapter(ItemStack.class, new JsonItemStack()).registerTypeHierarchyAdapter(NBTBase.class, new JsonNBT()).create();

   public static void buildWizItems() {
   }

   static {
      wizItemsMap.add(new WizItems.WizBuildItems(new ItemStack(RegItems.itemSiteCrafting, 3, 4), 17100, Arrays.asList("4", "0-2", "2", "15")));
      wizItemsMap.add(new WizItems.WizBuildItems(new ItemStack(RegItems.ItemMagicShard), 45900, Arrays.asList("8", "1-3", "8", "45")));
      wizItemsMap.add(new WizItems.WizBuildItems(new ItemStack(RegItems.itemKusFuel), 58500, Arrays.asList("13", "1-3", "12", "65")));
      wizItemsMap.add(new WizItems.WizBuildItems(new ItemStack(RegItems.ItemSD, 2), 37800, Arrays.asList("5", "2-4", "4", "30")));
   }

   public static class WizBuildItems {
      private final ItemStack stack;
      private final int i;
      private String time;
      private final List<String> list;

      public WizBuildItems(ItemStack stack, int i, List<String> list) {
         this.stack = stack;
         this.i = i;
         this.time = String.format("%01dч. %02dм.", i / 3600, i / 60 % 60);
         this.list = list;
      }

      public ItemStack getStack() {
         return this.stack;
      }

      public int getI() {
         return this.i;
      }

      public String getTime() {
         return this.time;
      }

      public List<String> getList() {
         return this.list;
      }
   }

   public static class WizStoreItemPush {
      public WeightedRandomChestContent[] items;
   }
}
