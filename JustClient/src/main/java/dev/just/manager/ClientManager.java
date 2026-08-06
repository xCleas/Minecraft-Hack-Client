package dev.just.manager;

import dev.just.manager.themeManager.StyleManager;
import dev.just.mixin.iface.BossBarHudAccessor;
import dev.just.mixin.iface.MinecraftClientAccessor;
import dev.just.util.KeyMappings;
import dev.just.util.color.ColorUtil;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.util.math.MathUtil;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.util.Formatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextColor;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.level.storage.LevelStorage;

public class ClientManager implements IMinecraft {
   public static boolean legitMode = false;
   private static float fps = 0.0F;
   public static float TICK_TIMER = 1.0F;

   public static float getTPS() {
      return (float)Math.round(Manager.SYNC_MANAGER.tps * 10.0F) / 10.0F;
   }

   public static int getFps() {
      MinecraftClient client = mc;
      int currentFps = client != null ? client.getCurrentFps() : 0;
      fps = MathUtil.fast(fps, (float)currentFps, 6.0F);
      return Math.round(fps);
   }

   public static String getBps(Entity entity) {
      if (mc != null && mc.player != null) {
         double dx = entity.getX() - entity.prevX;
         double dz = entity.getZ() - entity.prevZ;
         return String.format(Locale.ROOT, "%.2f", Math.hypot(dx, dz) * 20.0);
      } else {
         return "0.00";
      }
   }

   public static String getPing() {
      MinecraftClient client = mc;
      if (client != null && client.player != null && client.getNetworkHandler() != null) {
         PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
         return entry == null ? "N/A" : Integer.toString(entry.getLatency());
      } else {
         return "N/A";
      }
   }

   public static float[] getHealthFromScoreboard(LivingEntity target) {
      float currentHealth = target.getHealth();
      float maxHealth = target.getMaxHealth();
      if (target instanceof PlayerEntity player) {
         Scoreboard scoreboard = player.getScoreboard();
         ScoreboardObjective found = null;

         for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            String displayName = objective.getDisplayName().getString();
            if (displayName.contains("Здоровья") || displayName.contains(I0O1l0I1.b("U2HEn2zEsWs=")) || displayName.contains("Can")) {
               found = objective;
               break;
            }
         }

         if (found != null) {
            currentHealth = (float)scoreboard.getOrCreateScore(player, found).getScore();
            maxHealth = 20.0F;
         }
      }

      return new float[]{currentHealth, maxHealth};
   }

   public static String getKey(int keyCode) {
      return KeyMappings.keyMappings(keyCode);
   }

   public static boolean playerIsPVP() {
      if (mc != null && mc.inGameHud != null) {
         BossBarHud bossOverlayGui = mc.inGameHud.getBossBarHud();
         Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor)bossOverlayGui).getBossBars();

         for (ClientBossBar bossInfo : bossBars.values()) {
            String nameStrLower = bossInfo.getName().getString().toLowerCase(Locale.ROOT);
            if (nameStrLower.contains("pvp") || nameStrLower.contains("пвп") || nameStrLower.contains(I0O1l0I1.b("c2F2YcWf")) || nameStrLower.contains(I0O1l0I1.b("ZMO8ZWxsbw=="))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void loginAccount(String name) {
      UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
      Session session = new Session(name, uuid, "invalid_token", Optional.empty(), Optional.empty(), Session.AccountType.MOJANG);
      MinecraftClient client = MinecraftClient.getInstance();
      ((MinecraftClientAccessor)client).setSession(session);
   }

   public static void message(String string) {
      if (mc != null && mc.player != null && mc.world != null && mc.inGameHud != null) {
         StyleManager theme = Manager.STYLE_MANAGER;
         int start = theme.getFirstColor();
         int end = theme.getSecondColor();
         mc.inGameHud.getChatHud().addMessage(applyGradient(string, start, end));
      }
   }

   public static String gradient(String message, int first, int end) {
      if (message != null && !message.isEmpty()) {
         int length = message.length();
         StringBuilder result = new StringBuilder(length * 9);
         float inv = length <= 1 ? 0.0F : 1.0F / (float)(length - 1);

         for (int i = 0; i < length; i++) {
            float progress = length == 1 ? 0.5F : (float)i * inv;
            int color = ColorUtil.interpolateColor(first, end, progress) & 16777215;
            String hex = Integer.toHexString(color);
            if (hex.length() < 6) {
               hex = "000000".substring(hex.length()) + hex;
            }

            result.append('§').append('#').append(hex).append(message.charAt(i));
         }

         return result.toString();
      } else {
         return "";
      }
   }

   private static Text applyGradient(String string, int startColor, int endColor) {
      MutableText component = Text.empty();
      String name = "Just Client";
      int length = name.length();
      float inv = length <= 1 ? 0.0F : 1.0F / (float)(length - 1);

      for (int i = 0; i < length; i++) {
         int rgb = ColorUtil.blendColors(startColor, endColor, length == 1 ? 0.5F : (float)i * inv) & 16777215;
         component.append(
            Text.literal(String.valueOf(name.charAt(i)))
               .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)).withBold(true))
         );
      }

      int gray = Color.GRAY.getRGB() & 16777215;
      component.append(Text.literal(" > ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gray)).withBold(true)));
      component.append(Text.literal(string).setStyle(Style.EMPTY.withFormatting(Formatting.RESET)));
      return component;
   }
}
