package ru.letitems.client.gui.dolls;

import com.google.common.base.Joiner;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.GeneralClientUtils;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.items.ItemHairBand;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollKoko extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-koko.png");
   private String args;
   private final EntityPlayer player;
   private static final ArrayList<String> listText = new ArrayList(3);
   public static ArrayList<Integer> hairs;
   private final BlockPos blockPos;
   private int scale;
   private int guiMapY;
   private int maxHeight;

   public GuiDollKoko(EntityPlayer player, BlockPos blockPos) {
      this.player = player;
      this.blockPos = blockPos;
      this.args = null;
      hairs = new ArrayList();
   }

   public void initGui() {
      super.initGui();
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.scale = scaledRes.getScaleFactor();
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      this.drawDollName("Коко", 170, 150, 5993616);
      this.fontRendererObj.drawString("Обмен косичек", this.posX + 165, this.posY + 161, 9408399);
      int k = Mouse.getDWheel();
      if (k < 0) {
         this.guiMapY -= 8;
      } else if (k > 0) {
         this.guiMapY += 8;
      }

      if (this.guiMapY > 0) {
         --this.guiMapY;
      }

      if (this.guiMapY < -this.maxHeight) {
         ++this.guiMapY;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.enableGUIStandardItemLighting();
      GeneralClientUtils.ScissorHelper.startScissor(this.mc, this.scale, this.posX, this.posY + 14, 340, 85);
      ItemStack stackHover = null;
      int kfkfkd = 0;
      int fdsfdsf = 0;
      int j = 0;

      for(int i = 0; i < this.player.inventory.mainInventory.length; ++i) {
         ItemStack stack = this.player.inventory.mainInventory[i];
         if (stack != null && stack.getItem() instanceof ItemHairBand) {
            int l2 = 20 + 33 * kfkfkd;
            int i1 = 20 + fdsfdsf + this.guiMapY;
            if ((j + 1) % 4 == 0) {
               fdsfdsf += 27;
               kfkfkd = 0;
            } else {
               ++kfkfkd;
            }

            if (i1 > this.maxHeight) {
               this.maxHeight = i1 - this.guiMapY - 76;
            }

            boolean hover = xx > l2 - 5 && xx < l2 + 26 && yy > i1 - 6 && yy < i1 + 19;
            if (yy > 98 || yy < 13) {
               hover = false;
            }

            int colorDraw = hover ? 1083216016 : 285212671;
            if (hairs.contains(i)) {
               colorDraw = 1627389951;
            }

            this.drawGradientRect(this.posX + l2 + 25, this.posY + i1 + 20, this.posX + l2 - 4, this.posY + i1 - 4, colorDraw, colorDraw);
            GL11.glEnable(2896);
            GL11.glEnable(32826);
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack, l2 + this.posX + 2, i1 + this.posY);
            GL11.glDisable(2896);
            ++j;
            if (hover) {
               stackHover = stack;
               this.args = i + "";
            }
         }
      }

      GeneralClientUtils.ScissorHelper.endScissor();
      RenderHelper.disableStandardItemLighting();
      this.drawStringWhenSize("Обмен косичек", 1.25F, 34, 106, 15132394);
      this.drawStringWhenSize("Выберите 3 косички из инвентаря", 0.75F, 13, 118, 12369084);
      this.drawStringWhenSize("и обменяйте их на случайный", 0.75F, 22, 125, 12369084);
      this.drawStringWhenSize("предмет - косички, Артефэ", 0.75F, 28, 132, 12369084);
      this.drawStringWhenSize("или обелиски.", 0.75F, 54, 139, 12369084);
      if (hairs.size() != 3) {
         this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
      } else {
         boolean isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
         if (isHover) {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
            this.args = "s";
         } else {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
         }

         this.drawStringWhenSize("Обменять", 1.1F, 55, 155, isHover ? -1 : 9408399);
      }

      if (stackHover != null) {
         ArrayList<String> listText = new ArrayList();
         listText.add(stackHover.getDisplayName());
         this.drawHoveringText(listText, x + 4, y + 4);
      }

      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(GuiDollKoko.listText, x + 4, y + 4);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.args != null) {
         if (this.args.equals("s")) {
            if (TimeTuTick.instance.get(TimeTuTick.TypeTime.OTHER) > 0L) {
               this.mc.thePlayer.addChatMessage((new ChatComponentText("Подождите...")).setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GRAY)));
            } else {
               NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.KOKO, this.blockPos, Joiner.on(",").join(hairs)));
               TimeTuTick.instance.register(TimeTuTick.TypeTime.OTHER, 1L);
            }
         } else {
            int id = Integer.parseInt(this.args);
            if (hairs.contains(id)) {
               Iterator list = hairs.iterator();

               while(list.hasNext()) {
                  int idList = (Integer)list.next();
                  if (idList == id) {
                     list.remove();
                     break;
                  }
               }
            } else {
               hairs.add(id);
               if (hairs.size() > 3) {
                  hairs.remove(0);
               }
            }
         }
      }

   }

   static {
      listText.add("§6Кукла Коко Хекматьяр");
      listText.add("§7Обмен трёх косичек на");
      listText.add("§7случайные вещи");
   }
}
