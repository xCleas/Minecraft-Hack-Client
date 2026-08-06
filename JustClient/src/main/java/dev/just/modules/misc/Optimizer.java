package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.TimerUtil;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GraphicsMode;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Optimizer",
   desc = "TWluZWNyYWZ0J8SxIG9wdGltaXplIGVkZXIsIEZQUydpIGFydMSxcsSxcg==",
   type = Type.Misc
)
public class Optimizer extends Function {
   private final BooleanSetting memory = new BooleanSetting(I0O1l0I1.b("QmVsbGXEn2kgdGVtaXpsZQ=="), true);
   private final BooleanSetting graphics = new BooleanSetting(I0O1l0I1.b("RMO8xZ/DvGsgZ3JhZmlrbGVy"), true);
   private final BooleanSetting boostFPS = new BooleanSetting(I0O1l0I1.b("TWFrc2ltdW0gRlBT"), true);
   private final TimerUtil timerHelper = new TimerUtil();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Optimizer() {
      this.addSettings(new Setting[]{this.memory, this.graphics, this.boostFPS});
   }

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
               handleMemoryCleanupInternal();
               handleGraphicsInternal();
               handleFPSBoostInternal();
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
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

   private void handleMemoryCleanupInternal() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(this.memory.get(), this.timerHelper.hasTimeElapsed(lO1I0l1O.l(300000)))) {
         if (l1O0I1lO.opaqueTrue()) {
            System.gc();
            Runtime.getRuntime().freeMemory();
            this.timerHelper.reset();
         }
      }
   }

   private void handleGraphicsInternal() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(this.graphics.get(), mc.world != null)) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
            mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
         }
      }
   }

   private void handleFPSBoostInternal() {
      l1O0I1lO.fakeHandler();
      if (this.boostFPS.get()) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.options.getEnableVsync().setValue(false);
            mc.options.getMaxFps().setValue(lO1I0l1O.i(260));
         }
      }
   }
}
