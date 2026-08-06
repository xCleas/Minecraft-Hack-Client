package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKey;
import dev.just.events.impl.input.EventMouse;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.InventoryUtil;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "MiddleClickPearl",
   keywords = {"MCP"},
   desc = "RmFyZSB0ZWtlcmxlxJ9pbmUgYmFzYXJhayBFbmRlciBpbmNpc2kgZsSxcmxhdMSxcg==",
   type = Type.Player
)
public class MiddleClickPearl extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("w4dhbMSxxZ9tYSBUw7xyw7w="), I0O1l0I1.b("Tm9ybWFs"), I0O1l0I1.b("Tm9ybWFs"), I0O1l0I1.b("VHXFnyBBdGFtYWzEsQ=="));
   private final BindSetting bind = new BindSetting(I0O1l0I1.b("RsSxcmxhdG1hIFR1xZ91"), 0, () -> this.mode.is(I0O1l0I1.b("VHXFnyBBdGFtYWzEsQ==")));
   private final BooleanSetting inventoryUse = new BooleanSetting(I0O1l0I1.b("RW52YW50ZXJkZW4gS3VsbGFu"), true, I0O1l0I1.b("SG9sbHlXb3JsZCBzdW51Y3VzdW5kYSBrdWxsYW5tYXnEsW4gKFlhc2FrbGFubWEgcmlza2khKQ=="));

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public MiddleClickPearl() {
      this.addSettings(new Setting[]{this.mode, this.bind, this.inventoryUse});
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
               if (I1lO0l1I.and(this.mode.is(I0O1l0I1.b("Tm9ybWFs")), event instanceof EventMouse)) {
                  EventMouse mouseTick = (EventMouse) event;
                  if (mouseTick.getButton() == lO1I0l1O.i(2)) {
                     this.handleMouseTickEvent();
                  }
               }
               _s = 2;
               break;

            case 2:
               if (I1lO0l1I.and(this.mode.is(I0O1l0I1.b("VHXFnyBBdGFtYWzEsQ==")), event instanceof EventKey)) {
                  EventKey e = (EventKey) event;
                  if (e.key == this.bind.getKey()) {
                     this.handleMouseTickEvent();
                  }
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
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

   private void handleMouseTickEvent() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (mc.player.getItemCooldownManager().isCoolingDown(Items.ENDER_PEARL.getDefaultStack())) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
                  if (l1O0I1lO.opaqueTrue()) {
                     mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                        mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                        mc.player.getYaw(), mc.player.getPitch(),
                        mc.player.isOnGround(), mc.player.horizontalCollision
                     ));
                  }
               }
               _s = 2;
               break;

            case 2:
               if (l1O0I1lO.opaqueTrue()) {
                  InventoryUtil.inventorySwapClick2(Items.ENDER_PEARL, this.inventoryUse.get(), true);
               }
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
