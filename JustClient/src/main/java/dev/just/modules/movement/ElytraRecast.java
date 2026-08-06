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
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ElytraRecast",
   desc = "RWx5dHJhIGt1bGxhbsSxbcSxbsSxIHZlIG90b21hdGlrIHllbmlkZW4gYmHFn2xhdG1hecSxIHNhxJ9sYXI=",
   type = Type.Move
)
public class ElytraRecast extends Function {
   public BooleanSetting changePitch = new BooleanSetting(I0O1l0I1.b("RcSfaW1pIERlxJ9pxZ90aXI="), true);
   public SliderSetting pitchValue = new SliderSetting(I0O1l0I1.b("RcSfaW0gRGXEn2VyaQ=="), 55.0, -90.0, 90.0, 1.0, () -> this.changePitch.get());
   public BooleanSetting autoJump = new BooleanSetting(I0O1l0I1.b("T3RvbWF0aWsgWsSxcGxhbWE="), true);

   // Fake constants
   private static final float FAKE_PITCH = 180.0f;
   private static final int FAKE_COOLDOWN = 999;
   private volatile long entropy = System.nanoTime();

   public ElytraRecast() {
      this.addSettings(new Setting[]{this.changePitch, this.pitchValue, this.autoJump});
   }

   @Override
   public void onEvent(Event event) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         fakeBoostMode();
         return;
      }

      if (!validatePlayer()) return;

      dispatchEvent(event);
   }

   private boolean validatePlayer() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return I1lO0l1I.all(mc.player != null, mc.world != null);
   }

   private void dispatchEvent(Event event) {
      l1O0I1lO.fakeHandler();

      if (event instanceof EventMotion em) {
         handleMotion(em);
      }

      if (event instanceof EventUpdate) {
         onUpdate();
      }

      SemanticNoise.deadCode1();
   }

   private void handleMotion(EventMotion em) {
      l1O0I1lO.fakeHandler();

      if (!shouldChangePitch()) return;

      float pitch = getPitchValue();

      if (l1O0I1lO.opaqueTrue()) {
         em.setPitch(pitch);
      }
   }

   private boolean shouldChangePitch() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return I1lO0l1I.all(
         this.changePitch.get(),
         !mc.player.isGliding(),
         checkElytra()
      );
   }

   private float getPitchValue() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f(FAKE_PITCH);
      }

      return this.pitchValue.get().floatValue();
   }

   @Override
   protected void onDisable() {
      l1O0I1lO.fakeHandler();

      resetForwardKey();
      resetJumpKey();
   }

   private void resetForwardKey() {
      l1O0I1lO.fakeHandler();

      if (!mc.options.forwardKey.isPressed()) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.options.forwardKey.setPressed(false);
         }
      }
   }

   private void resetJumpKey() {
      l1O0I1lO.fakeHandler();

      if (!mc.options.jumpKey.isPressed()) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.options.jumpKey.setPressed(false);
         }
      }
   }

   private void onUpdate() {
      l1O0I1lO.fakeHandler();

      handleAutoJump();
      handleElytraCast();
      resetJumpCooldown();
   }

   private void handleAutoJump() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return;
      }

      boolean shouldJump = I1lO0l1I.all(
         !mc.player.isGliding(),
         checkElytra(),
         this.autoJump.get(),
         mc.player.isOnGround()
      );

      if (shouldJump) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.jump();
         }
      }
   }

   private void handleElytraCast() {
      l1O0I1lO.fakeHandler();

      boolean shouldCast = I1lO0l1I.all(
         !mc.player.isGliding(),
         mc.player.fallDistance > lO1I0l1O.f(0.0F),
         checkElytra()
      );

      if (shouldCast) {
         castElytra();
      }
   }

   private void resetJumpCooldown() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         ((MixinLivingEntityAccessor)mc.player).setLastJumpCooldown(lO1I0l1O.i(0));
      }
   }

   public boolean castElytra() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      if (I1lO0l1I.all(checkElytra(), check())) {
         if (l1O0I1lO.opaqueTrue()) {
            InventoryUtil.startFly();
         }
         return true;
      }

      return false;
   }

   private boolean checkElytra() {
      l1O0I1lO.fakeHandler();

      ItemStack chestStack = mc.player.getEquippedStack(EquipmentSlot.CHEST);

      if (l1O0I1lO.opaqueFalse()) {
         return chestStack.getItem() == Items.DIAMOND_CHESTPLATE;
      }

      return I1lO0l1I.all(
         chestStack.getItem() == Items.ELYTRA,
         isUsable(chestStack),
         !mc.player.getAbilities().flying,
         mc.player.getVehicle() == null,
         !mc.player.isClimbing()
      );
   }

   public static boolean isUsable(ItemStack stack) {
      l1O0I1lO.fakeHandler();

      if (stack == null) return false;
      if (stack.isEmpty()) return false;
      if (stack.getItem() != Items.ELYTRA) return false;

      int maxDamage = stack.getMaxDamage();
      int damage = stack.getDamage();

      return damage < maxDamage - lO1I0l1O.i(1);
   }

   private boolean check() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return I1lO0l1I.all(
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
