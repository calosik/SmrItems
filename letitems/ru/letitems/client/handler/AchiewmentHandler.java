package ru.letitems.client.handler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.client.util.LoadAndRenderPic;

@SideOnly(Side.CLIENT)
public class AchiewmentHandler {
   private final Minecraft mc;
   private long aLong;
   public final ArrayList<AchiewmentHandler.AchiewmentInfo> achiewmentInfos = new ArrayList();

   public AchiewmentHandler(Minecraft minecraft) {
      this.mc = minecraft;
   }

   public void queueResearchInformation(String text, String image) {
      if (this.aLong == 0L) {
         this.aLong = Minecraft.getSystemTime();
         this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "letitems:achievement", 0.25F, 0.9F, false);
      }

      this.achiewmentInfos.add(new AchiewmentHandler.AchiewmentInfo(text, image));
      LoadAndRenderPic.instance.queueResearchInformation(image, image);
   }

   public void updateAchiewmentWindow() {
      if (this.aLong != 0L && this.achiewmentInfos.size() > 0) {
         double var1 = (double)(Minecraft.getSystemTime() - this.aLong) / 3500.0D;
         if (var1 >= 0.0D && var1 <= 1.0D) {
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft(), this.mc.displayWidth, this.mc.displayHeight);
            int x = (int)(0.5F * (float)resolution.getScaledWidth()) - 80;
            if (x + 161 > resolution.getScaledWidth()) {
               x = resolution.getScaledWidth() - 158;
            }

            int y = (int)(0.8F * (float)resolution.getScaledHeight());
            if (y + 80 > resolution.getScaledHeight()) {
               y = resolution.getScaledHeight() - 82;
            }

            int opacityText = 255;
            float opacityDraw = 1.0F;
            double pos = var1 * 2.0D;
            if (pos > 1.0D) {
               pos = 2.0D - pos;
               opacityDraw = (float)((double)opacityDraw - 0.05000000074505806D / pos);
               opacityText -= (int)(15.0D / pos);
            }

            pos *= 4.0D;
            pos = 1.0D - pos;
            if (pos < 0.0D) {
               pos = 0.0D;
            }

            pos *= pos;
            pos *= pos;
            int xx = (int)(pos * 36.0D);
            xx += y;
            if (opacityText < 0) {
               opacityText = 0;
            }

            if (opacityDraw < 0.0F) {
               opacityDraw = 0.0F;
            }

            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, opacityDraw);
            GL11.glTranslatef(1.0F, 1.0F, 1.0F);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            Tessellator tessellator = Tessellator.instance;
            GeneralClientUtils.bind("letitems:textures/gui/achievement.png");
            GL11.glTranslatef(0.5F, 0.58461535F, 0.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)x, (double)(xx + 38), 5.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)(x + 161), (double)(xx + 38), 5.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)(x + 161), (double)xx, 5.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double)x, (double)xx, 5.0D, 0.0D, 0.0D);
            tessellator.draw();
            AchiewmentHandler.AchiewmentInfo achiewmentInfo = (AchiewmentHandler.AchiewmentInfo)this.achiewmentInfos.get(0);
            if (opacityText > 0) {
               this.mc.fontRenderer.drawString("Получено достижение", x + 40, xx + 9, 9144970 + (opacityText << 24));
               int titleWidth = this.mc.fontRenderer.getStringWidth(achiewmentInfo.text);
               float scale = Math.min(116.0F / (float)titleWidth, 1.0F);
               GL11.glPushMatrix();
               GL11.glTranslated((double)(x + 40), (double)(xx + 20), 0.0D);
               GL11.glScalef(scale, scale, scale);
               this.mc.fontRenderer.drawString(achiewmentInfo.text, 0, 0, 4934475 + (opacityText << 24));
               GL11.glPopMatrix();
            }

            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            LoadAndRenderPic.instance.renderPic(achiewmentInfo.image, 1.0F, x, xx, opacityDraw);
         } else {
            this.achiewmentInfos.remove(0);
            if (this.achiewmentInfos.size() > 0) {
               this.aLong = Minecraft.getSystemTime();
               this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "letitems:achievement", 0.25F, 0.9F, false);
            } else {
               this.aLong = 0L;
            }
         }
      }

   }

   private static class AchiewmentInfo {
      public final String text;
      public final String image;

      public AchiewmentInfo(String text, String image) {
         this.text = text;
         this.image = image;
      }
   }
}
