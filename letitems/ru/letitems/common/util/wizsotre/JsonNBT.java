package ru.letitems.common.util.wizsotre;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTBase.NBTPrimitive;

public class JsonNBT implements JsonSerializer<NBTBase>, JsonDeserializer<NBTBase> {
   static final Type TYPE = (new TypeToken<NBTBase>() {
   }).getType();

   public NBTBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      if (json.isJsonNull()) {
         return null;
      } else {
         Iterator var14;
         NBTBase nbtBase;
         if (json.isJsonObject()) {
            NBTTagCompound compound = new NBTTagCompound();
            JsonObject object = json.getAsJsonObject();
            var14 = object.entrySet().iterator();

            while(var14.hasNext()) {
               Entry<String, JsonElement> entry = (Entry)var14.next();
               nbtBase = (NBTBase)context.deserialize((JsonElement)entry.getValue(), TYPE);
               compound.setTag((String)entry.getKey(), nbtBase);
            }

            return compound;
         } else if (json.isJsonArray()) {
            NBTTagList nbtList = new NBTTagList();
            JsonArray jsonList = json.getAsJsonArray();
            var14 = jsonList.iterator();

            while(var14.hasNext()) {
               JsonElement element = (JsonElement)var14.next();
               nbtBase = (NBTBase)context.deserialize(element, TYPE);
               nbtList.appendTag(nbtBase);
            }

            return nbtList;
         } else if (json.isJsonPrimitive()) {
            String string = json.getAsString();
            if (string.matches("[-+]?[0-9]*\\.?[0-9]+[d|D]")) {
               return new NBTTagDouble(Double.parseDouble(string.substring(0, string.length() - 1)));
            } else if (string.matches("[-+]?[0-9]*\\.?[0-9]+[f|F]")) {
               return new NBTTagFloat(Float.parseFloat(string.substring(0, string.length() - 1)));
            } else if (string.matches("[-+]?[0-9]+[b|B]")) {
               return new NBTTagByte(Byte.parseByte(string.substring(0, string.length() - 1)));
            } else if (string.matches("[-+]?[0-9]+[l|L]")) {
               return new NBTTagLong(Long.parseLong(string.substring(0, string.length() - 1)));
            } else if (string.matches("[-+]?[0-9]+[s|S]")) {
               return new NBTTagShort(Short.parseShort(string.substring(0, string.length() - 1)));
            } else if (string.matches("[-+]?[0-9]+")) {
               return new NBTTagInt(Integer.parseInt(string));
            } else if (string.matches("[-+]?[0-9]*\\.?[0-9]+")) {
               return new NBTTagDouble(Double.parseDouble(string));
            } else if (!string.equalsIgnoreCase("true") && !string.equalsIgnoreCase("false")) {
               if (string.startsWith("[") && string.endsWith("]")) {
                  if (string.length() > 2) {
                     String s = string.substring(1, string.length() - 1);
                     String[] astring = s.split(",");

                     try {
                        if (astring.length <= 1) {
                           return new NBTTagIntArray(new int[]{Integer.parseInt(s.trim())});
                        } else {
                           int[] aint = new int[astring.length];

                           for(int i = 0; i < astring.length; ++i) {
                              aint[i] = Integer.parseInt(astring[i].trim());
                           }

                           return new NBTTagIntArray(aint);
                        }
                     } catch (NumberFormatException var9) {
                        return new NBTTagString(string);
                     }
                  } else {
                     return new NBTTagIntArray(new int[0]);
                  }
               } else {
                  if (string.startsWith("\"") && string.endsWith("\"") && string.length() > 2) {
                     string = string.substring(1, string.length() - 1);
                  }

                  string = string.replaceAll("\\\\\"", "\"");
                  return new NBTTagString(string);
               }
            } else {
               return new NBTTagByte((byte)(Boolean.parseBoolean(string) ? 1 : 0));
            }
         } else {
            throw new JsonParseException("Invalid NBT: " + json.toString());
         }
      }
   }

   public JsonElement serialize(NBTBase src, Type typeOfSrc, JsonSerializationContext context) {
      if (src instanceof NBTPrimitive) {
         return new JsonPrimitive(src.toString());
      } else if (src instanceof NBTTagString) {
         return new JsonPrimitive(((NBTTagString)src).func_150285_a_());
      } else if (src instanceof NBTTagCompound) {
         NBTTagCompound compound = (NBTTagCompound)src;
         JsonObject object = new JsonObject();
         Iterator var13 = compound.func_150296_c().iterator();

         while(var13.hasNext()) {
            Object name = var13.next();
            object.add((String)name, context.serialize(compound.getTag((String)name)));
         }

         return object;
      } else if (src instanceof NBTTagList) {
         NBTTagList list = (NBTTagList)src.copy();
         JsonArray array = new JsonArray();

         while(list.tagCount() != 0) {
            array.add(context.serialize(list.removeTag(0)));
         }

         return array;
      } else if (!(src instanceof NBTTagIntArray)) {
         throw new IllegalArgumentException("Not supported by MC's JSON code. Type: " + src.getClass() + " Value: " + src);
      } else {
         JsonArray array = new JsonArray();
         int[] var5 = ((NBTTagIntArray)src).func_150302_c();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int val = var5[var7];
            array.add(new JsonPrimitive(val));
         }

         return array;
      }
   }
}
