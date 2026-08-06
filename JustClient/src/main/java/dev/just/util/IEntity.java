package dev.just.util;

import dev.just.modules.render.Trails;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public interface IEntity {
   List<Trails.Trail> justClientFabric1_21_4$getTrails();

   Vec3d justClientFabric1_21_4$getLastTrailPos();

   void justClientFabric1_21_4$setLastTrailPos(Vec3d var1);
}
