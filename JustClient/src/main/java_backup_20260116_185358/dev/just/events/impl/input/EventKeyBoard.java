package dev.just.events.impl.input;

import dev.just.events.Event;

public class EventKeyBoard extends Event {
   private float movementForward;
   private float movementStrafe;
   private boolean jump;
   private boolean sneak;
   private boolean sprint;

   public EventKeyBoard(float movementForward, float movementStrafe, boolean jump, boolean sneak, boolean sprint) {
      this.movementForward = movementForward;
      this.movementStrafe = movementStrafe;
      this.jump = jump;
      this.sneak = sneak;
      this.sprint = sprint;
   }

   public float getMovementForward() {
      return this.movementForward;
   }

   public void setMovementForward(float movementForward) {
      this.movementForward = movementForward;
   }

   public float getMovementStrafe() {
      return this.movementStrafe;
   }

   public void setMovementStrafe(float movementStrafe) {
      this.movementStrafe = movementStrafe;
   }

   public boolean isJump() {
      return this.jump;
   }

   public void setJump(boolean jump) {
      this.jump = jump;
   }

   public boolean isSneak() {
      return this.sneak;
   }

   public void setSneak(boolean sneak) {
      this.sneak = sneak;
   }

   public boolean isSprint() {
      return this.sprint;
   }

   public void setSprint(boolean sprint) {
      this.sprint = sprint;
   }
}
