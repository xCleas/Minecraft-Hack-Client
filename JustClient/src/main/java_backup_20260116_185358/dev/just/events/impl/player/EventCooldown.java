package dev.just.events.impl.player;

import dev.just.events.Event;
import net.minecraft.item.Item;

public class EventCooldown extends Event {
   public Item itemStack;
   public float cooldown;

   public EventCooldown(Item item) {
      this.itemStack = item;
   }

   public Item getItemStack() {
      return this.itemStack;
   }

   public float getCooldown() {
      return this.cooldown;
   }

   public void setItemStack(Item itemStack) {
      this.itemStack = itemStack;
   }

   public void setCooldown(float cooldown) {
      this.cooldown = cooldown;
   }

   @Override
   public String toString() {
      return "EventCooldown(itemStack=" + this.getItemStack() + ", cooldown=" + this.getCooldown() + ")";
   }

   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EventCooldown other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else if (Float.compare(this.getCooldown(), other.getCooldown()) != 0) {
         return false;
      } else {
         Object this$itemStack = this.getItemStack();
         Object other$itemStack = other.getItemStack();
         return this$itemStack == null ? other$itemStack == null : this$itemStack.equals(other$itemStack);
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof EventCooldown;
   }

   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      result = result * 59 + Float.floatToIntBits(this.getCooldown());
      Object $itemStack = this.getItemStack();
      return result * 59 + ($itemStack == null ? 43 : $itemStack.hashCode());
   }
}
