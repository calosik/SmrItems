package ru.letitems.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public final class BagEventHandler {
   private final Random random = new Random();

   public static void init() {
      MinecraftForge.EVENT_BUS.register(new BagEventHandler());
   }

   @SubscribeEvent
   public void onPickup(EntityItemPickupEvent event) {
   }

   private static boolean addItemToInventory(IInventory inv, ItemStack stack) {
      return false;
   }
}
