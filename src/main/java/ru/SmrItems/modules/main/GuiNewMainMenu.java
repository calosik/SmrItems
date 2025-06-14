package ru.SmrItems.modules.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.SmrItems.common.utils.CharacterUtils;
import ru.SmrItems.common.utils.GuiUtils;
import ru.SmrItems.common.utils.TextureInfo;

@SideOnly(Side.CLIENT)
public final class GuiNewMainMenu extends GuiScreen {
   private static final ResourceLocation RESOURCE_BACKGROUND = new ResourceLocation("smritems", "textures/gui/bg.jpg");
   private static final ResourceLocation RESOURCE_LOGO = new ResourceLocation("smritems", "textures/gui/logo.png");
   private static final ResourceLocation RESOURCE_BTN_SERVER = new ResourceLocation("smritems", "textures/gui/btnServer.png");
   private static final ResourceLocation RESOURCE_VK_URI = new ResourceLocation("smritems", "textures/gui/vkontakte.png");
   private static final ResourceLocation RESOURCE_BTN_SINGLE = new ResourceLocation("smritems", "textures/gui/btnSingle.png");
   private static final ResourceLocation RESOURCE_BTN_OPTIONS = new ResourceLocation("smritems", "textures/gui/btnOptions.png");
   private static final ResourceLocation RESOURCE_BTN_EXIT = new ResourceLocation("smritems", "textures/gui/btnExit.png");
   private static final int OFFSET = 6;
   private static final int LOGO_WIDTH = 180;
   private static final int LOGO_HEIGHT = 60;
   private static final int LOGO_Y = 35;
   private static final int SITE_URI_WIDTH = 70;
   private static final int SITE_URI_HEIGHT = 15;
   private static final int VK_URI_WIDTH = 60;
   private static final int VK_URI_HEIGHT = 15;
   private static final int BTN_WIDTH = 200;
   private static final int BTN_HEIGHT = 25;
   private static final int BTN_EXIT_WIDTH = 60;
   private static final int BTN_EXIT_HEIGHT = 15;

   public GuiNewMainMenu() {
      FMLClientHandler.instance().setupServerList();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char key, int keyId) {
   }

   public String getServerIP(String serverName) {
      try {
         URL url = new URL("https://cloud.smrproject.ru/servers.php?serverName=" + URLEncoder.encode(serverName, "UTF-8").replace("%22", ""));
         HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
         conn.setRequestMethod("GET");
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init((KeyManager[])null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
         conn.setSSLSocketFactory(sc.getSocketFactory());
         BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         StringBuilder result = new StringBuilder();

         String line;
         while((line = rd.readLine()) != null) {
            result.append(line);
         }

         rd.close();
         JsonObject json = new JsonParser().parse(result.toString()).getAsJsonObject();
         return json.get("serverIP").getAsString();
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   public void initGui() {
      int btnX = (this.width - 200) / 2;
      byte btnY = 125;
      this.buttonList.add(new GuiImageButton(0, btnX, btnY, 200, 25, RESOURCE_BTN_SERVER));
      this.buttonList.add(new GuiImageButton(1, btnX, btnY + 25 + 6, 200, 25, RESOURCE_BTN_SINGLE));
      this.buttonList.add(new GuiImageButton(2, btnX, btnY + 62, 200, 25, RESOURCE_BTN_OPTIONS));
      this.buttonList.add(new GuiImageButton(3, this.width - 60 - 6, 6, 60, 15, RESOURCE_BTN_EXIT));
      this.buttonList.add(new GuiImageButton(5, this.width - 70 - 6, this.height - 15 - 6, 60, 15, RESOURCE_VK_URI));
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
      case 6:
         Properties properties = new Properties();

         try {
            properties.load(new FileReader("config/smritems/smr.cfg"));
            String serverName = properties.getProperty("ServerName");
            String serverIP = this.getServerIP(serverName);
            if (serverIP != null && serverName != null) {
               FMLClientHandler.instance().connectToServer(this, new ServerData(serverName, serverIP));
            } else {
               this.mc.thePlayer.addChatMessage(new ChatComponentText("Ошибка: не удалось получить IP-адрес сервера."));
            }
         } catch (IOException var5) {
            this.mc.thePlayer.addChatMessage(new ChatComponentText("Ошибка: не удалось загрузить конфигурацию сервера."));
            var5.printStackTrace();
         }

         break;
      case 1:
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
         break;
      case 2:
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
         break;
      case 3:
         this.mc.shutdown();
      case 4:
      default:
         break;
      case 5:
         this.openURI("https://vk.com/public208362951");
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTick) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      TextureInfo textureInfo = CharacterUtils.getBackCharacterInfo();
      GuiUtils.drawFullScreen(this.width, this.height, textureInfo != null && textureInfo.backgroundLocation != null ? textureInfo.backgroundLocation : RESOURCE_BACKGROUND);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      this.drawFullScreen(textureInfo);
      GuiUtils.drawImage((this.width - 180) / 2, 45, 180, 60, RESOURCE_LOGO);
      super.drawScreen(mouseX, mouseY, partialTick);
   }

   private void drawFullScreen(TextureInfo textureInfo) {
      if (textureInfo != null && textureInfo.resourceLocation != null) {
         GuiUtils.drawFullScreen(this.width, this.height, textureInfo.resourceLocation);
      }

   }

   private void openURI(String uri) {
      Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
      if (desktop != null && desktop.isSupported(Action.BROWSE)) {
         try {
            desktop.browse(new URI(uri));
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   class MyTrustManager implements X509TrustManager {
      public void checkClientTrusted(X509Certificate[] chain, String authType) {
      }

      public void checkServerTrusted(X509Certificate[] chain, String authType) {
      }

      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }
   }
}
