package ru.letitems.common.dakimakura.serialize;

import net.minecraft.nbt.NBTTagCompound;
import ru.letitems.common.dakimakura.Daki;

public final class DakiNbtSerializer {
   public static final String TAG_DAKI_DIR_NAME = "dakiDirName";
   public static final String TAG_FLIPPED = "flipped";

   private DakiNbtSerializer() {
   }

   public static NBTTagCompound serialize(Daki daki) {
      return serialize(daki, new NBTTagCompound());
   }

   public static NBTTagCompound serialize(Daki daki, NBTTagCompound compound) {
      compound.setString("dakiDirName", daki.getDakiDirectoryName());
      return compound;
   }

   public static void setFlipped(NBTTagCompound compound, boolean flipped) {
      compound.setBoolean("flipped", flipped);
   }

   public static boolean isFlipped(NBTTagCompound compound) {
      return compound.getBoolean("flipped");
   }
}
