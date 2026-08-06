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
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "MiddleClickPearl",
   keywords = {"MCP"},
   desc = "RmFyZSB0ZWtlcmxlxJ9pbmUgYmFzYXJhayBFbmRlciBpbmNpc2kgZsSxcmxhdMSxcg==",
   type = Type.Player
)
public class MiddleClickPearl extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("w4dhbMSxxZ9tYSBUw7xyw7w="), Strings.b("Tm9ybWFs"), Strings.b("Tm9ybWFs"), Strings.b("VHXFnyBBdGFtYWzEsQ=="));
   private final BindSetting bind = new BindSetting(Strings.b("RsSxcmxhdG1hIFR1xZ91"), 0, () -> this.mode.is(Strings.b("VHXFnyBBdGFtYWzEsQ==")));
   private final BooleanSetting inventoryUse = new BooleanSetting(Strings.b("RW52YW50ZXJkZW4gS3VsbGFu"), true, Strings.b("SG9sbHlXb3JsZCBzdW51Y3VzdW5kYSBrdWxsYW5tYXnEsW4gKFlhc2FrbGFubWEgcmlza2khKQ=="));

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public MiddleClickPearl() {
      this.addSettings(new Setting[]{this.mode, this.bind, this.inventoryUse});
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
               if (LogicSplit.and(this.mode.is(Strings.b("Tm9ybWFs")), event instanceof EventMouse)) {
                  EventMouse mouseTick = (EventMouse) event;
                  if (mouseTick.getButton() == NumberGuard.i(2)) {
                     this.handleMouseTickEvent();
                  }
               }
               _s = 2;
               break;

            case 2:
               if (LogicSplit.and(this.mode.is(Strings.b("VHXFnyBBdGFtYWzEsQ==")), event instanceof EventKey)) {
                  EventKey e = (EventKey) event;
                  if (e.key == this.bind.getKey()) {
                     this.handleMouseTickEvent();
                  }
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
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

   private void handleMouseTickEvent() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (mc.player.getItemCooldownManager().isCoolingDown(Items.ENDER_PEARL.getDefaultStack())) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
                  if (FlowObfuscator.opaqueTrue()) {
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
               if (FlowObfuscator.opaqueTrue()) {
                  InventoryUtil.inventorySwapClick2(Items.ENDER_PEARL, this.inventoryUse.get(), true);
               }
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
