package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemSelect extends ItemBase {
   public ItemSelect(String name) {
      super(name);
      this.setMaxStackSize(1);
      this.setFull3D();
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_POOR;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§6Предназначен для выделения территории");
      list.add("Как убрать вертикальное расширение: /rg claim [название] -v");
      list.add("§3SHIFT§7+§3ПКМ§7 - убрать сетку привата");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
      if (world.isRemote && entityPlayer.isSneaking()) {
         Minecraft.getMinecraft().thePlayer.sendChatMessage("//sel");
      }

      return itemStack;
   }
}
