package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;

@FunctionAnnotation(
   name = "HitBox",
   type = Type.Combat,
   desc = "Oyuncuların vuruş alanını (hitbox) genişletmenizi sağlar"
)
public class HitBox extends Function {
   public SliderSetting size = new SliderSetting("Boyut", 0.4F, 0.1F, 5.5, 0.1F);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public HitBox() {
      this.addSettings(new Setting[]{this.size});
   }

   @Override
   public void onEvent(Event event) {
      FlowObfuscator.fakeHandler();

      // Fake path - never executes
      if (FlowObfuscator.opaqueFalse()) {
         entropy ^= event.hashCode();
         return;
      }

      // HitBox doesn't need to handle events directly
      // The size is applied via Mixin
   }

   public float getSize() {
      FlowObfuscator.fakeHandler();

      // Fake path - never executes
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f(99.0f);
      }

      // Real path
      float value = this.size.get().floatValue();

      // Additional obfuscation layer
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f(entropy % FAKE_STATE);
      }

      return NumberGuard.f(value);
   }
}
