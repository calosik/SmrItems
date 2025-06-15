package ru.SmrItems.client.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.SmrItems.client.ClientProxy;
import ru.SmrItems.util.AnchorsChunkManager;

public class EventKeybinding {
   @SideOnly(Side.CLIENT)
   @SubscribeEvent(
      receiveCanceled = true
   )
   public void keyInputEvent(KeyInputEvent event) {
      if (ClientProxy.visualRenderKeybind.isPressed()) {
         AnchorsChunkManager.rendered = !AnchorsChunkManager.rendered;
      }

   }
}
