package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@FunctionAnnotation(
   name = "Velocity",
   keywords = {"AKB", "AntiKnockBack", "R2VyaSBUZXBtZQ=="},
   type = Type.Combat,
   desc = "QWzEsW5hbiBnZXJpIHRlcG1leWkgdmUgc2F2cnVsbWF5xLEgdGFtYW1lbiBpcHRhbCBlZGVy"
)
public class Velocity extends Function {
   public ModeSetting mode = new ModeSetting("Mod", Strings.b("xLBwdGFs"), Strings.b("xLBwdGFs"), "Grim");

   private static final int STATE_CANCEL = 0x4A ^ 0x4A;
   private static final int STATE_GRIM = 0x4B ^ 0x4A;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int internalState = 0;
   private volatile int entropy = (int) System.nanoTime();

   public Velocity() {
      this.addSettings(new Setting[]{this.mode});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  internalState = NumberGuard.i(999);
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (mc.player == null) {
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               processPacketInternal(event);
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  internalState = NumberGuard.c(entropy, FAKE_STATE);
                  FlowObfuscator.fakeBranch(event, internalState);
               }
               _s = 4;
               break;

            case 4:
               return;

            default:
               FlowObfuscator.fakeBranch(entropy, _s);
               _s = 4;
               break;
         }
      }
   }

   private void processPacketInternal(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 6);
      EventPacket eventPacket = null;
      int modeState = NumberGuard.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!(event instanceof EventPacket)) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               eventPacket = (EventPacket) event;
               _s = 2;
               break;

            case 2:
               modeState = computeModeState();
               if (FlowObfuscator.opaqueFalse()) {
                  modeState = NumberGuard.i(99);
                  entropy ^= modeState;
               }
               _s = 3;
               break;

            case 3:
               if (LogicSplit.equals(modeState, STATE_CANCEL)) {
                  handleCancelInternal(eventPacket);
               }
               _s = 5;
               break;

            case 4:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(modeState, FAKE_STATE);
                  internalState = NumberGuard.bits(entropy);
               }
               _s = 5;
               break;

            case 5:
               return;

            default:
               FlowObfuscator.fakeHandler();
               _s = 5;
               break;
         }
      }
   }

   private int computeModeState() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      int result = NumberGuard.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.i(42);
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               String current = this.mode.get();
               boolean isCancel = LogicSplit.equals(current.hashCode(), "\u0130ptal".hashCode());
               if (FlowObfuscator.opaqueTrue() && isCancel) {
                  result = STATE_CANCEL;
               } else {
                  result = STATE_GRIM;
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = FAKE_STATE;
                  entropy += result;
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

   private void handleCancelInternal(EventPacket eventPacket) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 6);
      EntityVelocityUpdateS2CPacket packet = null;
      int shouldCancelFlag = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!(eventPacket.getPacket() instanceof EntityVelocityUpdateS2CPacket)) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               packet = (EntityVelocityUpdateS2CPacket) eventPacket.getPacket();
               _s = 2;
               break;

            case 2:
               shouldCancelFlag = verifyPacket(packet);
               if (FlowObfuscator.opaqueFalse()) {
                  shouldCancelFlag = NumberGuard.bool(packet.getEntityId() > NumberGuard.i(99999));
               }
               _s = 3;
               break;

            case 3:
               if (FlowObfuscator.opaqueTrue() && NumberGuard.unbool(shouldCancelFlag)) {
                  internalState++;
                  eventPacket.setCancel(true);
               }
               _s = 5;
               break;

            case 4:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= shouldCancelFlag;
                  FlowObfuscator.fakeBranch(packet, entropy);
               }
               _s = 5;
               break;

            case 5:
               return;

            default:
               FlowObfuscator.fakeHandler();
               _s = 5;
               break;
         }
      }
   }

   private int verifyPacket(EntityVelocityUpdateS2CPacket packet) {
      int _s = ControlFlow.next(hashCode() ^ 0x79DF, 5);
      int packetId = NumberGuard.i(-1);
      int playerId = NumberGuard.i(-1);
      int result = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               packetId = packet.getEntityId();
               _s = 1;
               break;

            case 1:
               playerId = mc.player.getId();
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.bool(packetId == NumberGuard.i(-1));
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               result = NumberGuard.bool(LogicSplit.equals(packetId, playerId));
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.bool(entropy == FAKE_STATE);
                  FlowObfuscator.fakeBranch(packetId, playerId);
               }
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
}
