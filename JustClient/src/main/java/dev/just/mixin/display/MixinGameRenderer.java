package dev.just.mixin.display;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.modules.combat.I1lO0Il1;
import dev.just.modules.combat.lI0l1OlI;
import dev.just.modules.combat.SelfTrap;
import dev.just.modules.combat.rotation.RotationController;
import dev.just.modules.render.AspectRatio;
import dev.just.util.math.RayTraceUtil;
import dev.just.util.render.Render3DUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.vector.VectorUtil;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Fog;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRenderer.class})
public abstract class MixinGameRenderer implements IMinecraft {
   @Shadow
   public abstract float getViewDistance();

   @Inject(
      method = {"renderWorld"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
         opcode = 180,
         ordinal = 0
      )}
   )
   private void renderWorld(RenderTickCounter tickCounter, CallbackInfo callbackInfo, @Local(ordinal = 2) Matrix4f matrix4f) {
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.multiplyPositionMatrix(matrix4f);
      matrixStack.translate(mc.getEntityRenderDispatcher().camera.getPos().negate());
      VectorUtil.previousProjectionMatrix = RenderSystem.getProjectionMatrix();
      VectorUtil.lastWorldSpaceMatrix = matrixStack.peek();
      Render3DUtil.setLastProjMat(RenderSystem.getProjectionMatrix());
      Render3DUtil.setLastWorldSpaceMatrix(matrixStack.peek());
      EventRender3D event = new EventRender3D(matrixStack, tickCounter);
      Event.call(event);
      Render3DUtil.onWorldRender(event);
   }

   @Inject(
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
         opcode = 180,
         ordinal = 0
      )},
      method = {"renderWorld"}
   )
   private void render3dHook(RenderTickCounter tickCounter, CallbackInfo ci) {
      MatrixStack matrixStack = new MatrixStack();
      Camera camera = mc.gameRenderer.getCamera();
      RenderSystem.getModelViewStack().pushMatrix().mul(matrixStack.peek().getPositionMatrix());
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      RenderSystem.setShaderFog(Fog.DUMMY);
      RenderUtil.render3D.setTranslation(matrixStack);
      Event.call(new EventRender3D(matrixStack, tickCounter));
      RenderSystem.getModelViewStack().popMatrix();
      RenderSystem.getModelViewMatrix();
   }

   @Inject(
      method = {"getBasicProjectionMatrix"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void getBasicProjectionMatrixHook(float fov, CallbackInfoReturnable<Matrix4f> cir) {
      if (Manager.FUNCTION_MANAGER == null) return;
      AspectRatio aspectRatio = Manager.FUNCTION_MANAGER.aspectRatio;
      if (aspectRatio != null && aspectRatio.state) {
         float aspect = 1.0F;
         String mode = aspectRatio.mods.get();
         if (mode.equals("4:3")) {
            aspect = 1.3333334F;
         } else if (mode.equals("16:9")) {
            aspect = 1.7777778F;
         } else if (mode.equals("1:1")) {
            aspect = 1.0F;
         } else if (mode.equals("16:10")) {
            aspect = 1.6F;
         } else if (mode.equals(I0O1l0I1.b("w5Z6ZWw="))) {
            aspect = aspectRatio.slider.get().floatValue();
         }

         MatrixStack matrixStack = new MatrixStack();
         matrixStack.peek().getPositionMatrix().identity();
         matrixStack.peek()
            .getPositionMatrix()
            .mul(new Matrix4f().setPerspective((float)((double)fov * (Math.PI / 180.0)), aspect, 0.05F, this.getViewDistance() * 4.0F));
         cir.setReturnValue(matrixStack.peek().getPositionMatrix());
      }
   }

   @Inject(
      method = {"findCrosshairTarget"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onFindCrosshairTarget(Entity camera, double blockRange, double entityRange, float tickDelta, CallbackInfoReturnable<HitResult> cir) {
      if (Manager.FUNCTION_MANAGER == null || mc.player == null) return;
      I1lO0Il1 attackAura = Manager.FUNCTION_MANAGER.attackAura;
      lI0l1OlI crystalAura = Manager.FUNCTION_MANAGER.crystalAura;
      SelfTrap selfTrap = Manager.FUNCTION_MANAGER.selfTrap;
      RotationController rotation = Manager.ROTATION;
      float yaw;
      float pitch;
      if (attackAura.state || rotation.isControlling()) {
         yaw = rotation.getYaw();
         pitch = rotation.getPitch();
      } else if (crystalAura.state && crystalAura.rotate != null && crystalAura.closestCrystal != null) {
         yaw = crystalAura.rotate.x;
         pitch = crystalAura.rotate.y;
      } else if (selfTrap.state && selfTrap.active) {
         yaw = selfTrap.rotate.x;
         pitch = crystalAura.rotate.y;
      } else {
         yaw = mc.player.getYaw();
         pitch = mc.player.getPitch();
      }

      double maxRange = Math.max(blockRange, entityRange);
      Vec3d cameraPos = camera.getCameraPosVec(tickDelta);
      EntityHitResult entityHit = RayTraceUtil.rayCastEntity(maxRange, yaw, pitch, e -> !e.isSpectator() && e.canHit());
      BlockHitResult blockHit = RayTraceUtil.rayCast(maxRange, yaw, pitch, false);
      if (entityHit != null) {
         double entityDistSq = entityHit.getPos().squaredDistanceTo(cameraPos);
         double blockDistSq = blockHit != null ? blockHit.getPos().squaredDistanceTo(cameraPos) : Double.MAX_VALUE;
         if (entityDistSq <= entityRange * entityRange && entityDistSq < blockDistSq) {
            cir.setReturnValue(entityHit);
            return;
         }
      }

      if (blockHit != null && blockHit.getPos().squaredDistanceTo(cameraPos) <= blockRange * blockRange) {
         cir.setReturnValue(blockHit);
      } else {
         cir.setReturnValue(BlockHitResult.createMissed(cameraPos, null, null));
      }
   }

   @Redirect(
      method = {"findCrosshairTarget"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
      )
   )
   private EntityHitResult findCrosshairTarget(Entity entity, Vec3d start, Vec3d end, Box box, Predicate<Entity> predicate, double maxDistance) {
      if (Manager.FUNCTION_MANAGER == null || Manager.FUNCTION_MANAGER.noRayTrace == null) {
         return ProjectileUtil.raycast(entity, start, end, box, predicate, maxDistance);
      }
      return !Manager.FUNCTION_MANAGER.noRayTrace.state ? ProjectileUtil.raycast(entity, start, end, box, predicate, maxDistance) : null;
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tiltViewWhenHurtHook(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.noRender != null
          && Manager.FUNCTION_MANAGER.noRender.state && Manager.FUNCTION_MANAGER.noRender.mods.get(I0O1l0I1.b("S2FtZXJhIFNhcnPEsW50xLFzxLE="))) {
         ci.cancel();
      }
   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"
      )
   )
   private float badEffects(float delta, float first, float second) {
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.noRender != null
          && Manager.FUNCTION_MANAGER.noRender.state && Manager.FUNCTION_MANAGER.noRender.mods.get(I0O1l0I1.b("S8O2dMO8IEVmZWt0bGVy"))) {
         return 0.0F;
      }
      return MathHelper.lerp(delta, first, second);
   }
}
