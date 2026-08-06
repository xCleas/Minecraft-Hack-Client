package dev.just.manager.commandManager;

import com.mojang.brigadier.CommandDispatcher;
import dev.just.manager.IMinecraft;
import dev.just.manager.commandManager.impl.BlockESPCommand;
import dev.just.manager.commandManager.impl.ConfigCommand;
import dev.just.manager.commandManager.impl.FriendCommand;
import dev.just.manager.commandManager.impl.HclipCommand;
import dev.just.manager.commandManager.impl.PanicCommand;
import dev.just.manager.commandManager.impl.VclipCommand;
import dev.just.manager.commandManager.impl.WayPointCommand;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.client.network.ClientCommandSource;

public class CommandManager implements IMinecraft {
   private String prefix = ".";
   private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher();
   private final CommandSource source = new ClientCommandSource(null, mc);
   private final List<Command> commands = new ArrayList<>();

   public CommandManager() {
      this.register(new FriendCommand());
      this.register(new WayPointCommand());
      this.register(new ConfigCommand());
      this.register(new BlockESPCommand());
      this.register(new PanicCommand());
      this.register(new VclipCommand());
      this.register(new HclipCommand());
   }

   public void register(Command command) {
      if (command != null) {
         command.register(this.dispatcher);
         this.commands.add(command);
      }
   }

   public final CommandDispatcher getDispatcher() {
      return this.dispatcher;
   }

   public final CommandSource getSource() {
      return this.source;
   }

   public final String getPrefix() {
      return this.prefix;
   }
}
