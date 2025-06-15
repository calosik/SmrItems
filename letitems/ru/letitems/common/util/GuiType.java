package ru.letitems.common.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import ru.letitems.client.gui.GuiAnchor;
import ru.letitems.client.gui.GuiHairUpgrade;
import ru.letitems.client.gui.GuiVendingMachine;
import ru.letitems.client.gui.dolls.GuiDollAEGate;
import ru.letitems.client.gui.dolls.GuiDollAlice;
import ru.letitems.client.gui.dolls.GuiDollBellCranel;
import ru.letitems.client.gui.dolls.GuiDollHestia;
import ru.letitems.client.gui.dolls.GuiDollKilluaZoldyck;
import ru.letitems.client.gui.dolls.GuiDollKoko;
import ru.letitems.client.gui.dolls.GuiDollKysygaki;
import ru.letitems.client.gui.dolls.GuiDollRinTohsaka;
import ru.letitems.client.gui.dolls.GuiDollSora;
import ru.letitems.client.gui.dolls.GuiDollWiz;
import ru.letitems.client.gui.inventory.GuiAttackRepair;
import ru.letitems.client.gui.inventory.GuiNewCrafting;
import ru.letitems.client.gui.inventory.GuiNewEnderChest;
import ru.letitems.client.gui.inventory.GuiPrimeInventory;
import ru.letitems.client.gui.inventory.GuiScrollTitle;
import ru.letitems.client.gui.pouch.GuiContainerBag;
import ru.letitems.client.gui.pouch.GuiContainerBigBag;
import ru.letitems.common.LetItems;
import ru.letitems.common.block.BlockDoll;
import ru.letitems.common.inventory.container.ContainerAnchor;
import ru.letitems.common.inventory.container.ContainerBag;
import ru.letitems.common.inventory.container.ContainerBigBag;
import ru.letitems.common.inventory.container.ContainerPrimeInv;
import ru.letitems.common.inventory.container.ContainerVendingMachine;
import ru.letitems.common.items.ItemBag;
import ru.letitems.common.network.NetworkManager;
import ru.letitems.common.network.PacketDollsMech;
import ru.letitems.common.tile.TileAnchor;
import ru.letitems.common.tile.TileEntityDoll;
import ru.letitems.common.tile.TileVendingMachine;

