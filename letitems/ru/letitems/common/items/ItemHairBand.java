package ru.letitems.common.items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import ru.letitems.client.model.ModelHairBand;
import ru.letitems.common.LetItems;
import ru.letitems.common.lib.LibRare;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;

@InterfaceList({@Interface(
   iface = "thaumcraft.api.IVisDiscountGear",
   modid = "Thaumcraft"
)})
public class ItemHairBand extends ItemArmor implements IVisDiscountGear {
   public static final String NBT_THAUMCRAFT = "tcVis";

   public ItemHairBand() {
      super(ArmorMaterial.DIAMOND, 0, 0);
      this.setCreativeTab(LetItems.tabLetItems);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setNoRepair();
   }

   public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
      return false;
   }

   private static int getTypeOffset(ItemStack stack) {
      return getTypeOffset(stack == null ? 0 : stack.getItemDamage());
   }

   private static int getTypeOffset(int meta) {
      return meta < ItemHairBand.HairBandType.values().length ? meta : 0;
   }

   public static ItemHairBand.HairBandType getType(ItemStack stack) {
      return ItemHairBand.HairBandType.values()[getTypeOffset(stack)];
   }

   private static ItemHairBand.HairBandType getType(int meta) {
      return ItemHairBand.HairBandType.values()[getTypeOffset(meta)];
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase base, ItemStack itemStack, int armorSlot) {
      return getType(itemStack).getModel();
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      ItemHairBand.HairBandType[] var2 = ItemHairBand.HairBandType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemHairBand.HairBandType fancytailsType = var2[var4];
         fancytailsType.registerIcon(ir);
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return getType(meta).getIcon();
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      ItemHairBand.HairBandType type = getType(itemStack);
      return "item." + type.ordinal();
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs creativeTabs, List itemList) {
      ItemHairBand.HairBandType[] var4 = ItemHairBand.HairBandType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemHairBand.HairBandType fancytailsType = var4[var6];
         itemList.add(new ItemStack(item, 1, fancytailsType.ordinal()));
      }

   }

   public EnumRarity getRarity(ItemStack stack) {
      return getType(stack).getRare();
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      if (LetItems.loadThaumCraft && this.getVis(stack) > 0) {
         list.add("§5Уменьшение затрат Вис: " + this.getVisDiscount(stack, player, (Aspect)null) + "%");
      }

      list.add("§7Можно получить из сундука §4Hairstyles Tails");
      if (getType(stack).getParts() != 0) {
         list.add("§7Косичка имеет дополнительные вариации");
         list.add("");
      }

      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   @Method(
      modid = "Thaumcraft"
   )
   public int getVisDiscount(ItemStack itemStack, EntityPlayer entityPlayer, Aspect aspect) {
      return this.getVis(itemStack);
   }

   private int getVis(ItemStack stack) {
      return stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("tcVis") ? MathHelper.clamp_int(stack.getTagCompound().getInteger("tcVis"), 0, 5) : 0;
   }

   public static enum HairBandType {
      WHITE(LibRare.RARITY_POOR),
      RED(LibRare.RARITY_RARE, 1, 0),
      BLUE(LibRare.RARITY_RARE),
      YELLOW(LibRare.RARITY_COMMON),
      CYAN1(LibRare.RARITY_EPIC),
      PINK(LibRare.RARITY_RARE),
      CYAN2(LibRare.RARITY_LEGENDARY),
      BLACK(LibRare.RARITY_EPIC),
      BROWN(LibRare.RARITY_COMMON),
      VOICE(LibRare.RARITY_HEROIC, 4, 0),
      CYAN3(LibRare.RARITY_RARE),
      CYAN4(LibRare.RARITY_EPIC),
      RKN(LibRare.RARITY_RARE, 1, 1),
      AZUSA(LibRare.RARITY_RARE),
      ENJU(LibRare.RARITY_LEGENDARY, 2, 0),
      MINE(LibRare.RARITY_RARE),
      MOMOKA(LibRare.RARITY_COMMON),
      NIKO(LibRare.RARITY_RARE, 3, 1),
      RENGE(LibRare.RARITY_COMMON, 2, 0),
      CERBER(LibRare.RARITY_EPIC),
      ALTAIR(LibRare.RARITY_HEROIC, 1, 0),
      CLAIRE(LibRare.RARITY_RARE),
      KYKO(LibRare.RARITY_EPIC, 4, 0),
      ERINA(LibRare.RARITY_COMMON),
      ERIRI(LibRare.RARITY_COMMON),
      KURUMIEB(LibRare.RARITY_COMMON, 3, 0),
      KYSY(LibRare.RARITY_COMMON, 2, 2),
      LENALEE(LibRare.RARITY_RARE, 1, 1),
      MARYA(LibRare.RARITY_COMMON),
      MATSURIKA(LibRare.RARITY_EPIC, 3, 0),
      MEGUMIN(LibRare.RARITY_HEROIC, 2, 2),
      MSYU(LibRare.RARITY_RARE),
      SAKURAKIRISHIMA(LibRare.RARITY_EPIC),
      ZEROTWO(LibRare.RARITY_RARE, 1, 2),
      ALISA(LibRare.RARITY_RARE),
      AQUA(LibRare.RARITY_RARE),
      CELTY(LibRare.RARITY_LEGENDARY, 4, 2),
      KANADE(LibRare.RARITY_RARE),
      KARENSAO(LibRare.RARITY_EPIC, 2, 2),
      LINGYIN(LibRare.RARITY_EPIC, 2, 0),
      TSUKUYO(LibRare.RARITY_RARE, 1, 0);

      private EnumRarity rare;
      private int parts;
      private byte size;
      @SideOnly(Side.CLIENT)
      private ModelHairBand model;
      @SideOnly(Side.CLIENT)
      private IIcon icon;

      private HairBandType(EnumRarity rare) {
         this(rare, 0, 0);
      }

      private HairBandType(EnumRarity rare, int parts, int size) {
         this.rare = rare;
         this.parts = parts;
         this.size = (byte)size;
         if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            this.model = new ModelHairBand(this.ordinal(), parts);
         }

      }

      public EnumRarity getRare() {
         return this.rare;
      }

      public int getParts() {
         return this.parts;
      }

      public byte getSize() {
         return this.size;
      }

      @SideOnly(Side.CLIENT)
      public ModelBiped getModel() {
         return this.model;
      }

      @SideOnly(Side.CLIENT)
      public void registerIcon(IIconRegister iconRegister) {
         this.icon = iconRegister.registerIcon("letitems:hair/" + this.ordinal());
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIcon() {
         return this.icon;
      }
   }
}
