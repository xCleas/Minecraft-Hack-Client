package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.I0O1l0I1;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;

public class StaffCommand extends Command {
   public StaffCommand() {
      super("staff");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> root) {
      root.then(literal("add").then(arg("name", StringArgumentType.word()).suggests(this::suggestOnlinePlayers).executes(ctx -> {
         String name = StringArgumentType.getString(ctx, "name");
         if (Manager.STAFF_MANAGER.getStaffNames().contains(name)) {
            ClientManager.message(Formatting.RED + I0O1l0I1.b("QnUgb3l1bmN1IHphdGVuIFlldGtpbGkgTGlzdGVzaW5kZSE="));
         } else {
            Manager.STAFF_MANAGER.addStaff(name);
            ClientManager.message(Formatting.GREEN + I0O1l0I1.b("S3VsbGFuxLFjxLEgYWTEsSA=") + Formatting.WHITE + name + Formatting.GREEN + I0O1l0I1.b("IHlldGtpbGkgbGlzdGVzaW5lIGVrbGVuZGku"));
         }

         return 1;
      })));
      root.then(
         literal("remove")
            .then(
               arg("name", StringArgumentType.word())
                  .suggests(this::suggestExistingStaff)
                  .executes(
                     ctx -> {
                        String name = StringArgumentType.getString(ctx, "name");
                        if (Manager.STAFF_MANAGER.getStaffNames().contains(name)) {
                           Manager.STAFF_MANAGER.removeStaff(name);
                           ClientManager.message(
                              Formatting.GREEN + I0O1l0I1.b("S3VsbGFuxLFjxLEgYWTEsSA=") + Formatting.WHITE + name + Formatting.GREEN + I0O1l0I1.b("IHlldGtpbGkgbGlzdGVzaW5kZW4gc2lsaW5kaS4=")
                           );
                        } else {
                           ClientManager.message(Formatting.RED + I0O1l0I1.b("QnUgb3l1bmN1IHlldGtpbGkgbGlzdGVzaW5kZSBidWx1bmFtYWTEsSE="));
                        }

                        return 1;
                     }
                  )
            )
      );
      root.then(literal("clear").executes(ctx -> {
         if (Manager.STAFF_MANAGER.getStaffNames().isEmpty()) {
            ClientManager.message(Formatting.RED + I0O1l0I1.b("WWV0a2lsaSBsaXN0ZXNpIHphdGVuIGJvxZ8h"));
         } else {
            Manager.STAFF_MANAGER.clearStaffs();
            ClientManager.message(Formatting.GREEN + I0O1l0I1.b("WWV0a2lsaSBsaXN0ZXNpIHRlbWl6bGVuZGku"));
         }

         return 1;
      }));
      root.then(literal("list").executes(ctx -> {
         if (Manager.STAFF_MANAGER.getStaffNames().isEmpty()) {
            ClientManager.message(Formatting.GRAY + I0O1l0I1.b("WWV0a2lsaSBsaXN0ZXNpIGJvxZ8u"));
         } else {
            ClientManager.message(Formatting.GRAY + I0O1l0I1.b("S2F5xLF0bMSxIFlldGtpbGlsZXI6"));

            for (String name : Manager.STAFF_MANAGER.getStaffNames()) {
               ClientManager.message(Formatting.WHITE + "- " + name);
            }
         }

         return 1;
      }));
      root.then(literal("reload").executes(ctx -> {
         Manager.STAFF_MANAGER.reload();
         ClientManager.message(Formatting.GREEN + I0O1l0I1.b("WWV0a2lsaSBsaXN0ZXNpIGRvc3lhZGFuIHllbmlkZW4gecO8a2xlbmRpLg=="));
         return 1;
      }));
   }

   private CompletableFuture<Suggestions> suggestExistingStaff(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
      List<String> staffNames = Manager.STAFF_MANAGER.getStaffNames();
      return CommandSource.suggestMatching(staffNames, builder);
   }

   private CompletableFuture<Suggestions> suggestOnlinePlayers(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
      if (mc.player.networkHandler == null) {
         return builder.buildFuture();
      } else {
         List<String> playerNames = mc.player.networkHandler.getPlayerList().stream().map(p -> p.getProfile().getName()).collect(Collectors.toList());
         return CommandSource.suggestMatching(playerNames, builder);
      }
   }
}
