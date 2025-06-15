package ru.letitems.client.gui.screen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.GuiPreRenderScreen;
import ru.letitems.client.gui.inventory.InventoryTab;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketGuid;

@SideOnly(Side.CLIENT)
public final class GuiEnderBonus extends GuiPreRenderScreen {
   private static final ResourceLocation bg = new ResourceLocation("letitems", "textures/gui/bg-inv-ec-f3.png");
   public static int endSelect;
   private int sendId;

   public GuiEnderBonus(int pubEnderBonus) {
      endSelect = pubEnderBonus;
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.sendId = -1;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(bg);
      func_146110_a(this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, (float)this.xSize, (float)this.ySize);
      int xx = x - this.guiLeft;
      int yy = y - this.guiTop;
      this.drawStringWhenSize("Благословение Дракона", 1.25F, 29, 60, 12237498);
      this.drawStringWhenNSSize("Выберите один из бонусов на весь вайп", 1.0F, 29, 71, 10132122);
      this.drawStringWhenSize("Умения", 1.5F, 82, 92, 10439114);
      this.drawStringWhenNSSize("Повышает получаемый опыт", 1.0F, 82, 106, 12237498);
      this.drawStringWhenNSSize("умений на 8%", 1.0F, 82, 114, 12237498);
      if (xx > 27 && xx < 293 && yy > 82 && yy < 133 || endSelect == 1) {
         this.drawGradientRect(this.guiLeft + 28 + 265, this.guiTop + 82 + 47, this.guiLeft + 28, this.guiTop + 82, 277909648, 277909648);
         this.sendId = 1;
      }

      this.drawStringWhenSize("Опыт", 1.5F, 82, 142, 6798709);
      this.drawStringWhenNSSize("Повышает получаемый опыт", 1.0F, 82, 156, 12237498);
      this.drawStringWhenNSSize("персонажа на 10%", 1.0F, 82, 164, 12237498);
      if (xx > 27 && xx < 293 && yy > 133 && yy < 180 || endSelect == 2) {
         this.drawGradientRect(this.guiLeft + 28 + 265, this.guiTop + 133 + 47, this.guiLeft + 28, this.guiTop + 133, 277909648, 277909648);
         this.sendId = 2;
      }

      this.drawStringWhenSize("Урон", 1.5F, 82, 192, 13322838);
      this.drawStringWhenNSSize("Повышает наносимый урон", 1.0F, 82, 206, 12237498);
      this.drawStringWhenNSSize("монстрам на 15%", 1.0F, 82, 214, 12237498);
      if (xx > 27 && xx < 293 && yy > 184 && yy < 232 || endSelect == 3) {
         this.drawGradientRect(this.guiLeft + 28 + 265, this.guiTop + 184 + 47, this.guiLeft + 28, this.guiTop + 184, 277909648, 277909648);
         this.sendId = 3;
      }

      if (xx > 18 && xx < 115 && yy > 43 && yy < 57) {
         this.sendId = 0;
      }

      this.drawStringWhenNSSize("< Назад в инвентарь", 0.75F, 28, 50, this.sendId == 0 ? -1 : 4802889);
   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.sendId == 0) {
         InventoryTab.inventoryTab.openInventoryId(4, false);
      } else if (this.sendId > 0 && endSelect == 0) {
         NetworkManager.sendToServer(new PacketGuid((byte)1, this.sendId));
      }

   }
}
