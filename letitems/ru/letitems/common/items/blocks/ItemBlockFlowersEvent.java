package ru.letitems.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

public final class ItemBlockFlowersEvent extends ItemBlock {
   private final Block blockFlower;

   public ItemBlockFlowersEvent(Block block) {
      super(block);
      this.blockFlower = block;
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.blockFlower.getIcon(2, meta);
   }

   public int getMetadata(int meta) {
      return meta;
   }
}
