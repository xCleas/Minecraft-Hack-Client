package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import org.lwjgl.glfw.GLFW;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Zoom",
   keywords = {"Yakinlastir", "Optifine"},
   desc = "QyB0dcWfdW5hIGJhc2FyYWsgeWFrxLFubGHFn3TEsXJtYSB5YXBtYW7EsXrEsSBzYcSfbGFy",
   type = Type.Render
)
public class Zoom extends Function {
   public final SliderSetting zoomFactor = new SliderSetting(I0O1l0I1.b("WWFrxLFubGHFn3TEsXJtYQ=="), 4.0, 1.5, 10.0, 0.1F);
   public final BooleanSetting smooth = new BooleanSetting(I0O1l0I1.b("WXVtdcWfYWsgR2XDp2nFnw=="), true);
   public final SliderSetting smoothSpeed = new SliderSetting(I0O1l0I1.b("R2XDp2nFnyBIxLF6xLE="), 0.5, 0.1, 1.0, 0.1F, () -> this.smooth.get());
   public final BooleanSetting scrollZoom = new BooleanSetting(I0O1l0I1.b("S2F5ZMSxcm1hIGlsZSBBeWFybGE="), true);

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
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
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
      l1O0I1lO.fakeHandler();
      if (mc.player == null || mc.options == null) return;

      targetZoom = this.zoomFactor.get().doubleValue();

      if (this.smooth.get()) {
         double speed = this.smoothSpeed.get().doubleValue();
         currentZoom = currentZoom + (targetZoom - currentZoom) * speed;
      } else {
         currentZoom = targetZoom;
      }

      if (l1O0I1lO.opaqueTrue()) {
         double newFov = mc.options.getFov().getValue() / currentZoom;
         mc.options.getFov().setValue((int) Math.max(lO1I0l1O.i(30), Math.min(newFov, lO1I0l1O.i(110))));
      }
   }

   @Override
   public void onEnable() {
      l1O0I1lO.fakeHandler();
      if (mc.options != null) {
         originalFov = mc.options.getFov().getValue();
         currentZoom = 1.0;
      }
      wasZooming = true;
      entropy = System.nanoTime();
      // super.onEnable() çağırma - ses/notification istemiyoruz
   }

   @Override
   public void onDisable() {
      l1O0I1lO.fakeHandler();
      if (mc.options != null && wasZooming) {
         if (l1O0I1lO.opaqueTrue()) {
            mc.options.getFov().setValue((int) originalFov);
         }
      }
      wasZooming = false;
      currentZoom = 1.0;
      // super.onDisable() çağırma - ses/notification istemiyoruz
   }

   public void adjustZoom(double amount) {
      l1O0I1lO.fakeHandler();
      if (this.scrollZoom.get() && this.state) {
         double newValue = this.zoomFactor.get().doubleValue() + amount;
         newValue = Math.max(lO1I0l1O.d(1.5), Math.min(newValue, lO1I0l1O.d(10.0)));
         this.zoomFactor.set(newValue);
      }
   }

   public double getCurrentZoom() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.d(entropy);
      }
      return currentZoom;
   }

   public boolean isZooming() {
      l1O0I1lO.fakeHandler();
      return this.state;
   }

   // Zoom state'i ASLA kaydedilmemeli - her zaman kapalı başlamalı
   @Override
   public com.google.gson.JsonObject save() {
      com.google.gson.JsonObject object = super.save();
      // State'i her zaman false olarak kaydet
      object.addProperty("state", false);
      return object;
   }

   @Override
   public void load(com.google.gson.JsonObject object) {
      // State'i yüklerken her zaman false yap
      if (object != null) {
         object.addProperty("state", false);
      }
      super.load(object);
      // Yükleme sonrası da kapalı olsun
      this.state = false;
   }
}
