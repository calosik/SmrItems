package ru.letitems.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.LetItems;

@SideOnly(Side.CLIENT)
public class ObjDakimakura {
   private final Vector3d[] v;
   private final Vector2f[] vt;
   private final Vector3f[] vn;
   private final ObjDakimakura.Face[] faces;
   private int modelList = -1;

   private ObjDakimakura(Vector3d[] v, Vector2f[] vt, Vector3f[] vn, ObjDakimakura.Face[] faces) {
      this.v = v;
      this.vt = vt;
      this.vn = vn;
      this.faces = faces;
   }

   public void render() {
      if (this.modelList == -1) {
         this.modelList = GLAllocation.generateDisplayLists(1);
         GL11.glNewList(this.modelList, 4864);
         this.renderModel();
         GL11.glEndList();
      }

      GL11.glCallList(this.modelList);
   }

   private void renderModel() {
      Tessellator tess = Tessellator.instance;
      tess.startDrawing(4);

      try {
         ObjDakimakura.Face[] var2 = this.faces;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ObjDakimakura.Face face = var2[var4];
            Vector3d v1 = this.v[face.v1 - 1];
            Vector3d v2 = this.v[face.v2 - 1];
            Vector3d v3 = this.v[face.v3 - 1];
            Vector2f vt1 = this.vt[face.vt1 - 1];
            Vector2f vt2 = this.vt[face.vt2 - 1];
            Vector2f vt3 = this.vt[face.vt3 - 1];
            Vector3f vn1 = this.vn[face.vn1 - 1];
            Vector3f vn2 = this.vn[face.vn2 - 1];
            Vector3f vn3 = this.vn[face.vn3 - 1];
            tess.setTextureUV((double)vt1.x, (double)(-vt1.y));
            tess.setNormal(-vn1.x, -vn1.y, vn1.z);
            tess.addVertex(-v1.x, -v1.y, v1.z);
            tess.setTextureUV((double)vt2.x, (double)(-vt2.y));
            tess.setNormal(-vn2.x, -vn2.y, vn2.z);
            tess.addVertex(-v2.x, -v2.y, v2.z);
            tess.setTextureUV((double)vt3.x, (double)(-vt3.y));
            tess.setNormal(-vn3.x, -vn3.y, vn3.z);
            tess.addVertex(-v3.x, -v3.y, v3.z);
         }
      } catch (Exception var15) {
         var15.printStackTrace();
      }

      tess.draw();
   }

   public static ObjDakimakura loadModel(ResourceLocation resourceLocation) {
      byte[] modelData = loadResource(resourceLocation);
      String modelString = new String(modelData);
      String[] modelLines = modelString.split("\\r?\\n");
      ArrayList<Vector3d> vList = new ArrayList();
      ArrayList<Vector2f> vtList = new ArrayList();
      ArrayList<Vector3f> vnList = new ArrayList();
      ArrayList<ObjDakimakura.Face> faceList = new ArrayList();
      String[] var8 = modelLines;
      int var9 = modelLines.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String line = var8[var10];
         String[] lineSpit = line.split(" ");
         String var13 = lineSpit[0];
         byte var14 = -1;
         switch(var13.hashCode()) {
         case 102:
            if (var13.equals("f")) {
               var14 = 3;
            }
            break;
         case 118:
            if (var13.equals("v")) {
               var14 = 0;
            }
            break;
         case 3768:
            if (var13.equals("vn")) {
               var14 = 2;
            }
            break;
         case 3774:
            if (var13.equals("vt")) {
               var14 = 1;
            }
         }

         switch(var14) {
         case 0:
            vList.add(new Vector3d(Double.parseDouble(lineSpit[1]), Double.parseDouble(lineSpit[2]), Double.parseDouble(lineSpit[3])));
            break;
         case 1:
            vtList.add(new Vector2f(Float.parseFloat(lineSpit[1]), Float.parseFloat(lineSpit[2])));
            break;
         case 2:
            vnList.add(new Vector3f(Float.parseFloat(lineSpit[1]), Float.parseFloat(lineSpit[2]), Float.parseFloat(lineSpit[3])));
            break;
         case 3:
            faceList.add(new ObjDakimakura.Face(lineSpit[1], lineSpit[2], lineSpit[3]));
         }
      }

      Vector3d[] vArray = (Vector3d[])vList.toArray(new Vector3d[0]);
      Vector2f[] vtArray = (Vector2f[])vtList.toArray(new Vector2f[0]);
      Vector3f[] vnArray = (Vector3f[])vnList.toArray(new Vector3f[0]);
      ObjDakimakura.Face[] faces = (ObjDakimakura.Face[])faceList.toArray(new ObjDakimakura.Face[0]);
      return new ObjDakimakura(vArray, vtArray, vnArray, faces);
   }

   private static byte[] loadResource(ResourceLocation resourceLocation) {
      InputStream input = null;
      ByteArrayOutputStream output = null;

      try {
         input = ObjDakimakura.class.getClassLoader().getResourceAsStream("assets/" + resourceLocation.getResourceDomain() + '/' + resourceLocation.getResourcePath());
         if (input != null) {
            output = new ByteArrayOutputStream();
            IOUtils.copy(input, output);
            output.flush();
            byte[] var3 = output.toByteArray();
            return var3;
         }

         LetItems.LOGGER.error(String.format("Error extracting file %s.", resourceLocation.toString()));
      } catch (IOException var7) {
         var7.printStackTrace();
      } finally {
         IOUtils.closeQuietly(input);
         IOUtils.closeQuietly(output);
      }

      return null;
   }

   private static class Face {
      int v1;
      int v2;
      int v3;
      int vt1;
      int vt2;
      int vt3;
      int vn1;
      int vn2;
      int vn3;

      Face(String v1, String v2, String v3) {
         String[] s1 = v1.split("/");
         String[] s2 = v2.split("/");
         String[] s3 = v3.split("/");
         this.v1 = Integer.parseInt(s1[0]);
         this.vt1 = Integer.parseInt(s1[1]);
         this.vn1 = Integer.parseInt(s1[2]);
         this.v2 = Integer.parseInt(s2[0]);
         this.vt2 = Integer.parseInt(s2[1]);
         this.vn2 = Integer.parseInt(s2[2]);
         this.v3 = Integer.parseInt(s3[0]);
         this.vt3 = Integer.parseInt(s3[1]);
         this.vn3 = Integer.parseInt(s3[2]);
      }
   }
}
