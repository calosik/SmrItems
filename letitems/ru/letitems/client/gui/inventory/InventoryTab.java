package ru.letitems.client.gui.inventory;

import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpenBaublesInventory;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Optional.Method;
import java.util.ArrayList;
import java.util.Iterator;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientParams;
import ru.letitems.client.ClientProxy;
import ru.letitems.client.LetSettings;
import ru.letitems.client.gui.screen.GuiCollections;
import ru.letitems.client.gui.screen.GuiLootBox;
import ru.letitems.client.gui.screen.GuiStoreItems;
import ru.letitems.client.gui.screen.GuiTwilightForest;
import ru.letitems.client.gui.screen.GuiWitchery;
import ru.letitems.client.gui.screen.GuiWizStore;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.LetItems;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketGuiGlobalUpdate;
import ru.letitems.common.network.PacketOpenGui;
import ru.letitems.common.network.PacketStoreItems;
import ru.letitems.common.network.mods.PacketTwilightForest;
import ru.letitems.common.network.wizstore.PacketWizStore;
import ru.letitems.common.util.GuiType;
import tconstruct.armor.ArmorProxyClient;
import tconstruct.armor.TinkerArmor;
import tconstruct.client.ArmorControls;

public class InventoryTab {
   public static InventoryTab inventoryTab = new InventoryTab();
   public ArrayList<InventoryTab.BuildTab> tabList = new ArrayList();
   public int lastInventoryKey = 1;

   public void initInventoryTabs(Minecraft mc) {
      boolean isClient = mc.isSingleplayer();
      LetSettings setting = ((ClientProxy)LetItems.proxy).getModSettings();
      inventoryTab.tabList.clear();
      inventoryTab.tabList.add(new InventoryTab.BuildTab(1, "Основной"));
      if (mc.thePlayer != null && mc.playerController.isInCreativeMode() && (mc.thePlayer.getDisplayName().equals("Kirishima") || isClient)) {
         inventoryTab.tabList.add(new InventoryTab.BuildTab(2, "-"));
      }

      inventoryTab.tabList.add(new InventoryTab.BuildTab(3, "Сетка крафта"));
      if (!setting.invHideLockGui || ClientParams.isUnlockAch("attack_repair")) {
         inventoryTab.tabList.add(new InventoryTab.BuildTab(15, "Починка вещей"));
      }

      if (ClientParams.isUnlockAch("ender_chest")) {
         inventoryTab.tabList.add(new InventoryTab.BuildTab(4, "Эндер сундук"));
      }

      if (ClientParams.isOpenedPrimeStore) {
         inventoryTab.tabList.add(new InventoryTab.BuildTab(12, "Prime склад"));
      }

      if (LetItems.loadTConstruct && ArmorProxyClient.armorExtended != null && ArmorProxyClient.armorExtended.inventory[2] != null && ArmorProxyClient.armorExtended.inventory[2].getItem() == TinkerArmor.knapsack) {
         inventoryTab.tabList.add(new InventoryTab.BuildTab(7, "Ранец"));
      }

      if (!isClient) {
         inventoryTab.tabList.add(new InventoryTab.BuildTab(11, "Сундуки", true));
         inventoryTab.tabList.add(new InventoryTab.BuildTab(16, "Склад вещей", true));
         if (!setting.invHideLockGui || ClientParams.isUnlockAch("store_wiz")) {
            inventoryTab.tabList.add(new InventoryTab.BuildTab(9, "Лавочка Виз", true));
         }

         inventoryTab.tabList.add(new InventoryTab.BuildTab(13, "Коллекции", true));
         if (LetItems.loadWitchery) {
            inventoryTab.tabList.add(new InventoryTab.BuildTab(10, "Witchery", true));
         }

         if (LetItems.loadTF) {
            inventoryTab.tabList.add(new InventoryTab.BuildTab(14, "Сумеречный Лес", true));
         }
      }

   }

