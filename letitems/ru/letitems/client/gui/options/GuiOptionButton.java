package ru.letitems.client.gui.options;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import ru.letitems.client.LetSettings;

@SideOnly(Side.CLIENT)
public class GuiOptionButton extends GuiButton {
   private final LetSettings.Options enumOptions;

   public GuiOptionButton(int p_i45011_1_, int p_i45011_2_, int p_i45011_3_, String p_i45011_4_) {
      this(p_i45011_1_, p_i45011_2_, p_i45011_3_, (LetSettings.Options)null, p_i45011_4_);
   }

   public GuiOptionButton(int p_i45012_1_, int p_i45012_2_, int p_i45012_3_, int p_i45012_4_, int p_i45012_5_, String p_i45012_6_) {
      super(p_i45012_1_, p_i45012_2_, p_i45012_3_, p_i45012_4_, p_i45012_5_, p_i45012_6_);
      this.enumOptions = null;
   }

   public GuiOptionButton(int p_i45013_1_, int p_i45013_2_, int p_i45013_3_, LetSettings.Options p_i45013_4_, String p_i45013_5_) {
      super(p_i45013_1_, p_i45013_2_, p_i45013_3_, 150, 20, p_i45013_5_);
      this.enumOptions = p_i45013_4_;
   }

   public LetSettings.Options returnEnumOptions() {
      return this.enumOptions;
   }
}
