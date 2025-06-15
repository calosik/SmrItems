package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import ru.letitems.common.lib.LibRare;

public final class ItemArtefeUp extends ItemBase {
   private static final String ITEM_NAME = "artefeup";

   public ItemArtefeUp(int id) {
      super("artefeup" + id);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("Предназначено для использования в качестве топлива для лоадеров");
      list.add("§8Можно добыть в различных наградах");
      list.add("§8или создать на сайте §6letragon.ru/crafting/kys/");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_RARE;
   }
}
