package ru.letitems.common.tile;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.util.BlockPos;
import ru.letitems.common.util.GuiType;

public final class TileEntityDoll extends TileBase {
   private static final Random RANDOM = new Random();
   public int direction;
   public int waifuGob;
   private boolean waifuEnchanted;
   private int ticks = 0;
   private BlockPos blockPos = null;

   public TileEntityDoll() {
      super(true);
   }

   public BlockDoll.DollType getType() {
      Block block = this.getBlockType();
      return block instanceof BlockDoll ? BlockDoll.DollType.getTypeFromIndex(((BlockDoll)block).startTypeIndex + this.getBlockMetadata()) : BlockDoll.DollType.getTypeFromIndex(0);
   }

   public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
      super.onBlockPlacedBy(placer, stack);
      if (placer != null) {
         this.direction = MathHelper.floor_float(placer.rotationYaw * 4.0F / 360.0F + 0.5F) & 3;
      }

   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      BlockDoll.DollType type = this.getType();
      if (type.isGui()) {
         GuiType.DOLLS.openGui(player, (TileEntity)this);
      }

      return false;
   }

   public boolean canUpdate() {
      return false;
   }

   public void updateEntityServer() {
      super.updateEntityServer();
   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
      super.writeCustomToNBT(nbt);
      nbt.setByte("direction", (byte)this.direction);
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
      super.readCustomFromNBT(nbt);
      this.direction = nbt.getByte("direction");
   }
}
