package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.util.move.MoveUtil;
import net.minecraft.util.math.Vec3d;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "NoWeb",
   desc = "w5Zyw7xtY2VrIGHEn2xhcsSxIGnDp2luZGUgeWF2YcWfbGFtYW7EsXrEsSBlbmdlbGxlcg==",
   type = Type.Move
)
public class NoWeb extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("TW9k"), Strings.b("w5Z6ZWw="), Strings.b("w5Z6ZWw="), "ReallyWorld");
   private final SliderSetting speedXZ = new SliderSetting(Strings.b("WCB2ZSBaIEjEsXrEsQ=="), 0.1F, 0.1F, 1.0, 0.1F, () -> this.mode.is(Strings.b("w5Z6ZWw=")));
   private final SliderSetting speedY = new SliderSetting(Strings.b("WSDEsHrEsQ=="), 0.1F, 0.1F, 4.0, 0.1F, () -> this.mode.is(Strings.b("w5Z6ZWw=")));

   private static final int MODE_CUSTOM = 0x4A ^ 0x4A;
   private static final int MODE_RW = 0x4B ^ 0x4A;
   private static final double FAKE_SPEED = 5.0;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public NoWeb() {
      this.addSettings(new Setting[]{this.mode, this.speedXZ, this.speedY});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  fakePhaseModeInternal();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               int modeId = this.mode.getIndex();
               dispatchModeInternal(modeId);
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entropy, _s);
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

   private void dispatchModeInternal(int modeId) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (LogicSplit.equals(modeId, MODE_CUSTOM)) {
                  handleCustomInternal();
               } else if (LogicSplit.equals(modeId, MODE_RW)) {
                  handleReallyWorldInternal();
               }
               _s = 4;
               break;

            case 1:
               SemanticNoise.deadCode2();
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= modeId;
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

   private void handleCustomInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      Vec3d velocity = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!MoveUtil.isInWeb()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               velocity = mc.player.getVelocity();
               double yVel = computeYVelocityInternal(velocity);
               float xzSpeed = getCustomSpeedXZInternal();
               applyVelocityInternal(velocity.x, yVel, velocity.z, xzSpeed);
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(velocity, entropy);
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

   private double computeYVelocityInternal(Vec3d velocity) {
      FlowObfuscator.fakeHandler();
      if (mc.options.jumpKey.isPressed()) {
         return (double) this.speedY.get().floatValue();
      }
      if (mc.options.sneakKey.isPressed()) {
         return (double) (-this.speedY.get().floatValue());
      }
      if (FlowObfuscator.opaqueFalse()) {
         return FAKE_SPEED;
      }
      return NumberGuard.d(0.0);
   }

   private float getCustomSpeedXZInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f((float) FAKE_SPEED);
      }
      return SemanticNoise.o(this.speedXZ.get().floatValue());
   }

   private void handleReallyWorldInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);
      Vec3d velocity = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!MoveUtil.isInWeb()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               velocity = mc.player.getVelocity();
               double yVel = computeRWYVelocityInternal();
               float xzSpeed = NumberGuard.f(0.21F);
               applyVelocityInternal(velocity.x, yVel, velocity.z, xzSpeed);
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

   private double computeRWYVelocityInternal() {
      FlowObfuscator.fakeHandler();
      if (mc.options.jumpKey.isPressed()) {
         return NumberGuard.d(0.9);
      }
      if (mc.options.sneakKey.isPressed()) {
         return NumberGuard.d(-0.9);
      }
      return NumberGuard.d(0.0);
   }

   private void applyVelocityInternal(double x, double y, double z, float speed) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setVelocity(x, y, z);
         MoveUtil.setSpeed(speed);
      }
   }

   private void fakePhaseModeInternal() {
      FlowObfuscator.fakeHandler();
      entropy ^= System.nanoTime();
      MoveUtil.setSpeed((float) FAKE_SPEED);
      SemanticNoise.deadCode1();
   }
}
