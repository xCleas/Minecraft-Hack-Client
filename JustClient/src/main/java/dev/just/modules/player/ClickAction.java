package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ClickAction",
   keywords = {"SwapAction"},
   desc = "U2XDp2lsZW4gc3VudWN1IHTDvHLDvG5lIGfDtnJlIHTEsWtsYW1hL3N3YXAgbW9kdW51IGF5YXJsYXIgKHlhc2FrbGFubWF5xLEgw7ZubGVtZWsgacOnaW4p",
   type = Type.Player
)
public class ClickAction extends Function {
   public final ModeSetting type = new ModeSetting(I0O1l0I1.b("U3VudWN1IFTDvHLDvA=="), "ReallyWorld", "ReallyWorld", "FunTime", "HollyWorld");

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, event.hashCode());
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
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

   public final boolean nonBatch() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return entropy > FAKE_STATE;
      }
      return this.type.is("ReallyWorld");
   }

   public final boolean batch() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return entropy < FAKE_STATE;
      }
      return I1lO0l1I.or(this.type.is("FunTime"), this.type.is("HollyWorld"));
   }
}
