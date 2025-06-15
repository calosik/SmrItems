package ru.letitems.modules.tooltips;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.util.GeneralUtils;

@SideOnly(Side.CLIENT)
public final class Tooltip {
   private static final Map<String, String> itemIdToModName = new HashMap();
   private static final Map<EnumChatFormatting, Integer> formattingToColorCode = new EnumMap(EnumChatFormatting.class);
   private static Method info;
   private static boolean useNei = false;
   private int alpha;
   private int width;
   private int height;
   private EntityItem entity;
   private List<String> text = new ArrayList();

   public static void init() {
      Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
      Iterator var1 = modMap.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<String, ModContainer> modEntry = (Entry)var1.next();
         String lowercaseId = ((String)modEntry.getKey()).toLowerCase(Locale.ENGLISH);
         String modName = ((ModContainer)modEntry.getValue()).getName();
         itemIdToModName.put(lowercaseId, modName);
      }

      formattingToColorCode.put(EnumChatFormatting.BLACK, 0);
      formattingToColorCode.put(EnumChatFormatting.DARK_BLUE, 170);
      formattingToColorCode.put(EnumChatFormatting.DARK_GREEN, 43520);
      formattingToColorCode.put(EnumChatFormatting.DARK_AQUA, 43690);
      formattingToColorCode.put(EnumChatFormatting.DARK_RED, 11141120);
      formattingToColorCode.put(EnumChatFormatting.DARK_PURPLE, 11141290);
      formattingToColorCode.put(EnumChatFormatting.GOLD, 16755200);
      formattingToColorCode.put(EnumChatFormatting.GRAY, 11184810);
      formattingToColorCode.put(EnumChatFormatting.DARK_GRAY, 5592405);
      formattingToColorCode.put(EnumChatFormatting.BLUE, 5592575);
      formattingToColorCode.put(EnumChatFormatting.GREEN, 5635925);
      formattingToColorCode.put(EnumChatFormatting.AQUA, 5636095);
      formattingToColorCode.put(EnumChatFormatting.RED, 16733525);
      formattingToColorCode.put(EnumChatFormatting.LIGHT_PURPLE, 16733695);
      formattingToColorCode.put(EnumChatFormatting.YELLOW, 16777045);
      formattingToColorCode.put(EnumChatFormatting.WHITE, 16777215);

