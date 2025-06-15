package ru.letitems.client.renderer.commercial;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.tile.TileVendingMachine;

@SideOnly(Side.CLIENT)
public class TileEntityVendingMachineRenderer extends TileEntitySpecialRenderer {
   private final RenderItem renderer = new RenderItem();

   public TileEntityVendingMachineRenderer() {
      this.renderer.setRenderManager(RenderManager.instance);
   }

   public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
      TileVendingMachine machine = (TileVendingMachine)tileentity;
      if (machine != null && machine.getBlockType() != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)x + 0.5F, (float)y + 0.35F, (float)z + 0.5F);
         GL11.glEnable(32826);
         ItemStack itemStack = machine.getSoldItem();
         if (itemStack != null) {
            EntityItem entity = new EntityItem((World)null, x, y, z, itemStack);
            entity.hoverStart = 0.0F;
            if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
               entity.age = Minecraft.getMinecraft().thePlayer.ticksExisted;
            }

            try {
               this.renderer.doRender(entity);
            } catch (Throwable var13) {
            }
         }

         GL11.glDisable(32826);
         GL11.glPopMatrix();
      }
   }
}
