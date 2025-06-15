package ru.letitems.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import net.minecraft.util.MathHelper;
import org.apache.commons.io.IOUtils;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.DakiImageData;
import ru.letitems.modules.blur.Blur;
import ru.letitems.modules.fpsreducer.FPSReducer;
import ru.letitems.modules.tooltips.WorldTooltips;

public class LetSettings {
   private final File optionsFile;
   public boolean renderHairBand = true;
   public boolean renderCosmetic = true;
   public boolean renderTitle = true;
   public boolean renderDamage = true;
   public int vendingDistance = 12;
   public boolean renderTimerDim = true;
   public boolean blurEnabled = false;
   public int blurRadius = 7;
   public int blurFade = 120;
   public boolean tplEnabled = false;
   public int tplDistance = 8;
   public float tplAlpha = 0.8F;
   public boolean tplHideModName = false;
   public int dakiDistance = 48;
   public int dakiQuality = 4;
   public int dakiTexture = 3;
   public boolean dakiHideNEI = true;
   public int invChangePotions = 1;
   public boolean invHighlightingItems = true;
   public boolean invLastOpened = true;
   public boolean invSound = true;
   public float invCoverAlpha = 0.8F;
   public boolean invMinQuestInfo = false;
   public boolean invHideLockGui = false;
   public boolean invAvatar = true;
   public boolean fpsReducerDisplay = false;
   public boolean fpsReducerSound = false;
   public int otherArchType = 0;

   public LetSettings() {
      this.optionsFile = new File(new File(".." + File.separator + "Assets"), "options.txt");
      this.loadOptions();
      DakiImageData.syncSettings(this.dakiTexture);
   }

   public void setOptionFloatValue(LetSettings.Options settingsOption, float value) {
      if (settingsOption == LetSettings.Options.BLUR_RADIUS) {
         this.blurRadius = (int)value;
         Blur.instance.radius = this.blurRadius;
      } else if (settingsOption == LetSettings.Options.BLUR_FADE) {
         this.blurFade = (int)value;
         Blur.instance.fade = this.blurFade;
      } else if (settingsOption == LetSettings.Options.TPL_DISTANCE) {
         this.tplDistance = (int)value;
      } else if (settingsOption == LetSettings.Options.TPL_ALPHA) {
         this.tplAlpha = value;
         WorldTooltips.instance.syncSettings();
      }

      if (settingsOption == LetSettings.Options.DAKI_DISTANCE) {
         this.dakiDistance = (int)value;
         WorldTooltips.instance.syncSettings();
      } else if (settingsOption == LetSettings.Options.DAKI_QUALITY) {
         this.dakiQuality = (int)value;
      } else if (settingsOption == LetSettings.Options.VENDING_DISTANCE) {
         this.vendingDistance = (int)value;
      } else if (settingsOption == LetSettings.Options.INV_COVER_ALPHA) {
         this.invCoverAlpha = value;
      }

   }

