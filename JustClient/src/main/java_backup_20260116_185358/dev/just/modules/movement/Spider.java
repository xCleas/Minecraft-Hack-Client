package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.TimerUtil;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.util.Hand;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Spider",
   desc = "WsSxcGxhZMSxa3RhbiBzb25yYSBvdG9tYXRpayBvbGFyYWsgc3Uga295YXIgdmUgdMSxcm1hbm1hbsSxesSxIHNhxJ9sYXI=",
   type = Type.Move
)
public class Spider extends Function {
   public final ModeSetting mode = new ModeSetting(Strings.b("VMO8cg=="), "RwSu", "RwSu", "Matrix");
   private final TimerUtil timerUtil = new TimerUtil();
   private boolean climbing = false;
   private boolean switched = false;
   private int waterSlot = -1;
   private int originalSlot = -1;
   private int movedFromInvSlot = -1;

   private static final double FAKE_VELOCITY = 5.0;
   private static final int FAKE_SLOT = 99;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Spider() {
      this.addSettings(new Setting[]{this.mode});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  fakeClimbModeInternal();
                  _s = 6;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 7);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!validatePlayerInternal()) {
                  _s = 6;
                  break;
               }
               _s = 2;
               break;

            case 2:
               dispatchEventInternal(event);
               _s = 6;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entropy, _s);
               _s = 6;
               break;

            case 5:
               FlowObfuscator.fakeHandler();
               _s = 6;
               break;

            case 6:
               return;

            default:
               _s = 6;
               break;
         }
      }
   }

   private int validatePlayerFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > NumberGuard.l(0));
      }
      return NumberGuard.bool(LogicSplit.all(mc.player != null, mc.world != null));
   }

   private boolean validatePlayerInternal() {
      return NumberGuard.unbool(validatePlayerFlag());
   }

   private void dispatchEventInternal(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (event instanceof EventPacket eventPacket) {
                  handlePacketInternal(eventPacket);
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  handleUpdateInternal();
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventMotion) {
                  handleMotionInternal();
               }
               _s = 5;
               break;

            case 3:
               SemanticNoise.deadCode1();
               _s = 5;
               break;

            case 4:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
               }
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

   private void handlePacketInternal(EventPacket eventPacket) {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!(eventPacket.getPacket() instanceof GameMessageS2CPacket packet)) {
                  _s = 4;
                  break;
               }
               String message = packet.content().getString();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= message.hashCode();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               GameMessageS2CPacket pkt = (GameMessageS2CPacket) eventPacket.getPacket();
               String msg = pkt.content().getString();
               if (shouldCancelMessageInternal(msg)) {
                  if (FlowObfuscator.opaqueTrue()) {
                     eventPacket.setCancel(true);
                  }
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(eventPacket, entropy);
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

   private int shouldCancelMessageFlag(String message) {
      FlowObfuscator.fakeHandler();
      return NumberGuard.bool(LogicSplit.or(
         message.contains("Uzgunum, buraya blok koyamazsiniz."),
         message.contains(Strings.b("w5x6Z8O8bsO8bSwgYnVyYXlhIGJsb2sga295YW1henPEsW7EsXou")),
         message.contains("Sorry, you cannot place a block here")
      ));
   }

   private boolean shouldCancelMessageInternal(String message) {
      return NumberGuard.unbool(shouldCancelMessageFlag(message));
   }

   private void handleUpdateInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (this.mode.is("RwSu")) {
                  handleRwWaterInternal();
               } else if (this.mode.is("Matrix")) {
                  handleMatrixInternal();
               }
               _s = 4;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
               }
               _s = 4;
               break;

            case 2:
               FlowObfuscator.fakeBranch(climbing, entropy);
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

   private void handleMotionInternal() {
      FlowObfuscator.fakeHandler();
      if (!this.climbing) return;
      if (!this.mode.is("RwSu")) return;
      applyRotationInternal();
   }

   private void applyRotationInternal() {
      FlowObfuscator.fakeHandler();
      Vec3d eyePos = mc.player.getCameraPosVec(NumberGuard.f(1.0F));
      Vec3d lookVec = mc.player.getRotationVec(NumberGuard.f(1.0F)).normalize();
      Vec3d targetVec = eyePos.add(lookVec);
      BlockPos targetBlock = new BlockPos((int)targetVec.x, (int)targetVec.y - NumberGuard.i(1), (int)targetVec.z);
      float neededYaw = getNeededYawInternal(targetBlock.toCenterPos());
      float neededPitch = getNeededPitchInternal(targetBlock.toCenterPos());
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setPitch(neededPitch);
         mc.player.setYaw(neededYaw);
      }
   }

   private void handleRwWaterInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (shouldStartClimbingInternal()) {
                  startClimbingInternal();
               }
               _s = 1;
               break;

            case 1:
               if (this.climbing) {
                  processClimbingInternal();
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(climbing, entropy);
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

   private int shouldStartClimbingFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > NumberGuard.l(0));
      }
      return NumberGuard.bool(LogicSplit.all(
         mc.options.jumpKey.isPressed(),
         mc.player.horizontalCollision,
         LogicSplit.not(mc.player.isOnGround()),
         LogicSplit.not(this.climbing)
      ));
   }

   private boolean shouldStartClimbingInternal() {
      return NumberGuard.unbool(shouldStartClimbingFlag());
   }

   private void startClimbingInternal() {
      FlowObfuscator.fakeHandler();
      this.climbing = true;
      this.switched = false;
      this.originalSlot = mc.player.getInventory().selectedSlot;
      this.waterSlot = ensureWaterBucketInHotbarInternal();
   }

   private void processClimbingInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (this.waterSlot == NumberGuard.i(-1)) {
                  stopClimbingInternal();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (LogicSplit.or(LogicSplit.not(mc.player.horizontalCollision), mc.player.isOnGround())) {
                  stopClimbingInternal();
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               executeClimbInternal();
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= waterSlot;
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

   private void executeClimbInternal() {
      FlowObfuscator.fakeHandler();
      Vec3d eyePos = mc.player.getCameraPosVec(NumberGuard.f(1.0F));
      Vec3d lookVec = mc.player.getRotationVec(NumberGuard.f(1.0F)).normalize();
      Vec3d targetVec = eyePos.add(lookVec);
      BlockPos targetBlock = new BlockPos((int)targetVec.x, (int)targetVec.y - NumberGuard.i(1), (int)targetVec.z);
      float neededYaw = getNeededYawInternal(targetBlock.toCenterPos());
      float neededPitch = getNeededPitchInternal(targetBlock.toCenterPos());
      switchToWaterInternal();
      useWaterBucketInternal(neededYaw, neededPitch);
      applyClimbVelocityInternal();
   }

   private void switchToWaterInternal() {
      FlowObfuscator.fakeHandler();
      if (this.switched) return;
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.waterSlot));
         this.switched = true;
      }
   }

   private void useWaterBucketInternal(float yaw, float pitch) {
      FlowObfuscator.fakeHandler();
      if (this.timerUtil.hasTimeElapsed(NumberGuard.i(20), true)) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, NumberGuard.i(0), yaw, pitch));
         }
      }
   }

   private void applyClimbVelocityInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.fallDistance = NumberGuard.f(0.0F);
         mc.player.setVelocity(NumberGuard.d(0.0), NumberGuard.d(0.3), NumberGuard.d(0.0));
      }
   }

   private void handleMatrixInternal() {
      FlowObfuscator.fakeHandler();
      boolean shouldClimb = LogicSplit.all(
         mc.options.jumpKey.isPressed(),
         mc.player.horizontalCollision
      );
      if (shouldClimb) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.player.setVelocity(mc.player.getVelocity().x, NumberGuard.d(0.42), mc.player.getVelocity().z);
            mc.player.fallDistance = NumberGuard.f(0.0F);
         }
      }
   }

   @Override
   public void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x6E40, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               super.onDisable();
               stopClimbingInternal();
               _s = 1;
               break;

            case 1:
               if (mc.player != null) {
                  if (FlowObfuscator.opaqueTrue()) {
                     mc.player.setVelocity(NumberGuard.d(0.0), NumberGuard.d(0.0), NumberGuard.d(0.0));
                  }
               }
               _s = 2;
               break;

            case 2:
               this.timerUtil.reset();
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private void stopClimbingInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F51, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!this.climbing) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               this.climbing = false;
               this.switched = false;
               restoreSlotInternal();
               restoreInventoryInternal();
               _s = 2;
               break;

            case 2:
               this.waterSlot = NumberGuard.i(-1);
               this.movedFromInvSlot = NumberGuard.i(-1);
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

   private void restoreSlotInternal() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.and(this.originalSlot != NumberGuard.i(-1), mc.player != null)) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.originalSlot));
            this.originalSlot = NumberGuard.i(-1);
         }
      }
   }

   private void restoreInventoryInternal() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.all(this.waterSlot != NumberGuard.i(-1), this.movedFromInvSlot != NumberGuard.i(-1), mc.player != null)) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, this.waterSlot, this.movedFromInvSlot, SlotActionType.SWAP, mc.player);
         }
      }
   }

   private int ensureWaterBucketInHotbarInternal() {
      FlowObfuscator.fakeHandler();
      int hotbarSlot = findWaterInHotbarInternal();
      if (hotbarSlot != NumberGuard.i(-1)) {
         this.movedFromInvSlot = NumberGuard.i(-1);
         return hotbarSlot;
      }
      return moveWaterFromInventoryInternal();
   }

   private int findWaterInHotbarInternal() {
      FlowObfuscator.fakeHandler();
      for (int i = 0; i < NumberGuard.i(9); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() == Items.WATER_BUCKET) {
            return i;
         }
      }
      return NumberGuard.i(-1);
   }

   private int moveWaterFromInventoryInternal() {
      FlowObfuscator.fakeHandler();
      for (int ix = NumberGuard.i(9); ix < NumberGuard.i(36); ix++) {
         ItemStack stack = mc.player.getInventory().getStack(ix);
         if (stack.getItem() == Items.WATER_BUCKET) {
            int freeHotbar = findFreeHotbarSlotInternal();
            if (freeHotbar == NumberGuard.i(-1)) {
               freeHotbar = NumberGuard.i(0);
            }
            if (FlowObfuscator.opaqueTrue()) {
               mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, ix, freeHotbar, SlotActionType.SWAP, mc.player);
            }
            this.movedFromInvSlot = ix;
            return freeHotbar;
         }
      }
      return NumberGuard.i(-1);
   }

   private int findFreeHotbarSlotInternal() {
      FlowObfuscator.fakeHandler();
      for (int i = 0; i < NumberGuard.i(9); i++) {
         if (mc.player.getInventory().getStack(i).isEmpty()) {
            return i;
         }
      }
      return NumberGuard.i(-1);
   }

   private float getNeededYawInternal(Vec3d target) {
      FlowObfuscator.fakeHandler();
      double dx = target.x - mc.player.getX();
      double dz = target.z - mc.player.getZ();
      return (float)(Math.toDegrees(Math.atan2(dz, dx)) - NumberGuard.d(90.0));
   }

   private float getNeededPitchInternal(Vec3d target) {
      FlowObfuscator.fakeHandler();
      double dx = target.x - mc.player.getX();
      double dy = target.y - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double dz = target.z - mc.player.getZ();
      double dist = Math.sqrt(dx * dx + dz * dz);
      return (float)(-Math.toDegrees(Math.atan2(dy, dist)));
   }

   private void fakeClimbModeInternal() {
      FlowObfuscator.fakeHandler();
      entropy ^= System.nanoTime();
      mc.player.setVelocity(FAKE_VELOCITY, FAKE_VELOCITY, FAKE_VELOCITY);
      SemanticNoise.deadCode2();
   }
}
