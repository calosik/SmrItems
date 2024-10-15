package ru.SmrItems.modules.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigFile {
   private Properties configProps;
   private String configFilePath;

   public ConfigFile(String configFilePath) {
      this.configFilePath = configFilePath;
      this.configProps = new Properties();
   }

   public void load() {
      try {
         InputStream input = new FileInputStream(this.configFilePath);
         Throwable var2 = null;

         try {
            this.configProps.load(input);
         } catch (Throwable var12) {
            var2 = var12;
            throw var12;
         } finally {
            if (input != null) {
               if (var2 != null) {
                  try {
                     input.close();
                  } catch (Throwable var11) {
                     var2.addSuppressed(var11);
                  }
               } else {
                  input.close();
               }
            }

         }
      } catch (IOException var14) {
         var14.printStackTrace();
      }

   }

   public void save() {
      try {
         OutputStream output = new FileOutputStream(this.configFilePath);
         Throwable var2 = null;

         try {
            this.configProps.store(output, (String)null);
         } catch (Throwable var12) {
            var2 = var12;
            throw var12;
         } finally {
            if (output != null) {
               if (var2 != null) {
                  try {
                     output.close();
                  } catch (Throwable var11) {
                     var2.addSuppressed(var11);
                  }
               } else {
                  output.close();
               }
            }

         }
      } catch (IOException var14) {
         var14.printStackTrace();
      }

   }

   public String getProperty(String key) {
      return this.configProps.getProperty(key);
   }

   public void setProperty(String key, String value) {
      this.configProps.setProperty(key, value);
   }
}
