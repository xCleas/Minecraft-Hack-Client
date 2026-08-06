package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.combat.I1lO0Il1;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ElytraMotion",
   desc = "RWx5dHJhIGlsZSB1w6dhcmtlbiBiaXIgaGVkZWZlIG9kYWtsYW5kxLHEn8SxbsSxemRhIHNpemkgaGF2YWRhIHNhYml0bGVy",
   type = Type.Move
)
public class ElytraMotion extends Function {
   private final SliderSetting distance = new SliderSetting(I0O1l0I1.b("w4dhbMSxxZ9tYSBNZXNhZmVzaQ=="), 3.0, 0.1F, 5.0, 0.1F);
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
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         fakeBoostMode();
         return;
      }

      if (mc.player == null) return;

      dispatchEvent(event);
   }

   private void dispatchEvent(Event event) {
      l1O0I1lO.fakeHandler();

      if (event instanceof EventUpdate) {
         handleUpdate();
      }

      if (event instanceof EventMotion motion) {
         handleMotion(motion);
      }

      SemanticNoise.deadCode1();
   }

   private void handleUpdate() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         entropy ^= System.nanoTime();
         return;
      }

      updateFreezePosition();
      this.shouldFreeze = evaluateFreezeCondition();
   }

   private void updateFreezePosition() {
      l1O0I1lO.fakeHandler();

      if (mc.player.isGliding()) {
         if (l1O0I1lO.opaqueTrue()) {
            this.freezePosition = mc.player.getPos();
         }
      }
   }

   private boolean evaluateFreezeCondition() {
      l1O0I1lO.fakeHandler();

      if (!checkGliding()) return false;
      if (!checkAuraValid()) return false;

      return checkTargetDistance();
   }

   private boolean checkGliding() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return mc.player.isGliding();
   }

   private boolean checkAuraValid() {
      l1O0I1lO.fakeHandler();

      I1lO0Il1 aura = Manager.FUNCTION_MANAGER.attackAura;

      if (l1O0I1lO.opaqueFalse()) {
         return aura != null;
      }

      return I1lO0l1I.all(
         aura != null,
         aura.target != null
      );
   }

   private boolean checkTargetDistance() {
      l1O0I1lO.fakeHandler();

      I1lO0Il1 aura = Manager.FUNCTION_MANAGER.attackAura;
      if (aura == null || aura.target == null) return false;

      LivingEntity target = aura.target;
      float maxDist = getMaxDistance();

      if (l1O0I1lO.opaqueFalse()) {
         return target.distanceTo(mc.player) < FAKE_DISTANCE;
      }

      return target.distanceTo(mc.player) < maxDist;
   }

   private float getMaxDistance() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f((float) FAKE_DISTANCE);
      }

      return this.distance.get().floatValue();
   }

   private void handleMotion(EventMotion motion) {
      l1O0I1lO.fakeHandler();

      if (!this.shouldFreeze) return;

      if (l1O0I1lO.opaqueTrue()) {
         applyFreeze(motion);
      }
   }

   private void applyFreeze(EventMotion motion) {
      l1O0I1lO.fakeHandler();

      mc.player.setPosition(this.freezePosition);
      mc.player.setVelocity(Vec3d.ZERO);
      motion.setX(lO1I0l1O.d(0.0));
      motion.setY(lO1I0l1O.d(0.0));
      motion.setZ(lO1I0l1O.d(0.0));

      SemanticNoise.deadCode2();
   }

   private void fakeBoostMode() {
      // Never runs
      entropy ^= System.nanoTime();
      freezePosition = new Vec3d(FAKE_VELOCITY, FAKE_VELOCITY, FAKE_VELOCITY);
   }

   @Override
   public void onDisable() {
      l1O0I1lO.fakeHandler();

      this.shouldFreeze = false;
      super.onDisable();
   }
}
