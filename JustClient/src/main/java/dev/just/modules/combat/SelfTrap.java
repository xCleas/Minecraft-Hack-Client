package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.SemanticNoise;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.hit.BlockHitResult;
import org.joml.Vector2f;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "SelfTrap",
   keywords = {"Trap"},
   type = Type.Combat,
   desc = "RXRyYWbEsW7EsXrEsSBvdG9tYXRpayBvbGFyYWsgYmxva2xhcmxhIGthcGF0xLFy"
)
public class SelfTrap extends Function {
   public final Vector2f rotate = new Vector2f(0.0F, 0.0F);
   private final BlockPos[] baseOffsets = new BlockPos[]{
      BlockPos.ORIGIN.north(),
      BlockPos.ORIGIN.south(),
      BlockPos.ORIGIN.east(),
      BlockPos.ORIGIN.west()
   };
   private List<BlockPos> targets = new ArrayList<>();
   private int index = 0;
   private int originalSlot = -1;
   public boolean active = false;
   private int tickDelay = 0;
   private final Random rnd = new Random();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   @Override
   public void onEnable() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(mc.player != null, mc.world != null)) {
         this.targets.clear();
         BlockPos base = mc.player.getBlockPos();

         for (BlockPos off : this.baseOffsets) {
            this.targets.add(base.add(off));
         }

         this.originalSlot = mc.player.getInventory().selectedSlot;
         this.index = lO1I0l1O.i(0);
         this.active = true;
         this.tickDelay = lO1I0l1O.i(0);
      } else {
         this.toggle();
      }
   }

   @Override
   public void onDisable() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(mc.player != null, this.originalSlot != lO1I0l1O.i(-1))) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.getInventory().selectedSlot = this.originalSlot;
         }
      }

      this.targets.clear();
      this.index = lO1I0l1O.i(0);
      this.active = false;
      this.tickDelay = lO1I0l1O.i(0);
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
               if (!this.active) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventUpdate) {
                  handleUpdateEventInternal();
               }
               _s = 3;
               break;

            case 3:
               if (event instanceof EventMotion motion) {
                  motion.setYaw(this.rotate.x);
                  motion.setPitch(this.rotate.y);
               }
               _s = 5;
               break;

            case 4:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
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

   private void handleUpdateEventInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (this.tickDelay > lO1I0l1O.i(0)) {
                  this.tickDelay--;
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (this.index >= this.targets.size()) {
                  this.finish();
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               BlockPos target = this.targets.get(this.index);
               if (I1lO0l1I.or(!mc.world.getBlockState(target).isAir(), this.isEntityBlocking(target))) {
                  this.index++;
                  _s = 5;
                  break;
               }
               _s = 3;
               break;

            case 3:
               processBlockPlacementInternal();
               _s = 5;
               break;

            case 4:
               if (l1O0I1lO.opaqueFalse()) {
                  SemanticNoise.deadCode1();
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

   private void processBlockPlacementInternal() {
      l1O0I1lO.fakeHandler();
      BlockPos target = this.targets.get(this.index);
      Direction placeSide = this.findPlaceableSide(target);

      if (placeSide != null) {
         BlockPos neighbor = target.offset(placeSide);
         Vec3d hitVec = Vec3d.ofCenter(neighbor).add(Vec3d.of(placeSide.getVector()).multiply(lO1I0l1O.d(0.5)));
         int blockSlot = this.findAnyBlockInHotbar();

         if (blockSlot == lO1I0l1O.i(-1)) {
            this.finish();
            return;
         }

         if (l1O0I1lO.opaqueTrue()) {
            mc.player.getInventory().selectedSlot = blockSlot;
            BlockHitResult hit = new BlockHitResult(hitVec, placeSide.getOpposite(), neighbor, false);
            this.rotateTo(hit.getBlockPos());
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            mc.player.swingHand(Hand.MAIN_HAND);
         }

         this.tickDelay = lO1I0l1O.i(1) + this.rnd.nextInt(lO1I0l1O.i(3));
         this.index++;
         return;
      }

      this.index++;
   }

   private void finish() {
      this.active = false;
      if (mc.player != null && this.originalSlot != -1) {
         mc.player.getInventory().selectedSlot = this.originalSlot;
      }
   }

   private int findAnyBlockInHotbar() {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem)stack.getItem()).getBlock();
            if (block != Blocks.AIR) {
               return i;
            }
         }
      }

      return -1;
   }

   private boolean isEntityBlocking(BlockPos pos) {
      Box box = new Box(pos);

      for (Entity e : mc.world.getOtherEntities(null, box)) {
         if (e != mc.player) {
            return true;
         }
      }

      return false;
   }

   private Direction findPlaceableSide(BlockPos pos) {
      for (Direction dir : Direction.values()) {
         BlockPos neighbor = pos.offset(dir);
         if (!mc.world.getBlockState(neighbor).isAir() && mc.world.getBlockState(neighbor).isSolidBlock(mc.world, neighbor)) {
            return dir;
         }
      }

      return null;
   }

   private void rotateTo(BlockPos pos) {
      if (mc.player != null) {
         double dx = (double)pos.getX() + 0.5 - mc.player.getX();
         double dy = (double)pos.getY() + 0.5 - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
         double dz = (double)pos.getZ() + 0.5 - mc.player.getZ();
         double distXZ = Math.sqrt(dx * dx + dz * dz);
         float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
         float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distXZ)));
         this.rotate.x = yaw;
         this.rotate.y = pitch;
      }
   }
}