   public void setOptionValue(LetSettings.Options settingsOption, int value) {
      if (settingsOption == LetSettings.Options.HAIR_BAND) {
         this.renderHairBand = !this.renderHairBand;
      } else if (settingsOption == LetSettings.Options.COSMETIC) {
         this.renderCosmetic = !this.renderCosmetic;
      } else if (settingsOption == LetSettings.Options.TITLE) {
         this.renderTitle = !this.renderTitle;
         WorldTooltips.instance.switchEnable();
      } else if (settingsOption == LetSettings.Options.DAMAGE) {
         this.renderDamage = !this.renderDamage;
      } else if (settingsOption == LetSettings.Options.TIMER_DIM) {
         this.renderTimerDim = !this.renderTimerDim;
      } else if (settingsOption == LetSettings.Options.BLUR_ENABLED) {
         this.blurEnabled = !this.blurEnabled;
         Blur.instance.switchEnable();
      } else if (settingsOption == LetSettings.Options.TPL_ENABLED) {
         this.tplEnabled = !this.tplEnabled;
         WorldTooltips.instance.switchEnable();
      } else if (settingsOption == LetSettings.Options.TPL_HIDEMODNAME) {
         this.tplHideModName = !this.tplHideModName;
      } else if (settingsOption == LetSettings.Options.DAKI_HIDENEI) {
         this.dakiHideNEI = !this.dakiHideNEI;
      } else if (settingsOption == LetSettings.Options.DAKI_TEXTURE) {
         ++this.dakiTexture;
         if (this.dakiTexture > 4) {
            this.dakiTexture = 0;
         }

         DakiImageData.syncSettings(this.dakiTexture);
      } else if (settingsOption == LetSettings.Options.INV_POTIONS) {
         ++this.invChangePotions;
         if (this.invChangePotions > 2) {
            this.invChangePotions = 0;
         }
      } else if (settingsOption == LetSettings.Options.INV_HLITEMS) {
         this.invHighlightingItems = !this.invHighlightingItems;
      } else if (settingsOption == LetSettings.Options.INV_LASTGUI) {
         this.invLastOpened = !this.invLastOpened;
      } else if (settingsOption == LetSettings.Options.INV_SOUND) {
         this.invSound = !this.invSound;
      } else if (settingsOption == LetSettings.Options.INV_MINQUESTINFO) {
         this.invMinQuestInfo = !this.invMinQuestInfo;
      } else if (settingsOption == LetSettings.Options.INV_HIDELOCKGUI) {
         this.invHideLockGui = !this.invHideLockGui;
      } else if (settingsOption == LetSettings.Options.INV_AVATAR) {
         this.invAvatar = !this.invAvatar;
      } else if (settingsOption == LetSettings.Options.FPS_REDUCER_DISPLAY) {
         this.fpsReducerDisplay = !this.fpsReducerDisplay;
         FPSReducer.instance.switchEnable();
      } else if (settingsOption == LetSettings.Options.FPS_REDUCER_SOUND) {
         this.fpsReducerSound = !this.fpsReducerSound;
         FPSReducer.instance.switchEnable();
      } else if (settingsOption == LetSettings.Options.OTHER_ARCH_TYPE) {
         this.otherArchType = value;
      }

      this.saveOptions();
   }

   public float getOptionFloatValue(LetSettings.Options settingOption) {
      switch(settingOption) {
      case BLUR_RADIUS:
         return (float)this.blurRadius;
      case BLUR_FADE:
         return (float)this.blurFade;
      case TPL_DISTANCE:
         return (float)this.tplDistance;
      case TPL_ALPHA:
         return this.tplAlpha;
      case DAKI_DISTANCE:
         return (float)this.dakiDistance;
      case DAKI_QUALITY:
         return (float)this.dakiQuality;
      case VENDING_DISTANCE:
         return (float)this.vendingDistance;
      case INV_COVER_ALPHA:
         return this.invCoverAlpha;
      default:
         return 0.0F;
      }
   }

   public boolean getOptionOrdinalValue(LetSettings.Options settingOption) {
      switch(settingOption) {
      case HAIR_BAND:
         return this.renderHairBand;
      case COSMETIC:
         return this.renderCosmetic;
      case TITLE:
         return this.renderTitle;
      case DAMAGE:
         return this.renderDamage;
      case TIMER_DIM:
         return this.renderTimerDim;
      case BLUR_ENABLED:
         return this.blurEnabled;
      case TPL_ENABLED:
         return this.tplEnabled;
      case TPL_HIDEMODNAME:
         return this.tplHideModName;
      case DAKI_HIDENEI:
         return this.dakiHideNEI;
      case INV_HLITEMS:
         return this.invHighlightingItems;
      case INV_LASTGUI:
         return this.invLastOpened;
      case INV_SOUND:
         return this.invSound;
      case INV_MINQUESTINFO:
         return this.invMinQuestInfo;
      case INV_HIDELOCKGUI:
         return this.invHideLockGui;
      case INV_AVATAR:
         return this.invAvatar;
      case FPS_REDUCER_DISPLAY:
         return this.fpsReducerDisplay;
      case FPS_REDUCER_SOUND:
         return this.fpsReducerSound;
      default:
         return false;
      }
   }

