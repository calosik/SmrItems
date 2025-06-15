package ru.letitems.common.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import ru.letitems.common.items.ItemCosmetic;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketExtendedEquipment;
import ru.letitems.common.util.InventoryUtils;

public final class ExtendedPlayer extends InventoryBasic implements IExtendedEntityProperties {
   private static final String PROPERTY_NAME = "LetItemsProperty";
   private static final String NBT_LETITEMS = "LetItems";
   private static final String NBT_INVENTORY = "Inventory";
   /** @deprecated */
   @Deprecated
   private static final int SLOT_HAIR = 0;
   /** @deprecated */
   @Deprecated
   private static final int SLOT_SCOSMETIC = 1;
   /** @deprecated */
   @Deprecated
   private static final int SLOT_SCROLLTITLE = 2;
   private final ItemStack[] previousEquipment = new ItemStack[4];

   public ExtendedPlayer(int slotCount) {
      super("LetItemsInventory", false, slotCount);
   }

   public void saveNBTData(NBTTagCompound compound) {
      NBTTagCompound nbt = new NBTTagCompound();
      InventoryUtils.writeInvToNbt(nbt, "Inventory", this);
      compound.setTag("LetItems", nbt);
   }

   public void loadNBTData(NBTTagCompound compound) {
      NBTTagCompound nbt = compound.getCompoundTag("LetItems");
      InventoryUtils.readInvFromNbt(nbt, "Inventory", this);
   }

   public void init(Entity entity, World world) {
   }

   public void join(EntityPlayer player) {
      this.startTracking(player, player);
   }

   public void startTracking(EntityPlayer player, EntityPlayer otherPlayer) {
      for(int slot = 0; slot < 4; ++slot) {
         ItemStack stack = this.getStackInSlot(slot);
         NetworkManager.sendTo(new PacketExtendedEquipment(player.getEntityId(), slot, stack), otherPlayer);
      }

   }

   public void tick(EntityPlayer player, World worldObj) {
   }

   public ItemStack getHair() {
      return this.getStackInSlot(0);
   }

   public int getSlotHair() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   public ItemStack getScrollTitle() {
      return this.getStackInSlot(2);
   }

   /** @deprecated */
   @Deprecated
   public int getSlotScrollTitle() {
      return 2;
   }

   /** @deprecated */
   @Deprecated
   public ItemStack getCosmetic() {
      return this.getStackInSlot(1);
   }

   /** @deprecated */
   @Deprecated
   public int getSlotCosmetic() {
      return 1;
   }

   public boolean searchCosmeticId(int id) {
      for(int i = 1; i <= 2; ++i) {
         ItemStack itemStack = this.getStackInSlot(i);
         if (itemStack != null && itemStack.getItem() instanceof ItemCosmetic && itemStack.getItemDamage() == id) {
            return true;
         }
      }

      return false;
   }

   public void copyFrom(ExtendedPlayer extendedPlayer) {
      for(int slot = 0; slot < this.getSizeInventory(); ++slot) {
         this.setInventorySlotContents(slot, extendedPlayer.getStackInSlot(slot));
      }

   }

   public static void initExtendedPlayer(EntityPlayer player) {
      ExtendedPlayer extendedPlayer = new ExtendedPlayer(48);
      player.registerExtendedProperties("LetItemsProperty", extendedPlayer);
   }

   public static ExtendedPlayer getExtendedPlayer(EntityPlayer player) {
      return (ExtendedPlayer)player.getExtendedProperties("LetItemsProperty");
   }
}
