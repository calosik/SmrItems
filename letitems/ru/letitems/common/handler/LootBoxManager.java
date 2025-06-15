package ru.letitems.common.handler;

import cpw.mods.fml.common.Loader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.WeightedRandomChestContent;
import org.apache.commons.io.FileUtils;
import ru.letitems.common.util.wizsotre.WizItems;

public final class LootBoxManager {
   private static final File FILE_BOX = new File("./config/lootBox");
   public static List<LootBoxManager.LootBox> listLootBox = new ArrayList();

   static {
      listLootBox.add(new LootBoxManager.LootBox("Косички", 1, 1, (String)null, 1, 3));
      listLootBox.add(new LootBoxManager.LootBox("Куклы", 1, 1, (String)null, 2, 0));
      listLootBox.add(new LootBoxManager.LootBox("Дакимакуры", 1, 2, (String)null, 3, 4));
      listLootBox.add(new LootBoxManager.LootBox("Стандартный", 3, 1, (String)null, 4, 10));
      listLootBox.add(new LootBoxManager.LootBox("Blood Magic", 2, 1, "AWWayofTime", 5, 6));
      listLootBox.add(new LootBoxManager.LootBox("Botania Box", 2, 1, "Botania", 6, 5));
      listLootBox.add(new LootBoxManager.LootBox("Industrial Craft", 2, 1, "IC2", 12, 1));
      listLootBox.add(new LootBoxManager.LootBox("Epic IC2", 2, 3, "IC2", 13, 1));
      listLootBox.add(new LootBoxManager.LootBox("Mya Box", 2, 1, "Forestry", 11, 2));
      listLootBox.add(new LootBoxManager.LootBox("AE-Chest", 2, 2, "appliedenergistics2", 10, 7));
      listLootBox.add(new LootBoxManager.LootBox("ThaumCraft", 2, 1, "Thaumcraft", 7, 8));
      listLootBox.add(new LootBoxManager.LootBox("Tinkerer", 2, 3, "ThaumicTinkerer", 8, 9));
      listLootBox.add(new LootBoxManager.LootBox("Thaumic Horizons", 2, 1, "ThaumicHorizons", 9, 11));
      listLootBox.add(new LootBoxManager.LootBox("Blood Arsenal", 2, 1, "BloodArsenal", 14, 6));
      listLootBox.add(new LootBoxManager.LootBox("EMT", 2, 1, "EMT", 15, 10));
      listLootBox.add(new LootBoxManager.LootBox("DE", 2, 2, "DraconicEvolution", 16, 12));
   }

   public static class LootBox {
      private final String name;
      private final int type;
      private final int count;
      private final int id;
      private final int indexPic;
      private final boolean isActive;
      private WeightedRandomChestContent[] wizItemsMapPush = null;

      public LootBox(String name, int type, int count, String idMod, int id, int indexPic) {
         this.name = name;
         this.type = type;
         this.count = count;
         this.isActive = idMod == null || Loader.isModLoaded(idMod);
         this.id = id;
         this.indexPic = indexPic;
         if (this.isActive) {
            File fileItems = new File(LootBoxManager.FILE_BOX, String.format("%s.json", id));
            if (fileItems.exists()) {
               try {
                  LootBoxManager.LootBoxItems lootBoxItems = (LootBoxManager.LootBoxItems)WizItems.GSON.fromJson(FileUtils.readFileToString(fileItems), LootBoxManager.LootBoxItems.class);
                  this.wizItemsMapPush = lootBoxItems.items;
               } catch (IOException var9) {
                  var9.printStackTrace();
               }
            }
         }

      }

      public String getName() {
         return this.name;
      }

      public int getId() {
         return this.id;
      }

      public int getType() {
         return this.type;
      }

      public int getCount() {
         return this.count;
      }

      public int getIndexPic() {
         return this.indexPic;
      }

      public boolean isActive() {
         return this.isActive && this.wizItemsMapPush != null;
      }

      public WeightedRandomChestContent[] getWizItemsMapPush() {
         return this.wizItemsMapPush;
      }
   }

   public static class LootBoxItems {
      public WeightedRandomChestContent[] items;
   }
}
