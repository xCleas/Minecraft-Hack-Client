package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.player.EventAttack;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Criticals",
   type = Type.Combat,
   desc = "WsSxcGxhbWFuxLF6YSBnZXJlayBrYWxtYWRhbiBoZXIgdnVydcWfdW4ga3JpdGlrIG9sbWFzxLFuxLEgc2HEn2xhcg=="
)
public class Criticals extends Function {

   private static final double CRIT_OFFSET = 0.01250004768372;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

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
               if (!(event instanceof EventAttack)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               executeCriticalInternal();
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

   private void executeCriticalInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!checkConditionsInternal()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               double offset = computeOffsetInternal();
               sendPacketsInternal(offset);
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

   private int checkConditionsFlag() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.or(mc.player == null, mc.world == null)) {
         return NumberGuard.bool(false);
      }
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(mc.player.age % NumberGuard.i(2) == 0);
      }
      return NumberGuard.bool(LogicSplit.any(
         mc.player.isOnGround(),
         mc.player.getAbilities().flying,
         mc.player.isTouchingWater(),
         LogicSplit.and(
            LogicSplit.not(mc.player.isInLava()),
            LogicSplit.not(mc.player.isSubmergedInWater())
         )
      ));
   }

   private boolean checkConditionsInternal() {
      return NumberGuard.unbool(checkConditionsFlag());
   }

   private double computeOffsetInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.d(1.0);
      }
      return NumberGuard.d(CRIT_OFFSET);
   }

   private void sendPacketsInternal(double y) {
      FlowObfuscator.fakeHandler();
      double px = mc.player.getX();
      double py = mc.player.getY();
      double pz = mc.player.getZ();

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(px, py + y, pz, false, true)
         );
         mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(px, py, pz, false, true)
         );
      }
   }
}
