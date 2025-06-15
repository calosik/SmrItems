package ru.letitems.common.world;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import ru.letitems.common.registry.RegItems;

public final class EventGenOre implements IWorldGenerator {
   @SubscribeEvent
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
      switch(world.provider.dimensionId) {
      case -1:
         generate(world, chunkX * 16, chunkZ * 16, RegItems.OreArteforNether, 9, Blocks.netherrack, random);
         break;
      case 0:
         addOreSpawn(world, random, chunkX * 16, chunkZ * 16, 4);
         break;
      case 1:
         generate(world, chunkX * 16, chunkZ * 16, RegItems.OreArteforEnd, 18, Blocks.end_stone, random);
         break;
      case 180:
         addOreSpawn(world, random, chunkX * 16, chunkZ * 16, 5);
      }

   }

   private static void addOreSpawn(World world, Random random, int blockXPos, int blockZPos, int count) {
      for(int x = 0; x < count; ++x) {
         int posX = blockXPos + random.nextInt(16);
         int posY = 8 + random.nextInt(108);
         int posZ = blockZPos + random.nextInt(16);
         (new WorldGenMinable(RegItems.OreArteforOverworld, random.nextInt(2) + 4)).generate(world, random, posX, posY, posZ);
      }

   }

   private static void generate(World world, int x, int z, Block block, int number, Block target, Random random) {
      int xx = x + random.nextInt(16);
      int yy = 10 + random.nextInt(128);
      int zz = z + random.nextInt(16);
      (new WorldGenMinable(block, 1, number, target)).generate(world, random, xx, yy, zz);
   }
}
