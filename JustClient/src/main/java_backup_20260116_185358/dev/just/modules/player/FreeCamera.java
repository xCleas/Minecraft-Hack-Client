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
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "FreeCamera",
   desc = "U2VyYmVzdCBrYW1lcmEgbW9kdQ==",
   type = Type.Player
)
public class FreeCamera extends Function {
   private final SliderSetting speed = new SliderSetting(Strings.b("WCAtIEjEsXo="), 1.0, 0.1F, 3.0, 0.1F);
   private final SliderSetting yspeed = new SliderSetting(Strings.b("WSAtIEjEsXo="), 0.42F, 0.1F, 3.0, 0.1F);
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
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
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
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return LogicSplit.all(mc.player != null, mc.world != null);
   }

   private void dispatchEvent(Event event) {
      FlowObfuscator.fakeHandler();

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
      FlowObfuscator.fakeHandler();

      if (eventPacket.getPacket() instanceof PlayerMoveC2SPacket) {
         if (FlowObfuscator.opaqueTrue()) {
            eventPacket.setCancel(true);
         }
      }
   }

   private void handleKeyboard() {
      FlowObfuscator.fakeHandler();

      if (mc.player == null) return;

      if (this.trackEntity == null) {
         processMovement();
      }

      resetInput();
   }

   private void processMovement() {
      FlowObfuscator.fakeHandler();

      double[] motion = MoveUtil.forward((double)getXZSpeed());

      this.prevFakeX = this.fakeX;
      this.prevFakeY = this.fakeY;
      this.prevFakeZ = this.fakeZ;

      if (FlowObfuscator.opaqueTrue()) {
         this.fakeX = this.fakeX + motion[0];
         this.fakeZ = this.fakeZ + motion[1];
      }

      processVerticalMovement();
   }

   private float getXZSpeed() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f((float) FAKE_SPEED);
      }

      return this.speed.get().floatValue();
   }

   private float getYSpeed() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f((float) FAKE_SPEED);
      }

      return this.yspeed.get().floatValue();
   }

   private void processVerticalMovement() {
      FlowObfuscator.fakeHandler();

      if (mc.options.jumpKey.isPressed()) {
         if (FlowObfuscator.opaqueTrue()) {
            this.fakeY = this.fakeY + (double)getYSpeed();
         }
      }

      if (mc.options.sneakKey.isPressed()) {
         if (FlowObfuscator.opaqueTrue()) {
            this.fakeY = this.fakeY - (double)getYSpeed();
         }
      }
   }

   private void resetInput() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.input.movementForward = NumberGuard.f(0.0F);
         mc.player.input.movementSideways = NumberGuard.f(0.0F);
      }
   }

   private void handleMotion(EventMotion eventMotion) {
      FlowObfuscator.fakeHandler();

      handleFreeze(eventMotion);
      updateRotation();
      cancelMotion(eventMotion);
   }

   private void handleFreeze(EventMotion eventMotion) {
      FlowObfuscator.fakeHandler();

      if (mc.player == null) return;
      if (this.freezePosition.equals(Vec3d.ZERO)) return;

      if (FlowObfuscator.opaqueTrue()) {
         eventMotion.setCancel(true);
         mc.player.setPosition(this.freezePosition);
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   private void updateRotation() {
      FlowObfuscator.fakeHandler();

      this.prevFakeYaw = this.fakeYaw;
      this.prevFakePitch = this.fakePitch;

      if (this.trackEntity != null) {
         updateTrackingRotation();
      } else {
         updatePlayerRotation();
      }
   }

   private void updateTrackingRotation() {
      FlowObfuscator.fakeHandler();

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
      FlowObfuscator.fakeHandler();

      this.fakeYaw = mc.player.getYaw();
      this.fakePitch = mc.player.getPitch();
   }

   private void cancelMotion(EventMotion eventMove) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         eventMove.setX(NumberGuard.d(0.0));
         eventMove.setY(NumberGuard.d(0.0));
         eventMove.setZ(NumberGuard.d(0.0));
         eventMove.setCancel(true);
      }
   }

   @Override
   public void onEnable() {
      FlowObfuscator.fakeHandler();

      if (mc.player != null) {
         this.freezePosition = mc.player.getPos();
      }

      mc.chunkCullingEnabled = false;
      initializeCamera();
   }

   private void initializeCamera() {
      FlowObfuscator.fakeHandler();

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
      FlowObfuscator.fakeHandler();

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
