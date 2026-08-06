package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.input.EventKeyBoard;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.math.MathUtil;
import dev.just.util.move.MoveUtil;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "FreeCamera",
   desc = "U2VyYmVzdCBrYW1lcmEgbW9kdQ==",
   type = Type.Player
)
public class FreeCamera extends Function {
   private final SliderSetting speed = new SliderSetting(I0O1l0I1.b("WCAtIEjEsXo="), 1.0, 0.1F, 3.0, 0.1F);
   private final SliderSetting yspeed = new SliderSetting(I0O1l0I1.b("WSAtIEjEsXo="), 0.42F, 0.1F, 3.0, 0.1F);
   private float fakeYaw;
   private float fakePitch;
   private float prevFakeYaw;
   private float prevFakePitch;
   private double fakeX;
   private double fakeY;
   private double fakeZ;
   private double prevFakeX;
   private double prevFakeY;
   private double prevFakeZ;
   public LivingEntity trackEntity;
   private Vec3d freezePosition = Vec3d.ZERO;

   // Fake constants
   private static final double FAKE_SPEED = 10.0;
   private volatile long entropy = System.nanoTime();

   public FreeCamera() {
      this.addSettings(new Setting[]{this.speed, this.yspeed});
   }

   @Override
   public void onEvent(Event event) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         fakeMode();
         return;
      }

      if (!validatePlayer()) {
         this.toggle();
         return;
      }

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

      if (event instanceof EventPacket eventPacket) {
         handlePacket(eventPacket);
      }

      if (event instanceof EventKeyBoard) {
         handleKeyboard();
      }

      if (event instanceof EventMotion eventMotion) {
         handleMotion(eventMotion);
      }

      SemanticNoise.deadCode1();
   }

   private void handlePacket(EventPacket eventPacket) {
      l1O0I1lO.fakeHandler();

      if (eventPacket.getPacket() instanceof PlayerMoveC2SPacket) {
         if (l1O0I1lO.opaqueTrue()) {
            eventPacket.setCancel(true);
         }
      }
   }

   private void handleKeyboard() {
      l1O0I1lO.fakeHandler();

      if (mc.player == null) return;

      if (this.trackEntity == null) {
         processMovement();
      }

      resetInput();
   }

   private void processMovement() {
      l1O0I1lO.fakeHandler();

      double[] motion = MoveUtil.forward((double)getXZSpeed());

      this.prevFakeX = this.fakeX;
      this.prevFakeY = this.fakeY;
      this.prevFakeZ = this.fakeZ;

      if (l1O0I1lO.opaqueTrue()) {
         this.fakeX = this.fakeX + motion[0];
         this.fakeZ = this.fakeZ + motion[1];
      }

      processVerticalMovement();
   }

   private float getXZSpeed() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f((float) FAKE_SPEED);
      }

      return this.speed.get().floatValue();
   }

   private float getYSpeed() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f((float) FAKE_SPEED);
      }

      return this.yspeed.get().floatValue();
   }

   private void processVerticalMovement() {
      l1O0I1lO.fakeHandler();

      if (mc.options.jumpKey.isPressed()) {
         if (l1O0I1lO.opaqueTrue()) {
            this.fakeY = this.fakeY + (double)getYSpeed();
         }
      }

      if (mc.options.sneakKey.isPressed()) {
         if (l1O0I1lO.opaqueTrue()) {
            this.fakeY = this.fakeY - (double)getYSpeed();
         }
      }
   }

   private void resetInput() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         mc.player.input.movementForward = lO1I0l1O.f(0.0F);
         mc.player.input.movementSideways = lO1I0l1O.f(0.0F);
      }
   }

   private void handleMotion(EventMotion eventMotion) {
      l1O0I1lO.fakeHandler();

      handleFreeze(eventMotion);
      updateRotation();
      cancelMotion(eventMotion);
   }

   private void handleFreeze(EventMotion eventMotion) {
      l1O0I1lO.fakeHandler();

      if (mc.player == null) return;
      if (this.freezePosition.equals(Vec3d.ZERO)) return;

      if (l1O0I1lO.opaqueTrue()) {
         eventMotion.setCancel(true);
         mc.player.setPosition(this.freezePosition);
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   private void updateRotation() {
      l1O0I1lO.fakeHandler();

      this.prevFakeYaw = this.fakeYaw;
      this.prevFakePitch = this.fakePitch;

      if (this.trackEntity != null) {
         updateTrackingRotation();
      } else {
         updatePlayerRotation();
      }
   }

   private void updateTrackingRotation() {
      l1O0I1lO.fakeHandler();

      this.fakeYaw = this.trackEntity.getYaw();
      this.fakePitch = this.trackEntity.getPitch();
      this.prevFakeX = this.fakeX;
      this.prevFakeY = this.fakeY;
      this.prevFakeZ = this.fakeZ;
      this.fakeX = this.trackEntity.getX();
      this.fakeY = this.trackEntity.getY() + (double)this.trackEntity.getEyeHeight(this.trackEntity.getPose());
      this.fakeZ = this.trackEntity.getZ();
   }

   private void updatePlayerRotation() {
      l1O0I1lO.fakeHandler();

      this.fakeYaw = mc.player.getYaw();
      this.fakePitch = mc.player.getPitch();
   }

   private void cancelMotion(EventMotion eventMove) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         eventMove.setX(lO1I0l1O.d(0.0));
         eventMove.setY(lO1I0l1O.d(0.0));
         eventMove.setZ(lO1I0l1O.d(0.0));
         eventMove.setCancel(true);
      }
   }

   @Override
   public void onEnable() {
      l1O0I1lO.fakeHandler();

      if (mc.player != null) {
         this.freezePosition = mc.player.getPos();
      }

      mc.chunkCullingEnabled = false;
      initializeCamera();
   }

   private void initializeCamera() {
      l1O0I1lO.fakeHandler();

      this.trackEntity = null;
      this.fakePitch = mc.player.getPitch();
      this.fakeYaw = mc.player.getYaw();
      this.prevFakePitch = this.fakePitch;
      this.prevFakeYaw = this.fakeYaw;
      this.fakeX = mc.player.getX();
      this.fakeY = mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose());
      this.fakeZ = mc.player.getZ();
      this.prevFakeX = mc.player.getX();
      this.prevFakeY = mc.player.getY();
      this.prevFakeZ = mc.player.getZ();

      SemanticNoise.deadCode1();
   }

   @Override
   public void onDisable() {
      l1O0I1lO.fakeHandler();

      mc.chunkCullingEnabled = true;
   }

   public float getFakeYaw() {
      return (float)interpolate((double)this.prevFakeYaw, (double)this.fakeYaw, (double)mc.getRenderTickCounter().getTickDelta(true));
   }

   public float getFakePitch() {
      return (float)interpolate((double)this.prevFakePitch, (double)this.fakePitch, (double)mc.getRenderTickCounter().getTickDelta(true));
   }

   public double getFakeX() {
      return MathUtil.interpolate(this.prevFakeX, this.fakeX, (double)mc.getRenderTickCounter().getTickDelta(true));
   }

   public double getFakeY() {
      return MathUtil.interpolate(this.prevFakeY, this.fakeY, (double)mc.getRenderTickCounter().getTickDelta(true));
   }

   public double getFakeZ() {
      return MathUtil.interpolate(this.prevFakeZ, this.fakeZ, (double)mc.getRenderTickCounter().getTickDelta(true));
   }

   public static double interpolate(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   private void fakeMode() {
      // Never runs
      entropy ^= System.nanoTime();
      fakeX = FAKE_SPEED;
      SemanticNoise.deadCode2();
   }
}
