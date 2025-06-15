package ru.letitems.common.util;

import com.mojang.authlib.GameProfile;

public interface IHasOwner {
   boolean hasOwner();

   GameProfile getOwner();

   void setOwner(GameProfile var1);
}
