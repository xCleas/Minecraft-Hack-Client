package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ViewModel",
   desc = "RWxsZXJpbiB2ZSBlxZ95YWxhcsSxbiBla3JhbmRha2kga29udW11bnUgZGXEn2nFn3Rpcmly",
   type = Type.Render
)
public class ViewModel extends Function {
   public final SliderSetting right_x = new SliderSetting(I0O1l0I1.b("U2HEnyBY"), 0.6F, -2.0, 2.0, 0.1F);
   public final SliderSetting right_y = new SliderSetting(I0O1l0I1.b("U2HEnyBZ"), -0.6F, -2.0, 2.0, 0.1F);
   public final SliderSetting right_z = new SliderSetting(I0O1l0I1.b("U2HEnyBa"), -0.8F, -2.0, 2.0, 0.1F);
   public final SliderSetting left_x = new SliderSetting(I0O1l0I1.b("U29sIFg="), 0.0, -2.0, 2.0, 0.1F);
   public final SliderSetting left_y = new SliderSetting(I0O1l0I1.b("U29sIFk="), 0.0, -2.0, 2.0, 0.1F);
   public final SliderSetting left_z = new SliderSetting(I0O1l0I1.b("U29sIFo="), 0.0, -2.0, 2.0, 0.1F);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public ViewModel() {
      this.addSettings(new Setting[]{this.right_x, this.right_y, this.right_z, this.left_x, this.left_y, this.left_z});
   }

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
}
