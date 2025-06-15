package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class ItemCosmetic extends ItemBase {
   public static final int SUBTYPES = 35;
   private IIcon[] icons;
   private static final int[] renderType = new int[]{0, 1, 3, 12, 13, 14, 16, 28, 29, 30};

   public ItemCosmetic(String name) {
      super(name);
      this.setHasSubtypes(true);
   }

   public void registerIcons(IIconRegister register) {
      this.icons = new IIcon[35];

      for(int i = 0; i < 35; ++i) {
         this.icons[i] = register.registerIcon("letitems:cosmetic/cosmetic" + i);
      }

   }

   public void getSubItems(Item item, CreativeTabs tab, List list) {
      for(int i = 0; i < 35; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   public IIcon getIconFromDamage(int dmg) {
      return this.icons[Math.min(34, dmg)];
   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName(stack) + stack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("§7Можно получить у куклы §4Сора");
      list.add("§7Тип украшения: §3" + this.onBaubleType(stack.getItemDamage()).getName());
   }

   public int hasRenderHead(int damage) {
      return Arrays.binarySearch(renderType, damage);
   }

   public void onPlayerBaubleRender(int damage, EntityPlayer player, boolean head) {
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
      if (head) {
         translateToHeadLevel(player);
         this.faceTranslate();
         switch(damage) {
         case 2:
            this.scale(0.75F);
            GL11.glTranslatef(0.04F, -0.5F, 0.0F);
            this.renderIcon(2);
         case 3:
         case 12:
         case 13:
         case 14:
         case 16:
         case 28:
         case 29:
         case 30:
         default:
            break;
         case 4:
            this.scale(0.75F);
            GL11.glTranslatef(0.04F, -0.5F, 0.0F);
            this.renderIcon(4);
            break;
         case 5:
            this.scale(0.35F);
            GL11.glTranslatef(0.3F, -0.5F, 0.0F);
            this.renderIcon(5);
            break;
         case 6:
         case 32:
         case 33:
            this.scale(0.5F);
            GL11.glTranslatef(0.3F, 0.7F, 0.5F);
            this.renderIcon(damage);
            break;
         case 7:
            this.scale(0.6F);
            GL11.glTranslatef(0.2F, 0.3F, 0.6F);
            this.renderIcon(7);
            break;
         case 8:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            this.scale(0.6F);
            GL11.glTranslatef(-0.9F, 0.0F, 0.2F);
            this.renderIcon(8);
            break;
         case 9:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            this.scale(0.6F);
            GL11.glTranslatef(-0.9F, -0.2F, 0.2F);
            this.renderIcon(9);
            GL11.glTranslatef(0.0F, 0.0F, 1.0F);
            this.renderIcon(9);
            break;
         case 10:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            this.scale(0.4F);
            GL11.glTranslatef(-0.5F, -0.1F, 0.3F);
            GL11.glRotatef(120.0F, 0.0F, 1.0F, 0.0F);
            this.renderIcon(10);
            GL11.glRotatef(-100.0F, 0.0F, 1.0F, 0.0F);
            this.renderIcon(10);
            break;
         case 11:
            this.scale(0.6F);
            GL11.glTranslatef(0.2F, -0.1F, 0.6F);
            this.renderIcon(11);
            break;
         case 15:
            GL11.glTranslatef(-0.1F, -0.55F, 0.0F);
            this.renderIcon(15);
            break;
         case 17:
            this.scale(0.35F);
            GL11.glTranslatef(0.3F, -0.6F, 0.0F);
            this.renderIcon(17);
            break;
         case 18:
            this.scale(0.75F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.3F, 0.1F, 0.55F);
            this.renderIcon(18);
            break;
         case 19:
            this.scale(0.6F);
            GL11.glTranslatef(0.17F, -0.2F, 0.1F);
            this.renderIcon(19);
            break;
         case 20:
            this.scale(0.25F);
            GL11.glTranslatef(0.4F, 0.5F, -0.1F);
            this.renderIcon(20);
            GL11.glTranslatef(1.4F, 0.0F, 0.0F);
            this.renderIcon(20);
            break;
         case 21:
            this.scale(0.25F);
            GL11.glTranslatef(1.55F, -0.2F, -0.1F);
            this.renderIcon(21);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.1F, 0.0F, 0.1F);
            this.renderIcon(21);
            break;
         case 22:
         case 31:
            this.scale(0.75F);
            GL11.glTranslatef(0.04F, -0.4F, 0.0F);
            this.renderIcon(damage);
            break;
         case 23:
            this.scale(0.75F);
            GL11.glTranslatef(0.04F, -0.4F, 0.0F);
            this.renderIcon(23);
            break;
         case 24:
            this.scale(0.6F);
            GL11.glTranslatef(0.5F, -0.02F, 0.1F);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            this.renderIcon(24);
            break;
         case 25:
            this.scale(0.75F);
            GL11.glTranslatef(0.04F, -0.5F, 0.0F);
            this.renderIcon(25);
            break;
         case 26:
            GL11.glTranslatef(-0.1F, -0.4F, 0.0F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
            this.renderIcon(26);
            break;
         case 27:
            this.scale(0.75F);
            GL11.glTranslatef(0.04F, -0.65F, 0.0F);
            this.renderIcon(27);
            break;
         case 34:
            this.scale(0.5F);
            GL11.glTranslatef(0.3F, -0.4F, 0.0F);
            this.renderIcon(34);
         }
      } else {
         rotateIfSneaking(player);
         this.chestTranslate();
         switch(damage) {
         case 0:
            this.scale(0.5F);
            GL11.glTranslatef(0.5F, 0.7F, 0.0F);
            this.renderIcon(0);
            break;
         case 1:
            this.scale(0.75F);
            GL11.glTranslatef(0.15F, -0.1F, 0.0F);
            this.renderIcon(1);
         case 2:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 15:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         default:
            break;
         case 3:
            this.scale(0.6F);
            GL11.glTranslatef(0.35F, 0.3F, 0.0F);
            this.renderIcon(3);
            break;
         case 12:
         case 28:
         case 29:
         case 30:
            this.scale(0.8F);
            GL11.glTranslatef(0.2F, -0.2F, -0.35F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            this.renderIcon(damage);
            break;
         case 13:
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.scale(0.5F);
            GL11.glTranslatef(-1.3F, -0.4F, -1.0F);
            this.renderIcon(13);
            break;
         case 14:
            this.scale(0.5F);
            GL11.glTranslatef(2.3F, 1.0F, -0.05F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.renderIcon(14);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glColor4f(0.0F, 0.0F, 0.3F, 1.0F);
            GL11.glTranslatef(-2.6F, 0.0F, 0.05F);
            this.renderIcon(14);
            break;
         case 16:
            this.scale(0.225F);
            GL11.glTranslatef(2.3F, 1.9F, 0.0F);
            this.renderIcon(16);
         }
      }

   }

   private void faceTranslate() {
      GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(-0.4F, 0.1F, -0.25F);
   }

   private void chestTranslate() {
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.7F, 0.15F);
   }

   private void scale(float f) {
      GL11.glScalef(f, f, f);
   }

   public void renderIcon(int i) {
      IIcon icon = this.icons[i];
      ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
   }

   private static void rotateIfSneaking(EntityPlayer player) {
      if (player.isSneaking()) {
         GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
      }

   }

   private static void translateToHeadLevel(EntityPlayer player) {
      GL11.glTranslated(0.0D, (double)((player != Minecraft.getMinecraft().thePlayer ? 1.68F : 0.0F) - player.getDefaultEyeHeight()) + (player.isSneaking() ? 0.0625D : 0.0D), 0.0D);
   }

   public ItemCosmetic.CosmeticType onBaubleType(int damage) {
      switch(damage) {
      case 0:
      case 1:
      case 3:
      case 14:
      case 16:
         return ItemCosmetic.CosmeticType.CHEST;
      case 2:
      case 4:
      case 5:
      case 15:
      case 17:
      case 21:
      case 22:
      case 23:
      case 25:
      case 26:
      case 27:
      case 31:
      case 34:
         return ItemCosmetic.CosmeticType.EYE;
      case 6:
      case 24:
      case 32:
      case 33:
         return ItemCosmetic.CosmeticType.HALO;
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 18:
      case 19:
      case 20:
         return ItemCosmetic.CosmeticType.HEAD;
      case 12:
      case 13:
      case 28:
      case 29:
      case 30:
         return ItemCosmetic.CosmeticType.BACK;
      default:
         return ItemCosmetic.CosmeticType.HEAD;
      }
   }

   public static enum CosmeticType {
      HEAD("Головной убор"),
      EYE("Маски"),
      CHEST("Грудь"),
      BACK("Спина"),
      HALO("Над головой");

      private final String text;

      private CosmeticType(String text) {
         this.text = text;
      }

      public String getName() {
         return this.text;
      }
   }
}
