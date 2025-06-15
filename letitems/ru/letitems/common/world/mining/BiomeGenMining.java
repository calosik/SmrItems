package ru.letitems.common.world.mining;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.Height;

public class BiomeGenMining extends BiomeGenBase {
   public static BiomeGenMining instance;

   public BiomeGenMining(int i) {
      super(i);
      this.setColor(2900485);
      this.setBiomeName("Mining Biome");
      this.setTemperatureRainfall(0.5F, 0.15F);
      this.setHeight(new Height(0.0F, 0.0F));
      super.flowers.clear();
      super.spawnableCreatureList.clear();
      super.spawnableMonsterList.clear();
      super.spawnableWaterCreatureList.clear();
      super.spawnableCaveCreatureList.clear();
      super.theBiomeDecorator.treesPerChunk = 0;
      super.theBiomeDecorator.bigMushroomsPerChunk = 0;
      super.theBiomeDecorator.clayPerChunk = 0;
      super.theBiomeDecorator.deadBushPerChunk = 0;
      super.theBiomeDecorator.mushroomsPerChunk = 0;
      super.theBiomeDecorator.reedsPerChunk = 0;
      super.theBiomeDecorator.flowersPerChunk = 0;
      super.theBiomeDecorator.grassPerChunk = 0;
   }
}
