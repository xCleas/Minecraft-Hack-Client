package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.input.EventMouse;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import net.minecraft.util.Formatting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "MiddleClickFriend",
   keywords = {"MCF"},
   desc = "T3J0YSB0dcWfbGEgYXJrYWRhxZ8gZWtsZW1lL8OnxLFrYXJtYQ==",
   type = Type.Player
)
public class MiddleClickFriend extends Function {
   @Override
   public void onEvent(Event event) {
      if (event instanceof EventMouse e
         && e.getButton() == 2
         && mc.crosshairTarget instanceof EntityHitResult entityHitResult
         && entityHitResult.getEntity() instanceof PlayerEntity player) {
         String name = player.getName().getString();
         if (Manager.FRIEND_MANAGER.isFriend(name)) {
            Manager.FRIEND_MANAGER.removeFriend(name);
            ClientManager.message(Formatting.RED + Strings.b("U2lsaW5kaSEg") + Formatting.GOLD + name + Formatting.WHITE + Strings.b("IGFya2FkYcWfIGxpc3Rlc2luZGVuIMOnxLFrYXLEsWxkxLEu"));
         } else {
            Manager.FRIEND_MANAGER.addFriend(name);
            ClientManager.message(Formatting.GREEN + Strings.b("RWtsZW5kaSEg") + Formatting.GOLD + name + Formatting.WHITE + Strings.b("IGFya2FkYcWfIGxpc3Rlc2luZSBla2xlbmRpLg=="));
         }
      }
   }
}
