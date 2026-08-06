package dev.just.manager;

import dev.just.manager.accountManager.AccountManager;
import dev.just.manager.commandManager.CommandManager;
import dev.just.manager.configManager.ConfigManager;
import dev.just.manager.dragManager.DragManager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.friendManager.FriendManager;
import dev.just.manager.ircManager.IrcManager;
import dev.just.manager.macroManager.MacroManager;
import dev.just.manager.modulesManager.ChestStealerManager;
import dev.just.manager.notificationManager.NotificationManager;
import dev.just.manager.proxyManager.ProxyManager;
import dev.just.manager.staffManager.StaffManager;
import dev.just.manager.themeManager.StyleManager;
import dev.just.modules.FunctionManager;
import dev.just.modules.combat.rotation.RotationController;
import dev.just.protect.UserProfile;

public class Manager {
   public static final RotationController ROTATION = RotationController.get();
   public static UserProfile USER_PROFILE;
   public static FunctionManager FUNCTION_MANAGER;
   public static StyleManager STYLE_MANAGER;
   public static NotificationManager NOTIFICATION_MANAGER;
   public static FriendManager FRIEND_MANAGER;
   public static ConfigManager CONFIG_MANAGER;
   public static MacroManager MACROS_MANAGER;
   public static StaffManager STAFF_MANAGER;
   public static CommandManager COMMAND_MANAGER;
   public static DragManager DRAG_MANAGER;
   public static SyncManager SYNC_MANAGER;
   public static FontUtils FONT_MANAGER;
   public static AccountManager ACCOUNT_MANAGER;
   public static ChestStealerManager CHESTSTEALER_MANAGER;
   public static IrcManager IRC_MANAGER;
   public static ProxyManager PROXY_MANAGER;
}
