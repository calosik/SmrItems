package ru.letitems.client.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.common.tile.TileVendingMachine;
import ru.letitems.common.util.GeneralUtils;

@SideOnly(Side.CLIENT)
public class HintVendingHandler extends Gui {
   private final RenderItem render = new RenderItem();
   private final Minecraft mc;

   public HintVendingHandler(Minecraft mc) {
      this.mc = mc;
   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL
   )
   public void onRenderInfo(Post event) {
      if (!event.isCancelable() && event.type == ElementType.EXPERIENCE && this.mc != null && this.mc.thePlayer != null && this.mc.theWorld != null) {
         MovingObjectPosition mop = GeneralUtils.getMovingObjectPositionFromPlayer(this.mc.theWorld, this.mc.thePlayer, false);
         if (mop != null) {
            TileVendingMachine tile = (TileVendingMachine)GeneralUtils.getTileEntity(this.mc.theWorld, mop.blockX, mop.blockY, mop.blockZ, TileVendingMachine.class);
            if (tile != null) {
               this.draw(tile.getOwnerName(), tile.getSoldItem(), tile.getBoughtItem());
               GeneralClientUtils.bind("textures/gui/icons.png");
            }
         }
      }
   }

   private void drawNumberForItem(FontRenderer fontRenderer, ItemStack stack, int ux, int uy) {
      if (stack != null && stack.stackSize >= 2) {
         String line = "" + stack.stackSize;
         int x = ux + 19 - 2 - fontRenderer.getStringWidth(line);
         int y = uy + 6 + 3;
         GL11.glTranslatef(0.0F, 0.0F, 50.0F);
         this.drawString(fontRenderer, line, x + 1, y + 1, 8947848);
         this.drawString(fontRenderer, line, x, y, 16777215);
         GL11.glTranslatef(0.0F, 0.0F, -50.0F);
      }
   }

   private void drawItemsWithLabel(FontRenderer fontRenderer, String label, int x, int y, ItemStack itemStack, boolean drawDescription) {
      if (drawDescription) {
         if (itemStack != null) {
            String name = itemStack.getDisplayName();
            if (itemStack.getItem() == Items.enchanted_book && itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("StoredEnchantments", 9)) {
               NBTTagList tag = (NBTTagList)itemStack.stackTagCompound.getTag("StoredEnchantments");
               if (tag != null) {
                  StringBuilder text = new StringBuilder();

                  for(int i = 0; i < tag.tagCount(); ++i) {
                     if (i > 2) {
                        text.append(" и еще ").append(tag.tagCount() - i).append("  ");
                        break;
                     }

                     short id = tag.getCompoundTagAt(i).getShort("id");
                     if (Enchantment.enchantmentsList[id] != null) {
                        text.append(Enchantment.enchantmentsList[id].getTranslatedName(tag.getCompoundTagAt(i).getShort("lvl"))).append(", ");
                     }
                  }

                  name = text.substring(0, text.length() - 2);
               }
            }

            this.drawCenteredString(fontRenderer, label + " - " + itemStack.getRarity().rarityColor + name, x, y, 10526880);
         }
      } else {
         int w = fontRenderer.getStringWidth(label);
         x -= w / 2 + 6;
         this.drawString(fontRenderer, label, x, y, 10526880);
         if (itemStack != null) {
            this.drawNumberForItem(fontRenderer, itemStack, x + w + 2, y - 4);
            this.render.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, itemStack, x + w + 2, y - 4);
            GL11.glDisable(2896);
         }
      }

   }

   private void draw(String seller, ItemStack soldItems, ItemStack boughtItems) {
      boolean isSoldEmpty = soldItems == null;
      boolean isBoughtEmpty = boughtItems == null;
      if (!isBoughtEmpty || !isSoldEmpty) {
         ScaledResolution resolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
         FontRenderer fontRenderer = this.mc.fontRenderer;
         int width = resolution.getScaledWidth();
         int height = resolution.getScaledHeight();
         int linesBought = 0;
         int linesSold = 0;
         ItemStack[] items2 = new ItemStack[]{boughtItems, soldItems};
         ItemStack[] var13 = items2;
         int descHeight = items2.length;

         int w;
         int length;
         int tooltipLines;
         int cx;
         for(w = 0; w < descHeight; ++w) {
            ItemStack items = var13[w];
            length = 0;
            int lines = 0;
            if (items != null) {
               tooltipLines = 0;
               List<String> stackTooltip = items.getTooltip(this.mc.thePlayer, false);
               Iterator var21 = stackTooltip.iterator();

               while(var21.hasNext()) {
                  String tooltip = (String)var21.next();
                  if (!tooltip.isEmpty()) {
                     ++tooltipLines;
                     length = Math.max(length, fontRenderer.getStringWidth(tooltip));
                  }
               }

               cx = Math.max(lines, tooltipLines);
               if (items == boughtItems) {
                  linesBought = cx;
               } else {
                  linesSold = cx;
               }
            }
         }

         boolean drawDesc = this.mc.thePlayer.isSneaking();
         descHeight = Math.max(linesBought, linesSold) * 2;
         w = 140;
         int h = 44 + (drawDesc ? descHeight : 0) + (!isBoughtEmpty && !isSoldEmpty ? 16 : 0);
         length = -80 + (drawDesc ? descHeight / 2 : 0) + (!isBoughtEmpty && !isSoldEmpty ? 8 : 0);
         if (drawDesc) {
            if (!isBoughtEmpty && !isSoldEmpty) {
               h -= 6;
               length -= 8;
            }

            w += 200;
         }

         cx = width / 2;
         tooltipLines = cx - w / 2;
         int y = height / 2 - h / 2 + length;
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, -100.0F);
         GL11.glDisable(2896);
         this.drawGradientRect(tooltipLines, y, tooltipLines + w, y + h, -1072689136, -804253680);
         this.drawCenteredString(fontRenderer, seller, cx, y + 8, 16777215);
         if (!isBoughtEmpty && !isSoldEmpty) {
            this.drawItemsWithLabel(fontRenderer, "продает", cx, y + 26, soldItems, drawDesc);
            this.drawItemsWithLabel(fontRenderer, "за", cx, y + 46, boughtItems, drawDesc);
         } else if (!isBoughtEmpty) {
            this.drawItemsWithLabel(fontRenderer, "принимает", cx, y + 26, boughtItems, drawDesc);
         } else {
            this.drawItemsWithLabel(fontRenderer, "отдает бесплатно", cx, y + 26, soldItems, drawDesc);
         }

         GL11.glDisable(2896);
         GL11.glPopMatrix();
      }
   }
}
