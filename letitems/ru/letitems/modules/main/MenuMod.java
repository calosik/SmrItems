package ru.letitems.modules.main;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.lwjgl.opengl.Display;
import ru.letitems.client.ClientParams;
import ru.letitems.common.util.module.AbstractModule;

@SideOnly(Side.CLIENT)
public final class MenuMod extends AbstractModule {
   public static final MenuMod instance = new MenuMod();
   public final File cacheDirectory;
   public int teleportToNewLoc;

   public MenuMod() {
      this.cacheDirectory = new File("..", "Assets" + File.separator + "visual");
      this.teleportToNewLoc = 0;
   }

   public void preInitClient(FMLPreInitializationEvent event) {
      super.preInitClient(event);
      this.cacheDirectory.mkdirs();
      String name = Minecraft.getMinecraft().mcDataDir.getName();
      String finalName;
      if (name.startsWith("ptr")) {
         finalName = "PTR: Test World";
      } else {
         finalName = name.replace("_Shaders", "").replace("Tech", " Tech").replace("Change", " Change");
      }

      Display.setTitle("Minecraft 1.7.10 - Letragon " + finalName);
      CharacterUtils.loadCharacterInfo(Minecraft.getMinecraft());
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onWorldLoad(EntityJoinWorldEvent event) {
      if (event.world.isRemote && event.entity instanceof EntityPlayer) {
         if (event.world.provider.dimensionId == 0) {
            ClientParams.setEndTimeToDIM180();
         }

         if (this.teleportToNewLoc > 0) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + (this.teleportToNewLoc == 1 ? "spawn" : "home"));
            this.teleportToNewLoc = 0;
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onGuiOpen(GuiOpenEvent event) {
      if (event.gui instanceof GuiMainMenu) {
         Minecraft.getMinecraft().displayGuiScreen(new GuiNewMainMenu());
         event.setCanceled(true);
      }

   }
}
