package dev.just.mixin.display;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.just.events.Event;
import dev.just.events.impl.world.EventFog;
import dev.just.manager.Manager;
import dev.just.protect.runtime.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.BackgroundRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BackgroundRenderer.class})
public class MixinBackgroundRenderer {
   @Inject(
      method = {"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      if (Manager.FUNCTION_MANAGER.noRender.state && Manager.FUNCTION_MANAGER.noRender.mods.get(Strings.b("S8O2dMO8IEVmZWt0bGVy"))) {
         info.setReturnValue(null);
      }
   }

   @ModifyReturnValue(
      method = {"applyFog"},
      at = {@At("RETURN")}
   )
   private static Fog modifyFog(
      Fog original, Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta
   ) {
      EventFog fogEvent = new EventFog();
      Event.call(fogEvent);
      return fogEvent.modified ? new Fog(fogEvent.start, fogEvent.end, fogEvent.shape, fogEvent.r, fogEvent.g, fogEvent.b, fogEvent.alpha) : original;
   }
}
