package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.player.EventPlayerTravel;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.move.MoveUtil;
import dev.just.util.move.NetworkUtils;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "KTLeave",
   desc = "R2VsacWfbWnFnyB0ZWtuZSB1w6d1xZ91IHZlIHBha2V0IG1hbmlww7xsYXN5b251",
   type = Type.Misc
)
public class l0Il1O0l extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("TW9k"), "Motion", "Motion", "Packet");
   private final BooleanSetting phase = new BooleanSetting(I0O1l0I1.b("RHV2YXIgR2XDp21l"), false);
   private final BooleanSetting gravity = new BooleanSetting(I0O1l0I1.b("WWVyw6dla2ltaQ=="), false);
   private final BooleanSetting automount = new BooleanSetting(I0O1l0I1.b("T3RvIEJpbm1l"), false);
   private final BooleanSetting allowShift = new BooleanSetting(I0O1l0I1.b("U2hpZnQgxLB6bmk="), false);
   private final SliderSetting speed = new SliderSetting(I0O1l0I1.b("SMSxeg=="), 2.0, 0.0, 25.0, 0.1F);
   private final SliderSetting yspeed = new SliderSetting(I0O1l0I1.b("WS1IxLF6xLE="), 1.0, 0.0, 10.0, 0.1F);
   private final BooleanSetting cancel = new BooleanSetting(I0O1l0I1.b("xLBwdGFsIEV0"), false);
   private final BooleanSetting stopunloaded = new BooleanSetting(I0O1l0I1.b("WcO8a2xlbm1lbWnFn3RlIER1cg=="), false);
   private final BooleanSetting cancelrotations = new BooleanSetting(I0O1l0I1.b("Um90YXN5b251IMSwcHRhbCBFdA=="), false);
   private final BooleanSetting limit = new BooleanSetting(I0O1l0I1.b("U8SxbsSxcmxh"), false);
   private final SliderSetting jitter = new SliderSetting(I0O1l0I1.b("VGl0cmVtZQ=="), 0.1F, 0.0, 10.0, 0.1F);
   private final BooleanSetting spoofpackets = new BooleanSetting(I0O1l0I1.b("UGFrZXQgWWFuxLFsdG1h"), false);
   private final BooleanSetting ongroundpacket = new BooleanSetting(I0O1l0I1.b("WWVyZGUgUGFrZXRp"), false);
   private final CopyOnWriteArrayList<VehicleMoveC2SPacket> vehiclePackets = new CopyOnWriteArrayList<>();
   private int ticksEnabled = 0;
   private int enableDelay = 0;
   private boolean waitedCooldown = false;
   private boolean returnGravity = false;
   private boolean jitterSwitch = false;

   public l0Il1O0l() {
      this.addSettings(
         new Setting[]{
            this.mode,
            this.phase,
            this.gravity,
            this.automount,
            this.allowShift,
            this.speed,
            this.yspeed,
            this.cancel,
            this.stopunloaded,
            this.cancelrotations,
            this.limit,
            this.jitter,
            this.spoofpackets,
            this.ongroundpacket
         }
      );
   }

   private boolean fullNullCheck() {
      return mc.player == null || mc.world == null;
   }

   @Override
   public void onEnable() {
      if (this.fullNullCheck()) {
         this.toggle();
      } else {
         if (this.automount.get()) {
            this.mountToBoat();
         }
      }
   }

   @Override
   public void onDisable() {
      this.vehiclePackets.clear();
      this.waitedCooldown = false;
      if (mc.player != null) {
         if (this.phase.get() && this.mode.is("Motion")) {
            if (mc.player.getControllingVehicle() != null) {
               mc.player.getControllingVehicle().noClip = false;
            }

            mc.player.noClip = false;
         }

         if (mc.player.getControllingVehicle() != null) {
            mc.player.getControllingVehicle().setNoGravity(false);
         }

         mc.player.setNoGravity(false);
      }
   }

   private float randomizeYOffset() {
      this.jitterSwitch = !this.jitterSwitch;
      return this.jitterSwitch ? this.jitter.get().floatValue() : -this.jitter.get().floatValue();
   }

   private void sendMovePacket(VehicleMoveC2SPacket pac) {
      this.vehiclePackets.add(pac);
      this.send(pac);
   }

   private void teleportToGround(Entity boat) {
      if (boat != null && mc.world != null) {
         BlockPos blockPos = BlockPos.ofFloored(boat.getPos());

         for (int i = 0; i < 255; i++) {
            if (!mc.world.getBlockState(blockPos).isReplaceable() || mc.world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
               boat.setPosition(boat.getX(), (double)(blockPos.getY() + 1), boat.getZ());
               this.sendMovePacket(VehicleMoveC2SPacket.fromVehicle(boat));
               boat.setPosition(boat.getX(), boat.getY(), boat.getZ());
               break;
            }

            blockPos = blockPos.down();
         }
      }
   }

   private void mountToBoat() {
      if (mc.player != null && mc.world != null) {
         for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
            if (entity instanceof BoatEntity && !(mc.player.squaredDistanceTo(entity) > 25.0)) {
               this.send(PlayerInteractEntityC2SPacket.interact(entity, false, Hand.MAIN_HAND));
               break;
            }
         }
      }
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventPlayerTravel ev) {
         if (!ev.isPre() || this.fullNullCheck()) {
            return;
         }

         Entity entity = mc.player.getControllingVehicle();
         if (entity == null) {
            if (this.automount.get()) {
               this.mountToBoat();
            }

            return;
         }

         if (this.phase.get() && this.mode.is("Motion")) {
            entity.noClip = true;
            entity.setNoGravity(true);
            mc.player.noClip = true;
         }

         if (!this.returnGravity) {
            entity.setNoGravity(!this.gravity.get());
            mc.player.setNoGravity(!this.gravity.get());
         }

         if ((!mc.world.isChunkLoaded((int)entity.getX() >> 4, (int)entity.getZ() >> 4) || entity.getY() < -60.0)
            && this.stopunloaded.get()) {
            this.returnGravity = true;
            return;
         }

         entity.setYaw(mc.player.getYaw());
         double[] boatMotion = MoveUtil.forward((double)this.speed.get().floatValue());
         double predictedX = entity.getX() + boatMotion[0];
         double predictedZ = entity.getZ() + boatMotion[1];
         double predictedY = entity.getY();
         if ((!mc.world.isChunkLoaded((int)predictedX >> 4, (int)predictedZ >> 4) || predictedY < -60.0) && this.stopunloaded.get()) {
            this.returnGravity = true;
            return;
         }

         this.returnGravity = false;
         if (this.mode.is("Motion")) {
            entity.setVelocity(boatMotion[0], entity.getVelocity().y, boatMotion[1]);
         }

         if (mc.options.jumpKey.isPressed()) {
            if (this.mode.is("Motion")) {
               entity.setVelocity(
                  entity.getVelocity().x, entity.getVelocity().y + (double)this.yspeed.get().floatValue(), entity.getVelocity().z
               );
            } else {
               predictedY += (double)this.yspeed.get().floatValue();
            }
         } else if (mc.options.sneakKey.isPressed()) {
            if (this.mode.is("Motion")) {
               entity.setVelocity(
                  entity.getVelocity().x, entity.getVelocity().y - (double)this.yspeed.get().floatValue(), entity.getVelocity().z
               );
            } else {
               predictedY -= (double)this.yspeed.get().floatValue();
            }
         }

         if (!MoveUtil.isMoving()) {
            entity.setVelocity(0.0, entity.getVelocity().y, 0.0);
         }

         if (this.ongroundpacket.get()) {
            this.teleportToGround(entity);
         }

         if (this.mode.is("Packet")) {
            entity.setPosition(predictedX, predictedY, predictedZ);
            this.sendMovePacket(VehicleMoveC2SPacket.fromVehicle(entity));
         }

         ev.setCancel(true);
         this.ticksEnabled++;
      }

      if (event instanceof EventPacket eventPacket && eventPacket.isReceivePacket()) {
         if (this.fullNullCheck()) {
            return;
         }

         if (eventPacket.getPacket() instanceof DisconnectS2CPacket) {
            this.toggle();
         }

         if (!mc.player.isRiding() || this.returnGravity || this.waitedCooldown) {
            return;
         }

         if (this.cancel.get()
            && (
               eventPacket.getPacket() instanceof VehicleMoveS2CPacket
                  || eventPacket.getPacket() instanceof EntityS2CPacket
                  || eventPacket.getPacket() instanceof EntityAttachS2CPacket
                  || !(eventPacket.getPacket() instanceof KeepAliveC2SPacket)
            )) {
            eventPacket.setCancel(true);
         }
      }

      if (event instanceof EventPacket eventPacket2 && eventPacket2.isSendPacket()) {
         if (this.fullNullCheck()) {
            return;
         }

         if ((eventPacket2.getPacket() instanceof PlayerMoveC2SPacket.Full && this.cancelrotations.get() || eventPacket2.getPacket() instanceof PlayerInputC2SPacket)
            && mc.player.isRiding()) {
            eventPacket2.setCancel(true);
         }

         if (this.returnGravity && eventPacket2.getPacket() instanceof VehicleMoveC2SPacket) {
            eventPacket2.setCancel(true);
         }

         if (eventPacket2.getPacket() instanceof PlayerInputC2SPacket && this.allowShift.get()) {
            eventPacket2.setCancel(true);
         }

         if (mc.player.getControllingVehicle() == null || this.returnGravity || this.waitedCooldown) {
            return;
         }

         Vec3d boatPos = mc.player.getControllingVehicle().getPos();
         if ((!mc.world.isChunkLoaded((int)boatPos.x >> 4, (int)boatPos.z >> 4) || boatPos.y < -60.0) && this.stopunloaded.get()) {
            return;
         }

         if (eventPacket2.getPacket() instanceof VehicleMoveC2SPacket pac && this.limit.get() && this.mode.is("Packet")) {
            if (this.vehiclePackets.contains(pac)) {
               this.vehiclePackets.remove(pac);
            } else {
               eventPacket2.setCancel(true);
            }
         }
      }
   }

   private void send(Packet<?> packet) {
      if (mc.player != null && mc.player.networkHandler != null) {
         NetworkUtils.sendSilentPacket(packet);
      }
   }
}
