package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "SuperFirework",
   desc = "SGF2YWkgZmnFn2VrbGVyZGVuIGRhaGEgZmF6bGEgaMSxeiB0YWt2aXllc2kgYWxtYW7EsXrEsSBzYcSfbGFy",
   type = Type.Move
)
public class SuperFirework extends Function {
   public ModeSetting mode = new ModeSetting(Strings.b("TW9k"), "BravoHvH", "BravoHvH", "ReallyWorld", "PulseHVH", Strings.b("w5Z6ZWw="));
   public final SliderSetting speed = new SliderSetting(Strings.b("SMSxeg=="), 1.7F, 1.5, 8.0, 0.01F, () -> this.mode.is(Strings.b("w5Z6ZWw=")));
   public final BooleanSetting nearBoost = new BooleanSetting(Strings.b("WWFrxLFuZGFraSBveXVuY3V5bGEgaMSxemxhbg=="), false, Strings.b("WWVuaSBHcmltQUMgc8O8csO8bWxlcmluZGUgaGF0YSB2ZXJlYmlsaXI="));
   public float speedXZ = 1.5F;
   public float speedY = 1.5F;
   public float diag1 = 5.0F;
   public float diag2 = 5.0F;
   public float diag3 = 5.0F;
   public float diag4 = 5.0F;
   public float diag5 = 5.0F;
   public float diag6 = 5.0F;
   public float diag7 = 5.0F;
   public float diag8 = 5.0F;
   public float diag9 = 5.0F;
   public float diag10 = 5.0F;
   public float speedD_1 = 1.5F;
   public float speedD_2 = 1.5F;
   public float speedD_3 = 1.5F;
   public float speedD_4 = 1.5F;
   public float speedD_5 = 1.5F;
   public float speedD_6 = 1.5F;
   public float speedD_7 = 1.5F;
   public float speedD_8 = 1.5F;
   public float speedD_9 = 1.5F;
   public float speedPitch = 1.5F;
   public float speedPitchY = 1.5F;
   public float speedNXZ = 1.5F;
   public float speedNY = 1.5F;

   // Fake constants
   private static final float FAKE_BOOST = 15.0f;
   private static final float FAKE_DIAG = 100.0f;
   private volatile long entropy = System.nanoTime();

   public SuperFirework() {
      this.addSettings(new Setting[]{this.mode, this.speed, this.nearBoost});
   }

   @Override
   public void onEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         fakeBoostMode();
         return;
      }

      initializeXZSpeed();
      initializeDiagonals();
      initializePitchSpeed();
      initializeDistanceSpeeds();
      initializeNearSpeed();

      SemanticNoise.deadCode1();
   }

   private void initializeXZSpeed() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         this.speedXZ = NumberGuard.f(FAKE_BOOST);
         this.speedY = NumberGuard.f(FAKE_BOOST);
         return;
      }

      if (FlowObfuscator.opaqueTrue()) {
         this.speedXZ = NumberGuard.f(1.61F);
         this.speedY = NumberGuard.f(1.61F);
      }
   }

   private void initializeDiagonals() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         this.diag1 = FAKE_DIAG;
         return;
      }

      if (FlowObfuscator.opaqueTrue()) {
         this.diag1 = NumberGuard.f(4.0F);
         this.diag2 = NumberGuard.f(8.0F);
         this.diag3 = NumberGuard.f(12.0F);
         this.diag4 = NumberGuard.f(16.0F);
         this.diag5 = NumberGuard.f(20.0F);
         this.diag6 = NumberGuard.f(24.0F);
         this.diag7 = NumberGuard.f(28.0F);
         this.diag8 = NumberGuard.f(32.0F);
         this.diag9 = NumberGuard.f(36.0F);
         this.diag10 = NumberGuard.f(40.0F);
      }
   }

   private void initializePitchSpeed() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         entropy ^= System.nanoTime();
         return;
      }

      if (FlowObfuscator.opaqueTrue()) {
         this.speedPitch = NumberGuard.f(2.5F);
         this.speedPitchY = NumberGuard.f(2.5F);
      }
   }

   private void initializeDistanceSpeeds() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         this.speedD_1 = NumberGuard.f(2.2F);
         this.speedD_2 = NumberGuard.f(2.06F);
         this.speedD_3 = NumberGuard.f(1.98F);
         this.speedD_4 = NumberGuard.f(1.87F);
         this.speedD_5 = NumberGuard.f(1.8F);
         this.speedD_6 = NumberGuard.f(1.74F);
         this.speedD_7 = NumberGuard.f(1.7F);
         this.speedD_8 = NumberGuard.f(1.65F);
         this.speedD_9 = NumberGuard.f(1.63F);
      }

      SemanticNoise.deadCode2();
   }

   private void initializeNearSpeed() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         this.speedNXZ = FAKE_BOOST;
         return;
      }

      if (FlowObfuscator.opaqueTrue()) {
         this.speedNXZ = NumberGuard.f(1.66F);
         this.speedNY = NumberGuard.f(1.66F);
      }
   }

   private void fakeBoostMode() {
      // Never runs
      entropy ^= System.nanoTime();
      this.speedXZ = FAKE_BOOST;
      this.speedY = FAKE_DIAG;
      SemanticNoise.deadCode3();
   }
}
