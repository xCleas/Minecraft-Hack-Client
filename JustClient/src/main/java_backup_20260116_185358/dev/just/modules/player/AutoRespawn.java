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
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
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
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
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
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, FAKE_STATE);
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entropy, _s);
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
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeBranch(event, entropy);
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
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
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      String message = null;
      int isDeadFlag = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               message = packet.content().getString().toLowerCase();
               _s = 1;
               break;

            case 1:
               isDeadFlag = checkDeathMessage(message);
               _s = 2;
               break;

            case 2:
               if (LogicSplit.and(NumberGuard.unbool(isDeadFlag), this.autohome.get())) {
                  executeHomeCommand();
               }
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(message, entropy);
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
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(entropy % 2);
      }

      boolean isDead = LogicSplit.any(
         message.contains("olduruldun"),
         message.contains("\u00f6ld\u00fcr\u00fcld\u00fcn"),
         message.contains("oldun"),
         message.contains("\u00f6ld\u00fcn"),
         message.contains("you died")
      );

      return NumberGuard.bool(isDead);
   }

   private void executeHomeCommand() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         mc.execute(() -> {
            if (mc.player != null) {
               mc.player.networkHandler.sendChatCommand("home " + this.home.getValue());
            }
         });
      }
   }

   private void handleUpdateEvent(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
                  if (FlowObfuscator.opaqueTrue()) {
                     mc.player.requestRespawn();
                     mc.setScreen(null);
                  }
               }
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
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
