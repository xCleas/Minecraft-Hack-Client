package dev.just.modules.combat.rotation;

import dev.just.manager.IMinecraft;
import dev.just.util.math.MathUtil;
import dev.just.util.player.GCDUtil;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;

public final class RotationController implements IMinecraft {
   private static final RotationController I = new RotationController();
   private final Vector2f rotate = new Vector2f(0.0F, 0.0F);
   private long resetStartTime = -1L;
   private long resetDuration = 0L;
   private float startYaw;
   private float startPitch;
   private float targetYaw;
   private float targetPitch;
   private boolean resetting = false;

   public static RotationController get() {
      return I;
   }

   private RotationController() {
   }

   public float getYaw() {
      return this.rotate.x;
   }

   public float getPitch() {
      return this.rotate.y;
   }

   public void set(float yaw, float pitch) {
      this.rotate.x = yaw;
      this.rotate.y = pitch;
   }

   public void setSmooth(float targetYaw, float targetPitch, float smooth, float maxYawStep, float maxPitchStep, boolean applyGcd) {
      float dYaw = MathHelper.wrapDegrees(targetYaw - this.rotate.x);
      float dPitch = targetPitch - this.rotate.y;
      float stepYaw = Math.min(Math.max(Math.abs(dYaw), 1.0F), maxYawStep);
      float stepPitch = Math.min(Math.max(Math.abs(dPitch), 1.0F), maxPitchStep);
      float ny = this.rotate.x + (dYaw > 0.0F ? stepYaw : -stepYaw) * smooth;
      float np = MathHelper.clamp(this.rotate.y + (dPitch > 0.0F ? stepPitch : -stepPitch) * smooth, -89.9F, 89.9F);
      if (applyGcd) {
         float gcd = GCDUtil.getGCDValue();
         ny -= (ny - this.rotate.x) % gcd;
         np -= (np - this.rotate.y) % gcd;
      }

      this.rotate.x = ny;
      this.rotate.y = np;
   }

   public void smoothReturn(long durationMs) {
      if (mc.player != null) {
         this.resetStartTime = System.currentTimeMillis();
         this.resetDuration = durationMs;
         this.startYaw = this.rotate.x;
         this.startPitch = this.rotate.y;
         this.targetYaw = mc.player.getYaw();
         this.targetPitch = mc.player.getPitch();
         this.resetting = true;
      }
   }

   public void onUpdate() {
      if (this.resetting) {
         long elapsed = System.currentTimeMillis() - this.resetStartTime;
         float progress = Math.min(1.0F, (float)elapsed / (float)this.resetDuration);
         float eased = (float)(1.0 - Math.pow((double)(1.0F - progress), 3.0));
         this.rotate.x = MathUtil.interpolateAngle(this.startYaw, this.targetYaw, eased);
         this.rotate.y = MathUtil.interpolateAngle(this.startPitch, this.targetPitch, eased);
         if (progress >= 1.0F) {
            this.resetting = false;
            this.resetStartTime = -1L;
         }
      }
   }

   public boolean isControlling() {
      return this.resetting;
   }

   public void updateIfFree(float yaw, float pitch) {
      if (!this.resetting) {
         this.rotate.x = yaw;
         this.rotate.y = pitch;
      }
   }
}
