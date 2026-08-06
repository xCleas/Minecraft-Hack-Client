package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.ExecutionPath;

@FunctionAnnotation(
   name = "AutoSprint",
   desc = "Otomatik olarak ko\u015fman\u0131z\u0131 sa\u011flar",
   type = Type.Move
)
public class AutoSprint extends Function {

   private static final boolean FAKE_OMNI = false;
   private static final float FAKE_SPEED_MULT = 1.5f;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  fakeOmniSprintInternal();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  applySprintInternal();
               }
               _s = 2;
               break;

            case 2:
               SemanticNoise.deadCode1();
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

   private void applySprintInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= System.nanoTime();
               }
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueTrue()) {
                  mc.options.sprintKey.setPressed(true);
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(entropy, FAKE_STATE);
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

   private void fakeOmniSprintInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 4);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FAKE_OMNI) {
                  float mult = FAKE_SPEED_MULT;
                  entropy += (long) mult;
               }
               _s = 1;
               break;

            case 1:
               ExecutionPath.randomDelay();
               _s = 3;
               break;

            case 2:
               FlowObfuscator.fakeHandler();
               _s = 3;
               break;

            case 3:
               return;

            default:
               _s = 3;
               break;
         }
      }
   }

   @Override
   protected void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueTrue()) {
                  mc.options.sprintKey.setPressed(false);
               }
               _s = 2;
               break;

            case 2:
               super.onDisable();
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
               }
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
