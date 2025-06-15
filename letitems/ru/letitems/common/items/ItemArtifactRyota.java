package ru.letitems.common.items;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import java.util.Collections;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.letitems.common.lib.LibRare;

@InterfaceList({@Interface(
   iface = "ic2.api.item.IElectricItem",
   modid = "IC2"
), @Interface(
   iface = "ic2.api.item.IBoxable",
   modid = "IC2"
), @Interface(
   iface = "ic2.api.item.IItemHudInfo",
   modid = "IC2"
)})
public final class ItemArtifactRyota extends ItemBase implements IElectricItem, IBoxable, IItemHudInfo {
   private static final int[] RANGE = new int[]{4, 6, 8, 11};
   private static final int[] ENERGY_PER_ITEM = new int[]{5, 8, 10, 15};
   private static final String NBT_ACTIVE = "Active";
   private static final String NBT_TIER = "Tier";

   public ItemArtifactRyota() {
      super("ArtifactRyota");
      this.setMaxStackSize(1);
      this.setMaxDamage(27);
      this.setNoRepair();
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return stack;
   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean inHand) {
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_ARTIFACT;
   }

   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack stack) {
      return isActive(stack);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
      ItemStack charged = new ItemStack(this);
      ElectricItem.manager.charge(charged, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
      list.add(charged);
      ItemStack uncharged = new ItemStack(this);
      ElectricItem.manager.charge(uncharged, 0.0D, Integer.MAX_VALUE, true, false);
      list.add(uncharged);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§6При активации начинает собирать предметы вокруг Вас");
      list.add("§7Нажмите §3SHIFT§7+§3ПКМ§7, для смены радиуса работы");
      list.add("");
      list.add("§8Радиус: " + isRange(stack));
      list.add("§8EU за предмет: " + isEnergy(stack));
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   @Method(
      modid = "IC2"
   )
   public boolean canBeStoredInToolbox(ItemStack stack) {
      return true;
   }

   @Method(
      modid = "IC2"
   )
   public List<String> getHudInfo(ItemStack itemStack) {
      return Lists.newArrayList(new String[]{ElectricItem.manager.getToolTip(itemStack)});
   }

   @Method(
      modid = "IC2"
   )
   public boolean canProvideEnergy(ItemStack stack) {
      return false;
   }

   @Method(
      modid = "IC2"
   )
   public Item getChargedItem(ItemStack stack) {
      return this;
   }

   @Method(
      modid = "IC2"
   )
   public Item getEmptyItem(ItemStack stack) {
      return this;
   }

   @Method(
      modid = "IC2"
   )
   public double getMaxCharge(ItemStack stack) {
      return 100000.0D;
   }

   @Method(
      modid = "IC2"
   )
   public int getTier(ItemStack stack) {
      return 3;
   }

   @Method(
      modid = "IC2"
   )
   public double getTransferLimit(ItemStack stack) {
      return 512.0D;
   }

   private static boolean isActive(ItemStack stack) {
      return stack != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean("Active");
   }

   private static int isRange(ItemStack stack) {
      return stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("Tier") ? RANGE[stack.getTagCompound().getInteger("Tier")] : RANGE[0];
   }

   private static int isEnergy(ItemStack stack) {
      return stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("Tier") ? ENERGY_PER_ITEM[stack.getTagCompound().getInteger("Tier")] : ENERGY_PER_ITEM[0];
   }

   private static void changeActive(ItemStack stack) {
   }

   private static <T extends Entity> List<T> getEntitites(World world, double x, double y, double z, Class<T> clazz, ItemStack stack) {
      return Collections.emptyList();
   }
}
