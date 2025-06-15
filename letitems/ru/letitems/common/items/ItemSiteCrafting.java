package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public final class ItemSiteCrafting extends ItemBase {
   private static final int COUNT_SUB = 6;
   @SideOnly(Side.CLIENT)
   private IIcon[] icons;

   public ItemSiteCrafting(String name) {
      super(name);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs tab, List items) {
      for(int i = 0; i < 6; ++i) {
         items.add(new ItemStack(item, 1, i));
      }

   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName(stack) + stack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icons[MathHelper.clamp_int(meta, 0, 5)];
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister register) {
      this.icons = new IIcon[6];

      for(int i = 0; i < 6; ++i) {
         this.icons[i] = register.registerIcon("letitems:site_crafting_" + i);
      }

   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b) {
      int i = itemStack.getItemDamage();
      list.add(StatCollector.translateToLocal("tooltip.letitems.sc." + i));
      if (i == 5) {
         list.add("Создаётся в крафте на сайте");
      } else {
         if (i == 4) {
            list.add("Предмет для работы персонажа на сайте §6letragon.ru/charwork");
            list.add("§7Нажмите §3SHIFT§7+§3ПКМ§7, для открытия припасов");
         } else {
            list.add("Предмет для крафта вещей на сайте, отправляются командой /ccr");
            list.add("");
            list.add("§7Нажмите §3SHIFT§7+§3ПКМ§7, для отправки всех предметов");
         }

         list.add("§7Нажмите §3ПКМ§7, для отправки предмета");
         list.add(LibRare.rareBuild(this.getRarity(itemStack)));
      }
   }

   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
      if (itemStack.getItemDamage() == 5) {
         return itemStack;
      } else {
         if (entityPlayer.isSneaking()) {
            this.command(world, "/ccr all");
         } else {
            this.command(world, "/ccr");
         }

         return itemStack;
      }
   }

   private void command(World world, String cmd) {
      if (world.isRemote) {
         Minecraft.getMinecraft().thePlayer.sendChatMessage(cmd);
      }

   }

   public EnumRarity getRarity(ItemStack stack) {
      switch(stack.getItemDamage()) {
      case 0:
         return LibRare.RARITY_LEGENDARY;
      case 1:
         return LibRare.RARITY_ARTIFACT;
      case 2:
      case 4:
         return LibRare.RARITY_POOR;
      case 3:
         return LibRare.RARITY_COMMON;
      default:
         return LibRare.RARITY_POOR;
      }
   }
}
