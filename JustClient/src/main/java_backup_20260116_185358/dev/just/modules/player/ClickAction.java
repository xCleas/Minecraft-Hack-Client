package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ClickAction",
   keywords = {"SwapAction"},
   desc = "U2XDp2lsZW4gc3VudWN1IHTDvHLDvG5lIGfDtnJlIHTEsWtsYW1hL3N3YXAgbW9kdW51IGF5YXJsYXIgKHlhc2FrbGFubWF5xLEgw7ZubGVtZWsgacOnaW4p",
   type = Type.Player
)
public class ClickAction extends Function {
   public final ModeSetting type = new ModeSetting(Strings.b("U3VudWN1IFTDvHLDvA=="), "ReallyWorld", "ReallyWorld", "FunTime", "HollyWorld");

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, event.hashCode());
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
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
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return entropy > FAKE_STATE;
      }
      return this.type.is("ReallyWorld");
   }

   public final boolean batch() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return entropy < FAKE_STATE;
      }
      return LogicSplit.or(this.type.is("FunTime"), this.type.is("HollyWorld"));
   }
}
