package ru.SmrItems.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import ru.SmrItems.common.tileentity.TileWorldAnchor;
import ru.SmrItems.util.enums.FieldType;
import ru.SmrItems.util.enums.LoadingMode;

public class AnchorsChunkManager {
   public static List<BlockPos> anchorsList = new ArrayList();
   public static boolean rendered = false;

   public static Set<ChunkCoordIntPair> getLoadingChunksInRadius(World world, double x, double z) {
      return getLoadingChunksInRadius(world, x, z, 1024);
   }

   public static Set<ChunkCoordIntPair> getLoadingChunksInRadius(World world, double x, double z, int radius) {
      Set<ChunkCoordIntPair> chunks = new HashSet();
      Iterator var7 = anchorsList.iterator();

      while(true) {
         BlockPos a;
         TileWorldAnchor te;
         do {
            do {
               if (!var7.hasNext()) {
                  return chunks;
               }

               a = (BlockPos)var7.next();
            } while(!(Utils.getDistance(x, (double)a.getPosY(), z, (double)a.getPosX(), (double)a.getPosY(), (double)a.getPosZ()) < (double)radius));

            te = (TileWorldAnchor)world.getTileEntity(a.getPosX(), a.getPosY(), a.getPosZ());
         } while(te == null);

         LoadingMode mode = (LoadingMode)te.getField(FieldType.MODE);

         for(int i = -mode.ordinal(); i < mode.ordinal() + 1; ++i) {
            for(int j = -mode.ordinal(); j < mode.ordinal() + 1; ++j) {
               chunks.add(new ChunkCoordIntPair((a.getPosX() >> 4) + i, (a.getPosZ() >> 4) + j));
            }
         }
      }
   }

   public static List<ChunkCoordIntPair> getLoadArea(LoadingMode mode, int x, int z) {
      List<ChunkCoordIntPair> loadArea = new LinkedList();

      for(int i = -mode.ordinal(); i < mode.ordinal() + 1; ++i) {
         for(int j = -mode.ordinal(); j < mode.ordinal() + 1; ++j) {
            loadArea.add(new ChunkCoordIntPair((x >> 4) + i, (z >> 4) + j));
         }
      }

      return loadArea;
   }
}
