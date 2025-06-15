package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemRussianRoulette extends ItemBase {
   private static final String[] PISTOLS = new String[]{"", "Gold"};
   @SideOnly(Side.CLIENT)
   private IIcon[][] icons;
   private static final String TAG_OWNERPISTOL = "ownerpistol";
   private static final String TAG_OWNERWIN = "ownerwin";
   private static final String TAG_LASTPLAYER = "lastplayer";
   private static final String TAG_LASTDEAD = "lastdead";
   private static final String TAG_BULLETID = "bulletid";
   private static final String TAG_SHOTSID = "shotsid";
   private static final String TAG_ROTATE = "idrotate";
   private static final String TAG_SERIAL = "serialid";

   public ItemRussianRoulette(String name) {
      super(name);
      this.setMaxStackSize(1);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(item, 1, 0));
      list.add(new ItemStack(item, 1, 1));
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      NBTTagCompound nbttagcompound = this.pistolInfoNBT(stack);
      list.add("Сыграйте с друзьями в веселую игру");
      list.add("§3SHIFT§7+§3ПКМ§7 - зарядить пистолет, §3ПКМ§7 - испытать себя");
      list.add("Вы умрёте, если проиграете");
      list.add("§7Владелец: " + this.infoTooltips(nbttagcompound.getString("ownerpistol")));
      list.add("§7Побед: " + nbttagcompound.getInteger("ownerwin"));
      list.add("§7Последний умерший: " + this.infoTooltips(nbttagcompound.getString("lastdead")));
      list.add("§7Серийный номер: " + this.infoTooltips(nbttagcompound.getString("serialid")));
      int itemDamage = stack.getItemDamage();
      String getText = "§7Получение: ";
      if (itemDamage == 0) {
         getText = getText + "крафт";
      } else {
         getText = getText + "награда 4-го сезона";
      }

      list.add(getText);
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   private String infoTooltips(String nbt) {
      return !nbt.equals("") ? nbt : "-";
   }

   public EnumRarity getRarity(ItemStack stack) {
      return stack.getItemDamage() == 0 ? LibRare.RARITY_COMMON : LibRare.RARITY_LEGENDARY;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[PISTOLS.length][2];

      for(int i = 0; i < PISTOLS.length; ++i) {
         this.icons[i][0] = iconRegister.registerIcon("letitems:ItemRussianRoulette" + PISTOLS[i]);
         this.icons[i][1] = iconRegister.registerIcon("letitems:ItemRussianRouletteBar" + PISTOLS[i]);
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      int i = MathHelper.clamp_int(meta, 0, PISTOLS.length - 1);
      return this.icons[i][0];
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(ItemStack itemStack, int pass) {
      return this.icons[itemStack.getItemDamage()][this.idRotate(itemStack)];
   }

   private int idRotate(ItemStack itemStack) {
      NBTTagCompound pistol = this.pistolInfoNBT(itemStack);
      int idRo = pistol.getInteger("idrotate");
      if (idRo > 0) {
         pistol.setInteger("idrotate", idRo - 1);
      }

      return idRo != 0 ? 1 : 0;
   }

   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
      return itemStack;
   }

   private NBTTagCompound pistolInfoNBT(ItemStack stack) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();
      if (nbttagcompound == null) {
         nbttagcompound = new NBTTagCompound();
         stack.setTagCompound(nbttagcompound);
         nbttagcompound.setString("ownerpistol", "");
         nbttagcompound.setInteger("ownerwin", 0);
         nbttagcompound.setString("lastplayer", "");
         nbttagcompound.setString("lastdead", "");
         nbttagcompound.setInteger("bulletid", 0);
         nbttagcompound.setInteger("shotsid", 0);
         nbttagcompound.setInteger("idrotate", 0);
         nbttagcompound.setString("serialid", "");
      }

      return nbttagcompound;
   }
}
