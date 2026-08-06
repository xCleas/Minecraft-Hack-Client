package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.Strings;
import net.minecraft.util.Formatting;
import net.minecraft.item.Item;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;

public class ChestStealerCommand extends Command {
   public ChestStealerCommand() {
      super("cheststealer");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> root) {
      root.then(literal("add").then(arg("item", StringArgumentType.word()).suggests((ctx, builder) -> {
         for (Item item : Registries.ITEM) {
            String id = Registries.ITEM.getId(item).toString();
            builder.suggest(id.replace("minecraft:", ""));
         }

         return builder.buildFuture();
      }).executes(ctx -> {
         String input = StringArgumentType.getString(ctx, "item");
         String itemName = input.contains(":") ? input : "minecraft:" + input;
         if (Manager.CHESTSTEALER_MANAGER.addItem(itemName)) {
            ClientManager.message(Formatting.GREEN + itemName + Strings.b("IGXFn3lhc8SxIENoZXN0U3RlYWxlciBsaXN0ZXNpbmUgZWtsZW5kaS4="));
         } else {
            ClientManager.message(Formatting.RED + Strings.b("RcWfeWEgZWtsZW5lbWVkaTog") + itemName);
         }

         return 1;
      })));
      root.then(literal("remove").then(arg("item", StringArgumentType.word()).suggests((ctx, builder) -> {
         for (Item item : Manager.CHESTSTEALER_MANAGER.getWhitelist()) {
            String id = Registries.ITEM.getId(item).toString();
            builder.suggest(id.replace("minecraft:", ""));
         }

         return builder.buildFuture();
      }).executes(ctx -> {
         String input = StringArgumentType.getString(ctx, "item");
         String itemName = input.contains(":") ? input : "minecraft:" + input;
         if (Manager.CHESTSTEALER_MANAGER.removeItem(itemName)) {
            ClientManager.message(Formatting.GREEN + itemName + Strings.b("IGXFn3lhc8SxIENoZXN0U3RlYWxlciBsaXN0ZXNpbmRlbiBrYWxkxLFyxLFsZMSxLg=="));
         } else {
            ClientManager.message(Formatting.RED + Strings.b("RcWfeWEgbGlzdGVkZW4ga2FsZMSxcsSxbGFtYWTEsTog") + itemName);
         }

         return 1;
      })));
      root.then(literal("list").executes(ctx -> {
         if (Manager.CHESTSTEALER_MANAGER.getWhitelist().isEmpty()) {
            ClientManager.message(Formatting.RED + Strings.b("TGlzdGVkZSBla2xpIGXFn3lhIGJ1bHVubXV5b3Ih"));
         } else {
            ClientManager.message(Formatting.GREEN + Strings.b("QmV5YXogbGlzdGVkZWtpIGXFn3lhbGFyOg=="));

            for (Item item : Manager.CHESTSTEALER_MANAGER.getWhitelist()) {
               ClientManager.message(Formatting.GRAY + "- " + Formatting.WHITE + Registries.ITEM.getId(item).toString());
            }
         }

         return 1;
      }));
   }
}
