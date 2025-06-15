package ru.letitems.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientProxy;
import ru.letitems.client.texture.DakiTexture;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.Daki;

@SideOnly(Side.CLIENT)
public class ModelDakimakura extends ModelBase {
   private static final String MODEL_PATH = "models/dakimakura.obj";
   private static final String MODEL_PATH_LOD = "models/dakimakura-lod-%d.obj";
   private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation("letitems", "textures/models/blank.png");
   private final ObjDakimakura DAKIMAKURA_MODEL = ObjDakimakura.loadModel(new ResourceLocation("letitems", "models/dakimakura.obj"));
   private final ObjDakimakura[] DAKIMAKURA_MODEL_LODS = new ObjDakimakura[4];
   private final Profiler profiler;

   public ModelDakimakura() {
      for(int i = 0; i < this.DAKIMAKURA_MODEL_LODS.length; ++i) {
         this.DAKIMAKURA_MODEL_LODS[i] = ObjDakimakura.loadModel(new ResourceLocation("letitems", String.format("models/dakimakura-lod-%d.obj", i + 1)));
      }

      this.profiler = Minecraft.getMinecraft().mcProfiler;
   }

   public void render(Daki daki, double x, double y, double z) {
      double distance = Minecraft.getMinecraft().thePlayer.getDistance(x, y, z);
      this.render(daki, distance);
   }

   public void render(Daki daki, double distance) {
      this.render(daki, MathHelper.floor_double(distance / 16.0D));
   }

   public void render(Daki daki, int lod) {
      lod = MathHelper.clamp_int(lod, 0, 4);
      lod = 4 - Math.min(4 - lod, ((ClientProxy)LetItems.proxy).getModSettings().dakiQuality);
      this.profiler.startSection("lt-daki-texture");
      if (daki == null) {
         Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
      } else {
         DakiTexture dakiTexture = ((ClientProxy)LetItems.proxy).getDakiTextureManager().getTextureForDaki(daki);
         if (dakiTexture.isLoaded()) {
            GL11.glBindTexture(3553, dakiTexture.getGlTextureId());
         } else {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
         }
      }

      this.profiler.endStartSection("lt-daki-model");
      GL11.glPushMatrix();
      GL11.glPushAttrib(8264);
      GL11.glEnable(2977);
      GL11.glCullFace(1029);
      GL11.glEnable(2884);
      GL11.glShadeModel(7425);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glScalef(0.55F, 0.55F, 0.55F);
      GL11.glTranslatef(0.0F, 0.35F, 0.0F);
      GL11.glScalef(-1.0F, -1.0F, 1.0F);
      if (lod == 0) {
         this.DAKIMAKURA_MODEL.render();
      } else {
         this.DAKIMAKURA_MODEL_LODS[lod - 1].render();
      }

      GL11.glPopAttrib();
      GL11.glPopMatrix();
      this.profiler.endSection();
   }
}
