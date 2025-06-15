package ru.letitems.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.letitems.client.renderer.commercial.RenderConstants;
import ru.letitems.common.items.blocks.ItemBlockVendingMachine;
import ru.letitems.common.tile.TileVendingMachine;
import ru.letitems.common.util.registry.IHasItemBlock;
import ru.letitems.common.util.registry.IHasNamedTileEntity;

public class BlockVendingMachine extends BlockBaseContainer<TileVendingMachine> implements IHasNamedTileEntity<TileVendingMachine>, IHasItemBlock {
   @SideOnly(Side.CLIENT)
   private IIcon iconTop;
   @SideOnly(Side.CLIENT)
   private IIcon iconSide;

   public BlockVendingMachine() {
      super(Material.glass, "vendingMachine");
      this.setStepSound(Block.soundTypeGlass);
      this.setHardness(0.15F);
      this.setBlockUnbreakable();
      this.setBlockBounds(0.0625F, 0.125F, 0.0625F, 0.9375F, 0.9375F, 0.9375F);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 0;
   }

   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      return tileEntity instanceof TileVendingMachine && ((TileVendingMachine)tileEntity).isEmpty() ? 16711756 : super.colorMultiplier(world, x, y, z);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side < 2 ? this.iconTop : this.iconSide;
   }

   public Class<TileVendingMachine> getTileEntityClass() {
      return TileVendingMachine.class;
   }

   public String getTileEntityName() {
      return "containerVendingMachine";
   }

   public Class<? extends ItemBlock> getItemBlockClass() {
      return ItemBlockVendingMachine.class;
   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileVendingMachine();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int damageDropped(int i) {
      return i;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister register) {
      this.iconTop = register.registerIcon("letitems:vendor-top");
      this.iconSide = register.registerIcon("letitems:vendor-side");
   }

   public int getRenderType() {
      return RenderConstants.vendingRendererId;
   }
}
