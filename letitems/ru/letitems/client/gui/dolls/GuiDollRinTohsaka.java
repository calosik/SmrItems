package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollRinTohsaka extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-rin-tohsaka.png");
   private String args;
   private final EntityPlayer player;
   private static final ArrayList<String> listText = new ArrayList(2);
   private static final ItemStack graal;
   private float scaleFlex;
   private final BlockPos blockPos;

   public GuiDollRinTohsaka(EntityPlayer player, BlockPos blockPos) {
      this.player = player;
      this.blockPos = blockPos;
      this.args = null;
      this.scaleFlex = 0.0F;
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      this.drawStringWhenSize("Рин Тосака", 1.15F, 167, 150, 9646154);
      this.fontRendererObj.drawString("Создание Грааля", this.posX + 158, this.posY + 161, 9408399);
      this.drawStringWhenSize("Создание Грааля", 1.25F, 26, 97, 8137022);
      String text = "Требуется §6" + this.player.experienceLevel + " / 100§r уровней";
      this.drawStringWhenSize(text, 0.75F, 99 - this.fontRendererObj.getStringWidth(text) / 2, 111, 9408399);
      this.drawStringWhenSize("и предмет §4Адская Звезда", 0.75F, 31, 119, 9408399);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      RenderHelper.enableGUIStandardItemLighting();
      if (this.scaleFlex < 3.0F) {
         this.scaleFlex += 0.05F;
      } else {
         this.scaleFlex -= 0.05F;
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)(this.posX + 61) - this.scaleFlex / 2.0F, (float)(this.posY + 32) + this.scaleFlex / 2.0F, 0.0F);
      GL11.glScalef(2.25F + 0.08F * this.scaleFlex, 2.25F + 0.08F * this.scaleFlex, 2.25F + 0.08F * this.scaleFlex);
      GL11.glEnable(2896);
      GL11.glEnable(32826);
      itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), graal, 0, 0);
      GL11.glDisable(2896);
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(Gui.icons);
      this.drawTexturedModalRect(this.posX + 45, this.posY + 131, 10, 64, 71, 5);
      this.drawTexturedModalRect(this.posX + 45, this.posY + 131, 10, 69, MathHelper.clamp_int((int)((float)this.player.experienceLevel / 100.0F * 71.0F), 0, 71), 5);
      GL11.glDisable(3042);
      if (this.player.inventory.hasItem(Items.nether_star) && this.player.experienceLevel >= 100) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         boolean isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
         if (isHover) {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
            this.args = "1";
         } else {
            this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
         }

         this.drawStringWhenSize("Создать", 1.1F, 58, 155, isHover ? -1 : 9408399);
      } else {
         this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
      }

      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(listText, x + 4, y + 4);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.args != null) {
         if (TimeTuTick.instance.get(TimeTuTick.TypeTime.OTHER) > 0L) {
            this.mc.thePlayer.addChatMessage((new ChatComponentText("Подождите...")).setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GRAY)));
         } else {
            NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.RIN_TOHSAKA, this.blockPos, this.args));
            TimeTuTick.instance.register(TimeTuTick.TypeTime.OTHER, 1L);
         }
      }

   }

   static {
      graal = new ItemStack(RegItems.itemSiteCrafting, 1, 1);
      listText.add("§6Кукла Рин Тосака");
      listText.add("§7Создание Святого Грааля");
   }
}
