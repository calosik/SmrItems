package ru.letitems.common.lib;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;

public final class LibRare {
   public static final EnumRarity RARITY_POOR;
   public static final EnumRarity RARITY_COMMON;
   public static final EnumRarity RARITY_RARE;
   public static final EnumRarity RARITY_EPIC;
   public static final EnumRarity RARITY_LEGENDARY;
   public static final EnumRarity RARITY_HEROIC;
   public static final EnumRarity RARITY_ARTIFACT;

   public static String rareBuild(EnumRarity rared) {
      return "Редкость: " + rared.rarityColor + rared.rarityName;
   }

   static {
      RARITY_POOR = EnumHelper.addRarity("RARITY_POOR", EnumChatFormatting.GRAY, "Обычный");
      RARITY_COMMON = EnumHelper.addRarity("RARITY_COMMON", EnumChatFormatting.AQUA, "Необычный");
      RARITY_RARE = EnumHelper.addRarity("RARITY_RARE", EnumChatFormatting.DARK_GREEN, "Редкий");
      RARITY_EPIC = EnumHelper.addRarity("RARITY_EPIC", EnumChatFormatting.LIGHT_PURPLE, "Эпический");
      RARITY_LEGENDARY = EnumHelper.addRarity("RARITY_LEGENDARY", EnumChatFormatting.GOLD, "Легендарный");
      RARITY_HEROIC = EnumHelper.addRarity("RARITY_HEROIC", EnumChatFormatting.YELLOW, "Героический");
      RARITY_ARTIFACT = EnumHelper.addRarity("RARITY_ARTIFACT", EnumChatFormatting.RED, "Артефакт");
   }
}
