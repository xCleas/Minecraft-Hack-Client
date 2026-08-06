package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.movement.Speed;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.ExecutionPath;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "TargetStrafe",
   type = Type.Combat,
   desc = "SGVkZWYgZXRyYWbEsW5kYSBvdG9tYXRpayBkYWlyZWxlciDDp2l6ZXJlayBoYXJla2V0IGVkZXI="
)
public class TargetStrafe extends Function {
   public final SliderSetting speedSlider = new SliderSetting(Strings.b("SMSxeg=="), 0.095F, 0.01F, 1.2F, 0.01F);
   public final ModeSetting ptytag = new ModeSetting(Strings.b("w4dla2ltIE1vZHU="), "Vector", "Vector", "Motion / Velocity");
   public final SliderSetting blocks = new SliderSetting(Strings.b("w4dla2ltIE1lc2FmZXNp"), 7.0, 0.01F, 12.0, 0.01F);
   public final SliderSetting hitbox = new SliderSetting(Strings.b("Qm9vc3QgSGl0Ym94dQ=="), 0.095F, 0.01F, 50.0, 0.01F);
   public final BooleanSetting predictCheck = new BooleanSetting(Strings.b("VGFobWluIChQcmVkaWN0KQ=="), true);
   public final SliderSetting predict = new SliderSetting(Strings.b("VGFobWluIERlxJ9lcmk="), 2.5, 0.1F, 4.0, 0.1F, () -> this.predictCheck.get());
   public final BooleanSetting predictView = new BooleanSetting(Strings.b("VGFobWluaSBHw7ZzdGVy"), false, Strings.b("S2VuZGkgZWtyYW7EsW7EsXpkYSByYWtpYmkgZ2XDp2l5b3JtdcWfIGdpYmkgZ8O2csO8bsO8cnPDvG7DvHo="));

   private static final float FAKE_SPEED = 2.5f;
   private static final double FAKE_RADIUS = 15.0;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public TargetStrafe() {
      this.addSettings(new Setting[]{this.speedSlider, this.ptytag, this.blocks, this.hitbox, this.predictCheck, this.predict, this.predictView});
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
                  fakeProcessInternal();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  fakeProcessInternal();
               }
               _s = 2;
               break;

            case 2:
               SemanticNoise.deadCode2();
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.l(FAKE_STATE);
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

   private void fakeProcessInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 4);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               float speed = FAKE_SPEED * NumberGuard.f(1.5f);
               double radius = FAKE_RADIUS + entropy;
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

   public float getSpeed() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      float result = NumberGuard.f(0.0f);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.f(FAKE_SPEED);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               result = SemanticNoise.o(this.speedSlider.get().floatValue());
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

   public double getBlocks() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);
      double result = NumberGuard.d(0.0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.d(FAKE_RADIUS);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               result = ExecutionPath.multiPath((int)(this.blocks.get().doubleValue() * NumberGuard.i(100))) / NumberGuard.d(100.0);
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(result, entropy);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
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

   @Override
   protected void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x79DF, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueTrue() && mc.options.forwardKey.isPressed()) {
                  mc.options.forwardKey.setPressed(false);
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

   @Override
   public void onEnable() {
      int _s = ControlFlow.next(hashCode() ^ 0x5F37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               Speed speed = Manager.FUNCTION_MANAGER.speed;
               if (FlowObfuscator.opaqueTrue() && speed.state) {
                  speed.setState(false);
               }
               _s = 1;
               break;

            case 1:
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
}
