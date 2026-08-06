package dev.just;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKey;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.SyncManager;
import dev.just.manager.accountManager.AccountManager;
import dev.just.manager.commandManager.CommandManager;
import dev.just.manager.configManager.ConfigManager;
import dev.just.manager.dragManager.DragManager;
import dev.just.manager.dragManager.Dragging;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.friendManager.FriendManager;
import dev.just.manager.ircManager.IrcManager;
import dev.just.manager.macroManager.MacroManager;
import dev.just.manager.modulesManager.ChestStealerManager;
import dev.just.manager.notificationManager.NotificationManager;
import dev.just.manager.proxyManager.ProxyManager;
import dev.just.manager.staffManager.StaffManager;
import dev.just.manager.themeManager.StyleManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionManager;
import dev.just.modules.misc.UnHook;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.NativeHelper;
import dev.just.protect.runtime.UltraProtect;
import dev.just.protect.runtime.Strings;
import dev.just.screens.dropdown.ClickGUI;
import dev.just.util.color.ColorUtil;
import dev.just.util.player.AudioUtil;
import dev.just.util.render.providers.ResourceProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public final class JustClient implements ModInitializer {
   private static JustClient instance;
   private File directory;
   private File directoryAddon;
   public final String name = Strings.CLIENT_NAME();
   boolean initialized;

   public static JustClient getInstance() {
      return instance;
   }

   public JustClient() {
      instance = this;
   }

   private void initDirectories() {
      if (this.directory == null && MinecraftClient.getInstance() != null) {
         this.directory = new File(MinecraftClient.getInstance().runDirectory, Strings.FILES_DIR());
         this.directoryAddon = new File(MinecraftClient.getInstance().runDirectory, Strings.MODULES_DIR());
      }
   }

   private void setupProtection() {
      NativeHelper.setProfile();
   }

   public void onInitialize() {
      // ULTRA KORUMA SISTEMI - SOFT FAIL MODU (TLauncher uyumlu)
      UltraProtect.init();

      this.setupProtection();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         try {
            this.shutDown();
         } catch (Exception var2) {
         }
      }));
   }

   public void init() {
      this.ensureDirectoryExists();

      try {
         Manager.SYNC_MANAGER = new SyncManager();
         Manager.FUNCTION_MANAGER = new FunctionManager();
         Manager.STYLE_MANAGER = new StyleManager();
         Manager.STYLE_MANAGER.init();
         Manager.ACCOUNT_MANAGER = new AccountManager();
         Manager.ACCOUNT_MANAGER.init();
         Manager.FONT_MANAGER = new FontUtils();
         Manager.FONT_MANAGER.init();
         Manager.COMMAND_MANAGER = new CommandManager();
         Manager.DRAG_MANAGER = new DragManager();
         Manager.DRAG_MANAGER.init();
         Manager.MACROS_MANAGER = new MacroManager();
         Manager.MACROS_MANAGER.init();
         Manager.FRIEND_MANAGER = new FriendManager();
         Manager.FRIEND_MANAGER.init();
         Manager.STAFF_MANAGER = new StaffManager();
         Manager.STAFF_MANAGER.init();
         Manager.NOTIFICATION_MANAGER = new NotificationManager();
         Manager.CHESTSTEALER_MANAGER = new ChestStealerManager();
         Manager.PROXY_MANAGER = new ProxyManager();
         Manager.PROXY_MANAGER.init();
         Manager.IRC_MANAGER = new IrcManager();
         Manager.IRC_MANAGER.connect(Manager.USER_PROFILE.getName());
         Manager.CONFIG_MANAGER = new ConfigManager();
         Manager.CONFIG_MANAGER.init();
         ColorUtil.loadImage(ResourceProvider.color_image);
         if (Manager.FUNCTION_MANAGER.clientSounds.check.get(Strings.b("SG/FnWdlbGRpbg=="))) {
            AudioUtil.playSound(Strings.b("am9pbi53YXY="));
         }

         this.initialized = true;
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public void keyPress(int key) {
      int processedKey = key >= 0 ? key : -(100 + key + 2);
      Event.call(new EventKey(processedKey));
      if (key == UnHook.unHookKey.getKey() && ClientManager.legitMode) {
         UnHook.functionsToBack.forEach(function -> function.setState(true));
         File folder = new File(Strings.b("QzpcSnVzdENsaWVudA=="));
         if (folder.exists()) {
            try {
               Path folderPathObj = folder.toPath();
               DosFileAttributeView attributes = Files.getFileAttributeView(folderPathObj, DosFileAttributeView.class);
               attributes.setHidden(false);
            } catch (IOException var8) {
            }
         }

         UnHook.functionsToBack.clear();
         ClientManager.legitMode = false;
      }

      if (!ClientManager.legitMode) {
         for (Function module : Manager.FUNCTION_MANAGER.getFunctions()) {
            if (module.bind == processedKey) {
               module.toggle();
            }

            for (Setting setting : module.getSettings()) {
               if (setting instanceof BindBooleanSetting bindSetting) {
                  bindSetting.onKeyPress(key, true);
               }
            }
         }

         if (key == Manager.FUNCTION_MANAGER.clickGUI.getBindCode()) {
            MinecraftClient.getInstance().setScreen(new ClickGUI());
         }

         if (Manager.MACROS_MANAGER != null) {
            Manager.MACROS_MANAGER.onKeyPressed(key);
         }
      }
   }

   public void shutDown() {
      Manager.DRAG_MANAGER.save();
      Manager.ACCOUNT_MANAGER.saveAccounts();
      Manager.ACCOUNT_MANAGER.saveLastAlt();
      Manager.CONFIG_MANAGER.saveConfiguration("autocfg");
      Manager.IRC_MANAGER.shutdown();
      Manager.FUNCTION_MANAGER.globals.clear();
      System.out.println("[-] Client shutdown");
   }

   public static void openURL(String url) {
      try {
         String os = System.getProperty("os.name").toLowerCase();
         if (os.contains("win")) {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
         } else if (os.contains("mac")) {
            Runtime.getRuntime().exec(new String[]{"open", url});
         } else {
            Runtime.getRuntime().exec(new String[]{"xdg-open", url});
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public Dragging createDrag(Function function, String name, float x, float y) {
      DragManager.draggables.put(name, new Dragging(function, name, x, y));
      return DragManager.draggables.get(name);
   }

   private void ensureDirectoryExists() {
      this.initDirectories();
      if (!this.directory.exists() && !this.directory.mkdirs()) {
         System.err.println("Failed to create directory: " + this.directory.getAbsolutePath());
      }

      if (!this.directoryAddon.exists() && !this.directoryAddon.mkdirs()) {
         System.err.println("Failed to create directory: " + this.directoryAddon.getAbsolutePath());
      }
   }

   public boolean isInitialized() {
      return this.initialized;
   }
}
