package ru.letitems.common.util.wizsotre;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import java.lang.reflect.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class JsonItemStack implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
   static final Type TYPE = (new TypeToken<ItemStack>() {
   }).getType();

   public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject object = json.getAsJsonObject();
      UniqueIdentifier id = new UniqueIdentifier(object.get("item").getAsString());
      ItemStack stack = GameRegistry.findItemStack(id.modId, id.name, 1);
      if (stack == null) {
         throw new JsonParseException("Invalid item: " + json.toString());
      } else {
         if (object.has("damage")) {
            stack.setItemDamage(object.get("damage").getAsInt());
         }

         if (object.has("nbt")) {
            stack.stackTagCompound = (NBTTagCompound)context.deserialize(object.get("nbt"), JsonNBT.TYPE);
         }

         return stack;
      }
   }

   public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject object = new JsonObject();
      UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(src.getItem());
      object.addProperty("item", id.toString());
      object.addProperty("damage", src.getItemDamage());
      if (src.stackTagCompound != null) {
         object.add("nbt", context.serialize(src.stackTagCompound));
      }

      return object;
   }
}