      try {
         Class<?> nei = Class.forName("codechicken.nei.guihook.GuiContainerManager");
         if (nei != null) {
            info = nei.getDeclaredMethod("itemDisplayNameMultiline", ItemStack.class, GuiContainer.class, Boolean.TYPE);
            info.setAccessible(true);
            useNei = true;
         }
      } catch (Exception var5) {
      }

   }

   public Tooltip(EntityPlayer player, EntityItem entity) {
      this.entity = entity;
      this.syncSettings();
      this.generateTooltip(player, entity.getEntityItem());
   }

   public void syncSettings() {
      this.alpha = ((int)(WorldTooltips.alpha * 255.0F) & 255) << 24;
   }

   private int getWidth() {
      return this.width;
   }

   private int getHeight() {
      return this.height;
   }

   public EntityItem getEntity() {
      return this.entity;
   }

   public int size() {
      return this.text.size();
   }

   private String getLine(int line) {
      return (String)this.text.get(line);
   }

   public EnumChatFormatting getRarityColor() {
      return this.entity.getEntityItem().getRarity().rarityColor;
   }

   private void generateTooltip(EntityPlayer player, ItemStack item) {
      if (useNei) {
         try {
            this.text = (List)info.invoke((Object)null, this.entity.getEntityItem(), null, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
         } catch (Exception var6) {
         }
      }

      if (this.text == null || this.text.isEmpty()) {
         this.text = item.getTooltip(player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
      }

      if (!modsAreLoaded() && ((ClientProxy)LetItems.proxy).getModSettings().tplHideModName) {
         this.text.add(EnumChatFormatting.BLUE.toString() + EnumChatFormatting.ITALIC.toString() + getModName(item.getItem()) + EnumChatFormatting.RESET.toString());
      }

      if (item.stackSize > 1) {
         this.text.set(0, item.stackSize + " x " + (String)this.text.get(0));
      }

      int maxwidth = 0;

      for(int line = 0; line < this.text.size(); ++line) {
         int swidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(this.getLine(line));
         if (swidth > maxwidth) {
            maxwidth = swidth;
         }
      }

      this.width = maxwidth;
      this.height = 8;
      if (this.size() > 1) {
         this.height += 2 + (this.size() - 1) * 10;
      }

   }

   private static boolean modsAreLoaded() {
      return LetItems.loadWaila | LetItems.loadNEI;
   }

   private static String getModName(Item item) {
      String fullName = Item.itemRegistry.getNameForObject(item);
      if (fullName == null) {
         return "UNKNOWN_MOD";
      } else {
         String modId = fullName.substring(0, fullName.indexOf(58));
         String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
         String modName = (String)itemIdToModName.get(lowercaseModId);
         if (modName == null) {
            modName = WordUtils.capitalize(modId);
            itemIdToModName.put(lowercaseModId, modName);
         }

         return modName;
      }
   }

   public void renderTooltip3D(Minecraft mc, double partialTicks) {
      ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
      int outline1 = (Integer)GeneralUtils.getOrDefault(formattingToColorCode, this.getRarityColor(), 5243135);
      outline1 = (outline1 & 16711422) >> 1 | this.alpha;
      int outline2 = (outline1 & 16711422) >> 1 | this.alpha;
      float interpX = (float)(RenderManager.renderPosX - (this.entity.posX - (this.entity.prevPosX - this.entity.posX) * partialTicks));
      float interpY = (float)(RenderManager.renderPosY - (this.entity.posY - (this.entity.prevPosY - this.entity.posY) * partialTicks));
      float interpZ = (float)(RenderManager.renderPosZ - (this.entity.posZ - (this.entity.prevPosZ - this.entity.posZ) * partialTicks));
      float interpDistance = (float)Math.sqrt((double)(interpX * interpX + interpY * interpY + interpZ * interpZ));
      GL11.glPushMatrix();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glTranslatef(-interpX, -(interpY - 0.55F), -interpZ);
      GL11.glRotatef(-RenderManager.instance.playerViewY + 180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
      float scale = interpDistance / (float)(sr.getScaleFactor() * 160);
      if (scale <= 0.01F) {
         scale = 0.01F;
      }

      GL11.glScalef(scale, -scale, scale);
      int x = -this.getWidth() / 2;
      int y = -this.getHeight();
      renderTooltipTile(x, y, this.getWidth(), this.getHeight(), 1048592 | this.alpha, outline1 | this.alpha, outline2 | this.alpha);
      renderTooltipText(this, x, y, this.alpha);
      GL11.glScalef(1.0F / scale, 1.0F / -scale, 1.0F / scale);
      GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(RenderManager.instance.playerViewY - 180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(interpX, interpY - this.entity.height - 0.5F, interpZ);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }

   private static void renderTooltipText(Tooltip tooltip, int drawx, int drawy, int alpha) {
      for(int i = 0; i < tooltip.size(); ++i) {
         String s = tooltip.getLine(i);
         if (i == 0) {
            s = tooltip.getRarityColor() + s;
         }

         Minecraft.getMinecraft().fontRenderer.drawString(s, drawx, drawy, 16777215 | alpha, true);
         if (i == 0) {
            drawy += 2;
         }

         drawy += 10;
      }

   }

   private static void renderTooltipTile(int x, int y, int w, int h, int colorPrimary, int colorOutline, int colorSecondary) {
      drawGradientRect(x - 3, y - 4, w + 6, 1, colorPrimary, colorPrimary);
      drawGradientRect(x - 3, y + h + 3, w + 6, 1, colorPrimary, colorPrimary);
      drawGradientRect(x - 3, y - 3, w + 6, h + 6, colorPrimary, colorPrimary);
      drawGradientRect(x - 4, y - 3, 1, h + 6, colorPrimary, colorPrimary);
      drawGradientRect(x + w + 3, y - 3, 1, h + 6, colorPrimary, colorPrimary);
      drawGradientRect(x - 3, y - 2, 1, h + 4, colorOutline, colorSecondary);
      drawGradientRect(x + w + 2, y - 2, 1, h + 4, colorOutline, colorSecondary);
      drawGradientRect(x - 3, y - 3, w + 6, 1, colorOutline, colorOutline);
      drawGradientRect(x - 3, y + h + 2, w + 6, 1, colorSecondary, colorSecondary);
   }

   private static void drawGradientRect(int x, int y, int w, int h, int color1, int color2) {
      w += x;
      h += y;
      float alpha1 = (float)(color1 >> 24 & 255) / 255.0F;
      float red1 = (float)(color1 >> 16 & 255) / 255.0F;
      float green1 = (float)(color1 >> 8 & 255) / 255.0F;
      float blue1 = (float)(color1 & 255) / 255.0F;
      float alpha2 = (float)(color2 >> 24 & 255) / 255.0F;
      float red2 = (float)(color2 >> 16 & 255) / 255.0F;
      float green2 = (float)(color2 >> 8 & 255) / 255.0F;
      float blue2 = (float)(color2 & 255) / 255.0F;
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA_F(red1, green1, blue1, alpha1);
      tessellator.addVertex((double)w, (double)y, 0.0D);
      tessellator.addVertex((double)x, (double)y, 0.0D);
      tessellator.setColorRGBA_F(red2, green2, blue2, alpha2);
      tessellator.addVertex((double)x, (double)h, 0.0D);
      tessellator.addVertex((double)w, (double)h, 0.0D);
      tessellator.draw();
      GL11.glShadeModel(7424);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glEnable(3553);
   }
}
