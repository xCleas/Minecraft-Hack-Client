package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.util.move.MoveUtil;
import net.minecraft.util.math.Vec3d;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "NoWeb",
   desc = "w5Zyw7xtY2VrIGHEn2xhcsSxIGnDp2luZGUgeWF2YcWfbGFtYW7EsXrEsSBlbmdlbGxlcg==",
   type = Type.Move
)
public class NoWeb extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("TW9k"), I0O1l0I1.b("w5Z6ZWw="), I0O1l0I1.b("w5Z6ZWw="), "ReallyWorld");
   private final SliderSetting speedXZ = new SliderSetting(I0O1l0I1.b("WCB2ZSBaIEjEsXrEsQ=="), 0.1F, 0.1F, 1.0, 0.1F, () -> this.mode.is(I0O1l0I1.b("w5Z6ZWw=")));
   private final SliderSetting speedY = new SliderSetting(I0O1l0I1.b("WSDEsHrEsQ=="), 0.1F, 0.1F, 4.0, 0.1F, () -> this.mode.is(I0O1l0I1.b("w5Z6ZWw=")));

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
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakePhaseModeInternal();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               int modeId = this.mode.getIndex();
               dispatchModeInternal(modeId);
               _s = 5;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               l1O0I1lO.fakeBranch(entropy, _s);
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

   private void dispatchModeInternal(int modeId) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (I1lO0l1I.equals(modeId, MODE_CUSTOM)) {
                  handleCustomInternal();
               } else if (I1lO0l1I.equals(modeId, MODE_RW)) {
                  handleReallyWorldInternal();
               }
               _s = 4;
               break;

            case 1:
               SemanticNoise.deadCode2();
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= modeId;
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

   private void handleCustomInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      Vec3d velocity = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(velocity, entropy);
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

   private double computeYVelocityInternal(Vec3d velocity) {
      l1O0I1lO.fakeHandler();
      if (mc.options.jumpKey.isPressed()) {
         return (double) this.speedY.get().floatValue();
      }
      if (mc.options.sneakKey.isPressed()) {
         return (double) (-this.speedY.get().floatValue());
      }
      if (l1O0I1lO.opaqueFalse()) {
         return FAKE_SPEED;
      }
      return lO1I0l1O.d(0.0);
   }

   private float getCustomSpeedXZInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f((float) FAKE_SPEED);
      }
      return SemanticNoise.o(this.speedXZ.get().floatValue());
   }

   private void handleReallyWorldInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);
      Vec3d velocity = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!MoveUtil.isInWeb()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               velocity = mc.player.getVelocity();
               double yVel = computeRWYVelocityInternal();
               float xzSpeed = lO1I0l1O.f(0.21F);
               applyVelocityInternal(velocity.x, yVel, velocity.z, xzSpeed);
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

   private double computeRWYVelocityInternal() {
      l1O0I1lO.fakeHandler();
      if (mc.options.jumpKey.isPressed()) {
         return lO1I0l1O.d(0.9);
      }
      if (mc.options.sneakKey.isPressed()) {
         return lO1I0l1O.d(-0.9);
      }
      return lO1I0l1O.d(0.0);
   }

   private void applyVelocityInternal(double x, double y, double z, float speed) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.setVelocity(x, y, z);
         MoveUtil.setSpeed(speed);
      }
   }

   private void fakePhaseModeInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= System.nanoTime();
      MoveUtil.setSpeed((float) FAKE_SPEED);
      SemanticNoise.deadCode1();
   }
}
