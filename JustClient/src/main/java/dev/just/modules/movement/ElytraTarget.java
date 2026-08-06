package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.color.ColorUtil;
import dev.just.util.move.NetworkUtils;
import dev.just.util.player.InventoryUtil;
import dev.just.util.player.TimerUtil;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.Heightmap;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ElytraTarget",
   desc = "RWx5dHJhIGlsZSByYWtpcGxlcmUgb3RvbWF0aWsgZm9rdXNsYW7EsXIh",
   type = Type.Move
)
public class ElytraTarget extends Function {
   public final ModeSetting mode = new ModeSetting(I0O1l0I1.b("VcOndcWfIE1vZHU="), I0O1l0I1.b("Tm9ybWFs"), I0O1l0I1.b("Tm9ybWFs"), I0O1l0I1.b("R2VsacWfbWnFnw=="));
   private final SliderSetting sila = new SliderSetting(I0O1l0I1.b("VcOndcWfIEjEsXrEsQ=="), 8.0, 5.0, 40.0, 1.0, () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")));
   private final SliderSetting silaTime = new SliderSetting(I0O1l0I1.b("TWFuZXZyYSBTw7xyZXNp"), 400.0, 200.0, 1000.0, 10.0, () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")));
   private final BindBooleanSetting perelet = new BindBooleanSetting(I0O1l0I1.b("SGVkZWYgVGFobWluaQ=="), true);
   private final SliderSetting predict = new SliderSetting(I0O1l0I1.b("VGFobWluIEhhc3Nhc2l5ZXRp"), 2.6F, 0.1F, 6.0, 0.1F, () -> this.perelet.get());
   private final BooleanSetting leaveHP = new BooleanSetting(I0O1l0I1.b("T3RvbWF0aWsgS2HDp8SxxZ8="), true, () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")));
   private final MultiSetting leaveList = new MultiSetting(
      I0O1l0I1.b("S2HDp8SxxZ8gS2/Fn3VsbGFyxLE="),
      Arrays.asList(I0O1l0I1.b("RMO8xZ/DvGsgQ2Fu"), I0O1l0I1.b("RcWfeWEgS3VsbGFuxLFya2Vu"), I0O1l0I1.b("S2Fsa2FuIFRha8SxbMSxeWtlbg==")),
      List.of(I0O1l0I1.b("RMO8xZ/DvGsgQ2Fu"), I0O1l0I1.b("RcWfeWEgS3VsbGFuxLFya2Vu")),
      () -> this.leaveHP.get() && this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final BindBooleanSetting resolver = new BindBooleanSetting(
      I0O1l0I1.b("VnVydcWfIFNhcHTEsXLEsWPEsQ=="), true, I0O1l0I1.b("SGF2YWRhIHNhcnPEsWxhcmFrIHJha2liaW4gc2l6ZSB2dXJtYXPEsW7EsSB6b3JsYcWfdMSxcsSxcg=="), () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final SliderSetting resolverStrength = new SliderSetting(
      I0O1l0I1.b("U2FwdMSxcm1hIMWeaWRkZXRp"), 12.0, 4.0, 30.0, 1.0, () -> this.resolver.get() && this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   public final BindBooleanSetting prefer = new BindBooleanSetting(
      I0O1l0I1.b("RGluYW1payBZw7Zu"), true, I0O1l0I1.b("SGFzYXIgYWxkxLHEn8SxbsSxemRhIG90b21hdGlrIG9sYXJhayB1w6d1xZ8gecO2bsO8bsO8IGRlxJ9pxZ90aXJpcg=="), () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final MultiSetting preferDir = new MultiSetting(
      I0O1l0I1.b("S2HDp8SxxZ8gWcO2bmxlcmk="),
      Arrays.asList(I0O1l0I1.b("S3V6ZXk="), I0O1l0I1.b("R8O8bmV5"), I0O1l0I1.b("QmF0xLE="), I0O1l0I1.b("RG/En3U="), I0O1l0I1.b("WXVrYXLEsQ==")),
      List.of(I0O1l0I1.b("S3V6ZXk="), I0O1l0I1.b("R8O8bmV5"), I0O1l0I1.b("QmF0xLE="), I0O1l0I1.b("RG/En3U="), I0O1l0I1.b("WXVrYXLEsQ=="), I0O1l0I1.b("QcWfYcSfxLE=")),
      () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")) && this.prefer.get()
   );
   private final BooleanSetting avoidWalls = new BooleanSetting(
      I0O1l0I1.b("RW5nZWxkZW4gS2HDp8Sxbm1h"), true, I0O1l0I1.b("w5Zuw7xuw7x6ZGUgYmlyIGVuZ2VsIHZleWEgZHV2YXIgdmFyc2Egcm90YXnEsSBkZcSfacWfdGlyaXI="), () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final BooleanSetting firework = new BooleanSetting(I0O1l0I1.b("T3RvbWF0aWsgSGF2YWkgRmnFn2Vr"), true, () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")));
   private final SliderSetting fireworkTime = new SliderSetting(
      I0O1l0I1.b("RmnFn2VrIEt1bGxhbsSxbSBIxLF6xLE="), 800.0, 100.0, 2000.0, 10.0, () -> this.firework.get() && this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final BooleanSetting fakelags = new BooleanSetting("Fake Lagg", true, () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")));
   private final SliderSetting fakelagsDistance = new SliderSetting(
      I0O1l0I1.b("R2VjaWttZSBNZXNhZmVzaQ=="), 10.0, 5.0, 20.0, 0.5, () -> this.fakelags.get() && this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final SliderSetting fakelagsMaxDistance = new SliderSetting(
      I0O1l0I1.b("TWFrc2ltdW0gVGFraXAgTWVzYWZlc2k="), 30.0, 15.0, 50.0, 1.0, () -> this.fakelags.get() && this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw=="))
   );
   private final CopyOnWriteArrayList<Packet<?>> packetBuffer = new CopyOnWriteArrayList<>();
   private boolean isAccumulatingPackets = false;
   private boolean wasInFarRange = false;
   private LivingEntity currentTarget = null;
   private Vec3d defensivePos = null;
   private final TimerUtil defensiveTimer = new TimerUtil();
   private final BooleanSetting visual = new BooleanSetting(I0O1l0I1.b("UG96aXN5b251IGfDtnN0ZXI="), false, () -> this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")) || this.mode.is(I0O1l0I1.b("Tm9ybWFs")));
   boolean defensiveActive;
   boolean lastDefensive;
   private int rotationPhase = 0;
   private int directionIndex = 0;
   private long lastHitTime = 0L;
   private LivingEntity lastTarget = null;
   private long targetLastAttackTime = 0L;
   private final Map<String, Vec3d> namedDirections = Map.of(
      I0O1l0I1.b("S3V6ZXk="),
      new Vec3d(0.0, 0.0, -1.0),
      I0O1l0I1.b("R8O8bmV5"),
      new Vec3d(0.0, 0.0, 1.0),
      I0O1l0I1.b("QmF0xLE="),
      new Vec3d(-1.0, 0.0, 0.0),
      I0O1l0I1.b("RG/En3U="),
      new Vec3d(1.0, 0.0, 0.0),
      I0O1l0I1.b("WXVrYXLEsQ=="),
      new Vec3d(0.0, 1.0, 0.0),
      I0O1l0I1.b("QcWfYcSfxLE="),
      new Vec3d(0.0, -1.0, 0.0)
   );
   private List<Vec3d> airDirections = new ArrayList<>();
   private final TimerUtil fireworkTimer = new TimerUtil();
   private final TimerUtil sanyaEblan = new TimerUtil();
   public boolean trueFireWork = false;
   private int x;
   private int y;
   private int z;

   public ElytraTarget() {
      this.addSettings(
         new Setting[]{
            this.mode,
            this.sila,
            this.silaTime,
            this.perelet,
            this.predict,
            this.leaveHP,
            this.leaveList,
            this.resolver,
            this.resolverStrength,
            this.prefer,
            this.preferDir,
            this.avoidWalls,
            this.firework,
            this.fireworkTime,
            this.fakelags,
            this.fakelagsDistance,
            this.fakelagsMaxDistance,
            this.visual
         }
      );
   }

   @Override
   public void onEnable() {
      this.rebuildAirDirections();
      this.packetBuffer.clear();
      this.isAccumulatingPackets = false;
      this.wasInFarRange = false;
      this.currentTarget = null;
      this.defensivePos = null;
   }

   private void rebuildAirDirections() {
      this.airDirections.clear();
      boolean isFlying = mc.player != null && mc.player.isGliding();

      for (String name : this.preferDir.getAvailableModes()) {
         if (this.preferDir.get(name) && (!name.equals(I0O1l0I1.b("QcWfYcSfxLE=")) || isFlying)) {
            Vec3d dir = this.namedDirections.get(name);
            if (dir != null) {
               this.airDirections.add(dir);
            }
         }
      }

      if (this.airDirections.isEmpty()) {
         for (Entry<String, Vec3d> entry : this.namedDirections.entrySet()) {
            if (!entry.getKey().equals(I0O1l0I1.b("QcWfYcSfxLE=")) || isFlying) {
               this.airDirections.add(entry.getValue());
            }
         }
      }

      if (this.directionIndex >= this.airDirections.size()) {
         this.directionIndex = 0;
      }
   }

   @Override
   public void onEvent(Event event) {
      if (this.mode.is(I0O1l0I1.b("R2VsacWfbWnFnw==")) && mc.player.isGliding()) {
         if (event instanceof EventPacket packetEvent) {
            this.handlePacketEvent(packetEvent);
         }

         if (event instanceof EventUpdate) {
            this.updateFakeLagsState();
            if (this.firework.get() && this.fireworkTimer.hasTimeElapsed(this.fireworkTime.get().longValue())) {
               if (this.trueFireWork) {
                  InventoryUtil.inventorySwapClick2(Items.FIREWORK_ROCKET, true, false);
               }

               this.fireworkTimer.reset();
            }
         }

         if (event instanceof EventUpdate) {
            this.defensiveActive = this.shouldLeave();
            this.lastDefensive = this.defensiveActive;
            if (this.resolver.get() && this.rotationPhase != 0) {
               float evadePitch = (float)(Math.sin((double)System.currentTimeMillis() / 100.0) * (double)this.resolverStrength.get().floatValue());
               float evadeYaw = (float)(Math.cos((double)System.nanoTime() / 100.0) * (double)this.resolverStrength.get().floatValue());
               float cy = Manager.ROTATION.getYaw();
               float cp = Manager.ROTATION.getPitch();
               Manager.ROTATION.set(cy + evadeYaw, MathHelper.clamp(cp + evadePitch, -89.9F, 89.9F));
            }

            if (this.rotationPhase != 0 && (float)(System.currentTimeMillis() - this.lastHitTime) > this.silaTime.get().floatValue()) {
               this.rotationPhase = 0;
            }
         }

         if (event instanceof EventRender3D render) {
            if (this.visual.get() && mc.player.isGliding() && Manager.FUNCTION_MANAGER.attackAura.target != null) {
               BlockPos pos = new BlockPos(this.x, this.y, this.z);
               BlockState state = mc.world.getBlockState(pos);
               Box box = new Box(pos).contract(0.01);
               RenderUtil.render3D.drawHoleOutline(box, ColorUtil.rgba(128, 255, 128, 255), 2.0F);
            }

            if (this.defensivePos != null && this.fakelags.get() && this.isAccumulatingPackets) {
               RenderAddon.renderFakePlayer(this.defensivePos, this.fakelags.get(), this.isAccumulatingPackets, render);
            }
         }
      }
   }

   private void handlePacketEvent(EventPacket packetEvent) {
      if (this.fakelags.get() && packetEvent.getPacket() instanceof Packet) {
         if (this.isAccumulatingPackets && packetEvent.isSendPacket() && !(packetEvent.getPacket() instanceof KeepAliveC2SPacket)) {
            this.packetBuffer.add(packetEvent.getPacket());
            packetEvent.setCancel(true);
         }
      }
   }

   private void updateFakeLagsState() {
      if (!this.fakelags.get()) {
         if (this.isAccumulatingPackets) {
            this.sendBufferedPackets();
            this.isAccumulatingPackets = false;
         }
      } else {
         boolean isFlying = mc.player.isGliding();
         LivingEntity target = Manager.FUNCTION_MANAGER.attackAura.target;
         boolean hasTarget = target != null;
         if (target != null && !target.equals(this.currentTarget)) {
            this.currentTarget = target;
            if (this.isAccumulatingPackets) {
               this.sendBufferedPackets();
            }
         }

         if (isFlying && hasTarget && !this.perelet.get() && !this.defensiveActive) {
            double distanceToTarget = mc.player.getPos().distanceTo(target.getPos());
            double triggerDistance = this.fakelagsDistance.get().doubleValue();
            double maxDistance = this.fakelagsMaxDistance.get().doubleValue();
            if (distanceToTarget > maxDistance) {
               if (this.isAccumulatingPackets) {
                  this.sendBufferedPackets();
                  this.isAccumulatingPackets = false;
               }

               this.wasInFarRange = false;
               this.defensivePos = null;
            } else {
               if (distanceToTarget > triggerDistance && distanceToTarget <= maxDistance) {
                  if (!this.isAccumulatingPackets) {
                     this.isAccumulatingPackets = true;
                     this.defensivePos = mc.player.getPos();
                     this.defensiveTimer.reset();
                  }

                  this.wasInFarRange = true;
               } else if (distanceToTarget <= triggerDistance && this.wasInFarRange) {
                  this.sendBufferedPackets();
                  this.isAccumulatingPackets = false;
                  this.wasInFarRange = false;
                  this.defensivePos = null;
               } else if (distanceToTarget <= triggerDistance) {
                  this.isAccumulatingPackets = false;
                  this.wasInFarRange = false;
                  this.defensivePos = null;
               }

               if (this.isAccumulatingPackets && this.defensiveTimer.hasTimeElapsed(1500L)) {
                  this.sendBufferedPackets();
                  this.isAccumulatingPackets = true;
                  this.defensiveTimer.reset();
               }
            }
         } else {
            if (this.isAccumulatingPackets) {
               this.sendBufferedPackets();
               this.isAccumulatingPackets = false;
            }

            this.wasInFarRange = false;
            this.defensivePos = null;
         }
      }
   }

   private void sendBufferedPackets() {
      if (!this.packetBuffer.isEmpty()) {
         for (Packet<?> packet : this.packetBuffer) {
            NetworkUtils.sendSilentPacket(packet);
         }

         this.packetBuffer.clear();
      }
   }

   public void onTargetAttack(LivingEntity attacker) {
      if (attacker != null && attacker.equals(this.lastTarget)) {
         this.targetLastAttackTime = System.currentTimeMillis();
         if (this.fakelags.get()) {
            this.sendBufferedPackets();
            this.isAccumulatingPackets = false;
            this.wasInFarRange = false;
            this.defensivePos = null;
         }
      }
   }

   @Override
   public void onDisable() {
      this.sendBufferedPackets();
      this.isAccumulatingPackets = false;
      this.wasInFarRange = false;
      this.currentTarget = null;
      this.defensivePos = null;
   }

   private boolean shouldLeave() {
      boolean lowHP = mc.player.getHealth() <= 4.0F && this.leaveList.get(I0O1l0I1.b("QXogY2Fu"));
      boolean usingItem = mc.player.isUsingItem()
         && !mc.player.getActiveItem().isOf(Items.SHIELD)
         && this.leaveList.get(I0O1l0I1.b("RcWfeWEga3VsbGFuxLFya2Vu"));
      return this.leaveHP.get() && (lowHP || usingItem);
   }

   public void nextPhase(LivingEntity target) {
      this.lastTarget = target;
      this.lastHitTime = System.currentTimeMillis();
      this.rotationPhase = 1;
      if (this.fakelags.get()) {
         this.sendBufferedPackets();
         this.isAccumulatingPackets = false;
         this.wasInFarRange = false;
         this.defensivePos = null;
      }

      boolean isFlying = mc.player != null && mc.player.isGliding();
      List<Vec3d> newDirections = new ArrayList<>();

      for (String name : this.preferDir.getAvailableModes()) {
         if (this.preferDir.get(name) && (!name.equals(I0O1l0I1.b("QcWfYcSfxLE=")) || isFlying)) {
            Vec3d dir = this.namedDirections.get(name);
            if (dir != null) {
               newDirections.add(dir);
            }
         }
      }

      if (newDirections.isEmpty()) {
         for (Entry<String, Vec3d> entry : this.namedDirections.entrySet()) {
            if (!entry.getKey().equals(I0O1l0I1.b("QcWfYcSfxLE=")) || isFlying) {
               newDirections.add(entry.getValue());
            }
         }
      }

      int oldIndexInNew = -1;
      Vec3d oldDir = null;
      if (!this.airDirections.isEmpty() && this.directionIndex >= 0 && this.directionIndex < this.airDirections.size()) {
         oldDir = this.airDirections.get(this.directionIndex);
      }

      if (oldDir != null) {
         for (int i = 0; i < newDirections.size(); i++) {
            Vec3d v = newDirections.get(i);
            if (Double.compare(v.x, oldDir.x) == 0
               && Double.compare(v.y, oldDir.y) == 0
               && Double.compare(v.z, oldDir.z) == 0) {
               oldIndexInNew = i;
               break;
            }
         }
      }

      int size = newDirections.size();
      int candidateIndex = oldIndexInNew == -1 ? 0 : (oldIndexInNew + 1) % size;
      int attempts = 0;
      int chosenIndex = candidateIndex;

      while (attempts < size) {
         Vec3d dir = newDirections.get(candidateIndex);
         if (this.resolver.get() && dir.y < 0.0 && !mc.player.isGliding()) {
            candidateIndex = (candidateIndex + 1) % size;
            attempts++;
         } else {
            Vec3d candidatePos = target.getPos().add(dir.normalize().multiply((double)this.sila.get().floatValue()));
            Vec3d tryBetter = this.findClosestValidPosAround(candidatePos, 4.5);
            Vec3d checkPos = tryBetter != null
               ? tryBetter
               : new Vec3d(candidatePos.x, Math.max(candidatePos.y, mc.player.getY() + 6.0), candidatePos.z);
            if ((!this.avoidWalls.get() || !this.isObstructed(mc.player.getEyePos(), checkPos)) && this.isValidFlyPosition(checkPos)) {
               chosenIndex = candidateIndex;
               break;
            }

            candidateIndex = (candidateIndex + 1) % size;
            attempts++;
         }
      }

      this.airDirections = newDirections;
      this.directionIndex = chosenIndex;
   }

   public void overtakingElytra(LivingEntity base, boolean attack) {
      boolean leave = this.shouldLeave();
      boolean inEvasion = this.rotationPhase != 0 || leave;
      Vec3d targetPos = base.getPos().add(0.0, (double)base.getHeight() / 2.0, 0.0);
      Vec3d modifiedPos;
      if (inEvasion) {
         Vec3d dir = this.airDirections.get(this.directionIndex);
         double strength = (double)this.sila.get().floatValue();
         Vec3d candidatePos = targetPos.add(dir.normalize().multiply(strength));
         Vec3d tryBetter = this.findClosestValidPosAround(candidatePos, 10.0);
         if (tryBetter != null) {
            this.trueFireWork = true;
            candidatePos = tryBetter;
         } else {
            candidatePos = new Vec3d(
               candidatePos.x, Math.max(candidatePos.y, mc.player.getY() + 2.0), candidatePos.z
            );
         }

         modifiedPos = candidatePos;
         if (mc.player.getPos().distanceTo(candidatePos) < 1.0) {
            this.rotationPhase = 0;
         }
      } else {
         double bps;
         try {
            bps = Double.parseDouble(ClientManager.getBps(base));
         } catch (Exception var15) {
            bps = 0.0;
         }

         boolean canPredict = this.perelet.get() && bps >= 20.0 && !mc.player.isTouchingWater() && !mc.player.isSubmergedInWater();
         if (canPredict) {
            Vec3d motion = new Vec3d(
               base.getX() - base.prevX, base.getY() - base.prevY, base.getZ() - base.prevZ
            );
            double predictFactor = (double)MathHelper.clamp(this.predict.get().floatValue(), 0.2F, 2.5F);
            Vec3d predicted = motion.multiply(predictFactor * 1.5);
            if (predicted.lengthSquared() < 1.0E-4) {
               predicted = base.getRotationVecClient().normalize().multiply(predictFactor);
            }

            modifiedPos = base.getPos().add(predicted).add(0.0, (double)base.getHeight() / 2.0, 0.0);
         } else {
            modifiedPos = base.getPos().add(0.0, (double)base.getHeight() / 2.0, 0.0);
         }
      }

      double dx = modifiedPos.x - mc.player.getX();
      double dy = modifiedPos.y - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double dz = modifiedPos.z - mc.player.getZ();
      this.x = (int)modifiedPos.x;
      this.y = (int)modifiedPos.y;
      this.z = (int)modifiedPos.z;
      float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
      float targetPitch = (float)(-Math.toDegrees(Math.atan2(dy, Math.hypot(dx, dz))));
      Manager.ROTATION.setSmooth(targetYaw, targetPitch, 1.15F, 140.0F, 40.0F, true);
   }

   public void targetDefault(LivingEntity base, boolean attack) {
      Vec3d targetPos = base.getPos();

      double bps;
      try {
         bps = Double.parseDouble(ClientManager.getBps(base));
      } catch (Exception var15) {
         bps = 0.0;
      }

      boolean canPredict = this.perelet.get() && bps >= 20.0 && !mc.player.isTouchingWater() && !mc.player.isSubmergedInWater();
      if (canPredict) {
         Vec3d motion = new Vec3d(base.getX() - base.prevX, base.getY() - base.prevY, base.getZ() - base.prevZ);
         double predictFactor = (double)MathHelper.clamp(this.predict.get().floatValue(), 0.2F, 2.5F);
         Vec3d predicted = motion.multiply(predictFactor * 1.5);
         if (predicted.lengthSquared() < 1.0E-4) {
            predicted = base.getRotationVecClient().normalize().multiply(predictFactor);
         }

         targetPos = targetPos.add(predicted);
      }

      double diffX = targetPos.x - mc.player.getX();
      double diffY = targetPos.y - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double diffZ = targetPos.z - mc.player.getZ();
      this.x = (int)targetPos.x;
      this.y = (int)targetPos.y;
      this.z = (int)targetPos.z;
      float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, Math.hypot(diffX, diffZ))));
      Manager.ROTATION.setSmooth(yaw, pitch, 1.15F, 140.0F, 40.0F, true);
   }

   private Vec3d findClosestValidPosAround(Vec3d center, double radius) {
      List<Vec3d> offsets = new ArrayList<>();

      for (int y = 2; y >= -1; y--) {
         for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
               if (dx != 0 || dz != 0 || y != 0) {
                  offsets.add(new Vec3d((double)dx, (double)y, (double)dz));
               }
            }
         }
      }

      Vec3d best = null;
      double bestDist = Double.MAX_VALUE;

      for (Vec3d offset : offsets) {
         if (this.isValidFlyPosition(center) && !this.isObstructed(mc.player.getEyePos(), center)) {
            double dist = center.squaredDistanceTo(center);
            if (dist < bestDist) {
               best = center;
               bestDist = dist;
            }
         }
      }

      return best;
   }

   private boolean isObstructed(Vec3d from, Vec3d to) {
      return mc.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getType()
         != HitResult.Type.MISS;
   }

   private boolean isValidFlyPosition(Vec3d pos) {
      Vec3d head = new Vec3d(pos.x, pos.y + (double)mc.player.getHeight(), pos.z);
      Vec3d feet = new Vec3d(pos.x, pos.y, pos.z);
      boolean obstructedVertically = mc.world
            .raycast(new RaycastContext(head, feet, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player))
            .getType()
         != HitResult.Type.MISS;
      int blockX = (int)Math.floor(pos.x);
      int blockY = (int)Math.floor(pos.y);
      int blockZ = (int)Math.floor(pos.z);
      boolean isBlockSolid = mc.world.getBlockState(new BlockPos(blockX, blockY, blockZ)).isOpaqueFullCube();
      int groundY = mc.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, new BlockPos(blockX, 0, blockZ)).getY();
      boolean belowGround = pos.y < (double)groundY + 1.0;
      return !obstructedVertically && !isBlockSolid && !belowGround;
   }

   public boolean isDefensiveActive() {
      return this.defensiveActive;
   }

   public boolean isLastDefensive() {
      return this.lastDefensive;
   }
}
