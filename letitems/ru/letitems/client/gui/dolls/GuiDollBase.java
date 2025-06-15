package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiDollBase extends GuiScreen {
   protected int posX;
   protected int posY;
   protected static final int xSize = 256;
   protected static final int ySize = 190;

   public GuiDollBase() {
      this.allowUserInput = false;
   }

   public void initGui() {
      super.initGui();
      this.posX = (this.width - 256) / 2;
      this.posY = (this.height - 190) / 2;
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void drawDollName(String text, int x, int y, int color) {
      this.drawStringWhenSize("Кукла " + text, 1.15F, x, y, color);
   }

   protected void drawStringWhenSize(String text, float size, int x, int y, int color) {
      GL11.glPushMatrix();
      GL11.glTranslated((double)(this.posX + x), (double)(this.posY + y), 0.0D);
      GL11.glScalef(size, size, size);
      this.fontRendererObj.drawString(text, 0, 0, color);
      GL11.glPopMatrix();
   }

   protected void drawHoveringText(List<String> list, int x, int y) {
      if (!list.isEmpty()) {
         GL11.glDisable(2929);
         int k = 0;
         Iterator var5 = list.iterator();

         int i1;
         while(var5.hasNext()) {
            String s = (String)var5.next();
            i1 = this.fontRendererObj.getStringWidth(s);
            if (i1 > k) {
               k = i1;
            }
         }

         int j2 = x + 12;
         int k2 = y - 12;
         i1 = 8;
         if (list.size() > 1) {
            i1 += 2 + (list.size() - 1) * 10;
         }

         if (j2 + k > this.width) {
            j2 -= 28 + k;
         }

         if (k2 + i1 + 6 > this.height) {
            k2 = this.height - i1 - 6;
         }

         int j1 = -267386864;
         int radius = 8;
         this.drawGradientRect(j2 - radius, k2 - radius, j2 + k + radius, k2 + i1 + radius, j1, j1);

         for(Iterator var10 = list.iterator(); var10.hasNext(); k2 += 10) {
            String text = (String)var10.next();
            this.fontRendererObj.drawString(text, j2, k2, -1);
         }
      }

   }
}
