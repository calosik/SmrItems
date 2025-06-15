package ru.letitems.client.gui.options;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.LetSettings;

@SideOnly(Side.CLIENT)
public class GuiOptionSlider extends GuiButton {
   private float field_146134_p;
   public boolean field_146135_o;
   private final LetSettings modSettings;
   private final LetSettings.Options field_146133_q;
   private final float field_146132_r;
   private final float field_146131_s;

   public GuiOptionSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, LetSettings.Options p_i45016_4_, LetSettings modSettings) {
      this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F, modSettings);
   }

   public GuiOptionSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, LetSettings.Options p_i45017_4_, float p_i45017_5_, float p_i45017_6_, LetSettings modSettings) {
      super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
      this.field_146134_p = 1.0F;
      this.field_146133_q = p_i45017_4_;
      this.field_146132_r = p_i45017_5_;
      this.field_146131_s = p_i45017_6_;
      this.modSettings = modSettings;
      this.field_146134_p = p_i45017_4_.normalizeValue(modSettings.getOptionFloatValue(p_i45017_4_));
      this.displayString = modSettings.getKeyBinding(p_i45017_4_);
   }

   public int getHoverState(boolean p_146114_1_) {
      return 0;
   }

   protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) {
      if (this.visible) {
         if (this.field_146135_o) {
            this.field_146134_p = MathHelper.clamp_float((float)(p_146119_2_ - (this.xPosition + 4)) / (float)(this.width - 8), 0.0F, 1.0F);
            float f = this.field_146133_q.denormalizeValue(this.field_146134_p);
            this.modSettings.setOptionFloatValue(this.field_146133_q, f);
            this.field_146134_p = this.field_146133_q.normalizeValue(f);
            this.displayString = this.modSettings.getKeyBinding(this.field_146133_q);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawTexturedModalRect(this.xPosition + (int)(this.field_146134_p * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
         this.drawTexturedModalRect(this.xPosition + (int)(this.field_146134_p * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
      }

   }

   public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
      if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
         this.field_146134_p = MathHelper.clamp_float((float)(p_146116_2_ - (this.xPosition + 4)) / (float)(this.width - 8), 0.0F, 1.0F);
         this.modSettings.setOptionFloatValue(this.field_146133_q, this.field_146133_q.denormalizeValue(this.field_146134_p));
         this.displayString = this.modSettings.getKeyBinding(this.field_146133_q);
         this.field_146135_o = true;
         return true;
      } else {
         return false;
      }
   }

   public void mouseReleased(int p_146118_1_, int p_146118_2_) {
      this.field_146135_o = false;
   }
}
