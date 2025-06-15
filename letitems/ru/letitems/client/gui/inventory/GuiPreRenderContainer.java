package ru.letitems.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientProxy;
import ru.letitems.client.LetSettings;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.LetItems;

@SideOnly(Side.CLIENT)
public abstract class GuiPreRenderContainer extends GuiContainer {
   protected static final ResourceLocation resource = GeneralClientUtils.put("letitems:textures/gui/inv-material.png");
   protected final ResourceLocation resource2 = GeneralClientUtils.put("letitems:textures/gui/inv-other-material.png");
   protected final LetSettings modSettings;
   protected final InventoryTab inventoryTab;

   public GuiPreRenderContainer(Container container) {
      super(container);
      this.xSize = 320;
      this.ySize = 245;
      this.modSettings = ((ClientProxy)LetItems.proxy).getModSettings();
      this.inventoryTab = InventoryTab.inventoryTab;
   }

   public void initGui() {
      super.initGui();
      this.inventoryTab.initInventoryTabs(this.mc);
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.renderPotionsBar(x, y);
   }

   public void drawGuiContainerForegroundLayer(int x, int y) {
      super.drawGuiContainerForegroundLayer(x, y);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      this.inventoryTab.renderInventoryTabs(this, this.mc, resource, this.fontRendererObj, xx, yy, 0, 0);
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      this.inventoryTab.mouseClicked(x, y, this.guiLeft, this.guiTop);
   }

   protected void renderPlayer(int x, int y) {
      this.renderPlayer(45.0F, x, y);
   }

   protected void renderPlayer(float size, int x, int y) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(x + 115), (float)(y + 63), 50.0F);
      GL11.glScalef(-size, size, size);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(65.0F, 0.0F, 1.0F, 0.0F);
      float f2 = this.mc.thePlayer.renderYawOffset;
      float f3 = this.mc.thePlayer.rotationYaw;
      float f4 = this.mc.thePlayer.rotationPitch;
      float f5 = this.mc.thePlayer.prevRotationYawHead;
      float f6 = this.mc.thePlayer.rotationYawHead;
      RenderHelper.enableStandardItemLighting();
      this.mc.thePlayer.renderYawOffset = 38.0F;
      this.mc.thePlayer.rotationYaw = 45.0F;
      this.mc.thePlayer.rotationPitch = -12.0F;
      this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw;
      this.mc.thePlayer.prevRotationYawHead = this.mc.thePlayer.rotationYaw;
      RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      this.mc.thePlayer.renderYawOffset = f2;
      this.mc.thePlayer.rotationYaw = f3;
      this.mc.thePlayer.rotationPitch = f4;
      this.mc.thePlayer.prevRotationYawHead = f5;
      this.mc.thePlayer.rotationYawHead = f6;
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   private void renderPotionsBar(int x, int y) {
      int smallPot = this.modSettings.invChangePotions;
      if (smallPot != 0) {
         Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();
         if (!collection.isEmpty()) {
            int j = this.guiTop;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(2896);

            for(Iterator iterator = this.mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext(); j += 30) {
               PotionEffect potioneffect = (PotionEffect)iterator.next();
               Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               this.mc.getTextureManager().bindTexture(resource);
               GL11.glEnable(3042);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               int i = this.guiLeft;
               if (smallPot == 1) {
                  i -= 124;
                  this.drawTexturedModalRect(i, j, 0, 170, 120, 28);
               } else {
                  i -= 36;
                  this.drawTexturedModalRect(i, j, 120, 170, 30, 28);
               }

               int xx;
               if (potion.hasStatusIcon()) {
                  xx = potion.getStatusIconIndex();
                  this.drawTexturedModalRect(i + 6, j + 7, xx % 8 * 18, 198 + xx / 8 * 20, 18, 18);
               }

               potion.renderInventoryEffect(i, j, potioneffect, this.mc);
               if (potion.shouldRenderInvText(potioneffect)) {
                  if (smallPot == 1) {
                     this.fontRendererObj.drawStringWithShadow(this.getPotionsName(potion.getName(), potioneffect.getAmplifier()), i + 28, j + 5, 11776947);
                     this.fontRendererObj.drawStringWithShadow(Potion.getDurationString(potioneffect), i + 28, j + 15, 6842472);
                  } else {
                     xx = x - this.guiLeft;
                     int yy = y - this.guiTop;
                     if (xx > -39 && xx < -5 && yy > 0 && yy < 28) {
                        ArrayList<String> list = new ArrayList();
                        list.add("§6" + this.getPotionsName(potion.getName(), potioneffect.getAmplifier()));
                        list.add("§7Еще " + Potion.getDurationString(potioneffect));
                        this.drawHoveringText(list, x + 10, y + 10, this.fontRendererObj);
                        GL11.glDisable(2896);
                     }
                  }
               }
            }
         }

      }
   }

   private String getPotionsName(String name, int level) {
      String s1 = I18n.format(name, new Object[0]);
      if (level == 1) {
         s1 = s1 + " II";
      } else if (level == 2) {
         s1 = s1 + " III";
      } else if (level == 3) {
         s1 = s1 + " IV";
      } else if (level >= 4) {
         s1 = s1 + " V+";
      }

      return s1;
   }

   protected void voidSlot(int x, int y, int offsetX, int offsetY, int size) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      this.drawTexturedModalRect(x, y, offsetX, offsetY, size, size);
      GL11.glPopMatrix();
   }

   protected void drawStringWhenSize(String text, float size, int x, int y, int color) {
      GL11.glPushMatrix();
      GL11.glTranslated((double)(this.guiLeft + x), (double)(this.guiTop + y), 0.0D);
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
            this.drawStringWhenSize(text, 1.0F, j2 - this.guiLeft, k2 - this.guiTop, -1);
         }
      }

   }
}
