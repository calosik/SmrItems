package ru.SmrItems.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.SmrItems.SmMain;

public class SelectItem extends Item {
   public SelectItem(String name, String texture, int maxStackSize) {
      this.setUnlocalizedName(name);
      this.setTextureName("SmrItems:" + texture);
      this.setCreativeTab(SmMain.tabsmritems);
      this.maxStackSize = maxStackSize;
      GameRegistry.registerItem(this, name);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§6Предназначен для выделения территории");
      list.add("Как убрать вертикальное расширение: /rg claim [название] -v");
      list.add("§3SHIFTВ§7+§3ПКМВ§7 - убрать сетку привата");
   }

   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
      if (world.isRemote && entityPlayer.isSneaking()) {
         Minecraft.getMinecraft().thePlayer.sendChatMessage("//sel");
      }

      return itemStack;
   }
}