   public void openInventoryId(int id, boolean isLock) {
      if (id != inventoryTab.lastInventoryKey || !isLock) {
         Minecraft mc = Minecraft.getMinecraft();
         switch(id) {
         case 1:
            mc.displayGuiScreen(new GuiGlobal(mc.thePlayer));
            NetworkManager.sendToServer(new PacketGuiGlobalUpdate());
            break;
         case 2:
            mc.displayGuiScreen(new GuiNewCreative(mc.thePlayer));
            break;
         case 3:
            NetworkManager.sendToServer(new PacketOpenGui(GuiType.CRAFT));
            break;
         case 4:
            if (ClientParams.isUnlockAch("ender_chest")) {
               NetworkManager.sendToServer(new PacketOpenGui(GuiType.ENDER));
            }
            break;
         case 5:
            if (LetItems.loadBaubles) {
               PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory());
            }
            break;
         case 6:
            if (LetItems.loadTConstruct) {
               ArmorControls.openArmorGui();
            }
            break;
         case 7:
            if (LetItems.loadTConstruct && ArmorProxyClient.armorExtended != null && ArmorProxyClient.armorExtended.inventory[2] != null && ArmorProxyClient.armorExtended.inventory[2].getItem() == TinkerArmor.knapsack) {
               ArmorControls.openKnapsackGui();
            }
            break;
         case 8:
            if (LetItems.loadGC) {
               this.openGuiGC();
            }
            break;
         case 9:
            mc.displayGuiScreen(new GuiWizStore(mc.thePlayer));
            NetworkManager.sendToServer(new PacketWizStore(0, (byte)0));
            break;
         case 10:
            if (LetItems.loadWitchery) {
               mc.displayGuiScreen(new GuiWitchery(mc.thePlayer));
            }
            break;
         case 11:
            mc.displayGuiScreen(new GuiLootBox());
            break;
         case 12:
            if (ClientParams.isOpenedPrimeStore) {
               NetworkManager.sendToServer(new PacketOpenGui(GuiType.PRIME_INV));
            }
            break;
         case 13:
            mc.displayGuiScreen(new GuiCollections(mc.thePlayer));
            break;
         case 14:
            if (LetItems.loadTF) {
               NetworkManager.sendToServer(new PacketTwilightForest(0));
               mc.displayGuiScreen(new GuiTwilightForest(mc.thePlayer));
            }
            break;
         case 15:
            NetworkManager.sendToServer(new PacketOpenGui(GuiType.ATTACK_REPAIR));
            break;
         case 16:
            mc.displayGuiScreen(new GuiStoreItems());
            NetworkManager.sendToServer(new PacketStoreItems());
         }

         if (((ClientProxy)LetItems.proxy).getModSettings().invSound) {
            mc.renderViewEntity.worldObj.playSound(mc.renderViewEntity.posX, mc.renderViewEntity.posY, mc.renderViewEntity.posZ, "letitems:nav.but", 0.08F, 1.25F, false);
         }

         inventoryTab.lastInventoryKey = id;
      }

   }

   @Method(
      modid = "GalacticraftCore"
   )
   private void openGuiGC() {
      GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, new Object[0]));
      ClientProxyCore.playerClientHandler.onBuild(0, FMLClientHandler.instance().getClientPlayerEntity());
   }

   public void renderInventoryTabs(GuiScreen screen, Minecraft mc, ResourceLocation resource, FontRenderer fontRendererObj, int x, int y, int guiLeft, int guiTop) {
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (!inventoryTab.tabList.isEmpty()) {
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         mc.getTextureManager().bindTexture(resource);
         int xxx = x - guiLeft;
         int yyy = y - guiTop;
         int xxT = guiLeft;
         int xxB = guiLeft;
         Iterator var13 = inventoryTab.tabList.iterator();

         while(var13.hasNext()) {
            InventoryTab.BuildTab tab = (InventoryTab.BuildTab)var13.next();
            GL11.glDisable(2896);
            boolean isBottom = tab.isPosBottom();
            int xx = isBottom ? xxB : xxT;
            int yy = isBottom ? guiTop + 277 : guiTop;
            boolean isHover = xxx > xx - guiLeft && xxx < 31 + xx - guiLeft && yyy > (!isBottom ? -30 : 254) && yyy < (!isBottom ? 0 : 286);
            boolean isFullHover = isHover || inventoryTab.lastInventoryKey == tab.getId();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover ? 1.0F : 0.5F);
            screen.drawTexturedModalRect(xx, yy - 30, 120, 170, 30, 28);
            int idIcon = tab.getId();
            if (idIcon >= 15) {
               idIcon -= 14;
            }

            int xxIc = (idIcon - 1) * 18;
            int yyIc = !isFullHover ? 18 : 0;
            if (tab.getId() >= 15) {
               yyIc += 36;
            }

            screen.drawTexturedModalRect(xx + 6, yy - 24, xxIc, yyIc, 18, 18);
            if (isHover) {
               fontRendererObj.drawStringWithShadow(tab.getName(), xx, !isBottom ? yy - 40 : yy, 10526880);
               mc.getTextureManager().bindTexture(resource);
            }

            if (isBottom) {
               xxB += 32;
            } else {
               xxT += 32;
            }

            if (tab.getId() == 9 && GuiWizStore.isActive && TimeTuTick.instance.get(TimeTuTick.TypeTime.WIZ_STORE) == 0L) {
               Gui.drawRect(xx + 4 + 3, yy - 26 + 3, xx + 4, yy - 26, isFullHover ? -2132482844 : 820307172);
               GL11.glEnable(3042);
            }
         }
      }

      GL11.glPopMatrix();
   }

   public void mouseClicked(int x, int y, int xx, int yy) {
      if (!inventoryTab.tabList.isEmpty()) {
         x -= xx;
         y -= yy;
         int xxT = xx;
         int xxB = xx;
         Iterator var7 = inventoryTab.tabList.iterator();

         while(var7.hasNext()) {
            InventoryTab.BuildTab tab = (InventoryTab.BuildTab)var7.next();
            boolean isBottom = tab.isPosBottom();
            int xxx = isBottom ? xxB : xxT;
            if (x > xxx - xx && x < 31 + xxx - xx && y > (!isBottom ? -30 : 246) && y < (!isBottom ? 0 : 276)) {
               inventoryTab.openInventoryId(tab.getId(), true);
               break;
            }

            if (isBottom) {
               xxB += 32;
            } else {
               xxT += 32;
            }
         }
      }

   }

   public static class BuildTab {
      private final String name;
      private final int id;
      private final boolean posBottom;

      public BuildTab(int id, String name) {
         this(id, name, false);
      }

      public BuildTab(int id, String name, boolean posBottom) {
         this.id = id;
         this.name = name;
         this.posBottom = posBottom;
      }

      public int getId() {
         return this.id;
      }

      public String getName() {
         return this.name;
      }

      public boolean isPosBottom() {
         return this.posBottom;
      }
   }
}
