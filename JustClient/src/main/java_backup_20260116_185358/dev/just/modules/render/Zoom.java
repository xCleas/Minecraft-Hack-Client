package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import org.lwjgl.glfw.GLFW;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Zoom",
   keywords = {"Yakinlastir", "Optifine"},
   desc = "QyB0dcWfdW5hIGJhc2FyYWsgeWFrxLFubGHFn3TEsXJtYSB5YXBtYW7EsXrEsSBzYcSfbGFy",
   type = Type.Render
)
public class Zoom extends Function {
   public final SliderSetting zoomFactor = new SliderSetting(Strings.b("WWFrxLFubGHFn3TEsXJtYQ=="), 4.0, 1.5, 10.0, 0.1F);
   public final BooleanSetting smooth = new BooleanSetting(Strings.b("WXVtdcWfYWsgR2XDp2nFnw=="), true);
   public final SliderSetting smoothSpeed = new SliderSetting(Strings.b("R2XDp2nFnyBIxLF6xLE="), 0.5, 0.1, 1.0, 0.1F, () -> this.smooth.get());
   public final BooleanSetting scrollZoom = new BooleanSetting(Strings.b("S2F5ZMSxcm1hIGlsZSBBeWFybGE="), true);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   private double currentZoom = 1.0;
   private double targetZoom = 1.0;
   private double originalFov = 0;
   private boolean wasZooming = false;

   public Zoom() {
      this.addSettings(new Setting[]{this.zoomFactor, this.smooth, this.smoothSpeed, this.scrollZoom});
      this.bind = GLFW.GLFW_KEY_C;
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventUpdate)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handleZoomInternal();
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void handleZoomInternal() {
      FlowObfuscator.fakeHandler();
      if (mc.player == null || mc.options == null) return;

      targetZoom = this.zoomFactor.get().doubleValue();

      if (this.smooth.get()) {
         double speed = this.smoothSpeed.get().doubleValue();
         currentZoom = currentZoom + (targetZoom - currentZoom) * speed;
      } else {
         currentZoom = targetZoom;
      }

      if (FlowObfuscator.opaqueTrue()) {
         double newFov = mc.options.getFov().getValue() / currentZoom;
         mc.options.getFov().setValue((int) Math.max(NumberGuard.i(30), Math.min(newFov, NumberGuard.i(110))));
      }
   }

   @Override
   public void onEnable() {
      FlowObfuscator.fakeHandler();
      if (mc.options != null) {
         originalFov = mc.options.getFov().getValue();
         currentZoom = 1.0;
      }
      wasZooming = true;
      entropy = System.nanoTime();
      super.onEnable();
   }

   @Override
   public void onDisable() {
      FlowObfuscator.fakeHandler();
      if (mc.options != null && wasZooming) {
         if (FlowObfuscator.opaqueTrue()) {
            mc.options.getFov().setValue((int) originalFov);
         }
      }
      wasZooming = false;
      currentZoom = 1.0;
      super.onDisable();
   }

   public void adjustZoom(double amount) {
      FlowObfuscator.fakeHandler();
      if (this.scrollZoom.get() && this.state) {
         double newValue = this.zoomFactor.get().doubleValue() + amount;
         newValue = Math.max(NumberGuard.d(1.5), Math.min(newValue, NumberGuard.d(10.0)));
         this.zoomFactor.set(newValue);
      }
   }

   public double getCurrentZoom() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.d(entropy);
      }
      return currentZoom;
   }

   public boolean isZooming() {
      FlowObfuscator.fakeHandler();
      return this.state;
   }
}
