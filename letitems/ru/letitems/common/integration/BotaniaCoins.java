package ru.letitems.common.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;

public final class BotaniaCoins {
   public static BotaniaCoins botaniaCoins = new BotaniaCoins();
   private final ExecutorService executor = Executors.newFixedThreadPool(3);
   private final List<BotaniaCoins.ItemStoreALice> itemStoreALice = new ArrayList(5);
   private final List<BotaniaCoins.ItemStoreALice> relicStoreALice;

   public BotaniaCoins() {
      this.itemStoreALice.add(new BotaniaCoins.ItemStoreALice(new ItemStack(ModItems.manaResource, 1, 5), 2));
      this.itemStoreALice.add(new BotaniaCoins.ItemStoreALice(new ItemStack(ModItems.manaResource, 1, 14), 10));
      this.itemStoreALice.add(new BotaniaCoins.ItemStoreALice(new ItemStack(ModItems.manaResource, 1, 24), 24));
      this.itemStoreALice.add(new BotaniaCoins.ItemStoreALice(new ItemStack(ModItems.manaResource, 1, 25), 30));
      this.itemStoreALice.add(new BotaniaCoins.ItemStoreALice(new ItemStack(ModItems.blackLotus, 1, 1), 1));
      ItemStack[] relics = this.getListRelics();
      this.relicStoreALice = new ArrayList(relics.length);
      ItemStack[] var2 = relics;
      int var3 = relics.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack stack = var2[var4];
         this.relicStoreALice.add(new BotaniaCoins.ItemStoreALice(stack, ((IRelic)stack.getItem()).getCoins()));
      }

   }

   public void addPlayer(String playerName, int coins, int will) {
   }

   private ItemStack[] getListRelics() {
      return new ItemStack[]{new ItemStack(ModItems.infiniteFruit), new ItemStack(ModItems.kingKey), new ItemStack(ModItems.flugelEye), new ItemStack(ModItems.thorRing), new ItemStack(ModItems.odinRing), new ItemStack(ModItems.lokiRing), new ItemStack(ModItems.maniRing)};
   }

   public List<BotaniaCoins.ItemStoreALice> getListAliceItems() {
      return this.itemStoreALice;
   }

   public List<BotaniaCoins.ItemStoreALice> getListAliceRelics() {
      return this.relicStoreALice;
   }

   // $FF: synthetic method
   private static void lambda$addPlayer$0(String playerName, int coins, int will) {
   }

   public static class ItemStoreALice {
      public final ItemStack stack;
      public final int coins;
      public boolean lock;

      private ItemStoreALice(ItemStack stack, int coins) {
         this.lock = false;
         this.stack = stack;
         this.coins = coins;
      }

      public void setLock(boolean lock) {
         this.lock = lock;
      }

      // $FF: synthetic method
      ItemStoreALice(ItemStack x0, int x1, Object x2) {
         this(x0, x1);
      }
   }
}
