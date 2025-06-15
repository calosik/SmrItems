package ru.letitems.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.handler.HintVendingHandler;
import ru.letitems.client.handler.PlacementPreviewHandler;
import ru.letitems.client.model.ModelDakimakura;
import ru.letitems.client.renderer.RenderItemMultiTransparency;
import ru.letitems.client.renderer.TileRenderWorldPortal;
import ru.letitems.client.renderer.commercial.BlockVendingMachineRenderer;
import ru.letitems.client.renderer.commercial.RenderConstants;
import ru.letitems.client.renderer.commercial.TileEntityVendingMachineRenderer;
import ru.letitems.client.renderer.dakimakura.RenderBlockDakimakura;
import ru.letitems.client.renderer.dakimakura.RenderEntityDakimakura;
import ru.letitems.client.renderer.dakimakura.RenderItemDakimakura;
import ru.letitems.client.renderer.doll.ItemRenderDoll;
import ru.letitems.client.renderer.doll.TileRenderDoll;
import ru.letitems.client.texture.DakiTextureManagerClient;
import ru.letitems.common.CommonProxy;
import ru.letitems.common.block.BlockMinePortal;
import ru.letitems.common.dakimakura.pack.IDakiPack;
import ru.letitems.common.entity.EntityDakimakura;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.tile.TileDakimakura;
import ru.letitems.common.tile.TileEntityDoll;
import ru.letitems.common.tile.TileVendingMachine;
import ru.letitems.common.util.GuiType;
import ru.letitems.common.util.module.IModule;
import ru.letitems.modules.blur.Blur;
import ru.letitems.modules.fpsreducer.FPSReducer;
import ru.letitems.modules.main.MenuMod;
import ru.letitems.modules.scene.SceneManager;
import ru.letitems.modules.tooltips.WorldTooltips;

@SideOnly(Side.CLIENT)
public final class ClientProxy extends CommonProxy {
   public LetSettings modSettings;
   private DakiTextureManagerClient dakiTextureManager;
   private int maxGpuTextureSize;

   public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
      GuiType guiType = GuiType.getGuiType(id);
      return guiType == null ? null : guiType.getClientGuiElement(player, world, x, y, z);
   }

   public void initModules(List<IModule> modules) {
      super.initModules(modules);
      modules.add(MenuMod.instance);
      modules.add(Blur.instance);
      modules.add(new SceneManager());
      modules.add(WorldTooltips.instance);
      modules.add(FPSReducer.instance);
   }

   public void preInit(FMLPreInitializationEvent event) {
      this.modSettings = new LetSettings();
      super.preInit(event);
   }

   public void registerEventHandlers() {
      super.registerEventHandlers();
      MinecraftForge.EVENT_BUS.register(new HintVendingHandler(Minecraft.getMinecraft()));
      EventHandlerClient.init();
   }

   public void registerRenderers() {
      super.registerRenderers();
      this.dakiTextureManager = new DakiTextureManagerClient();
      ModelDakimakura modelDakimakura = new ModelDakimakura();
      MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegItems.blockDakimakura), new RenderItemDakimakura(modelDakimakura));
      ClientRegistry.bindTileEntitySpecialRenderer(TileDakimakura.class, new RenderBlockDakimakura(modelDakimakura));
      RenderingRegistry.registerEntityRenderingHandler(EntityDakimakura.class, new RenderEntityDakimakura(modelDakimakura));
      this.maxGpuTextureSize = GL11.glGetInteger(3379);
      new PlacementPreviewHandler(modelDakimakura);
      RenderConstants.vendingRendererId = RenderingRegistry.getNextAvailableRenderId();
      ClientRegistry.bindTileEntitySpecialRenderer(TileVendingMachine.class, new TileEntityVendingMachineRenderer());
      RenderingRegistry.registerBlockHandler(new BlockVendingMachineRenderer());
      RenderItemMultiTransparency wandRenderer = new RenderItemMultiTransparency();
      Item[] var3 = RegItems.itemBuildersWands;
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         Item wandItem = var3[var5];
         MinecraftForgeClient.registerItemRenderer(wandItem, wandRenderer);
      }

      Block[] var7 = RegItems.DollBlocks;
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         Block dollBlock = var7[var5];
         MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(dollBlock), new ItemRenderDoll());
      }

      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDoll.class, new TileRenderDoll());
      if (this.modSettings.renderTimerDim) {
         ClientRegistry.bindTileEntitySpecialRenderer(BlockMinePortal.TileEntityMinePortal.class, new TileRenderWorldPortal());
      }

   }

   public void setDakiList(List<IDakiPack> packs) {
      super.setDakiList(packs);
      this.dakiTextureManager.clear();
   }

   public LetSettings getModSettings() {
      return this.modSettings;
   }

   public DakiTextureManagerClient getDakiTextureManager() {
      return this.dakiTextureManager;
   }

   public int getMaxGpuTextureSize() {
      return this.maxGpuTextureSize;
   }
}
