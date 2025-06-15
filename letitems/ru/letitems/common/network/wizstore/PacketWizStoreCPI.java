package ru.letitems.common.network.wizstore;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import ru.letitems.client.gui.screen.GuiWizStore;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.network.AbstractPacket;

public final class PacketWizStoreCPI extends AbstractPacket {
   private int type;
   private List<ItemStack> stacks;
   private List<Integer> stackSize;
   private boolean legendary;
   private int coins;
   private int count;

   public PacketWizStoreCPI() {
   }

   public PacketWizStoreCPI(List<ItemStack> stacks, ArrayList<Integer> stackSize, boolean flagLeg, int coins) {
      this.type = 0;
      this.stacks = stacks;
      this.stackSize = stackSize;
      this.legendary = flagLeg;
      this.coins = coins;
   }

   public PacketWizStoreCPI(int count) {
      this.type = 1;
      this.count = count;
   }

   public void fromBytes(ByteBuf buf) {
      this.type = buf.readInt();
      if (this.type == 0) {
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

         this.legendary = buf.readBoolean();
         this.coins = buf.readInt();
      } else {
         this.count = buf.readInt();
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.type);
      if (this.type == 0) {
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

         buf.writeBoolean(this.legendary);
         buf.writeInt(this.coins);
      } else {
         buf.writeInt(this.count);
      }

   }

   @SideOnly(Side.CLIENT)
   public void processClient(NetHandlerPlayClient netHandler) {
      if (this.type == 0) {
         GuiWizStore.isActive = false;
         TimeTuTick.instance.delete(TimeTuTick.TypeTime.WIZ_STORE);
         GuiWizStore.finStacks = this.stacks;
         GuiWizStore.dropLegendary = this.legendary;
         GuiWizStore.dropCoins = this.coins;
      } else {
         TimeTuTick.instance.delete(TimeTuTick.TypeTime.WIZ_STORE);
         GuiWizStore.isLoaded = true;
         if (this.count == -1) {
            GuiWizStore.isActive = false;
         } else {
            GuiWizStore.isActive = true;
            TimeTuTick.instance.register(TimeTuTick.TypeTime.WIZ_STORE, (long)this.count);
         }
      }

   }

   public static final class Handler extends AbstractPacket.AbstractHandler {
   }
}
