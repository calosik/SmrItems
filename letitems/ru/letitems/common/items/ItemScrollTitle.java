package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.letitems.common.lib.LibRare;

public class ItemScrollTitle extends ItemBase {
   private static final String TAG_TITLE = "titles";
   private static final String TAG_COLOR = "color";
   private static final String TAG_TIME = "time";
   private static final String TAG_OWNER = "name";
   private static final String TAG_POSX = "posX";

   public ItemScrollTitle(String name) {
      super(name);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
      NBTTagCompound nbttagcompound = scrollInfo(stack);
      if (nbttagcompound.hasKey("titles") && !nbttagcompound.getString("titles").equals("")) {
         list.add("Имеет титул: " + nbttagcompound.getString("titles"));
         if (!nbttagcompound.getString("name").equals("")) {
            list.add("Титул игрока: " + nbttagcompound.getString("name"));
         }

         list.add("Отображается с" + (nbttagcompound.getBoolean("posX") ? "верх" : "низ") + "у ника.");
      } else {
         list.add("У свитка нет титула.");
      }

      list.add("§7Титул меняется в главном инвентаре");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_EPIC;
   }

   public static NBTTagCompound scrollInfo(ItemStack stack) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();
      if (nbttagcompound == null) {
         nbttagcompound = new NBTTagCompound();
         stack.setTagCompound(nbttagcompound);
         nbttagcompound.setString("titles", "");
         nbttagcompound.setString("color", "");
         nbttagcompound.setString("time", "");
         nbttagcompound.setString("name", "");
         nbttagcompound.setBoolean("posX", false);
      }

      return nbttagcompound;
   }

   public static String getTitle(ItemStack stack) {
      NBTTagCompound nbttagcompound = scrollInfo(stack);
      return nbttagcompound.hasKey("titles") ? nbttagcompound.getString("titles") : "";
   }

   public static void setTitle(ItemStack stack, String title) {
   }
}
