package ru.letitems.modules.scene;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import net.minecraft.client.Minecraft;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketSceneSender;
import ru.letitems.common.util.module.AbstractModule;

@SideOnly(Side.CLIENT)
public final class SceneManager extends AbstractModule {
   public static final SceneManager instance = new SceneManager();
   private final String CHANNEL = "SceneBuildChannel";
   private final SimpleNetworkWrapper NETWORK;
   public boolean startSceneBuild;
   private static SceneBuilderHandler sceneBuilder;
   public Future<byte[]> lastFuture;
   private long lastUpdateTimeMills;

   public SceneManager() {
      this.NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("SceneBuildChannel");
      this.startSceneBuild = false;
   }

   public void preInitClient(FMLPreInitializationEvent event) {
      this.NETWORK.registerMessage(PacketScene.Handler.class, PacketScene.class, 0, Side.CLIENT);
      sceneBuilder = new SceneBuilderHandler();
   }

   @SubscribeEvent
   public void event(ClientTickEvent event) {
      if (this.startSceneBuild && event.phase == Phase.END) {
         ++this.lastUpdateTimeMills;
         if (this.lastUpdateTimeMills > 12000L) {
            this.lastUpdateTimeMills = 0L;
            this.startSceneBuild = false;
            FMLCommonHandler.instance().bus().unregister(instance);
         } else if (this.lastFuture != null || this.lastUpdateTimeMills % 480L == 0L) {
            this.sceneBuild();
         }
      }

   }

   public void sceneBuild() {
      Minecraft mc = Minecraft.getMinecraft();
      if (mc.thePlayer != null && mc.theWorld != null && mc.theWorld.getScoreboard() != null && !mc.isIntegratedServerRunning()) {
         try {
            if (sceneBuilder == null) {
               return;
            }

            sceneBuilder.buildScene();
            Future<byte[]> bufferedImageFuture = this.lastFuture != null ? this.lastFuture : (this.lastFuture = sceneBuilder.getScreenshot());
            if (bufferedImageFuture == null || bufferedImageFuture.isCancelled() || !bufferedImageFuture.isDone()) {
               return;
            }

            this.lastFuture = null;
            this.startSceneBuild = false;
            FMLCommonHandler.instance().bus().unregister(instance);
            byte[] image = (byte[])bufferedImageFuture.get();
            List<byte[]> list = new ArrayList();
            int cursor = 0;

            int uid;
            for(boolean var6 = true; cursor < image.length; cursor += uid) {
               uid = Math.min(image.length - cursor, 15000);
               byte[] bytes = new byte[uid];
               System.arraycopy(image, cursor, bytes, 0, uid);
               list.add(bytes);
            }

            uid = (new Random(System.currentTimeMillis())).nextInt(1000);

            for(int i = 0; i < list.size(); ++i) {
               ImageCollector.INSTANCE.addPuzzle(Minecraft.getMinecraft().thePlayer.getCommandSenderName() + uid, i, list.size(), (byte[])list.get(i));
               NetworkManager.sendToServer(new PacketSceneSender(uid, i, list.size(), (byte[])list.get(i)));
            }
         } catch (ExecutionException | InterruptedException var9) {
         }
      }

   }
}
