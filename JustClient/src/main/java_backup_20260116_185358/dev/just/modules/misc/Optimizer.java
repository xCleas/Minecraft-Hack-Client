package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.TimerUtil;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GraphicsMode;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Optimizer",
   desc = "TWluZWNyYWZ0J8SxIG9wdGltaXplIGVkZXIsIEZQUydpIGFydMSxcsSxcg==",
   type = Type.Misc
)
public class Optimizer extends Function {
   private final BooleanSetting memory = new BooleanSetting(Strings.b("QmVsbGXEn2kgdGVtaXpsZQ=="), true);
   private final BooleanSetting graphics = new BooleanSetting(Strings.b("RMO8xZ/DvGsgZ3JhZmlrbGVy"), true);
   private final BooleanSetting boostFPS = new BooleanSetting(Strings.b("TWFrc2ltdW0gRlBT"), true);
   private final TimerUtil timerHelper = new TimerUtil();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Optimizer() {
      this.addSettings(new Setting[]{this.memory, this.graphics, this.boostFPS});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
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
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
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
      FlowObfuscator.fakeHandler();
      if (LogicSplit.and(this.memory.get(), this.timerHelper.hasTimeElapsed(NumberGuard.l(300000)))) {
         if (FlowObfuscator.opaqueTrue()) {
            System.gc();
            Runtime.getRuntime().freeMemory();
            this.timerHelper.reset();
         }
      }
   }

   private void handleGraphicsInternal() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.and(this.graphics.get(), mc.world != null)) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
            mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
         }
      }
   }

   private void handleFPSBoostInternal() {
      FlowObfuscator.fakeHandler();
      if (this.boostFPS.get()) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.options.getEnableVsync().setValue(false);
            mc.options.getMaxFps().setValue(NumberGuard.i(260));
         }
      }
   }
}
