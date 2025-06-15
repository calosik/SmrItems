package ru.letitems.client.gui.dolls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.letitems.client.ClientParams;
import ru.letitems.client.util.TimeTuTick;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.items.ItemArtefe;
import ru.letitems.common.items.ItemBuildersWand;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.util.BlockPos;

@SideOnly(Side.CLIENT)
public final class GuiDollKysygaki extends GuiDollBase {
   private static final ResourceLocation RESOURCE = new ResourceLocation("letitems", "textures/gui/dolls/gui-doll-kysygaki.png");
   private String args;
   public static int openPage = 0;
   private final EntityPlayer player;
   private static final ArrayList<String> listText = new ArrayList(3);
   private static Map<Integer, ItemBuildersWand.BuildersWandType> wands;
   private static Item countBlockKys = null;
   private final BlockPos blockPos;

   public GuiDollKysygaki(EntityPlayer player, BlockPos blockPos) {
      this.player = player;
      this.blockPos = blockPos;
      this.args = null;
      if (countBlockKys == null) {
         if (ClientParams.idKys == 0) {
            countBlockKys = Item.getItemFromBlock(Blocks.melon_block);
         } else if (ClientParams.idKys == 4) {
            countBlockKys = Item.getItemFromBlock(Blocks.pumpkin);
         }
      }

      wands = new HashMap();
      int count = 0;

      for(int i = 0; i < this.player.inventory.mainInventory.length; ++i) {
         ItemStack stack = this.player.inventory.mainInventory[i];
         if (stack != null && stack.getItem() instanceof ItemBuildersWand) {
            ItemBuildersWand.BuildersWandType wandType = ((ItemBuildersWand)stack.getItem()).type;
            if (wandType.getArtefeUpgrade() > 0) {
               wands.put(i, wandType);
               ++count;
               if (count >= 5) {
                  break;
               }
            }
         }
      }

   }

