package ru.letitems.common.tile;

import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.block.BlockDakimakura;
import ru.letitems.common.dakimakura.Daki;
import ru.letitems.common.dakimakura.serialize.DakiNbtSerializer;
import ru.letitems.common.integration.waila.IWailaBodyProvider;
import ru.letitems.common.items.blocks.ItemBlockDakimakura;
import ru.letitems.common.registry.RegItems;

public class TileDakimakura extends TileBase implements IWailaBodyProvider {
   private static final AxisAlignedBB[] ROTS = new AxisAlignedBB[]{AxisAlignedBB.getBoundingBox(0.0D, 0.0D, -1.0D, 1.0D, 0.2800000011920929D, 1.0D), AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.2800000011920929D, 2.0D), AxisAlignedBB.getBoundingBox(-1.0D, 0.0D, 0.0D, 1.0D, 0.2800000011920929D, 1.0D), AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 2.0D, 0.2800000011920929D, 1.0D)};
   private String dakiDirName;
   private boolean flipped;

   public TileDakimakura() {
      super(true);
   }

   public void setDaki(Daki daki) {
      this.dakiDirName = daki != null ? daki.getDakiDirectoryName() : null;
      this.markDirty();
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public Daki getDaki() {
      return LetItems.proxy.getDakimakuraManager().getDakiFromMap(this.dakiDirName);
   }

   private void setFlipped(boolean flipped) {
      this.flipped = flipped;
      this.markDirty();
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public boolean isFlipped() {
      return this.flipped;
   }

   private void flip() {
      this.setFlipped(!this.flipped);
   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      return player.isSneaking();
   }

   public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
      super.onBlockPlacedBy(placer, stack);
      Daki daki = ItemBlockDakimakura.getDaki(stack);
      this.setDaki(daki);
      this.setFlipped(ItemBlockDakimakura.isFlipped(stack));
   }

   public void onBlockRemoval(Block block, int meta) {
      super.onBlockRemoval(block, meta);
   }

   public ArrayList<ItemStack> getDrops(Block block, int meta, int fortune, boolean onBreak) {
      if (onBreak) {
         return new ArrayList();
      } else {
         Daki daki = this.getDaki();
         ItemStack itemStack = new ItemStack(RegItems.blockDakimakura, 1, ItemBlockDakimakura.getDakiMeta(daki));
         if (daki != null) {
            itemStack.setTagCompound(DakiNbtSerializer.serialize(daki));
         }

         ArrayList<ItemStack> drops = new ArrayList(1);
         drops.add(itemStack);
         return drops;
      }
   }

   private TileDakimakura getMasterTile() {
      return this;
   }

   public boolean canUpdate() {
      return false;
   }

   public void readCustomFromNBT(NBTTagCompound compound) {
      super.readCustomFromNBT(compound);
      if (compound.hasKey("dakiDirName", 8)) {
         this.dakiDirName = compound.getString("dakiDirName");
      }

      this.flipped = DakiNbtSerializer.isFlipped(compound);
   }

   public void writeCustomToNBT(NBTTagCompound compound) {
      super.writeCustomToNBT(compound);
      if (this.dakiDirName != null) {
         compound.setString("dakiDirName", this.dakiDirName);
      }

      DakiNbtSerializer.setFlipped(compound, this.flipped);
   }

   public void onDataPacket(NBTTagCompound nbt) {
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   @SideOnly(Side.CLIENT)
   public double getMaxRenderDistanceSquared() {
      return (double)(((ClientProxy)LetItems.proxy).getModSettings().dakiDistance * ((ClientProxy)LetItems.proxy).getModSettings().dakiDistance);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      int meta = this.getBlockMetadata();
      if (BlockDakimakura.isStanding(meta)) {
         return AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 2), (double)(this.zCoord + 1));
      } else {
         ForgeDirection rot = BlockDakimakura.getRotation(meta);
         return ROTS[rot.ordinal() - 2 & 3].getOffsetBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord);
      }
   }

   @Method(
      modid = "Waila"
   )
   public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   @Method(
      modid = "Waila"
   )
   public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
      TileEntity tileEntity = accessor.getTileEntity();
      return tileEntity instanceof TileDakimakura ? new ItemStack(RegItems.blockDakimakura, 1, ItemBlockDakimakura.getDakiMeta(((TileDakimakura)tileEntity).getDaki())) : null;
   }
}
