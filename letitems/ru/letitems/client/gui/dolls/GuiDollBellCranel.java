package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.model.ModelHairBand;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.inventory.ExtendedPlayer;
import ru.letitems.common.items.ItemHairBand;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollBellCranel extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-bell-cranel.png");
   private static int isRebuild;
   private boolean isActive;
   private ModelHairBand MainHairRender;
   private boolean isThaumcraftUpdate;
   private byte sizeHair;
   private float zoom;
   private static final ArrayList<String> listText = new ArrayList(4);
   private final EntityPlayer player;
   private final BlockPos blockPos;

   public GuiDollBellCranel(EntityPlayer player, BlockPos blockPos) {
      this.player = player;
      this.blockPos = blockPos;
      isRebuild = 1;
      this.sizeHair = 0;
      this.zoom = 0.05F;
      this.MainHairRender = null;
      this.update();
      this.isActive = false;
   }

   public void updateScreen() {
      super.updateScreen();
      this.update();
   }

   public void update() {
      ItemStack itemHairBand = ExtendedPlayer.getExtendedPlayer(this.player).getHair();
      if (isRebuild > 0 && itemHairBand != null) {
         int color = 0;
         this.isThaumcraftUpdate = false;
         if (itemHairBand.hasTagCompound()) {
            color = itemHairBand.getTagCompound().getByte("type");
            this.isThaumcraftUpdate = itemHairBand.getTagCompound().hasKey("tcVis");
         }

         ItemHairBand.HairBandType hairBandType = ItemHairBand.getType(itemHairBand);
         this.MainHairRender = new ModelHairBand(hairBandType.ordinal(), color != 0 ? "_" + color : null);
         this.sizeHair = hairBandType.getSize();
         --isRebuild;
      }

   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.isActive = false;
      this.drawStringWhenSize("Белл Кранел", 1.15F, 167, 150, 5993616);
      this.drawStringWhenSize("Улучшение косичек", 0.75F, 166, 161, 9408399);
      int xx = x - this.posX;
      int yy = y - this.posY;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.MainHairRender == null) {
         this.drawStringWhenSize("Нет активной косички", 1.0F, 22, 95, 7105644);
      } else {
         if (this.zoom <= 0.65F) {
            this.zoom += 0.005F;
         }

         GL11.glPushMatrix();
         GL11.glTranslatef((float)(this.posX + 85 - this.sizeHair * 3), (float)(this.posY + 65 + this.sizeHair * 18), -3.0F + this.zLevel);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glScalef(65.0F, 65.0F, 65.0F);
         GL11.glScalef(1.0F, 1.0F, -1.0F);
         GL11.glClear(256);
         GL11.glDisable(2884);
         GL11.glRotatef(55.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(this.zoom, this.zoom, 1.0F);
         this.MainHairRender.render(this.player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
         GL11.glPopMatrix();
         boolean isHover;
         if (this.isThaumcraftUpdate) {
            this.drawStringWhenSize("Убрать Вис улучшение", 0.75F, 37, 130, 9408399);
            this.drawStringWhenSize("Требуется §2Сгусток слизи", 0.75F, 30, 137, 9408399);
            if (!this.player.inventory.hasItem(Items.slime_ball)) {
               this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
            } else {
               isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
               if (isHover) {
                  this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
                  this.isActive = true;
               } else {
                  this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
               }

               this.drawStringWhenSize("Снять Вис", 1.1F, 52, 155, isHover ? -1 : 9408399);
            }
         } else {
            this.drawStringWhenSize("Наложить Вис улучшение", 0.75F, 32, 130, 9408399);
            this.drawStringWhenSize("Требуется §dМагический осколок", 0.75F, 19, 137, 9408399);
            if (!this.player.inventory.hasItem(RegItems.ItemMagicShard)) {
               this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
            } else {
               isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
               if (isHover) {
                  this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
                  this.isActive = true;
               } else {
                  this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
               }

               this.drawStringWhenSize("Улучшить", 1.1F, 55, 155, isHover ? -1 : 9408399);
            }
         }
      }

      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(listText, x + 4, y + 4);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.isActive && this.mc.thePlayer != null) {
         NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.BELL_CRANEL, this.blockPos));
      }

   }

   public static void rebuild() {
      isRebuild = 10;
   }

   static {
      listText.add("§6Кукла Белл Кранел");
      listText.add("§7Улучшает косички за §dМагический осколок§7");
      listText.add("§7добавляя им снижение затрат");
      listText.add("§7Вис из ThaumCraft");
   }
}
