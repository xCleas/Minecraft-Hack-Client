package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "SuperFirework",
   desc = "SGF2YWkgZmnFn2VrbGVyZGVuIGRhaGEgZmF6bGEgaMSxeiB0YWt2aXllc2kgYWxtYW7EsXrEsSBzYcSfbGFy",
   type = Type.Move
)
public class SuperFirework extends Function {
   public ModeSetting mode = new ModeSetting(I0O1l0I1.b("TW9k"), "BravoHvH", "BravoHvH", "ReallyWorld", "PulseHVH", I0O1l0I1.b("w5Z6ZWw="));
   public final SliderSetting speed = new SliderSetting(I0O1l0I1.b("SMSxeg=="), 1.7F, 1.5, 8.0, 0.01F, () -> this.mode.is(I0O1l0I1.b("w5Z6ZWw=")));
   public final BooleanSetting nearBoost = new BooleanSetting(I0O1l0I1.b("WWFrxLFuZGFraSBveXVuY3V5bGEgaMSxemxhbg=="), false, I0O1l0I1.b("WWVuaSBHcmltQUMgc8O8csO8bWxlcmluZGUgaGF0YSB2ZXJlYmlsaXI="));
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
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
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
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         this.speedXZ = lO1I0l1O.f(FAKE_BOOST);
         this.speedY = lO1I0l1O.f(FAKE_BOOST);
         return;
      }

      if (l1O0I1lO.opaqueTrue()) {
         this.speedXZ = lO1I0l1O.f(1.61F);
         this.speedY = lO1I0l1O.f(1.61F);
      }
   }

   private void initializeDiagonals() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         this.diag1 = FAKE_DIAG;
         return;
      }

      if (l1O0I1lO.opaqueTrue()) {
         this.diag1 = lO1I0l1O.f(4.0F);
         this.diag2 = lO1I0l1O.f(8.0F);
         this.diag3 = lO1I0l1O.f(12.0F);
         this.diag4 = lO1I0l1O.f(16.0F);
         this.diag5 = lO1I0l1O.f(20.0F);
         this.diag6 = lO1I0l1O.f(24.0F);
         this.diag7 = lO1I0l1O.f(28.0F);
         this.diag8 = lO1I0l1O.f(32.0F);
         this.diag9 = lO1I0l1O.f(36.0F);
         this.diag10 = lO1I0l1O.f(40.0F);
      }
   }

   private void initializePitchSpeed() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         entropy ^= System.nanoTime();
         return;
      }

      if (l1O0I1lO.opaqueTrue()) {
         this.speedPitch = lO1I0l1O.f(2.5F);
         this.speedPitchY = lO1I0l1O.f(2.5F);
      }
   }

   private void initializeDistanceSpeeds() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         this.speedD_1 = lO1I0l1O.f(2.2F);
         this.speedD_2 = lO1I0l1O.f(2.06F);
         this.speedD_3 = lO1I0l1O.f(1.98F);
         this.speedD_4 = lO1I0l1O.f(1.87F);
         this.speedD_5 = lO1I0l1O.f(1.8F);
         this.speedD_6 = lO1I0l1O.f(1.74F);
         this.speedD_7 = lO1I0l1O.f(1.7F);
         this.speedD_8 = lO1I0l1O.f(1.65F);
         this.speedD_9 = lO1I0l1O.f(1.63F);
      }

      SemanticNoise.deadCode2();
   }

   private void initializeNearSpeed() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         this.speedNXZ = FAKE_BOOST;
         return;
      }

      if (l1O0I1lO.opaqueTrue()) {
         this.speedNXZ = lO1I0l1O.f(1.66F);
         this.speedNY = lO1I0l1O.f(1.66F);
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
