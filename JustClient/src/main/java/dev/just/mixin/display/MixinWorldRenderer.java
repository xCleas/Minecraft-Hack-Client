package dev.just.mixin.display;

import dev.just.manager.Manager;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.Fog;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({WorldRenderer.class})
public abstract class MixinWorldRenderer {
   @Shadow
   protected abstract void renderMain(
      FrameGraphBuilder var1,
      Frustum var2,
      Camera var3,
      Matrix4f var4,
      Matrix4f var5,
      Fog var6,
      boolean var7,
      boolean var8,
      RenderTickCounter var9,
      Profiler var10
   );

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;renderMain(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/Frustum;Lnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/render/Fog;ZZLnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/util/profiler/Profiler;)V"
      )
   )
   private void onRender(
      WorldRenderer instance,
      FrameGraphBuilder frameGraphBuilder,
      Frustum frustum,
      Camera camera,
      Matrix4f positionMatrix,
      Matrix4f projectionMatrix,
      Fog fog,
      boolean renderBlockOutline,
      boolean hasEntitiesToRender,
      RenderTickCounter renderTickCounter,
      Profiler profiler
   ) {
      boolean showBlockOutline = Manager.FUNCTION_MANAGER == null || Manager.FUNCTION_MANAGER.blockHighLight == null
         || !Manager.FUNCTION_MANAGER.blockHighLight.isState();
      this.renderMain(
         frameGraphBuilder,
         frustum,
         camera,
         positionMatrix,
         projectionMatrix,
         fog,
         showBlockOutline,
         hasEntitiesToRender,
         renderTickCounter,
         profiler
      );
   }
}
