package dev.just.util.animations.impl;

import dev.just.util.animations.Animation;

public class EaseInOutQuad extends Animation {
   public EaseInOutQuad(int ms, double endPoint) {
      super(ms, endPoint);
   }

   @Override
   protected double getEquation(double x1) {
      double x = x1 / (double)this.duration;
      return x < 0.5 ? 2.0 * Math.pow(x, 2.0) : 1.0 - Math.pow(-2.0 * x + 2.0, 2.0) / 2.0;
   }
}
