package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.input.EventKey;
import dev.just.events.impl.input.EventKeyBoard;
import dev.just.events.impl.move.EventMotion;
import dev.just.events.impl.world.EventObsidianPlace;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.move.MoveUtil;
import dev.just.protect.runtime.Strings;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext;
import org.joml.Vector2f;

@FunctionAnnotation(
   name = "AutoExplosion",
   type = Type.Combat,
   desc = "T2JzaWR5ZW4ga295dWxkdcSfdW5kYSBvdG9tYXRpayBvbGFyYWsga3Jpc3RhbCBwYXRsYXTEsXI="
)
public class AutoExplosion extends Function {
   private final BooleanSetting correction = new BooleanSetting(Strings.b("SGFyZWtldCBEw7x6ZWx0bWU="), true);
   private final SliderSetting delay = new SliderSetting(Strings.b("R2VjaWttZQ=="), 100.0, 50.0, 300.0, 1.0);
   private final BooleanSetting sanya = new BooleanSetting(Strings.b("VHXFnyBBdGFtYXPEsQ=="), false);
   private final BindSetting bind = new BindSetting(Strings.b("VHXFnw=="), 0, () -> this.sanya.get());
   private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
   private BlockPos crystalPos = null;
   private Entity crystalEntity = null;
   private int previousSlot = -1;
   public Vector2f serverRot = null;
   private boolean rotating = false;

   public AutoExplosion() {
      this.addSettings(new Setting[]{this.correction, this.delay, this.sanya, this.bind});
   }

   public boolean check() {
      return this.correction.get() && this.crystalEntity != null && this.crystalPos != null && this.serverRot != null && this.state;
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventKey key && this.sanya.get() && key.key == this.bind.getKey()) {
         this.schedulePlaceSequence();
      }

      if (event instanceof EventKeyBoard input && this.shouldCorrect()) {
         MoveUtil.fixMovement(input, this.serverRot.x);
      }

      if (event instanceof EventMotion motion) {
         this.handleRotation(motion);
      }

      if (event instanceof EventObsidianPlace place) {
         this.scheduleCrystalPlace(place.getPos());
      }

      if (event instanceof EventUpdate) {
         this.updateLogic();
      }
   }

   private void schedulePlaceSequence() {
      BlockPos pos = this.getLookingBlockPos();
      if (pos != null) {
         int obsidianSlot = this.findObsidianSlot();
         if (obsidianSlot != -1) {
            this.previousSlot = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = obsidianSlot;
            this.scheduler.schedule(() -> mc.execute(() -> {
                  this.placeBlock(pos);
                  mc.player.getInventory().selectedSlot = this.previousSlot;
                  this.scheduleCrystalPlace(pos);
               }), 50L, TimeUnit.MILLISECONDS);
         }
      }
   }

   private void placeBlock(BlockPos pos) {
      BlockHitResult bhr = new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false);
      ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
      if (result == ActionResult.SUCCESS) {
         mc.player.swingHand(Hand.MAIN_HAND);
      }
   }

   private void scheduleCrystalPlace(BlockPos pos) {
      int crystalSlot = this.findCrystalSlot();
      if (crystalSlot != -1 && this.canPlaceCrystal(pos)) {
         this.scheduler.schedule(() -> mc.execute(() -> {
               this.previousSlot = mc.player.getInventory().selectedSlot;
               mc.player.getInventory().selectedSlot = crystalSlot;
               BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false);
               ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
               if (result == ActionResult.SUCCESS) {
                  mc.player.swingHand(Hand.MAIN_HAND);
                  this.crystalPos = pos;
               }

               mc.player.getInventory().selectedSlot = this.previousSlot;
            }), this.delay.get().longValue(), TimeUnit.MILLISECONDS);
      }
   }

   private boolean canPlaceCrystal(BlockPos pos) {
      BlockPos above = pos.up();
      return mc.world.isAir(above);
   }

   private void updateLogic() {
      if (this.crystalPos != null) {
         if (mc.player.getPos().distanceTo(Vec3d.ofCenter(this.crystalPos)) > 6.0) {
            this.reset();
         } else {
            List<Entity> crystals = mc.world
               .getOtherEntities(null, new Box(this.crystalPos).expand(1.0))
               .stream()
               .filter(e -> e instanceof EndCrystalEntity)
               .toList();
            if (!crystals.isEmpty()) {
               this.crystalEntity = crystals.get(0);
               this.tryAttack(this.crystalEntity);
            }
         }
      }
   }

   private void handleRotation(EventMotion event) {
      if (this.crystalEntity != null) {
         Vector2f targetRot = rotationToEntity(this.crystalEntity);
         if (this.serverRot == null) {
            this.serverRot = targetRot;
         }

         this.serverRot.x = this.serverRot.x + this.clampRotation(targetRot.x - this.serverRot.x, 10.0F);
         this.serverRot.y = this.serverRot.y + this.clampRotation(targetRot.y - this.serverRot.y, 10.0F);
         event.setYaw(this.serverRot.x);
         event.setPitch(this.serverRot.y);
      }
   }

   private float clampRotation(float value, float maxStep) {
      if (value > maxStep) {
         return maxStep;
      } else {
         return value < -maxStep ? -maxStep : value;
      }
   }

   private void tryAttack(Entity entity) {
      if (entity != null && !(mc.player.getAttackCooldownProgress(0.0F) < 1.0F)) {
         mc.interactionManager.attackEntity(mc.player, entity);
         mc.player.swingHand(Hand.MAIN_HAND);
         this.reset();
      }
   }

   private int findCrystalSlot() {
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getStack(i).isOf(Items.END_CRYSTAL)) {
            return i;
         }
      }

      return -1;
   }

   private int findObsidianSlot() {
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getStack(i).isOf(Items.OBSIDIAN)) {
            return i;
         }
      }

      return -1;
   }

   private BlockPos getLookingBlockPos() {
      Vec3d eyes = mc.player.getCameraPosVec(1.0F);
      Vec3d look = mc.player.getRotationVec(1.0F).multiply(4.0);
      BlockHitResult bhr = mc.world.raycast(new RaycastContext(eyes, eyes.add(look), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
      return bhr != null && bhr.getBlockPos() != null ? bhr.getBlockPos() : null;
   }

   private boolean shouldCorrect() {
      return this.correction.get() && this.crystalEntity != null && this.crystalPos != null && this.serverRot != null && this.state;
   }

   private void reset() {
      this.crystalEntity = null;
      this.crystalPos = null;
      this.serverRot = null;
      this.previousSlot = -1;
   }

   public static Vector2f rotationToEntity(Entity entity) {
      Vec3d diff = entity.getPos().subtract(mc.player.getPos());
      double flatDist = Math.hypot(diff.x, diff.z);
      float yaw = (float)Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diff.y, flatDist)));
      return new Vector2f(yaw, pitch);
   }

   @Override
   protected void onDisable() {
      this.reset();
      super.onDisable();
   }
}
