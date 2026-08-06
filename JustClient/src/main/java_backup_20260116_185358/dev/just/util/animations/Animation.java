package dev.just.util.animations;

import dev.just.util.player.TimerUtil;
import net.minecraft.util.math.Direction.AxisDirection;

public abstract class Animation {
   public TimerUtil timerUtil = new TimerUtil();
   protected int duration;
   protected double endPoint;
   protected AxisDirection direction;

   public Animation(int ms, double endPoint) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = AxisDirection.POSITIVE;
   }

   public Animation(int ms, double endPoint, AxisDirection direction) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = direction;
   }

   public boolean finished(AxisDirection direction) {
      return this.isDone() && this.direction.equals(direction);
   }

   public void setEndPoint(double endPoint) {
      this.endPoint = endPoint;
   }

   public void reset() {
      this.timerUtil.reset();
   }

   public boolean isDone() {
      return this.timerUtil.hasTimeElapsed((long)this.duration);
   }

   public AxisDirection getDirection() {
      return this.direction;
   }

   public void setDirection(AxisDirection direction) {
      if (this.direction != direction) {
         this.direction = direction;
         this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTime())));
      }
   }

   public Animation setDirection(boolean forwards) {
      AxisDirection direction = forwards ? AxisDirection.POSITIVE : AxisDirection.POSITIVE;
      if (this.direction != direction) {
         this.direction = direction;
         this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTime())));
      }

      return this;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   protected boolean correctOutput() {
      return false;
   }

   public double getOutput() {
      if (this.direction == AxisDirection.POSITIVE) {
         return this.isDone() ? this.endPoint : this.getEquation((double)this.timerUtil.getTime()) * this.endPoint;
      } else if (this.isDone()) {
         return 0.0;
      } else if (this.correctOutput()) {
         double revTime = (double)Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
         return this.getEquation(revTime) * this.endPoint;
      } else {
         return (1.0 - this.getEquation((double)this.timerUtil.getTime())) * this.endPoint;
      }
   }

   public double getEndput() {
      if (this.direction == AxisDirection.NEGATIVE) {
         return this.isDone() ? this.endPoint : this.getEquation((double)this.timerUtil.getTime()) * this.endPoint;
      } else if (this.isDone()) {
         return 0.0;
      } else if (this.correctOutput()) {
         double revTime = (double)Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
         return this.getEquation(revTime) * this.endPoint;
      } else {
         return (1.0 - this.getEquation((double)this.timerUtil.getTime())) * this.endPoint;
      }
   }

   protected abstract double getEquation(double var1);
}
