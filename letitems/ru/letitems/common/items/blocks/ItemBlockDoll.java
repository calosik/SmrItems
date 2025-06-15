package ru.letitems.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import ru.letitems.common.block.BlockDoll;

public final class ItemBlockDoll extends ItemBlock {
   public ItemBlockDoll(Block block) {
      super(block);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int meta) {
      return meta;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName(stack) + '.' + BlockDoll.DollType.getTypeFromStack(stack).getItemName();
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      BlockDoll.DollType dollType = BlockDoll.DollType.getTypeFromStack(stack);
      String animeOnItemDoll = dollType.getAnimeOnItemDoll();
      if (!animeOnItemDoll.equals("")) {
         list.add("Сериал - " + animeOnItemDoll);
      }

      if (dollType.getLoreOnItemDoll()) {
         list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip.doll.lore." + dollType.getItemName()));
      }

   }
}
