package ru.letitems.common.util.wizsotre;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.item.EnumRarity;

public class JsonEnumRarity implements JsonSerializer<EnumRarity>, JsonDeserializer<EnumRarity> {
   public EnumRarity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      String name = json.getAsString();
      EnumRarity[] var5 = EnumRarity.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumRarity rarity = var5[var7];
         if (rarity.rarityName.equalsIgnoreCase(name) || rarity.name().equalsIgnoreCase(name)) {
            return rarity;
         }
      }

      return EnumRarity.common;
   }

   public JsonElement serialize(EnumRarity src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src.rarityName);
   }
}
