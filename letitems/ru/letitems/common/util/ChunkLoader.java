package ru.letitems.common.util;

import java.util.Collections;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public final class ChunkLoader implements IChunkLoader {
   private final int radius;
   private Ticket ticket;

   public ChunkLoader(int radius) {
      this.radius = radius;
   }

   public void loadChunks(TileEntity tile, String playerName) {
   }

   private void loadChunks(World world, int x, int y, int z, String playerName) {
   }

   public void loadChunks(int x, int z, Ticket ticket) {
   }

   public void unloadChunks() {
   }

   public int getMaxChunksSize() {
      int squareSideLength = 1 + this.radius * 2;
      return squareSideLength * squareSideLength;
   }

   public int getLoadedChunksSize() {
      return this.getLoadedChunks().size();
   }

   private Set<ChunkCoordIntPair> getLoadedChunks() {
      return (Set)(this.ticket == null ? Collections.emptySet() : this.ticket.getChunkList());
   }
}
