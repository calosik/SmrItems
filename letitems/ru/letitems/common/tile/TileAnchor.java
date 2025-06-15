package ru.letitems.common.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import org.apache.commons.lang3.ArrayUtils;
import ru.letitems.common.block.BlockAnchor;
import ru.letitems.common.util.ChunkLoader;
import ru.letitems.common.util.IChunkLoader;
import ru.letitems.common.util.IHasOwner;

public final class TileAnchor extends TileInventory implements IHasOwner, ISidedInventory, IChunkLoader {
   private static final String NBT_OWNER = "Owner";
   private static final String NBT_FUEL = "Fuel";
   private static final String NBT_FORCEDSHUTDOWN = "forcedShutdown";
   private static final int SLOT_FUEL = 0;
   private final ChunkLoader chunkLoader = new ChunkLoader(1);
   private GameProfile ownerProfile;
   private int fuel;
   private boolean active;
   private boolean forcedShutdown;

   public TileAnchor() {
      super(1);
   }

   public void updateEntityServer() {
      super.updateEntityServer();
   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
      super.writeCustomToNBT(nbt);
      if (this.hasOwner()) {
         NBTTagCompound ownerNbt = new NBTTagCompound();
         NBTUtil.func_152460_a(ownerNbt, this.getOwner());
         nbt.setTag("Owner", ownerNbt);
      }

      nbt.setInteger("Fuel", this.getFuel());
      nbt.setBoolean("forcedShutdown", this.getForcedShutdown());
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
      super.readCustomFromNBT(nbt);
      this.setOwner(NBTUtil.func_152459_a(nbt.getCompoundTag("Owner")));
      this.setFuel(nbt.getInteger("Fuel"));
      this.setForcedShutdown(nbt.getBoolean("forcedShutdown"));
   }

   public void invalidate() {
      super.invalidate();
   }

   public void onBlockRemoval(Block block, int meta) {
      super.onBlockRemoval(block, meta);
   }

   public boolean openGui(EntityPlayer player) {
      return true;
   }

   public BlockAnchor.AnchorType getAnchorType() {
      return BlockAnchor.AnchorType.getType(this.getBlockMetadata());
   }

   public void loadChunks(int x, int z, Ticket ticket) {
   }

   private boolean canWork() {
      return false;
   }

   private boolean isActive() {
      return this.active;
   }

   private void setActive(boolean active) {
      if (this.active != active) {
         this.active = active;
         this.markDirty();
      }

   }

   private boolean consumeFuelStack() {
      return false;
   }

   private boolean hasFuel() {
      return this.getFuel() >= 1;
   }

   public int getFuel() {
      return this.fuel;
   }

   private void addFuel(int amount) {
      this.setFuel((int)Math.min(2147483647L, (long)this.getFuel() + (long)amount));
   }

   private void removeFuel() {
      int fuel = this.getFuel();
      if (fuel >= 1) {
         this.setFuel(fuel - 1);
      }

   }

   private void setFuel(int amount) {
      int newFuel = Math.max(0, amount);
      if (this.fuel != newFuel) {
         this.fuel = newFuel;
         this.markDirty();
      }

   }

   public boolean getForcedShutdown() {
      return this.forcedShutdown;
   }

   public void setForcedShutdown(boolean forcedShutdown) {
      if (this.forcedShutdown != forcedShutdown) {
         this.forcedShutdown = forcedShutdown;
         this.markDirty();
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

   public boolean isItemValidForSlot(int slot, ItemStack stack) {
      return super.isItemValidForSlot(slot, stack) && (slot != 0 || this.getAnchorType().getFuelForStack(stack) > 0);
   }

   public int[] getAccessibleSlotsFromSide(int side) {
      return ArrayUtils.EMPTY_INT_ARRAY;
   }

   public boolean canInsertItem(int slot, ItemStack stack, int side) {
      return false;
   }

   public boolean canExtractItem(int slot, ItemStack stack, int side) {
      return false;
   }
}
