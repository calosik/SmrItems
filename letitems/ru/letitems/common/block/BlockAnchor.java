package ru.letitems.common.block;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.items.blocks.ItemBlockAnchor;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.tile.TileAnchor;
import ru.letitems.common.util.BlockPos;
import ru.letitems.common.util.registry.IHasItemBlock;

public final class BlockAnchor extends BlockBaseContainer<TileAnchor> implements IHasItemBlock {
   public BlockAnchor() {
      super(Material.ground, "anchor");
      this.setHardness(1.0F);
      this.setStepSound(soundTypeStone);
   }

   public int damageDropped(int meta) {
      return meta;
   }

   public Class<? extends ItemBlock> getItemBlockClass() {
      return ItemBlockAnchor.class;
   }

   public Class<TileAnchor> getTileEntityClass() {
      return TileAnchor.class;
   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return this.hasTileEntity(meta) ? new TileAnchor() : null;
   }

   public boolean hasTileEntity(int meta) {
      return meta >= 0 && meta < BlockAnchor.AnchorType.values().length;
   }

   @SideOnly(Side.CLIENT)
   public boolean isBlockNormalCube() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public float getAmbientOcclusionLightValue() {
      return 1.0F;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      BlockAnchor.AnchorType[] var2 = BlockAnchor.AnchorType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockAnchor.AnchorType anchorType = var2[var4];
         anchorType.registerIcons(this, iconRegister);
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      int meta = world.getBlockMetadata(x, y, z);
      return BlockAnchor.AnchorType.getType(meta).getIcon(side, meta, world.getTileEntity(x, y, z));
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return BlockAnchor.AnchorType.getType(meta).getIcon(side, meta);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List stackList) {
      BlockAnchor.AnchorType[] var4 = BlockAnchor.AnchorType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockAnchor.AnchorType anchorType = var4[var6];
         stackList.add(new ItemStack(item, 1, anchorType.ordinal()));
      }

   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      super.onNeighborBlockChange(world, x, y, z, block);
   }

   @SideOnly(Side.CLIENT)
   public static class RenderAnchorChunks {
      public static BlockAnchor.RenderAnchorChunks instance = new BlockAnchor.RenderAnchorChunks();
      public BlockPos warpPosRender = null;

      public void init(BlockPos wrapLoad) {
         this.warpPosRender = wrapLoad;
         if (this.warpPosRender != null) {
            MinecraftForge.EVENT_BUS.register(this);
         } else {
            MinecraftForge.EVENT_BUS.unregister(this);
         }

      }

      @SubscribeEvent(
         priority = EventPriority.NORMAL
      )
      public void onRenderWorldLast(RenderWorldLastEvent event) {
         if (this.warpPosRender != null) {
            Minecraft mc = Minecraft.getMinecraft();
            if (this.getDistance(mc.thePlayer.posX, (double)this.warpPosRender.y, mc.thePlayer.posZ, (double)this.warpPosRender.x, (double)this.warpPosRender.y, (double)this.warpPosRender.z) < 1024.0D) {
               if (mc.thePlayer.worldObj.getTileEntity(this.warpPosRender.x, this.warpPosRender.y, this.warpPosRender.z) instanceof TileAnchor) {
                  int centerChunkX = this.warpPosRender.x >> 4;
                  int centerChunkZ = this.warpPosRender.z >> 4;

                  for(int chunkX = centerChunkX - 1; chunkX <= centerChunkX + 1; ++chunkX) {
                     for(int chunkZ = centerChunkZ - 1; chunkZ <= centerChunkZ + 1; ++chunkZ) {
                        this.renderVisualChunk(mc, chunkX, chunkZ, event.partialTicks);
                     }
                  }
               } else {
                  this.init((BlockPos)null);
               }
            }
         }

      }

      private void renderVisualChunk(Minecraft mc, int chunkX, int chunkZ, float partialTicks) {
         double playerX = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * (double)partialTicks;
         double playerY = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * (double)partialTicks;
         double playerZ = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double)partialTicks;
         float x = (float)(chunkX * 16);
         float y = (float)(mc.thePlayer.posY - 5.0D);
         float z = (float)(chunkZ * 16);
         GL11.glPushMatrix();
         GL11.glDisable(2896);
         GL11.glDisable(3553);
         GL11.glDisable(2929);
         GL11.glTranslated(-playerX, -playerY, -playerZ);
         GL11.glColor3ub((byte)-1, (byte)-1, (byte)-1);
         GL11.glLineWidth(3.0F);
         GL11.glBegin(1);
         GL11.glVertex3f(x, y, z);
         GL11.glVertex3f(x + 16.0F, y, z);
         GL11.glVertex3f(x, y, z);
         GL11.glVertex3f(x, y, z + 16.0F);
         GL11.glVertex3f(x, y, z + 16.0F);
         GL11.glVertex3f(x + 16.0F, y, z + 16.0F);
         GL11.glVertex3f(x + 16.0F, y, z);
         GL11.glVertex3f(x + 16.0F, y, z + 16.0F);
         GL11.glEnd();
         GL11.glEnable(2896);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GL11.glPopMatrix();
      }

      private double getDistance(double x, double y, double z, double x1, double y1, double z1) {
         x -= x1;
         y -= y1;
         z -= z1;
         return (double)MathHelper.sqrt_double(x * x + y * y + z * z);
      }
   }

   public static enum AnchorType {
      WORLD(false, false),
      PERSONAL(true, true);

      private final boolean playerChunkTicket;
      private final boolean playerMustBeOnline;
      private final String lowerCaseName;
      @SideOnly(Side.CLIENT)
      private IIcon icon_head;
      @SideOnly(Side.CLIENT)
      private IIcon icon_side;
      @SideOnly(Side.CLIENT)
      private IIcon icon_side_active;

      private AnchorType(boolean playerChunkTicket, boolean playerMustBeOnline) {
         this.playerChunkTicket = playerChunkTicket;
         this.playerMustBeOnline = playerMustBeOnline;
         this.lowerCaseName = this.name().toLowerCase();
      }

      public boolean isPlayerChunkTicket() {
         return this.playerChunkTicket;
      }

      public boolean isPlayerMustBeOnline() {
         return this.playerMustBeOnline;
      }

      public int getFuelForStack(ItemStack stack) {
         if (stack == null) {
            return 0;
         } else {
            float hours = this.getFuelForStackInHours(stack);
            return hours <= 0.0F ? 0 : MathHelper.floor_float(hours * 72000.0F);
         }
      }

      public float getFuelForStackInHours(ItemStack stack) {
         return stack != null && stack.getItem() == RegItems.itemKusFuel ? 10.0F : 0.0F;
      }

      @SideOnly(Side.CLIENT)
      public void registerIcons(BlockAnchor block, IIconRegister iconRegister) {
         String blockTexture = block.getTextureName() + '.' + this.getLowerCaseName() + '.';
         this.icon_head = iconRegister.registerIcon(blockTexture + "head");
         this.icon_side = iconRegister.registerIcon(blockTexture + "side");
         this.icon_side_active = iconRegister.registerIcon(blockTexture + "side.active");
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIcon(int side, int meta, TileEntity tile) {
         if (side == 1) {
            return this.icon_head;
         } else {
            return tile instanceof TileAnchor && ((TileAnchor)tile).getFuel() > 0 && !((TileAnchor)tile).getForcedShutdown() ? this.icon_side_active : this.icon_side;
         }
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIcon(int side, int meta) {
         return side == 1 ? this.icon_head : this.icon_side;
      }

      public String getLowerCaseName() {
         return this.lowerCaseName;
      }

      public static BlockAnchor.AnchorType getType(int meta) {
         BlockAnchor.AnchorType[] types = values();
         return types[MathHelper.clamp_int(meta, 0, types.length - 1)];
      }
   }
}
