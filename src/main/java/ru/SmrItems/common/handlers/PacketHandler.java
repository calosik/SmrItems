package ru.SmrItems.common.handlers;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import ru.SmrItems.common.packets.PacketClickButton;

public class PacketHandler {
   public static final SimpleNetworkWrapper INSTANCE;

   public static void init() {
      INSTANCE.registerMessage(PacketClickButton.Handler.class, PacketClickButton.class, 0, Side.SERVER);
   }

   static {
      INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SmrItems");
   }
}
