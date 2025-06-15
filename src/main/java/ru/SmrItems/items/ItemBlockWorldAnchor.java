package ru.SmrItems.items;

import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.SmrItems.util.Utils;
import ru.SmrItems.util.enums.LoadingMode;

public class ItemBlockWorldAnchor extends ItemBlock {
   public ItemBlockWorldAnchor(Block block) {
      super(block);
   }

   public void addInformation(ItemStack itemStack, EntityPlayer player, List lore, boolean par4) {
      NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
      if (nbtTagCompound != null) {
         int chunkLoadingTime = nbtTagCompound.hasKey("chunkLoadingTime") ? nbtTagCompound.getInteger("chunkLoadingTime") : 0;
         LoadingMode loadingMode = LoadingMode.fromInteger(nbtTagCompound.hasKey("mode") ? nbtTagCompound.getShort("mode") : 0);
         if (chunkLoadingTime != 0) {
            lore.add(I18n.format("lore.workTime.text", new Object[0]) + " " + Utils.getChunkLoadingTime(chunkLoadingTime));
            lore.add(I18n.format("lore.mode.text", new Object[0]) + " " + Utils.getMode(((LoadingMode)Objects.requireNonNull(loadingMode)).ordinal()));
         }
      }

   }
}
