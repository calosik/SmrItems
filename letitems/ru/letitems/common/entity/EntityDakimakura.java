package ru.letitems.common.entity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.Daki;

public class EntityDakimakura extends Entity implements IEntityAdditionalSpawnData {
   private static final String TAG_FLIPPED = "flipped";
   private static final String TAG_ROTATION = "rotation";
   private String dakiDirName;
   private ForgeDirection rotation;

   public EntityDakimakura(World world) {
      super(world);
      this.noClip = true;
      this.setSize(4.0F, 1.0F);
   }

   protected void entityInit() {
      this.dataWatcher.addObject(2, (byte)0);
   }

   public void setDaki(Daki daki) {
      this.dakiDirName = daki != null ? daki.getDakiDirectoryName() : null;
   }

   public void onUpdate() {
   }

   public void dropAsItem() {
   }

   @SideOnly(Side.CLIENT)
   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int p_70056_9_) {
      this.setPosition(x, y, z);
      this.setRotation(yaw, pitch);
   }

   protected void writeEntityToNBT(NBTTagCompound compound) {
      if (this.dakiDirName != null) {
         compound.setString("dakiDirName", this.dakiDirName);
      }

      if (this.rotation != null) {
         compound.setInteger("rotation", this.rotation.ordinal());
      }

      compound.setBoolean("flipped", this.isFlipped());
   }

   protected void readEntityFromNBT(NBTTagCompound compound) {
      if (compound.hasKey("dakiDirName", 8)) {
         this.dakiDirName = compound.getString("dakiDirName");
      }

      if (compound.hasKey("rotation", 3)) {
         this.rotation = ForgeDirection.getOrientation(compound.getInteger("rotation"));
      }

      this.setFlipped(compound.getBoolean("flipped"));
   }

   public void writeSpawnData(ByteBuf buf) {
      buf.writeDouble(this.posX);
      buf.writeDouble(this.posY);
      buf.writeDouble(this.posZ);
      if (this.dakiDirName != null) {
         buf.writeBoolean(true);
         ByteBufUtils.writeUTF8String(buf, this.dakiDirName);
      } else {
         buf.writeBoolean(false);
      }

      if (this.rotation != null) {
         buf.writeBoolean(true);
         buf.writeInt(this.rotation.ordinal());
      } else {
         buf.writeBoolean(false);
      }

   }

   public void readSpawnData(ByteBuf buf) {
      this.posX = buf.readDouble();
      this.posY = buf.readDouble();
      this.posZ = buf.readDouble();
      if (buf.readBoolean()) {
         this.dakiDirName = ByteBufUtils.readUTF8String(buf);
      }

      if (buf.readBoolean()) {
         this.rotation = ForgeDirection.getOrientation(buf.readInt());
      }

   }

   public Daki getDaki() {
      return LetItems.proxy.getDakimakuraManager().getDakiFromMap(this.dakiDirName);
   }

   public boolean isFlipped() {
      return this.dataWatcher.getWatchableObjectByte(2) == 1;
   }

   public void setFlipped(boolean flipped) {
      this.dataWatcher.updateObject(2, Byte.valueOf((byte)(flipped ? 1 : 0)));
   }

   public void flip() {
      this.setFlipped(!this.isFlipped());
   }

   public void setRotation(ForgeDirection rotation) {
      this.rotation = rotation;
   }

   public ForgeDirection getRotation() {
      return this.rotation;
   }

   public boolean isDakiOverBlock(int x, int y, int z) {
      if (MathHelper.floor_double(this.posX) == x & MathHelper.floor_double(this.posY) == y + 1 & MathHelper.floor_double(this.posZ) == z) {
         return true;
      } else {
         x -= this.rotation.offsetX;
         z -= this.rotation.offsetZ;
         return MathHelper.floor_double(this.posX) == x & MathHelper.floor_double(this.posY) == y + 1 & MathHelper.floor_double(this.posZ) == z;
      }
   }
}
