package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ViewModel",
   desc = "RWxsZXJpbiB2ZSBlxZ95YWxhcsSxbiBla3JhbmRha2kga29udW11bnUgZGXEn2nFn3Rpcmly",
   type = Type.Render
)
public class ViewModel extends Function {
   public final SliderSetting right_x = new SliderSetting(Strings.b("U2HEnyBY"), 0.6F, -2.0, 2.0, 0.1F);
   public final SliderSetting right_y = new SliderSetting(Strings.b("U2HEnyBZ"), -0.6F, -2.0, 2.0, 0.1F);
   public final SliderSetting right_z = new SliderSetting(Strings.b("U2HEnyBa"), -0.8F, -2.0, 2.0, 0.1F);
   public final SliderSetting left_x = new SliderSetting(Strings.b("U29sIFg="), 0.0, -2.0, 2.0, 0.1F);
   public final SliderSetting left_y = new SliderSetting(Strings.b("U29sIFk="), 0.0, -2.0, 2.0, 0.1F);
   public final SliderSetting left_z = new SliderSetting(Strings.b("U29sIFo="), 0.0, -2.0, 2.0, 0.1F);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public ViewModel() {
      this.addSettings(new Setting[]{this.right_x, this.right_y, this.right_z, this.left_x, this.left_y, this.left_z});
   }

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
