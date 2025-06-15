package ru.letitems.common.dakimakura;

import com.google.common.base.Preconditions;
import cpw.mods.fml.common.FMLCommonHandler;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.pack.DakiPackZipFile;
import ru.letitems.common.dakimakura.pack.IDakiPack;
import ru.letitems.common.dakimakura.strict.DakiType;

public class DakiManager implements Closeable {
   private static final String PACK_FOLDER_NAME = "Assets";
   private static final String PACK_FILE_NAME = "t-daki-pack.zip";
   @Nullable
   private final ZipFile packZipFile;
   private final Map<String, IDakiPack> dakiPacksMap = new LinkedHashMap();
   private final List<Daki> dakiIndex = new ArrayList();

   public DakiManager() {
      File rootDirectory = new File("..");
      boolean rootIsDirectory = rootDirectory.exists() ? rootDirectory.isDirectory() : rootDirectory.mkdirs();
      Preconditions.checkArgument(rootIsDirectory, rootDirectory.getAbsolutePath() + " is not directory");
      boolean forceLoad = FMLCommonHandler.instance().getSide().isClient();
      File packFolder = new File(rootDirectory, "Assets");
      boolean packIsDirectory = packFolder.exists() ? packFolder.isDirectory() : !forceLoad || packFolder.mkdirs();
      Preconditions.checkArgument(packIsDirectory, packFolder.getAbsolutePath() + " is not directory");
      File packFile = new File(packFolder, "t-daki-pack.zip");
      ZipFile packZipFile = null;
      if (!packFile.exists()) {
         if (forceLoad) {
            LetItems.LOGGER.warn("{} not exists", new Object[]{packFile.getAbsolutePath()});
         }
      } else if (!packFile.isFile()) {
         LetItems.LOGGER.warn("{} is not file", new Object[]{packFile.getAbsolutePath()});
      } else {
         try {
            packZipFile = new ZipFile(packFile);
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }

      this.packZipFile = packZipFile;
   }

   public void loadPacks() {
      this.dakiPacksMap.clear();
      DakiType[] var1 = DakiType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         DakiType type = var1[var3];
         String packName = type.name();
         DakiPackZipFile dakiPack = new DakiPackZipFile(this.packZipFile, packName);
         this.dakiPacksMap.put(packName, dakiPack);
         dakiPack.loadPack(packName);
      }

   }

   public Daki getDakiFromMap(String dakiDirName) {
      IDakiPack dakiPack = (IDakiPack)this.dakiPacksMap.get(dakiDirName);
      return dakiPack == null ? null : dakiPack.getDaki(dakiDirName);
   }

   public Daki getDakiByGlobalIndex(int globalIndex) {
      if (globalIndex < 0) {
         return null;
      } else {
         return globalIndex >= this.dakiIndex.size() ? null : (Daki)this.dakiIndex.get(globalIndex);
      }
   }

   public List<Daki> getDakiList() {
      if (this.dakiPacksMap.isEmpty()) {
         return Collections.emptyList();
      } else {
         ArrayList<Daki> dakimakuraList = new ArrayList();
         Iterator var2 = this.dakiPacksMap.values().iterator();

         while(var2.hasNext()) {
            IDakiPack dakiPack = (IDakiPack)var2.next();
            dakimakuraList.addAll(dakiPack.getDakisInPack());
         }

         return dakimakuraList;
      }
   }

   public void setDakiList(List<IDakiPack> packs) {
      this.dakiPacksMap.clear();
      Iterator var2 = packs.iterator();

      while(var2.hasNext()) {
         IDakiPack pack = (IDakiPack)var2.next();
         this.dakiPacksMap.put(pack.getResourceName(), pack);
      }

   }

   public int getDakiGlobalIndex(@Nullable Daki daki) {
      return daki == null ? -1 : this.dakiIndex.indexOf(daki);
   }

   public IDakiPack getDakiPack(String packName) {
      return (IDakiPack)this.dakiPacksMap.get(packName);
   }

   public void updateIndex() {
      this.dakiIndex.clear();
      this.dakiIndex.addAll(this.getDakiList());
   }

   public void close() throws IOException {
      if (this.packZipFile != null) {
         this.packZipFile.close();
      }

   }
}
