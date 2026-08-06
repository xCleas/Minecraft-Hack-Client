package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ExtraTab",
   desc = "VGFiIGxpc3Rlc2luZGUgZGFoYSBmYXpsYSBveXVuY3UgZ8O2c3RlcmlsbWVzaW5pIHNhxJ9sYXI=",
   type = Type.Render
)
public class ExtraTab extends Function {
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
}
