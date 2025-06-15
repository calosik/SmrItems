package ru.letitems.common.network;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import ru.letitems.common.LetItems;
import ru.letitems.common.network.mods.PacketTwilightForest;
import ru.letitems.common.network.mods.PacketTwilightForestSync;
import ru.letitems.common.network.mods.PacketWitchery;
import ru.letitems.common.network.wizstore.PacketWizStore;
import ru.letitems.common.network.wizstore.PacketWizStoreCPI;

public final class NetworkManager {
   private static SimpleNetworkWrapper network;
   private static int nextPacketId;

   public static void init() {
      network = NetworkRegistry.INSTANCE.newSimpleChannel("letitems");
      registerPacket(PacketOpenGui.class, Side.SERVER);
      registerPacket(PacketExtendedEquipment.class, Side.CLIENT);
      registerPacket(PacketAnchor.class, Side.SERVER);
      registerPacket(PacketLootBox.class, Side.SERVER);
      registerPacket(PacketLootBoxCPI.class, Side.CLIENT);
      registerPacket(PacketInvisibility.class, Side.SERVER);
      registerPacket(PacketSceneSender.class, Side.SERVER);
      registerPacket(PacketAchiewment.class, Side.CLIENT);
      registerPacket(PacketGuiGlobalUpdate.class, Side.SERVER);
      registerPacket(PacketGuiUpdate.class, Side.CLIENT);
      registerPacket(PacketVending.class, Side.SERVER);
      registerPacket(PacketClientParams.class, Side.CLIENT);
      registerPacket(PacketDollsMech.class, Side.SERVER);
      registerPacket(PacketDollsSync.class, Side.CLIENT);
      registerPacket(PacketWizStore.class, Side.SERVER);
      registerPacket(PacketWizStoreCPI.class, Side.CLIENT);
      registerPacket(PacketCollections.class, Side.SERVER);
      registerPacket(PacketGuid.class, Side.SERVER);
      registerPacket(PacketStoreItems.class, Side.SERVER);
      registerPacket(PacketGuisSync.class, Side.CLIENT);
      if (LetItems.loadWitchery) {
         registerPacket(PacketWitchery.class, Side.SERVER);
      }

      if (LetItems.loadTF) {
         registerPacket(PacketTwilightForest.class, Side.SERVER);
         registerPacket(PacketTwilightForestSync.class, Side.CLIENT);
      }

   }

   public static void sendTo(IMessage message, EntityPlayer player) {
      if (player instanceof EntityPlayerMP) {
         network.sendTo(message, (EntityPlayerMP)player);
      }

   }

   public static void sendToServer(IMessage message) {
      network.sendToServer(message);
   }

   public static void sendToAllAround(IMessage message, World world, double x, double y, double z, double range) {
      network.sendToAllAround(message, new TargetPoint(world.provider.dimensionId, x, y, z, range));
   }

   public static void sendToAll(IMessage message) {
      network.sendToAll(message);
   }

   private static <T extends AbstractPacket> void registerPacket(Class<T> packetClass, Side handlerSide) {
      Class handlerClass = null;

      try {
         handlerClass = Class.forName(packetClass.getName() + "$Handler");
      } catch (ClassNotFoundException var4) {
         throw Throwables.propagate(var4);
      }

      network.registerMessage(handlerClass, packetClass, nextPacketId++, handlerSide);
   }
}
