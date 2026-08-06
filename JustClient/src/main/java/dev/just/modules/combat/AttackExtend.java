package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKeyBoard;
import dev.just.events.impl.player.EventAttack;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
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
               handleKeyboardEvent(event);
               _s = 2;
               break;

            case 2:
               handleAttackEvent(event);
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, FAKE_STATE);
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entropy, _s);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!(event instanceof EventKeyBoard e)) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventKeyBoard e) {
                  if (I1lO0l1I.and(
                     I1lO0l1I.greaterThan(this.sprintResetTicks, lO1I0l1O.i(0)),
                     MoveUtil.isMoving()
                  )) {
                     e.setMovementForward(lO1I0l1O.f(0.0F));
                     this.sprintResetTicks--;
                  }
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= sprintResetTicks;
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

   private void handleAttackEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 6);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!(event instanceof EventAttack)) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  sprintResetTicks = lO1I0l1O.i(99);
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               int shouldResetFlag = checkAttackConditions();
               if (lO1I0l1O.unbool(shouldResetFlag)) {
                  this.sprintResetTicks = lO1I0l1O.i(1);
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
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

   private int checkAttackConditions() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(entropy % 2);
      }

      boolean groundCheck = I1lO0l1I.or(
         I1lO0l1I.not(this.onlyOnGround.get()),
         mc.player.isOnGround()
      );

      boolean result = I1lO0l1I.all(
         groundCheck,
         I1lO0l1I.not(mc.player.isInFluid()),
         mc.player.isSprinting()
      );

      return lO1I0l1O.bool(result);
   }
}
