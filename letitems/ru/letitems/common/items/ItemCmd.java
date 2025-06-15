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
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public class ItemCmd extends ItemBase {
   private final String[] commands = new String[]{"/home", "/rtp", "/spawn"};
   @SideOnly(Side.CLIENT)
   private IIcon[] icons;

   public ItemCmd(String name) {
      super(name);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (player.isSneaking()) {
         return stack;
      } else {
         if (world.isRemote) {
            int meta = MathHelper.clamp_int(stack.getItemDamage(), 0, this.commands.length - 1);
            String command = this.commands[meta];
            Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
         }

         return stack;
      }
   }

   public String getUnlocalizedName(ItemStack stack) {
      int i = MathHelper.clamp_int(stack.getItemDamage(), 0, this.commands.length - 1);
      return super.getUnlocalizedName(stack) + '_' + i;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs tab, List items) {
      for(int i = 0; i < this.commands.length; ++i) {
         items.add(new ItemStack(item, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      int i = MathHelper.clamp_int(meta, 0, this.commands.length - 1);
      return this.icons[i];
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister register) {
      this.icons = new IIcon[this.commands.length];

      for(int i = 0; i < this.commands.length; ++i) {
         this.icons[i] = register.registerIcon(this.getIconString() + '_' + i);
      }

   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§7Нажмите §3SHIFT§7+§3ПКМ§7, чтобы сменить команду свитка");
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_POOR;
   }
}