   public String getKeyBinding(LetSettings.Options settingOption) {
      String s = settingOption.getEnumString() + ": ";
      if (settingOption == LetSettings.Options.DAKI_TEXTURE) {
         return s + (this.dakiTexture == 0 ? "Минимальное" : (this.dakiTexture == 1 ? "Низкое" : (this.dakiTexture == 2 ? "Среднее" : (this.dakiTexture == 3 ? "Высокое" : "Ня-Кавай"))));
      } else if (settingOption == LetSettings.Options.INV_POTIONS) {
         return s + (this.invChangePotions == 0 ? "Отключить" : (this.invChangePotions == 1 ? "Обычное" : "Минимум"));
      } else if (settingOption == LetSettings.Options.INV_LASTGUI) {
         return s + (this.invLastOpened ? "Последний" : "Основной");
      } else if (settingOption == LetSettings.Options.INV_HIDELOCKGUI) {
         return s + (this.invHideLockGui ? "Скрыть" : "Показывать");
      } else if (settingOption == LetSettings.Options.INV_AVATAR) {
         return s + (this.invAvatar ? "Мой" : "Персонаж");
      } else if (settingOption.getEnumFloat()) {
         float f = this.getOptionFloatValue(settingOption);
         if (settingOption != LetSettings.Options.BLUR_RADIUS && settingOption != LetSettings.Options.BLUR_FADE && settingOption != LetSettings.Options.DAKI_DISTANCE && settingOption != LetSettings.Options.DAKI_QUALITY && settingOption != LetSettings.Options.VENDING_DISTANCE && settingOption != LetSettings.Options.TPL_DISTANCE) {
            if (settingOption != LetSettings.Options.TPL_ALPHA && settingOption != LetSettings.Options.INV_COVER_ALPHA) {
               f = settingOption.normalizeValue(f);
               return f == 0.0F ? s + "ВЫКЛ" : s + (int)(f * 100.0F) + "%";
            } else {
               return s + f;
            }
         } else {
            return s + (int)f;
         }
      } else if (settingOption.getEnumBoolean()) {
         return this.getOptionOrdinalValue(settingOption) ? s + "ВКЛ" : s + "ВЫКЛ";
      } else {
         return s;
      }
   }

