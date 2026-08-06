package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.input.EventKeyBoard;
import dev.just.events.impl.move.EventMotion;
import dev.just.events.impl.player.EventSprint;
import dev.just.manager.Manager;
import dev.just.mixin.iface.ClientPlayerEntityAccessor;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.movement.ElytraTarget;
import dev.just.modules.render.littlePet.GhostWolfEntity;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.EntityHelper;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;
import dev.just.util.math.RayTraceUtil;
import dev.just.util.move.MoveUtil;
import dev.just.util.player.AuraUtil;
import dev.just.util.player.InventoryUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

@FunctionAnnotation(
   name = "AttackAura",
   keywords = {"Legit", "Rage", "KillAura"},
   desc = "RXRyYWZ0YWtpIGhlZGVmbGVyZSBvdG9tYXRpayBvZGFrbGFuxLFyLg==",
   type = Type.Combat
)
public class AttackAura extends Function {
   private final ModeSetting mode = new ModeSetting(
      Strings.b("U2FsZMSxcsSxIE1vZHU="), Strings.b("U3RhbmRhcnQ="), Strings.b("U3RhbmRhcnQ="), Strings.b("SGFzc2Fz"), Strings.b("QWvEsWPEsQ=="), Strings.b("RGluYW1paw=="), Strings.b("U2VyaQ=="), Strings.b("R2VsacWfbWnFnw=="), Strings.b("QWx0ZXJuYXRpZg=="), Strings.b("VnVydcWfIE9wdGltaXpl"), Strings.b("R3JpbSBZYXLEsSBCeXBhc3M=")
   );
   private final MultiSetting targets = new MultiSetting(
      Strings.b("SGVkZWYgU2XDp2ltaQ=="),
      Arrays.asList(Strings.b("T3l1bmN1bGFy"), Strings.b("TW9ibGFy"), Strings.b("Q2FuYXZhcmxhcg==")),
      new String[]{Strings.b("T3l1bmN1bGFy"), Strings.b("WsSxcmhzxLF6bGFy"), Strings.b("RG9zdGxhcg=="), Strings.b("TW9ibGFy"), Strings.b("Q2FuYXZhcmxhcg=="), Strings.b("S8O2eWzDvGxlcg==")}
   );
   private final ModeSetting sort = new ModeSetting(Strings.b("SGVkZWYgw5ZuY2VsacSfaQ=="), Strings.b("Q2Fu"), Strings.b("Q2Fu"), Strings.b("TWVzYWZl"), Strings.b("WsSxcmg="));
   private final MultiSetting setting = new MultiSetting(
      Strings.b("U2FsZMSxcsSxIEF5YXJsYXLEsQ=="), Arrays.asList(Strings.b("U2FkZWNlIEtyaXRpaw=="), Strings.b("S2Fsa2FuIEvEsXLEsWPEsQ==")), new String[]{Strings.b("U2FkZWNlIEtyaXRpaw=="), Strings.b("S2Fsa2FuIEvEsXLEsWPEsQ=="), Strings.b("S2Fsa2FuIETDvMWfw7xy")}
   );
   private final SliderSetting distance = new SliderSetting(Strings.b("VnVydcWfIE1lbnppbGk="), 3.0, 1.8F, 6.0, 0.1F);
   private final SliderSetting rotateDistance = new SliderSetting(Strings.b("T2Rha2xhbm1hIE1lc2FmZXNp"), 5.0, 0.0, 10.0, 0.1F);
   private final SliderSetting elytraDistance = new SliderSetting(Strings.b("RWx5dHJhIFRlc3BpdCBNZW56aWxp"), 40.0, 0.0, 80.0, 1.0);
   private final SliderSetting predictionAmount = new SliderSetting(Strings.b("VGFobWluIEhhc3Nhc2l5ZXRp"), 1.0, 0.0, 3.0, 0.1F);
   private final BooleanSetting predictFlyingTargetsOnly = new BooleanSetting(Strings.b("U2FkZWNlIFXDp2FubGFyxLEgVGFraXAgRXQ="), true);
   private final BooleanSetting elytraEnabled = new BooleanSetting(Strings.b("RWx5dHJhIERlc3RlxJ9p"), true);
   private final ModeSetting elytraMode = new ModeSetting(() -> this.elytraEnabled.get(), Strings.b("U2FsZMSxcsSxIFRpcGk="), Strings.b("U2VyaQ=="), Strings.b("U2VyaQ=="), Strings.b("QcSfxLFy"), "ElytraTarget");
   private final SliderSetting elytraRotationSpeed = new SliderSetting(
      Strings.b("VGFraXAgSGFzc2FzaXlldGk="), 15.0, 5.0, 30.0, 0.5, () -> this.elytraEnabled.get() && !this.elytraMode.is("ElytraTarget")
   );
   private final SliderSetting elytraAimHeight = new SliderSetting(
      Strings.b("SGVkZWYgT2RhayBOb2t0YXPEsQ=="), 0.5, 0.0, 1.0, 0.01F, () -> this.elytraEnabled.get() && !this.elytraMode.is("ElytraTarget")
   );
   private final BooleanSetting elytraPredict = new BooleanSetting(
      Strings.b("S29udW0gS2VzdGlyaW1p"), true, () -> this.elytraEnabled.get() && !this.elytraMode.is("ElytraTarget")
   );
   private final SliderSetting elytraPredictFactor = new SliderSetting(
      Strings.b("S2VzdGlyaW0gS2F0c2F5xLFzxLE="), 0.5, 0.0, 2.0, 0.05F, () -> this.elytraEnabled.get() && !this.elytraMode.is("ElytraTarget") && this.elytraPredict.get()
   );
   private final SliderSetting elytraReach = new SliderSetting(Strings.b("SGF2YSBWdXJ1xZ8gTWVuemlsaQ=="), 6.5, 4.0, 12.0, 0.1F, () -> this.elytraEnabled.get());
   private final SliderSetting normalCPS = new SliderSetting(
      "Normal CPS", 20.0, 1.0, 50.0, 0.5, () -> !this.elytraEnabled.get() || mc.player != null && !mc.player.isGliding()
   );
   private final SliderSetting elytraCPS = new SliderSetting("Elytra CPS", 15.0, 1.0, 30.0, 0.5, () -> this.elytraEnabled.get());
   private final BooleanSetting elytraFastHitbox = new BooleanSetting(Strings.b("R2VuacWfIEhpdGJveA=="), true, () -> this.elytraEnabled.get() && this.elytraMode.is(Strings.b("SMSxemzEsQ==")));
   private final SliderSetting elytraHitboxSize = new SliderSetting(
      Strings.b("SGl0Ym94IEJveXV0dQ=="), 0.3F, 0.1F, 1.0, 0.05F, () -> this.elytraEnabled.get() && this.elytraMode.is(Strings.b("SMSxemzEsQ==")) && this.elytraFastHitbox.get()
   );
   private final BooleanSetting elytraTargetIntegration = new BooleanSetting(
      Strings.b("VcOndcWfIE1vZMO8bMO8IEVudGVncmVzaQ=="), true, () -> this.elytraEnabled.get() && this.elytraMode.is("ElytraTarget")
   );
   private final SliderSetting snapSpeed = new SliderSetting(Strings.b("S2lsaXRsZW5tZSBIxLF6xLE="), 150.0, 50.0, 300.0, 50.0, () -> this.mode.is(Strings.b("SMSxemzEsQ==")));
   private final BindBooleanSetting onlySpaceCritical = new BindBooleanSetting(Strings.b("U2FkZWNlIFrEsXBsYXJrZW4gVnVy"), false, () -> this.setting.get(Strings.b("U2FkZWNlIEtyaXRpaw==")));
   private final BooleanSetting noAttackIfEat = new BooleanSetting(Strings.b("WWVtZWsgWWVya2VuIER1cmFrbGF0"), false);
   private final BooleanSetting raycast = new BooleanSetting("Raycast", false);
   public final BooleanSetting correction = new BooleanSetting(Strings.b("SGFyZWtldCBEw7x6ZWx0bWU="), true);
   public final ModeSetting correctionType = new ModeSetting(() -> this.correction.get(), Strings.b("RMO8emVsdG1lIFTDvHLDvA=="), Strings.b("U2VyYmVzdA=="), Strings.b("U2VyYmVzdA=="), Strings.b("T2Rhaw=="));
   private final ModeSetting sprintreset = new ModeSetting(Strings.b("S2/Fn3UgU8SxZsSxcmxhbWE="), "Rage", "Rage", "Legit", Strings.b("S2FwYWzEsQ=="));
   public LivingEntity target = null;
   private long lastAttackTime = 0L;
   private long lastHitMs = 0L;
   private int preSprintTicks = 0;
   private boolean wasElytraLastTick = false;
   private long grimBypassLastSwitch = 0L;
   private int grimBypassPattern = 0;
   private float randomYawOffset = 0.0F;
   private float randomPitchOffset = 0.0F;
   private int randomUpdateTicks = 0;
   private float bodyYaw;
   private float bodyPitch;
   private float prevBodyYaw;
   private float prevBodyPitch;
   private float headYaw;
   private float headPitch;
   private float prevHeadYaw;
   private float prevHeadPitch;
   private final int updateInterval = 2;
   private final float maxYawShake = 0.3F;
   private final float maxPitchShake = 0.25F;
   private final Random random = new Random();
   private long shakeStartTime = 0L;
   private boolean swingSideRight = false;
   private float jitterYaw = 0.0F;
   private float jitterYawTarget = 0.0F;
   private float jitterYawSpeed = 0.0F;
   private float microJitter = 0.0F;
   private float swayPhase = 0.0F;
   private float swaySpeed = 0.04F;
   private float swayAmplitude = 2.5F;
   private long lastSwitch = 0L;
   private long lastBreathChange = 0L;

