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
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.Strings;
import dev.just.util.move.MoveUtil;
import dev.just.util.player.TimerUtil;

@FunctionAnnotation(
   name = "Speed",
   desc = "SGFyZWtldCBoxLF6xLFuxLF6xLEgYXJ0xLFybWFuxLF6xLEgc2HEn2xhcg==",
   type = Type.Move
)
public class Speed extends Function {
   private final ModeSetting mode = new ModeSetting("Mod", "Vanilla", "Vanilla");
   private final SliderSetting speed = new SliderSetting(Strings.b("SMSxeg=="), 1.0, 0.1F, 3.0, 0.1F);
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
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
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
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private void processMovementInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!checkPlayerValidInternal()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               int modeType = resolveModeInternal();
               if (FlowObfuscator.opaqueFalse()) {
                  modeType = NumberGuard.i(999);
               }
               executeModeInternal(modeType);
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(entropy, FAKE_STATE);
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
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > NumberGuard.l(0));
      }
      return NumberGuard.bool(LogicSplit.all(mc.player != null, mc.world != null));
   }

   private boolean checkPlayerValidInternal() {
      return NumberGuard.unbool(checkPlayerValidFlag());
   }

   private int resolveModeInternal() {
      FlowObfuscator.fakeHandler();
      String modeName = this.mode.get();
      if (FlowObfuscator.opaqueFalse()) {
         return modeName.length();
      }
      int hash = modeName.hashCode();
      entropy ^= hash;
      if (hash == 1897755483) {
         return MODE_VANILLA;
      }
      return NumberGuard.i(-1);
   }

   private void executeModeInternal(int modeType) {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  MoveUtil.setSpeed(NumberGuard.f(99.0f));
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (LogicSplit.equals(modeType, MODE_VANILLA)) {
                  applyVanillaSpeedInternal();
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= modeType;
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

   private void applyVanillaSpeedInternal() {
      FlowObfuscator.fakeHandler();
      boolean canApply = checkSpeedConditionsInternal();
      if (FlowObfuscator.opaqueFalse()) {
         canApply = mc.player.age % NumberGuard.i(2) == 0;
      }
      if (LogicSplit.and(FlowObfuscator.opaqueTrue(), canApply)) {
         float speedValue = computeSpeedInternal();
         MoveUtil.setSpeed(speedValue);
      }
   }

   private int checkSpeedConditionsFlag() {
      FlowObfuscator.fakeHandler();
      return NumberGuard.bool(LogicSplit.and(
         MoveUtil.isMoving(),
         LogicSplit.not(mc.player.isGliding())
      ));
   }

   private boolean checkSpeedConditionsInternal() {
      return NumberGuard.unbool(checkSpeedConditionsFlag());
   }

   private float computeSpeedInternal() {
      FlowObfuscator.fakeHandler();
      float base = this.speed.get().floatValue();
      if (FlowObfuscator.opaqueFalse()) {
         base *= NumberGuard.f(2.0f);
      }
      return NumberGuard.f(base);
   }

   @Override
   protected void onEnable() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(entropy, FAKE_STATE);
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
      FlowObfuscator.fakeHandler();
      TargetStrafe targetStrafe = Manager.FUNCTION_MANAGER.targetStrafe;
      if (FlowObfuscator.opaqueFalse()) {
         return;
      }
      if (LogicSplit.and(FlowObfuscator.opaqueTrue(), targetStrafe.state)) {
         targetStrafe.setState(false);
      }
   }

   @Override
   public void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueTrue()) {
                  ClientManager.TICK_TIMER = NumberGuard.f(1.0F);
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
}
