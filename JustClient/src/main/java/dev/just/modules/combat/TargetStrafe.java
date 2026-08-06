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
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.ExecutionPath;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "TargetStrafe",
   type = Type.Combat,
   desc = "SGVkZWYgZXRyYWbEsW5kYSBvdG9tYXRpayBkYWlyZWxlciDDp2l6ZXJlayBoYXJla2V0IGVkZXI="
)
public class TargetStrafe extends Function {
   public final SliderSetting speedSlider = new SliderSetting(I0O1l0I1.b("SMSxeg=="), 0.095F, 0.01F, 1.2F, 0.01F);
   public final ModeSetting ptytag = new ModeSetting(I0O1l0I1.b("w4dla2ltIE1vZHU="), "Vector", "Vector", "Motion / Velocity");
   public final SliderSetting blocks = new SliderSetting(I0O1l0I1.b("w4dla2ltIE1lc2FmZXNp"), 7.0, 0.01F, 12.0, 0.01F);
   public final SliderSetting hitbox = new SliderSetting(I0O1l0I1.b("Qm9vc3QgSGl0Ym94dQ=="), 0.095F, 0.01F, 50.0, 0.01F);
   public final BooleanSetting predictCheck = new BooleanSetting(I0O1l0I1.b("VGFobWluIChQcmVkaWN0KQ=="), true);
   public final SliderSetting predict = new SliderSetting(I0O1l0I1.b("VGFobWluIERlxJ9lcmk="), 2.5, 0.1F, 4.0, 0.1F, () -> this.predictCheck.get());
   public final BooleanSetting predictView = new BooleanSetting(I0O1l0I1.b("VGFobWluaSBHw7ZzdGVy"), false, I0O1l0I1.b("S2VuZGkgZWtyYW7EsW7EsXpkYSByYWtpYmkgZ2XDp2l5b3JtdcWfIGdpYmkgZ8O2csO8bsO8cnPDvG7DvHo="));

   private static final float FAKE_SPEED = 2.5f;
   private static final double FAKE_RADIUS = 15.0;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public TargetStrafe() {
      this.addSettings(new Setting[]{this.speedSlider, this.ptytag, this.blocks, this.hitbox, this.predictCheck, this.predict, this.predictView});
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
                  fakeProcessInternal();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.l(FAKE_STATE);
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

   private void fakeProcessInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 4);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               float speed = FAKE_SPEED * lO1I0l1O.f(1.5f);
               double radius = FAKE_RADIUS + entropy;
               _s = 1;
               break;

            case 1:
               ExecutionPath.randomDelay();
               _s = 3;
               break;

            case 2:
               l1O0I1lO.fakeHandler();
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      float result = lO1I0l1O.f(0.0f);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.f(FAKE_SPEED);
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
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.f(entropy % FAKE_STATE);
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

   public double getBlocks() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);
      double result = lO1I0l1O.d(0.0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.d(FAKE_RADIUS);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               result = ExecutionPath.multiPath((int)(this.blocks.get().doubleValue() * lO1I0l1O.i(100))) / lO1I0l1O.d(100.0);
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(result, entropy);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x79DF, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueTrue() && mc.options.forwardKey.isPressed()) {
                  mc.options.forwardKey.setPressed(false);
               }
               _s = 2;
               break;

            case 2:
               super.onDisable();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x5F37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               Speed speed = Manager.FUNCTION_MANAGER.speed;
               if (l1O0I1lO.opaqueTrue() && speed.state) {
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
}
