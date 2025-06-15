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
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

public final class ItemArtefe extends ItemBase {
   private static final int COUNT_SUB = 2;
   @SideOnly(Side.CLIENT)
   private IIcon[] icons;

   public ItemArtefe(String name) {
      super(name);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs tab, List items) {
      for(int i = 0; i < 2; ++i) {
         items.add(new ItemStack(item, 1, i));
      }

   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName(stack) + stack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icons[MathHelper.clamp_int(meta, 0, 1)];
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister register) {
      this.icons = new IIcon[2];
      this.icons[0] = register.registerIcon("letitems:ItemArtefe");
      this.icons[1] = register.registerIcon("letitems:ItemArtefeShard");
   }

   public EnumRarity getRarity(ItemStack stack) {
      return stack.getItemDamage() == 0 ? LibRare.RARITY_EPIC : LibRare.RARITY_RARE;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return stack;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      if (stack.getItemDamage() == 0) {
         list.add("§9Предназначен для §dпрокачивания уровня мастера§9 на сайте");
         list.add("§9Уровень мастера нужен для открытия §dновых талантов");
         list.add("§7Добываются из руды Артефор");
         list.add("§7Профиль -> Таланты");
         list.add("");
         list.add("§7Нажмите §3ПКМ§7 и кристаллы активируются");
      } else {
         list.add("Осколки из руды Артефора, можно добыть");
         list.add("при помощи навыков §dДобытчик Артефэ §7и §dРыболов");
         list.add("Отдайте 6 осколков кукле Кусугаки,");
         list.add("чтобы превратить их в §dКристаллы Артефэ");
      }

      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }
}
