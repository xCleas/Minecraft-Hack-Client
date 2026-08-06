package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.Strings;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.scoreboard.Team;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;

public class ParseCommand extends Command implements IMinecraft {
   private static final File PARSE_DIR = new File(mc.runDirectory, "files\\parser");
   private static final Map<String, String> PRIVILEGE_REPLACEMENTS = new HashMap<>();

   public ParseCommand() {
      super("parse");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("start").executes(context -> {
         this.parsePlayers();
         return 1;
      }));
      builder.then(literal("dir").executes(context -> {
         this.openDirectory();
         return 1;
      }));
   }

   private void parsePlayers() {
      if (!PARSE_DIR.exists() && !PARSE_DIR.mkdirs()) {
         ClientManager.message(Formatting.RED + Strings.b("RGl6aW4gb2x1xZ90dXJtYSBoYXRhc8SxIQ=="));
      } else {
         ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
         if (networkHandler == null) {
            ClientManager.message(Formatting.RED + Strings.b("U3VudWN1eWEgYmHEn2zEsSBkZcSfaWxzaW5peiE="));
         } else {
            Collection<PlayerListEntry> playerEntries = networkHandler.getPlayerList();
            Map<String, List<String>> privilegeGroups = new LinkedHashMap<>();
            String serverIp = "bilinmiyor";
            if (mc.getCurrentServerEntry() != null) {
               serverIp = mc.getCurrentServerEntry().address;
            }

            File file = this.getUniqueFileName(serverIp);

            try {
               try (FileWriter fileWriter = new FileWriter(file)) {
                  for (PlayerListEntry entry : playerEntries) {
                     Team team = entry.getScoreboardTeam();
                     if (team != null) {
                        Text prefix = team.getPrefix();
                        if (prefix != null) {
                           String privilege = this.replaceSymbols(prefix.getString());
                           String playerName = entry.getProfile().getName();
                           privilegeGroups.computeIfAbsent(privilege, k -> new ArrayList<>()).add(playerName);
                        }
                     }
                  }

                  if (!privilegeGroups.isEmpty()) {
                     int totalParsed = 0;

                     for (Entry<String, List<String>> group : privilegeGroups.entrySet()) {
                        fileWriter.write("// Rutbe: " + group.getKey() + "\n");

                        for (String player : group.getValue()) {
                           fileWriter.write(player + "\n");
                           totalParsed++;
                        }

                        fileWriter.write("\n");
                     }

                     ClientManager.message(Formatting.GREEN + Strings.b("QmHFn2FyxLFsxLEhIA==") + totalParsed + Strings.b("IG95dW5jdSB0YXJhbmTEsS4="));
                     ClientManager.message(Formatting.GRAY + "Dosya: " + file.getName());
                     return;
                  }

                  ClientManager.message(Formatting.YELLOW + Strings.b("VGFiIGxpc3Rlc2luZGUgcsO8dGJlIGJ1bHVuYW1hZMSxIQ=="));
               }
            } catch (Exception var15) {
               ClientManager.message(Formatting.RED + Strings.b("SGF0YTog") + var15.getMessage());
            }
         }
      }
   }

   private File getUniqueFileName(String baseName) {
      String safeName = baseName.replaceAll("[^a-zA-Z0-9.-]", "_");
      File file = new File(PARSE_DIR, safeName + ".txt");

      for (int counter = 2; file.exists(); counter++) {
         file = new File(PARSE_DIR, safeName + "_" + counter + ".txt");
      }

      return file;
   }

   private String replaceSymbols(String prefix) {
      for (Entry<String, String> entry : PRIVILEGE_REPLACEMENTS.entrySet()) {
         if (prefix.contains(entry.getKey())) {
            return entry.getValue();
         }
      }

      return prefix;
   }

   private void openDirectory() {
      try {
         Runtime.getRuntime().exec("explorer \"" + PARSE_DIR.getAbsolutePath() + "\"");
      } catch (IOException var2) {
         ClientManager.message(Formatting.RED + Strings.b("S2xhc8O2ciBhw6fEsWxhbWFkxLE6IA==") + var2.getMessage());
      }
   }

   static {
      String[] mods = new String[]{"§a●§f §fꔥ §7§7", "§c●§f §fꔥ §7§7", "§6●§f §fꔥ §7§7"};

      for (String s : mods) {
         PRIVILEGE_REPLACEMENTS.put(s, "MODER");
      }

      String[] helpers = new String[]{"§a●§f §fꔉ §7§7", "§c●§f §fꔉ §7§7", "§6●§f §fꔉ §7§7"};

      for (String s : helpers) {
         PRIVILEGE_REPLACEMENTS.put(s, "HELPER");
      }

      String[] media = new String[]{"§a●§f §fꔁ §7§7", "§c●§f §fꔁ §7§7", "§6●§f §fꔁ §7§7"};

      for (String s : media) {
         PRIVILEGE_REPLACEMENTS.put(s, "MEDIA");
      }

      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꕠ §7§7", "D.HELPER");
      PRIVILEGE_REPLACEMENTS.put("§c●§f §fꕒ §7§7", "RABBIT");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꕄ §7§7", "DRACULA");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꕈ §7§7", "COBRA");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꕀ §7§7", "HYDRA");
      PRIVILEGE_REPLACEMENTS.put("§c●§f §fꕖ §7§7", "BUNNY");
      PRIVILEGE_REPLACEMENTS.put("§c●§f §fꔶ §7§7", "TIGER");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꔨ §7§7", "DRAGON");
      PRIVILEGE_REPLACEMENTS.put("§c●§f §fꔤ §7§7", "IMPERATOR");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꔠ §7§7", "MAGISTER");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꔖ §7§7", "OVERLORD");
      PRIVILEGE_REPLACEMENTS.put("§c●§f §fꔒ §7§7", "AVENGER");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꔈ §7§7", "TITAN");
      PRIVILEGE_REPLACEMENTS.put("§a●§f §fꔄ §7§7", "HERO");
      PRIVILEGE_REPLACEMENTS.put("§c●§f §fꔀ §7§7", "PLAYER");
   }
}
