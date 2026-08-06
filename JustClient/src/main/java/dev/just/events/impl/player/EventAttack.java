package dev.just.events.impl.player;

import dev.just.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class EventAttack extends Event {
   private final PlayerEntity attacker;
   private final Entity target;

   public EventAttack(PlayerEntity attacker, Entity target) {
      this.attacker = attacker;
      this.target = target;
   }

   public PlayerEntity getAttacker() {
      return this.attacker;
   }

   public Entity getTarget() {
      return this.target;
   }
}
