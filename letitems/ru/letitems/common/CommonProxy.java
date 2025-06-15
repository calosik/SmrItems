package ru.letitems.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ru.letitems.common.dakimakura.DakiManager;
import ru.letitems.common.dakimakura.pack.IDakiPack;
import ru.letitems.common.entity.EntityDakimakura;
import ru.letitems.common.handler.QuestManager;
import ru.letitems.common.handler.SkillsManager;
import ru.letitems.common.handler.WizStoreManager;
import ru.letitems.common.integration.waila.CompatWaila;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.util.GuiType;
import ru.letitems.common.util.module.IModule;
import ru.letitems.common.util.registry.RegistryUtils;
import ru.letitems.modules.scene.SceneSender;

public class CommonProxy implements IGuiHandler {
   private DakiManager dakimakuraManager;
   private SkillsManager skillsManager;
   private QuestManager questManager;
   private WizStoreManager wizStoreManager;
   private SceneSender webSender;

   public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
      GuiType guiType = GuiType.getGuiType(id);
      return guiType == null ? null : guiType.getServerGuiElement(player, world, x, y, z);
   }

   public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
      return null;
   }

   public void initModules(List<IModule> modules) {
   }

   public void preInit(FMLPreInitializationEvent event) {
      this.dakimakuraManager = new DakiManager();
      this.skillsManager = new SkillsManager();
      this.questManager = new QuestManager();
      this.wizStoreManager = new WizStoreManager();
      RegistryUtils.registerModEntity(EntityDakimakura.class, "entityDakimakura", 1, 64, 120, false);
      Iterator var2 = LetItems.MODULES.iterator();

      while(var2.hasNext()) {
         IModule module = (IModule)var2.next();
         module.preInit(event);
      }

   }

   public void init(FMLInitializationEvent event) {
      NetworkManager.init();
      CompatWaila.init(event);
      this.registerEventHandlers();
      this.registerRenderers();
      Iterator var2 = LetItems.MODULES.iterator();

      while(var2.hasNext()) {
         IModule module = (IModule)var2.next();
         module.init(event);
      }

   }

   public void postInit(FMLPostInitializationEvent event) {
      this.dakimakuraManager.loadPacks();
      Iterator var2 = LetItems.MODULES.iterator();

      while(var2.hasNext()) {
         IModule module = (IModule)var2.next();
         module.postInit(event);
      }

   }

   public void registerEventHandlers() {
   }

   public void registerRenderers() {
   }

   public DakiManager getDakimakuraManager() {
      return this.dakimakuraManager;
   }

   public void setDakiList(List<IDakiPack> packs) {
      this.dakimakuraManager.setDakiList(packs);
   }

   public SceneSender getWebSender() {
      return this.webSender;
   }

   public SkillsManager getSkillsManager() {
      return this.skillsManager;
   }

   public QuestManager getQuestManager() {
      return this.questManager;
   }

   public WizStoreManager getWizStoreManager() {
      return this.wizStoreManager;
   }
}
