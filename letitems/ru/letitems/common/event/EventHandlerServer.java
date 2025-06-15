package ru.letitems.common.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import ru.letitems.common.inventory.ExtendedPlayer;

public final class EventHandlerServer {
   private final Map<UUID, ExtendedPlayer> savedData = new HashMap();
   private static List<String> blockEntries = new ArrayList(5);
   private static List<ItemStack> itemEntries = new ArrayList(11);

   public static void init() {
      EventHandlerServer handler = new EventHandlerServer();
      FMLCommonHandler.instance().bus().register(handler);
      MinecraftForge.EVENT_BUS.register(handler);
   }

   @SubscribeEvent
   public void onCrafting(ItemCraftedEvent event) {
   }

   @SubscribeEvent
   public void onEntityConstructing(EntityConstructing event) {
      if (event.entity instanceof EntityPlayer && ExtendedPlayer.getExtendedPlayer((EntityPlayer)event.entity) == null) {
         ExtendedPlayer.initExtendedPlayer((EntityPlayer)event.entity);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerDeath(LivingDeathEvent event) {
   }

   @SubscribeEvent
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      EntityPlayer player = event.player;
   }

   @SubscribeEvent
   public void onQuit(PlayerLoggedOutEvent event) {
      EntityPlayer player = event.player;
   }

   @SubscribeEvent
   public void onPlayerUpdate(LivingUpdateEvent event) {
   }

   @SubscribeEvent
   public void onStartTracking(StartTracking event) {
      if (event.target instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.target;
         ExtendedPlayer.getExtendedPlayer(player).startTracking(player, event.entityPlayer);
      }

   }

   @SubscribeEvent
   public void onJoin(PlayerLoggedInEvent event) {
      EntityPlayer player = event.player;
      ExtendedPlayer.getExtendedPlayer(player).join(player);
   }

   @SubscribeEvent
   public void entityHurt(LivingHurtEvent event) {
   }

   @SubscribeEvent
   public void harvestBlock(HarvestDropsEvent event) {
   }

   @SubscribeEvent
   public void onBonemeal(BonemealEvent event) {
   }

   @SubscribeEvent
   public void onPlayerInteract(PlayerInteractEvent event) {
   }
}
