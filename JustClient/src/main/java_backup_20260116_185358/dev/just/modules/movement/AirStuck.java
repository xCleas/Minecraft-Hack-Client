package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.util.math.Vec3d;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "AirStuck",
   desc = "SGF2YWRhIGFzxLFsxLEga2FsbWFuxLF6xLEgc2HEn2xhcg==",
   type = Type.Move
)
public class AirStuck extends Function {
   private BooleanSetting packet = new BooleanSetting(Strings.b("SGFyZWtldGkgxLBwdGFsIEV0"), true, Strings.b("U3VudWN1eWEgZ8O2bmRlcmlsZW4gaGFyZWtldCBwYWtldGxlcmluaSBpcHRhbCBlZGVy"));
   private Vec3d freezePosition = Vec3d.ZERO;

   private static final double FAKE_OFFSET = 500.0;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public AirStuck() {
      this.addSettings(new Setting[]{this.packet});
   }

   @Override
   public void onEnable() {
      int _s = ControlFlow.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(freezePosition, entropy);
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

   private int checkPlayerValidFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > NumberGuard.l(0));
      }
      return NumberGuard.bool(mc.player != null);
   }

   private boolean checkPlayerValidInternal() {
      return NumberGuard.unbool(checkPlayerValidFlag());
   }

   private void capturePositionInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         this.freezePosition = mc.player.getPos();
      }
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

   private int validateStateFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(false);
      }
      return NumberGuard.bool(LogicSplit.all(
         mc.player != null,
         LogicSplit.not(this.freezePosition.equals(Vec3d.ZERO))
      ));
   }

   private boolean validateStateInternal() {
      return NumberGuard.unbool(validateStateFlag());
   }

   private void processFreezeInternal(EventMotion eventMotion) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(eventMotion, entropy);
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
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return;
      }
      if (this.packet.get()) {
         if (FlowObfuscator.opaqueTrue()) {
            eventMotion.setCancel(true);
         }
      }
   }

   private void applyFreezeInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setPosition(this.freezePosition);
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   private void fakePhaseModeInternal() {
      FlowObfuscator.fakeHandler();
      entropy ^= System.nanoTime();
      freezePosition = new Vec3d(FAKE_OFFSET, FAKE_OFFSET, FAKE_OFFSET);
      SemanticNoise.deadCode2();
   }
}
