package ru.letitems.client.gui.options;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import ru.letitems.client.ClientProxy;
import ru.letitems.client.LetSettings;
import ru.letitems.common.LetItems;

@SideOnly(Side.CLIENT)
public final class GuiOptions extends GuiScreen {
   private final GuiScreen prevGui;
   private final LetSettings modSettings;
   private final LetSettings.Options[][] options_;

   public GuiOptions(GuiScreen prevGui) {
      this.options_ = new LetSettings.Options[][]{{LetSettings.Options.HAIR_BAND, LetSettings.Options.COSMETIC, LetSettings.Options.TITLE, LetSettings.Options.DAMAGE, LetSettings.Options.VENDING_DISTANCE, LetSettings.Options.TIMER_DIM}, {LetSettings.Options.DAKI_DISTANCE, LetSettings.Options.DAKI_QUALITY, LetSettings.Options.DAKI_TEXTURE, LetSettings.Options.DAKI_HIDENEI}, {LetSettings.Options.TPL_ENABLED, LetSettings.Options.TPL_DISTANCE, LetSettings.Options.TPL_ALPHA, LetSettings.Options.TPL_HIDEMODNAME}, {LetSettings.Options.BLUR_ENABLED, LetSettings.Options.BLUR_RADIUS, LetSettings.Options.BLUR_FADE}, {LetSettings.Options.INV_POTIONS, LetSettings.Options.INV_HLITEMS, LetSettings.Options.INV_LASTGUI, LetSettings.Options.INV_SOUND, LetSettings.Options.INV_COVER_ALPHA, LetSettings.Options.INV_MINQUESTINFO, LetSettings.Options.INV_HIDELOCKGUI, LetSettings.Options.INV_AVATAR}, {LetSettings.Options.FPS_REDUCER_DISPLAY, LetSettings.Options.FPS_REDUCER_SOUND}};
      super.allowUserInput = false;
      this.prevGui = prevGui;
      this.modSettings = ((ClientProxy)LetItems.proxy).getModSettings();
   }

   public void initGui() {
      int w = this.width / 2;
      int h = this.height / 6;
      this.buttonList.add(new GuiButton(1, w - 155, h + 24 - 6, 150, 20, "Рендер деталей"));
      this.buttonList.add(new GuiButton(2, w + 5, h + 24 - 6, 150, 20, "Дакимакуры"));
      this.buttonList.add(new GuiButton(3, w - 155, h + 48 - 6, 150, 20, "Тултип"));
      this.buttonList.add(new GuiButton(4, w + 5, h + 48 - 6, 150, 20, "Размытие"));
      this.buttonList.add(new GuiButton(5, w - 155, h + 72 - 6, 150, 20, "Инвентарь"));
      this.buttonList.add(new GuiButton(6, w + 5, h + 72 - 6, 150, 20, "FPS Reducer"));
      this.buttonList.add(new GuiButton(200, w - 100, h + 120, "Готово"));
   }

   protected void actionPerformed(GuiButton button) {
      if (button.enabled) {
         this.modSettings.saveOptions();
         if (button.id == 200) {
            this.mc.displayGuiScreen(this.prevGui);
         } else {
            this.mc.displayGuiScreen(new GuiOptionRender(this, this.modSettings, this.options_[button.id - 1]));
         }
      }

   }

   public void drawScreen(int i, int i1, float v) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, "Настройки LetItems", this.width / 2, 15, 16777215);
      super.drawScreen(i, i1, v);
   }
}
