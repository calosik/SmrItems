package ru.SmrItems.modules.main;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class MenuMod {
   public static boolean teleportToSpawn = true;

   public void onWorldLoad(EntityJoinWorldEvent event) {
      if (teleportToSpawn && event.world.isRemote) {
         Minecraft.getMinecraft().thePlayer.sendChatMessage("/spawn");
         teleportToSpawn = true;
      }

   }
}
