package dev.just.mixin.world;

import dev.just.events.Event;
import dev.just.events.impl.move.EventEntitySpawn;
import net.minecraft.entity.Entity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientWorld.class})
public class MixinClientWorld {
   @Inject(
      method = {"addEntity"},
      at = {@At("TAIL")}
   )
   private void onAddEntity(Entity entity, CallbackInfo ci) {
      Event.call(new EventEntitySpawn(entity));
   }
}
