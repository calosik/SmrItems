package ru.letitems.common.dakimakura.pack;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.Nonnull;
import ru.letitems.common.LetItems;
import ru.letitems.common.dakimakura.Daki;
import ru.letitems.common.dakimakura.DakiManager;

public abstract class AbstractDakiPack implements IDakiPack {
   protected final DakiManager dakiManager;
   private final String resourceName;
   private final LinkedHashMap<String, Daki> dakiMap = new LinkedHashMap();
   private final List<Daki> cachedDakiList = new ArrayList();

   protected AbstractDakiPack(String resourceName) {
      this.dakiManager = LetItems.proxy.getDakimakuraManager();
      this.resourceName = resourceName;
   }

   public String getResourceName() {
      return this.resourceName;
   }

   public Daki getDaki(String dakiDirName) {
      return (Daki)this.dakiMap.get(this.getResourceName() + ':' + dakiDirName);
   }

   protected void addDaki(@Nonnull Daki daki) {
      Daki prevDaki = (Daki)this.dakiMap.put(this.getResourceName() + ':' + daki.getDakiDirectoryName(), daki);
      if (prevDaki == null) {
         this.cachedDakiList.add(daki);
      } else {
         this.cachedDakiList.clear();
         this.cachedDakiList.addAll(this.dakiMap.values());
      }

      this.dakiManager.updateIndex();
   }

   public List<Daki> getDakisInPack() {
      return Collections.unmodifiableList(this.cachedDakiList);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.resourceName == null ? 0 : this.resourceName.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         AbstractDakiPack other = (AbstractDakiPack)obj;
         return Objects.equal(this.resourceName, other.resourceName);
      } else {
         return false;
      }
   }
}
