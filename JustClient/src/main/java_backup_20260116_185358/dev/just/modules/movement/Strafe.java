package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import dev.just.util.move.MoveUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Strafe",
   desc = "SMSxemzEsSBoYXJla2V0IGV0bWVuaXppIHZlIGhhdmFkYSB5w7ZuIGtvbnRyb2zDvCB5YXBtYW7EsXrEsSBzYcSfbGFy",
   type = Type.Move
)
public class Strafe extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("VMO8cg=="), "MetaHvH", "MetaHvH");

   private static final float FAKE_BASE = 1.5f;
   private static final float FAKE_BOOST = 2.0f;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Strafe() {
      this.addSettings(new Setting[]{this.mode});
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
               if (!(event instanceof EventMotion)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (!checkConditionsInternal()) {
                  _s = 5;
                  break;
               }
               _s = 3;
               break;

            case 3:
               processStrafeInternal();
               _s = 5;
               break;

            case 4:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
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

   private int checkConditionsFlag() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);
      int result = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (mc.player == null) {
                  result = NumberGuard.i(0);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (!this.mode.is("MetaHvH")) {
                  result = NumberGuard.i(0);
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.bool(entropy > NumberGuard.l(0));
                  _s = 4;
                  break;
               }
               result = NumberGuard.bool(LogicSplit.all(
                  LogicSplit.not(mc.player.isGliding()),
                  LogicSplit.or(LogicSplit.not(mc.player.isTouchingWater()), LogicSplit.not(mc.player.isSwimming())),
                  MoveUtil.isMoving()
               ));
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

   private boolean checkConditionsInternal() {
      return NumberGuard.unbool(checkConditionsFlag());
   }

   private void processStrafeInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      float motion = NumberGuard.f(0.0f);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               motion = computeBaseMotionInternal();
               _s = 1;
               break;

            case 1:
               motion = applySpeedEffectInternal(motion);
               motion = applyJumpBoostInternal(motion);
               _s = 2;
               break;

            case 2:
               if (FlowObfuscator.opaqueTrue()) {
                  MoveUtil.setMotion((double) motion);
               }
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(motion, entropy);
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

   private float computeBaseMotionInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f(FAKE_BASE);
      }
      return NumberGuard.f(0.19F);
   }

   private float applySpeedEffectInternal(float motion) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);
      float result = motion;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               StatusEffectInstance speedEffect = mc.player.getStatusEffect(StatusEffects.SPEED);
               if (speedEffect == null) {
                  result = motion;
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               StatusEffectInstance effect = mc.player.getStatusEffect(StatusEffects.SPEED);
               int amplifier = effect.getAmplifier();
               if (FlowObfuscator.opaqueFalse()) {
                  result = motion * FAKE_BOOST;
                  _s = 4;
                  break;
               }
               result = computeSpeedValue(amplifier);
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.f(FAKE_BOOST);
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

   private float computeSpeedValue(int amplifier) {
      FlowObfuscator.fakeHandler();
      return switch (amplifier) {
         case 0 -> NumberGuard.f(0.25F);
         case 1 -> NumberGuard.f(0.37F);
         case 2 -> NumberGuard.f(0.46F);
         case 3 -> NumberGuard.f(0.7F);
         default -> NumberGuard.f(0.75F) + (float)(amplifier - NumberGuard.i(3)) * NumberGuard.f(0.05F);
      };
   }

   private float applyJumpBoostInternal(float motion) {
      FlowObfuscator.fakeHandler();
      if (mc.options.jumpKey.isPressed()) {
         return motion + NumberGuard.f(0.1F);
      }
      return motion;
   }

   private void fakeBoostModeInternal() {
      FlowObfuscator.fakeHandler();
      float boost = FAKE_BASE * FAKE_BOOST;
      entropy ^= (long) boost;
      SemanticNoise.deadCode1();
   }
}
