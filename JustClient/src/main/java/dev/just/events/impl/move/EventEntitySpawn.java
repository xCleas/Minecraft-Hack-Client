package dev.just.events.impl.move;

import dev.just.events.Event;
import net.minecraft.entity.Entity;

public class EventEntitySpawn extends Event {
   private final Entity entity;

   public Entity getEntity() {
      return this.entity;
   }

   public EventEntitySpawn(Entity entity) {
      this.entity = entity;
   }
}
