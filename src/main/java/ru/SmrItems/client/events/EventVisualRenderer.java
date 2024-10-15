package ru.SmrItems.client.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import ru.SmrItems.common.utils.AnchorsChunkManager;

public class EventVisualRenderer {
   private final Minecraft mc = Minecraft.getMinecraft();

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderWorldLastEvent(RenderWorldLastEvent event) {
      if (AnchorsChunkManager.rendered) {
         Iterator var2 = AnchorsChunkManager.getLoadingChunksInRadius(this.mc.thePlayer.worldObj, this.mc.thePlayer.posX, this.mc.thePlayer.posZ).iterator();

         while(var2.hasNext()) {
            ChunkCoordIntPair pair = (ChunkCoordIntPair)var2.next();
            this.renderVisualChunk(pair.chunkXPos, pair.chunkZPos, event.partialTicks);
         }
      }

   }

   private void renderVisualChunk(int chunkX, int chunkZ, float partialTicks) {
      double playerX = this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (double)partialTicks;
      double playerY = this.mc.thePlayer.prevPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.prevPosY) * (double)partialTicks;
      double playerZ = this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)partialTicks;
      float x = (float)(chunkX * 16);
      float y = (float)(this.mc.thePlayer.posY - 5.0D);
      float z = (float)(chunkZ * 16);
      GL11.glPushMatrix();
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glTranslated(-playerX, -playerY, -playerZ);
      GL11.glColor3ub((byte)-1, (byte)-1, (byte)-1);
      GL11.glLineWidth(3.0F);
      GL11.glBegin(1);
      GL11.glVertex3f(x, y, z);
      GL11.glVertex3f(x + 16.0F, y, z);
      GL11.glVertex3f(x, y, z);
      GL11.glVertex3f(x, y, z + 16.0F);
      GL11.glVertex3f(x, y, z + 16.0F);
      GL11.glVertex3f(x + 16.0F, y, z + 16.0F);
      GL11.glVertex3f(x + 16.0F, y, z);
      GL11.glVertex3f(x + 16.0F, y, z + 16.0F);
      GL11.glEnd();
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glPopMatrix();
   }
}
