package ru.letitems.common.tile;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.Optional.Method;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import ru.letitems.common.integration.waila.IWailaBodyProvider;
import ru.letitems.common.util.IHasOwner;

public final class TilePlayerDetector extends TileBase implements IHasOwner, IWailaBodyProvider {
   private static final String NBT_OWNER = "Owner";
   private static final String NBT_ACTIVE = "Active";
   private static final int RANGE = 8;
   private GameProfile ownerProfile;
   private boolean active;
   private int tick = 0;

   public TilePlayerDetector() {
      super(true);
   }

   public void updateEntityServer() {
      super.updateEntityServer();
   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      return true;
   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
      super.writeCustomToNBT(nbt);
      if (this.hasOwner()) {
         NBTTagCompound ownerNbt = new NBTTagCompound();
         NBTUtil.func_152460_a(ownerNbt, this.getOwner());
         nbt.setTag("Owner", ownerNbt);
      }

      nbt.setBoolean("Active", this.isActive());
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
      super.readCustomFromNBT(nbt);
      this.setOwner(NBTUtil.func_152459_a(nbt.getCompoundTag("Owner")));
      this.setActive(nbt.getBoolean("Active"), false);
   }

   public boolean isActive() {
      return this.active;
   }

   private void setActive(boolean active, boolean update) {
      if (this.active != active) {
         this.active = active;
         this.markDirty();
         if (update) {
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
         }
      }

   }

   public boolean hasOwner() {
      GameProfile owner = this.getOwner();
      return owner != null && owner.isComplete();
   }

   public GameProfile getOwner() {
      return this.ownerProfile;
   }

   public void setOwner(GameProfile ownerProfile) {
      this.ownerProfile = ownerProfile != null && ownerProfile.isComplete() ? new GameProfile(ownerProfile.getId(), ownerProfile.getName()) : null;
   }

   @Method(
      modid = "Waila"
   )
   public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      TileEntity tileEntity = accessor.getTileEntity();
      if (tileEntity instanceof TilePlayerDetector && ((TilePlayerDetector)tileEntity).hasOwner()) {
         currenttip.add(EnumChatFormatting.GRAY + "Владелец: " + ((TilePlayerDetector)tileEntity).getOwner().getName());
      }

      return currenttip;
   }
}
