package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.Strings;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;

public class DragCommand extends Command {
   public DragCommand() {
      super("drag");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> root) {
      root.then(literal("reset").executes(ctx -> {
         Manager.DRAG_MANAGER.reset();
         ClientManager.message(Formatting.GREEN + Strings.b("RWtyYW4gw7bEn2VsZXJpbmluIHBvemlzeW9ubGFyxLEgc8SxZsSxcmxhbmTEsS4="));
         return 1;
      }));
      root.then(literal("save").executes(ctx -> {
         Manager.DRAG_MANAGER.save();
         ClientManager.message(Formatting.GREEN + Strings.b("RWtyYW4gw7bEn2VsZXJpbmluIHBvemlzeW9ubGFyxLEga2F5ZGVkaWxkaS4="));
         return 1;
      }));
   }
}
