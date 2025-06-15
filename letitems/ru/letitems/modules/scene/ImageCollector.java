package ru.letitems.modules.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageCollector {
   public static final ImageCollector INSTANCE = new ImageCollector();
   private static final long REMOVAL_TIME_MILLS = 30000L;
   private final Map<String, ImageCollector.TempObjWrap<ImageCollector.ImagePuzzle[]>> puzzleMap = Collections.synchronizedMap(new HashMap());

   private ImageCollector() {
      Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
         synchronized(this.puzzleMap) {
            this.puzzleMap.values().removeIf((tempObjWrap) -> {
               return System.currentTimeMillis() - tempObjWrap.getCreationTimeMills() > 30000L;
            });
         }
      }, 0L, 60L, TimeUnit.SECONDS);
   }

   public void addPuzzle(String token, int partIndex, int maxParts, byte[] data) {
      synchronized(this.puzzleMap) {
         ImageCollector.TempObjWrap<ImageCollector.ImagePuzzle[]> tempObjWrap = (ImageCollector.TempObjWrap)this.puzzleMap.get(token);
         if (tempObjWrap == null) {
            tempObjWrap = new ImageCollector.TempObjWrap(new ImageCollector.ImagePuzzle[maxParts]);
         }

         ImageCollector.ImagePuzzle[] array = (ImageCollector.ImagePuzzle[])tempObjWrap.getObject();
         if (array == null) {
            array = new ImageCollector.ImagePuzzle[maxParts];
         }

         if (partIndex < array.length) {
            ImageCollector.ImagePuzzle imagePuzzle = array[partIndex];
            if (imagePuzzle != null && imagePuzzle.data != null) {
               imagePuzzle.markAsDead();
            } else {
               imagePuzzle = new ImageCollector.ImagePuzzle(data);
            }

            array[partIndex] = imagePuzzle;
         }

         this.puzzleMap.put(token, tempObjWrap);
      }
   }

   public List<Integer> getDeadPuzzlesIndexes(String token) {
      synchronized(this.puzzleMap) {
         List<Integer> list = new ArrayList();
         ImageCollector.TempObjWrap<ImageCollector.ImagePuzzle[]> tempObjWrap = (ImageCollector.TempObjWrap)this.puzzleMap.get(token);
         if (tempObjWrap != null) {
            ImageCollector.ImagePuzzle[] puzzles = (ImageCollector.ImagePuzzle[])tempObjWrap.getObject();

            for(int i = 0; i < puzzles.length; ++i) {
               ImageCollector.ImagePuzzle imagePuzzle = puzzles[i];
               if (imagePuzzle.isDead || imagePuzzle.data == null) {
                  list.add(i);
               }
            }
         }

         return list;
      }
   }

   public byte[] collectPuzzles(String token) {
      return null;
   }

   public void clearPuzzles(String token) {
      synchronized(this.puzzleMap) {
         this.puzzleMap.remove(token);
      }
   }

   private static class TempObjWrap<T> {
      private final T object;
      private final long creationTimeMills;

      TempObjWrap(T o) {
         this.object = o;
         this.creationTimeMills = System.currentTimeMillis();
      }

      public T getObject() {
         return this.object;
      }

      long getCreationTimeMills() {
         return this.creationTimeMills;
      }
   }

   private static class ImagePuzzle {
      private boolean isDead;
      private final byte[] data;

      private ImagePuzzle(byte[] data) {
         this.data = data;
      }

      private void markAsDead() {
         this.isDead = true;
      }

      // $FF: synthetic method
      ImagePuzzle(byte[] x0, Object x1) {
         this(x0);
      }
   }
}
