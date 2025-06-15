package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.items.ItemMagicShard;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollSora extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-sora.png");
   private static RenderItem itemRender = new RenderItem();
   private boolean isActive;
   private final int countMagicShardsInventory;
   private static List<ItemStack> cacheCosmetics = new ArrayList(35);
   private static final ArrayList<String> listText = new ArrayList(4);
   private final BlockPos blockPos;

   public GuiDollSora(EntityPlayer player, BlockPos blockPos) {
      this.blockPos = blockPos;
      int shard = 0;
      ItemStack[] var4 = player.inventory.mainInventory;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemStack item = var4[var6];
         if (item != null && item.getItem() instanceof ItemMagicShard) {
            shard += item.stackSize;
         }
      }

      this.countMagicShardsInventory = shard;
      this.isActive = false;
   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      GL11.glPushMatrix();
      GL11.glScalef(1.1F, 1.1F, 1.1F);
      this.fontRendererObj.drawString("Обмен осколков", (int)((float)(this.posX + 151) / 1.1F), (int)((float)(this.posY + 132) / 1.1F), 13290186);
      GL11.glPopMatrix();
      this.fontRendererObj.drawString("Осколков есть: " + this.countMagicShardsInventory, this.posX + 152, this.posY + 142, 9408399);
      int xx = x - this.posX;
      int yy = y - this.posY;
      if (this.countMagicShardsInventory >= 1) {
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         boolean isHover = xx > 144 && xx < 247 && yy > 159 && yy < 179;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, isHover ? 1.0F : 0.4F);
         this.mc.getTextureManager().bindTexture(RESOURCE);
         this.drawTexturedModalRect(this.posX + 144, this.posY + 159, 0, 190, 129, 21);
         GL11.glPushMatrix();
         GL11.glScalef(1.1F, 1.1F, 1.1F);
         this.fontRendererObj.drawString("Обменять", (int)((float)(this.posX + 170) / 1.1F), (int)((float)(this.posY + 165) / 1.1F), -1);
         this.isActive = true;
      } else {
         GL11.glPushMatrix();
         GL11.glScalef(1.1F, 1.1F, 1.1F);
         this.fontRendererObj.drawString("Нет осколков", (int)((float)(this.posX + 158) / 1.1F), (int)((float)(this.posY + 165) / 1.1F), 7105644);
      }

      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.enableGUIStandardItemLighting();
      int cacheSize = cacheCosmetics.size();
      int u = 0;
      int kfkfkd = 0;
      int fdsfdsf = 0;

      for(int i = 0; i < 35; ++u) {
         int l2 = 12 + this.posX + 20 * kfkfkd;
         int i1 = 20 + this.posY + fdsfdsf;
         if ((u + 1) % 6 == 0) {
            fdsfdsf += 26;
            kfkfkd = 0;
         } else {
            ++kfkfkd;
         }

         GL11.glEnable(32826);
         if (cacheSize != 35) {
            cacheCosmetics.add(new ItemStack(RegItems.itemCosmetics, 1, i));
         }

         itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), (ItemStack)cacheCosmetics.get(i), l2, i1);
         l2 -= this.posX;
         i1 -= this.posY;
         if (xx > l2 && xx < l2 + 21 && yy > i1 && yy < i1 + 19) {
            String nameCosmetic = ((ItemStack)cacheCosmetics.get(i)).getDisplayName();
            int stringWidth = this.fontRendererObj.getStringWidth(nameCosmetic);
            if (stringWidth > 100) {
               float scale = Math.min(110.0F / (float)stringWidth, 1.0F);
               GL11.glPushMatrix();
               GL11.glTranslated((double)(this.posX + 195 - (int)((float)(stringWidth / 2) * scale)), (double)(this.posY + 119), 0.0D);
               GL11.glScalef(scale, scale, scale);
               this.fontRendererObj.drawString(nameCosmetic, 0, 0, 8999017);
               GL11.glPopMatrix();
            } else {
               this.fontRendererObj.drawString(nameCosmetic, this.posX + 195 - stringWidth / 2, this.posY + 119, 8999017);
            }
         }

         ++i;
      }

      RenderHelper.disableStandardItemLighting();
      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(listText, x + 4, y + 4);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.isActive && x - this.posX > 144 && x - this.posX < 247 && y - this.posY > 159 && y - this.posY < 179 && this.mc.thePlayer != null) {
         NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.SORA, this.blockPos));
      }

   }

   static {
      listText.add("§6Кукла Сора");
      listText.add("§7Меняет §dМагический осколок§7 на");
      listText.add("§7случайное украшение. С наличием §5Prime");
      listText.add("§5подписки§7 можно получить два украшения");
   }
}
