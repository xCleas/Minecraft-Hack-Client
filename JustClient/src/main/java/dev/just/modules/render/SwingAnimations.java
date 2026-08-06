package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.render.EventHeldItemRenderer;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import net.minecraft.util.Hand;
import net.minecraft.util.Arm;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.item.ModelTransformationMode;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "SwingAnimations",
   desc = "RWxkZWtpIGXFn3lhbGFyIGnDp2luIMO2emVsIHZ1cnXFnyBhbmltYXN5b25sYXLEsQ==",
   type = Type.Render
)
public class SwingAnimations extends Function {
   private final ModeSetting mode = new ModeSetting(
      I0O1l0I1.b("QW5pbWFzeW9uIFTDvHLDvA=="), "Smooth", "Smooth", "Block", "ToBack", "SelfBack", "360", "Down", "Glide", "DropDown", "DeadCode"
   );
   public final BooleanSetting slowAnimation = new BooleanSetting(I0O1l0I1.b("WWF2YcWfbGF0bWE="), false);
   public final SliderSetting slowAnimationSpeed = new SliderSetting(I0O1l0I1.b("WWF2YcWfbGF0bWEgSMSxesSx"), 12.0, 1.0, 50.0, 1.0, () -> this.slowAnimation.get());
   private final SliderSetting corner = new SliderSetting(I0O1l0I1.b("S8O2xZ9lIEHDp8Sxc8Sx"), 12.0, 1.0, 360.0, 1.0);
   private final SliderSetting slant = new SliderSetting(I0O1l0I1.b("RcSfaW0gQcOnxLFzxLE="), 12.0, 1.0, 360.0, 1.0);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public SwingAnimations() {
      this.addSettings(new Setting[]{this.mode, this.slowAnimation, this.slowAnimationSpeed, this.corner, this.slant});
   }

