package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.move.MoveUtil;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "HighJump",
   type = Type.Move,
   desc = "U2h1bGtlciBleHBsb2l0aW5pIGt1bGxhbmFyYWsgecO8a3NlxJ9lIHrEsXBsYW1hbsSxesSxIHNhxJ9sYXI="
)
public class HighJump extends Function {
   private final SliderSetting sila = new SliderSetting(Strings.b("R8O8w6c="), 2.0, 0.0, 5.0, 0.1F);
   private boolean wasShulkerOpen = false;
   private long jumpStartTime = 0L;
   private long guiOpenTime = 0L;

   // Fake constants
   private static final float FAKE_POWER = 10.0f;
   private static final long FAKE_DURATION = 10000L;
   private volatile long entropy = System.nanoTime();

   public HighJump() {
      this.addSettings(new Setting[]{this.sila});
   }

   @Override
   public void onEvent(Event event) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         fakeMode();
         return;
      }

      if (!(event instanceof EventMotion)) return;

      processJump();
   }

   private void processJump() {
      FlowObfuscator.fakeHandler();

      applyMainVelocity();
      handleShulkerState();
      handleJumpReset();

      SemanticNoise.deadCode1();
   }

   private void applyMainVelocity() {
      FlowObfuscator.fakeHandler();

      float power = getJumpPower();

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setVelocity(mc.player.getVelocity().x, (double)power, mc.player.getVelocity().z);
      }

      if (mc.options.sprintKey.isPressed()) {
         if (FlowObfuscator.opaqueTrue()) {
            MoveUtil.setMotion((double)power);
         }
      }
   }

   private float getJumpPower() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.f(FAKE_POWER);
      }

      return this.sila.get().floatValue();
   }

   private void handleShulkerState() {
      FlowObfuscator.fakeHandler();

      long currentTime = System.currentTimeMillis();

      checkShulkerOpen(currentTime);
      checkAutoClose(currentTime);
      checkJumpTrigger(currentTime);
   }

   private void checkShulkerOpen(long currentTime) {
      FlowObfuscator.fakeHandler();

      if (mc.currentScreen instanceof ShulkerBoxScreen && this.guiOpenTime == 0L) {
         this.wasShulkerOpen = true;
         this.guiOpenTime = currentTime;
      }
   }

   private void checkAutoClose(long currentTime) {
      FlowObfuscator.fakeHandler();

      long threshold = NumberGuard.i(800);

      if (this.guiOpenTime != 0L && currentTime - this.guiOpenTime >= threshold) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.player.closeScreen();
            mc.player.closeHandledScreen();
         }
         this.guiOpenTime = 0L;
      }
   }

   private void checkJumpTrigger(long currentTime) {
      FlowObfuscator.fakeHandler();

      if (LogicSplit.all(this.wasShulkerOpen, mc.currentScreen == null)) {
         this.wasShulkerOpen = false;
         this.jumpStartTime = currentTime;
         triggerJump();
      }
   }

   private void triggerJump() {
      FlowObfuscator.fakeHandler();

      float power = getJumpPower();

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setVelocity(mc.player.getVelocity().x, (double)power, mc.player.getVelocity().z);
      }

      if (mc.options.sprintKey.isPressed()) {
         if (FlowObfuscator.opaqueTrue()) {
            MoveUtil.setMotion((double)power);
         }
      }
   }

   private void handleJumpReset() {
      FlowObfuscator.fakeHandler();

      long currentTime = System.currentTimeMillis();
      long resetThreshold = NumberGuard.i(3000);

      if (this.jumpStartTime != 0L && currentTime - this.jumpStartTime >= resetThreshold) {
         this.jumpStartTime = 0L;
      }
   }

   private void fakeMode() {
      // Never runs
      entropy ^= System.nanoTime();
      jumpStartTime = FAKE_DURATION;
      SemanticNoise.deadCode2();
   }
}
