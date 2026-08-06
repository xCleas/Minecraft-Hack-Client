package dev.just.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.just.events.Event;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.notificationManager.NotificationType;
import dev.just.modules.misc.ClientSounds;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.modules.setting.TextSetting;
import dev.just.util.player.AudioUtil;
import dev.just.protect.runtime.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Function implements IMinecraft {
   private final FunctionAnnotation initerFunctions = this.getClass().getAnnotation(FunctionAnnotation.class);
   public String name;
   public String keywords;
   private Type category;
   public int bind;
   public String desc;
   public boolean state;
   public boolean expanded;
   private final ArrayList<Setting> settings = new ArrayList<>();

   public Function() {
      this.initializeProperties();
   }

   public Function(String name, Type category) {
      this.name = name;
      this.category = category;
      this.state = false;
      this.bind = 0;
   }

   private void initializeProperties() {
      this.name = this.initerFunctions.name();
      this.desc = this.initerFunctions.desc();
      this.category = this.initerFunctions.type();
      this.keywords = Arrays.toString((Object[])this.initerFunctions.keywords());
      this.state = false;
      this.bind = this.initerFunctions.key();
   }

   public abstract void onEvent(Event var1);

   public final void setState(boolean enabled) {
      if (this.state != enabled) {
         this.state = enabled;

         try {
            if (this.state) {
               this.onEnable();
            } else {
               this.onDisable();
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         // Otomatik kaydet
         autoSave();
      }
   }

   // Otomatik kaydetme fonksiyonu
   public static void autoSave() {
      try {
         if (Manager.CONFIG_MANAGER != null) {
            Manager.CONFIG_MANAGER.saveConfiguration("OTOCONFIG");
         }
      } catch (Exception e) {
         // Sessizce devam et
      }
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   public void addSettings(Setting... options) {
      this.settings.addAll(Arrays.asList(options));
   }

   public void toggle() {
      this.state = !this.state;

      try {
         if (this.state) {
            this.onEnable();
            this.playSound(true);
         } else {
            this.onDisable();
            this.playSound(false);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      Manager.NOTIFICATION_MANAGER
         .add(
            this.state ? NotificationType.SUCCESS : NotificationType.REMOVED,
            this.name,
            this.state ? Strings.b("QWt0aWY=") : Strings.b("RGV2cmUgRMSxxZ/EsQ=="),
            2
         );

      // Otomatik kaydet
      autoSave();
   }

   private void playSound(boolean enable) {
      ClientSounds clientSounds = Manager.FUNCTION_MANAGER.clientSounds;
      if (clientSounds.state) {
         String mode = clientSounds.mode.get();
         String soundFile;
         switch (mode) {
            case "Tip-1":
               soundFile = enable ? "nuron.wav" : "nuroff.wav";
               break;
            case "Tip-2":
               soundFile = enable ? "akron.wav" : "akroff.wav";
               break;
            case "Tip-3":
               soundFile = enable ? "celon.wav" : "celoff.wav";
               break;
            case "Tip-4":
               soundFile = enable ? "enableold.wav" : "disableold.wav";
               break;
            default:
               return;
         }

         AudioUtil.playSound(soundFile);
      }
   }

   public Type getCategory() {
      return this.category;
   }

   public boolean isState() {
      return this.state;
   }

   public List<Setting> getSettings() {
      return this.settings;
   }

   public int getBindCode() {
      return this.bind;
   }

   public void setBindCode(int key) {
      if (this.bind != key) {
         this.bind = key;
         autoSave();
      }
   }

   public JsonObject save() {
      JsonObject object = new JsonObject();
      object.addProperty("state", this.state);
      object.addProperty("keyIndex", this.bind);
      JsonObject propertiesObject = new JsonObject();

      for (Setting set : this.settings) {
         if (set instanceof BindBooleanSetting bbs) {
            JsonObject bbsObject = new JsonObject();
            bbsObject.addProperty("state", bbs.get());
            bbsObject.addProperty("bindKey", bbs.getBindKey());
            propertiesObject.add(bbs.getName(), bbsObject);
         } else if (set instanceof BooleanSetting bs) {
            propertiesObject.addProperty(set.getName(), bs.get());
         } else if (set instanceof MultiSetting ms) {
            propertiesObject.addProperty(set.getName(), ms.getConfigValue());
         } else if (set instanceof ModeSetting ms) {
            propertiesObject.addProperty(set.getName(), ms.get());
         } else if (set instanceof SliderSetting ss) {
            propertiesObject.addProperty(set.getName(), ss.get());
         } else if (set instanceof BindSetting bs) {
            propertiesObject.addProperty(set.getName(), bs.getKey());
         } else if (set instanceof TextSetting ts) {
            propertiesObject.addProperty(set.getName(), ts.getValue());
         }
      }

      object.add("Ayarlar", propertiesObject);
      return object;
   }

   public void load(JsonObject object) {
      if (object != null) {
         if (object.has("state")) {
            this.setState(object.get("state").getAsBoolean());
         }

         if (object.has("keyIndex")) {
            this.setBindCode(object.get("keyIndex").getAsInt());
         }

         JsonElement settingsElement = object.get("Ayarlar");
         if (settingsElement != null && settingsElement.isJsonObject()) {
            JsonObject propertiesObject = settingsElement.getAsJsonObject();

            for (Setting set : this.settings) {
               String name = set.getName();
               if (propertiesObject.has(name)) {
                  if (set instanceof BindBooleanSetting) {
                     BindBooleanSetting bbs = (BindBooleanSetting)set;
                     if (propertiesObject.get(name).isJsonObject()) {
                        JsonObject bbsObject = propertiesObject.getAsJsonObject(name);
                        if (bbsObject.has("state")) {
                           bbs.set(bbsObject.get("state").getAsBoolean());
                        }

                        if (bbsObject.has("bindKey")) {
                           bbs.setKey(bbsObject.get("bindKey").getAsInt());
                        }
                        continue;
                     }
                  }

                  if (set instanceof BooleanSetting bs) {
                     bs.set(propertiesObject.get(name).getAsBoolean());
                  } else if (set instanceof MultiSetting ms) {
                     ms.setConfigValue(propertiesObject.get(name).getAsString());
                  } else if (set instanceof ModeSetting ms) {
                     ms.set(propertiesObject.get(name).getAsString());
                  } else if (set instanceof SliderSetting ss) {
                     ss.set((double)propertiesObject.get(name).getAsFloat());
                  } else if (set instanceof BindSetting bs) {
                     bs.setKey(propertiesObject.get(name).getAsInt());
                  } else if (set instanceof TextSetting ts) {
                     ts.setValue(propertiesObject.get(name).getAsString());
                  }
               }
            }
         }
      }
   }
}
