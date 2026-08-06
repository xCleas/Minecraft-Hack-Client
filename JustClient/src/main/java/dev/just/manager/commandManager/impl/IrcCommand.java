package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.I0O1l0I1;
import net.minecraft.command.CommandSource;

public class IrcCommand extends Command {
   public IrcCommand() {
      super("irc");
   }

   @SuppressWarnings("unchecked")
   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      RequiredArgumentBuilder<CommandSource, String> ignoreArg = (RequiredArgumentBuilder<CommandSource, String>)(RequiredArgumentBuilder<?, ?>)RequiredArgumentBuilder.argument(
            "nick", StringArgumentType.word()
         )
         .executes(context -> {
            String nick = StringArgumentType.getString(context, "nick");
            Manager.IRC_MANAGER.ignoreNick(nick);
            Manager.IRC_MANAGER.messageClient(nick + I0O1l0I1.b("IGVuZ2VsbGVuZW5sZXJlIGVrbGVuZGku"));
            return 1;
         });
      builder.then(literal("ignore").then(ignoreArg));
      RequiredArgumentBuilder<CommandSource, String> unignoreArg = (RequiredArgumentBuilder<CommandSource, String>)(RequiredArgumentBuilder<?, ?>)RequiredArgumentBuilder.argument(
            "nick", StringArgumentType.word()
         )
         .executes(context -> {
            String nick = StringArgumentType.getString(context, "nick");
            Manager.IRC_MANAGER.unignoreNick(nick);
            Manager.IRC_MANAGER.messageClient(nick + I0O1l0I1.b("IGVuZ2VsbGVtZSBsaXN0ZXNpbmRlbiDDp8Sxa2FyxLFsZMSxLg=="));
            return 1;
         });
      builder.then(literal("unignore").then(unignoreArg));
      builder.then(literal("ignorelist").executes(context -> {
         if (Manager.IRC_MANAGER.getIgnoredNicks().isEmpty()) {
            Manager.IRC_MANAGER.messageClient(I0O1l0I1.b("RW5nZWxsZW5lbmxlciBsaXN0ZXNpIGJvxZ8u"));
         } else {
            Manager.IRC_MANAGER.messageClient(I0O1l0I1.b("RW5nZWxsZW5lbmxlcjog") + String.join(", ", Manager.IRC_MANAGER.getIgnoredNicks()));
         }

         return 1;
      }));
      RequiredArgumentBuilder<CommandSource, String> msgArg = (RequiredArgumentBuilder<CommandSource, String>)(RequiredArgumentBuilder<?, ?>)RequiredArgumentBuilder.argument(
            "message", StringArgumentType.greedyString()
         )
         .executes(context -> {
            String message = StringArgumentType.getString(context, "message");
            if (message.matches("(https?://|www\\.)\\S+")) {
               Manager.IRC_MANAGER.messageClient(I0O1l0I1.b("TWVzYWrEsW7EsXogYmlyIGJhxJ9sYW50xLEgacOnZXJpeW9yIChMaW5rIHBheWxhxZ/EsW3EsSB5YXNha3TEsXIpLg=="));
               return 1;
            } else {
               if (Manager.FUNCTION_MANAGER.irc.state) {
                  Manager.IRC_MANAGER.messageHost(message);
               } else {
                  Manager.IRC_MANAGER.messageClient(I0O1l0I1.b("TMO8dGZlbiDDtm5jZSBJUkMgbW9kw7xsw7xuw7wgYWt0aWYgZWRpbi4="));
               }

               return 1;
            }
         });
      builder.then(msgArg);
   }
}
