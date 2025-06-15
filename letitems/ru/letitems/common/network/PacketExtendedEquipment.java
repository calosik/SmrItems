package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.letitems.common.inventory.ExtendedPlayer;

public final class PacketExtendedEquipment extends AbstractPacket {
   private int entityId;
   private int slot;
   private ItemStack stack;

   public PacketExtendedEquipment() {
   }

   public PacketExtendedEquipment(int entityId, int slot, ItemStack stack) {
      this.entityId = entityId;
      this.slot = slot;
      this.stack = stack;
   }

   public void fromBytes(ByteBuf buf) {
      this.entityId = buf.readInt();
      this.slot = buf.readShort();
      this.stack = ByteBufUtils.readItemStack(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeShort(this.slot);
      ByteBufUtils.writeItemStack(buf, this.stack);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.entityId);
      if (entity instanceof EntityPlayer) {
         ExtendedPlayer.getExtendedPlayer((EntityPlayer)entity).setInventorySlotContents(this.slot, this.stack);
      }

   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
