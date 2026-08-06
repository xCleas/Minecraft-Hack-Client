package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.modules.Function;
import dev.just.modules.FunctionManager;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.KeyMappings;
import java.util.concurrent.CompletableFuture;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;
import dev.just.protect.runtime.I0O1l0I1;

public class BindCommand extends Command {
   public BindCommand() {
      super("bind");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> root) {
      root.then(
         literal("add")
            .then(
               arg("module", StringArgumentType.word())
                  .suggests(this::suggestModules)
                  .then(arg("key", StringArgumentType.word()).suggests(this::suggestKeys).executes(ctx -> {
                     String moduleName = StringArgumentType.getString(ctx, "module");
                     String keyName = StringArgumentType.getString(ctx, "key").toUpperCase();
                     this.addKeyBinding(moduleName, keyName);
                     return 1;
                  }))
            )
      );
      root.then(
         literal("remove")
            .then(
               arg("module", StringArgumentType.word())
                  .suggests(this::suggestModules)
                  .then(arg("key", StringArgumentType.word()).suggests(this::suggestKeys).executes(ctx -> {
                     String moduleName = StringArgumentType.getString(ctx, "module");
                     String keyName = StringArgumentType.getString(ctx, "key").toUpperCase();
                     this.removeKeyBinding(moduleName, keyName);
                     return 1;
                  }))
            )
      );
      root.then(literal("list").executes(ctx -> {
         this.listBoundKeys();
         return 1;
      }));
      root.then(literal("clear").executes(ctx -> {
         this.clearAllBindings();
         return 1;
      }));
   }

   private CompletableFuture<Suggestions> suggestModules(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
      Manager.FUNCTION_MANAGER.getFunctions().forEach(f -> builder.suggest(f.name));
      return builder.buildFuture();
   }

   private CompletableFuture<Suggestions> suggestKeys(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
      KeyMappings.getAllKeys().forEach(builder::suggest);
      return builder.buildFuture();
   }

   private void listBoundKeys() {
      ClientManager.message(Formatting.GRAY + I0O1l0I1.b("VMO8bSBNb2TDvGxsZXJpbiBMaXN0ZXNpOg=="));

      for (Function f : Manager.FUNCTION_MANAGER.getFunctions()) {
         if (f.bind != 0) {
            String keyName = KeyMappings.keyMappings(f.bind);
            ClientManager.message(f.name + " [" + Formatting.GRAY + keyName + Formatting.RESET + "]");
         }
      }
   }

   private void clearAllBindings() {
      for (Function f : Manager.FUNCTION_MANAGER.getFunctions()) {
         if (f != Manager.FUNCTION_MANAGER.clickGUI) {
            f.bind = 0;

            for (Setting setting : f.getSettings()) {
               if (setting instanceof BindBooleanSetting bindBooleanSetting) {
                  bindBooleanSetting.setKey(0);
               }

               if (setting instanceof BindSetting bindSetting) {
                  bindSetting.setKey(-1);
               }
            }
         }
      }

      ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.WHITE + I0O1l0I1.b("VMO8bSB0dcWfIGF0YW1hbGFyxLEgdGVtaXpsZW5kaS4="));
   }

   private void addKeyBinding(String moduleName, String keyName) {
      Function module = FunctionManager.get(moduleName);
      int key = KeyMappings.keyCode(keyName);
      if (module == null) {
         ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.WHITE + moduleName + I0O1l0I1.b("IG1vZMO8bMO8IGJ1bHVuYW1hZMSxLg=="));
      } else if (key == -1) {
         ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.WHITE + keyName + I0O1l0I1.b("IHR1xZ91IGJ1bHVuYW1hZMSxLg=="));
      } else {
         module.bind = key;
         ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.GOLD + module.name + Formatting.WHITE + I0O1l0I1.b("IG1vZMO8bMO8IA==") + Formatting.GOLD + keyName + Formatting.WHITE + I0O1l0I1.b("IHR1xZ91bmEgYXRhbmTEsS4="));
      }
   }

   private void removeKeyBinding(String moduleName, String keyName) {
      Function module = FunctionManager.get(moduleName);
      if (module == null) {
         ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.WHITE + moduleName + I0O1l0I1.b("IG1vZMO8bMO8IGJ1bHVuYW1hZMSxLg=="));
      } else {
         module.bind = 0;
         ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.GOLD + module.name + Formatting.WHITE + I0O1l0I1.b("IG1vZMO8bMO8bsO8biB0dcWfIGF0YW1hc8SxIGthbGTEsXLEsWxkxLEu"));
      }
   }
}
