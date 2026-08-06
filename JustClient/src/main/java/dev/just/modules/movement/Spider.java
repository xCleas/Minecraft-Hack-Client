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
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.util.Hand;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Spider",
   desc = "WsSxcGxhZMSxa3RhbiBzb25yYSBvdG9tYXRpayBvbGFyYWsgc3Uga295YXIgdmUgdMSxcm1hbm1hbsSxesSxIHNhxJ9sYXI=",
   type = Type.Move
)
public class Spider extends Function {
   public final ModeSetting mode = new ModeSetting(I0O1l0I1.b("VMO8cg=="), "RwSu", "RwSu", "Matrix");
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
      int _s = O1lI0O1l.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakeClimbModeInternal();
                  _s = 6;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 7);
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entropy, _s);
               _s = 6;
               break;

            case 5:
               l1O0I1lO.fakeHandler();
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
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(I1lO0l1I.all(mc.player != null, mc.world != null));
   }

   private boolean validatePlayerInternal() {
      return lO1I0l1O.unbool(validatePlayerFlag());
   }

   private void dispatchEventInternal(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(event, entropy);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!(eventPacket.getPacket() instanceof GameMessageS2CPacket packet)) {
                  _s = 4;
                  break;
               }
               String message = packet.content().getString();
               if (l1O0I1lO.opaqueFalse()) {
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
                  if (l1O0I1lO.opaqueTrue()) {
                     eventPacket.setCancel(true);
                  }
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(eventPacket, entropy);
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

   private int shouldCancelMessageFlag(String message) {
      l1O0I1lO.fakeHandler();
      return lO1I0l1O.bool(I1lO0l1I.or(
         message.contains("Uzgunum, buraya blok koyamazsiniz."),
         message.contains(I0O1l0I1.b("w5x6Z8O8bsO8bSwgYnVyYXlhIGJsb2sga295YW1henPEsW7EsXou")),
         message.contains("Sorry, you cannot place a block here")
      ));
   }

   private boolean shouldCancelMessageInternal(String message) {
      return lO1I0l1O.unbool(shouldCancelMessageFlag(message));
   }

   private void handleUpdateInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (this.mode.is("RwSu")) {
                  handleRwWaterInternal();
               } else if (this.mode.is("Matrix")) {
                  handleMatrixInternal();
               }
               _s = 4;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
               }
               _s = 4;
               break;

            case 2:
               l1O0I1lO.fakeBranch(climbing, entropy);
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

   private void handleMotionInternal() {
      l1O0I1lO.fakeHandler();
      if (!this.climbing) return;
      if (!this.mode.is("RwSu")) return;
      applyRotationInternal();
   }

   private void applyRotationInternal() {
      l1O0I1lO.fakeHandler();
      Vec3d eyePos = mc.player.getCameraPosVec(lO1I0l1O.f(1.0F));
      Vec3d lookVec = mc.player.getRotationVec(lO1I0l1O.f(1.0F)).normalize();
      Vec3d targetVec = eyePos.add(lookVec);
      BlockPos targetBlock = new BlockPos((int)targetVec.x, (int)targetVec.y - lO1I0l1O.i(1), (int)targetVec.z);
      float neededYaw = getNeededYawInternal(targetBlock.toCenterPos());
      float neededPitch = getNeededPitchInternal(targetBlock.toCenterPos());
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.setPitch(neededPitch);
         mc.player.setYaw(neededYaw);
      }
   }

   private void handleRwWaterInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(climbing, entropy);
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

   private int shouldStartClimbingFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(I1lO0l1I.all(
         mc.options.jumpKey.isPressed(),
         mc.player.horizontalCollision,
         I1lO0l1I.not(mc.player.isOnGround()),
         I1lO0l1I.not(this.climbing)
      ));
   }

   private boolean shouldStartClimbingInternal() {
      return lO1I0l1O.unbool(shouldStartClimbingFlag());
   }

   private void startClimbingInternal() {
      l1O0I1lO.fakeHandler();
      this.climbing = true;
      this.switched = false;
      this.originalSlot = mc.player.getInventory().selectedSlot;
      this.waterSlot = ensureWaterBucketInHotbarInternal();
   }

   private void processClimbingInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (this.waterSlot == lO1I0l1O.i(-1)) {
                  stopClimbingInternal();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.or(I1lO0l1I.not(mc.player.horizontalCollision), mc.player.isOnGround())) {
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
               if (l1O0I1lO.opaqueFalse()) {
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
      l1O0I1lO.fakeHandler();
      Vec3d eyePos = mc.player.getCameraPosVec(lO1I0l1O.f(1.0F));
      Vec3d lookVec = mc.player.getRotationVec(lO1I0l1O.f(1.0F)).normalize();
      Vec3d targetVec = eyePos.add(lookVec);
      BlockPos targetBlock = new BlockPos((int)targetVec.x, (int)targetVec.y - lO1I0l1O.i(1), (int)targetVec.z);
      float neededYaw = getNeededYawInternal(targetBlock.toCenterPos());
      float neededPitch = getNeededPitchInternal(targetBlock.toCenterPos());
      switchToWaterInternal();
      useWaterBucketInternal(neededYaw, neededPitch);
      applyClimbVelocityInternal();
   }

   private void switchToWaterInternal() {
      l1O0I1lO.fakeHandler();
      if (this.switched) return;
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.waterSlot));
         this.switched = true;
      }
   }

   private void useWaterBucketInternal(float yaw, float pitch) {
      l1O0I1lO.fakeHandler();
      if (this.timerUtil.hasTimeElapsed(lO1I0l1O.i(20), true)) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, lO1I0l1O.i(0), yaw, pitch));
         }
      }
   }

   private void applyClimbVelocityInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.fallDistance = lO1I0l1O.f(0.0F);
         mc.player.setVelocity(lO1I0l1O.d(0.0), lO1I0l1O.d(0.3), lO1I0l1O.d(0.0));
      }
   }

   private void handleMatrixInternal() {
      l1O0I1lO.fakeHandler();
      boolean shouldClimb = I1lO0l1I.all(
         mc.options.jumpKey.isPressed(),
         mc.player.horizontalCollision
      );
      if (shouldClimb) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.setVelocity(mc.player.getVelocity().x, lO1I0l1O.d(0.42), mc.player.getVelocity().z);
            mc.player.fallDistance = lO1I0l1O.f(0.0F);
         }
      }
   }

   @Override
   public void onDisable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x6E40, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               super.onDisable();
               stopClimbingInternal();
               _s = 1;
               break;

            case 1:
               if (mc.player != null) {
                  if (l1O0I1lO.opaqueTrue()) {
                     mc.player.setVelocity(lO1I0l1O.d(0.0), lO1I0l1O.d(0.0), lO1I0l1O.d(0.0));
                  }
               }
               _s = 2;
               break;

            case 2:
               this.timerUtil.reset();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F51, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               this.waterSlot = lO1I0l1O.i(-1);
               this.movedFromInvSlot = lO1I0l1O.i(-1);
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

   private void restoreSlotInternal() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(this.originalSlot != lO1I0l1O.i(-1), mc.player != null)) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.originalSlot));
            this.originalSlot = lO1I0l1O.i(-1);
         }
      }
   }

   private void restoreInventoryInternal() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.all(this.waterSlot != lO1I0l1O.i(-1), this.movedFromInvSlot != lO1I0l1O.i(-1), mc.player != null)) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, this.waterSlot, this.movedFromInvSlot, SlotActionType.SWAP, mc.player);
         }
      }
   }

   private int ensureWaterBucketInHotbarInternal() {
      l1O0I1lO.fakeHandler();
      int hotbarSlot = findWaterInHotbarInternal();
      if (hotbarSlot != lO1I0l1O.i(-1)) {
         this.movedFromInvSlot = lO1I0l1O.i(-1);
         return hotbarSlot;
      }
      return moveWaterFromInventoryInternal();
   }

   private int findWaterInHotbarInternal() {
      l1O0I1lO.fakeHandler();
      for (int i = 0; i < lO1I0l1O.i(9); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() == Items.WATER_BUCKET) {
            return i;
         }
      }
      return lO1I0l1O.i(-1);
   }

   private int moveWaterFromInventoryInternal() {
      l1O0I1lO.fakeHandler();
      for (int ix = lO1I0l1O.i(9); ix < lO1I0l1O.i(36); ix++) {
         ItemStack stack = mc.player.getInventory().getStack(ix);
         if (stack.getItem() == Items.WATER_BUCKET) {
            int freeHotbar = findFreeHotbarSlotInternal();
            if (freeHotbar == lO1I0l1O.i(-1)) {
               freeHotbar = lO1I0l1O.i(0);
            }
            if (l1O0I1lO.opaqueTrue()) {
               mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, ix, freeHotbar, SlotActionType.SWAP, mc.player);
            }
            this.movedFromInvSlot = ix;
            return freeHotbar;
         }
      }
      return lO1I0l1O.i(-1);
   }

   private int findFreeHotbarSlotInternal() {
      l1O0I1lO.fakeHandler();
      for (int i = 0; i < lO1I0l1O.i(9); i++) {
         if (mc.player.getInventory().getStack(i).isEmpty()) {
            return i;
         }
      }
      return lO1I0l1O.i(-1);
   }

   private float getNeededYawInternal(Vec3d target) {
      l1O0I1lO.fakeHandler();
      double dx = target.x - mc.player.getX();
      double dz = target.z - mc.player.getZ();
      return (float)(Math.toDegrees(Math.atan2(dz, dx)) - lO1I0l1O.d(90.0));
   }

   private float getNeededPitchInternal(Vec3d target) {
      l1O0I1lO.fakeHandler();
      double dx = target.x - mc.player.getX();
      double dy = target.y - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double dz = target.z - mc.player.getZ();
      double dist = Math.sqrt(dx * dx + dz * dz);
      return (float)(-Math.toDegrees(Math.atan2(dy, dist)));
   }

   private void fakeClimbModeInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= System.nanoTime();
      mc.player.setVelocity(FAKE_VELOCITY, FAKE_VELOCITY, FAKE_VELOCITY);
      SemanticNoise.deadCode2();
   }
}