   public AttackAura() {
      this.addSettings(
         new Setting[]{
            this.mode,
            this.targets,
            this.sort,
            this.setting,
            this.distance,
            this.rotateDistance,
            this.elytraDistance,
            this.predictionAmount,
            this.predictFlyingTargetsOnly,
            this.elytraEnabled,
            this.elytraMode,
            this.elytraRotationSpeed,
            this.elytraAimHeight,
            this.elytraPredict,
            this.elytraPredictFactor,
            this.elytraReach,
            this.normalCPS,
            this.elytraCPS,
            this.elytraFastHitbox,
            this.elytraHitboxSize,
            this.elytraTargetIntegration,
            this.snapSpeed,
            this.correction,
            this.correctionType,
            this.sprintreset,
            this.onlySpaceCritical,
            this.noAttackIfEat,
            this.raycast
         }
      );
   }

   @Override
   public void onEvent(Event event) {
      ClientPlayerEntity player = mc.player;
      if (player != null && !player.isDead()) {
         if (event instanceof EventKeyBoard e && this.correction.get() && this.correctionType.is("Free")) {
            MoveUtil.fixMovement(e, Manager.FUNCTION_MANAGER.autoPotion.isActivePotion ? Manager.ROTATION.getPitch() : Manager.ROTATION.getYaw());
         }

         if (event instanceof EventSprint sprint && this.sprintreset.is("Legit") && this.canAttack() && this.target != null && player.isSprinting()) {
            sprint.setSprinting(false);
         }

         if (event instanceof EventUpdate) {
            if (this.target == null || !this.isValidTarget(this.target)) {
               this.target = this.findTarget();
            }

            if (this.target == null) {
               Manager.ROTATION.set(player.getYaw(), player.getPitch());
               return;
            }

            this.handleAttackAndRotation(this.target);
            this.wasElytraLastTick = player.isGliding();
         }

         if (event instanceof EventMotion motion) {
            motion.setYaw(Manager.ROTATION.getYaw());
            motion.setPitch(Manager.ROTATION.getPitch());
         }
      } else {
         this.target = null;
         this.preSprintTicks = 0;
         this.wasElytraLastTick = false;
      }
   }

   @Override
   protected void onEnable() {
      super.onEnable();
      this.wasElytraLastTick = false;
      this.lastAttackTime = 0L;
      this.grimBypassLastSwitch = System.currentTimeMillis();
      this.grimBypassPattern = (int)(Math.random() * 3.0);
   }

   @Override
   protected void onDisable() {
      if (this.target != null && this.isValidTarget(this.target)) {
         String modeName = this.mode.get();
         if (!modeName.equals(Strings.b("QWvEsWPEsQ==")) && !modeName.equals("Hassas") && !modeName.equals("Standart")) {
            Manager.ROTATION.set(mc.player.getYaw(), mc.player.getPitch());
         } else {
            Manager.ROTATION.smoothReturn(350L);
         }
      }

      this.target = null;
      this.lastAttackTime = 0L;
      this.wasElytraLastTick = false;
      super.onDisable();
   }

   private boolean isValidTarget(LivingEntity entity) {
      FlowObfuscator.fakeHandler();

      // Fake check - never runs
      if (FlowObfuscator.opaqueFalse()) {
         return entity.age > NumberGuard.i(9999);
      }

      int score = evaluateTargetScore(entity);
      FlowObfuscator.fakeHandler();

      return LogicSplit.threshold(score, NumberGuard.i(5));
   }

