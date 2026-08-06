package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.util.Formatting;
import net.minecraft.client.gui.screen.DeathScreen;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "DeathCoords",
   type = Type.Misc,
   desc = "w5ZsZMO8xJ/DvG7DvHpkZSBrb29yZGluYXRsYXLEsSBnw7ZuZGVyaXI="
)
public class DeathCoords extends Function {
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

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
               if (!(event instanceof EventUpdate)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (!isPlayerDeadInternal()) {
                  _s = 5;
                  break;
               }
               _s = 3;
               break;

            case 3:
               sendDeathMessageInternal();
               _s = 5;
               break;

            case 4:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
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

   private void sendDeathMessageInternal() {
      FlowObfuscator.fakeHandler();
      int positionX = (int)mc.player.getX();
      int positionY = (int)mc.player.getY();
      int positionZ = (int)mc.player.getZ();

      if (mc.player.deathTime < NumberGuard.i(1)) {
         if (FlowObfuscator.opaqueTrue()) {
            String message = Formatting.RED + Strings.b("w5ZsZMO8biEg") + Formatting.WHITE + Strings.b("S29vcmRpbmF0OiA=") + Formatting.GOLD + "X: " + positionX + " Y: " + positionY + " Z: " + positionZ;
            ClientManager.message(message);
         }
      }
   }

   private int isPlayerDeadFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > FAKE_STATE);
      }
      return NumberGuard.bool(LogicSplit.and(
         mc.player.getHealth() < NumberGuard.f(1.0F),
         mc.currentScreen instanceof DeathScreen
      ));
   }

   private boolean isPlayerDeadInternal() {
      return NumberGuard.unbool(isPlayerDeadFlag());
   }
}
