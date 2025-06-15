package ru.letitems.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockVendingMachine extends ItemBlock {
   public ItemBlockVendingMachine(Block block) {
      super(block);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int meta) {
      return meta;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack s, EntityPlayer p, List l, boolean b) {
      l.add("§7Аппараты можно приобрести в магазине блоков или скрафтить");
      l.add("§7Позволяют продавать предметы другим игрокам путём бартера");
   }
}
