package ru.letitems.common.integration;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import codechicken.nei.recipe.DefaultOverlayHandler;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import ru.letitems.client.ClientProxy;
import ru.letitems.client.gui.INEIShow;
import ru.letitems.client.gui.inventory.GuiNewCrafting;
import ru.letitems.common.LetItems;
import ru.letitems.common.registry.RegItems;

public class NEILetItemsConfig implements IConfigureNEI {
   public void loadConfig() {
      API.hideItem(new ItemStack(RegItems.itemBuildersWands[3]));
      API.hideItem(new ItemStack(RegItems.itemBuildersWands[4]));
      if (((ClientProxy)LetItems.proxy).getModSettings().dakiHideNEI) {
         API.hideItem(new ItemStack(RegItems.blockDakimakura, 1, 32767));
      }

      API.registerNEIGuiHandler(new NEILetItemsConfig.NEIGuiHandler());
      API.registerGuiOverlay(GuiNewCrafting.class, "crafting", 83, 90);
      API.registerGuiOverlayHandler(GuiNewCrafting.class, new DefaultOverlayHandler(83, 90), "crafting");
   }

   public String getName() {
      return "LetItems-NEIConfig";
   }

   public String getVersion() {
      return "1.0";
   }

   public static class NEIGuiHandler implements INEIGuiHandler {
      public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
         if (gui instanceof INEIShow) {
            currentVisibility.showNEI = false;
         }

         return currentVisibility;
      }

      public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
         return Collections.emptyList();
      }

      public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
         return null;
      }

      public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
         return false;
      }

      public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
         return false;
      }
   }
}
