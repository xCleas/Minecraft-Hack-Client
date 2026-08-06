package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.Strings;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class PanicCommand extends Command {
   public PanicCommand() {
      super("panic");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(context -> {
         Manager.FUNCTION_MANAGER.getFunctions().stream().filter(function -> function.state).forEach(function -> function.setState(false));
         ClientManager.message(Formatting.GREEN + Strings.b("QmHFn2FyxLFsxLEhIA==") + Formatting.WHITE + Strings.b("VMO8bSBtb2TDvGxsZXIga2FwYXTEsWxkxLEu"));
         return 1;
      });
   }
}
