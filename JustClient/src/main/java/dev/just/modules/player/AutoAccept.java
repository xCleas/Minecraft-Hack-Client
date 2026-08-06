package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoAccept",
   keywords = {"TpaAccept"},
   type = Type.Player,
   desc = "R2VsZW4gxLHFn8Sxbmxhbm1hIGlzdGVrbGVyaW5pIG90b21hdGlrIGthYnVsIGVkZXIu"
)
public class AutoAccept extends Function {
   private final BooleanSetting onlyFriend = new BooleanSetting(I0O1l0I1.b("U2FkZWNlIEFya2FkYcWfbGFy"), true);

   public AutoAccept() {
      this.addSettings(new Setting[]{this.onlyFriend});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventPacket eventPacket && eventPacket.getPacket() instanceof GameMessageS2CPacket packet) {
         String message = packet.content().getString().toLowerCase();
         boolean isTpaRequest = message.contains("size isinlanmak istiyor")
            || message.contains(I0O1l0I1.b("c2l6ZSDEscWfxLFubGFubWFrIGlzdGl5b3I="))
            || message.contains("has requested to teleport")
            || message.contains("wants to teleport to you");
         if (isTpaRequest) {
            String sender = this.extractName(packet.content().getString());
            String realName = nameCheck(sender);
            if (!this.onlyFriend.get() || Manager.FRIEND_MANAGER.isFriend(realName)) {
               mc.execute(() -> {
                  if (mc.player != null) {
                     mc.player.networkHandler.sendCommand("tpaccept");
                  }
               });
            }
         }
      }
   }

   private String extractName(String message) {
      String clean = message.replaceAll("(?i)§[0-9a-fk-or]", "").trim();
      String[] parts = clean.split(" ");
      return parts.length > 0 ? parts[0] : "UNKNOWN";
   }

   public static String nameCheck(String notSolved) {
      AtomicReference<String> result = new AtomicReference<>(notSolved);
      if (mc.getNetworkHandler() != null) {
         mc.getNetworkHandler().getListedPlayerListEntries().forEach(player -> {
            if (notSolved.equalsIgnoreCase(player.getProfile().getName())) {
               result.set(player.getProfile().getName());
            }
         });
      }

      return result.get();
   }
}