public enum GuiType {
   ANCHOR {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         TileEntity tile = world.getTileEntity(x, y, z);
         return tile instanceof TileAnchor ? new ContainerAnchor(player.inventory, (TileAnchor)tile) : null;
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         TileEntity tile = world.getTileEntity(x, y, z);
         return tile instanceof TileAnchor ? new GuiAnchor(new ContainerAnchor(player.inventory, (TileAnchor)tile)) : null;
      }
   },
   VENDING {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         TileEntity tile = world.getTileEntity(x, y, z);
         return tile instanceof TileVendingMachine ? new ContainerVendingMachine(player.inventory, (TileVendingMachine)tile) : null;
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         TileEntity tile = world.getTileEntity(x, y, z);
         return tile instanceof TileVendingMachine ? new GuiVendingMachine(player, (TileVendingMachine)tile) : null;
      }
   },
   PRIME_INV(false, true) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new ContainerPrimeInv(player);
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiPrimeInventory(player);
      }
   },
   BAG {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         Pair<ItemStack, ItemBag> bag = this.getItemBag(player);
         if (bag == null) {
            return null;
         } else {
            ItemStack stack = (ItemStack)bag.getLeft();
            return (Container)(((ItemBag)bag.getRight()).isBig() ? new ContainerBigBag(player.inventory, stack) : new ContainerBag(player.inventory, stack));
         }
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         Pair<ItemStack, ItemBag> bag = this.getItemBag(player);
         if (bag == null) {
            return null;
         } else {
            ItemStack stack = (ItemStack)bag.getLeft();
            return (GuiScreen)(((ItemBag)bag.getRight()).isBig() ? new GuiContainerBigBag(player.inventory, stack) : new GuiContainerBag(player.inventory, stack));
         }
      }

      private Pair<ItemStack, ItemBag> getItemBag(EntityPlayer player) {
         ItemStack stack = player.getHeldItem();
         if (stack != null && stack.stackSize == 1) {
            Item item = stack.getItem();
            if (item instanceof ItemBag) {
               return new ImmutablePair(stack, (ItemBag)item);
            }
         }

         return null;
      }
   },
   UPGRADE_HAIRS(true, false) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return null;
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiHairUpgrade(player);
      }
   },
   SCROLL_TITLE(false, true) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiScrollTitle.ContainerScrollTitle(player);
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiScrollTitle(player);
      }
   },
   CRAFT(false, true) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiNewCrafting.ContainerWorkbench(player);
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiNewCrafting(player);
      }
   },
   ENDER(false, true) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         InventoryEnderChest enderChest = player.getInventoryEnderChest();
         return enderChest == null ? null : new GuiNewEnderChest.ContainerEnderChest(player.inventory, enderChest);
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         InventoryEnderChest enderChest = player.getInventoryEnderChest();
         return enderChest == null ? null : new GuiNewEnderChest(player.inventory, enderChest);
      }
   },
   ATTACK_REPAIR(false, true) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiAttackRepair.ContainerAttackRepair(player.inventory, player);
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return new GuiAttackRepair(player);
      }
   },
   DOLLS(true, false) {
      public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         return null;
      }

      @SideOnly(Side.CLIENT)
      public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
         TileEntity tileEntity = world.getTileEntity(x, y, z);
         if (tileEntity instanceof TileEntityDoll) {
            BlockDoll.DollType dollType = ((TileEntityDoll)tileEntity).getType();
            if (dollType.isGui()) {
               BlockPos blockPos = new BlockPos(x, y, z);
               switch(dollType) {
               case SORA:
                  return new GuiDollSora(player, blockPos);
               case HESTIA:
                  return LetItems.loadThaumCraft ? new GuiDollHestia(player, blockPos) : null;
               case BELL_CRANEL:
                  return LetItems.loadThaumCraft ? new GuiDollBellCranel(player, blockPos) : null;
               case ALICE_KISARAGI:
                  if (LetItems.loadBotania) {
                     NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.ALICE_KISARAGI, blockPos, "-1"));
                     return new GuiDollAlice(player, blockPos);
                  }

                  return null;
               case KYSYGAKI:
                  return new GuiDollKysygaki(player, blockPos);
               case RIN_TOHSAKA:
                  return new GuiDollRinTohsaka(player, blockPos);
               case KOKO:
                  return new GuiDollKoko(player, blockPos);
               case KURISU_MAKISE:
               case OKABE:
                  return LetItems.loadAE2 ? new GuiDollAEGate(player, blockPos, dollType) : null;
               case WIZ:
                  NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.WIZ, blockPos, "-1"));
                  return new GuiDollWiz(player, blockPos);
               case IRINA_LUMINESK:
                  return null;
               case KILLUA_ZOLDYCK:
                  NetworkManager.sendToServer(new PacketDollsMech(BlockDoll.DollType.KILLUA_ZOLDYCK, blockPos, "-1"));
                  return new GuiDollKilluaZoldyck(player, blockPos);
               default:
                  return null;
               }
            }
         }

         return null;
      }
   };

   private final boolean clientOnly;
   private final boolean allowPacket;

   private GuiType() {
      this(false, false);
   }

   private GuiType(boolean clientOnly, boolean allowPacket) {
      this.clientOnly = clientOnly;
      this.allowPacket = allowPacket;
   }

   public void openGui(EntityPlayer player, TileEntity tile) {
      if (tile != null && tile.hasWorldObj()) {
         this.openGui(player, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
      }

   }

   public void openGui(EntityPlayer player, World world) {
      this.openGui(player, world, 0, 0, 0);
   }

   public void openGui(EntityPlayer player, World world, int x, int y, int z) {
      if (this.clientOnly == world.isRemote) {
         player.openGui(LetItems.instance, this.ordinal(), world, x, y, z);
      }

   }

   public boolean isClientOnly() {
      return this.clientOnly;
   }

   public boolean isAllowPacket() {
      return this.allowPacket;
   }

   public abstract Container getServerGuiElement(EntityPlayer var1, World var2, int var3, int var4, int var5);

   @SideOnly(Side.CLIENT)
   public abstract GuiScreen getClientGuiElement(EntityPlayer var1, World var2, int var3, int var4, int var5);

   public static GuiType getGuiType(int id) {
      GuiType[] types = values();
      return id >= 0 && id < types.length ? types[id] : null;
   }

   // $FF: synthetic method
   GuiType(Object x2) {
      this();
   }

   // $FF: synthetic method
   GuiType(boolean x2, boolean x3, Object x4) {
      this(x2, x3);
   }
}