   public void drawScreen(int x, int y, float f) {
      super.drawScreen(x, y, f);
      this.mc.getTextureManager().bindTexture(RESOURCE);
      this.drawTexturedModalRect(this.posX, this.posY, 0, 0, 256, 190);
      this.args = null;
      int xx = x - this.posX;
      int yy = y - this.posY;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      boolean isFullHover1 = xx > -34 && xx < -1 && yy > -2 && yy < 28 || openPage == 0;
      if (isFullHover1) {
         openPage = 0;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover1 ? 1.0F : 0.25F);
      this.drawTexturedModalRect(this.posX - 32, this.posY, 0, 190, 30, 28);
      this.drawTexturedModalRect(this.posX - 25, this.posY + 6, 30, 190, 16, 16);
      boolean isFullHover2 = xx > -34 && xx < -1 && yy > 28 && yy < 60 || openPage == 1;
      if (isFullHover2) {
         openPage = 1;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover2 ? 1.0F : 0.25F);
      this.drawTexturedModalRect(this.posX - 32, this.posY + 30, 0, 190, 30, 28);
      this.drawTexturedModalRect(this.posX - 25, this.posY + 37, 46, 190, 16, 16);
      boolean isFullHover4;
      boolean hover;
      if (ClientParams.idKys == 0) {
         isFullHover4 = xx > -34 && xx < -1 && yy > 60 && yy < 88 || openPage == 2;
         if (isFullHover4) {
            openPage = 2;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover4 ? 1.0F : 0.25F);
         this.drawTexturedModalRect(this.posX - 32, this.posY + 60, 0, 190, 30, 28);
         this.drawTexturedModalRect(this.posX - 25, this.posY + 67, 78, 190, 16, 16);
      } else if (ClientParams.idKys == 4) {
         isFullHover4 = xx > -34 && xx < -1 && yy > 60 && yy < 88 || openPage == 3;
         if (isFullHover4) {
            openPage = 3;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, isFullHover4 ? 1.0F : 0.25F);
         this.drawTexturedModalRect(this.posX - 32, this.posY + 60, 0, 190, 30, 28);
         this.drawTexturedModalRect(this.posX - 25, this.posY + 67, 126, 190, 16, 16);
         hover = xx > -34 && xx < -1 && yy > 88 && yy < 120 || openPage == 4;
         if (hover) {
            openPage = 4;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.25F);
         this.drawTexturedModalRect(this.posX - 32, this.posY + 90, 0, 190, 30, 28);
         this.drawTexturedModalRect(this.posX - 25, this.posY + 97, 142, 190, 16, 16);
      }

      GL11.glPopMatrix();
      this.drawDollName("Кусугаки", 155, 150, 5993616);
      this.fontRendererObj.drawString("Обмен товаров", this.posX + 164, this.posY + 161, 9408399);
      int stackHover;
      int var11;
      ItemStack item;
      int shard;
      ItemStack[] var22;
      String countArtefe;
      boolean isHover;
      if (openPage == 0) {
         shard = 0;
         var22 = this.player.inventory.mainInventory;
         stackHover = var22.length;

         for(var11 = 0; var11 < stackHover; ++var11) {
            item = var22[var11];
            if (item != null && item.getItem() instanceof ItemArtefe && item.getItemDamage() == 1) {
               shard += item.stackSize;
            }
         }

         if (shard > 0) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawGradientRect(this.posX + 20 + 117, this.posY + 40 + 30, this.posX + 20, this.posY + 40, 815175318, 815175318);
            this.mc.getTextureManager().bindTexture(RESOURCE);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(this.posX + 17), (float)(this.posY + 39), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            this.drawTexturedModalRect(0, 0, 62, 190, 16, 16);
            GL11.glPopMatrix();
            this.drawStringWhenSize("Осколки Кристаллов", 0.75F, 49, 50, 13806330);
            this.drawStringWhenSize("Имеется " + shard, 0.75F, 49, 58, 10982331);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(RESOURCE);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(this.posX + 65), (float)(this.posY + 80), 0.0F);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            this.drawTexturedModalRect(0, 0, 30, 190, 16, 16);
            GL11.glPopMatrix();
            this.drawStringWhenSize("Кристаллы Артефэ", 1.0F, 35, 112, 13806330);
            countArtefe = "Можно получить " + shard / 6;
            this.drawStringWhenSize(countArtefe, 0.75F, (int)(80.0F - (float)(this.fontRendererObj.getStringWidth(countArtefe) / 2) * 0.75F), 122, 10982331);
            if (shard >= 6) {
               isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
               if (isHover) {
                  this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
                  this.args = "1";
               } else {
                  this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
               }

               this.drawStringWhenSize("Обменять", 1.1F, 55, 155, isHover ? -1 : 9408399);
            } else {
               this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
            }
         } else {
            this.drawStringWhenSize("Нет осколков", 1.1F, 41, 95, 7105644);
         }
      } else if (openPage == 1) {
         if (wands.isEmpty()) {
            this.drawStringWhenSize("Нет жезлов", 1.1F, 48, 95, 7105644);
         } else {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableGUIStandardItemLighting();
            shard = 0;
            var22 = this.player.inventory.mainInventory;
            stackHover = var22.length;

            for(var11 = 0; var11 < stackHover; ++var11) {
               item = var22[var11];
               if (item != null && item.getItem() instanceof ItemArtefe && item.getItemDamage() == 0) {
                  shard += item.stackSize;
               }
            }

            stackHover = -1;
            int l2 = 18;
            int i = 0;
            Iterator var13 = wands.entrySet().iterator();

            label317:
            while(true) {
               Entry entry;
               int i1;
               do {
                  do {
                     if (!var13.hasNext()) {
                        RenderHelper.disableStandardItemLighting();
                        this.drawStringWhenSize("Кристаллы Артефэ", 1.0F, 35, 155, 10987431);
                        String countArtefe = "Кристаллов в инвентаре " + shard;
                        this.drawStringWhenSize(countArtefe, 0.75F, (int)(80.0F - (float)(this.fontRendererObj.getStringWidth(countArtefe) / 2) * 0.75F), 164, 9474192);
                        if (stackHover != -1) {
                           ItemStack stack = new ItemStack(RegItems.itemBuildersWands[stackHover]);
                           ArrayList<String> listText = new ArrayList();
                           listText.add("§6" + stack.getDisplayName());
                           if (stackHover == 0) {
                              listText.add("§8Переход на 2 ур. Жезла");
                              listText.add("§8Стоит §3320§8 кристаллов Артефэ");
                           } else {
                              listText.add("§8Переход на 3 ур. Жезла");
                              listText.add("§8Стоит §3720§8 кристаллов Артефэ");
                           }

                           this.drawHoveringText(listText, x + 4, y + 4);
                        }
                        break label317;
                     }

                     entry = (Entry)var13.next();
                     i1 = 18 + 27 * i;
                  } while(i1 > 180);
               } while(i1 < 1);

               hover = xx > l2 && xx < l2 + 129 && yy > i1 - 6 && yy < i1 + 19;
               if (yy > 180 || yy < 10) {
                  hover = false;
               }

               int colorDraw = hover ? 1083216016 : 285212671;
               this.drawGradientRect(this.posX + l2 + 128, this.posY + i1 + 20, this.posX + l2 - 4, this.posY + i1 - 4, colorDraw, colorDraw);
               int needArtefe = ((ItemBuildersWand.BuildersWandType)entry.getValue()).getArtefeUpgrade();
               GL11.glEnable(2896);
               GL11.glEnable(32826);
               ItemStack stack = needArtefe == 320 ? new ItemStack(RegItems.itemBuildersWands[0]) : new ItemStack(RegItems.itemBuildersWands[1]);
               itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack, l2 + this.posX, i1 + this.posY);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glDisable(2896);
               String itemName = stack.getDisplayName();
               int titleWidth = this.fontRendererObj.getStringWidth(itemName);
               this.drawStringWhenSize(itemName, Math.min(112.0F / (float)titleWidth, 1.0F), l2 + 20, i1 + 1, 13816530);
               this.drawStringWhenSize("Потребуется " + needArtefe, 0.75F, l2 + 20, i1 + 10, 7305877);
               if (hover) {
                  stackHover = needArtefe == 320 ? 0 : 1;
                  if (shard >= needArtefe) {
                     this.args = String.format("2#%s", entry.getKey());
                  }
               }

               ++i;
            }
         }
      } else if (openPage == 2 && ClientParams.idKys == 0) {
         shard = 0;
         var22 = this.player.inventory.mainInventory;
         stackHover = var22.length;

         for(var11 = 0; var11 < stackHover; ++var11) {
            item = var22[var11];
            if (item != null && item.getItem() == countBlockKys) {
               shard += item.stackSize;
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(RESOURCE);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(this.posX + 65), (float)(this.posY + 30), 0.0F);
         GL11.glScalef(2.0F, 2.0F, 2.0F);
         this.drawTexturedModalRect(0, 0, 78, 190, 16, 16);
         GL11.glPopMatrix();
         this.drawStringWhenSize("Сочные арбузы", 1.0F, 45, 64, 7698492);
         countArtefe = "Арбузов в инвентаре " + shard;
         this.drawStringWhenSize(countArtefe, 0.75F, (int)(83.0F - (float)(this.fontRendererObj.getStringWidth(countArtefe) / 2) * 0.75F), 74, 6448936);
         this.drawStringWhenSize("Летний Кусь", 1.25F, 42, 94, 12017855);
         this.drawStringWhenSize("Обменяйте 8 Арбузов, 1", 0.75F, 37, 106, 9139341);
         this.drawStringWhenSize("обелиск и жетон события", 0.75F, 32, 113, 9139341);
         this.drawStringWhenSize("на случайные награды.", 0.75F, 39, 120, 9139341);
         if (shard >= 8) {
            isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
            if (isHover) {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
               this.args = "3";
            } else {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
            }

            this.drawStringWhenSize("Обменять", 1.1F, 55, 155, isHover ? -1 : 9408399);
         } else {
            this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
         }
      } else if (openPage == 3 && ClientParams.idKys == 4) {
         shard = 0;
         var22 = this.player.inventory.mainInventory;
         stackHover = var22.length;

         for(var11 = 0; var11 < stackHover; ++var11) {
            item = var22[var11];
            if (item != null && item.getItem() == countBlockKys) {
               shard += item.stackSize;
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(RESOURCE);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(this.posX + 65), (float)(this.posY + 30), 0.0F);
         GL11.glScalef(2.0F, 2.0F, 2.0F);
         this.drawTexturedModalRect(0, 0, 126, 190, 16, 16);
         GL11.glPopMatrix();
         this.drawStringWhenSize("Страшные тыквы", 1.0F, 40, 64, 12022815);
         countArtefe = "Тыкв в инвентаре " + shard;
         this.drawStringWhenSize(countArtefe, 0.75F, (int)(81.0F - (float)(this.fontRendererObj.getStringWidth(countArtefe) / 2) * 0.75F), 74, 9987107);
         this.drawStringWhenSize("Осенний Кусь", 1.25F, 39, 94, 12017855);
         this.drawStringWhenSize("Обменяйте 5 Тыкв, 1", 0.75F, 42, 106, 9139341);
         this.drawStringWhenSize("обелиск и жетон события", 0.75F, 32, 113, 9139341);
         this.drawStringWhenSize("на случайные награды.", 0.75F, 39, 120, 9139341);
         if (shard >= 5) {
            isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
            if (isHover) {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
               this.args = "3";
            } else {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
            }

            this.drawStringWhenSize("Обменять", 1.1F, 55, 155, isHover ? -1 : 9408399);
         } else {
            this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
         }
      } else if (openPage == 4 && ClientParams.idKys == 4) {
         shard = 0;
         var22 = this.player.inventory.mainInventory;
         stackHover = var22.length;

         for(var11 = 0; var11 < stackHover; ++var11) {
            item = var22[var11];
            if (item != null && item.getItem() == Items.pumpkin_pie) {
               shard += item.stackSize;
            }
         }

         if (shard > 80) {
            shard = 80;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(RESOURCE);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(this.posX + 65), (float)(this.posY + 30), 0.0F);
         GL11.glScalef(2.0F, 2.0F, 2.0F);
         this.drawTexturedModalRect(0, 0, 142, 190, 16, 16);
         GL11.glPopMatrix();
         this.drawStringWhenSize("Пироги на 1 Кусь", 1.0F, 36, 64, 13792845);
         countArtefe = "Пирогов в инвентаре " + shard + " / 80";
         this.drawStringWhenSize(countArtefe, 0.75F, (int)(81.0F - (float)(this.fontRendererObj.getStringWidth(countArtefe) / 2) * 0.75F), 74, 11364169);
         this.drawStringWhenSize("Тыквенный подарок", 1.0F, 30, 94, 12017855);
         this.drawStringWhenSize("Подарите кукле 80 пирогов", 0.75F, 27, 106, 9139341);
         this.drawStringWhenSize("чтобы получить достижение", 0.75F, 26, 114, 9139341);
         if (shard >= 80) {
            isHover = xx > 28 && xx < 132 && yy > 148 && yy < 168;
            if (isHover) {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, -1072689136, -1072689136);
               this.args = "4";
            } else {
               this.drawGradientRect(this.posX + 29 + 102, this.posY + 149 + 20, this.posX + 29, this.posY + 149, 1083216016, 1083216016);
            }

            this.drawStringWhenSize("Подарить", 1.1F, 55, 155, isHover ? -1 : 9408399);
         } else {
            this.drawStringWhenSize("Недостаточно", 1.1F, 41, 155, 9408399);
         }
      }

      this.fontRendererObj.drawString("?", this.posX + 245, this.posY + 5, 9408399);
      if (xx > 240 && xx < 255 && yy > 3 && yy < 15) {
         this.drawHoveringText(GuiDollKysygaki.listText, x + 4, y + 4);
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      super.mouseClicked(x, y, button);
      if (this.args != null) {
         if (TimeTuTick.instance.get(TimeTuTick.TypeTime.OTHER) > 0L) {
            this.mc.thePlayer.addChatMessage((new ChatComponentText("Подождите...")).setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GRAY)));
         } else {
            NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.KYSYGAKI, this.blockPos, this.args));
            TimeTuTick.instance.register(TimeTuTick.TypeTime.OTHER, this.args.equals("3") ? 4L : 1L);
         }
      }

   }

   public static void buildWands(int id, int j) {
      if (wands != null && !wands.isEmpty() && wands.containsKey(id)) {
         if (j == 2) {
            wands.remove(id);
         } else {
            wands.put(id, ItemBuildersWand.BuildersWandType.values()[1]);
         }
      }

   }

   static {
      listText.add("§6Кукла Кусугаки");
      listText.add("§7Позволяет обменять осколки и");
      listText.add("§7улучшить жезлы");
   }
}
