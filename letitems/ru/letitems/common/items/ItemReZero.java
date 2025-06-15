package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemReZero extends ItemBase {
   public ItemReZero(String name) {
      super(name);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§9Данный предмет предназначен для открытия §dнабора Re:Zero§9 на сайте");
      list.add("§9Можно добыть в качестве подарка активности на сервере");
      list.add("");
      list.add("§7Нажмите §3ПКМ§7, для активации");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_HEROIC;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return stack;
   }
}
