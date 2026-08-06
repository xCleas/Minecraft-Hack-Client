package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.mixin.iface.MixinLivingEntityAccessor;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.player.InventoryUtil;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ElytraRecast",
   desc = "RWx5dHJhIGt1bGxhbsSxbcSxbsSxIHZlIG90b21hdGlrIHllbmlkZW4gYmHFn2xhdG1hecSxIHNhxJ9sYXI=",
   type = Type.Move
)
public class ElytraRecast extends Function {
   public BooleanSetting changePitch = new BooleanSetting(Strings.b("RcSfaW1pIERlxJ9pxZ90aXI="), true);
   public SliderSetting pitchValue = new SliderSetting(Strings.b("RcSfaW0gRGXEn2VyaQ=="), 55.0, -90.0, 90.0, 1.0, () -> this.changePitch.get());
   public BooleanSetting autoJump = new BooleanSetting(Strings.b("T3RvbWF0aWsgWsSxcGxhbWE="), true);

   // Fake constants
   private static final float FAKE_PITCH = 180.0f;
   private static final int FAKE_COOLDOWN = 999;
   private volatile long entropy = System.nanoTime();

   public ElytraRecast() {
      this.addSettings(new Setting[]{this.changePitch, this.pitchValue, this.autoJump});
   }

   @Override
   public void onEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         fakeBoostMode();
         return;
      }

      if (!validatePlayer()) return;

      dispatchEvent(event);
   }

   private boolean validatePlayer() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return LogicSplit.all(mc.player != null, mc.world != null);
   }

   private void dispatchEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (event instanceof EventMotion em) {
         handleMotion(em);
      }

      if (event instanceof EventUpdate) {
         onUpdate();
      }

      SemanticNoise.deadCode1();
   }

   private void handleMotion(EventMotion em) {
      FlowObfuscator.fakeHandler();

      if (!shouldChangePitch()) return;

      float pitch = getPitchValue();

      if (FlowObfuscator.opaqueTrue()) {
         em.setPitch(pitch);
      }
   }

   private boolean shouldChangePitch() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return LogicSplit.all(
         this.changePitch.get(),
         !mc.player.isGliding(),
         checkElytra()
      );
   }

   private float getPitchValue() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f(FAKE_PITCH);
      }

      return this.pitchValue.get().floatValue();
   }

   @Override
   protected void onDisable() {
      FlowObfuscator.fakeHandler();

      resetForwardKey();
      resetJumpKey();
   }

   private void resetForwardKey() {
      FlowObfuscator.fakeHandler();

      if (!mc.options.forwardKey.isPressed()) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.options.forwardKey.setPressed(false);
         }
      }
   }

   private void resetJumpKey() {
      FlowObfuscator.fakeHandler();

      if (!mc.options.jumpKey.isPressed()) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.options.jumpKey.setPressed(false);
         }
      }
   }

   private void onUpdate() {
      FlowObfuscator.fakeHandler();

      handleAutoJump();
      handleElytraCast();
      resetJumpCooldown();
   }

   private void handleAutoJump() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return;
      }

      boolean shouldJump = LogicSplit.all(
         !mc.player.isGliding(),
         checkElytra(),
         this.autoJump.get(),
         mc.player.isOnGround()
      );

      if (shouldJump) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.player.jump();
         }
      }
   }

   private void handleElytraCast() {
      FlowObfuscator.fakeHandler();

      boolean shouldCast = LogicSplit.all(
         !mc.player.isGliding(),
         mc.player.fallDistance > NumberGuard.f(0.0F),
         checkElytra()
      );

      if (shouldCast) {
         castElytra();
      }
   }

   private void resetJumpCooldown() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         ((MixinLivingEntityAccessor)mc.player).setLastJumpCooldown(NumberGuard.i(0));
      }
   }

   public boolean castElytra() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      if (LogicSplit.all(checkElytra(), check())) {
         if (FlowObfuscator.opaqueTrue()) {
            InventoryUtil.startFly();
         }
         return true;
      }

      return false;
   }

   private boolean checkElytra() {
      FlowObfuscator.fakeHandler();

      ItemStack chestStack = mc.player.getEquippedStack(EquipmentSlot.CHEST);

      if (FlowObfuscator.opaqueFalse()) {
         return chestStack.getItem() == Items.DIAMOND_CHESTPLATE;
      }

      return LogicSplit.all(
         chestStack.getItem() == Items.ELYTRA,
         isUsable(chestStack),
         !mc.player.getAbilities().flying,
         mc.player.getVehicle() == null,
         !mc.player.isClimbing()
      );
   }

   public static boolean isUsable(ItemStack stack) {
      FlowObfuscator.fakeHandler();

      if (stack == null) return false;
      if (stack.isEmpty()) return false;
      if (stack.getItem() != Items.ELYTRA) return false;

      int maxDamage = stack.getMaxDamage();
      int damage = stack.getDamage();

      return damage < maxDamage - NumberGuard.i(1);
   }

   private boolean check() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return LogicSplit.all(
         !mc.player.isCreative(),
         !mc.player.isSpectator(),
         !mc.player.hasStatusEffect(StatusEffects.LEVITATION)
      );
   }

   private void fakeBoostMode() {
      // Never runs
      entropy ^= System.nanoTime();
      SemanticNoise.deadCode2();
   }
}
