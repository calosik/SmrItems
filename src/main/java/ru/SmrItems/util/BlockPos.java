package ru.SmrItems.util;

public class BlockPos {
   private int posX;
   private int posY;
   private int posZ;

   public BlockPos() {
   }

   public BlockPos(int posX, int posY, int posZ) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
   }

   public void setBlockPosition(int posX, int posY, int posZ) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
   }

   public int getPosX() {
      return this.posX;
   }

   public int getPosY() {
      return this.posY;
   }

   public int getPosZ() {
      return this.posZ;
   }
}
