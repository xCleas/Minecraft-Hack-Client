package dev.just.util.vector;

import dev.just.util.math.MathUtil;
import net.minecraft.entity.Entity;
import org.joml.Vector3d;

public class EntityPosition extends Vector3d {
   protected EntityPosition(Entity entity, float height, float pt) {
      super(
         MathUtil.interpolate(entity.lastRenderX, entity.getX(), (double)pt),
         MathUtil.interpolate(entity.lastRenderY, entity.getY(), (double)pt) + (double)height,
         MathUtil.interpolate(entity.lastRenderZ, entity.getZ(), (double)pt)
      );
   }

   public static Vector3d get(Entity entity, float height, float pt) {
      return new EntityPosition(entity, height, pt);
   }

   public static Vector3d get(Entity entity, float pt) {
      return get(entity, 0.0F, pt);
   }
}
