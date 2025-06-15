package ru.letitems.common.util;

import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

public final class GeneralUtils {
   private static List<String> potionsIdGoods;
   private static List<String> potionsIdBads;

   public static boolean isFakePlayer(EntityPlayer player) {
      return player.worldObj == null || !player.worldObj.isRemote && player.getClass() != EntityPlayerMP.class && !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
   }

   public static boolean isOnlinePlayer(UUID playerId) {
      return FMLCommonHandler.instance().getSide().isClient();
   }

   public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean b) {
      float var4 = 1.0F;
      float var5 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * 1.0F;
      float var6 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * 1.0F;
      double var7 = player.prevPosX + (player.posX - player.prevPosX) * 1.0D;
      double var9 = player.prevPosY + (player.posY - player.prevPosY) * 1.0D + 1.62D - (double)player.yOffset;
      double var11 = player.prevPosZ + (player.posZ - player.prevPosZ) * 1.0D;
      Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
      float var14 = MathHelper.cos(-var6 * 0.017453292F - 3.1415927F);
      float var15 = MathHelper.sin(-var6 * 0.017453292F - 3.1415927F);
      float var16 = -MathHelper.cos(-var5 * 0.017453292F);
      float var17 = MathHelper.sin(-var5 * 0.017453292F);
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      double var21 = 5.0D;
      if (player instanceof EntityPlayerMP) {
         var21 = ((EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance();
      }

      Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
      return world.rayTraceBlocks(var13, var23, b);
   }

   public static <T extends TileEntity> T getTileEntity(World world, int x, int y, int z, Class<T> tileClass) {
      TileEntity tile = world.getTileEntity(x, y, z);
      return tileClass.isInstance(tile) ? tile : null;
   }

   public static <K, V> V getOrDefault(Map<? super K, ? extends V> map, K key, V defaultValue) {
      V value = map.get(key);
      return value == null ? defaultValue : value;
   }

   /** @deprecated */
   @Deprecated
   public static List<SpawnListEntry> getFish() {
      return new ArrayList();
   }

   public static String isPlayerFromServers(EntityPlayerMP player) {
      return null;
   }

   public static void removePlayersFromServer() {
   }

   public static void getSendAvoid(EntityPlayer player, int i, String s) {
   }

   public static void getSendAvoidPercent(EntityPlayer player, int i, String s, int o, int e, boolean f) {
   }

   public static void getSendAvoidProgress(EntityPlayer player, int i, String s, int o, String e) {
   }

   public static int getLevelSkill(EntityPlayer player, int i) {
      return 0;
   }

   public static void skillAddExperience(EntityPlayer player, int i, int exp) {
   }

   public static void placeSeedBlock(EntityPlayer player, World world, Block block) {
   }

   public static int changeAddExperience(EntityPlayer player, int amount) {
      return amount;
   }

   public static void sendQuest(EntityPlayer player, int id, String args) {
   }

   public static float sendExpSkillFire(EntityPlayer player, DamageSource damagesource, float damage) {
      return damage;
   }

   public static PotionEffect changePotionEffect(PotionEffect potioneffect, EntityPlayerMP player, boolean isNotActive) {
      return potioneffect;
   }

   public static int putEnchantPlayer(EntityPlayer player, int expDrain, int sizeEnch, ItemStack itemStack) {
      return expDrain;
   }

   public static void setItemCurse(ItemStack itemStack, boolean remove) {
   }

   public static boolean isItemCurse(ItemStack itemStack) {
      return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("itemCurse");
   }

   public static void putRepairPlayer(EntityPlayer player, int expDrain, ItemStack itemStack, String itemName) {
   }

   public static int addStats(EntityPlayer player, ItemStack itemStack, int foodLevel) {
      return foodLevel;
   }

   public static ItemStack[] getRandomFishable(EntityPlayer player, Random rand) {
      return null;
   }

   private static ItemStack getItemFish(Random rand, ArrayList<WeightedRandomFishable> type) {
      return ((WeightedRandomFishable)WeightedRandom.getRandomItem(rand, type)).func_150708_a(rand);
   }

   public static void sendMessage(EntityPlayer player, String text) {
      sendMessage(player, text, EnumChatFormatting.GOLD);
   }

   public static void sendMessage(EntityPlayer player, String text, EnumChatFormatting color) {
      player.addChatMessage((new ChatComponentText(text + ".")).setChatStyle((new ChatStyle()).setColor(color)));
   }

   public static String declination(int v, String[] t) {
      return t[v % 10 == 1 && v % 100 != 11 ? 0 : (v % 10 < 2 || v % 10 > 4 || v % 100 >= 10 && v % 100 < 20 ? 2 : 1)];
   }

   public static String declinationCombine(int v, String[] t, String s) {
      return String.format("%d %s%s", v, s, declination(v, t));
   }
}
