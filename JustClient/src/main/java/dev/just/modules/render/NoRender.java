package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I0O1l0I1;
import java.util.Arrays;

@FunctionAnnotation(
   name = "NoRender",
   type = Type.Render,
   desc = "RWtyYW5kYWtpIMOnZcWfaXRsaSBnw7Zyc2VsIGVmZWt0bGVyaSB0ZW1pemxlcg=="
)
public class NoRender extends Function {
   public MultiSetting mods = new MultiSetting(
      I0O1l0I1.b("RW5nZWxsZQ=="),
      Arrays.asList(I0O1l0I1.b("S2FtZXJhIFNhcnPEsW50xLFzxLE="), I0O1l0I1.b("RWtyYW4gQXRlxZ9p"), I0O1l0I1.b("RWtyYW4gU3V5dQ=="), I0O1l0I1.b("Qm/En3VsbWE="), I0O1l0I1.b("S8O2dMO8IEVmZWt0bGVy")),
      new String[]{I0O1l0I1.b("S2FtZXJhIFNhcnPEsW50xLFzxLE="), I0O1l0I1.b("RWtyYW4gQXRlxZ9p"), I0O1l0I1.b("RWtyYW4gU3V5dQ=="), I0O1l0I1.b("Qm/En3VsbWE="), I0O1l0I1.b("U2tvciBUYWJsb3N1"), I0O1l0I1.b("S8O2dMO8IEVmZWt0bGVy")}
   );

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public NoRender() {
      this.addSettings(new Setting[]{this.mods});
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
