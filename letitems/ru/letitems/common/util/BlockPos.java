package ru.letitems.common.util;

import net.minecraftforge.common.util.ForgeDirection;

public final class BlockPos {
   public final int x;
   public final int y;
   public final int z;

   public BlockPos(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public BlockPos offset(ForgeDirection direction) {
      return this.offset(direction.offsetX, direction.offsetY, direction.offsetZ);
   }

   public BlockPos offset(int xOffset, int yOffset, int zOffset) {
      return new BlockPos(this.x + xOffset, this.y + yOffset, this.z + zOffset);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof BlockPos)) {
         return false;
      } else {
         BlockPos pos = (BlockPos)obj;
         return pos.x == this.x && pos.y == this.y && pos.z == this.z;
      }
   }

   public int hashCode() {
      return this.x * 8976890 + this.y * 981131 + this.z;
   }

   public String toString() {
      return this.x + ";" + this.y + ';' + this.z;
   }
}
