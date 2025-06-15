package ru.letitems.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ru.letitems.common.LetItems;
import ru.letitems.common.block.BlockDakimakura;
import ru.letitems.common.dakimakura.Daki;
import ru.letitems.common.dakimakura.serialize.DakiNbtSerializer;

public class ItemBlockDakimakura extends ItemBlock {
   public ItemBlockDakimakura(Block block) {
      super(block);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(this));
      List<Daki> dakiList = LetItems.proxy.getDakimakuraManager().getDakiList();
      Iterator var5 = dakiList.iterator();

      while(var5.hasNext()) {
         Daki daki = (Daki)var5.next();
         list.add(new ItemStack(this, 1, getDakiMeta(daki)));
      }

   }

   public String getUnlocalizedName(ItemStack stack) {
      Daki daki = getDaki(stack);
      String dakiName = daki == null ? "" : '.' + daki.getDakiDirectoryName().toLowerCase();
      return super.getUnlocalizedName(stack) + dakiName;
   }

   public static int getDakiMeta(Daki daki) {
      return daki == null ? 0 : LetItems.proxy.getDakimakuraManager().getDakiGlobalIndex(daki) + 1;
   }

   public static Daki getDaki(ItemStack stack) {
      return stack == null ? null : LetItems.proxy.getDakimakuraManager().getDakiByGlobalIndex(stack.getItemDamage() - 1);
   }

   public static boolean isFlipped(ItemStack itemStack) {
      return itemStack != null && itemStack.hasTagCompound() ? DakiNbtSerializer.isFlipped(itemStack.getTagCompound()) : false;
   }

   public static ItemStack setFlipped(ItemStack itemStack, boolean flipped) {
      if (itemStack == null) {
         return null;
      } else {
         if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
         }

         DakiNbtSerializer.setFlipped(itemStack.getTagCompound(), flipped);
         return itemStack;
      }
   }

   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
      if (entityPlayer.isSneaking()) {
         boolean flipped = isFlipped(itemStack);
         itemStack = setFlipped(itemStack, !flipped);
      }

      return itemStack;
   }

   public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      Block block = world.getBlock(x, y, z);
      ForgeDirection sideDir = ForgeDirection.getOrientation(side);
      if ((block == Blocks.snow_layer && (world.getBlockMetadata(x, y, y) & 7) < 1) | block == Blocks.tallgrass) {
         sideDir = ForgeDirection.UP;
         side = 1;
      } else if (block != Blocks.vine && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
         x += sideDir.offsetX;
         y += sideDir.offsetY;
         z += sideDir.offsetZ;
      }

      if (itemStack.stackSize == 0) {
         return false;
      } else if (y == 255 && this.field_150939_a.getMaterial().isSolid()) {
         return false;
      } else if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, side, entityPlayer, itemStack)) {
         int rot = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
         ForgeDirection[] rots = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
         ForgeDirection rotation = rots[rot].getOpposite();
         if (this.canPlaceDakiAt(world, entityPlayer, itemStack, x, y, z, sideDir, rotation)) {
            if (block.isBed(world, x, y, z, entityPlayer) & side == 1) {
               this.placeAsEntity(world, itemStack, x, y, z, rotation);
            } else {
               this.placeDakiAt(world, entityPlayer, itemStack, x, y, z, sideDir, rotation);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void placeAsEntity(World world, ItemStack itemStack, int x, int y, int z, ForgeDirection rotation) {
   }

   private boolean canPlaceDakiAt(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side, ForgeDirection rotation) {
      if (this.canPlaceAtLocation(world, entityPlayer, itemStack, x, y, z, side)) {
         if (side != ForgeDirection.UP) {
            rotation = ForgeDirection.UP;
         }

         if (side == ForgeDirection.DOWN) {
            --y;
         }

         x += rotation.offsetX;
         y += rotation.offsetY;
         z += rotation.offsetZ;
         return this.canPlaceAtLocation(world, entityPlayer, itemStack, x, y, z, side);
      } else {
         return false;
      }
   }

   private boolean canPlaceAtLocation(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side) {
      Block block = world.getBlock(x, y, z);
      if (!entityPlayer.canPlayerEdit(x, y, z, side.ordinal(), itemStack)) {
         return false;
      } else {
         return y >= 255 && this.field_150939_a.getMaterial().isSolid() ? false : block.isReplaceable(world, x, y, z);
      }
   }

   private void placeDakiAt(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side, ForgeDirection rotation) {
      int meta = 0;
      int meta = BlockDakimakura.setRotationOnMeta(meta, rotation);
      meta = BlockDakimakura.setStandingOnMeta(meta, false);
      meta = BlockDakimakura.setTopPartOnMeta(meta, false);
      if (side != ForgeDirection.UP & side != ForgeDirection.DOWN) {
         meta = BlockDakimakura.setRotationOnMeta(meta, side.getOpposite());
         meta = BlockDakimakura.setStandingOnMeta(meta, true);
         rotation = ForgeDirection.UP;
      }

      if (side == ForgeDirection.DOWN) {
         meta = BlockDakimakura.setStandingOnMeta(meta, true);
         rotation = ForgeDirection.UP;
         --y;
      }

      this.placeBlockAt(itemStack, entityPlayer, world, x, y, z, side.ordinal(), 0.0F, 0.0F, 0.0F, meta);
      world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
      --itemStack.stackSize;
      world.markBlockForUpdate(x, y, z);
      meta = BlockDakimakura.setTopPartOnMeta(meta, true);
      x += rotation.offsetX;
      y += rotation.offsetY;
      z += rotation.offsetZ;
      this.placeBlockAt(itemStack, entityPlayer, world, x, y, z, side.ordinal(), 0.0F, 0.0F, 0.0F, meta);
      world.markBlockForUpdate(x, y, z);
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      if (!world.setBlock(x, y, z, this.field_150939_a, metadata, 2)) {
         return false;
      } else {
         if (world.getBlock(x, y, z) == this.field_150939_a) {
            this.field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            this.field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
         }

         return true;
      }
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips) {
      list.add("Нажмите §aПКМ + Shift§7 чтобы перевернуть.");
      int meta = itemStack.getItemDamage();
      if (meta >= 169 && meta <= 177) {
         list.add("");
         list.add("Дакимакура из набора Genshin Impact.");
         list.add("Можно получить только во время Кусь событий.");
      }

   }

   @SideOnly(Side.CLIENT)
   public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer entityPlayer, ItemStack itemStack) {
      Block block = world.getBlock(x, y, z);
      if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, y) & 7) < 1) {
         side = 0;
      } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
         ForgeDirection sideDir = ForgeDirection.getOrientation(side);
         x += sideDir.offsetX;
         y += sideDir.offsetY;
         z += sideDir.offsetZ;
      }

      return world.canPlaceEntityOnSide(Blocks.stone, x, y, z, false, side, (Entity)null, itemStack);
   }
}