   public void loadOptions() {
      try {
         if (!this.optionsFile.exists()) {
            return;
         }

         BufferedReader var1 = new BufferedReader(new FileReader(this.optionsFile));
         String var2 = "";

         while((var2 = var1.readLine()) != null) {
            try {
               String[] split = var2.split(":");
               String var4 = split[0];
               byte var5 = -1;
               switch(var4.hashCode()) {
               case -1973581996:
                  if (var4.equals("blur_fade")) {
                     var5 = 8;
                  }
                  break;
               case -1898078345:
                  if (var4.equals("daki_texture")) {
                     var5 = 15;
                  }
                  break;
               case -1853705405:
                  if (var4.equals("fps_reducer_dispay")) {
                     var5 = 25;
                  }
                  break;
               case -1714746087:
                  if (var4.equals("daki_distance")) {
                     var5 = 13;
                  }
                  break;
               case -1708331514:
                  if (var4.equals("fps_reducer_sound")) {
                     var5 = 26;
                  }
                  break;
               case -1706229297:
                  if (var4.equals("tpl_alpha")) {
                     var5 = 11;
                  }
                  break;
               case -1692005527:
                  if (var4.equals("blur_enabled")) {
                     var5 = 6;
                  }
                  break;
               case -1186434504:
                  if (var4.equals("render_damage")) {
                     var5 = 3;
                  }
                  break;
               case -1116465272:
                  if (var4.equals("inv_cover_alpha")) {
                     var5 = 21;
                  }
                  break;
               case -971007516:
                  if (var4.equals("tpl_distance")) {
                     var5 = 10;
                  }
                  break;
               case -835575020:
                  if (var4.equals("other_arch_type")) {
                     var5 = 27;
                  }
                  break;
               case -513319748:
                  if (var4.equals("tpl_hidemodname")) {
                     var5 = 12;
                  }
                  break;
               case -85734977:
                  if (var4.equals("inv_change_potions")) {
                     var5 = 17;
                  }
                  break;
               case 170953563:
                  if (var4.equals("daki_quality")) {
                     var5 = 14;
                  }
                  break;
               case 277897048:
                  if (var4.equals("render_cosmetic")) {
                     var5 = 1;
                  }
                  break;
               case 289440626:
                  if (var4.equals("tpl_enabled")) {
                     var5 = 9;
                  }
                  break;
               case 385123687:
                  if (var4.equals("inv_avatar")) {
                     var5 = 24;
                  }
                  break;
               case 518178361:
                  if (var4.equals("daki_hide_nei")) {
                     var5 = 16;
                  }
                  break;
               case 543246629:
                  if (var4.equals("render_timer_dim")) {
                     var5 = 4;
                  }
                  break;
               case 606703889:
                  if (var4.equals("inv_hiitems")) {
                     var5 = 18;
                  }
                  break;
               case 635818724:
                  if (var4.equals("inv_last_opened")) {
                     var5 = 19;
                  }
                  break;
               case 663568599:
                  if (var4.equals("vending_distance")) {
                     var5 = 5;
                  }
                  break;
               case 799954185:
                  if (var4.equals("render_hair_band")) {
                     var5 = 0;
                  }
                  break;
               case 1085128303:
                  if (var4.equals("render_title")) {
                     var5 = 2;
                  }
                  break;
               case 1261371398:
                  if (var4.equals("inv_min_quest_info")) {
                     var5 = 22;
                  }
                  break;
               case 1298745014:
                  if (var4.equals("inv_hide_lock_gui")) {
                     var5 = 23;
                  }
                  break;
               case 2106804074:
                  if (var4.equals("blur_radius")) {
                     var5 = 7;
                  }
                  break;
               case 2107067201:
                  if (var4.equals("inv_sound")) {
                     var5 = 20;
                  }
               }

               switch(var5) {
               case 0:
                  this.renderHairBand = split[1].equals("true");
                  break;
               case 1:
                  this.renderCosmetic = split[1].equals("true");
                  break;
               case 2:
                  this.renderTitle = split[1].equals("true");
                  break;
               case 3:
                  this.renderDamage = split[1].equals("true");
                  break;
               case 4:
                  this.renderTimerDim = split[1].equals("true");
                  break;
               case 5:
                  this.vendingDistance = Integer.parseInt(split[1]);
                  break;
               case 6:
                  this.blurEnabled = split[1].equals("true");
                  break;
               case 7:
                  this.blurRadius = Integer.parseInt(split[1]);
                  break;
               case 8:
                  this.blurFade = Integer.parseInt(split[1]);
                  break;
               case 9:
                  this.tplEnabled = split[1].equals("true");
                  break;
               case 10:
                  this.tplDistance = Integer.parseInt(split[1]);
                  break;
               case 11:
                  this.tplAlpha = this.parseFloat(split[1]);
                  break;
               case 12:
                  this.tplHideModName = split[1].equals("true");
                  break;
               case 13:
                  this.dakiDistance = Integer.parseInt(split[1]);
                  break;
               case 14:
                  this.dakiQuality = Integer.parseInt(split[1]);
                  break;
               case 15:
                  this.dakiTexture = MathHelper.clamp_int(Integer.parseInt(split[1]), 0, 4);
                  break;
               case 16:
                  this.dakiHideNEI = split[1].equals("true");
                  break;
               case 17:
                  this.invChangePotions = MathHelper.clamp_int(Integer.parseInt(split[1]), 0, 2);
                  break;
               case 18:
                  this.invHighlightingItems = split[1].equals("true");
                  break;
               case 19:
                  this.invLastOpened = split[1].equals("true");
                  break;
               case 20:
                  this.invSound = split[1].equals("true");
                  break;
               case 21:
                  this.invCoverAlpha = this.parseFloat(split[1]);
                  break;
               case 22:
                  this.invMinQuestInfo = split[1].equals("true");
                  break;
               case 23:
                  this.invHideLockGui = split[1].equals("true");
                  break;
               case 24:
                  this.invAvatar = split[1].equals("true");
                  break;
               case 25:
                  this.fpsReducerDisplay = split[1].equals("true");
                  break;
               case 26:
                  this.fpsReducerSound = split[1].equals("true");
                  break;
               case 27:
                  this.otherArchType = MathHelper.clamp_int(Integer.parseInt(split[1]), 0, 1);
               }
            } catch (Exception var6) {
               LetItems.LOGGER.warn("Skipping bad option: " + var2);
            }
         }

         var1.close();
      } catch (Exception var7) {
         LetItems.LOGGER.error("Failed to load options", var7);
      }

   }

