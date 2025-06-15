package ru.letitems.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import ru.letitems.common.block.BlockAnchor;

public final class ItemBlockAnchor extends ItemBlock {
   public ItemBlockAnchor(Block block) {
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
      return super.getUnlocalizedName(stack) + '.' + BlockAnchor.AnchorType.getType(stack.getItemDamage()).getLowerCaseName();
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("Механизм для загрузки чанков. Область работы - 3х3.");
      list.add(StatCollector.translateToLocal("tooltip.letitems.anchor." + stack.getItemDamage()));
   }
}
