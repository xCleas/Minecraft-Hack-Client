package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import java.util.Arrays;

@FunctionAnnotation(
   name = "NoPush",
   desc = "Farkl\u0131 t\u00fcrlerdeki itilmeleri ve \u00e7arp\u0131\u015fmalar\u0131 engeller",
   type = Type.Player
)
public class NoPush extends Function {
   public MultiSetting mods = new MultiSetting("T\u00fcrler", Arrays.asList("Oyuncular", "Bloklar"), new String[]{"Su", "Oyuncular", "Bloklar"});

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public NoPush() {
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
