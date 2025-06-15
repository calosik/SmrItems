package ru.SmrItems.util.registry;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import ru.SmrItems.SmMain;

public final class RegistryUtils {
   private static final Set<Class<? extends TileEntity>> REGISTERED_TILES = new HashSet();

   public static <T extends Item & IHasName> T registerItem(T item) {
      return registerItem(item, ((IHasName)item).getName());
   }

   public static <T extends Item> T registerItem(T item, String name) {
      GameRegistry.registerItem(item, name);
      return item;
   }

   public static <T extends Block & IHasName> T registerBlock(T block) {
      return registerBlock(block, ((IHasName)block).getName());
   }

   public static <T extends Block & IHasName> T registerBlock(T block, Class<? extends ItemBlock> itemClass) {
      return registerBlock(block, itemClass, ((IHasName)block).getName());
   }

   public static <T extends Block> T registerBlock(T block, String name) {
      if (block instanceof IHasItemBlock) {
         return registerBlock(block, ((IHasItemBlock)block).getItemBlockClass(), name);
      } else {
         GameRegistry.registerBlock(block, name);
         if (block instanceof IHasTileEntity) {
            registerTileEntity((IHasTileEntity)block);
         }

         return block;
      }
   }

   public static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass, String name) {
      GameRegistry.registerBlock(block, itemClass, name);
      if (block instanceof IHasTileEntity) {
         registerTileEntity((IHasTileEntity)block);
      }

      return block;
   }

   public static void registerTileEntity(IHasTileEntity<?> tileEntityProvider) {
      Class<? extends TileEntity> tileClass = tileEntityProvider.getTileEntityClass();
      if (REGISTERED_TILES.add(tileClass)) {
         String tileName = tileEntityProvider instanceof IHasNamedTileEntity ? ((IHasNamedTileEntity)tileEntityProvider).getTileEntityName() : tileClass.getName();
         GameRegistry.registerTileEntity(tileClass, tileName);
      }

   }

   public static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
      EntityRegistry.registerModEntity(entityClass, entityName, id, SmMain.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
   }
}
