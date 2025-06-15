package ru.letitems.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import ru.letitems.client.ClientParams;
import ru.letitems.client.gui.screen.GuiLootBox;

public final class PacketLootBoxCPI extends AbstractPacket {
   private List<ItemStack> stacks;
   private List<Integer> stackSize;
   private int type;
   private int i;
   private int rCoins;

   public PacketLootBoxCPI() {
   }

   public PacketLootBoxCPI(List<ItemStack> stacks, ArrayList<Integer> stackSize, int type, int i, int rCoins) {
      this.stacks = stacks;
      this.stackSize = stackSize;
      this.type = type;
      this.i = i;
      this.rCoins = rCoins;
   }

   public void fromBytes(ByteBuf buf) {
      int size = buf.readInt();
      this.stacks = new ArrayList(size);
      this.stackSize = new ArrayList(size);

      int i;
      for(i = 0; i < size; ++i) {
         this.stacks.add(ByteBufUtils.readItemStack(buf));
      }

      for(i = 0; i < size; ++i) {
         ((ItemStack)this.stacks.get(i)).stackSize = buf.readInt();
      }

      this.type = buf.readInt();
      this.i = buf.readInt();
      this.rCoins = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.stacks.size());
      Iterator var2 = this.stacks.iterator();

      while(var2.hasNext()) {
         ItemStack stack = (ItemStack)var2.next();
         ByteBufUtils.writeItemStack(buf, stack);
      }

      var2 = this.stackSize.iterator();

      while(var2.hasNext()) {
         Integer i = (Integer)var2.next();
         buf.writeInt(i);
      }

      buf.writeInt(this.type);
      buf.writeInt(this.i);
      buf.writeInt(this.rCoins);
   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      GuiLootBox.finItemStack = this.stacks;
      if (this.type == 1) {
         ClientParams.userCoinsBox1 = this.i;
      } else if (this.type == 2) {
         ClientParams.userCoinsBox2 = this.i;
      } else {
         ClientParams.userCoinsBox3 = this.i;
      }

      GuiLootBox.rCoins = this.rCoins;
   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
