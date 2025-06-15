package ru.letitems.modules.scene;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

@SideOnly(Side.CLIENT)
public final class PacketScene implements IMessage {
   public void toBytes(ByteBuf par1ByteBuf) {
   }

   public void fromBytes(ByteBuf buf) {
   }

   @SideOnly(Side.CLIENT)
   public static final class Handler implements IMessageHandler<PacketScene, IMessage> {
      public IMessage onMessage(PacketScene packet, MessageContext context) {
         FMLCommonHandler.instance().bus().register(SceneManager.instance);
         SceneManager.instance.startSceneBuild = true;
         SceneManager.instance.lastFuture = null;
         return null;
      }
   }
}