   private void renderSwordAnimation(MatrixStack matrices, float swingProgress, float equipProgress, Arm arm) {
      int i = arm == Arm.RIGHT ? 1 : -1;
      String var6 = this.mode.get();
      switch (var6) {
         case "Smooth":
            matrices.translate(0.56F * (float)i, -0.52F, -0.72F);
            this.applySwingOffset(matrices, arm, swingProgress);
            break;
         case "Block":
            if (swingProgress > 0.0F) {
               float gx = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               matrices.translate(0.56F * (float)i, equipProgress * -0.2F - 0.5F, -0.7F);
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(45 * i)));
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(gx * -85.0F));
               matrices.translate(-0.1F * (float)i, 0.28F, 0.2F);
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-85.0F));
            } else {
               float n = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float m = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float f1 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               matrices.translate(n * (float)i, m, f1);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
            }
            break;
         case "ToBack":
            float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
            this.applyEquipOffset(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(50.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((-30.0F * (1.0F - g) - 30.0F) * (float)i));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(110.0F * (float)i));
            break;
         case "SelfBack": {
            float anim = (float)Math.sin((double)swingProgress * (Math.PI / 2) * 2.0);
            this.applyEquipOffset(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(90 * i)));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)(-70 * i)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-100.0F - 60.0F * anim));
            break;
         }
         case "360":
            matrices.translate(0.56F * (float)i, -0.52F, -0.72F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-swingProgress * 360.0F));
            break;
         case "Down":
            float yPosition;
            if (swingProgress < 0.8F) {
               yPosition = -0.52F - swingProgress * 1.25F;
            } else {
               float returnProgress = (swingProgress - 0.8F) * 5.0F;
               yPosition = -0.52F - (1.0F - returnProgress);
            }

            matrices.translate(0.56F * (float)i, yPosition, -0.72F);
            break;
         case "Glide":
            this.applyEquipOffset(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(80 * i)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
            matrices.translate(0.0, 0.0, -0.8 * (double)swingProgress);
            break;
         case "DropDown": {
            float anim = (float)Math.sin((double)swingProgress * (Math.PI / 2) * 2.0);
            this.applyEquipOffset(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(80.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.corner.get().floatValue()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-this.slant.get().floatValue() * anim));
            break;
         }
         case "DeadCode": {
            float anim = (float)Math.sin((double)swingProgress * (Math.PI / 2) * 2.0);
            this.applyEquipOffset(matrices, arm, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(anim * -40.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(30.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(60.0F));
         }
      }
   }

   public void renderFirstPersonItem(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light
   ) {
      if (!player.isUsingSpyglass()) {
         boolean bl = hand == Hand.MAIN_HAND;
         Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
         matrices.push();
         if (item.isOf(Items.CROSSBOW)) {
            boolean bl2 = CrossbowItem.isCharged(item);
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               matrices.translate((float)i * -0.4785682F, -0.094387F, 0.05731531F);
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * 65.3F));
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)i * -9.785F));
               float f = (float)item.getMaxUseTime(mc.player) - ((float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F);
               float g = f / (float)CrossbowItem.getPullTime(item, mc.player);
               if (g > 1.0F) {
                  g = 1.0F;
               }

               if (g > 0.1F) {
                  float h = MathHelper.sin((f - 0.1F) * 1.3F);
                  float j = g - 0.1F;
                  float k = h * j;
                  matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
               }

               matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
               matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
               matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)i * 45.0F));
            } else {
               float fx = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float gx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float h = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               matrices.translate((float)i * fx, gx, h);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
               if (bl2 && swingProgress < 0.001F && bl) {
                  matrices.translate((float)i * -0.641864F, 0.0F, 0.0F);
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * 10.0F));
               }
            }

            EventHeldItemRenderer event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
            Event.call(event);
            this.renderItem(player, item, bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !bl3, matrices, vertexConsumers, light);
         } else {
            boolean bl2 = arm == Arm.RIGHT;
            ViewModel viewModel = Manager.FUNCTION_MANAGER.viewModel;
            if (viewModel.state) {
               if (bl2) {
                  matrices.translate(viewModel.right_x.get().floatValue(), viewModel.right_y.get().floatValue(), viewModel.right_z.get().floatValue());
               } else {
                  matrices.translate(-viewModel.left_x.get().floatValue(), viewModel.left_y.get().floatValue(), viewModel.left_z.get().floatValue());
               }
            }

            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
               int l = bl2 ? 1 : -1;
               switch (item.getUseAction()) {
                  case NONE:
                  case BLOCK:
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     break;
                  case EAT:
                  case DRINK:
                     this.applyEatOrDrinkTransformation(matrices, tickDelta, arm, item);
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     break;
                  case BOW:
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     matrices.translate((float)l * -0.2785682F, 0.18344387F, 0.15731531F);
                     matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-13.935F));
                     matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)l * 35.3F));
                     matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)l * -9.785F));
                     float mx = (float)item.getMaxUseTime(mc.player) - ((float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F);
                     float fxx = mx / 20.0F;
                     fxx = (fxx * fxx + fxx * 2.0F) / 3.0F;
                     if (fxx > 1.0F) {
                        fxx = 1.0F;
                     }

                     if (fxx > 0.1F) {
                        float gx = MathHelper.sin((mx - 0.1F) * 1.3F);
                        float h = fxx - 0.1F;
                        float j = gx * h;
                        matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                     }

                     matrices.translate(fxx * 0.0F, fxx * 0.0F, fxx * 0.04F);
                     matrices.scale(1.0F, 1.0F, 1.0F + fxx * 0.2F);
                     matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)l * 45.0F));
                     break;
                  case SPEAR:
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     matrices.translate((float)l * -0.5F, 0.7F, 0.1F);
                     matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-55.0F));
                     matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)l * 35.3F));
                     matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)l * -9.785F));
                     float m = (float)item.getMaxUseTime(mc.player) - ((float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F);
                     float fx = m / 10.0F;
                     if (fx > 1.0F) {
                        fx = 1.0F;
                     }

                     if (fx > 0.1F) {
                        float gx = MathHelper.sin((m - 0.1F) * 1.3F);
                        float h = fx - 0.1F;
                        float j = gx * h;
                        matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                     }

                     matrices.translate(0.0F, 0.0F, fx * 0.2F);
                     matrices.scale(1.0F, 1.0F, 1.0F + fx * 0.2F);
                     matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)l * 45.0F));
                     break;
                  case BRUSH:
                     this.applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
               }
            } else if (player.isUsingRiptide()) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               int l = bl2 ? 1 : -1;
               matrices.translate((float)l * -0.4F, 0.8F, 0.3F);
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)l * 65.0F));
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)l * -85.0F));
            } else if (arm == mc.player.getMainArm() && this.state) {
               this.renderSwordAnimation(matrices, swingProgress, equipProgress, arm);
            } else {
               float n = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float mxx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float fxxx = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               int o = bl2 ? 1 : -1;
               matrices.translate((float)o * n, mxx, fxxx);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
            }

            EventHeldItemRenderer event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
            Event.call(event);
            this.renderItem(player, item, bl2 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !bl2, matrices, vertexConsumers, light);
         }

         matrices.pop();
      }
   }

   private void applyBrushTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress) {
      this.applyEquipOffset(matrices, arm, equipProgress);
      float f = (float)(mc.player.getItemUseTimeLeft() % 10);
      float g = f - tickDelta + 1.0F;
      float h = 1.0F - g / 10.0F;
      float n = -15.0F + 75.0F * MathHelper.cos(h * 2.0F * (float) Math.PI);
      if (arm != Arm.RIGHT) {
         matrices.translate(0.1, 0.83, 0.35);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n));
         matrices.translate(-0.3, 0.22, 0.35);
      } else {
         matrices.translate(-0.25, 0.22, 0.35);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n));
      }
   }

   private void applyEatOrDrinkTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack) {
      float f = (float)mc.player.getItemUseTimeLeft() - tickDelta + 1.0F;
      float g = f / (float)stack.getMaxUseTime(mc.player);
      if (g < 0.8F) {
         float h = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.1F);
         matrices.translate(0.0F, h, 0.0F);
      }

      float h = 1.0F - (float)Math.pow((double)g, 27.0);
      int i = arm == Arm.RIGHT ? 1 : -1;
      matrices.translate(h * 0.6F * (float)i, h * -0.5F, h * 0.0F);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * h * 90.0F));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h * 10.0F));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)i * h * 30.0F));
   }

   private void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress) {
      int i = arm == Arm.RIGHT ? 1 : -1;
      matrices.translate((float)i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
   }

   private void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress) {
      int i = arm == Arm.RIGHT ? 1 : -1;
      float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * (45.0F + f * -20.0F)));
      float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)i * g * -20.0F));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0F));
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * -45.0F));
   }

   public void renderItem(
      LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light
   ) {
      if (!stack.isEmpty()) {
         mc.getItemRenderer()
            .renderItem(
               entity,
               stack,
               renderMode,
               leftHanded,
               matrices,
               vertexConsumers,
               entity.getWorld(),
               light,
               OverlayTexture.DEFAULT_UV,
               entity.getId() + renderMode.ordinal()
            );
      }
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, event.hashCode());
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }
}
