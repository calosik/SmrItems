package ru.letitems.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.DecimalFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockAnchor;
import ru.letitems.common.inventory.container.ContainerAnchor;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketAnchor;
import ru.letitems.common.tile.TileAnchor;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiAnchor extends GuiTileBase<TileAnchor, ContainerAnchor> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("letitems", "textures/gui/anchor.png");
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
   private final ItemStack anchorStack;

   public GuiAnchor(ContainerAnchor container) {
      super(container, TEXTURE);
      super.allowUserInput = false;
      this.anchorStack = new ItemStack(((TileAnchor)this.tile).getBlockType(), 1, ((TileAnchor)this.tile).getBlockMetadata());
      this.xSize = 200;
      this.ySize = 147;
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.fontRendererObj.drawString(this.anchorStack.getDisplayName(), 19, 13, 4737096);
      this.fontRendererObj.drawString("Синтезированный Кусь", 49, 35, 10895809);
      this.fontRendererObj.drawString("Работает еще " + DECIMAL_FORMAT.format((double)((ContainerAnchor)this.container).minutesRemaining / 60.0D) + " часов", 49, 45, 6842472);
   }

   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.renderEngine.bindTexture(TEXTURE);
      int h = this.guiLeft;
      int k = this.guiTop;
      this.drawTexturedModalRect(h, k, 0, 0, super.xSize, super.ySize);
      if (((ContainerAnchor)this.container).minutesRemaining > 0) {
         this.drawTexturedModalRect(h + 19, k + 23, 0, 147, (int)((double)((ContainerAnchor)this.container).minutesRemaining / 600.0D * 76.0D), 2);
      }

      if (!((TileAnchor)this.tile).getForcedShutdown()) {
         this.drawTexturedModalRect(h + 160, k + 11, 200, 0, 21, 14);
      }

      if (BlockAnchor.RenderAnchorChunks.instance.warpPosRender != null) {
         this.drawTexturedModalRect(h + 194, k + 3, 200, 14, 3, 3);
      }

      if (this.anchorStack.getItemDamage() == 1) {
         int color = 4737096;
         if (i - this.guiLeft <= 44 || i - this.guiLeft >= 160 || j - this.guiTop <= -22 || j - this.guiTop >= -6) {
            GL11.glEnable(3042);
            color = 6842472;
         }

         this.drawTexturedModalRect(h + 45, k - 20, 0, 149, 114, 14);
         this.fontRendererObj.drawString("Улучшить", h + 78, k - 17, color);
         if (color == 4737096) {
            this.fontRendererObj.drawStringWithShadow("Улучшить до мирового", i + 8, j - 6, -1111);
            this.fontRendererObj.drawStringWithShadow(" - за 600 эссенции - ", i + 8, j + 4, -5555);
         }
      }

   }

   public void drawScreen(int x, int y, float p_73863_3_) {
      super.drawScreen(x, y, p_73863_3_);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      if (xx > 160 && xx < 181 && yy > 11 && yy < 25) {
         this.fontRendererObj.drawStringWithShadow((((TileAnchor)this.tile).getForcedShutdown() ? "В" : "От") + "ключение механизма", x - 88, y - 12, -1);
      } else if (xx > 193 && xx < 198 && yy > 1 && yy < 7) {
         this.fontRendererObj.drawStringWithShadow((BlockAnchor.RenderAnchorChunks.instance.warpPosRender == null ? "В" : "От") + "ключение сетки чанков", x - 88, y - 12, -1);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      x -= this.guiLeft;
      y -= this.guiTop;
      if (x > 160 && x < 181 && y > 11 && y < 25) {
         this.sendPacket(1);
      } else if (x > 193 && x < 198 && y > 1 && y < 7) {
         BlockAnchor.RenderAnchorChunks.instance.init(BlockAnchor.RenderAnchorChunks.instance.warpPosRender == null ? new BlockPos(((TileAnchor)this.tile).xCoord, ((TileAnchor)this.tile).yCoord, ((TileAnchor)this.tile).zCoord) : null);
      } else if (x > 44 && x < 160 && y > -22 && y < -6) {
         this.sendPacket(2);
      }

   }

   private void sendPacket(int id) {
      if (TimeTuTick.instance.get(TimeTuTick.TypeTime.OTHER) > 0L) {
         this.mc.thePlayer.addChatMessage((new ChatComponentText("Подождите...")).setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GRAY)));
      } else {
         NetworkManager.sendToServer(new PacketAnchor(id, ((TileAnchor)this.tile).xCoord, ((TileAnchor)this.tile).yCoord, ((TileAnchor)this.tile).zCoord));
         TimeTuTick.instance.register(TimeTuTick.TypeTime.OTHER, 3L);
      }

   }
}