   private int evaluateTargetScore(LivingEntity entity) {
      int score = 0;
      FlowObfuscator.fakeHandler();

      // Basic validity
      score += LogicSplit.score(entity != null);
      score += LogicSplit.score(EntityHelper.checkAlive(entity));
      score += LogicSplit.score(EntityHelper.checkNotSelf(entity));

      if (score < NumberGuard.i(3)) return 0;

      // Distance check
      double dist = AuraUtil.getDistance(entity);
      double attackRange = this.distance.get().doubleValue();
      double detectRange = mc.player.isGliding() ? this.elytraDistance.get().doubleValue() : this.rotateDistance.get().doubleValue();

      boolean inRange = LogicSplit.or(
         LogicSplit.not(LogicSplit.greaterThan((int)(dist * 10), (int)(attackRange * 10))),
         LogicSplit.and(
            LogicSplit.greaterThan((int)(detectRange * 10), 0),
            LogicSplit.not(LogicSplit.greaterThan((int)(dist * 10), (int)(detectRange * 10)))
         )
      );
      score += LogicSplit.score(inRange);

      // Fake check
      if (FlowObfuscator.opaqueFalse()) {
         score += entity.getId() % NumberGuard.i(100);
      }

      // AntiBot check
      score += LogicSplit.score(LogicSplit.not(Manager.FUNCTION_MANAGER.antiBot.check(entity)));

      // Entity type checks
      score += checkEntityType(entity);

      FlowObfuscator.fakeHandler();
      return score;
   }

