package ru.letitems.common.block;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.BiomeDictionary.Type;
import ru.letitems.common.LetItems;
import ru.letitems.common.tile.TileBase;
import ru.letitems.common.util.registry.IHasNamedTileEntity;
import ru.letitems.common.world.EventGenOre;
import ru.letitems.common.world.mining.BiomeGenMining;
import ru.letitems.common.world.mining.WorldProviderMiner;

public final class BlockMinePortal extends BlockBaseContainer<BlockMinePortal.TileEntityMinePortal> implements IHasNamedTileEntity<BlockMinePortal.TileEntityMinePortal> {
   public static final int DIMENSION_ID = 180;
   public static final int DIMENSION_HEIGHT = 64;
   public static final int BIOME_ID = 64;

   public static void init() {
      BiomeGenMining.instance = new BiomeGenMining(64);
      if (DimensionManager.isDimensionRegistered(180)) {
         LetItems.LOGGER.debug("Failed to register the Mining Dimension with the ID 180.");
      }

      DimensionManager.registerProviderType(180, WorldProviderMiner.class, false);
      DimensionManager.registerDimension(180, 180);
      WorldProvider provider = DimensionManager.createProviderFor(180);
      if (provider instanceof WorldProviderMiner) {
         FMLInterModComms.sendMessage("BuildCraft|Energy", "oil-gen-exclude", "64");
         if (LetItems.loadThaumCraft) {
            FMLInterModComms.sendMessage("Thaumcraft", "dimensionBlacklist", "180:1");
            FMLInterModComms.sendMessage("Thaumcraft", "biomeBlacklist", "64:1");
         }

         BiomeManager.addStrongholdBiome(BiomeGenMining.instance);
         BiomeDictionary.registerBiomeType(BiomeGenMining.instance, new Type[]{Type.FOREST, Type.DENSE, Type.SPOOKY, Type.MAGICAL, Type.DRY, Type.JUNGLE});
      }

      GameRegistry.registerWorldGenerator(new EventGenOre(), 0);
   }

   public BlockMinePortal() {
      super(Material.portal, "miningPortal");
      this.setHardness(-1.0F);
      this.setStepSound(Block.soundTypeGlass);
      this.setLightLevel(0.75F);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public IIcon getIcon(int side, int meta) {
      return Blocks.portal.getIcon(side, meta);
   }

   public int getRenderBlockPass() {
      return 1;
   }

   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      return 16711756;
   }

   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (entity.ridingEntity == null && entity.riddenByEntity == null && entity.timeUntilPortal <= 0 && entity instanceof EntityPlayerMP) {
         EntityPlayerMP playerMP = (EntityPlayerMP)entity;
         if (playerMP.timeUntilPortal > 0) {
            playerMP.timeUntilPortal = 10;
         } else if (playerMP.dimension == 0) {
            transferPlayer(playerMP);
         }
      }

   }

   public static void transferPlayer(EntityPlayer player) {
   }

   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (random.nextInt(100) == 0) {
         if (random.nextInt(25) == 0) {
            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "letitems:niko", 0.3F, 1.0F, false);
         } else {
            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "portal.portal", 0.4F, random.nextFloat() * 0.4F + 0.8F, false);
         }
      }

      for(int l = 0; l < 3; ++l) {
         int i1 = random.nextInt(2) * 2 - 1;
         double d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
         double d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
         double d = (double)i + 0.5D + 0.25D * (double)i1;
         double d3 = (double)(random.nextFloat() * 2.0F * (float)i1);
         world.spawnParticle("portal", d, (double)((float)j + random.nextFloat()), (double)((float)k + random.nextFloat()), d3, d4, d5);
      }

   }

   public Class<BlockMinePortal.TileEntityMinePortal> getTileEntityClass() {
      return BlockMinePortal.TileEntityMinePortal.class;
   }

   public String getTileEntityName() {
      return "MinePortal";
   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return new BlockMinePortal.TileEntityMinePortal();
   }

   public static final class TileEntityMinePortal extends TileBase {
      public boolean active;

      public TileEntityMinePortal() {
         super(true);
      }

      public boolean onBlockActivated(EntityPlayer player, int side) {
         return true;
      }

      public boolean canUpdate() {
         return false;
      }

      public void readCustomFromNBT(NBTTagCompound nbt) {
         super.readCustomFromNBT(nbt);
         this.active = nbt.getBoolean("active");
      }

      public void writeCustomToNBT(NBTTagCompound nbt) {
         super.writeCustomToNBT(nbt);
         nbt.setBoolean("active", this.active);
      }
   }

   public static class TeleporterMining extends Teleporter {
      public TeleporterMining(WorldServer world) {
         super(world);
      }

      public void placeInPortal(Entity p_77185_1_, double p_77185_2_, double p_77185_4_, double p_77185_6_, float p_77185_8_) {
      }

      public boolean placeInExistingPortal(Entity entity, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_) {
         return false;
      }

      public boolean makePortal(Entity p_85188_1_) {
         return false;
      }

      public void removeStalePortalLocations(long p_85189_1_) {
      }
   }
}
