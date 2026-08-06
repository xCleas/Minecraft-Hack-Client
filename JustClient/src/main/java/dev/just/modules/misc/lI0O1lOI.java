package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.util.Formatting;
import net.minecraft.client.gui.screen.DeathScreen;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "DeathCoords",
   type = Type.Misc,
   desc = "w5ZsZMO8xJ/DvG7DvHpkZSBrb29yZGluYXRsYXLEsSBnw7ZuZGVyaXI="
)
public class lI0O1lOI extends Function {
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
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
      l1O0I1lO.fakeHandler();
      int positionX = (int)mc.player.getX();
      int positionY = (int)mc.player.getY();
      int positionZ = (int)mc.player.getZ();

      if (mc.player.deathTime < lO1I0l1O.i(1)) {
         if (l1O0I1lO.opaqueTrue()) {
            String message = Formatting.RED + I0O1l0I1.b("w5ZsZMO8biEg") + Formatting.WHITE + I0O1l0I1.b("S29vcmRpbmF0OiA=") + Formatting.GOLD + "X: " + positionX + " Y: " + positionY + " Z: " + positionZ;
            ClientManager.message(message);
         }
      }
   }

   private int isPlayerDeadFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > FAKE_STATE);
      }
      return lO1I0l1O.bool(I1lO0l1I.and(
         mc.player.getHealth() < lO1I0l1O.f(1.0F),
         mc.currentScreen instanceof DeathScreen
      ));
   }

   private boolean isPlayerDeadInternal() {
      return lO1I0l1O.unbool(isPlayerDeadFlag());
   }
}
