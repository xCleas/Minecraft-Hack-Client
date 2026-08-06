package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.manager.macroManager.Macro;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.util.KeyMappings;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;

public class MacroCommand extends Command {
   public MacroCommand() {
      super("macro");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      SuggestionProvider<CommandSource> keySuggestions = (context, builder1) -> {
         for (String key : KeyMappings.getAllKeys()) {
            builder1.suggest(key);
         }

         return builder1.buildFuture();
      };
      SuggestionProvider<CommandSource> macroSuggestions = (context, builder1) -> {
         for (Macro macro : Manager.MACROS_MANAGER.getMacros()) {
            String keyName = KeyMappings.keyMappings(macro.getKey());
            if (keyName != null) {
               builder1.suggest(keyName);
            }
         }

         return builder1.buildFuture();
      };
      RequiredArgumentBuilder<CommandSource, String> addKeyArg = arg("key", StringArgumentType.word()).suggests(keySuggestions);
      RequiredArgumentBuilder<CommandSource, String> addMessageArg = (RequiredArgumentBuilder<CommandSource, String>)arg("message", StringArgumentType.greedyString())
         .executes(
            ctx -> {
               String keyName = StringArgumentType.getString(ctx, "key").toUpperCase();
               Integer key = KeyMappings.keyCode(keyName);
               if (key == null) {
                  ClientManager.message(Formatting.RED + I0O1l0I1.b("VHXFnyBidWx1bmFtYWTEsTog") + keyName);
                  return 1;
               } else {
                  String message = StringArgumentType.getString(ctx, "message");
                  Manager.MACROS_MANAGER.addMacros(new Macro(message, key));
                  ClientManager.message(
                     ""
                        + Formatting.GREEN
                        + Formatting.RED
                        + keyName
                        + Formatting.WHITE
                        + I0O1l0I1.b("IHR1xZ91bmEg")
                        + Formatting.RED
                        + message
                        + Formatting.WHITE
                        + I0O1l0I1.b("IGtvbXV0dXlsYSBtYWtybyBla2xlbmRpLg==")
                  );
                  return 1;
               }
            }
         );
      addKeyArg.then(addMessageArg);
      builder.then(literal("add").then(addKeyArg));
      RequiredArgumentBuilder<CommandSource, String> removeKeyArg = (RequiredArgumentBuilder<CommandSource, String>)arg("key", StringArgumentType.word())
         .suggests(macroSuggestions)
         .executes(ctx -> {
            String keyName = StringArgumentType.getString(ctx, "key").toUpperCase();
            Integer key = KeyMappings.keyCode(keyName);
            if (key == null) {
               ClientManager.message(Formatting.RED + I0O1l0I1.b("VHXFnyBidWx1bmFtYWTEsTog") + keyName);
               return 1;
            } else {
               Macro macro = Manager.MACROS_MANAGER.getMacroByKey(key);
               if (macro == null) {
                  ClientManager.message(Formatting.RED + keyName + I0O1l0I1.b("IHR1xZ91bmRhIGF0YW5txLHFnyBiaXIgbWFrcm8geW9rLg=="));
                  return 1;
               } else {
                  Manager.MACROS_MANAGER.deleteMacro(key);
                  ClientManager.message(Formatting.GREEN + keyName + I0O1l0I1.b("IHR1xZ91bmRha2kgbWFrcm8gc2lsaW5kaS4="));
                  return 1;
               }
            }
         });
      builder.then(literal("remove").then(removeKeyArg));
      builder.then(
         literal("list")
            .executes(
               ctx -> {
                  if (Manager.MACROS_MANAGER.getMacros().isEmpty()) {
                     ClientManager.message(I0O1l0I1.b("TWFrcm8gbGlzdGVzaSBib8WfLg=="));
                  } else {
                     ClientManager.message(Formatting.GREEN + I0O1l0I1.b("TWFrcm8gTGlzdGVzaTo="));
                     Manager.MACROS_MANAGER
                        .getMacros()
                        .forEach(
                           macro -> {
                              String keyName = KeyMappings.keyMappings(macro.getKey());
                              ClientManager.message(
                                 Formatting.WHITE
                                    + I0O1l0I1.b("S29tdXQ6IA==")
                                    + Formatting.RED
                                    + macro.getMessage()
                                    + Formatting.WHITE
                                    + I0O1l0I1.b("LCBUdcWfOiA=")
                                    + Formatting.RED
                                    + keyName
                              );
                           }
                        );
                  }

                  return 1;
               }
            )
      );
      builder.then(literal("clear").executes(ctx -> {
         if (Manager.MACROS_MANAGER.getMacros().isEmpty()) {
            ClientManager.message(Formatting.RED + I0O1l0I1.b("TWFrcm8gbGlzdGVzaSB6YXRlbiBib8WfLg=="));
         } else {
            Manager.MACROS_MANAGER.getMacros().clear();
            Manager.MACROS_MANAGER.updateFile();
            ClientManager.message(Formatting.GREEN + I0O1l0I1.b("TWFrcm8gbGlzdGVzaSBiYcWfYXLEsXlsYSB0ZW1pemxlbmRpLg=="));
         }

         return 1;
      }));
   }
}
