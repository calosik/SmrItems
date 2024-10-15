package ru.SmrItems.world;

import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import ru.SmrItems.registry.RegItems;

public class GenOre<block> implements IWorldGenerator {
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
      switch(world.provider.dimensionId) {
      case -1:
         this.generateNether(random, chunkX * 16, chunkZ * 16, world);
         break;
      case 0:
         this.generateOverWorld(random, chunkX * 16, chunkZ * 16, world);
         break;
      case 1:
         this.generateEnd(random, chunkX * 16, chunkZ * 16, world);
      }

   }

   private void addOre(Block block, Block blockspawn, Random random, World world, int posX, int posZ, int minY, int maxY, int minVien, int maxVien, int spawnChance) {
      for(int i = 0; i < spawnChance; ++i) {
         int defaultChunkSize = 16;
         int xPos = posX + random.nextInt(defaultChunkSize);
         int yPos = minY + random.nextInt(maxY - minY);
         int zPos = posZ + random.nextInt(defaultChunkSize);
         (new WorldGenMinable(block, minVien + random.nextInt(maxVien - minVien), blockspawn)).generate(world, random, xPos, yPos, zPos);
      }

   }

   private void generateEnd(Random random, int chunkX, int chunkZ, World world) {
      this.addOre(RegItems.unstableore, Blocks.stone, random, world, chunkX, chunkZ, 1, 128, 1, 4, 20);
   }

   private void generateOverWorld(Random random, int chunkX, int chunkZ, World world) {
      this.addOre(RegItems.unstableore, Blocks.stone, random, world, chunkX, chunkZ, 1, 128, 1, 4, 20);
   }

   private void generateNether(Random random, int chunkX, int chunkZ, World world) {
   }
}
