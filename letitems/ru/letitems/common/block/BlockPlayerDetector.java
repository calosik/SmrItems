package ru.letitems.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.letitems.common.items.blocks.ItemBlockPlayerDetector;
import ru.letitems.common.tile.TilePlayerDetector;
import ru.letitems.common.util.registry.IHasItemBlock;

public final class BlockPlayerDetector extends BlockBaseContainer<TilePlayerDetector> implements IHasItemBlock {
   private static final int SUBCOUNT = 2;
   @SideOnly(Side.CLIENT)
   private IIcon icon_head;
   @SideOnly(Side.CLIENT)
   private IIcon icon_side;
   @SideOnly(Side.CLIENT)
   private IIcon icon_side_active;

   public BlockPlayerDetector() {
      super(Material.ground, "player_detector");
      this.setHardness(0.25F);
      this.setStepSound(soundTypeStone);
   }

   public int damageDropped(int meta) {
      return meta;
   }

   public Class<? extends ItemBlock> getItemBlockClass() {
      return ItemBlockPlayerDetector.class;
   }

   public Class<TilePlayerDetector> getTileEntityClass() {
      return TilePlayerDetector.class;
   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return this.hasTileEntity(meta) ? new TilePlayerDetector() : null;
   }

   public boolean hasTileEntity(int meta) {
      return meta >= 0 && meta < 2;
   }

   @SideOnly(Side.CLIENT)
   public boolean isBlockNormalCube() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icon_head = iconRegister.registerIcon("letitems:player_detector.head");
      this.icon_side = iconRegister.registerIcon("letitems:player_detector.side");
      this.icon_side_active = iconRegister.registerIcon("letitems:player_detector.side.active");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      if (side == 1) {
         return this.icon_head;
      } else {
         TileEntity tile = world.getTileEntity(x, y, z);
         return tile instanceof TilePlayerDetector && ((TilePlayerDetector)tile).isActive() ? this.icon_side_active : this.icon_side;
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side == 1 ? this.icon_head : this.icon_side;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List stackList) {
      for(int i = 0; i < 2; ++i) {
         stackList.add(new ItemStack(item, 1, i));
      }

   }

   public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
      return side != 0 && side != 1;
   }

   public boolean canProvidePower() {
      return true;
   }

   public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int meta) {
      TileEntity te = world.getTileEntity(x, y, z);
      return te instanceof TilePlayerDetector && ((TilePlayerDetector)te).isActive() ? 15 : 0;
   }

   public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int meta) {
      TileEntity te = world.getTileEntity(x, y, z);
      return te instanceof TilePlayerDetector && ((TilePlayerDetector)te).isActive() ? 15 : 0;
   }
}
