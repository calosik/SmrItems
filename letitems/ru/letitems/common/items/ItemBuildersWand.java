package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.letitems.common.inventory.MergedInventory;
import ru.letitems.common.lib.LibRare;
import ru.letitems.common.util.BlockPos;
import ru.letitems.common.util.GeneralUtils;

public final class ItemBuildersWand extends ItemBase {
   private static final String ITEM_NAME = "builderswand";
   public final ItemBuildersWand.BuildersWandType type;
   @SideOnly(Side.CLIENT)
   private IIcon[] icons;

   public ItemBuildersWand(ItemBuildersWand.BuildersWandType type) {
      super("builderswand" + type.getNameSuffix());
      this.type = type;
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack par1ItemStack, int pass) {
      return false;
   }

   public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean isFull3D() {
      return true;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.none;
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if (world.isRemote) {
         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(x, y, z, side, stack, hitX, hitY, hitZ));
      }

      return true;
   }

   public boolean onItemUse(ItemStack wand, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      return true;
   }

   public List<BlockPos> getPotentialBlocks(EntityPlayer player, World world, int x, int y, int z, int face, MergedInventory mergedInventory) {
      if (world == null) {
         return Collections.emptyList();
      } else {
         Block block = world.getBlock(x, y, z);
         if (world.isAirBlock(x, y, z)) {
            return Collections.emptyList();
         } else {
            if (player != null && !GeneralUtils.isFakePlayer(player)) {
               Item item = Item.getItemFromBlock(block);
               if (item == null) {
                  return Collections.emptyList();
               }

               int meta = item.getHasSubtypes() ? block.getDamageValue(world, x, y, z) : -1;
               int numBlocks = 0;
               ItemStack genericStack = null;
               Iterator var13 = mergedInventory.getInventories().iterator();

               while(true) {
                  int xRange;
                  while(var13.hasNext()) {
                     MergedInventory.SubInventory subInventory = (MergedInventory.SubInventory)var13.next();
                     IInventory inventory = subInventory.inventory;

                     for(xRange = 0; xRange < inventory.getSizeInventory(); ++xRange) {
                        ItemStack stack = inventory.getStackInSlot(xRange);
                        if (stack != null && stack.getItem() == item && (meta == -1 || meta == stack.getItemDamage())) {
                           genericStack = stack;
                           if (player.capabilities.isCreativeMode) {
                              numBlocks = this.type.getMaxBlocks();
                              break;
                           }

                           numBlocks += stack.stackSize;
                           if (numBlocks >= this.type.getMaxBlocks()) {
                              numBlocks = this.type.getMaxBlocks();
                              break;
                           }
                        }
                     }
                  }

                  int dx = Facing.offsetsXForSide[face];
                  int dy = Facing.offsetsYForSide[face];
                  int dz = Facing.offsetsZForSide[face];
                  xRange = dx == 0 ? 1 : 0;
                  int yRange = dy == 0 ? 1 : 0;
                  int zRange = dz == 0 ? 1 : 0;
                  if (player.isSneaking()) {
                     if (face <= 1) {
                        return Collections.emptyList();
                     }

                     yRange = 0;
                  }

                  if (numBlocks > 0 && y + dy < 255 && block.canPlaceBlockOnSide(world, x + dx, y + dy, z + dz, face)) {
                     AxisAlignedBB aabb = block.getCollisionBoundingBoxFromPool(world, x, y, z);
                     if (checkAAB(world, aabb, dx, dy, dz)) {
                        List<BlockPos> blocks = new ArrayList();
                        blocks.add(new BlockPos(x + dx, y + dy, z + dz));

                        for(int i = 0; i < blocks.size() && blocks.size() < numBlocks; ++i) {
                           BlockPos origin = (BlockPos)blocks.get(i);

                           for(int xOffset = -xRange; xOffset <= xRange; ++xOffset) {
                              for(int yOffset = -yRange; yOffset <= yRange; ++yOffset) {
                                 for(int zOffset = -zRange; zOffset <= zRange; ++zOffset) {
                                    if (blocks.size() >= numBlocks) {
                                       return blocks;
                                    }

                                    BlockPos temp = origin.offset(xOffset, yOffset, zOffset);
                                    if (player.canPlayerEdit(temp.x, temp.y, temp.z, face, genericStack) && !blocks.contains(temp) && world.getBlock(temp.x - dx, temp.y - dy, temp.z - dz) == block && block.canPlaceBlockOnSide(world, temp.x, temp.y, temp.z, face) && (meta == -1 || meta == block.getDamageValue(world, temp.x - dx, temp.y - dy, temp.z - dz)) && checkAAB(world, aabb, temp.x - x, temp.y - y, temp.z - z) && !blocks.contains(temp)) {
                                       blocks.add(temp);
                                    }
                                 }
                              }
                           }
                        }

                        return blocks;
                     }
                  }
                  break;
               }
            }

            return Collections.emptyList();
         }
      }
   }

   private static boolean checkAAB(World world, AxisAlignedBB bounds, int dx, int dy, int dz) {
      return bounds == null || world.checkNoEntityCollision(bounds.getOffsetBoundingBox((double)dx, (double)dy, (double)dz));
   }

   public EnumRarity getRarity(ItemStack stack) {
      return LibRare.RARITY_ARTIFACT;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add(StatCollector.translateToLocal("tooltip.letitems.wandkysy.0" + this.type.getNameSuffix()));
      if (this.type.getArtefeUpgrade() > 0) {
         list.add("§7Используйте Куклу Кусугаки для улучшения жезла");
      }

      list.add(StatCollector.translateToLocal("tooltip.letitems.wandkysy.1" + this.type.getNameSuffix()));
      list.add(LibRare.rareBuild(this.getRarity(stack)));
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      String iconString = this.getIconString();
      this.icons = new IIcon[2];
      this.itemIcon = this.icons[0] = iconRegister.registerIcon(iconString);
      this.icons[1] = iconRegister.registerIcon(iconString + '1');
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconForTransparentRender(ItemStack stack, int pass) {
      return this.icons[pass];
   }

   public static enum BuildersWandType {
      COMMON(9, "", 320),
      RARE(49, ".lvl2", 720),
      VERY_RARE(100, ".lvl3", 0),
      VKREPOST(49, ".vk_repost", 0),
      SEASON3(64, ".season3", 0);

      private final int maxBlocks;
      private final String nameSuffix;
      private final int artefeUpgrade;

      private BuildersWandType(int maxBlocks, String nameSuffix, int artefeUpgrade) {
         this.maxBlocks = maxBlocks;
         this.nameSuffix = nameSuffix;
         this.artefeUpgrade = artefeUpgrade;
      }

      public int getMaxBlocks() {
         return this.maxBlocks;
      }

      public String getNameSuffix() {
         return this.nameSuffix;
      }

      public int getArtefeUpgrade() {
         return this.artefeUpgrade;
      }
   }
}
