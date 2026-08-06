package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;
import java.util.Arrays;

@FunctionAnnotation(
   name = "NoRender",
   type = Type.Render,
   desc = "RWtyYW5kYWtpIMOnZcWfaXRsaSBnw7Zyc2VsIGVmZWt0bGVyaSB0ZW1pemxlcg=="
)
public class NoRender extends Function {
   public MultiSetting mods = new MultiSetting(
      Strings.b("RW5nZWxsZQ=="),
      Arrays.asList(Strings.b("S2FtZXJhIFNhcnPEsW50xLFzxLE="), Strings.b("RWtyYW4gQXRlxZ9p"), Strings.b("RWtyYW4gU3V5dQ=="), Strings.b("Qm/En3VsbWE="), Strings.b("S8O2dMO8IEVmZWt0bGVy")),
      new String[]{Strings.b("S2FtZXJhIFNhcnPEsW50xLFzxLE="), Strings.b("RWtyYW4gQXRlxZ9p"), Strings.b("RWtyYW4gU3V5dQ=="), Strings.b("Qm/En3VsbWE="), Strings.b("U2tvciBUYWJsb3N1"), Strings.b("S8O2dMO8IEVmZWt0bGVy")}
   );

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public NoRender() {
      this.addSettings(new Setting[]{this.mods});
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
