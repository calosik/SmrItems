package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientProxy;
import ru.letitems.client.LetSettings;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.LetItems;

@SideOnly(Side.CLIENT)
public abstract class GuiPreRenderScreen extends GuiScreen {
   protected int xSize;
   protected int ySize;
   protected int guiLeft;
   protected int guiTop;
   protected static final ResourceLocation resource = GeneralClientUtils.put("letitems:textures/gui/inv-material.png");
   protected final LetSettings modSettings;
   protected final InventoryTab inventoryTab;
   protected boolean scrollMouse = false;
   protected boolean scrollWheel = false;
   protected int mouseY = 0;
   protected int guiMapY;
   protected int maxHeight;
   protected boolean mouseMoving;
   protected boolean clickMouseMoving = false;

   public GuiPreRenderScreen() {
      this.allowUserInput = false;
      this.xSize = 320;
      this.ySize = 245;
      this.modSettings = ((ClientProxy)LetItems.proxy).getModSettings();
      this.inventoryTab = InventoryTab.inventoryTab;
   }

   public void initGui() {
      super.initGui();
      this.guiLeft = (this.width - this.xSize) / 2;
      this.guiTop = (this.height - this.ySize) / 2;
      this.inventoryTab.initInventoryTabs(this.mc);
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      super.drawScreen(x, y, f);
      if (this.scrollMouse && this.mouseMoving && this.isScrollBox(x, y)) {
         this.guiMapY += y - this.mouseY;
         this.mouseY = y;
      }

      if (this.scrollWheel && this.isScrollBox(x, y)) {
         int k = Mouse.getDWheel();
         if (k < 0) {
            this.guiMapY -= 10;
         } else if (k > 0) {
            this.guiMapY += 10;
         }
      }

      if (this.scrollMouse && !this.mouseMoving) {
         if (this.guiMapY > 0) {
            --this.guiMapY;
            if (this.guiMapY > 40) {
               this.guiMapY -= 2;
            }
         }

         if (this.guiMapY < -this.maxHeight) {
            ++this.guiMapY;
            if (this.guiMapY < -this.maxHeight - 40) {
               this.guiMapY += 4;
            }
         }
      }

      this.inventoryTab.renderInventoryTabs(this, this.mc, resource, this.fontRendererObj, x, y, this.guiLeft, this.guiTop);
   }

   protected boolean isScrollBox(int x, int y) {
      return false;
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      this.inventoryTab.mouseClicked(x, y, this.guiLeft, this.guiTop);
      if (button == 0) {
         this.clickMouseMoving = true;
      }

   }

   public void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
      super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
      if (this.scrollMouse && lastButtonClicked == 0 && !this.mouseMoving && this.clickMouseMoving && timeSinceMouseClick >= 50L) {
         this.mouseMoving = true;
         this.mouseY = mouseY;
      }

   }

   public void mouseMovedOrUp(int mouseX, int mouseY, int which) {
      super.mouseMovedOrUp(mouseX, mouseY, which);
      if (this.mouseMoving) {
         this.mouseMoving = false;
      }

      this.clickMouseMoving = false;
   }

   protected void keyTyped(char p_73869_1_, int p_73869_2_) {
      if (p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
         this.mc.thePlayer.closeScreen();
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void updateScreen() {
      super.updateScreen();
      if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
         this.mc.thePlayer.closeScreen();
      }

   }

   protected void pushSound() {
      this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "letitems:nav.but", 0.08F, 1.25F, false);
   }

   protected void drawStringWhenNSSize(String text, float size, int x, int y, int color) {
      GL11.glPushMatrix();
      GL11.glTranslated((double)(this.guiLeft + x), (double)(this.guiTop + y), 0.0D);
      GL11.glScalef(size, size, size);
      this.fontRendererObj.drawString(text, 0, 0, color);
      GL11.glPopMatrix();
   }

   protected void drawStringWhenSize(String text, float size, int x, int y, int color) {
      GL11.glPushMatrix();
      GL11.glTranslated((double)(this.guiLeft + x), (double)(this.guiTop + y), 0.0D);
      GL11.glScalef(size, size, size);
      this.fontRendererObj.drawStringWithShadow(text, 0, 0, color);
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
            this.drawStringWhenSize(text, 1.0F, j2 - this.guiLeft, k2 - this.guiTop, -1);
         }
      }

   }
}
