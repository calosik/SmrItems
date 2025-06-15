package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemZero extends ItemBase {
   public ItemZero(String name) {
      super(name);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§9Данный предмет предназначен для открытия §dнабора персонажа Zero§9 на сайте");
      list.add("§9Вы можете добыть его 2-мя способами - §dотвечать на викторины §9или §dпросто играя");
      list.add("");
      list.add("§7Нажмите §3ПКМ§7 и книга активируется");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_HEROIC;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return stack;
   }
}
