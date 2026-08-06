package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "FullBright",
   desc = "w4dldnJleWkgdGFtYW1lbiBheWTEsW5sYXTEsXI=",
   type = Type.Render
)
public class FullBright extends Function {
   private final StatusEffectInstance nightVisionEffect = new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 255, false, false, true);

   private static final int FAKE_DURATION = 999999;
   private static final int FAKE_AMPLIFIER = 50;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakeModeInternal();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventUpdate)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               applyNightVisionInternal();
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

   private void applyNightVisionInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueTrue()) {
                  mc.player.addStatusEffect(this.nightVisionEffect, mc.player);
               }
               _s = 2;
               break;

            case 2:
               SemanticNoise.deadCode1();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_DURATION;
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
   public void onDisable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               removeNightVisionInternal();
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

   private void removeNightVisionInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.removeStatusEffect(this.nightVisionEffect.getEffectType());
      }
   }

   private void fakeModeInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= FAKE_DURATION;
      SemanticNoise.deadCode2();
   }
}
