package ru.letitems.common.world.mining;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

public class WorldProviderMiner extends WorldProvider {
   public void registerWorldChunkManager() {
      super.registerWorldChunkManager();
      super.worldChunkMgr = new WorldChunkManagerHell(BiomeGenMining.instance, 1.0F);
   }

   public String getWelcomeMessage() {
      return "";
   }

   public String getDepartMessage() {
      return "";
   }

   public String getDimensionName() {
      return "Mining World";
   }

   public boolean canRespawnHere() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public float getCloudHeight() {
      return 96.0F;
   }

   public boolean getWorldHasVoidParticles() {
      return false;
   }

   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderGenerate(super.worldObj, super.worldObj.getSeed(), true);
   }

   public BiomeGenBase getBiomeGenForCoords(int x, int z) {
      return BiomeGenMining.instance;
   }

   public int getAverageGroundLevel() {
      return 64;
   }

   protected void generateLightBrightnessTable() {
      Arrays.fill(super.lightBrightnessTable, 4.0F);
      super.generateLightBrightnessTable();
   }
}
