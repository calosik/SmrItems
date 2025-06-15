package ru.letitems.common.tile;

import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Locale;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.ArrayUtils;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.integration.waila.IWailaBodyProvider;

public class TileVendingMachine extends TileInventory implements ISidedInventory, IWailaBodyProvider {
   private String ownerName = "";
   private String owners = "";
   private int countSale = 0;
   private boolean empty = false;
   private ForgeDirection forgeDirection;

   public TileVendingMachine() {
      super(12, "Items", "slot");
      this.setDropContent(false);
   }

   public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
      super.onBlockPlacedBy(placer, stack);
   }

   public void onBlockClicked(EntityPlayer player) {
   }

   private static boolean isEmpty(IInventory inventory) {
      return true;
   }

   public boolean onBlockActivated(EntityPlayer player, int side) {
      return true;
   }

   public boolean openGui(EntityPlayer player) {
      return true;
   }

   public boolean canUpdate() {
      return false;
   }

   public ItemStack getSoldItem() {
      return this.getStackInSlot(9);
   }

   public ItemStack getBoughtItem() {
      return this.getStackInSlot(10);
   }

   public ItemStack getRenderItem() {
      return this.getStackInSlot(11);
   }

   public boolean hasCustomInventoryName() {
      return true;
   }

   public String getInventoryName() {
      return "Vending Machine";
   }

   public void onBlockRemoval(Block block, int meta) {
   }

   public void readCustomFromNBT(NBTTagCompound nbt) {
      super.readCustomFromNBT(nbt);
      this.ownerName = nbt.getString("owner");
      this.owners = nbt.getString("owners");
      this.countSale = nbt.getInteger("count");
      this.empty = nbt.getBoolean("empty");
      if (nbt.hasKey("direction")) {
         this.forgeDirection = ForgeDirection.getOrientation(nbt.getInteger("direction"));
      } else {
         this.forgeDirection = ForgeDirection.DOWN;
      }

   }

   public void writeCustomToNBT(NBTTagCompound nbt) {
      super.writeCustomToNBT(nbt);
      nbt.setString("owner", this.ownerName);
      nbt.setString("owners", this.owners);
      nbt.setInteger("count", this.countSale);
      nbt.setBoolean("empty", this.empty);
      if (this.forgeDirection != null) {
         nbt.setInteger("direction", this.forgeDirection.ordinal());
      }

   }

   public boolean isItemValidForSlot(int slot, ItemStack stack) {
      return slot != 13;
   }

   public boolean canInsertItem(int slot, ItemStack stack, int side) {
      return false;
   }

   public boolean canExtractItem(int slot, ItemStack stack, int side) {
      return false;
   }

   public int[] getAccessibleSlotsFromSide(int side) {
      return ArrayUtils.EMPTY_INT_ARRAY;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public boolean hisPermission(EntityPlayer player) {
      return this.ownerName.equals(player.getDisplayName());
   }

   public String getOwners() {
      return this.owners;
   }

   public void setOwners(String owners) {
   }

   public boolean hisPermissionOwner(EntityPlayer player) {
      return false;
   }

   public int getCountSale() {
      return this.countSale;
   }

   public void emptyCountSale() {
   }

   public void setEmpty() {
      this.empty = !this.empty;
      this.markDirty();
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public ForgeDirection getForgeDirection() {
      return this.forgeDirection;
   }

   public void setForgeDirection() {
   }

   private IInventory getSourceInventory() {
      return null;
   }

   private void vend(EntityPlayer player) {
   }

   @SideOnly(Side.CLIENT)
   public double getMaxRenderDistanceSquared() {
      return (double)(((ClientProxy)LetItems.proxy).getModSettings().vendingDistance * ((ClientProxy)LetItems.proxy).getModSettings().vendingDistance);
   }

   @Method(
      modid = "Waila"
   )
   public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      TileEntity tileEntity = accessor.getTileEntity();
      if (tileEntity instanceof TileVendingMachine) {
         TileVendingMachine machine = (TileVendingMachine)tileEntity;
         currenttip.add(String.format("%sВладелец: %s", EnumChatFormatting.GRAY, machine.getOwnerName()));
         if (!machine.getOwners().isEmpty()) {
            currenttip.add(String.format("%sПомощники: %s", EnumChatFormatting.GRAY, machine.getOwners().replace(",", " и ")));
         }

         EntityPlayer player = accessor.getPlayer();
         if (machine.hisPermission(player)) {
            currenttip.add(String.format("%sСторона: %s", EnumChatFormatting.GRAY, EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.tile.side." + machine.forgeDirection.name().toLowerCase(Locale.US))));
         }
      }

      return currenttip;
   }
}
