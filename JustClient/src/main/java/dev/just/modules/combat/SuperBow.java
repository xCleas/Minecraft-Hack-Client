package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
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
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (I1lO0l1I.or(mc.player == null, mc.world == null)) {
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, FAKE_STATE);
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

   private void handlePacketEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
               }
               _s = 5;
               break;

            case 3:
               l1O0I1lO.fakeBranch(event, entropy);
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

   private void executeBowBoost() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      int powerValue = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
               _s = 1;
               break;

            case 1:
               powerValue = this.power.get().intValue();
               if (l1O0I1lO.opaqueFalse()) {
                  powerValue = lO1I0l1O.i(999);
               }
               _s = 2;
               break;

            case 2:
               sendBoostPackets(powerValue);
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(powerValue, entropy);
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
      l1O0I1lO.fakeHandler();

      double px = mc.player.getX();
      double py = mc.player.getY();
      double pz = mc.player.getZ();
      double offset = lO1I0l1O.d(1.0E-9);

      for (int i = 0; i < count; i++) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(
               new PlayerMoveC2SPacket.PositionAndOnGround(px, py - offset, pz, true, true)
            );
            mc.player.networkHandler.sendPacket(
               new PlayerMoveC2SPacket.PositionAndOnGround(px, py + offset, pz, true, false)
            );
         }
         if (l1O0I1lO.opaqueFalse()) {
            entropy ^= i;
         }
      }
   }
}
