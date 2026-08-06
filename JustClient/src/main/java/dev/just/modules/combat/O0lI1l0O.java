package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;

@FunctionAnnotation(
   name = "HitBox",
   type = Type.Combat,
   desc = "Oyuncuların vuruş alanını (hitbox) genişletmenizi sağlar"
)
public class O0lI1l0O extends Function {
   public SliderSetting size = new SliderSetting("Boyut", 0.4F, 0.1F, 5.5, 0.1F);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public O0lI1l0O() {
      this.addSettings(new Setting[]{this.size});
   }

   @Override
   public void onEvent(Event event) {
      l1O0I1lO.fakeHandler();

      // Fake path - never executes
      if (l1O0I1lO.opaqueFalse()) {
         entropy ^= event.hashCode();
         return;
      }

      // HitBox doesn't need to handle events directly
      // The size is applied via Mixin
   }

   public float getSize() {
      l1O0I1lO.fakeHandler();

      // Fake path - never executes
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f(99.0f);
      }

      // Real path
      float value = this.size.get().floatValue();

      // Additional obfuscation layer
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.f(entropy % FAKE_STATE);
      }

      return lO1I0l1O.f(value);
   }
}
