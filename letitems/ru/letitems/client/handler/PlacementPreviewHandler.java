package ru.letitems.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.model.ModelDakimakura;
import ru.letitems.common.dakimakura.Daki;
import ru.letitems.common.registry.RegItems;

@SideOnly(Side.CLIENT)
public class PlacementPreviewHandler {
   private final ModelDakimakura modelDakimakura;

   public PlacementPreviewHandler(ModelDakimakura modelDakimakura) {
      this.modelDakimakura = modelDakimakura;
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
      EntityPlayer entityPlayer = event.player;
      World world = event.player.worldObj;
      MovingObjectPosition target = event.target;
      if (target != null && target.typeOfHit == MovingObjectType.BLOCK) {
         float partialTicks = event.partialTicks;
         int x = target.blockX;
         int y = target.blockY;
         int z = target.blockZ;
         int side = target.sideHit;
         float xOff = (float)(entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)partialTicks);
         float yOff = (float)(entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)partialTicks);
         float zOff = (float)(entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)partialTicks);
         if (event.currentItem != null && event.currentItem.getItem() == Item.getItemFromBlock(RegItems.blockDakimakura)) {
            Block block = world.getBlock(x, y, z);
            ForgeDirection sideDir = ForgeDirection.getOrientation(side);
            int rot = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            ForgeDirection[] rots = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
            ForgeDirection rotation = rots[rot].getOpposite();
            boolean standing = false;
            if ((block == Blocks.snow_layer && (world.getBlockMetadata(x, y, y) & 7) < 1) | block == Blocks.tallgrass) {
               sideDir = ForgeDirection.UP;
            } else if (block != Blocks.vine && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
               x += sideDir.offsetX;
               y += sideDir.offsetY;
               z += sideDir.offsetZ;
            }

            if (sideDir != ForgeDirection.UP & sideDir != ForgeDirection.DOWN) {
               rotation = sideDir.getOpposite();
               standing = true;
            }

            if (sideDir == ForgeDirection.DOWN) {
               --y;
               standing = true;
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(-xOff + (float)x, -yOff + (float)y, -zOff + (float)z);
            float scale = 0.0625F;
            GL11.glTranslatef(0.5F, 0.125F, 0.5F);
            if (rotation == ForgeDirection.WEST) {
               GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            } else if (rotation == ForgeDirection.NORTH) {
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else if (rotation == ForgeDirection.EAST) {
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (block.isBed(world, x, y, z, entityPlayer) & side == 1) {
               GL11.glTranslatef(0.0F, -0.4375F, 0.0F);
            }

            GL11.glTranslatef(0.0F, 0.0F, 0.25F);
            if (!standing) {
               GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            } else {
               GL11.glTranslatef(0.0F, 0.625F, 0.125F);
            }

            GL11.glPushAttrib(1048575);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(1.0F);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            GL11.glPolygonMode(1032, 6913);
            this.modelDakimakura.render((Daki)null, 0);
            GL11.glPolygonMode(1032, 6914);
            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
         }

      }
   }
}
