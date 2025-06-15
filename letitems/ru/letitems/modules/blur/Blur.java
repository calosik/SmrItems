package ru.letitems.modules.blur;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientProxy;
import ru.letitems.common.LetItems;
import ru.letitems.common.util.module.AbstractModule;

@SideOnly(Side.CLIENT)
public final class Blur extends AbstractModule {
   public static final Blur instance = new Blur();
   private Field _listShaders;
   private long start;
   private boolean enable;
   public int radius;
   public int fade;
   private final ShaderResourcePack dummyPack = new ShaderResourcePack();

   private Blur() {
   }

   public void preInitClient(FMLPreInitializationEvent event) {
      super.preInitClient(event);
      ((List)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), new String[]{"field_110449_ao", "defaultResourcePacks"})).add(this.dummyPack);
      ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this.dummyPack);
      this.switchEnable();
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public void onGuiChange(GuiOpenEvent event) throws JsonException {
      if (this.enable) {
         if (this._listShaders == null) {
            this._listShaders = ReflectionHelper.findField(ShaderGroup.class, new String[]{"field_148031_d", "listShaders"});
         }

         if (Minecraft.getMinecraft().theWorld != null && ShaderLinkHelper.getStaticShaderLinkHelper() != null) {
            GL11.glPushMatrix();
            EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
            if (!er.isShaderActive() && event.gui != null && !(event.gui instanceof GuiChat)) {
               Minecraft mc = Minecraft.getMinecraft();
               er.theShaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), new ResourceLocation("shaders/post/fade_in_blur.json"));
               er.updateShaderGroupSize(mc.displayWidth, mc.displayHeight);
               this.start = System.currentTimeMillis();
            } else if (er.isShaderActive() && event.gui == null) {
               er.deactivateShader();
            }

            GL11.glPopMatrix();
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public void onRenderTick(RenderTickEvent event) {
      if (this.enable) {
         if (event.phase == Phase.END && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
            ShaderGroup sg = Minecraft.getMinecraft().entityRenderer.getShaderGroup();

            try {
               GL11.glPushMatrix();
               List<Shader> shaders = (List)this._listShaders.get(sg);
               Iterator var4 = shaders.iterator();

               while(var4.hasNext()) {
                  Shader s = (Shader)var4.next();
                  ShaderUniform su = s.getShaderManager().func_147991_a("Progress");
                  if (su != null) {
                     su.func_148090_a((float)Math.min((System.currentTimeMillis() - this.start) / (long)this.fade, 1L));
                  }
               }

               GL11.glPopMatrix();
            } catch (Exception var7) {
               Throwables.propagate(var7);
            }
         }

      }
   }

   public void switchEnable() {
      if (((ClientProxy)LetItems.proxy).getModSettings().blurEnabled) {
         MinecraftForge.EVENT_BUS.register(instance);
         FMLCommonHandler.instance().bus().register(instance);
         this.enable = true;
         this.dummyPack.onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());
      } else {
         MinecraftForge.EVENT_BUS.unregister(instance);
         FMLCommonHandler.instance().bus().unregister(instance);
         this.enable = false;
      }

      this.radius = ((ClientProxy)LetItems.proxy).getModSettings().blurRadius;
      this.fade = ((ClientProxy)LetItems.proxy).getModSettings().blurFade;
      if (Minecraft.getMinecraft().theWorld != null) {
         Minecraft.getMinecraft().entityRenderer.deactivateShader();
      }

   }
}
