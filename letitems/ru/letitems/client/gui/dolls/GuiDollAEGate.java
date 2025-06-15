package ru.letitems.client.gui.dolls;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollAEGate extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-ae2.png");
   private String args;
   private final EntityPlayer player;
   private static final ArrayList<String> listText = new ArrayList(4);
   private final BlockPos blockPos;
   private final BlockDoll.DollType dollType;
   private static Item item;

   public GuiDollAEGate(EntityPlayer player, BlockPos blockPos, BlockDoll.DollType dollType) {
      this.player = player;
      this.blockPos = blockPos;
      this.dollType = dollType;
      this.args = null;
      if (item == null) {
         ItemStack stack = GameRegistry.findItemStack("appliedenergistics2", "item.ItemMultiMaterial", 1);
         item = stack.getItem();
      }

   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      if (this.dollType == BlockDoll.DollType.OKABE) {
         this.drawStringWhenSize("Ринтаро Окабэ", 1.15F, 161, 150, 11555880);
      } else {
         this.drawStringWhenSize("Курису Макисэ", 1.15F, 161, 150, 11555880);
      }

      this.fontRendererObj.drawString("Сингулярность", this.posX + 164, this.posY + 161, 9408399);
      this.drawStringWhenSize("Запутанная Сингулярность", 0.75F, 28, 97, 3429595);
      String text = "Требуется §6" + this.player.experienceLevel + " / 40§r уровней";
      this.drawStringWhenSize(text, 0.75F, 99 - this.fontRendererObj.getStringWidth(text) / 2, 108, 9408399);
      this.drawStringWhenSize("и предмет §9Сингулярность", 0.75F, 31, 117, 9408399);
      boolean hisSing = false;
      ItemStack[] var8 = this.player.inventory.mainInventory;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         ItemStack i = var8[var10];
         if (i != null && i.getItem() == item && i.getItemDamage() == 47) {
            hisSing = true;
            break;
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(Gui.icons);
      this.drawTexturedModalRect(this.posX + 45, this.posY + 131, 10, 64, 71, 5);
      this.drawTexturedModalRect(this.posX + 45, this.posY + 131, 10, 69, MathHelper.clamp_int((int)((float)this.player.experienceLevel / 40.0F * 71.0F), 0, 71), 5);
      GL11.glDisable(3042);
      if (hisSing && this.player.experienceLevel >= 40) {
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
            NetworkManager.sendToServer(new PacketDollsMech(this.dollType, this.blockPos, this.args));
            TimeTuTick.instance.register(TimeTuTick.TypeTime.OTHER, 1L);
         }
      }

   }

   static {
      listText.add("§6Запутанная Сингулярность");
      listText.add("§7Создание квантовой Сингулярности.");
      listText.add("§7Ринтаро Окабэ может создать");
      listText.add("§7с шансом 80%");
   }
}
