package dev.just.events.impl.player;

import dev.just.events.Event;

public class EventSprint extends Event {
   private boolean sprinting;

   public EventSprint(boolean sprinting) {
      this.sprinting = sprinting;
   }

   public EventSprint() {
   }

   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EventSprint other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         return !super.equals(o) ? false : this.isSprinting() == other.isSprinting();
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof EventSprint;
   }

   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      return result * 59 + (this.isSprinting() ? 79 : 97);
   }

   public boolean isSprinting() {
      return this.sprinting;
   }

   public void setSprinting(boolean sprinting) {
      this.sprinting = sprinting;
   }

   @Override
   public String toString() {
      return "EventSprint(sprinting=" + this.isSprinting() + ")";
   }
}
