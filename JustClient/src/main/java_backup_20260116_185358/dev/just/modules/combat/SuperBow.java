package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@FunctionAnnotation(
   name = "SuperBow",
   type = Type.Combat,
   desc = "Yaydan \u00e7\u0131kan okun hasar g\u00fcc\u00fcn\u00fc art\u0131r\u0131r"
)
public class SuperBow extends Function {
   private final SliderSetting power = new SliderSetting("G\u00fc\u00e7", 30.0, 1.0, 200.0, 1.0);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public SuperBow() {
      this.addSettings(new Setting[]{this.power});
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
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (LogicSplit.or(mc.player == null, mc.world == null)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handlePacketEvent(event);
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, FAKE_STATE);
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

   private void handlePacketEvent(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!(event instanceof EventPacket e)) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventPacket e && e.getPacket() instanceof PlayerActionC2SPacket p) {
                  if (p.getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM) {
                     executeBowBoost();
                  }
               }
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeBranch(event, entropy);
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

   private void executeBowBoost() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      int powerValue = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
               _s = 1;
               break;

            case 1:
               powerValue = this.power.get().intValue();
               if (FlowObfuscator.opaqueFalse()) {
                  powerValue = NumberGuard.i(999);
               }
               _s = 2;
               break;

            case 2:
               sendBoostPackets(powerValue);
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(powerValue, entropy);
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

   private void sendBoostPackets(int count) {
      FlowObfuscator.fakeHandler();

      double px = mc.player.getX();
      double py = mc.player.getY();
      double pz = mc.player.getZ();
      double offset = NumberGuard.d(1.0E-9);

      for (int i = 0; i < count; i++) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(
               new PlayerMoveC2SPacket.PositionAndOnGround(px, py - offset, pz, true, true)
            );
            mc.player.networkHandler.sendPacket(
               new PlayerMoveC2SPacket.PositionAndOnGround(px, py + offset, pz, true, false)
            );
         }
         if (FlowObfuscator.opaqueFalse()) {
            entropy ^= i;
         }
      }
   }
}
