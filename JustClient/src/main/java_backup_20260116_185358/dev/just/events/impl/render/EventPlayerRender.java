package dev.just.events.impl.render;

import dev.just.events.Event;
import net.minecraft.entity.LivingEntity;

public class EventPlayerRender extends Event {
   private final LivingEntity livingEntity;
   private float prevYaw;
   private float yaw;
   private float prevPitch;
   private float pitch;
   private float prevBodyYaw;
   private float bodyYaw;

   public EventPlayerRender(LivingEntity entity) {
      this.livingEntity = entity;
      this.yaw = entity.headYaw;
      this.prevYaw = entity.prevHeadYaw;
      this.pitch = entity.getPitch();
      this.prevPitch = entity.prevPitch;
      this.bodyYaw = entity.bodyYaw;
      this.prevBodyYaw = entity.prevBodyYaw;
   }

   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EventPlayerRender other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else if (Float.compare(this.getPrevYaw(), other.getPrevYaw()) != 0) {
         return false;
      } else if (Float.compare(this.getYaw(), other.getYaw()) != 0) {
         return false;
      } else if (Float.compare(this.getPrevPitch(), other.getPrevPitch()) != 0) {
         return false;
      } else if (Float.compare(this.getPitch(), other.getPitch()) != 0) {
         return false;
      } else if (Float.compare(this.getPrevBodyYaw(), other.getPrevBodyYaw()) != 0) {
         return false;
      } else if (Float.compare(this.getBodyYaw(), other.getBodyYaw()) != 0) {
         return false;
      } else {
         Object this$livingEntity = this.getLivingEntity();
         Object other$livingEntity = other.getLivingEntity();
         return this$livingEntity == null ? other$livingEntity == null : this$livingEntity.equals(other$livingEntity);
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof EventPlayerRender;
   }

   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      result = result * 59 + Float.floatToIntBits(this.getPrevYaw());
      result = result * 59 + Float.floatToIntBits(this.getYaw());
      result = result * 59 + Float.floatToIntBits(this.getPrevPitch());
      result = result * 59 + Float.floatToIntBits(this.getPitch());
      result = result * 59 + Float.floatToIntBits(this.getPrevBodyYaw());
      result = result * 59 + Float.floatToIntBits(this.getBodyYaw());
      Object $livingEntity = this.getLivingEntity();
      return result * 59 + ($livingEntity == null ? 43 : $livingEntity.hashCode());
   }

   public LivingEntity getLivingEntity() {
      return this.livingEntity;
   }

   public float getPrevYaw() {
      return this.prevYaw;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPrevPitch() {
      return this.prevPitch;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getPrevBodyYaw() {
      return this.prevBodyYaw;
   }

   public float getBodyYaw() {
      return this.bodyYaw;
   }

   public void setPrevYaw(float prevYaw) {
      this.prevYaw = prevYaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public void setPrevPitch(float prevPitch) {
      this.prevPitch = prevPitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public void setPrevBodyYaw(float prevBodyYaw) {
      this.prevBodyYaw = prevBodyYaw;
   }

   public void setBodyYaw(float bodyYaw) {
      this.bodyYaw = bodyYaw;
   }

   @Override
   public String toString() {
      return "EventPlayerRender(livingEntity="
         + this.getLivingEntity()
         + ", prevYaw="
         + this.getPrevYaw()
         + ", yaw="
         + this.getYaw()
         + ", prevPitch="
         + this.getPrevPitch()
         + ", pitch="
         + this.getPitch()
         + ", prevBodyYaw="
         + this.getPrevBodyYaw()
         + ", bodyYaw="
         + this.getBodyYaw()
         + ")";
   }
}
