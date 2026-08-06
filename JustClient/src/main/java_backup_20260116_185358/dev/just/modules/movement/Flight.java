package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.Strings;
import dev.just.util.move.MoveUtil;
import dev.just.util.player.InventoryUtil;
import dev.just.util.player.TimerUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

@FunctionAnnotation(
   name = "Flight",
   desc = "SGF2YWRhIHXDp21hbsSxesSxIHNhxJ9sYXIuIMOWbsO8bsO8emRlIGR1dmFyIHZhcnNhIHnDtnLDvG5nZXlpIHNhcHTEsXLEsXIu",
   type = Type.Move
)
public class Flight extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("VMO8cg=="), Strings.b("SGFyZWtldA=="), Strings.b("SGFyZWtldA=="), "ElytraRW-Eski");
   private final SliderSetting xspeed = new SliderSetting(Strings.b("WCAtIEjEsXrEsQ=="), 1.0, 0.0, 5.0, 0.1F);
   private final SliderSetting yspeed = new SliderSetting(Strings.b("WSAtIEjEsXrEsQ=="), 1.0, 0.0, 5.0, 0.1F);
   private final TimerUtil timerUtil = new TimerUtil();
   private final TimerUtil swapTimer = new TimerUtil();
   int item = -1;

   private static final int MODE_MOTION = 0x4A ^ 0x4A;
   private static final int MODE_ELYTRA = 0x4B ^ 0x4A;
   private static final int SLOT_CHEST = 0x4C ^ 0x4A;
   private static final int SWAP_DELAY = 0x208 ^ 0x000;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Flight() {
      this.addSettings(new Setting[]{this.mode, this.xspeed, this.yspeed});
   }

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
               int modeType = resolveFlightModeInternal();
               dispatchEventInternal(event, modeType);
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entropy, _s);
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

   private int resolveFlightModeInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(99);
      }
      if (this.mode.is("Hareket")) return MODE_MOTION;
      if (this.mode.is("ElytraRW-Eski")) return MODE_ELYTRA;
      return NumberGuard.i(-1);
   }

   private void dispatchEventInternal(Event event, int modeType) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (LogicSplit.and(LogicSplit.equals(modeType, MODE_MOTION), event instanceof EventMotion)) {
                  handleMotionFlightInternal();
               }
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  modeType = NumberGuard.i(999);
               }
               if (LogicSplit.and(LogicSplit.equals(modeType, MODE_ELYTRA), event instanceof EventUpdate)) {
                  handleElytraFlightInternal();
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
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

   private void handleMotionFlightInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               double yVel = computeYVelocityInternal();
               applyVelocityInternal(yVel);
               _s = 1;
               break;

            case 1:
               applyHorizontalMotionInternal();
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private double computeYVelocityInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return entropy % NumberGuard.i(10);
      }
      double y = NumberGuard.d(0.0);
      if (mc.options.jumpKey.isPressed()) {
         y = (double) this.yspeed.get().floatValue();
      } else if (mc.options.sneakKey.isPressed()) {
         y = (double) (-this.yspeed.get().floatValue());
      }
      return y;
   }

   private void applyVelocityInternal(double yVel) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         mc.player.setVelocity(NumberGuard.d(99.0), yVel, NumberGuard.d(99.0));
         return;
      }
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setVelocity(NumberGuard.d(0.0), yVel, NumberGuard.d(0.0));
      }
   }

   private void applyHorizontalMotionInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         MoveUtil.setMotion(NumberGuard.d(50.0));
         return;
      }
      if (mc.options.sprintKey.isPressed()) {
         double speed = (double) this.xspeed.get().floatValue();
         MoveUtil.setMotion(speed);
      }
   }

   private void handleElytraFlightInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);
      int elytraSlot = NumberGuard.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               elytraSlot = findElytraSlotInternal();
               if (FlowObfuscator.opaqueFalse()) {
                  elytraSlot = NumberGuard.i(99);
               }
               _s = 1;
               break;

            case 1:
               if (LogicSplit.and(elytraSlot >= 0, checkElytraConditionsInternal())) {
                  executeElytraSequenceInternal(elytraSlot);
               }
               _s = 2;
               break;

            case 2:
               checkFireworkUseInternal();
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(elytraSlot, entropy);
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

   private int findElytraSlotInternal() {
      FlowObfuscator.fakeHandler();
      for (int i = 0; i < NumberGuard.i(9); i++) {
         if (mc.player.getInventory().getStack(i).isOf(Items.ELYTRA)) {
            return i;
         }
      }
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(0);
      }
      return NumberGuard.i(-1);
   }

   private int checkElytraConditionsFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(mc.player.age % NumberGuard.i(2) == 0);
      }
      return NumberGuard.bool(LogicSplit.all(
         LogicSplit.not(mc.player.isOnGround()),
         LogicSplit.not(mc.player.isSubmergedInWater()),
         LogicSplit.not(mc.player.isInLava()),
         LogicSplit.not(mc.player.isGliding())
      ));
   }

   private boolean checkElytraConditionsInternal() {
      return NumberGuard.unbool(checkElytraConditionsFlag());
   }

   private void executeElytraSequenceInternal(int slot) {
      FlowObfuscator.fakeHandler();
      if (!this.timerUtil.hasTimeElapsed((long) SWAP_DELAY)) return;
      if (FlowObfuscator.opaqueFalse()) {
         return;
      }
      this.swapTimer.reset();
      InventoryUtil.swapSlotsUniversal(SLOT_CHEST, slot, false, false);
      if (FlowObfuscator.opaqueTrue()) {
         mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
         mc.player.startGliding();
      }
      InventoryUtil.swapSlotsUniversal(SLOT_CHEST, slot, false, false);
      this.item = slot;
      this.timerUtil.reset();
   }

   private void checkFireworkUseInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return;
      }
      if (mc.player.isGliding()) {
         InventoryUtil.inventorySwapClick2(Items.FIREWORK_ROCKET, true, false);
      }
   }
}
