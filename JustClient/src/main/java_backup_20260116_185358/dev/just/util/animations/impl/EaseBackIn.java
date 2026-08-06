package dev.just.util.animations.impl;

import dev.just.util.animations.Animation;
import net.minecraft.util.math.Direction.AxisDirection;

public class EaseBackIn extends Animation {
   private final float easeAmount;

   public EaseBackIn(int ms, double endPoint, float easeAmount) {
      super(ms, endPoint);
      this.easeAmount = easeAmount;
   }

   public EaseBackIn(int ms, double endPoint, float easeAmount, AxisDirection direction) {
      super(ms, endPoint, direction);
      this.easeAmount = easeAmount;
   }

   @Override
   protected boolean correctOutput() {
      return true;
   }

   @Override
   protected double getEquation(double x) {
      double x1 = x / (double)this.duration;
      float shrink = this.easeAmount + 1.0F;
      return Math.max(0.0, 1.0 + (double)shrink * Math.pow(x1 - 1.0, 3.0) + (double)this.easeAmount * Math.pow(x1 - 1.0, 2.0));
   }
}
