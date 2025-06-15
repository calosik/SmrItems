package ru.SmrItems.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class Logger {
   private static File LOGS = new File("anchors.log");
   private static File CURRENT = new File("currentAnchors.txt");

   public static void log(String str) throws IOException {
      if (!LOGS.exists()) {
         LOGS.createNewFile();
      }

      FileWriter writer = new FileWriter(LOGS, true);
      writer.append(str).append("\n");
      writer.flush();
   }

   public static class Active {
      public static void add(String placer, int x, int y, int z) throws IOException {
         if (!Logger.CURRENT.exists()) {
            Logger.CURRENT.createNewFile();
         }

         FileWriter writer = new FileWriter(Logger.CURRENT, true);
         writer.append(placer + " - " + x + " " + y + " " + z).append("\n");
         writer.flush();
         writer.close();
      }

      public static void remove(int x, int y, int z) throws IOException {
         if (Logger.CURRENT.exists()) {
            List<String> out = (List)Files.lines(Logger.CURRENT.toPath()).filter((line) -> {
               return !line.endsWith(x + " " + y + " " + z);
            }).collect(Collectors.toList());
            Files.write(Logger.CURRENT.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
         }

      }
   }
}
