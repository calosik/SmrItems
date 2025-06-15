package ru.letitems.common.util.wizsotre;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

public class JsonWeightedRandomChestContent implements JsonSerializer<WeightedRandomChestContent>, JsonDeserializer<WeightedRandomChestContent> {
   public static final Type TYPE = (new TypeToken<WeightedRandomChestContent>() {
   }).getType();

   public WeightedRandomChestContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject object = json.getAsJsonObject();
      ItemStack stack = (ItemStack)context.deserialize(object.get("itemstack"), JsonItemStack.TYPE);
      int min = object.get("min").getAsInt();
      int max = object.get("max").getAsInt();
      int weight = object.get("weight").getAsInt();
      return new WeightedRandomChestContent(stack, min, max, weight);
   }

   public JsonElement serialize(WeightedRandomChestContent src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject object = new JsonObject();
      object.addProperty("min", src.theMinimumChanceToGenerateItem);
      object.addProperty("max", src.theMaximumChanceToGenerateItem);
      object.addProperty("weight", src.itemWeight);
      object.add("itemstack", context.serialize(src.theItemId));
      return object;
   }
}
