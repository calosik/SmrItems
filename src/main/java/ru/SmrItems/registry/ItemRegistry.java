package ru.SmrItems.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import ru.SmrItems.common.blocks.BlockDoll;
import ru.SmrItems.items.CompressUnstableCrystal;
import ru.SmrItems.items.FuelUnstable;
import ru.SmrItems.items.ItemCase;
import ru.SmrItems.items.ItemHairBand;
import ru.SmrItems.items.SelectItem;
import ru.SmrItems.items.UnstableDrop;
import ru.SmrItems.items.UnstableDust;
import ru.SmrItems.util.registry.RegistryUtils;
import ru.SmrItems.world.ore.UnstableOre;
import ru.SmrItems.world.ore.UnstableOreEnd;

public class ItemRegistry {
   public static void init() {
      initItems();
   }

   private static void initItems() {
      BlockDoll.DollType[] dollTypes = BlockDoll.DollType.values();
      RegItems.DollBlocks = new Block[MathHelper.ceiling_double_int((double)dollTypes.length / 16.0D)];

      for(int i = 0; i < RegItems.DollBlocks.length; ++i) {
      RegItems.DollBlocks[i] = RegistryUtils.registerBlock(new BlockDoll("Doll", i * 16), (String)("Doll" + i));
      }

      RegItems.fuelunstable = new FuelUnstable("FuelUnstable", "FuelUnstable2", 64);
      RegItems.unstableore = new UnstableOre("UnstableOre", "UnstableOre", 64);
      RegItems.unstableoreEnd = new UnstableOreEnd("UnstableOreEnd", "UnstableOreEnd", 64);
      RegItems.SelectItem = new SelectItem("ItemSelect", "ItemSelect", 1);
      RegItems.unstabledrop = new UnstableDrop("UnstableDrop", "UnstableDrop", 64);
      RegItems.unstablecrystal = new CompressUnstableCrystal("CompressUnstableCrystal", "CompressUnstableCrystal", 64);
      RegItems.unstabledust = new UnstableDust("UnstableDust", "UnstableDust", 64);
      RegItems.hairBand = (ItemHairBand)RegistryUtils.registerItem(new ItemHairBand(), "hairBand");
      // Регистрация кейсов
      RegItems.draconicCase = new ItemCase("DraconicCase", "draconic_case", 64, "draconic");
      RegItems.industrialCase = new ItemCase("IndustrialCase", "industrial_case", 64, "industrial");
      RegItems.vanillaCase = new ItemCase("VanillaCase", "vanilla_case", 64, "vanilla");
      RegItems.thaumcraftCase = new ItemCase("ThaumcraftCase", "thaumcraft_case", 64, "thaumcraft");
      RegItems.dakimakuraCase = new ItemCase("DakimakuraCase", "dakimakura_case", 64, "dakimakura");
   }
}
