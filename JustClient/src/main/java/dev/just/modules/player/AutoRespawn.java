package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.TextSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

@FunctionAnnotation(
   name = "AutoRespawn",
   desc = "\u00d6ld\u00fc\u011f\u00fcn\u00fczde otomatik olarak yeniden do\u011fman\u0131z\u0131 sa\u011flar.",
   type = Type.Player
)
public class AutoRespawn extends Function {
   private final BooleanSetting autohome = new BooleanSetting("Otomatik Eve Git", true);
   private final TextSetting home = new TextSetting("Ev \u0130smi", "home", () -> this.autohome.get());

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public AutoRespawn() {
      this.addSettings(new Setting[]{this.autohome, this.home});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               handlePacketEvent(event);
               _s = 2;
               break;

            case 2:
               handleUpdateEvent(event);
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, FAKE_STATE);
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entropy, _s);
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void handlePacketEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!(event instanceof EventPacket eventPacket)) {
                  _s = 5;
                  break;
               }
               if (!(eventPacket.getPacket() instanceof GameMessageS2CPacket packet)) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventPacket ep && ep.getPacket() instanceof GameMessageS2CPacket pkt) {
                  processDeathMessage(pkt);
               }
               _s = 5;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
               }
               _s = 5;
               break;

            case 3:
               l1O0I1lO.fakeBranch(event, entropy);
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void processDeathMessage(GameMessageS2CPacket packet) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      String message = null;
      int isDeadFlag = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               message = packet.content().getString().toLowerCase();
               _s = 1;
               break;

            case 1:
               isDeadFlag = checkDeathMessage(message);
               _s = 2;
               break;

            case 2:
               if (I1lO0l1I.and(lO1I0l1O.unbool(isDeadFlag), this.autohome.get())) {
                  executeHomeCommand();
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(message, entropy);
               }
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private int checkDeathMessage(String message) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(entropy % 2);
      }

      boolean isDead = I1lO0l1I.any(
         message.contains("olduruldun"),
         message.contains("\u00f6ld\u00fcr\u00fcld\u00fcn"),
         message.contains("oldun"),
         message.contains("\u00f6ld\u00fcn"),
         message.contains("you died")
      );

      return lO1I0l1O.bool(isDead);
   }

   private void executeHomeCommand() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         mc.execute(() -> {
            if (mc.player != null) {
               mc.player.networkHandler.sendChatCommand("home " + this.home.getValue());
            }
         });
      }
   }

   private void handleUpdateEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!(event instanceof EventUpdate)) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (mc.player == null) {
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (mc.currentScreen instanceof DeathScreen) {
                  if (l1O0I1lO.opaqueTrue()) {
                     mc.player.requestRespawn();
                     mc.setScreen(null);
                  }
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= mc.player.hashCode();
               }
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }
}
