package dev.just.manager.configManager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.manager.Manager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public final class ConfigManager implements IMinecraft {
   public final File CONFIG_DIR = new File(MinecraftClient.getInstance().runDirectory, "\\files\\configs");
   private final File autoCfgDir = new File(MinecraftClient.getInstance().runDirectory, "\\files\\configs\\OTOCONFIG.cfg");
   private final JsonParser jsonParser = new JsonParser();

   public void init() throws Exception {
      if (!this.CONFIG_DIR.exists()) {
         this.CONFIG_DIR.mkdirs();
      } else if (this.autoCfgDir.exists()) {
         this.loadConfiguration("OTOCONFIG", true);
         System.out.println(I0O1l0I1.b("WytdIE90b21hdGlrIHlhcMSxbGFuZMSxcm1hIHnDvGtsZW5peW9yLi4u"));
      } else {
         System.out.println(I0O1l0I1.b("Wy1dIE90b21hdGlrIHlhcMSxbGFuZMSxcm1hIGRvc3lhc8SxIGJ1bHVuYW1hZMSx"));
         this.autoCfgDir.createNewFile();
      }

      // HUD, TargetESP ve TargetHUD her zaman otomatik acik olsun
      if (Manager.FUNCTION_MANAGER != null) {
         if (Manager.FUNCTION_MANAGER.hud != null && !Manager.FUNCTION_MANAGER.hud.isState()) {
            Manager.FUNCTION_MANAGER.hud.setState(true);
         }
         if (Manager.FUNCTION_MANAGER.targetESP != null && !Manager.FUNCTION_MANAGER.targetESP.isState()) {
            Manager.FUNCTION_MANAGER.targetESP.setState(true);
         }
         if (Manager.FUNCTION_MANAGER.targetHUD != null && !Manager.FUNCTION_MANAGER.targetHUD.isState()) {
            Manager.FUNCTION_MANAGER.targetHUD.setState(true);
         }
      }
   }

   public List<String> getAllConfigurations() {
      List<String> configurations = new ArrayList<>();
      File[] files = this.CONFIG_DIR.listFiles();
      if (files != null) {
         for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".cfg")) {
               String configName = file.getName().substring(0, file.getName().lastIndexOf(".cfg"));
               configurations.add(configName);
            }
         }
      }

      return configurations;
   }

   public void loadConfiguration(String configuration, boolean start) {
      Config config = this.findConfig(configuration);
      if (config == null) {
         ClientManager.message(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgZG9zeWFzxLEgJw==") + configuration + I0O1l0I1.b("JyBidWx1bmFtYWTEsSE="));
      } else {
         try {
            JsonElement element = this.readJsonFromFile(config.getFile());
            if (element != null && element.isJsonObject()) {
               config.load(element.getAsJsonObject(), configuration, start);
               ClientManager.message(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgJw==") + configuration + I0O1l0I1.b("JyBiYcWfYXLEsXlsYSB5w7xrbGVuZGku"));
            } else {
               this.saveConfiguration(configuration);
               ClientManager.message(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgJw==") + configuration + I0O1l0I1.b("JyBib3p1ayBvbGR1xJ91IHZleWEgYnVsdW5tYWTEscSfxLEgacOnaW4geWVuaWRlbiBvbHXFn3R1cnVsZHUu"));
            }
         } catch (JsonParseException var5) {
            ClientManager.message("'" + configuration + I0O1l0I1.b("JyBkb3N5YXPEsW5kYWtpIEpTT04gdmVyaXNpIGF5csSxxZ90xLFyxLFsxLFya2VuIGhhdGEgb2x1xZ90dSE="));
         } catch (NullPointerException var6) {
            ClientManager.message("'" + configuration + I0O1l0I1.b("JyB5YXDEsWxhbmTEsXJtYXPEsSBnw7xuY2VsIGRlxJ9pbCB2ZSB5w7xrbGVuZW1peW9yIQ=="));
         } catch (ClassCastException var7) {
            ClientManager.message("'" + configuration + I0O1l0I1.b("JyB5YXDEsWxhbmTEsXJtYXPEsSBib8WfIHZleWEgaGF0YWzEsSB2ZXJpIGnDp2VyaXlvciE="));
         }
      }
   }

   private JsonElement readJsonFromFile(File file) {
      try {
         JsonElement var3;
         try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            var3 = this.jsonParser.parse(reader);
         }

         return var3;
      } catch (IOException var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public void saveConfiguration(String configuration) {
      Config config = this.findConfig(configuration);
      if (config == null) {
         config = new Config(configuration);
      }

      this.writeJsonToFile(config.getFile(), config.save());
      System.out.println(I0O1l0I1.b("WytdIFlhcMSxbGFuZMSxcm1hICc=") + configuration + I0O1l0I1.b("JyBiYcWfYXLEsXlsYSBrYXlkZWRpbGRpLg=="));
   }

   private void writeJsonToFile(File file, JsonElement jsonElement) {
      try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
         String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
         writer.write(jsonString);
      } catch (IOException var8) {
         var8.printStackTrace();
      }
   }

   public Config findConfig(String configName) {
      if (configName == null) {
         return null;
      } else {
         return new File(this.CONFIG_DIR, configName + ".cfg").exists() ? new Config(configName) : null;
      }
   }

   public void deleteConfig(String configName) {
      if (configName != null) {
         Config config = this.findConfig(configName);
         if (config != null) {
            File file = config.getFile();
            if (file.exists()) {
               boolean deleted = file.delete();
               if (!deleted) {
                  System.out.println(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgZG9zeWFzxLEgc2lsaW5lbWVkaTogIA==") + file.getAbsolutePath());
               }
            }
         }
      }
   }
}
