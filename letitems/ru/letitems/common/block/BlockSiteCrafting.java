package ru.letitems.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import ru.letitems.common.util.LockPlayer;

public final class BlockSiteCrafting extends BlockBase {
   private static LockPlayer lockPlayer = new LockPlayer();

   public BlockSiteCrafting() {
      super(Material.rock, "siteCrafting");
      this.setHardness(2.5F);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      super.randomDisplayTick(world, x, y, z, rand);
      if (rand.nextInt(16) == 0) {
         for(int x1 = x - 2; x1 <= x + 2; ++x1) {
            for(int z1 = z - 2; z1 <= z + 2; ++z1) {
               if (x1 > x - 2 && x1 < x + 2 && z1 == z - 1) {
                  z1 = z + 2;
               }

               for(int y1 = y; y1 <= y + 1; ++y1) {
                  world.spawnParticle("enchantmenttable", (double)x1 + 0.4D + (double)rand.nextFloat() * 0.2D, (double)y1 + 1.2D, (double)z1 + 0.4D + (double)rand.nextFloat() * 0.2D, (double)((float)(x - x1) + rand.nextFloat()) - 0.5D, (double)(y - y1 - 2), (double)((float)(z - z1) + rand.nextFloat()) - 0.5D);
               }
            }
         }
      }

   }

   // $FF: synthetic method
   private static void lambda$onBlockActivated$0(EntityPlayerMP playerMp, EntityPlayer player, UUID uuid) {
   }
}
