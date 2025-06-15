package ru.letitems.client;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.gui.inventory.InventoryTab;
import ru.letitems.client.handler.AchiewmentHandler;
import ru.letitems.client.model.ModelHairBand;
import ru.letitems.client.renderer.particles.RenderDamageParticles;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.LetItems;
import ru.letitems.common.inventory.ExtendedPlayer;
import ru.letitems.common.items.ItemBuildersWand;
import ru.letitems.common.items.ItemCosmetic;
import ru.letitems.common.items.ItemHairBand;
import ru.letitems.common.items.ItemScrollTitle;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketInvisibility;
import ru.letitems.common.util.BlockPos;
import ru.letitems.common.util.GeneralUtils;
import ru.letitems.common.util.InventoryUtils;

@SideOnly(Side.CLIENT)
public final class EventHandlerClient {
   private static final Field FIELD_MAIN_MODEL = ReflectionHelper.findField(RendererLivingEntity.class, new String[]{"field_77045_g", "mainModel"});
   private static final Method METHOD_BIND_TEXTURE = ReflectionHelper.findMethod(Render.class, (Object)null, new String[]{"func_110776_a", "bindTexture"}, new Class[]{ResourceLocation.class});
   private final Minecraft mc = Minecraft.getMinecraft();
   private long detected;
   public final AchiewmentHandler achiewmentHandler;
   public boolean enchHair;
   public static EventHandlerClient instance;
   private static LetSettings modSettings;
   private int elapsedTicks;

   public EventHandlerClient() {
      this.achiewmentHandler = new AchiewmentHandler(this.mc);
      this.enchHair = false;
   }

   public static void init() {
      EventHandlerClient eventHandlerClient = new EventHandlerClient();
      FMLCommonHandler.instance().bus().register(eventHandlerClient);
      MinecraftForge.EVENT_BUS.register(eventHandlerClient);
      instance = eventHandlerClient;
      modSettings = ((ClientProxy)LetItems.proxy).getModSettings();
   }

   @SubscribeEvent
   public void tickEnd(ClientTickEvent event) {
      if (event.phase == Phase.END && this.mc.theWorld != null) {
         if (this.elapsedTicks % 20 == 0) {
            TimeTuTick.instance.update();
         }

         if (this.elapsedTicks % 200 == 0) {
            ModelHairBand.hairPlayers.clear();
         }

         ++this.elapsedTicks;
      }

   }

