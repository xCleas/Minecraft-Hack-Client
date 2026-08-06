package dev.just.util.math;

import dev.just.manager.IMinecraft;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext;

public class RayTraceUtil implements IMinecraft {
   private static final Map<UUID, Long> lastHitTimes = new HashMap<>();
   private static final long EFFECT_DURATION = 200L;

   public static void markHit(Entity entity) {
      lastHitTimes.put(entity.getUuid(), System.currentTimeMillis());
   }

   public static float getHitProgress(Entity entity) {
      Long hitTime = lastHitTimes.get(entity.getUuid());
      if (hitTime == null) {
         return 0.0F;
      } else {
         long elapsed = System.currentTimeMillis() - hitTime;
         if (elapsed > 200L) {
            lastHitTimes.remove(entity.getUuid());
            return 0.0F;
         } else {
            return 1.0F - (float)elapsed / 200.0F;
         }
      }
   }

   private static Vec3d getVector(float pitch, float yaw) {
      float yawRad = (float)Math.toRadians((double)yaw);
      float pitchRad = (float)Math.toRadians((double)pitch);
      float cosPitch = (float)Math.cos((double)(-pitchRad));
      return new Vec3d(-Math.sin((double)yawRad) * (double)cosPitch, -Math.sin((double)pitchRad), Math.cos((double)yawRad) * (double)cosPitch);
   }

   public static Entity getMouseOver(Entity target, float yaw, float pitch, double distance) {
      Entity entity = mc.getCameraEntity();
      if (entity != null && mc.world != null && target != null) {
         Box playerBox = entity.getBoundingBox();
         Box targetBox = target.getBoundingBox();
         Vec3d startVec = entity.getEyePos();
         Vec3d directionVec = getVector(pitch, yaw);
         Vec3d endVec = startVec.add(directionVec.x * distance, directionVec.y * distance, directionVec.z * distance);
         if (playerBox.intersects(targetBox)) {
            EntityHitResult hitResult = rayCastEntity(distance, yaw, pitch, e -> e == target && !e.isSpectator() && e.canBeHitByProjectile());
            if (hitResult != null && hitResult.getEntity() == target) {
               return target;
            }
         }

         EntityHitResult entityHitResult = rayCastEntities(entity, startVec, endVec, targetBox, e -> e == target && !e.isSpectator() && e.canBeHitByProjectile(), distance);
         return entityHitResult != null && startVec.distanceTo(entityHitResult.getPos()) <= distance ? entityHitResult.getEntity() : null;
      } else {
         return null;
      }
   }

   public static EntityHitResult rayCastEntity(double range, float yaw, float pitch, Predicate<Entity> filter) {
      Entity entity = mc.getCameraEntity();
      if (entity != null && mc.world != null) {
         Vec3d cameraVec = entity.getCameraPosVec(1.0F);
         float pitchRad = pitch * (float) (Math.PI / 180.0);
         float yawRad = -yaw * (float) (Math.PI / 180.0);
         float cosPitch = (float)Math.cos((double)pitchRad);
         float sinPitch = (float)Math.sin((double)pitchRad);
         float cosYaw = (float)Math.cos((double)yawRad);
         float sinYaw = (float)Math.sin((double)yawRad);
         Vec3d rotationVec = new Vec3d((double)(sinYaw * cosPitch), (double)(-sinPitch), (double)(cosYaw * cosPitch));
         Vec3d end = cameraVec.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range);
         Box box = entity.getBoundingBox().stretch(rotationVec.multiply(range)).expand(1.0, 1.0, 1.0);
         return ProjectileUtil.raycast(entity, cameraVec, end, box, filter, range * range);
      } else {
         return null;
      }
   }

   private static EntityHitResult rayCastEntities(
      Entity source, Vec3d start, Vec3d end, Box boundingBox, Predicate<Entity> predicate, double maxDistance
   ) {
      World world = source.getWorld();
      double closestDistance = maxDistance;
      Entity closestEntity = null;
      Vec3d closestHitPos = null;

      for (Entity entity : world.getEntitiesByClass(Entity.class, boundingBox, predicate)) {
         if (entity != source) {
            Box entityBox = entity.getBoundingBox();
            Optional<Vec3d> hit = entityBox.raycast(start, end);
            if (hit.isPresent()) {
               Vec3d hitPos = hit.get();
               double distance = start.distanceTo(hitPos);
               if (distance < closestDistance) {
                  closestEntity = entity;
                  closestHitPos = hitPos;
                  closestDistance = distance;
               }
            }
         }
      }

      return closestEntity != null ? new EntityHitResult(closestEntity, closestHitPos) : null;
   }

   public static BlockHitResult rayCast(double range, float yaw, float pitch, boolean includeFluids) {
      Entity entity = mc.getCameraEntity();
      if (entity != null && mc.world != null) {
         Vec3d start = entity.getCameraPosVec(1.0F);
         float pitchRad = pitch * (float) (Math.PI / 180.0);
         float yawRad = -yaw * (float) (Math.PI / 180.0);
         float cosPitch = (float)Math.cos((double)pitchRad);
         float sinPitch = (float)Math.sin((double)pitchRad);
         float cosYaw = (float)Math.cos((double)yawRad);
         float sinYaw = (float)Math.sin((double)yawRad);
         Vec3d rotationVec = new Vec3d((double)(sinYaw * cosPitch), (double)(-sinPitch), (double)(cosYaw * cosPitch));
         Vec3d end = start.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range);
         World world = mc.world;
         RaycastContext.FluidHandling fluidHandling = includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE;
         RaycastContext context = new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, fluidHandling, entity);
         return world.raycast(context);
      } else {
         return null;
      }
   }
}
