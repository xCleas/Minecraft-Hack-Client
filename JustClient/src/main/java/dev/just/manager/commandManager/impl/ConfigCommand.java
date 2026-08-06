package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.manager.commandManager.Command;
import java.io.IOException;
import java.util.List;
import net.minecraft.command.CommandSource;

public class ConfigCommand extends Command {
   public ConfigCommand() {
      super("cfg");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> root) {
      root.then(literal("save").then(arg("name", StringArgumentType.word()).suggests((ctx, builder) -> {
         Manager.CONFIG_MANAGER.getAllConfigurations().forEach(s -> builder.suggest(s.replace(".json", "")));
         return builder.buildFuture();
      }).executes(ctx -> {
         String name = StringArgumentType.getString(ctx, "name").toUpperCase();
         Manager.CONFIG_MANAGER.saveConfiguration(name);
         ClientManager.message(name + I0O1l0I1.b("IGlzaW1saSB5YXDEsWxhbmTEsXJtYSBrYXlkZWRpbGRp"));
         return 1;
      })));
      root.then(literal("load").then(arg("name", StringArgumentType.word()).suggests((ctx, builder) -> {
         Manager.CONFIG_MANAGER.getAllConfigurations().forEach(s -> builder.suggest(s.replace(".json", "")));
         return builder.buildFuture();
      }).executes(ctx -> {
         String name = StringArgumentType.getString(ctx, "name").toUpperCase();
         Manager.CONFIG_MANAGER.loadConfiguration(name, false);
         return 1;
      })));
      root.then(literal("remove").then(arg("name", StringArgumentType.word()).suggests((ctx, builder) -> {
         Manager.CONFIG_MANAGER.getAllConfigurations().forEach(s -> builder.suggest(s.replace(".json", "")));
         return builder.buildFuture();
      }).executes(ctx -> {
         String name = StringArgumentType.getString(ctx, "name").toUpperCase();
         Manager.CONFIG_MANAGER.deleteConfig(name);
         ClientManager.message(name + I0O1l0I1.b("IHlhcMSxbGFuZMSxcm1hc8SxIHNpbGluZGk="));
         return 1;
      })));
      root.then(literal("list").executes(ctx -> {
         List<String> configs = Manager.CONFIG_MANAGER.getAllConfigurations();
         if (configs.isEmpty()) {
            ClientManager.message(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgbGlzdGVzaSBib8Wf"));
         } else {
            ClientManager.message(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgbGlzdGVzaTo="));
            configs.forEach(s -> ClientManager.message(s.replace(".json", "")));
         }

         return 1;
      }));
      root.then(literal("reset").executes(ctx -> {
         Manager.FUNCTION_MANAGER.getFunctions().stream().filter(f -> f.state).forEach(f -> f.setState(false));
         ClientManager.message(I0O1l0I1.b("QXlhcmxhciBzxLFmxLFybGFuZMSx"));
         return 1;
      }));
      root.then(literal("dir").executes(ctx -> {
         try {
            Runtime.getRuntime().exec("explorer " + Manager.CONFIG_MANAGER.CONFIG_DIR.getAbsolutePath());
         } catch (IOException var2) {
            ClientManager.message(I0O1l0I1.b("S2xhc8O2ciBhw6fEsWzEsXJrZW4gaGF0YSBvbHXFn3R1OiA=") + var2.getMessage());
         }

         return 1;
      }));
      root.then(literal("clear").executes(ctx -> {
         List<String> configs = Manager.CONFIG_MANAGER.getAllConfigurations();
         if (configs.isEmpty()) {
            ClientManager.message(I0O1l0I1.b("WWFwxLFsYW5kxLFybWEgbGlzdGVzaSB6YXRlbiBib8Wf"));
            return 1;
         } else {
            configs.forEach(Manager.CONFIG_MANAGER::deleteConfig);
            ClientManager.message(I0O1l0I1.b("VMO8bSB5YXDEsWxhbmTEsXJtYWxhciBzaWxpbmRp"));
            return 1;
         }
      }));
   }
}
