package ru.letitems.client.gui.options;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.LetSettings;

@SideOnly(Side.CLIENT)
public final class GuiOptionRender extends GuiScreen {
   private final GuiScreen guiOptions;
   private final LetSettings modSettings;
   private final LetSettings.Options[] options;
   private int lastMouseX = 0;
   private int lastMouseY = 0;

   public GuiOptionRender(GuiOptions guiOptions, LetSettings modSettings, LetSettings.Options[] options) {
      this.guiOptions = guiOptions;
      this.modSettings = modSettings;
      this.options = options;
   }

   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 27, "Готово"));

      for(int i = 0; i < this.options.length; ++i) {
         LetSettings.Options y = this.options[i];
         if (y != null) {
            int x = this.width / 2 - 155 + i % 2 * 160;
            int y1 = this.height / 6 + 22 * (i / 2) - 10;
            if (y.getEnumFloat()) {
               this.buttonList.add(new GuiOptionSlider(y.returnEnumOrdinal(), x, y1, y, this.modSettings));
            } else {
               this.buttonList.add(new GuiOptionButton(y.returnEnumOrdinal(), x, y1, y, this.modSettings.getKeyBinding(y)));
            }
         }
      }

   }

   protected void actionPerformed(GuiButton button) {
      if (button.enabled) {
         if (button.id < 200 && button instanceof GuiOptionButton) {
            this.modSettings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
            button.displayString = this.modSettings.getKeyBinding(LetSettings.Options.getEnumOptions(button.id));
         } else if (button.id == 200) {
            this.modSettings.saveOptions();
            this.mc.displayGuiScreen(this.guiOptions);
         }
      }

   }

   public void drawScreen(int x, int y, float z) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, "Настройки LetItems", this.width / 2, 15, 16777215);
      super.drawScreen(x, y, z);
      if (Math.abs(x - this.lastMouseX) <= 15 && Math.abs(y - this.lastMouseY) <= 15) {
         GuiButton btn = this.getSelectedButton(x, y);
         if (btn != null) {
            String textTooltipe = this.getTooltipLines(btn.displayString);
            if (textTooltipe == null) {
               return;
            }

            String[] lines = textTooltipe.split("#");
            GL11.glDisable(2929);
            int k = 0;
            String[] var8 = lines;
            int k2 = lines.length;

            int i1;
            for(i1 = 0; i1 < k2; ++i1) {
               String s = var8[i1];
               int l = this.fontRendererObj.getStringWidth(s);
               if (l > k) {
                  k = l;
               }
            }

            int j2 = x + 12;
            k2 = y - 12;
            i1 = 8;
            if (lines.length > 1) {
               i1 += 2 + (lines.length - 1) * 10;
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
            String[] var13 = lines;
            int var14 = lines.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               String text = var13[var15];
               this.fontRendererObj.drawStringWithShadow(text, j2, k2, 14540253);
               k2 += 10;
            }
         }
      } else {
         this.lastMouseX = x;
         this.lastMouseY = y;
      }

   }

   public String getTooltipLines(String displayString) {
      int pos = displayString.indexOf(58);
      String var3 = pos < 0 ? displayString : displayString.substring(0, pos);
      byte var4 = -1;
      switch(var3.hashCode()) {
      case -1844252338:
         if (var3.equals("Сворачивание")) {
            var4 = 26;
         }
         break;
      case -1808703268:
         if (var3.equals("Урон и исцеление")) {
            var4 = 3;
         }
         break;
      case -1584245061:
         if (var3.equals("Кэширование")) {
            var4 = 23;
         }
         break;
      case -1515359462:
         if (var3.equals("Древний пергамент")) {
            var4 = 2;
         }
         break;
      case -1459895848:
         if (var3.equals("Отображение в NEI")) {
            var4 = 16;
         }
         break;
      case -1350424947:
         if (var3.equals("Модификация")) {
            var4 = 12;
         }
         break;
      case -1212985463:
         if (var3.equals("OBJ модели")) {
            var4 = 7;
         }
         break;
      case -988952171:
         if (var3.equals("Вкладки")) {
            var4 = 25;
         }
         break;
      case -949163034:
         if (var3.equals("Задержка")) {
            var4 = 15;
         }
         break;
      case -779014140:
         if (var3.equals("Дистанция рендера")) {
            var4 = 5;
         }
         break;
      case -492427847:
         if (var3.equals("Открытие GUI")) {
            var4 = 20;
         }
         break;
      case 11719907:
         if (var3.equals("Торговый аппарат")) {
            var4 = 4;
         }
         break;
      case 217688554:
         if (var3.equals("Громкость звуков")) {
            var4 = 27;
         }
         break;
      case 511109010:
         if (var3.equals("Звук переключения")) {
            var4 = 21;
         }
         break;
      case 626941239:
         if (var3.equals("Вид заданий")) {
            var4 = 24;
         }
         break;
      case 1000087360:
         if (var3.equals("Зелья")) {
            var4 = 17;
         }
         break;
      case 1009503885:
         if (var3.equals("Слоты")) {
            var4 = 18;
         }
         break;
      case 1141638278:
         if (var3.equals("Время вайпа")) {
            var4 = 6;
         }
         break;
      case 1190754802:
         if (var3.equals("Радиус")) {
            var4 = 14;
         }
         break;
      case 1214718605:
         if (var3.equals("Прозрачность")) {
            var4 = 11;
         }
         break;
      case 1265777807:
         if (var3.equals("Тултип")) {
            var4 = 9;
         }
         break;
      case 1281077378:
         if (var3.equals("Косметика")) {
            var4 = 1;
         }
         break;
      case 1367361995:
         if (var3.equals("Текстура")) {
            var4 = 8;
         }
         break;
      case 1835963915:
         if (var3.equals("Дистанция")) {
            var4 = 10;
         }
         break;
      case 1936424298:
         if (var3.equals("Косички")) {
            var4 = 0;
         }
         break;
      case 1943919369:
         if (var3.equals("Размытие")) {
            var4 = 13;
         }
         break;
      case 1998348072:
         if (var3.equals("Редкость")) {
            var4 = 19;
         }
         break;
      case 2068226379:
         if (var3.equals("Прозрачность обложки")) {
            var4 = 22;
         }
      }

      switch(var4) {
      case 0:
         return "Переключение отображения#косичек на других и своём#персонажах";
      case 1:
         return "Переключение отображения#украшений на других и своём#персонажах";
      case 2:
         return "Переключение отображения#титула на других персонажах";
      case 3:
         return "Переключение отображения#эффектов урона и исцеления#на живых существах";
      case 4:
      case 5:
         return "Дистанция отображения";
      case 6:
         return "Отображение времени вайпа в#портале Шахтёрского мира#§4Применяется с перезагрузкой";
      case 7:
         return "Дакимакуры имеют 5 3D моделей, которые отображаются#в зависимости от дистанции.#Вы можете выбрать предел моделей.#Чем меньше значение, тем проще модель.";
      case 8:
         return "Качество и разрешение#текстуры всех дакимакур#§4Применяется с перезагрузкой";
      case 9:
         return "Информация предмета при наведении#на него в мире";
      case 10:
         return "Дистанция до объекта, когда#тултип сработает";
      case 11:
         return "Степень прозрачности окна тултипа";
      case 12:
         return "Отображение названия модификации#в окне тултипа";
      case 13:
         return "Эффект размытия заднего фона";
      case 14:
         return "Степень размытия";
      case 15:
         return "Время вхождения в эффект размытия.#0 - мгновенно.#120 - оптимальный вариант";
      case 16:
         return "Отображаются ли дакимакуры#в списке предметов#§4Применяется с перезагрузкой";
      case 17:
         return "Тип отображения эффектов";
      case 18:
         return "Выделять слоты подходящих вещей";
      case 19:
         return "Выделение редкости косички";
      case 20:
         return "Вариант открытия интерфейса";
      case 21:
         return "Звук открытия инвентаря";
      case 22:
         return "Обложка в главном инвентаре";
      case 23:
         return "Кэшировать ли данные игроков.#Практически ни на что не влияет";
      case 24:
         return "Минимальный размер информации.";
      case 25:
         return "Недоступные вкладки больше не отображаются.#Например, Лавочка Виз и Починка предметов.#§4Включится, если станут доступны";
      case 26:
         return "Снижает FPS до 5 кадров при сворачии окна#игры или отрытии другого приложения";
      case 27:
         return "Сильно снижает громкость звуков при сворачии окна#игры или отрытии другого приложения";
      default:
         return null;
      }
   }

   public GuiButton getSelectedButton(int i, int j) {
      for(int k = 0; k < this.buttonList.size(); ++k) {
         GuiButton btn = (GuiButton)this.buttonList.get(k);
         if (i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btn.width && j < btn.yPosition + btn.height) {
            return btn;
         }
      }

      return null;
   }
}
