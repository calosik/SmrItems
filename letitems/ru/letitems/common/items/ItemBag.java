package ru.letitems.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.letitems.common.inventory.InventoryBag;
import ru.letitems.common.lib.LibRare;
import ru.letitems.common.registry.RegItems;
import ru.letitems.common.util.InventoryUtils;

public final class ItemBag extends ItemBase {
   private final boolean big;

   public ItemBag(int id) {
      this(id, false);
   }

   public ItemBag(int id, boolean big) {
      super("bag" + id);
      this.big = big;
      this.setMaxStackSize(1);
   }

   public boolean isBig() {
      return this.big;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return stack;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
      list.add("Сумка для хранения вещей");
      if (stack.getItem() == RegItems.itemBag4) {
         list.add("Способна собирать вещи автоматически");
      } else if (stack.getItem() == RegItems.itemBag5) {
         list.add("Самый большой размер, 108 слотов");
         list.add(stack.getItemDamage() == 1 ? "Способна собирать вещи автоматически" : "Кольцо не пробуждено");
      }

      if (!GuiScreen.isShiftKeyDown()) {
         list.add("§7Зажмите §3LSHIFT §7для получения информации.");
      } else {
         addInventoryInformation(stack, list, player);
      }

   }

   @SideOnly(Side.CLIENT)
   private static void addInventoryInformation(ItemStack stack, List<String> list, EntityPlayer player) {
      if (stack.getItem() instanceof ItemBag) {
         IInventory inv = new InventoryBag(player, stack);
         List<ItemStack> items = new ArrayList();
         boolean[] checked = new boolean[inv.getSizeInventory()];

         ItemStack s1;
         int j;
         for(int i = 0; i < inv.getSizeInventory(); ++i) {
            if (!checked[i]) {
               checked[i] = true;
               s1 = inv.getStackInSlot(i);
               if (s1 != null) {
                  items.add(s1);

                  for(j = 0; j < inv.getSizeInventory(); ++j) {
                     if (!checked[j]) {
                        ItemStack s2 = inv.getStackInSlot(j);
                        if (s2 != null && InventoryUtils.isItemEqual(s1, s2)) {
                           s1.stackSize += s2.stackSize;
                           checked[j] = true;
                        }
                     }
                  }
               }
            }
         }

         if (items.size() > 0) {
            list.add("Предметы:");
         }

         Iterator var10 = items.iterator();

         while(true) {
            while(var10.hasNext()) {
               s1 = (ItemStack)var10.next();
               j = s1.stackSize;
               int maxSize = s1.getMaxStackSize();
               if (j >= maxSize && maxSize != 1) {
                  if (j % maxSize != 0) {
                     list.add("    §a" + maxSize + 'x' + j / maxSize + '+' + j % maxSize + ' ' + getItemName(s1));
                  } else {
                     list.add("    §a" + maxSize + 'x' + j / maxSize + ' ' + getItemName(s1));
                  }
               } else {
                  list.add("    §a" + j + ' ' + getItemName(s1));
               }
            }

            return;
         }
      }
   }

   public int getInventorySize() {
      return this.big ? 108 : 27;
   }

   @SideOnly(Side.CLIENT)
   private static String getItemName(ItemStack stack) {
      String s = "§r";
      EnumRarity rarity = stack.getRarity();
      if (rarity != EnumRarity.uncommon && rarity != LibRare.RARITY_HEROIC) {
         if (rarity != EnumRarity.rare && rarity != LibRare.RARITY_COMMON) {
            if (rarity == EnumRarity.epic) {
               s = "§d";
            } else if (rarity == LibRare.RARITY_RARE) {
               s = "§2";
            } else if (rarity == LibRare.RARITY_EPIC) {
               s = "§d";
            } else if (rarity == LibRare.RARITY_LEGENDARY) {
               s = "§6";
            } else if (rarity == LibRare.RARITY_ARTIFACT) {
               s = "§c";
            }
         } else {
            s = "§b";
         }
      } else {
         s = "§e";
      }

      s = s + stack.getDisplayName() + "§r";
      return s;
   }
}