   private float parseFloat(String str) {
      return "true".equals(str) ? 1.0F : ("false".equals(str) ? 0.0F : Float.parseFloat(str));
   }

   public void saveOptions() {
      PrintWriter printwriter = null;

      try {
         printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
         printwriter.println("render_hair_band:" + this.renderHairBand);
         printwriter.println("render_cosmetic:" + this.renderCosmetic);
         printwriter.println("render_title:" + this.renderTitle);
         printwriter.println("render_damage:" + this.renderDamage);
         printwriter.println("render_timer_dim:" + this.renderTimerDim);
         printwriter.println("vending_distance:" + this.vendingDistance);
         printwriter.println("blur_enabled:" + this.blurEnabled);
         printwriter.println("blur_radius:" + this.blurRadius);
         printwriter.println("blur_fade:" + this.blurFade);
         printwriter.println("tpl_enabled:" + this.tplEnabled);
         printwriter.println("tpl_distance:" + this.tplDistance);
         printwriter.println("tpl_alpha:" + this.tplAlpha);
         printwriter.println("tpl_hidemodname:" + this.tplHideModName);
         printwriter.println("daki_distance:" + this.dakiDistance);
         printwriter.println("daki_quality:" + this.dakiQuality);
         printwriter.println("daki_texture:" + this.dakiTexture);
         printwriter.println("daki_hide_nei:" + this.dakiHideNEI);
         printwriter.println("inv_change_potions:" + this.invChangePotions);
         printwriter.println("inv_hiitems:" + this.invHighlightingItems);
         printwriter.println("inv_last_opened:" + this.invLastOpened);
         printwriter.println("inv_sound:" + this.invSound);
         printwriter.println("inv_cover_alpha:" + this.invCoverAlpha);
         printwriter.println("inv_min_quest_info:" + this.invMinQuestInfo);
         printwriter.println("inv_hide_lock_gui:" + this.invHideLockGui);
         printwriter.println("inv_avatar:" + this.invAvatar);
         printwriter.println("fps_reducer_dispay:" + this.fpsReducerDisplay);
         printwriter.println("fps_reducer_sound:" + this.fpsReducerSound);
         printwriter.println("other_arch_type:" + this.otherArchType);
      } catch (Exception var6) {
         LetItems.LOGGER.error("Failed to save options", var6);
      } finally {
         IOUtils.closeQuietly(printwriter);
      }

   }

