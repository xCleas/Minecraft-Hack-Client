package dev.just.modules.render.littlePet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;

public class GhostWolfEntity extends WolfEntity {
   public GhostWolfEntity(EntityType<? extends WolfEntity> entityType, World world) {
      super(entityType, world);
   }

   protected EntityDimensions getBaseDimensions(EntityPose pose) {
      return EntityDimensions.fixed(0.0F, 0.0F);
   }

   public boolean isAttackable() {
      return false;
   }

   public boolean canHit() {
      return false;
   }
}
