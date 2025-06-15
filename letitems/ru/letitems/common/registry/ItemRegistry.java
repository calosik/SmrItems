package ru.letitems.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import ru.letitems.common.LetItems;
import ru.letitems.common.block.BlockAnchor;
import ru.letitems.common.block.BlockDakimakura;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.block.BlockFlowersEvent;
import ru.letitems.common.block.BlockMinePortal;
import ru.letitems.common.block.BlockOreArtefor;
import ru.letitems.common.block.BlockPlayerDetector;
import ru.letitems.common.block.BlockSiteCrafting;
import ru.letitems.common.block.BlockVendingMachine;
import ru.letitems.common.items.ItemArtefe;
import ru.letitems.common.items.ItemArtefeUp;
import ru.letitems.common.items.ItemArtifactRyota;
import ru.letitems.common.items.ItemAsuka;
import ru.letitems.common.items.ItemBag;
import ru.letitems.common.items.ItemBuildersWand;
import ru.letitems.common.items.ItemCheck;
import ru.letitems.common.items.ItemCmd;
import ru.letitems.common.items.ItemCosmetic;
import ru.letitems.common.items.ItemHairBand;
import ru.letitems.common.items.ItemMagicShard;
import ru.letitems.common.items.ItemMikuCover;
import ru.letitems.common.items.ItemReZero;
import ru.letitems.common.items.ItemRimuru;
import ru.letitems.common.items.ItemRussianRoulette;
import ru.letitems.common.items.ItemSD;
import ru.letitems.common.items.ItemScrollTitle;
import ru.letitems.common.items.ItemSelect;
import ru.letitems.common.items.ItemSiteCrafting;
import ru.letitems.common.items.ItemZero;
import ru.letitems.common.util.registry.RegistryUtils;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public final class ItemRegistry {
   public static void init() {
      initItems();
      addAspects();
      addDungeonLoot();
      Crafting.init();
   }

   private static void initItems() {
      RegItems.ItemSelect = RegistryUtils.registerItem(new ItemSelect("ItemSelect"));
      RegItems.ItemCheck = RegistryUtils.registerItem(new ItemCheck("ItemCheck"));
      RegItems.itemBag1 = RegistryUtils.registerItem(new ItemBag(1));
      RegItems.itemBag2 = RegistryUtils.registerItem(new ItemBag(2));
      RegItems.itemBag3 = RegistryUtils.registerItem(new ItemBag(3));
      RegItems.itemBag4 = RegistryUtils.registerItem(new ItemBag(4));
      RegItems.itemBag5 = RegistryUtils.registerItem(new ItemBag(5, true));
      RegItems.hairBand = (ItemHairBand)RegistryUtils.registerItem(new ItemHairBand(), "hairBand");
      RegItems.blockVendingMachine = RegistryUtils.registerBlock(new BlockVendingMachine());
      RegItems.ItemSD = RegistryUtils.registerItem(new ItemSD("ItemSD"));
      RegItems.ItemCmd = RegistryUtils.registerItem(new ItemCmd("ItemCmd"));
      if (LetItems.loadIC2) {
         RegItems.itemArtifactRyota = RegistryUtils.registerItem(new ItemArtifactRyota());
      }

      RegItems.ItemZero = RegistryUtils.registerItem(new ItemZero("ItemZero"));
      RegItems.ItemReZero = RegistryUtils.registerItem(new ItemReZero("ItemReZero"));
      RegItems.ItemAsuka = RegistryUtils.registerItem(new ItemAsuka("ItemAsuka"));
      RegItems.ItemArtefe = RegistryUtils.registerItem(new ItemArtefe("ItemArtefe"));
      RegItems.OreArteforOverworld = RegistryUtils.registerBlock(new BlockOreArtefor("OreArteforNormal"));
      RegItems.OreArteforNether = RegistryUtils.registerBlock(new BlockOreArtefor("OreArteforNether"));
      RegItems.OreArteforEnd = RegistryUtils.registerBlock(new BlockOreArtefor("OreArteforEnd"));
      ItemBuildersWand.BuildersWandType[] wandTypes = ItemBuildersWand.BuildersWandType.values();
      RegItems.itemBuildersWands = new Item[wandTypes.length];

      for(int i = 0; i < wandTypes.length; ++i) {
         RegItems.itemBuildersWands[i] = RegistryUtils.registerItem(new ItemBuildersWand(wandTypes[i]));
      }

      RegItems.ItemMagicShard = RegistryUtils.registerItem(new ItemMagicShard("ItemMagicShard"));
      RegItems.ItemMikuCover = RegistryUtils.registerItem(new ItemMikuCover("ItemMikuCover"));
      BlockDoll.DollType[] dollTypes = BlockDoll.DollType.values();
      RegItems.DollBlocks = new Block[MathHelper.ceiling_double_int((double)dollTypes.length / 16.0D)];

      for(int i = 0; i < RegItems.DollBlocks.length; ++i) {
         RegItems.DollBlocks[i] = RegistryUtils.registerBlock(new BlockDoll("Doll", i * 16), (String)("Doll" + i));
      }

      RegItems.anchor = (BlockAnchor)RegistryUtils.registerBlock(new BlockAnchor());
      RegItems.blockPlayerDetector = (BlockPlayerDetector)RegistryUtils.registerBlock(new BlockPlayerDetector());
      RegItems.blockDakimakura = RegistryUtils.registerBlock(new BlockDakimakura());
      RegItems.itemSiteCrafting = RegistryUtils.registerItem(new ItemSiteCrafting("SiteCrafting"));
      RegItems.itemKusFuel = RegistryUtils.registerItem(new ItemArtefeUp(3));
      RegItems.itemRimuru = RegistryUtils.registerItem(new ItemRimuru("ItemRimuru"));
      RegItems.itemRRPistol = RegistryUtils.registerItem(new ItemRussianRoulette("ItemRussianRoulette"));
      RegItems.itemScrollTitle = RegistryUtils.registerItem(new ItemScrollTitle("ItemScrollTitle"));
      RegItems.itemCosmetics = RegistryUtils.registerItem(new ItemCosmetic("cosmetic"));
      RegistryUtils.registerBlock(new BlockFlowersEvent());
      RegistryUtils.registerBlock(new BlockSiteCrafting());
      RegistryUtils.registerBlock(new BlockMinePortal());
   }

   private static void addAspects() {
      if (LetItems.loadThaumCraft) {
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.OreArteforOverworld, 1, 0), (new AspectList()).add(Aspect.CRYSTAL, 4).add(Aspect.MAGIC, 5));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.OreArteforNether, 1, 0), (new AspectList()).add(Aspect.CRYSTAL, 4).add(Aspect.MAGIC, 5));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.OreArteforEnd, 1, 0), (new AspectList()).add(Aspect.CRYSTAL, 4).add(Aspect.MAGIC, 5));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.ItemSD, 1, 0), (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.SENSES, 4).add(Aspect.MIND, 5));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.ItemZero, 1, 0), (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.GREED, 5).add(Aspect.BEAST, 3).add(Aspect.CLOTH, 3));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.ItemReZero, 1, 0), (new AspectList()).add(Aspect.MAGIC, 6).add(Aspect.GREED, 8).add(Aspect.BEAST, 4).add(Aspect.CLOTH, 4));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.ItemArtefe, 1, 0), (new AspectList()).add(Aspect.CRYSTAL, 3).add(Aspect.MAGIC, 2));
         ThaumcraftApi.registerObjectTag(new ItemStack(RegItems.ItemMikuCover, 1, 0), (new AspectList()).add(Aspect.LIFE, 10).add(Aspect.SOUL, 6));
      }
   }

   private static void addDungeonLoot() {
      String[] types = new String[]{"dungeonChest", "mineshaftCorridor", "strongholdCorridor", "pyramidJungleChest", "pyramidJungleDispenser", "pyramidDesertyChest", "villageBlacksmith"};
      String[] var1 = types;
      int var2 = types.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String type = var1[var3];
         ChestGenHooks dungeonType = ChestGenHooks.getInfo(type);
         dungeonType.addItem(new WeightedRandomChestContent(new ItemStack(RegItems.ItemSD), 1, 2, 3));
         dungeonType.addItem(new WeightedRandomChestContent(new ItemStack(RegItems.itemSiteCrafting, 1, 3), 1, 1, 6));
      }

   }
}
