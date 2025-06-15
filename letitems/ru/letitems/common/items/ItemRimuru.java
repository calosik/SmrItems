package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemRimuru extends ItemBase {
   private final String TAG_MAGLIMENT = "magliment";
   private final String TAG_COPYCOUNT = "dollcopies";
   private final String TAG_DAYRELODE = "dayreloade";
   private final String TAG_DOLLTYPE = "dolltype";

   public ItemRimuru(String name) {
      super(name);
      this.setMaxStackSize(1);
      this.setNoRepair();
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      NBTTagCompound nbttagcompound = this.rimuruMaglimentInfo(stack);
      list.add("§7Копирует куклу и добавляет её в инвентарь");
      list.add("§7 - Может выпасть в сундуке с куклами или скрафчен на сайте");
      list.add("§5Маглиментов: " + String.format("%.2f", this.rimuruMaglimentPercent(nbttagcompound.getInteger("magliment"), nbttagcompound.getInteger("dayreloade"))) + '%');
      list.add("§5Копирований: " + nbttagcompound.getInteger("dollcopies"));
      list.add("§5Последняя кукла: " + (nbttagcompound.getString("dolltype").isEmpty() ? "-" : StatCollector.translateToLocal("tile.Doll." + nbttagcompound.getString("dolltype") + ".name")));
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_LEGENDARY;
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      return false;
   }

   private NBTTagCompound rimuruMaglimentInfo(ItemStack stack) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();
      if (nbttagcompound == null) {
         nbttagcompound = new NBTTagCompound();
         stack.setTagCompound(nbttagcompound);
         nbttagcompound.setInteger("magliment", 0);
         nbttagcompound.setInteger("dollcopies", 0);
         nbttagcompound.setInteger("dayreloade", 0);
      }

      return nbttagcompound;
   }

   private double rimuruMaglimentPercent(int time, int day) {
      if (time != 0 && day != 0) {
         day *= 86400;
         double lastTime = (double)((int)(System.currentTimeMillis() / 1000L) - time) / (double)day * 100.0D;
         return Math.min(lastTime, 100.0D);
      } else {
         return 100.0D;
      }
   }
}
