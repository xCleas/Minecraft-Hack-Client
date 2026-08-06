package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.combat.AttackAura;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ElytraMotion",
   desc = "RWx5dHJhIGlsZSB1w6dhcmtlbiBiaXIgaGVkZWZlIG9kYWtsYW5kxLHEn8SxbsSxemRhIHNpemkgaGF2YWRhIHNhYml0bGVy",
   type = Type.Move
)
public class ElytraMotion extends Function {
   private final SliderSetting distance = new SliderSetting(Strings.b("w4dhbMSxxZ9tYSBNZXNhZmVzaQ=="), 3.0, 0.1F, 5.0, 0.1F);
   private boolean shouldFreeze;
   private Vec3d freezePosition = Vec3d.ZERO;

   // Fake constants
   private static final double FAKE_DISTANCE = 100.0;
   private static final double FAKE_VELOCITY = 50.0;
   private volatile long entropy = System.nanoTime();

   public ElytraMotion() {
      this.addSettings(new Setting[]{this.distance});
   }

   @Override
   public void onEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         fakeBoostMode();
         return;
      }

      if (mc.player == null) return;

      dispatchEvent(event);
   }

   private void dispatchEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (event instanceof EventUpdate) {
         handleUpdate();
      }

      if (event instanceof EventMotion motion) {
         handleMotion(motion);
      }

      SemanticNoise.deadCode1();
   }

   private void handleUpdate() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         entropy ^= System.nanoTime();
         return;
      }

      updateFreezePosition();
      this.shouldFreeze = evaluateFreezeCondition();
   }

   private void updateFreezePosition() {
      FlowObfuscator.fakeHandler();

      if (mc.player.isGliding()) {
         if (FlowObfuscator.opaqueTrue()) {
            this.freezePosition = mc.player.getPos();
         }
      }
   }

   private boolean evaluateFreezeCondition() {
      FlowObfuscator.fakeHandler();

      if (!checkGliding()) return false;
      if (!checkAuraValid()) return false;

      return checkTargetDistance();
   }

   private boolean checkGliding() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return mc.player.isGliding();
   }

   private boolean checkAuraValid() {
      FlowObfuscator.fakeHandler();

      AttackAura aura = Manager.FUNCTION_MANAGER.attackAura;

      if (FlowObfuscator.opaqueFalse()) {
         return aura != null;
      }

      return LogicSplit.all(
         aura != null,
         aura.target != null
      );
   }

   private boolean checkTargetDistance() {
      FlowObfuscator.fakeHandler();

      AttackAura aura = Manager.FUNCTION_MANAGER.attackAura;
      if (aura == null || aura.target == null) return false;

      LivingEntity target = aura.target;
      float maxDist = getMaxDistance();

      if (FlowObfuscator.opaqueFalse()) {
         return target.distanceTo(mc.player) < FAKE_DISTANCE;
      }

      return target.distanceTo(mc.player) < maxDist;
   }

   private float getMaxDistance() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f((float) FAKE_DISTANCE);
      }

      return this.distance.get().floatValue();
   }

   private void handleMotion(EventMotion motion) {
      FlowObfuscator.fakeHandler();

      if (!this.shouldFreeze) return;

      if (FlowObfuscator.opaqueTrue()) {
         applyFreeze(motion);
      }
   }

   private void applyFreeze(EventMotion motion) {
      FlowObfuscator.fakeHandler();

      mc.player.setPosition(this.freezePosition);
      mc.player.setVelocity(Vec3d.ZERO);
      motion.setX(NumberGuard.d(0.0));
      motion.setY(NumberGuard.d(0.0));
      motion.setZ(NumberGuard.d(0.0));

      SemanticNoise.deadCode2();
   }

   private void fakeBoostMode() {
      // Never runs
      entropy ^= System.nanoTime();
      freezePosition = new Vec3d(FAKE_VELOCITY, FAKE_VELOCITY, FAKE_VELOCITY);
   }

   @Override
   public void onDisable() {
      FlowObfuscator.fakeHandler();

      this.shouldFreeze = false;
      super.onDisable();
   }
}
