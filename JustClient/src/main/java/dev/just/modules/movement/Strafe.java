package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.util.move.MoveUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Strafe",
   desc = "SMSxemzEsSBoYXJla2V0IGV0bWVuaXppIHZlIGhhdmFkYSB5w7ZuIGtvbnRyb2zDvCB5YXBtYW7EsXrEsSBzYcSfbGFy",
   type = Type.Move
)
public class Strafe extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("VMO8cg=="), "MetaHvH", "MetaHvH");

   private static final float FAKE_BASE = 1.5f;
   private static final float FAKE_BOOST = 2.0f;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Strafe() {
      this.addSettings(new Setting[]{this.mode});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakeBoostModeInternal();
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  l1O0I1lO.fakeBranch(event, entropy);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);
      int result = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (mc.player == null) {
                  result = lO1I0l1O.i(0);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (!this.mode.is("MetaHvH")) {
                  result = lO1I0l1O.i(0);
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
                  _s = 4;
                  break;
               }
               result = lO1I0l1O.bool(I1lO0l1I.all(
                  I1lO0l1I.not(mc.player.isGliding()),
                  I1lO0l1I.or(I1lO0l1I.not(mc.player.isTouchingWater()), I1lO0l1I.not(mc.player.isSwimming())),
                  MoveUtil.isMoving()
               ));
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeBranch(result, entropy);
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
      return lO1I0l1O.unbool(checkConditionsFlag());
   }

   private void processStrafeInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      float motion = lO1I0l1O.f(0.0f);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               motion = computeBaseMotionInternal();
               _s = 1;
               break;

            case 1:
               motion = applySpeedEffectInternal(motion);
               motion = applyJumpBoostInternal(motion);
               _s = 2;
               break;

            case 2:
               if (l1O0I1lO.opaqueTrue()) {
                  MoveUtil.setMotion((double) motion);
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(motion, entropy);
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
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f(FAKE_BASE);
      }
      return lO1I0l1O.f(0.19F);
   }

   private float applySpeedEffectInternal(float motion) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);
      float result = motion;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  result = motion * FAKE_BOOST;
                  _s = 4;
                  break;
               }
               result = computeSpeedValue(amplifier);
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.f(FAKE_BOOST);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeBranch(result, entropy);
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
      l1O0I1lO.fakeHandler();
      return switch (amplifier) {
         case 0 -> lO1I0l1O.f(0.25F);
         case 1 -> lO1I0l1O.f(0.37F);
         case 2 -> lO1I0l1O.f(0.46F);
         case 3 -> lO1I0l1O.f(0.7F);
         default -> lO1I0l1O.f(0.75F) + (float)(amplifier - lO1I0l1O.i(3)) * lO1I0l1O.f(0.05F);
      };
   }

   private float applyJumpBoostInternal(float motion) {
      l1O0I1lO.fakeHandler();
      if (mc.options.jumpKey.isPressed()) {
         return motion + lO1I0l1O.f(0.1F);
      }
      return motion;
   }

   private void fakeBoostModeInternal() {
      l1O0I1lO.fakeHandler();
      float boost = FAKE_BASE * FAKE_BOOST;
      entropy ^= (long) boost;
      SemanticNoise.deadCode1();
   }
}
