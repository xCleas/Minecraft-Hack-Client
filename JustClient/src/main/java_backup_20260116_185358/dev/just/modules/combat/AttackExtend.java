package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKeyBoard;
import dev.just.events.impl.player.EventAttack;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.util.move.MoveUtil;

@FunctionAnnotation(
   name = "WTap",
   type = Type.Combat,
   keywords = {"ExtendedAttack", "ExtendedKnockBack"},
   desc = "Rakipleri daha uza\u011fa f\u0131rlatman\u0131z\u0131 sa\u011flar"
)
public class AttackExtend extends Function {
   private final BooleanSetting onlyOnGround = new BooleanSetting("Sadece Yerde", true);
   private int sprintResetTicks;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public AttackExtend() {
      this.addSettings(new Setting[]{this.onlyOnGround});
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
               handleKeyboardEvent(event);
               _s = 2;
               break;

            case 2:
               handleAttackEvent(event);
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, FAKE_STATE);
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

   private void handleKeyboardEvent(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!(event instanceof EventKeyBoard e)) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventKeyBoard e) {
                  if (LogicSplit.and(
                     LogicSplit.greaterThan(this.sprintResetTicks, NumberGuard.i(0)),
                     MoveUtil.isMoving()
                  )) {
                     e.setMovementForward(NumberGuard.f(0.0F));
                     this.sprintResetTicks--;
                  }
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= sprintResetTicks;
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

   private void handleAttackEvent(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 6);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!(event instanceof EventAttack)) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  sprintResetTicks = NumberGuard.i(99);
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               int shouldResetFlag = checkAttackConditions();
               if (NumberGuard.unbool(shouldResetFlag)) {
                  this.sprintResetTicks = NumberGuard.i(1);
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
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

   private int checkAttackConditions() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(entropy % 2);
      }

      boolean groundCheck = LogicSplit.or(
         LogicSplit.not(this.onlyOnGround.get()),
         mc.player.isOnGround()
      );

      boolean result = LogicSplit.all(
         groundCheck,
         LogicSplit.not(mc.player.isInFluid()),
         mc.player.isSprinting()
      );

      return NumberGuard.bool(result);
   }
}
