package dev.just.manager.proxyManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dev.just.manager.IMinecraft;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.apache.commons.io.FileUtils;
import dev.just.protect.runtime.I0O1l0I1;

public class ProxyManager implements IMinecraft {
   private final File CONFIG_PATH = new File(MinecraftClient.getInstance().runDirectory, "files/proxy.ew");
   public HashMap<String, Proxy> accounts = new HashMap<>();
   public String lastPlayerName = "";
   public static boolean proxyEnabled = false;
   public static Proxy proxy = new Proxy();
   public static Proxy lastUsedProxy = new Proxy();
   public static ButtonWidget proxyMenuButton;

   public static String getLastUsedProxyIp() {
      return lastUsedProxy.ipPort.isEmpty() ? "yok" : lastUsedProxy.getIp();
   }

   public void init() {
      try {
         if (!this.CONFIG_PATH.exists() && !this.CONFIG_PATH.createNewFile()) {
            System.out.println(I0O1l0I1.b("UHJveHkgeWFwxLFsYW5kxLFybWEgZG9zeWFzxLEgb2x1xZ90dXJ1bHVya2VuIGhhdGEgb2x1xZ90dS4="));
            return;
         }

         String content = FileUtils.readFileToString(this.CONFIG_PATH, StandardCharsets.UTF_8);
         if (!content.isEmpty()) {
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            proxyEnabled = json.get("proxy-enabled").getAsBoolean();
            Type type = (new TypeToken<HashMap<String, Proxy>>() {
            }).getType();
            this.accounts = (HashMap<String, Proxy>)new Gson().fromJson(json.get("accounts"), type);
            if (this.accounts == null) {
               this.accounts = new HashMap<>();
            }
         }
      } catch (Exception var4) {
         System.out.println(I0O1l0I1.b("UHJveHkgeWFwxLFsYW5kxLFybWFzxLEgb2t1bnVya2VuIGhhdGEgb2x1xZ90dS4="));
         var4.printStackTrace();
      }
   }

   public void setDefaultProxy(Proxy proxy) {
      this.accounts.put("", proxy);
   }

   public void saveConfig() {
      try {
         JsonObject json = new JsonObject();
         json.addProperty("proxy-enabled", proxyEnabled);
         json.add("accounts", new Gson().toJsonTree(this.accounts));
         FileUtils.write(this.CONFIG_PATH, new GsonBuilder().setPrettyPrinting().create().toJson(json), StandardCharsets.UTF_8);
      } catch (IOException var2) {
         System.out.println(I0O1l0I1.b("UHJveHkgeWFwxLFsYW5kxLFybWFzxLEga2F5ZGVkaWxpcmtlbiBoYXRhIG9sdcWfdHUu"));
         var2.printStackTrace();
      }
   }
}
