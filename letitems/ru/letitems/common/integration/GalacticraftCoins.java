package ru.letitems.common.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;

public final class GalacticraftCoins {
   public static GalacticraftCoins galacticraftCoins = new GalacticraftCoins();
   private final ExecutorService executor = Executors.newFixedThreadPool(3);
   private final List<GalacticraftCoins.ItemStoreIrina> gcListResources;
   private final List<GalacticraftCoins.ItemStoreIrina> gcListMech = new ArrayList(5);

   public GalacticraftCoins() {
      this.gcListMech.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(GCItems.rocketTier1), 2));
      this.gcListMech.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 14), 10));
      this.gcListMech.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 24), 24));
      this.gcListMech.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 25), 30));
      this.gcListMech.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.blackLotus, 1, 1), 1));
      this.gcListResources = new ArrayList(4);
      this.gcListResources.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 5), 2));
      this.gcListResources.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 14), 10));
      this.gcListResources.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 24), 24));
      this.gcListResources.add(new GalacticraftCoins.ItemStoreIrina(new ItemStack(ModItems.manaResource, 1, 25), 30));
   }

   public void addPlayer(String playerName, int coins) {
   }

   public List<GalacticraftCoins.ItemStoreIrina> getGcListResources() {
      return this.gcListResources;
   }

   public List<GalacticraftCoins.ItemStoreIrina> getGcListMech() {
      return this.gcListMech;
   }

   // $FF: synthetic method
   private static void lambda$addPlayer$0(String playerName, int coins) {
   }

   public static class ItemStoreIrina {
      public final ItemStack stack;
      public final int coins;
      public boolean lock;

      private ItemStoreIrina(ItemStack stack, int coins) {
         this.lock = false;
         this.stack = stack;
         this.coins = coins;
      }

      public void setLock(boolean lock) {
         this.lock = lock;
      }

      // $FF: synthetic method
      ItemStoreIrina(ItemStack x0, int x1, Object x2) {
         this(x0, x1);
      }
   }
}
