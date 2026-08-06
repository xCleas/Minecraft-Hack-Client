package dev.just.mixin.world;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.render.ItemPhysic;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
public abstract class MixinItemEntityRenderer implements IMinecraft {
   @Unique
   private boolean isOnGround = false;
   @Unique
   private float entityAge = 0.0F;
   @Unique
   private float uniqueOffset = 0.0F;

   @Inject(
      method = {"updateRenderState"},
      at = {@At("HEAD")}
   )
   private void onUpdateRenderState(ItemEntity entity, ItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
      this.isOnGround = entity.isOnGround();
      this.entityAge = state.age;
      this.uniqueOffset = state.uniqueOffset;
   }

   @ModifyVariable(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderStack(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;Lnet/minecraft/util/math/random/Random;)V"
      ),
      ordinal = 0
   )
   private MatrixStack modifyMatrixStack(MatrixStack matrices, ItemEntityRenderState state, MatrixStack original, VertexConsumerProvider vertexConsumers, int light) {
      ItemPhysic itemPhysic = Manager.FUNCTION_MANAGER.itemPhysic;
      if (itemPhysic.state) {
         if (itemPhysic.mode.is("Normal")) {
            matrices.pop();
            matrices.push();
            float rotation = ItemEntity.getRotation(this.entityAge, this.uniqueOffset);
            if (this.isOnGround) {
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            } else {
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation * 300.0F));
            }
         } else if (itemPhysic.mode.is("2D")) {
            matrices.pop();
            matrices.push();
            matrices.translate(0.0F, 0.1F, 0.0F);
            matrices.multiply(mc.getEntityRenderDispatcher().getRotation());
            matrices.scale(1.1F, 1.1F, 0.0F);
            state.itemRenderState.render(matrices, vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV);
         }
      }

      return matrices;
   }
}
