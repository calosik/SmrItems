package ru.letitems.modules.scene;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SideOnly(Side.SERVER)
public class SceneSender {
   private final ExecutorService executorService = Executors.newFixedThreadPool(4);

   public void doRequest(File file, String playerName, SceneSender.RequestEndTask requestEndTask) {
   }

   private void sendRequest(File file, String playerName) throws IOException {
   }

   // $FF: synthetic method
   private void lambda$doRequest$0(File file, String playerName, SceneSender.RequestEndTask requestEndTask) {
   }

   public interface RequestEndTask {
      void execute();
   }
}
