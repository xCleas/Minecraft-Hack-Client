package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.player.EventAttack;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.modules.render.littlePet.GhostWolfEntity;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "LittlePet",
   desc = "U2l6aSB0YWtpcCBlZGVuIGvDvMOnw7xrIGJpciBkb3N0IGVrbGVy",
   type = Type.Render
)
public class LittleSnickers extends Function {
   private LivingEntity fakeEntity = null;
   private boolean active = false;
   private World lastWorld = null;
   private final BooleanSetting followTarget = new BooleanSetting(Strings.b("SGVkZWZlIFNhbGTEsXI="), false);
   private int ticksAway = 0;
   private int stuckTicks = 0;
   private LivingEntity currentTarget = null;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();
   private final EndTick tickListener = client -> {
      if (this.active && mc.player != null && mc.world != null) {
         if (this.fakeEntity != null && mc.world == this.lastWorld) {
            double distToPlayer = mc.player.getPos().distanceTo(this.fakeEntity.getPos());
            if (distToPlayer > 10.0 || this.stuckTicks > 40) {
               this.ticksAway++;
               if (this.ticksAway >= 60 || this.stuckTicks > 40) {
                  this.fakeEntity.refreshPositionAndAngles(mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0.0F, 0.0F);
                  this.ticksAway = 0;
                  this.stuckTicks = 0;
               }
            }

            Vec3d moveTo = mc.player.getPos();
            Entity lookTarget = mc.player;
            if (this.followTarget.get() && this.currentTarget != null) {
               if (this.currentTarget.getPos().distanceTo(this.fakeEntity.getPos()) <= 12.0 && !this.currentTarget.isDead()) {
                  moveTo = this.currentTarget.getPos();
                  lookTarget = this.currentTarget;
               } else {
                  this.currentTarget = null;
               }
            }

            this.moveEntityTowardsTarget(moveTo);
            this.fakeEntity.tickMovement();
            this.updateEntityRotation(lookTarget);
         } else {
            if (this.fakeEntity != null) {
               mc.world.removeEntity(this.fakeEntity.getId(), Entity.RemovalReason.DISCARDED);
            }

            this.fakeEntity = this.createEntity();
            if (this.fakeEntity != null) {
               mc.world.addEntity(this.fakeEntity);
               this.lastWorld = mc.world;
            }
         }
      }
   };

   public LittleSnickers() {
      this.addSettings(new Setting[]{this.followTarget});
   }

   private LivingEntity createEntity() {
      GhostWolfEntity entity = new GhostWolfEntity(EntityType.WOLF, mc.world);
      if (entity == null) {
         return null;
      } else {
         entity.refreshPositionAndAngles(
            mc.player.getX(),
            mc.player.getY(),
            mc.player.getZ(),
            mc.player.getYaw(),
            mc.player.getPitch()
         );
         entity.setInvisible(false);
         entity.setCustomNameVisible(false);
         entity.setHealth(20.0F);
         if (entity instanceof TameableEntity) {
            entity.setTamed(true, false);
            entity.setOwnerUuid(mc.player.getUuid());
         }

         return entity;
      }
   }

   private boolean isSolid(BlockPos pos) {
      BlockState blockState = mc.world.getBlockState(pos);
      return !blockState.isAir() && !blockState.getCollisionShape(mc.world, pos).isEmpty();
   }

   private void moveEntityTowardsTarget(Vec3d targetPos) {
      Vec3d entityPos = this.fakeEntity.getPos();
      double dx = targetPos.x - entityPos.x;
      double dz = targetPos.z - entityPos.z;
      double distanceXZ = Math.sqrt(dx * dx + dz * dz);
      double dy = targetPos.y - entityPos.y;
      boolean isMoving = false;
      boolean isJumping = false;
      BlockPos entityBlockPos = BlockPos.ofFloored(entityPos.x, entityPos.y - 0.1, entityPos.z);
      boolean onGround = this.isSolid(entityBlockPos);
      if (!(Math.abs(dy) >= 12.0) && !(distanceXZ > 20.0)) {
         if (distanceXZ > 1.2) {
            double speed = 0.21;
            double normX = dx / distanceXZ;
            double normZ = dz / distanceXZ;
            BlockPos frontPos = BlockPos.ofFloored(entityPos.x + normX * 0.6, entityPos.y, entityPos.z + normZ * 0.6);
            if (this.isSolid(frontPos) && !this.isSolid(frontPos.up()) && onGround) {
               Vec3d vel = this.fakeEntity.getVelocity();
               this.fakeEntity.setVelocity(vel.x, 0.5, vel.z);
               isJumping = true;
               onGround = false;
            }

            if (!isJumping && onGround) {
               this.fakeEntity.move(MovementType.SELF, new Vec3d(normX * speed, 0.0, normZ * speed));
               isMoving = true;
               this.stuckTicks = 0;
            }

            if (isMoving) {
               float yaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
               this.fakeEntity.setYaw(yaw);
               this.fakeEntity.setBodyYaw(yaw);
               if (this.fakeEntity instanceof MobEntity mob) {
                  mob.setHeadYaw(yaw);
               }
            }
         }

         if (!onGround) {
            Vec3d velocity = this.fakeEntity.getVelocity();
            this.fakeEntity.setVelocity(velocity.x * 0.91, Math.max(velocity.y - 0.08, -0.5), velocity.z * 0.91);
            this.fakeEntity.move(MovementType.SELF, this.fakeEntity.getVelocity());
         }

         if (this.fakeEntity instanceof WolfEntity wolf) {
            wolf.setSprinting(isMoving && !isJumping);
            wolf.setSitting(!isMoving && onGround);
         }
      } else {
         this.fakeEntity
            .refreshPositionAndAngles(targetPos.x, targetPos.y, targetPos.z, this.fakeEntity.getYaw(), this.fakeEntity.getPitch());
         this.fakeEntity.setVelocity(Vec3d.ZERO);
         this.stuckTicks = 0;
      }
   }

   private void updateEntityRotation(Entity target) {
      if (target != null) {
         Vec3d diff = target.getEyePos().subtract(this.fakeEntity.getEyePos());
         double flatDist = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
         float yaw = (float)Math.toDegrees(Math.atan2(-diff.x, diff.z));
         float pitch = (float)Math.toDegrees(-Math.atan2(diff.y, flatDist));
         this.fakeEntity.setYaw(yaw);
         this.fakeEntity.setPitch(pitch);
         this.fakeEntity.setBodyYaw(yaw);
         if (this.fakeEntity instanceof MobEntity mob) {
            mob.setHeadYaw(yaw);
         }
      }
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueTrue() && event instanceof EventAttack attack && this.followTarget.get() && attack.getTarget() instanceof LivingEntity living && living != mc.player) {
                  this.currentTarget = living;
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
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

   @Override
   public void onEnable() {
      ClientTickEvents.END_CLIENT_TICK.register(this.tickListener);
      this.active = true;
      this.lastWorld = mc.world;
      if (mc.player != null && mc.world != null) {
         this.fakeEntity = this.createEntity();
         if (this.fakeEntity != null) {
            mc.world.addEntity(this.fakeEntity);
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.active) {
         if (mc.world != null && this.fakeEntity != null) {
            mc.world.removeEntity(this.fakeEntity.getId(), Entity.RemovalReason.DISCARDED);
            this.fakeEntity = null;
         }

         this.active = false;
         this.currentTarget = null;
      }
   }
}
