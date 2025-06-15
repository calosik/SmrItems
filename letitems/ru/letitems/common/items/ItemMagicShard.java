package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import ru.letitems.common.lib.LibRare;

public class ItemMagicShard extends ItemBase {
   public ItemMagicShard(String name) {
      super(name);
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_LEGENDARY;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§8Можно добыть в различных наградах");
      list.add("§8или создать на сайте §6letragon.ru/crafting/magicshard/");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }
}
