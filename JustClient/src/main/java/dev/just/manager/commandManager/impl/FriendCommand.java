package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.manager.commandManager.impl.args.FriendArgumentType;
import dev.just.manager.commandManager.impl.args.PlayerArgumentType;
import dev.just.manager.friendManager.FriendManager;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import dev.just.protect.runtime.I0O1l0I1;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("friend");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("add").then(arg("player", PlayerArgumentType.create()).executes(context -> {
         String name = (String)context.getArgument("player", String.class);
         if (Manager.FRIEND_MANAGER.isFriend(name)) {
            ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.GOLD + name + Formatting.WHITE + I0O1l0I1.b("IHphdGVuIGFya2FkYcWfIGxpc3Rlc2luZGUu"));
            return 1;
         } else {
            Manager.FRIEND_MANAGER.addFriend(name);
            ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.GOLD + name + Formatting.WHITE + I0O1l0I1.b("IGFya2FkYcWfIGxpc3Rlc2luZSBla2xlbmRpLg=="));
            return 1;
         }
      })));
      builder.then(literal("remove").then(arg("player", FriendArgumentType.create()).executes(context -> {
         String name = (String)context.getArgument("player", String.class);
         Manager.FRIEND_MANAGER.removeFriend(name);
         ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.GOLD + name + Formatting.WHITE + I0O1l0I1.b("IGFya2FkYcWfIGxpc3Rlc2luZGVuIHNpbGluZGku"));
         return 1;
      })));
      builder.then(literal("clear").executes(context -> {
         this.clearFriendList();
         return 1;
      }));
      builder.then(literal("list").executes(context -> {
         if (Manager.FRIEND_MANAGER.getFriends().isEmpty()) {
            ClientManager.message(Formatting.GRAY + I0O1l0I1.b("QXJrYWRhxZ8gbGlzdGVzaSBib8WfLg=="));
         } else {
            String friendsList = Manager.FRIEND_MANAGER.getFriends().stream().map(f -> f.getName()).reduce((a, b) -> a + ", " + b).orElse("");
            ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QXJrYWRhxZ9sYXI6IA==") + Formatting.WHITE + friendsList);
         }

         return 1;
      }));
   }

   private void clearFriendList() {
      FriendManager friendManager = Manager.FRIEND_MANAGER;
      if (friendManager.getFriends().isEmpty()) {
         ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.WHITE + I0O1l0I1.b("QXJrYWRhxZ8gbGlzdGVzaSB6YXRlbiBib8WfLg=="));
      } else {
         friendManager.clearFriends();
         ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.WHITE + I0O1l0I1.b("QXJrYWRhxZ8gbGlzdGVzaSB0ZW1pemxlbmRpLg=="));
      }
   }
}
