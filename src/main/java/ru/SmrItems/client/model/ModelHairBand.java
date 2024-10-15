package ru.SmrItems.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelHairBand extends ModelBiped {
   private final int parts;
   private final String get;
   private final int list;
   public static final HashMap<UUID, Byte> hairPlayers = new HashMap();
   private final HashMap<String, ResourceLocation> locationHashMap;

   public ModelHairBand(int get, int parts) {
      this(get, parts, (String)null);
   }

   public ModelHairBand(int get, String sub) {
      this(get, 0, sub);
   }

   private ModelHairBand(int get, int parts, String sub) {
      this.locationHashMap = new HashMap();
      this.get = get + (sub != null ? sub : "");
      this.parts = parts;
      if (get == 6 || get == 10 || get == 11) {
         get = 4;
      }

      IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("smritems:models/hair/" + get + ".obj"));
      this.list = GL11.glGenLists(1);
      GL11.glNewList(this.list, 4864);
      model.renderAll();
      GL11.glEndList();
   }

   public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float headAngleY, float headAngleX, float p_78088_7_) {
      String idTexture = this.get;
      if (this.parts > 0) {
         byte color = this.getPlayerColor((EntityPlayer)entity);
         if (color != 0) {
            idTexture = idTexture + "_" + color;
         }
      }

      ResourceLocation res = this.getModelTexture(idTexture);
      Minecraft.getMinecraft().getTextureManager().bindTexture(res);
      GL11.glPushMatrix();
      GL11.glRotatef(headAngleY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(headAngleX, 1.0F, 0.0F, 0.0F);
      if (entity.isSneaking()) {
         GL11.glDisable(2896);
         GL11.glRotatef(-25.0F, 0.0F, 0.0F, 0.0F);
      }

      GL11.glCallList(this.list);
      GL11.glPopMatrix();
   }

   private byte getPlayerColor(EntityPlayer player) {
      return 0;
   }

   private ResourceLocation getModelTexture(String id) {
      if (!this.locationHashMap.containsKey(id)) {
         this.locationHashMap.put(id, new ResourceLocation("smritems", "textures/models/hairs/" + id + ".png"));
      }

      return (ResourceLocation)this.locationHashMap.get(id);
   }
}
