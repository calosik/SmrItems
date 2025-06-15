package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemMikuCover extends ItemBase {
   public ItemMikuCover(String name) {
      super(name);
      this.setMaxStackSize(1);
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_LEGENDARY;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§3Позволяет открыть набор обложек и персонажей с Miku Hatsune");
      list.add("§7Выпадает в сундуке с косичками");
      list.add("");
      list.add("§7Нажмите §3ПКМ§7, для активации набора");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return stack;
   }
}
