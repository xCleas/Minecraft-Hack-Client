package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKey;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.combat.I1lO0Il1;
import dev.just.modules.movement.freelook.FreeLookState;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.client.option.Perspective;

@FunctionAnnotation(
   name = "FreeLook",
   desc = "SGFyZWtldCB5w7Zuw7xuw7wgZGXEn2nFn3Rpcm1lZGVuIGthbWVyYXnEsSBzZXJiZXN0w6dlIGTDtm5kw7xybWVuaXppIHNhxJ9sYXI=",
   type = Type.Move
)
public class FreeLook extends Function {
   private final BindSetting bind = new BindSetting(I0O1l0I1.b("VHXFnw=="), 0);
   private Perspective previousPerspective;

   // Fake constants
   private static final int FAKE_KEY = 999;
   private volatile long entropy = System.nanoTime();

   public FreeLook() {
      this.addSettings(new Setting[]{this.bind});
   }

   @Override
   public void onEvent(Event event) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         fakeMode();
         return;
      }

      if (!(event instanceof EventKey keyEvent)) return;

      handleKeyEvent(keyEvent);
   }

   private void handleKeyEvent(EventKey keyEvent) {
      l1O0I1lO.fakeHandler();

      if (keyEvent.key != this.bind.getKey()) return;
      if (!validateOptions()) return;

      if (checkAuraConflict()) {
         showConflictMessage();
         return;
      }

      toggleFreeLook();

      SemanticNoise.deadCode1();
   }

   private boolean validateOptions() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return I1lO0l1I.all(mc != null, mc.options != null);
   }

   private boolean checkAuraConflict() {
      l1O0I1lO.fakeHandler();

      I1lO0Il1 attackAura = Manager.FUNCTION_MANAGER.attackAura;

      if (l1O0I1lO.opaqueFalse()) {
         return false;
      }

      return I1lO0l1I.all(
         attackAura != null,
         attackAura.state,
         attackAura.getTarget() != null
      );
   }

   private void showConflictMessage() {
      l1O0I1lO.fakeHandler();

      I1lO0Il1 attackAura = Manager.FUNCTION_MANAGER.attackAura;

      if (l1O0I1lO.opaqueTrue()) {
         ClientManager.message(attackAura.name + I0O1l0I1.b("IGlsZSBiaXJsaWt0ZSBrdWxsYW7EsWxhbWF6"));
      }
   }

   private void toggleFreeLook() {
      l1O0I1lO.fakeHandler();

      FreeLookState.active = !FreeLookState.active;

      if (FreeLookState.active) {
         activateFreeLook();
      } else {
         deactivateFreeLook();
      }
   }

   private void activateFreeLook() {
      l1O0I1lO.fakeHandler();

      this.previousPerspective = mc.options.getPerspective();

      if (this.previousPerspective != Perspective.THIRD_PERSON_FRONT) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
         }
      }
   }

   private void deactivateFreeLook() {
      l1O0I1lO.fakeHandler();

      Perspective perspective = this.previousPerspective != null ? this.previousPerspective : Perspective.FIRST_PERSON;

      if (l1O0I1lO.opaqueTrue()) {
         mc.options.setPerspective(perspective);
      }
   }

   @Override
   public void onDisable() {
      l1O0I1lO.fakeHandler();

      FreeLookState.active = false;

      if (validateOptions()) {
         restorePerspective();
      }

      this.previousPerspective = null;
   }

   private void restorePerspective() {
      l1O0I1lO.fakeHandler();

      Perspective perspective = this.previousPerspective != null ? this.previousPerspective : Perspective.FIRST_PERSON;

      if (l1O0I1lO.opaqueTrue()) {
         mc.options.setPerspective(perspective);
      }
   }

   private void fakeMode() {
      // Never runs
      entropy ^= FAKE_KEY;
      SemanticNoise.deadCode2();
   }
}
