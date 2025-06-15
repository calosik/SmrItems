package ru.letitems.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public final class ItemBlockPlayerDetector extends ItemBlock {
   public ItemBlockPlayerDetector(Block block) {
      super(block);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.field_150939_a.getIcon(2, meta);
   }

   public int getMetadata(int meta) {
      return meta;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName(stack) + '.' + stack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("Механизм выдаёт редстоун сигнал.");
      if (stack.getItemDamage() == 0) {
         list.add("Срабатывает на любого игрока в радиусе 8 блоков.");
      } else {
         list.add("Срабатывает если владелец блока на сервере.");
         list.add("Владельцем считается установивший блок игрок.");
      }

      list.add("Нажмите §3SHIFT§7+§3ПКМ§7 для смены типа блока");
   }
}
