package ru.letitems.common.dakimakura;

import java.util.Objects;

public class Daki implements Comparable<Daki> {
   private final String dakiDirectoryName;

   public Daki(String dakiDirectoryName) {
      this.dakiDirectoryName = dakiDirectoryName;
   }

   public String getDakiDirectoryName() {
      return this.dakiDirectoryName;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.dakiDirectoryName == null ? 0 : this.dakiDirectoryName.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         Daki other = (Daki)obj;
         return Objects.equals(this.dakiDirectoryName, other.dakiDirectoryName);
      } else {
         return false;
      }
   }

   public int compareTo(Daki o) {
      return this.dakiDirectoryName.compareTo(o.dakiDirectoryName);
   }

   public String toString() {
      return "Daki [dakiDirectoryName=" + this.dakiDirectoryName + ']';
   }
}
