package ru.letitems.common.dakimakura.pack;

import java.util.List;
import ru.letitems.common.dakimakura.Daki;

public interface IDakiPack {
   String getResourceName();

   String getName();

   Daki getDaki(String var1);

   List<Daki> getDakisInPack();

   byte[] getResource(String var1);

   void loadPack(String var1);
}
