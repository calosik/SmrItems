package ru.letitems.client.gui.dolls;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollWiz extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-wiz.png");
   private String args;
   private final EntityPlayer player;
   private static final ArrayList<String> listText = new ArrayList(2);
   private static ItemStack itemStackWizBuy;
   private static boolean isFinalBuy = false;
   private final BlockPos blockPos;

   public GuiDollWiz(EntityPlayer player, BlockPos blockPos) {
      this.player = player;
      this.blockPos = blockPos;
      this.args = null;
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      this.drawDollName("Виз", 172, 150, 9070682);
      this.fontRendererObj.drawString("Скупка вещей", this.posX + 166, this.posY + 161, 9408399);
      if (isFinalBuy) {
         this.drawStringWhenSize("Скупка предметов", 1.1F, 29, 85, 13290186);
         this.drawStringWhenSize("На этой неделе Вы продали", 0.75F, 27, 97, 9408399);
         this.drawStringWhenSize("все предметы Виз", 0.75F, 46, 105, 9408399);
      } else if (itemStackWizBuy == null) {
         this.drawStringWhenSize("Скупка предметов", 1.1F, 29, 82, 13290186);
         this.drawStringWhenSize("Сейчас Виз ничего не покупает.", 0.75F, 18, 94, 9408399);
         this.drawStringWhenSize("Запросы на покупку поступают", 0.75F, 19, 102, 9408399);
         this.drawStringWhenSize("на сайте раз в неделю", 0.75F, 36, 110, 9408399);
      } else {
         int countBlockKysEvent = 0;
         ItemStack[] var7 = this.player.inventory.mainInventory;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            ItemStack item = var7[var9];
            if (item != null && item.getItem() == itemStackWizBuy.getItem()) {
               countBlockKysEvent += item.stackSize;
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.enableGUIStandardItemLighting();
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(this.posX + 65), (float)(this.posY + 24), 0.0F);
         GL11.glScalef(2.0F, 2.0F, 2.0F);
         GL11.glEnable(2896);
         GL11.glEnable(32826);
         itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStackWizBuy, 0, 0);
         GL11.glDisable(2896);
         GL11.glPopMatrix();
         RenderHelper.disableStandardItemLighting();
         String itemName = itemStackWizBuy.getDisplayName();
         this.drawStringWhenSize(itemName, 1.0F, 81 - this.fontRendererObj.getStringWidth(itemName) / 2, 64, 9859169);
         String countArtefe = "Предметов в инвентаре " + countBlockKysEvent + " / 800";
         this.drawStringWhenSize(countArtefe, 0.75F, (int)(81.0F - (float)(this.fontRendererObj.getStringWidth(countArtefe) / 2) * 0.75F), 74, 9993328);
         this.drawStringWhenSize("Закупка товаров", 1.25F, 26, 94, 10654606);
         this.drawStringWhenSize("Виз хочет купить товары", 0.75F, 31, 106, 7366756);
         this.drawStringWhenSize("передайте ей 800 единиц", 0.75F, 31, 113, 7366756);
         this.drawStringWhenSize("и посетите её лавочку", 0.75F, 36, 120, 7366756);
         this.drawStringWhenSize("на сайте.", 0.75F, 62, 127, 7366756);
         if (countBlockKysEvent >= 800) {
            boolean isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
            if (isHover) {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
               this.args = "1";
            } else {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
            }

            this.drawStringWhenSize("Передать", 1.1F, 55, 155, isHover ? -1 : 9408399);
         } else {
            this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
         }

         this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
         if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
            this.drawHoveringText(listText, x + 4, y + 4);
         }

      }
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.args != null) {
         if (TimeTuTick.instance.get(TimeTuTick.TypeTime.OTHER) > 0L) {
            this.mc.thePlayer.addChatMessage((new ChatComponentText("Подождите...")).setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GRAY)));
         } else {
            NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.WIZ, this.blockPos, this.args));
            TimeTuTick.instance.register(TimeTuTick.TypeTime.OTHER, 2L);
         }
      }

   }

   public static void buildItem(String item) {
      itemStackWizBuy = null;
      isFinalBuy = false;
      if (item != null && !item.isEmpty()) {
         if (item.equals("-1")) {
            isFinalBuy = true;
            return;
         }

         String[] itemString = item.split(":");
         ItemStack stack = GameRegistry.findItemStack(itemString[0], itemString[1], 1);
         if (stack != null) {
            stack.setItemDamage(itemString.length > 2 ? Integer.parseInt(itemString[2]) : 0);
            itemStackWizBuy = stack;
         }
      }

   }

   static {
      listText.add("§6Кукла Виз");
      listText.add("§7Скупка бесполезных вещей");
   }
}
