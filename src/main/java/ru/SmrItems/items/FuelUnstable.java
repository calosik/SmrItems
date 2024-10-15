package ru.SmrItems.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.SmrItems.SmMain;
import ru.SmrItems.common.lib.LibRare;

public class FuelUnstable extends Item {
   public FuelUnstable(String name, String texture, int maxStackSize) {
      this.setUnlocalizedName(name);
      this.setTextureName("SmrItems:" + texture);
      this.setCreativeTab(SmMain.tabsmritems);
      this.maxStackSize = maxStackSize;
      GameRegistry.registerItem(this, name);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("Топливо для прогрузчика чанков");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_LEGENDARY;
   }
}
