package ru.SmrItems.modules.main;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public final class ClientEventHandler {
   private final Minecraft mc = Minecraft.getMinecraft();

   public static void init() {
      MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onGuiOpen(GuiOpenEvent event) {
      if (event.gui instanceof GuiMainMenu) {
         this.mc.displayGuiScreen(new GuiNewMainMenu());
         event.setCanceled(true);
      }

   }
}
