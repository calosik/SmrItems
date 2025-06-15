package ru.letitems.common.util;

import com.emoniph.witchery.common.ExtendedPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.common.ISpecialArmor;
import ru.letitems.client.gui.inventory.GuiNewCreative;
import ru.letitems.common.LetItems;

public final class MethodUtils {
   private static String charSet;

   public static boolean checkArmorBase(EntityPlayer player) {
      if (LetItems.loadWitchery) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if (playerEx.getWerewolfLevel() > 0 && playerEx.getCreatureType().isWolf()) {
            return false;
         }
      }

      return true;
   }

   @SideOnly(Side.CLIENT)
   public static int getForgeTotalArmorValue(EntityPlayer player) {
      if (LetItems.loadWitchery) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if (playerEx.getWerewolfLevel() > 0 && playerEx.getCreatureType().isWolf()) {
            return 0;
         }
      }

      int ret = 0;

      for(int x = 0; x < player.inventory.armorInventory.length; ++x) {
         ItemStack stack = player.inventory.armorInventory[x];
         if (stack != null && stack.getItem() instanceof ISpecialArmor) {
            ret += ((ISpecialArmor)stack.getItem()).getArmorDisplay(player, stack, x);
         } else if (stack != null && stack.getItem() instanceof ItemArmor) {
            ret += ((ItemArmor)stack.getItem()).damageReduceAmount;
         }
      }

      return ret;
   }

   public static void handleSetSlot(S2FPacketSetSlot slot) {
      Minecraft mc = Minecraft.getMinecraft();
      EntityClientPlayerMP player = mc.thePlayer;
      int windowIndex = slot.func_149175_c();
      if (windowIndex == -1) {
         player.inventory.setItemStack(slot.func_149174_e());
      } else {
         boolean flag = false;
         if (mc.currentScreen instanceof GuiNewCreative) {
            flag = ((GuiNewCreative)mc.currentScreen).func_147056_g() != CreativeTabs.tabInventory.getTabIndex();
         }

         if (windowIndex == 0 && slot.func_149173_d() >= 34 && slot.func_149173_d() < 43) {
            ItemStack itemstack = player.inventoryContainer.getSlot(slot.func_149173_d()).getStack();
            if (slot.func_149174_e() != null && (itemstack == null || itemstack.stackSize < slot.func_149174_e().stackSize)) {
               slot.func_149174_e().animationsToGo = 5;
            }

            player.inventoryContainer.putStackInSlot(slot.func_149173_d(), slot.func_149174_e());
         } else if (windowIndex == player.openContainer.windowId && (windowIndex != 0 || !flag)) {
            player.openContainer.putStackInSlot(slot.func_149173_d(), slot.func_149174_e());
         }
      }

   }

   public static String handleMessage(String message) {
      char[] var1 = message.toCharArray();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         char c = var1[var3];
         if (isNotAllowed(c)) {
            char[] var5 = message.toCharArray();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               char c2 = var5[var7];
               if (isNotAllowed(c2)) {
                  message = message.replace(Character.valueOf(c2).toString(), "");
               }
            }
         }
      }

      return message;
   }

   private static boolean isNotAllowed(char ch) {
      return !charSet.contains(String.valueOf(ch));
   }
}
