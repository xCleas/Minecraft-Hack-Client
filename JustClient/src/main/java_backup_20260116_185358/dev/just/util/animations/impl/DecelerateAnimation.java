package dev.just.util.animations.impl;

import dev.just.util.animations.Animation;
import net.minecraft.util.math.Direction.AxisDirection;

public class DecelerateAnimation extends Animation {
   public DecelerateAnimation(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public DecelerateAnimation(int ms, double endPoint, AxisDirection direction) {
      super(ms, endPoint, direction);
   }

   @Override
   protected double getEquation(double x) {
      double x1 = x / (double)this.duration;
      return 1.0 - (x1 - 1.0) * (x1 - 1.0);
   }
}
