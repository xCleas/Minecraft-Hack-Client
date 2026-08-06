package dev.just.manager.macroManager;

import dev.just.manager.IMinecraft;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public class MacroManager {
   public List<Macro> macros = new ArrayList<>();
   private static final File macroFile = new File(MinecraftClient.getInstance().runDirectory, "\\files\\macros.ew");

   public List<Macro> getMacros() {
      return this.macros;
   }

   public void init() throws Exception {
      if (!macroFile.exists()) {
         macroFile.createNewFile();
      } else {
         this.readMacro();
      }
   }

   public void addMacros(Macro macro) {
      this.macros.add(macro);
      this.updateFile();
   }

   public Macro getMacroByKey(int key) {
      for (Macro macro : this.macros) {
         if (macro.getKey() == key) {
            return macro;
         }
      }

      return null;
   }

   public void deleteMacro(int key) {
      this.macros.removeIf(macro -> macro.getKey() == key);
      this.updateFile();
   }

   public void onKeyPressed(int key) {
      try {
         int processedKey = key >= 0 ? key : -(100 + key + 2);
         this.macros.stream().filter(macro -> macro.getKey() == processedKey).forEach(macro -> {
            String msg = macro.getMessage().trim();
            if (msg.startsWith("/")) {
               IMinecraft.mc.player.networkHandler.sendChatCommand(msg.substring(1));
            } else {
               IMinecraft.mc.player.networkHandler.sendChatMessage(msg);
            }
         });
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public void updateFile() {
      try {
         StringBuilder builder = new StringBuilder();
         this.macros.forEach(macro -> builder.append(macro.getMessage()).append(":").append(String.valueOf(macro.getKey()).toUpperCase()).append("\n"));
         Files.write(macroFile.toPath(), builder.toString().getBytes());
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private void readMacro() {
      try {
         FileInputStream fileInputStream = new FileInputStream(macroFile.getAbsolutePath());
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fileInputStream)));

         String line;
         while ((line = reader.readLine()) != null) {
            String curLine = line.trim();
            String command = curLine.split(":")[0];
            String key = curLine.split(":")[1];
            this.macros.add(new Macro(command, Integer.parseInt(key)));
         }

         reader.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }
}
