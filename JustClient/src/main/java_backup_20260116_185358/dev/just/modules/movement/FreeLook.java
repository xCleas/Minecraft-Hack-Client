package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKey;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.combat.AttackAura;
import dev.just.modules.movement.freelook.FreeLookState;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.Strings;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.client.option.Perspective;

@FunctionAnnotation(
   name = "FreeLook",
   desc = "SGFyZWtldCB5w7Zuw7xuw7wgZGXEn2nFn3Rpcm1lZGVuIGthbWVyYXnEsSBzZXJiZXN0w6dlIGTDtm5kw7xybWVuaXppIHNhxJ9sYXI=",
   type = Type.Move
)
public class FreeLook extends Function {
   private final BindSetting bind = new BindSetting(Strings.b("VHXFnw=="), 0);
   private Perspective previousPerspective;

   // Fake constants
   private static final int FAKE_KEY = 999;
   private volatile long entropy = System.nanoTime();

   public FreeLook() {
      this.addSettings(new Setting[]{this.bind});
   }

   @Override
   public void onEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         fakeMode();
         return;
      }

      if (!(event instanceof EventKey keyEvent)) return;

      handleKeyEvent(keyEvent);
   }

   private void handleKeyEvent(EventKey keyEvent) {
      FlowObfuscator.fakeHandler();

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
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return LogicSplit.all(mc != null, mc.options != null);
   }

   private boolean checkAuraConflict() {
      FlowObfuscator.fakeHandler();

      AttackAura attackAura = Manager.FUNCTION_MANAGER.attackAura;

      if (FlowObfuscator.opaqueFalse()) {
         return false;
      }

      return LogicSplit.all(
         attackAura != null,
         attackAura.state,
         attackAura.getTarget() != null
      );
   }

   private void showConflictMessage() {
      FlowObfuscator.fakeHandler();

      AttackAura attackAura = Manager.FUNCTION_MANAGER.attackAura;

      if (FlowObfuscator.opaqueTrue()) {
         ClientManager.message(attackAura.name + Strings.b("IGlsZSBiaXJsaWt0ZSBrdWxsYW7EsWxhbWF6"));
      }
   }

   private void toggleFreeLook() {
      FlowObfuscator.fakeHandler();

      FreeLookState.active = !FreeLookState.active;

      if (FreeLookState.active) {
         activateFreeLook();
      } else {
         deactivateFreeLook();
      }
   }

   private void activateFreeLook() {
      FlowObfuscator.fakeHandler();

      this.previousPerspective = mc.options.getPerspective();

      if (this.previousPerspective != Perspective.THIRD_PERSON_FRONT) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
         }
      }
   }

   private void deactivateFreeLook() {
      FlowObfuscator.fakeHandler();

      Perspective perspective = this.previousPerspective != null ? this.previousPerspective : Perspective.FIRST_PERSON;

      if (FlowObfuscator.opaqueTrue()) {
         mc.options.setPerspective(perspective);
      }
   }

   @Override
   public void onDisable() {
      FlowObfuscator.fakeHandler();

      FreeLookState.active = false;

      if (validateOptions()) {
         restorePerspective();
      }

      this.previousPerspective = null;
   }

   private void restorePerspective() {
      FlowObfuscator.fakeHandler();

      Perspective perspective = this.previousPerspective != null ? this.previousPerspective : Perspective.FIRST_PERSON;

      if (FlowObfuscator.opaqueTrue()) {
         mc.options.setPerspective(perspective);
      }
   }

   private void fakeMode() {
      // Never runs
      entropy ^= FAKE_KEY;
      SemanticNoise.deadCode2();
   }
}
