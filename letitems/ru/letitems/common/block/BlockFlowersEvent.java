package ru.letitems.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.letitems.client.ClientParams;
import ru.letitems.common.items.blocks.ItemBlockFlowersEvent;
import ru.letitems.common.tile.TileEntityFlowersEvent;
import ru.letitems.common.util.registry.IHasItemBlock;
import ru.letitems.common.util.registry.IHasNamedTileEntity;

public final class BlockFlowersEvent extends BlockBaseContainer<TileEntityFlowersEvent> implements IHasNamedTileEntity<TileEntityFlowersEvent>, IHasItemBlock {
   private static final String[] FLOWERS_STAGE = new String[]{"flower_rose", "flower_blue_orchid", "flower_allium", "flower_houstonia", "flower_tulip_red"};
   private IIcon[] icons;

   public BlockFlowersEvent() {
      super(Material.plants, "flowersEvent");
      this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
   }

   public IIcon getIcon(int side, int meta) {
      if (meta >= this.icons.length) {
         meta = 0;
      }

      return this.icons[meta];
   }

   public int damageDropped(int i) {
      return i;
   }

   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[FLOWERS_STAGE.length];

      for(int i = 0; i < this.icons.length; ++i) {
         this.icons[i] = iconRegister.registerIcon(FLOWERS_STAGE[i]);
      }

   }

   public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
      for(int i = 0; i < this.icons.length; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   public Class<TileEntityFlowersEvent> getTileEntityClass() {
      return TileEntityFlowersEvent.class;
   }

   public String getTileEntityName() {
      return "FlowersEvent";
   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileEntityFlowersEvent();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
      return null;
   }

   public int getRenderType() {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      super.randomDisplayTick(world, x, y, z, rand);
      if (ClientParams.idKys == 3) {
         TileEntity tileEntity = world.getTileEntity(x, y, z);
         if (tileEntity instanceof TileEntityFlowersEvent) {
            TileEntityFlowersEvent tileEntityFlowersEvent = (TileEntityFlowersEvent)tileEntity;
            if (tileEntityFlowersEvent.getTimeUntil() <= 0) {
               world.spawnParticle("flame", (double)((float)x + rand.nextFloat()), (double)((float)y + rand.nextFloat()) + 0.5D, (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
               world.spawnParticle("smoke", (double)((float)x + rand.nextFloat()), (double)((float)y + rand.nextFloat() + 1.0F), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
            } else {
               world.spawnParticle("townaura", (double)((float)x + rand.nextFloat()), (double)((float)y + rand.nextFloat()), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
               world.spawnParticle("townaura", (double)((float)x + rand.nextFloat()), (double)((float)y + rand.nextFloat()) + 0.5D, (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
               world.spawnParticle("townaura", (double)((float)x + rand.nextFloat()) + 0.5D, (double)((float)y + rand.nextFloat()), (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
            }
         }
      }

   }

   public Class<? extends ItemBlock> getItemBlockClass() {
      return ItemBlockFlowersEvent.class;
   }
}
