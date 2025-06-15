package ru.letitems.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.letitems.common.LetItems;
import ru.letitems.common.util.registry.IHasName;

public class BlockBase extends Block implements IHasName {
   private final String name;

   public BlockBase(Material material, String name) {
      super(material);
      this.name = name;
      this.setBlockName(name);
      this.setBlockTextureName("letitems:" + name);
      this.setCreativeTab(LetItems.tabLetItems);
   }

   public String getName() {
      return this.name;
   }
}
