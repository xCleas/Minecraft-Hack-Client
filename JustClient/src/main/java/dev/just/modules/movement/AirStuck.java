package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.util.math.Vec3d;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AirStuck",
   desc = "SGF2YWRhIGFzxLFsxLEga2FsbWFuxLF6xLEgc2HEn2xhcg==",
   type = Type.Move
)
public class AirStuck extends Function {
   private BooleanSetting packet = new BooleanSetting(I0O1l0I1.b("SGFyZWtldGkgxLBwdGFsIEV0"), true, I0O1l0I1.b("U3VudWN1eWEgZ8O2bmRlcmlsZW4gaGFyZWtldCBwYWtldGxlcmluaSBpcHRhbCBlZGVy"));
   private Vec3d freezePosition = Vec3d.ZERO;

   private static final double FAKE_OFFSET = 500.0;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public AirStuck() {
      this.addSettings(new Setting[]{this.packet});
   }

   @Override
   public void onEnable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= System.nanoTime();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (checkPlayerValidInternal()) {
                  capturePositionInternal();
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(freezePosition, entropy);
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

   private int checkPlayerValidFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(mc.player != null);
   }

   private boolean checkPlayerValidInternal() {
      return lO1I0l1O.unbool(checkPlayerValidFlag());
   }

   private void capturePositionInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         this.freezePosition = mc.player.getPos();
      }
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
               if (!(event instanceof EventMotion)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (!validateStateInternal()) {
                  _s = 5;
                  break;
               }
               processFreezeInternal((EventMotion) event);
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

   private int validateStateFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(false);
      }
      return lO1I0l1O.bool(I1lO0l1I.all(
         mc.player != null,
         I1lO0l1I.not(this.freezePosition.equals(Vec3d.ZERO))
      ));
   }

   private boolean validateStateInternal() {
      return lO1I0l1O.unbool(validateStateFlag());
   }

   private void processFreezeInternal(EventMotion eventMotion) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               handlePacketCancelInternal(eventMotion);
               _s = 1;
               break;

            case 1:
               applyFreezeInternal();
               _s = 4;
               break;

            case 2:
               SemanticNoise.deadCode1();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(eventMotion, entropy);
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

   private void handlePacketCancelInternal(EventMotion eventMotion) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return;
      }
      if (this.packet.get()) {
         if (l1O0I1lO.opaqueTrue()) {
            eventMotion.setCancel(true);
         }
      }
   }

   private void applyFreezeInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.setPosition(this.freezePosition);
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   private void fakePhaseModeInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= System.nanoTime();
      freezePosition = new Vec3d(FAKE_OFFSET, FAKE_OFFSET, FAKE_OFFSET);
      SemanticNoise.deadCode2();
   }
}
