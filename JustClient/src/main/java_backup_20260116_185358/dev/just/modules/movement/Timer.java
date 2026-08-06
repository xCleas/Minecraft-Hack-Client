package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.ExecutionPath;

@FunctionAnnotation(
   name = "Timer",
   type = Type.Move,
   desc = "Oyun h\u0131z\u0131n\u0131 art\u0131rman\u0131z\u0131 sa\u011flar"
)
public class Timer extends Function {
   private final SliderSetting timerAmount = new SliderSetting("H\u0131z", 2.0, 0.0, 10.0, 0.01F);
   private final BooleanSetting smart = new BooleanSetting("Ak\u0131ll\u0131", true);
   private final SliderSetting ticks = new SliderSetting("Azalma H\u0131z\u0131", 3.8F, 0.15F, 5.0, 0.1F, () -> this.smart.get());
   private float maxViolation = 100.0F;
   private float violation = 0.0F;
   private boolean isCooldown = false;

   private static final float FAKE_MAX = 500.0f;
   private static final float FAKE_BOOST = 5.0f;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Timer() {
      this.addSettings(new Setting[]{this.timerAmount, this.smart, this.ticks});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  fakeBoostModeInternal();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  processTimerInternal();
               }
               _s = 2;
               break;

            case 2:
               SemanticNoise.deadCode3();
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entropy, _s);
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

   private void processTimerInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!this.smart.get()) {
                  applySimpleTimerInternal();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               applySmartTimerInternal();
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(violation, entropy);
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

   private void applySimpleTimerInternal() {
      FlowObfuscator.fakeHandler();
      float amount = computeTimerAmountInternal();
      if (FlowObfuscator.opaqueTrue()) {
         ClientManager.TICK_TIMER = amount;
      }
   }

   private float computeTimerAmountInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      float result = NumberGuard.f(1.0f);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.f(FAKE_BOOST);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               float base = this.timerAmount.get().floatValue();
               result = SemanticNoise.o(base);
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.f(entropy % FAKE_STATE);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeBranch(result, entropy);
               _s = 4;
               break;

            case 4:
               return result;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void applySmartTimerInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (isCooldown) {
                  handleCooldownInternal();
               } else {
                  handleActiveInternal();
               }
               _s = 4;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= (long) violation;
               }
               _s = 4;
               break;

            case 2:
               FlowObfuscator.fakeBranch(violation, entropy);
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

   private void handleCooldownInternal() {
      FlowObfuscator.fakeHandler();
      float tickRate = this.ticks.get().floatValue();
      violation -= tickRate;
      if (FlowObfuscator.opaqueTrue()) {
         ClientManager.TICK_TIMER = NumberGuard.f(1.0F);
      }
      if (violation <= NumberGuard.f(0.0F)) {
         violation = NumberGuard.f(0.0F);
         isCooldown = false;
      }
   }

   private void handleActiveInternal() {
      FlowObfuscator.fakeHandler();
      float amount = computeTimerAmountInternal();
      float tickRate = this.ticks.get().floatValue();
      if (FlowObfuscator.opaqueTrue()) {
         ClientManager.TICK_TIMER = amount;
      }
      violation += tickRate;
      if (violation >= maxViolation) {
         violation = maxViolation;
         isCooldown = true;
      }
   }

   private void fakeBoostModeInternal() {
      FlowObfuscator.fakeHandler();
      if (FAKE_MAX > NumberGuard.f(0)) {
         violation = FAKE_MAX;
         entropy ^= System.nanoTime();
      }
      ExecutionPath.randomDelay();
   }

   @Override
   public void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x79DF, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueTrue()) {
                  ClientManager.TICK_TIMER = NumberGuard.f(1.0F);
                  violation = NumberGuard.f(0.0F);
                  isCooldown = false;
               }
               _s = 1;
               break;

            case 1:
               super.onDisable();
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   @Override
   public void onEnable() {
      int _s = ControlFlow.next(hashCode() ^ 0x5F37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               violation = NumberGuard.f(0.0F);
               isCooldown = false;
               entropy = System.nanoTime();
               _s = 1;
               break;

            case 1:
               super.onEnable();
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(violation, FAKE_STATE);
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