   @SubscribeEvent
   public void renderTick(RenderTickEvent event) {
      if (event.phase != Phase.START && Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         this.achiewmentHandler.updateAchiewmentWindow();
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onOpenGui(GuiOpenEvent event) {
      if (event.gui instanceof GuiInventory) {
         event.setCanceled(true);
         InventoryTab.inventoryTab.openInventoryId(modSettings.invLastOpened ? InventoryTab.inventoryTab.lastInventoryKey : 1, false);
      }

   }

   @SubscribeEvent
   public void displayDamageIndicator(LivingUpdateEvent event) {
      if (modSettings.renderDamage) {
         EntityLivingBase entity = event.entityLiving;
         if (entity.isInvisible() || !entity.worldObj.isRemote) {
            return;
         }

         if (this.mc.thePlayer.getDistanceSqToEntity(entity) <= 576.0D) {
            int currentHealth = (int)Math.ceil((double)entity.getHealth());
            if (entity.getEntityData().hasKey("health")) {
               int entityHealth = ((NBTTagInt)entity.getEntityData().getTag("health")).func_150287_d();
               if (entityHealth != currentHealth) {
                  World world = entity.worldObj;
                  int age = this.mc.gameSettings.particleSetting == 2 ? 10 : 18;
                  RenderDamageParticles damageIndicator = new RenderDamageParticles(entityHealth - currentHealth, world, entity.posX, entity.posY + (double)entity.height, entity.posZ, world.rand.nextGaussian() * 0.02D, 0.5D, world.rand.nextGaussian() * 0.02D, age);
                  Minecraft.getMinecraft().effectRenderer.addEffect(damageIndicator);
               }
            }

            entity.getEntityData().setTag("health", new NBTTagInt(currentHealth));
         }
      }

   }

   @SubscribeEvent
   public void itemTooltipEvent(ItemTooltipEvent event) {
      if (event.itemStack != null && event.entityPlayer != null && GeneralUtils.isItemCurse(event.itemStack)) {
         event.toolTip.add(EnumChatFormatting.GOLD + "Предмет проклят");
      }

   }

   @SubscribeEvent
   public void onRenderHelmet(SetArmorModel event) {
      if (event.slot == 3) {
         EntityPlayer player = event.entityPlayer;
         ItemStack stackInInventory = player.getEquipmentInSlot(event.slot);
         if (stackInInventory != null && stackInInventory.getItem() instanceof ItemHairBand && !modSettings.renderHairBand) {
            event.result = 0;
         } else {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getExtendedPlayer(player);
            ItemStack hairStack = extendedPlayer.getHair();
            if (hairStack != null) {
               Item item = hairStack.getItem();
               if (item instanceof ItemArmor) {
                  if (item instanceof ItemHairBand && !modSettings.renderHairBand) {
                     event.result = 0;
                     return;
                  }

                  int slot = 3 - event.slot;

                  try {
                     METHOD_BIND_TEXTURE.invoke(event.renderer, RenderBiped.getArmorResource(player, hairStack, slot, (String)null));
                  } catch (InvocationTargetException | IllegalAccessException var12) {
                     throw Throwables.propagate(var12);
                  }

                  ModelBiped model = event.renderer.modelArmorChestplate;
                  model.bipedHead.showModel = true;
                  model.bipedHeadwear.showModel = true;
                  model.bipedBody.showModel = false;
                  model.bipedRightArm.showModel = false;
                  model.bipedLeftArm.showModel = false;
                  model.bipedRightLeg.showModel = false;
                  model.bipedLeftLeg.showModel = false;
                  model = ForgeHooksClient.getArmorModel(player, hairStack, slot, model);
                  event.renderer.setRenderPassModel(model);

                  ModelBase mainModel;
                  try {
                     mainModel = (ModelBase)FIELD_MAIN_MODEL.get(event.renderer);
                  } catch (IllegalAccessException var11) {
                     throw Throwables.propagate(var11);
                  }

                  model.onGround = mainModel.onGround;
                  model.isRiding = mainModel.isRiding;
                  model.isChild = mainModel.isChild;
                  GL11.glColor3f(1.0F, 1.0F, 1.0F);
                  if (this.enchHair) {
                     GL11.glDisable(2896);
                  }

                  event.result = this.enchHair ? 15 : 1;
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void buildersWandRender(DrawBlockHighlightEvent event) {
      if (event.currentItem != null && event.currentItem.getItem() instanceof ItemBuildersWand) {
         Block block = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
         if (block != Blocks.air) {
            List<BlockPos> blocks = ((ItemBuildersWand)event.currentItem.getItem()).getPotentialBlocks(event.player, event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ, event.target.sideHit, InventoryUtils.getAllPlayerInventories(event.player));
            if (!blocks.isEmpty()) {
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.35F);
               GL11.glLineWidth(3.0F);
               GL11.glDisable(3553);
               GL11.glDisable(2929);
               GL11.glDepthMask(false);
               float px = (float)(event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * (double)event.partialTicks);
               float py = (float)(event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * (double)event.partialTicks);
               float pz = (float)(event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * (double)event.partialTicks);
               GL11.glTranslatef(-px, -py, -pz);
               Iterator var7 = blocks.iterator();

               while(var7.hasNext()) {
                  BlockPos temp = (BlockPos)var7.next();
                  drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBox((double)temp.x, (double)temp.y, (double)temp.z, (double)(temp.x + 1), (double)(temp.y + 1), (double)(temp.z + 1)));
               }

               GL11.glDepthMask(true);
               GL11.glEnable(3553);
               GL11.glDisable(3042);
               GL11.glEnable(2929);
               GL11.glTranslatef(px, py, pz);
               event.setCanceled(true);
            }
         }
      }

   }

   private static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
      Tessellator tesselator = Tessellator.instance;
      tesselator.startDrawing(3);
      tesselator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
      tesselator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
      tesselator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
      tesselator.draw();
      tesselator.startDrawing(3);
      tesselator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
      tesselator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
      tesselator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
      tesselator.draw();
      tesselator.startDrawing(1);
      tesselator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
      tesselator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
      tesselator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
      tesselator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
      tesselator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
      tesselator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
      tesselator.draw();
   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL
   )
   public void onRenderWorldLast(RenderWorldLastEvent event) {
      EntityClientPlayerMP playerMP = this.mc.thePlayer;
      if (!playerMP.isDead && playerMP.isInvisible()) {
         List<EntityPlayer> lists = this.mc.theWorld.playerEntities;
         if (lists.isEmpty()) {
            return;
         }

         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         Iterator var4 = lists.iterator();

         while(var4.hasNext()) {
            EntityPlayer object = (EntityPlayer)var4.next();
            if (this.isApplicableForCircle(object)) {
               this.handlePlayerCircle(object, event.partialTicks);
            }
         }

         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glDisable(3042);
         GL11.glEnable(3553);
         GL11.glPopMatrix();
      }

   }

   private boolean isApplicableForCircle(EntityPlayer player) {
      if (player != null && player != this.mc.thePlayer && !player.isInvisible() && !player.isInvisibleToPlayer(this.mc.thePlayer) && !player.isDead && !player.isPlayerSleeping() && !player.isRiding() && this.mc.thePlayer.canEntityBeSeen(player)) {
         Iterator var2 = this.mc.getNetHandler().playerInfoList.iterator();

         GuiPlayerInfo guiPlayerInfo;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            guiPlayerInfo = (GuiPlayerInfo)var2.next();
         } while(!guiPlayerInfo.name.equals(player.getGameProfile().getName()));

         return true;
      } else {
         return false;
      }
   }

   private void handlePlayerCircle(EntityPlayer player, float partialTicks) {
      if ((double)this.mc.thePlayer.getDistanceToEntity(player) <= 8.0D && this.detected < System.currentTimeMillis()) {
         NetworkManager.sendToServer(new PacketInvisibility());
         this.detected = System.currentTimeMillis() + 5000L;
      } else {
         double var10000 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
         RenderManager var10001 = RenderManager.instance;
         double posX = var10000 - RenderManager.renderPosX;
         var10000 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
         var10001 = RenderManager.instance;
         double posY = var10000 - RenderManager.renderPosY;
         var10000 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
         var10001 = RenderManager.instance;
         double posZ = var10000 - RenderManager.renderPosZ;
         GL11.glPushMatrix();
         GL11.glLineWidth(4.85F);
         GL11.glBegin(2);
         double tanFactor = Math.tan(0.017453292519943295D);
         double cosFactor = Math.cos(0.017453292519943295D);
         double xPosition = 3.5D;
         double zPosition = 7.0D;

         for(int i = 0; (double)i <= 360.0D; ++i) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.35F);
            GL11.glVertex3d(posX + xPosition, posY + 0.25D, posZ + zPosition);
            double tanX = -zPosition;
            double var28 = xPosition + tanX * tanFactor;
            zPosition += xPosition * tanFactor;
            xPosition = var28 * cosFactor;
            zPosition *= cosFactor;
         }

         GL11.glEnd();
         GL11.glPopMatrix();
      }

   }

   @SubscribeEvent
   public void onPlayerRender(Post event) {
      if (modSettings.renderCosmetic && !event.entityPlayer.isInvisible() && !event.entityPlayer.isInvisibleToPlayer(this.mc.thePlayer)) {
         EntityPlayer player = event.entityPlayer;
         ExtendedPlayer extendedPlayer = ExtendedPlayer.getExtendedPlayer(player);

         for(int i = 1; i <= 2; ++i) {
            ItemStack itemStack = extendedPlayer.getStackInSlot(i);
            if (itemStack != null) {
               Item item = itemStack.getItem();
               if (item instanceof ItemCosmetic) {
                  if (((ItemCosmetic)item).hasRenderHead(itemStack.getItemDamage()) < 0) {
                     float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
                     float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
                     float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;
                     GL11.glPushMatrix();
                     GL11.glRotatef(yawOffset, 0.0F, -1.0F, 0.0F);
                     GL11.glRotatef(yaw - 270.0F, 0.0F, 1.0F, 0.0F);
                     GL11.glRotatef(pitch, 0.0F, 0.0F, 1.0F);
                     GL11.glPushMatrix();
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     ((ItemCosmetic)item).onPlayerBaubleRender(itemStack.getItemDamage(), player, true);
                     GL11.glPopMatrix();
                  } else {
                     GL11.glPushMatrix();
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     ((ItemCosmetic)item).onPlayerBaubleRender(itemStack.getItemDamage(), player, false);
                  }

                  GL11.glPopMatrix();
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void render(Pre evt) {
      if (this.mc.thePlayer != null && !this.mc.thePlayer.isDead && this.mc.thePlayer.getDisplayName().equals("Kirishima")) {
         if (evt.entity instanceof EntityPlayer) {
            evt.setCanceled(true);
            MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Specials.Post(evt.entity, evt.renderer, evt.x, evt.y, evt.z));
         }

      }
   }

   @SubscribeEvent
   public void onRenderWorldLastTw(RenderWorldLastEvent event) {
      if (Minecraft.isGuiEnabled()) {
         if (this.mc.thePlayer != null && !this.mc.thePlayer.isDead && this.mc.thePlayer.getDisplayName().equals("Kirishima")) {
            Iterator var2 = this.mc.theWorld.playerEntities.iterator();

            while(var2.hasNext()) {
               EntityPlayer object = (EntityPlayer)var2.next();
               if (this.isApplicableForCircle(object)) {
                  this.renderHealthBar(object, event.partialTicks);
               }
            }

         }
      }
   }

   public void renderHealthBar(EntityPlayer entityPlayer, float partialTicks) {
      float distance = this.mc.thePlayer.getDistanceToEntity(entityPlayer);
      if (distance <= 36.0F) {
         float maxHealth = entityPlayer.getMaxHealth();
         if (maxHealth > 0.0F) {
            double x = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)partialTicks;
            double y = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)partialTicks;
            double z = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)partialTicks;
            float scale = 0.026666673F;
            float health = Math.min(maxHealth, entityPlayer.getHealth());
            GL11.glPushMatrix();
            int barHeight = 1;
            float size = 18.0F;
            int r = 243;
            int g = 69;
            int b = 69;
            int colorName = 16777215;
            int opacity = 135;
            boolean isSneak = entityPlayer.isSneaking();
            String scrollTitle = null;
            if (modSettings.renderTitle && !isSneak && distance <= 12.0F) {
               ItemStack itemStack = ExtendedPlayer.getExtendedPlayer(entityPlayer).getStackInSlot(3);
               if (itemStack != null && itemStack.getItem() instanceof ItemScrollTitle) {
                  scrollTitle = ItemScrollTitle.getTitle(itemStack);
                  if (!StringUtils.isNullOrEmpty(scrollTitle)) {
                     scrollTitle = "<" + scrollTitle + ">";
                  }
               }

               scrollTitle = "<Убивший Дракона>";
            }

            if (isSneak) {
               y -= 0.3D;
               colorName = 553648127;
               opacity = 28;
               b = 0;
               g = 0;
               r = 0;
               scrollTitle = "";
            }

            GL11.glTranslatef((float)(x - RenderManager.renderPosX), (float)(y - RenderManager.renderPosY + (double)entityPlayer.height + 0.35D), (float)(z - RenderManager.renderPosZ));
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-scale, -scale, scale);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Tessellator tessellator = Tessellator.instance;
            GL11.glTranslatef(0.0F, 2.0F, 0.0F);
            boolean isTitle = StringUtils.isNullOrEmpty(scrollTitle);
            float s = !isTitle ? 0.5F : 0.75F;
            if (distance > 12.0F) {
               s = 1.0F;
            }

            String name = entityPlayer.getDisplayName();
            float namel = (float)this.mc.fontRenderer.getStringWidth(name) * s;
            if (namel + 20.0F > size * 2.0F) {
               size = namel / 2.0F + 10.0F;
            }

            float healthSize = size * (health / maxHealth);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA(192, 192, 192, opacity - 20);
            tessellator.addVertex((double)(-size), 0.0D, 0.0D);
            tessellator.addVertex((double)(-size), (double)barHeight, 0.0D);
            tessellator.addVertex((double)size, (double)barHeight, 0.0D);
            tessellator.addVertex((double)size, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA(r, g, b, opacity);
            tessellator.addVertex((double)(-size), 0.0D, 0.0D);
            tessellator.addVertex((double)(-size), (double)barHeight, 0.0D);
            tessellator.addVertex((double)(healthSize * 2.0F - size), (double)barHeight, 0.0D);
            tessellator.addVertex((double)(healthSize * 2.0F - size), 0.0D, 0.0D);
            tessellator.draw();
            GL11.glPushMatrix();
            GL11.glTranslatef(-namel / 2.0F, -5.5F, 0.0F);
            GL11.glScalef(s, s, s);
            if (!isTitle) {
               int j = (int)((float)this.mc.fontRenderer.getStringWidth(scrollTitle) * s);
               double padding = 4.0D;
               GL11.glPushMatrix();
               GL11.glTranslatef(namel, 0.0F, 0.0F);
               tessellator.startDrawingQuads();
               tessellator.setColorRGBA(0, 0, 0, 40);
               tessellator.addVertex((double)(-j) - padding, -2.0D, 0.0D);
               tessellator.addVertex((double)(-j) - padding, 5.0D + padding, 0.0D);
               tessellator.addVertex((double)j + padding, 5.0D + padding, 0.0D);
               tessellator.addVertex((double)j + padding, -2.0D, 0.0D);
               tessellator.draw();
               GL11.glPopMatrix();
               GL11.glEnable(3553);
               this.mc.fontRenderer.drawString(scrollTitle, (int)(namel - (float)j), 0, 14737632);
               this.mc.fontRenderer.drawString(name, 0, -11, colorName);
            } else {
               GL11.glEnable(3553);
               this.mc.fontRenderer.drawString(name, 0, s == 1.0F ? -4 : -2, colorName);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glEnable(2896);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }

   }
}