   public static enum Options {
      HAIR_BAND("Косички", false, true),
      COSMETIC("Косметика", false, true),
      TITLE("Древний пергамент", false, true),
      DAMAGE("Урон и исцеление", false, true),
      TIMER_DIM("Время вайпа", false, true),
      VENDING_DISTANCE("Торговый аппарат", true, false, 2.0F, 64.0F, 1.0F),
      BLUR_ENABLED("Размытие", false, true),
      BLUR_RADIUS("Радиус", true, false, 1.0F, 16.0F, 1.0F),
      BLUR_FADE("Задержка", true, false, 0.0F, 1400.0F, 20.0F),
      TPL_ENABLED("Тултип", false, true),
      TPL_DISTANCE("Дистанция", true, false, 2.0F, 64.0F, 1.0F),
      TPL_ALPHA("Прозрачность", true, false, 0.1F, 1.0F, 0.1F),
      TPL_HIDEMODNAME("Модификация", false, true),
      DAKI_DISTANCE("Дистанция рендера", true, false, 2.0F, 64.0F, 1.0F),
      DAKI_QUALITY("OBJ модели", true, false, 1.0F, 4.0F, 1.0F),
      DAKI_TEXTURE("Текстура", false, false),
      DAKI_HIDENEI("Отображение в NEI", false, true),
      INV_POTIONS("Зелья", false, false),
      INV_HLITEMS("Слоты", false, true),
      INV_LASTGUI("Открытие GUI", false, true),
      INV_SOUND("Звук переключения", false, true),
      INV_COVER_ALPHA("Прозрачность обложки", true, false, 0.1F, 1.0F, 0.1F),
      INV_MINQUESTINFO("Вид заданий", false, true),
      INV_HIDELOCKGUI("Вкладки", false, true),
      INV_AVATAR("Аватар", false, true),
      FPS_REDUCER_DISPLAY("Сворачивание", false, true),
      FPS_REDUCER_SOUND("Громкость звуков", false, true),
      OTHER_ARCH_TYPE("Вид достижений", false, false);

      private final boolean enumFloat;
      private final boolean enumBoolean;
      private final String enumString;
      private final float valueStep;
      private float valueMin;
      private float valueMax;

      public static LetSettings.Options getEnumOptions(int ordinal) {
         LetSettings.Options[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            LetSettings.Options gamesettings$options = var1[var3];
            if (gamesettings$options.returnEnumOrdinal() == ordinal) {
               return gamesettings$options;
            }
         }

         return null;
      }

      private Options(String str, boolean isFloat, boolean isBoolean) {
         this(str, isFloat, isBoolean, 0.0F, 1.0F, 0.0F);
      }

      private Options(String str, boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep) {
         this.enumString = str;
         this.enumFloat = isFloat;
         this.enumBoolean = isBoolean;
         this.valueMin = valMin;
         this.valueMax = valMax;
         this.valueStep = valStep;
      }

      public boolean getEnumFloat() {
         return this.enumFloat;
      }

      public boolean getEnumBoolean() {
         return this.enumBoolean;
      }

      public int returnEnumOrdinal() {
         return this.ordinal();
      }

      public String getEnumString() {
         return this.enumString;
      }

      public float getValueMin() {
         return this.valueMin;
      }

      public float getValueMax() {
         return this.valueMax;
      }

      public void setValueMax(float value) {
         this.valueMax = value;
      }

      public float normalizeValue(float value) {
         return MathHelper.clamp_float((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
      }

      public float denormalizeValue(float p_148262_1_) {
         return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
      }

      public float snapToStepClamp(float value) {
         return MathHelper.clamp_float(this.snapToStep(value), this.valueMin, this.valueMax);
      }

      private float snapToStep(float value) {
         if (this.valueStep > 0.0F) {
            value = this.valueStep * (float)Math.round(value / this.valueStep);
         }

         return value;
      }
   }
}
