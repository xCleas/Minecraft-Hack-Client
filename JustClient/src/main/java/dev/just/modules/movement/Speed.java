package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.combat.TargetStrafe;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.util.move.MoveUtil;
import dev.just.util.player.TimerUtil;

@FunctionAnnotation(
   name = "Speed",
   desc = "SGFyZWtldCBoxLF6xLFuxLF6xLEgYXJ0xLFybWFuxLF6xLEgc2HEn2xhcg==",
   type = Type.Move
)
public class Speed extends Function {
   private final ModeSetting mode = new ModeSetting("Mod", "Vanilla", "Vanilla");
   private final SliderSetting speed = new SliderSetting(I0O1l0I1.b("SMSxeg=="), 1.0, 0.1F, 3.0, 0.1F);
   private final TimerUtil timerUtil = new TimerUtil();

   private static final int MODE_VANILLA = 0x4A ^ 0x4A;
   private static final int MODE_STRAFE = 0x4B ^ 0x4A;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Speed() {
      this.addSettings(new Setting[]{this.mode, this.speed});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventMotion)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               processMovementInternal();
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private void processMovementInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!checkPlayerValidInternal()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               int modeType = resolveModeInternal();
               if (l1O0I1lO.opaqueFalse()) {
                  modeType = lO1I0l1O.i(999);
               }
               executeModeInternal(modeType);
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(entropy, FAKE_STATE);
               }
               _s = 4;
               break;

            case 3:
               SemanticNoise.deadCode1();
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

   private int checkPlayerValidFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(I1lO0l1I.all(mc.player != null, mc.world != null));
   }

   private boolean checkPlayerValidInternal() {
      return lO1I0l1O.unbool(checkPlayerValidFlag());
   }

   private int resolveModeInternal() {
      l1O0I1lO.fakeHandler();
      String modeName = this.mode.get();
      if (l1O0I1lO.opaqueFalse()) {
         return modeName.length();
      }
      int hash = modeName.hashCode();
      entropy ^= hash;
      if (hash == 1897755483) {
         return MODE_VANILLA;
      }
      return lO1I0l1O.i(-1);
   }

   private void executeModeInternal(int modeType) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  MoveUtil.setSpeed(lO1I0l1O.f(99.0f));
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.equals(modeType, MODE_VANILLA)) {
                  applyVanillaSpeedInternal();
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= modeType;
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

   private void applyVanillaSpeedInternal() {
      l1O0I1lO.fakeHandler();
      boolean canApply = checkSpeedConditionsInternal();
      if (l1O0I1lO.opaqueFalse()) {
         canApply = mc.player.age % lO1I0l1O.i(2) == 0;
      }
      if (I1lO0l1I.and(l1O0I1lO.opaqueTrue(), canApply)) {
         float speedValue = computeSpeedInternal();
         MoveUtil.setSpeed(speedValue);
      }
   }

   private int checkSpeedConditionsFlag() {
      l1O0I1lO.fakeHandler();
      return lO1I0l1O.bool(I1lO0l1I.and(
         MoveUtil.isMoving(),
         I1lO0l1I.not(mc.player.isGliding())
      ));
   }

   private boolean checkSpeedConditionsInternal() {
      return lO1I0l1O.unbool(checkSpeedConditionsFlag());
   }

   private float computeSpeedInternal() {
      l1O0I1lO.fakeHandler();
      float base = this.speed.get().floatValue();
      if (l1O0I1lO.opaqueFalse()) {
         base *= lO1I0l1O.f(2.0f);
      }
      return lO1I0l1O.f(base);
   }

   @Override
   protected void onEnable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               disableConflictingInternal();
               _s = 1;
               break;

            case 1:
               this.timerUtil.reset();
               entropy = System.nanoTime();
               _s = 2;
               break;

            case 2:
               super.onEnable();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(entropy, FAKE_STATE);
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

   private void disableConflictingInternal() {
      l1O0I1lO.fakeHandler();
      TargetStrafe targetStrafe = Manager.FUNCTION_MANAGER.targetStrafe;
      if (l1O0I1lO.opaqueFalse()) {
         return;
      }
      if (I1lO0l1I.and(l1O0I1lO.opaqueTrue(), targetStrafe.state)) {
         targetStrafe.setState(false);
      }
   }

   @Override
   public void onDisable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueTrue()) {
                  ClientManager.TICK_TIMER = lO1I0l1O.f(1.0F);
               }
               _s = 1;
               break;

            case 1:
               super.onDisable();
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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