   private int checkEntityType(LivingEntity entity) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entity.hashCode() % NumberGuard.i(10);
      }

      if (entity instanceof PlayerEntity) {
         if (!this.targets.get("Oyuncular")) return -10;
         if (!this.targets.get(Strings.b("QXJrYWRhxZ9sYXI=")) && Manager.FRIEND_MANAGER.isFriend(entity.getName().getString())) return -10;
         return 1;
      }

      if (entity instanceof ArmorStandEntity) return -10;

      if (Manager.FUNCTION_MANAGER.littleSnickers.state && entity instanceof GhostWolfEntity) return -10;

      if (entity instanceof VillagerEntity) {
         return this.targets.get(Strings.b("S8O2eWzDvGxlcg==")) ? 1 : -10;
      }

      // Monster kontrolü MobEntity'den ÖNCE yapılmalı çünkü Monster'lar aynı zamanda MobEntity
      if (entity instanceof Monster) {
         return this.targets.get("Canavarlar") ? 1 : -10;
      }

      if (entity instanceof AnimalEntity) {
         return this.targets.get("Moblar") ? 1 : -10;
      }

      if (entity instanceof MobEntity) {
         return this.targets.get("Moblar") ? 1 : -10;
      }

      FlowObfuscator.fakeHandler();
      return 1;
   }

   private LivingEntity findTarget() {
      List<LivingEntity> list = new ArrayList<>();

      // Null check - crash fix
      Iterable<Entity> entities = Manager.SYNC_MANAGER.getEntities();
      if (entities == null) {
         return null;
      }

      for (Entity e : entities) {
         if (e instanceof LivingEntity le) {
            if (this.isValidTarget(le)) {
               list.add(le);
            }
         }
      }

      if (list.isEmpty()) {
         return null;
      }

      // Sort by preference
      String sortMode = this.sort.get();
      if (sortMode.equals(Strings.b("U2HEn2zEsWs="))) {
         list.sort(Comparator.comparing(LivingEntity::getHealth));
      } else if (sortMode.equals("Mesafe")) {
         list.sort(Comparator.comparingDouble(mc.player::distanceTo));
      } else if (sortMode.equals(Strings.b("WsSxcmg="))) {
         list.sort(Comparator.comparingDouble(AuraUtil::getArmor));
      }

      return list.get(0);
   }

   private void handleAttackAndRotation(LivingEntity t) {
      float currYaw = Manager.ROTATION.getYaw();
      float currPitch = Manager.ROTATION.getPitch();
      boolean canAttackNow = this.shouldAttack(t);
      boolean passRay = mc.player.isGliding() || !this.raycast.get() || this.checkRaycast(t, currYaw, currPitch);
      boolean noPotion = !Manager.FUNCTION_MANAGER.autoPotion.isActivePotion;
      if (this.mode.is("Grim AC Bypass")) {
         this.handleGrimACBypass(t, canAttackNow, passRay, noPotion);
      } else if (mc.player.isGliding() && this.elytraEnabled.get()) {
         this.handleElytraCombat(t, canAttackNow, passRay, noPotion);
      } else if (this.handleElytraRotation(t)) {
         if (canAttackNow && passRay && noPotion) {
            this.attackTarget(mc.player);
         }
      } else if (this.mode.is(Strings.b("xLBsZXJpIFNldml5ZQ==")) || this.mode.is(Strings.b("QWx0ZXJuYXRpZg=="))) {
         this.koopinVector(t, true);
         if (canAttackNow && passRay && noPotion) {
            this.attackTarget(mc.player);
         }
      } else if (this.mode.is("Dinamik")) {
         if (mc.player != null) {
            if (this.target == null) {
               this.randomYawOffset = 0.0F;
               this.randomPitchOffset = 0.0F;
               float centerYaw = mc.player.getYaw();
               float centerPitch = mc.player.getPitch();
               this.prevBodyYaw = this.bodyYaw;
               this.prevBodyPitch = this.bodyPitch;
               float yawDiff = MathHelper.wrapDegrees(centerYaw - this.bodyYaw);
               float pitchDiff = centerPitch - this.bodyPitch;
               float yawStep = MathHelper.clamp(yawDiff, -45.0F, 45.0F);
               float pitchStep = MathHelper.clamp(pitchDiff, -45.0F, 45.0F);
               this.bodyYaw += yawStep;
               this.bodyPitch = MathHelper.clamp(this.bodyPitch + pitchStep, -90.0F, 90.0F);
               this.prevHeadYaw = this.headYaw;
               this.prevHeadPitch = this.headPitch;
               yawDiff = MathHelper.wrapDegrees(centerYaw - this.headYaw);
               pitchDiff = centerPitch - this.headPitch;
               yawStep = MathHelper.clamp(yawDiff, -50.0F, 50.0F);
               pitchStep = MathHelper.clamp(pitchDiff, -50.0F, 50.0F);
               this.headYaw += yawStep;
               this.headPitch = MathHelper.clamp(this.headPitch + pitchStep, -90.0F, 90.0F);
            } else {
               this.randomUpdateTicks++;
               if (this.randomUpdateTicks >= 2) {
                  this.randomUpdateTicks = 0;
                  this.randomYawOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                  this.randomPitchOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.25F;
               }

               double x = this.target.getBoundingBox().getCenter().x;
               double y = this.target.getY();
               double z = this.target.getBoundingBox().getCenter().z;
               int randPoint = this.random.nextInt(4);
               switch (randPoint) {
                  case 0:
                     y += (double)this.target.getHeight() * 0.9;
                     break;
                  case 1:
                     y += (double)this.target.getHeight() * 0.75;
                     break;
                  case 2:
                     y += (double)this.target.getHeight() * 0.5;
                     break;
                  case 3:
                     y += (double)this.target.getHeight() * 0.25;
               }

               x += this.random.nextDouble() * 0.4 - 0.2;
               z += this.random.nextDouble() * 0.4 - 0.2;
               Vec3d targetPos = new Vec3d(x, y, z);
               Vec3d eyePos = mc.player.getEyePos();
               y = targetPos.x - eyePos.x;
               z = targetPos.y - eyePos.y;
               double deltaZ = targetPos.z - eyePos.z;
               double hDistance = Math.sqrt(y * y + deltaZ * deltaZ);
               double yaw = Math.atan2(deltaZ, y) * 180.0 / Math.PI - 90.0;
               double pitch = -(Math.atan2(z, hDistance) * 180.0 / Math.PI);
               Vec2f rot = new Vec2f((float)pitch, (float)yaw);
               this.prevBodyYaw = this.bodyYaw;
               this.prevBodyPitch = this.bodyPitch;
               float yawDiff = MathHelper.wrapDegrees(rot.y - this.bodyYaw);
               float pitchDiff = rot.x - this.bodyPitch;
               this.bodyYaw = this.bodyYaw + MathHelper.clamp(yawDiff, -45.0F, 45.0F);
               this.bodyPitch = MathHelper.clamp(this.bodyPitch + pitchDiff, -45.0F, 45.0F);
               this.prevHeadYaw = this.headYaw;
               this.prevHeadPitch = this.headPitch;
               float yawDiffx = MathHelper.wrapDegrees(rot.y - this.headYaw);
               float pitchDiffx = rot.x - this.headPitch;
               this.headYaw = this.headYaw + MathHelper.clamp(yawDiffx, -50.0F, 50.0F);
               this.headPitch = MathHelper.clamp(pitchDiffx, -50.0F, 50.0F);
               Vec2f headRotation = new Vec2f(rot.x + this.randomPitchOffset, rot.y + this.randomYawOffset);
               Manager.ROTATION.setSmooth(headRotation.y, headRotation.x, 0.8F, 60.0F, 90.0F, true);
               if (canAttackNow && passRay && noPotion) {
                  this.attackTarget(mc.player);
               }
            }
         }
      } else if (this.mode.is(Strings.b("VnVydcWfIE9wdGltaXpl"))) {
         Vec3d tp = this.predictPos(t);
         double yawToTarget = Math.toDegrees(Math.atan2(tp.z - mc.player.getZ(), tp.x - mc.player.getX())) - 90.0;
         double yawDiff = (double)Math.abs(MathHelper.wrapDegrees((float)yawToTarget - currYaw));
         if (yawDiff <= 180.0 && canAttackNow && passRay && noPotion) {
            this.attackTarget(mc.player);
         }

         Manager.ROTATION.set(mc.player.getYaw(), mc.player.getPitch());
      } else if (this.mode.is(Strings.b("QWvEsWPEsQ=="))) {
         if (canAttackNow && this.canAttack() && noPotion) {
            if (passRay) {
               this.attackTarget(mc.player);
            }

            this.lastHitMs = System.currentTimeMillis();
         }

         if (System.currentTimeMillis() - this.lastHitMs < 450L) {
            this.funtime(t);
         } else {
            long currentTime = System.currentTimeMillis();
            if (this.shakeStartTime == 0L) {
               this.shakeStartTime = currentTime;
            }

            float elapsedSec = (float)(currentTime - this.shakeStartTime) / 1000.0F;
            double angle = 15.079644737231007 * (double)elapsedSec;
            float yawOffset = (float)Math.sin(angle) * 24.0F;
            double angle2 = 0.5026548133391561 * (double)elapsedSec;
            double[] options = new double[]{5.0, 5.5, 5.8, 6.0};
            double randAmplitude = options[(int)(Math.random() * (double)options.length)];
            float yawOffset2 = (float)((double)((float)Math.sin(angle2)) * randAmplitude);
            float finalYaw = mc.player.getYaw() + yawOffset + yawOffset2;
            float finalPitch = 0.0F + yawOffset2;
            Manager.ROTATION.setSmooth(finalYaw, finalPitch, 1.0F, 20.0F, 10.0F, true);
         }
      } else if (!this.mode.is("Hassas")) {
         if (this.mode.is(Strings.b("SMSxemzEsQ=="))) {
            if (canAttackNow && this.canAttack() && passRay && noPotion) {
               this.attackTarget(mc.player);
               this.lastHitMs = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - this.lastHitMs < (long)this.snapSpeed.get().floatValue()) {
               this.setRotation(t, true);
            } else {
               Manager.ROTATION.set(mc.player.getYaw(), mc.player.getPitch());
            }
         } else {
            if (canAttackNow && passRay && noPotion) {
               this.attackTarget(mc.player);
            }

            this.setRotation(t, true);
         }
      } else {
         if (canAttackNow && this.canAttack() && passRay && noPotion) {
            this.hollyworld(t, true);
            this.attackTarget(mc.player);
         } else {
            this.hollyworld(t, false);
         }
      }
   }

   private void handleGrimACBypass(LivingEntity target, boolean canAttackNow, boolean passRay, boolean noPotion) {
      if (target != null) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.grimBypassLastSwitch > (long)(30000 + (int)(Math.random() * 30000.0))) {
            this.grimBypassLastSwitch = currentTime;
            this.grimBypassPattern = (int)(Math.random() * 3.0);
         }

         Vec3d aimPoint;
         switch (this.grimBypassPattern) {
            case 0: {
               Vec3d predictedPos = this.getAntiBacktrackPosition(target);
               aimPoint = this.getRandomizedAimPoint(target, predictedPos);
               break;
            }
            case 1: {
               Vec3d predictedPos = this.predictPos(target);
               aimPoint = this.addHumanJitterToAim(predictedPos, target);
               break;
            }
            case 2: {
               Vec3d predictedPos = target.getPos();
               aimPoint = this.getMultiPointAim(target);
               break;
            }
            default: {
               Vec3d predictedPos = this.predictPos(target);
               aimPoint = predictedPos.add(0.0, (double)target.getHeight() * 0.5, 0.0);
            }
         }

         Vec2f rotation = this.calculateHumanLikeRotation(aimPoint);
         float randomizedCPS = 8.0F + (float)(Math.random() * 12.0);
         long requiredDelay = (long)(1000.0F / randomizedCPS);
         if (currentTime - this.lastAttackTime < requiredDelay) {
            canAttackNow = false;
         }

         Manager.ROTATION
            .setSmooth(
               rotation.y,
               rotation.x,
               0.6F + (float)(Math.random() * 0.4F),
               120.0F + (float)(Math.random() * 60.0),
               20.0F + (float)(Math.random() * 25.0),
               true
            );
         if (canAttackNow && noPotion) {
            double currentDistance = AuraUtil.getDistance(target);
            double attackRange = this.distance.get().doubleValue() * 0.95;
            if (currentDistance <= attackRange && Math.random() > 0.1) {
               this.attackTarget(mc.player);
            }
         }
      }
   }

   private Vec3d getAntiBacktrackPosition(LivingEntity entity) {
      Vec3d current = entity.getPos();
      Vec3d velocity = new Vec3d(
         entity.getX() - entity.prevX, entity.getY() - entity.prevY, entity.getZ() - entity.prevZ
      );
      double randomFactor = 0.8 + Math.random() * 0.4;
      Vec3d predicted = current.add(velocity.multiply(randomFactor));
      double offsetX = Math.random() * 0.06 - 0.03;
      double offsetY = Math.random() * 0.04 - 0.02;
      double offsetZ = Math.random() * 0.06 - 0.03;
      return predicted.add(offsetX, offsetY, offsetZ);
   }

   private Vec3d getRandomizedAimPoint(LivingEntity entity, Vec3d basePos) {
      double randomValue = Math.random();
      double yOffset;
      if (randomValue < 0.3) {
         yOffset = (double)entity.getHeight() * 0.85;
      } else if (randomValue < 0.6) {
         yOffset = (double)entity.getHeight() * 0.55;
      } else if (randomValue < 0.85) {
         yOffset = (double)entity.getHeight() * 0.35;
      } else {
         yOffset = (double)entity.getHeight() * 0.15;
      }

      double offsetX = Math.random() * 0.15 - 0.075;
      double offsetZ = Math.random() * 0.15 - 0.075;
      return new Vec3d(basePos.x + offsetX, basePos.y + yOffset, basePos.z + offsetZ);
   }

   private Vec3d addHumanJitterToAim(Vec3d basePos, LivingEntity entity) {
      long time = System.currentTimeMillis();
      double jitterX = Math.sin((double)time * 0.003) * 0.02;
      double jitterY = Math.cos((double)time * 0.0027) * 0.015;
      double jitterZ = Math.sin((double)time * 0.0023) * 0.02;
      jitterX += Math.random() * 0.01 - 0.005;
      jitterY += Math.random() * 0.008 - 0.004;
      jitterZ += Math.random() * 0.01 - 0.005;
      return basePos.add(jitterX, jitterY + (double)entity.getHeight() * 0.5, jitterZ);
   }

   private Vec3d getMultiPointAim(LivingEntity entity) {
      long time = System.currentTimeMillis();
      int pointIndex = (int)(time / 200L % 5L);

      return entity.getPos().add(0.0, switch (pointIndex) {
         case 0 -> (double)entity.getHeight() * 0.9;
         case 1 -> (double)entity.getHeight() * 0.7;
         case 2 -> (double)entity.getHeight() * 0.5;
         case 3 -> (double)entity.getHeight() * 0.3;
         default -> (double)entity.getHeight() * 0.1;
      }, 0.0);
   }

   private Vec2f calculateHumanLikeRotation(Vec3d aimPoint) {
      Vec3d eyePos = mc.player.getEyePos();
      double dx = aimPoint.x - eyePos.x;
      double dy = aimPoint.y - eyePos.y;
      double dz = aimPoint.z - eyePos.z;
      double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
      float baseYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float basePitch = (float)(-Math.toDegrees(Math.atan2(dy, horizontalDistance)));
      long time = System.currentTimeMillis();
      float yawError = (float)Math.sin((double)time * 0.0015) * 0.8F;
      float pitchError = (float)Math.cos((double)time * 0.0012) * 0.6F;
      yawError += (float)(Math.random() * 0.4) - 0.2F;
      pitchError += (float)(Math.random() * 0.3) - 0.15F;
      return new Vec2f(MathHelper.clamp(basePitch + pitchError, -89.9F, 89.9F), baseYaw + yawError);
   }

   private boolean checkRaycast(LivingEntity target, float yaw, float pitch) {
      Entity raycastEntity = RayTraceUtil.getMouseOver(target, yaw, pitch, (double)this.distance.get().floatValue());
      return raycastEntity == target;
   }

   private void handleElytraCombat(LivingEntity target, boolean canAttackNow, boolean passRay, boolean noPotion) {
      if (target != null) {
         ElytraTarget ely = Manager.FUNCTION_MANAGER.elytraTarget;
         if (this.elytraMode.is("ElytraTarget") && ely != null && ely.state && this.elytraTargetIntegration.get()) {
            this.handleElytraTargetCombat(target, ely, canAttackNow, noPotion);
         } else if (this.elytraMode.is(Strings.b("SMSxemzEsQ=="))) {
            this.handleFastElytraCombat(target, canAttackNow, noPotion);
         } else {
            this.setElytraRotation(target);
            if (canAttackNow && noPotion && this.shouldElytraAttackWithHitbox(target)) {
               this.attackTarget(mc.player);
            }
         }
      }
   }

   private void handleFastElytraCombat(LivingEntity target, boolean canAttackNow, boolean noPotion) {
      Vec3d targetPos = this.getEnhancedElytraTargetPos(target);
      Vec3d eyePos = mc.player.getEyePos();
      double dx = targetPos.x - eyePos.x;
      double dy = targetPos.y - eyePos.y;
      double dz = targetPos.z - eyePos.z;
      double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
      float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float targetPitch = (float)(-Math.toDegrees(Math.atan2(dy, horizontalDistance)));
      Manager.ROTATION.setSmooth(targetYaw, targetPitch, this.elytraRotationSpeed.get().floatValue() * 0.3F, 200.0F, 60.0F, true);
      if (canAttackNow && noPotion && this.elytraFastHitbox.get() && this.checkFastElytraHitbox(target)) {
         this.attackTarget(mc.player);
      }
   }

   private boolean checkFastElytraHitbox(LivingEntity target) {
      if (target == null) {
         return false;
      } else {
         double currentDistance = AuraUtil.getDistance(target);
         double attackRange = this.elytraReach.get().doubleValue();
         if (currentDistance > attackRange) {
            return false;
         } else {
            float hitboxFactor = this.elytraHitboxSize.get().floatValue();
            float baseHitboxWidth = target.getWidth();
            float enhancedHitboxWidth = baseHitboxWidth * (1.0F + hitboxFactor);
            float yaw = Manager.ROTATION.getYaw();
            float pitch = Manager.ROTATION.getPitch();
            Vec3d eyePos = mc.player.getEyePos();
            List<Vec3d> hitboxPoints = new ArrayList<>();
            double height = (double)target.getHeight();

            for (int i = 0; i < 5; i++) {
               double yOffset = height * (double)i * 0.2;

               for (int j = 0; j < 8; j++) {
                  double angle = (double)j * Math.PI * 2.0 / 8.0;
                  double xOffset = Math.cos(angle) * (double)enhancedHitboxWidth * 0.8;
                  double zOffset = Math.sin(angle) * (double)enhancedHitboxWidth * 0.8;
                  hitboxPoints.add(target.getPos().add(xOffset, yOffset, zOffset));
               }
            }

            hitboxPoints.add(target.getPos().add(0.0, height * 0.5, 0.0));

            for (Vec3d point : hitboxPoints) {
               double dx = point.x - eyePos.x;
               double dy = point.y - eyePos.y;
               double dz = point.z - eyePos.z;
               double distToPoint = Math.sqrt(dx * dx + dz * dz);
               float pointYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
               float pointPitch = (float)(-Math.toDegrees(Math.atan2(dy, distToPoint)));
               float yawDiff = Math.abs(MathHelper.wrapDegrees(pointYaw - yaw));
               float pitchDiff = Math.abs(pointPitch - pitch);
               if (yawDiff < 50.0F && pitchDiff < 40.0F && distToPoint <= attackRange) {
                  return true;
               }
            }

            if (this.raycast.get()) {
               Entity raycastEntity = RayTraceUtil.getMouseOver(target, yaw, pitch, (double)this.elytraReach.get().floatValue());
               return raycastEntity == target;
            } else {
               return false;
            }
         }
      }
   }

   private void handleElytraTargetCombat(LivingEntity target, ElytraTarget ely, boolean canAttackNow, boolean noPotion) {
      if (target != null) {
         if (ely.mode.is(Strings.b("R2VsacWfbWnFnw=="))) {
            ely.overtakingElytra(target, false);
         } else {
            ely.targetDefault(target, false);
         }

         if (canAttackNow && noPotion && this.checkFastElytraHitbox(target)) {
            this.attackTarget(mc.player);
         }
      } else {
         this.handleFastElytraCombat(target, canAttackNow, noPotion);
      }
   }

   private Vec3d getEnhancedElytraTargetPos(LivingEntity entity) {
      Vec3d basePos = this.predictPos(entity);
      if (this.elytraPredict.get() && this.elytraPredictFactor.get().floatValue() > 0.0F) {
         Vec3d velocity = entity.getVelocity();
         float factor = this.elytraPredictFactor.get().floatValue();
         basePos = basePos.add(velocity.x * (double)factor, velocity.y * (double)factor, velocity.z * (double)factor);
      }

      float aimHeight = this.elytraAimHeight.get().floatValue();
      double targetY = basePos.y + (double)(entity.getHeight() * aimHeight);
      Vec3d hitboxCenter = entity.getBoundingBox().getCenter();
      double centerX = (basePos.x + hitboxCenter.x) / 2.0;
      double centerZ = (basePos.z + hitboxCenter.z) / 2.0;
      double randomOffset = Math.random() * 0.1 - 0.05;
      centerX += randomOffset;
      centerZ += randomOffset;
      return new Vec3d(centerX, targetY, centerZ);
   }

   private void setElytraRotation(LivingEntity entity) {
      Vec3d targetPos = this.getEnhancedElytraTargetPos(entity);
      Vec3d eyePos = mc.player.getEyePos();
      double dx = targetPos.x - eyePos.x;
      double dy = targetPos.y - eyePos.y;
      double dz = targetPos.z - eyePos.z;
      double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
      float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float targetPitch = (float)(-Math.toDegrees(Math.atan2(dy, horizontalDistance)));
      float rotationSpeed = this.getElytraRotationSpeed();
      Manager.ROTATION.setSmooth(targetYaw, targetPitch, rotationSpeed * 0.5F, 120.0F, 45.0F, true);
   }

   private float getElytraRotationSpeed() {
      String var1 = this.elytraMode.get();
      if (var1.equals(Strings.b("SMSxemzEsQ=="))) {
         return this.elytraRotationSpeed.get().floatValue();
      } else if (var1.equals(Strings.b("QcSfxLFy"))) {
         return Math.min(this.elytraRotationSpeed.get().floatValue(), 8.0F);
      } else {
         return 4.0F;
      }
   }

   private boolean shouldElytraAttackWithHitbox(LivingEntity target) {
      if (target == null) {
         return false;
      } else {
         double currentDistance = AuraUtil.getDistance(target);
         double attackRange = this.elytraReach.get().doubleValue();
         return !(currentDistance > attackRange);
      }
   }

   private Vec3d predictPos(LivingEntity entity) {
      if (entity == null) {
         return Vec3d.ZERO;
      } else {
         Vec3d currentPos = entity.getPos();
         if (this.predictionAmount.get().floatValue() <= 0.0F) {
            return currentPos;
         } else {
            boolean isFlyingTarget = entity.isGliding() || entity.isGliding() || entity.getVelocity().y != 0.0 || entity.fallDistance > 0.0F;
            if (this.predictFlyingTargetsOnly.get() && !isFlyingTarget) {
               return currentPos;
            } else {
               double velocityX = entity.getX() - entity.prevX;
               double velocityY = entity.getY() - entity.prevY;
               double velocityZ = entity.getZ() - entity.prevZ;
               velocityY *= 0.5;
               double velocityMagnitude = Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
               if (velocityMagnitude < 0.01) {
                  return currentPos;
               } else {
                  float predAmount = this.predictionAmount.get().floatValue();
                  double predictedX = currentPos.x + velocityX * (double)predAmount;
                  double predictedY = currentPos.y + velocityY * (double)predAmount;
                  double predictedZ = currentPos.z + velocityZ * (double)predAmount;
                  Vec3d predictedPos = new Vec3d(predictedX, predictedY, predictedZ);
                  TargetStrafe ts = Manager.FUNCTION_MANAGER.targetStrafe;
                  if (ts.state && ts.predictCheck.get()) {
                     float pr = ts.predict.get().floatValue();
                     if (pr > 0.0F) {
                        Vec3d v = entity.getVelocity();
                        predictedPos = predictedPos.add(v.x * (double)pr, v.y * (double)pr, v.z * (double)pr);
                     }
                  }

                  return predictedPos;
               }
            }
         }
      }
   }

   private void setRotation(LivingEntity entity, boolean applyGcd) {
      Vec3d predictedPos = this.predictPos(entity);
      double dx = predictedPos.x - mc.player.getX();
      double dy = predictedPos.y
         + (double)entity.getEyeHeight(entity.getPose()) / 2.0
         - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double dz = predictedPos.z - mc.player.getZ();
      float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(dy, Math.hypot(dx, dz))));
      Manager.ROTATION.setSmooth(yaw, pitch, 1.2F, 180.0F, 15.0F, applyGcd);
   }

   private void koopinVector(LivingEntity entity, boolean attackContext) {
      Vec3d basePos = this.predictPos(entity);
      Vec3d head = basePos.add(0.0, (double)entity.getHeight(), 0.0);
      Vec3d chest = basePos.add(0.0, (double)(entity.getStandingEyeHeight() / 2.0F), 0.0);
      Vec3d legs = basePos.add(0.0, 0.05, 0.0);
      Vec3d[] points = new Vec3d[]{head, chest, legs};
      float bestPitchDelta = Float.MAX_VALUE;
      Vec3d best = chest;
      float currPitch = Manager.ROTATION.getPitch();
      float currYaw = Manager.ROTATION.getYaw();

      for (Vec3d p : points) {
         Vec3d eye = mc.player.getEyePos();
         double dx = p.x - eye.x;
         double dy = p.y - eye.y;
         double dz = p.z - eye.z;
         float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
         float pitch = (float)(-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));
         float pitchDelta = Math.abs(pitch - currPitch);
         if (pitchDelta < bestPitchDelta) {
            bestPitchDelta = pitchDelta;
            best = p;
         }
      }

      Vec3d eye = mc.player.getEyePos();
      double dx = best.x - eye.x;
      double dy = best.y - eye.y;
      double dz = best.z - eye.z;
      double dst = Math.sqrt(dx * dx + dz * dz);
      float yawTo = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float pitchTo = (float)(-Math.toDegrees(Math.atan2(dy, dst)));
      float yawDelta = MathHelper.wrapDegrees(yawTo - currYaw);
      float pitchDelta = pitchTo - currPitch;
      float addYaw = Math.min(Math.max(Math.abs(yawDelta), 1.0F), 80.0F);
      if (Math.abs(addYaw) <= 3.0F) {
         addYaw = 3.1F;
      }

      float addPitch = Math.max(attackContext ? Math.abs(pitchDelta) : 1.0F, 2.0F);
      float ny = currYaw + (yawDelta > 0.0F ? addYaw : -addYaw);
      float np = MathHelper.clamp(currPitch + (pitchDelta > 0.0F ? addPitch : -addPitch), -90.0F, 90.0F);
      Manager.ROTATION.set(ny, np);
   }

   private void funtime(LivingEntity entity) {
      Vec3d predictedPos = this.predictPos(entity);
      Vec3d eye = mc.player.getEyePos();
      float[] points = new float[]{0.82F, 0.67F, 0.43F, 0.27F};
      float mul = points[(int)(System.currentTimeMillis() / 180L % (long)points.length)];
      Vec3d targetPos = new Vec3d(predictedPos.x, predictedPos.y + (double)(entity.getHeight() * mul), predictedPos.z);
      double halfWidth = (double)entity.getWidth() / 2.0;
      double sideOffset = this.swingSideRight ? halfWidth * 1.2F : -halfWidth * 1.0;
      double yawToEntity = Math.atan2(predictedPos.z - mc.player.getZ(), predictedPos.x - mc.player.getX());
      double offsetX = Math.cos(yawToEntity + (Math.PI / 2)) * sideOffset;
      double offsetZ = Math.sin(yawToEntity + (Math.PI / 2)) * sideOffset;
      targetPos = targetPos.add(offsetX, 0.0, offsetZ);
      double dx = targetPos.x - eye.x;
      double dy = targetPos.y - eye.y;
      double dz = targetPos.z - eye.z;
      double dist = Math.sqrt(dx * dx + dz * dz);
      float baseYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float basePitch = (float)(-Math.toDegrees(Math.atan2(dy, dist)));
      long now = System.currentTimeMillis();
      if (now - this.lastSwitch > (long)(200 + this.random.nextInt(250))) {
         this.lastSwitch = now;
         this.swingSideRight = !this.swingSideRight;
         float distanceFactor = (float)MathHelper.clamp(dist / 6.0, 0.4F, 1.0);
         float maxDeviation = 4.0F * distanceFactor;
         this.jitterYawTarget = (this.swingSideRight ? maxDeviation : -maxDeviation) + (float)(this.random.nextGaussian() * 0.6F);
      }

      float diff = this.jitterYawTarget - this.jitterYaw;
      this.jitterYawSpeed += diff * 0.05F;
      this.jitterYawSpeed *= 0.88F;
      this.jitterYaw = this.jitterYaw + this.jitterYawSpeed;
      this.jitterYaw *= 0.985F;
      if (now - this.lastBreathChange > (long)(2000 + this.random.nextInt(1500))) {
         this.lastBreathChange = now;
         this.swaySpeed = 0.035F + this.random.nextFloat() * 0.02F;
         this.swayAmplitude = 2.0F + this.random.nextFloat() * 1.2F;
      }

      this.swayPhase = this.swayPhase + this.swaySpeed;
      float sway = (float)Math.sin((double)this.swayPhase) * this.swayAmplitude;
      float totalYawOffset = (float)MathHelper.clamp((double)(this.jitterYaw + sway), -halfWidth * 8.5, halfWidth * 8.5);
      this.microJitter = this.microJitter + (this.random.nextFloat() - 0.5F) * 0.25F;
      this.microJitter *= 0.85F;
      float finalYaw = baseYaw + totalYawOffset + this.microJitter;
      float finalPitch = basePitch + (float)Math.sin((double)(this.swayPhase * 0.8F)) * 0.5F;
      Manager.ROTATION.setSmooth(finalYaw, finalPitch, 1.1F, 180.0F, 15.0F, true);
   }

   private void hollyworld(LivingEntity entity, boolean force) {
      Vec3d predictedPos = this.predictPos(entity);
      Vec3d eye = mc.player.getEyePos();
      float[] points = new float[]{0.85F, 0.65F, 0.35F, 0.25F};
      float mul = points[(int)(System.nanoTime() % (long)points.length)];
      Vec3d aim = new Vec3d(predictedPos.x, predictedPos.y + (double)(entity.getHeight() * mul), predictedPos.z);
      double dx = aim.x - eye.x;
      double dy = aim.y - eye.y;
      double dz = aim.z - eye.z;
      double hd = Math.sqrt(dx * dx + dz * dz);
      float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(dy, hd)));
      if (force) {
         Manager.ROTATION.set(yaw, MathHelper.clamp(pitch, -89.9F, 89.9F));
      } else {
         Manager.ROTATION.setSmooth(yaw, pitch, 0.25F, 45.0F, 12.0F, true);
      }
   }

   private boolean handleElytraRotation(LivingEntity t) {
      ElytraTarget ely = Manager.FUNCTION_MANAGER.elytraTarget;
      if (ely != null && ely.state && mc.player.isGliding() && !this.elytraEnabled.get()) {
         if (ely.mode.is(Strings.b("R2VsacWfbWnFnw=="))) {
            ely.overtakingElytra(t, false);
         } else {
            ely.targetDefault(t, false);
         }

         return true;
      } else {
         return false;
      }
   }

   public void attackTarget(PlayerEntity player) {
      boolean sprintStop = false;
      boolean canStartSprint = mc.player.input.movementForward > 0.0F
         && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
         && !mc.player.isGliding()
         && !mc.player.isUsingItem()
         && !mc.player.horizontalCollision
         && mc.player.getHungerManager().getFoodLevel() > 6
         && !mc.player.isSneaking();
      this.lastAttackTime = System.currentTimeMillis();
      if (this.setting.get(Strings.b("S2Fsa2FuIETDvMWfw7xy")) && mc.player.isBlocking()) {
         mc.interactionManager.stopUsingItem(mc.player);
      }

      if (!this.sprintreset.is("Legit") || !mc.player.isSprinting() && !this.canAttack() || !mc.player.isSprinting()) {
         if (this.sprintreset.is("Rage") && ((ClientPlayerEntityAccessor)mc.player).getLastSprinting()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            mc.player.setSprinting(false);
            sprintStop = true;
         }

         mc.interactionManager.attackEntity(player, this.target);
         mc.player.swingHand(Hand.MAIN_HAND);
         ElytraTarget elytraTarget = Manager.FUNCTION_MANAGER.elytraTarget;
         if (elytraTarget != null && elytraTarget.mode.is(Strings.b("R2VsacWfbWnFnw=="))) {
            elytraTarget.trueFireWork = true;
            if (elytraTarget.prefer.get()) {
               elytraTarget.nextPhase(this.target);
            }
         }

         if (this.setting.get(Strings.b("S2Fsa2FuIEvEsXLEsWPEsQ=="))) {
            this.shieldBreaker(false);
         }

         if (this.sprintreset.is("Rage") && sprintStop && canStartSprint && ((ClientPlayerEntityAccessor)mc.player).getLastSprinting()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            mc.player.setSprinting(true);
         }
      }
   }

   private boolean shouldAttack(LivingEntity e) {
      if (e == null) {
         return false;
      } else {
         long currentTime = System.currentTimeMillis();
         long timeSinceLastAttack = currentTime - this.lastAttackTime;
         if (this.mode.is("Grim AC Bypass")) {
            float randomizedCPS = 8.0F + (float)(Math.random() * 12.0);
            long requiredDelay = (long)(1000.0F / randomizedCPS);
            if (timeSinceLastAttack < requiredDelay) {
               return false;
            }
         } else {
            float requiredCPS;
            if (mc.player != null && mc.player.isGliding() && this.elytraEnabled.get()) {
               requiredCPS = this.elytraCPS.get().floatValue();
            } else {
               requiredCPS = this.normalCPS.get().floatValue();
            }

            long requiredDelay = (long)(1000.0F / requiredCPS);
            if (timeSinceLastAttack < requiredDelay) {
               return false;
            }
         }

         double currentDistance = AuraUtil.getDistance(e);
         double attackRange;
         if (mc.player != null && mc.player.isGliding() && this.elytraEnabled.get()) {
            attackRange = this.elytraReach.get().doubleValue();
         } else {
            attackRange = this.distance.get().doubleValue();
         }

         return currentDistance > attackRange ? false : this.canAttack();
      }
   }

   private boolean canAttack() {
      FlowObfuscator.fakeHandler();

      // Fake path - never executes
      if (FlowObfuscator.opaqueFalse()) {
         return mc.player.age % NumberGuard.i(2) == 0;
      }

      int checks = runAttackChecks();
      FlowObfuscator.fakeHandler();

      return LogicSplit.threshold(checks, NumberGuard.i(3));
   }

   private int runAttackChecks() {
      int score = 0;
      FlowObfuscator.fakeHandler();

      // Eating check
      boolean eating = LogicSplit.and(
         this.noAttackIfEat.get(),
         LogicSplit.and(mc.player.isUsingItem(), LogicSplit.not(mc.player.getActiveItem().isOf(Items.SHIELD)))
      );
      score += LogicSplit.score(LogicSplit.not(eating));

      // Cooldown check
      boolean hasMace = mc.player.getMainHandStack().isOf(Items.MACE);
      float cooldown = mc.player.getAttackCooldownProgress(mc.getRenderTickCounter().getTickDelta(true));
      boolean cooldownReady = LogicSplit.or(hasMace, cooldown >= 0.9F);
      score += LogicSplit.score(cooldownReady);

      // Fake check
      if (FlowObfuscator.opaqueFalse()) {
         score += mc.player.getInventory().selectedSlot;
      }

      // Restriction checks
      boolean restricted = checkRestrictions();
      score += LogicSplit.score(LogicSplit.not(restricted));

      // Critical check
      score += checkCriticalCondition();

      FlowObfuscator.fakeHandler();
      return score;
   }

   private boolean checkRestrictions() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return mc.player.isSneaking();
      }

      // Simple boolean checks instead of varargs
      if (mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) return true;
      if (mc.player.hasStatusEffect(StatusEffects.LEVITATION)) return true;
      if (mc.player.hasStatusEffect(StatusEffects.SLOW_FALLING)) return true;
      if (mc.player.isInLava()) return true;
      if (mc.player.inPowderSnow) return true;
      if (mc.player.isClimbing()) return true;
      if (mc.player.hasVehicle()) return true;
      if (mc.player.getAbilities().flying) return true;
      if (mc.player.isInFluid() && !mc.options.jumpKey.isPressed()) return true;
      if (MoveUtil.isInWeb()) return true;

      return false;
   }

   private int checkCriticalCondition() {
      FlowObfuscator.fakeHandler();

      if (!this.setting.get("Sadece Kritik")) return 1;
      if (checkRestrictions()) return 1;

      // Simple boolean checks instead of varargs
      boolean needSpace = this.onlySpaceCritical.get() && mc.player.isOnGround() && !mc.options.jumpKey.isPressed();
      boolean falling = !mc.player.isOnGround() && mc.player.fallDistance > 0.0F;

      if (FlowObfuscator.opaqueFalse()) {
         return mc.player.age % NumberGuard.i(5);
      }

      return (needSpace || falling) ? 1 : 0;
   }

   private boolean shieldBreaker(boolean instant) {
      int axeSlot = InventoryUtil.getAxe().slot();
      if (axeSlot == -1) {
         return false;
      } else if (!(this.target instanceof PlayerEntity)) {
         return false;
      } else if (!((PlayerEntity)this.target).isUsingItem() && !instant) {
         return false;
      } else if (((PlayerEntity)this.target).getOffHandStack().getItem() != Items.SHIELD
         && ((PlayerEntity)this.target).getMainHandStack().getItem() != Items.SHIELD) {
         return false;
      } else {
         if (axeSlot >= 9) {
            mc.interactionManager
               .clickSlot(mc.player.currentScreenHandler.syncId, axeSlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
            mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            mc.interactionManager.attackEntity(mc.player, this.target);
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.interactionManager
               .clickSlot(mc.player.currentScreenHandler.syncId, axeSlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
            mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
         } else {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(axeSlot));
            mc.interactionManager.attackEntity(mc.player, this.target);
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
         }

         return true;
      }
   }

   public LivingEntity getTarget() {
      return this.target;
   }

   public boolean isEnabled() {
      return this.state;
   }

   public boolean isCorrectionEnabled() {
      return this.correction.get();
   }
}
