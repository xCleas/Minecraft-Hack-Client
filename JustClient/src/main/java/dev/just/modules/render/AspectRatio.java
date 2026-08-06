package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AspectRatio",
   desc = "RWtyYW7EsW4gZW4tYm95IG9yYW7EsW7EsSBkZcSfacWfdGlybWVuaXppIHNhxJ9sYXI=",
   type = Type.Render
)
public class AspectRatio extends Function {
   public final ModeSetting mods = new ModeSetting(I0O1l0I1.b("TW9k"), "16:9", "4:3", "16:9", "1:1", "16:10", I0O1l0I1.b("w5Z6ZWw="));
   public final SliderSetting slider = new SliderSetting(I0O1l0I1.b("T3Jhbg=="), 1.8F, 0.1F, 5.0, 0.1F, () -> this.mods.is(I0O1l0I1.b("w5Z6ZWw=")));

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public AspectRatio() {
      this.addSettings(new Setting[]{this.mods, this.slider});
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
