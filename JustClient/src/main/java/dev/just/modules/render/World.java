package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.world.EventFog;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.color.ColorUtil;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.client.render.FogShape;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "World",
   desc = "R8O8bsO8biBzYWF0aW5pLCBoYXZhIGR1cnVtdW51IHZlIHNpc2kgZGXEn2nFn3Rpcm1lbml6aSBzYcSfbGFy",
   type = Type.Render
)
public class World extends Function {
   private final BooleanSetting timeBox = new BooleanSetting(I0O1l0I1.b("WmFtYW7EsSBEZcSfacWfdGly"), true);
   private final ModeSetting timeMode = new ModeSetting(this.timeBox::get, I0O1l0I1.b("R8O8bsO8biBTYWF0aQ=="), I0O1l0I1.b("R8O8bmTDvHo="), I0O1l0I1.b("R8O8bmTDvHo="), I0O1l0I1.b("R2VjZQ=="), I0O1l0I1.b("U2FiYWg="), I0O1l0I1.b("R8O8biBEb8SfdW11"), I0O1l0I1.b("w5Z6ZWw="));
   private final SliderSetting customTime = new SliderSetting(I0O1l0I1.b("w5Z6ZWwgWmFtYW4="), 6000.0, 0.0, 24000.0, 100.0, () -> this.timeBox.get() && this.timeMode.is(I0O1l0I1.b("w5Z6ZWw=")));
   private final BooleanSetting weatherBox = new BooleanSetting(I0O1l0I1.b("SGF2YSBEdXJ1bXUgRGXEn2nFn3Rpcg=="), true);
   private final ModeSetting weatherMode = new ModeSetting(this.weatherBox::get, I0O1l0I1.b("SGF2YSBEdXJ1bXU="), I0O1l0I1.b("QcOnxLFr"), I0O1l0I1.b("QcOnxLFr"), I0O1l0I1.b("WWHEn211cmx1"), I0O1l0I1.b("RsSxcnTEsW5hbMSx"));
   public final BooleanSetting fog = new BooleanSetting(I0O1l0I1.b("U2lz"), false);
   public final SliderSetting fogEnd = new SliderSetting(I0O1l0I1.b("U2lzIE1lc2FmZXNp"), 200.0, 0.0, 500.0, 1.0, this.fog::get);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public World() {
      this.addSettings(new Setting[]{this.timeBox, this.timeMode, this.customTime, this.weatherBox, this.weatherMode, this.fog, this.fogEnd});
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
               if (mc.world == null) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handlePacketEventInternal(event);
               handleUpdateEventInternal(event);
               handleFogEventInternal(event);
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

   private void handlePacketEventInternal(Event event) {
      l1O0I1lO.fakeHandler();
      if (event instanceof EventPacket packet) {
         if (I1lO0l1I.and(this.timeBox.get(), packet.getPacket() instanceof WorldTimeUpdateS2CPacket)) {
            if (l1O0I1lO.opaqueTrue()) {
               packet.setCancel(true);
            }
         }
      }
   }

   private void handleUpdateEventInternal(Event event) {
      l1O0I1lO.fakeHandler();
      if (event instanceof EventUpdate) {
         if (this.timeBox.get()) {
            if (l1O0I1lO.opaqueTrue()) {
               mc.world.setTime(this.resolveTime(), this.resolveTime(), false);
            }
         }

         if (this.weatherBox.get()) {
            applyWeatherInternal();
         }
      }
   }

   private void applyWeatherInternal() {
      l1O0I1lO.fakeHandler();
      String mode = this.weatherMode.get();
      if (l1O0I1lO.opaqueTrue()) {
         if (mode.equals(I0O1l0I1.b("QcOnxLFr"))) {
               mc.world.setRainGradient(lO1I0l1O.f(0.0F));
               mc.world.setThunderGradient(lO1I0l1O.f(0.0F));
         } else if (mode.equals(I0O1l0I1.b("WWHEn211cmx1"))) {
               mc.world.setRainGradient(lO1I0l1O.f(1.0F));
               mc.world.setThunderGradient(lO1I0l1O.f(0.0F));
         } else if (mode.equals(I0O1l0I1.b("RsSxcnTEsW5hbMSx"))) {
               mc.world.setRainGradient(lO1I0l1O.f(1.0F));
               mc.world.setThunderGradient(lO1I0l1O.f(1.0F));
         }
      }
   }

   private void handleFogEventInternal(Event event) {
      l1O0I1lO.fakeHandler();
      if (event instanceof EventFog fogEvent && this.fog.get()) {
         if (l1O0I1lO.opaqueTrue()) {
            int themeColor = ColorUtil.gradient(lO1I0l1O.i(15), lO1I0l1O.i(360), Manager.STYLE_MANAGER.getFirstColor(), Manager.STYLE_MANAGER.getSecondColor());
            fogEvent.r = (float)(themeColor >> lO1I0l1O.i(16) & 0xFF) / lO1I0l1O.f(255.0F);
            fogEvent.g = (float)(themeColor >> lO1I0l1O.i(8) & 0xFF) / lO1I0l1O.f(255.0F);
            fogEvent.b = (float)(themeColor & 0xFF) / lO1I0l1O.f(255.0F);
            fogEvent.alpha = lO1I0l1O.f(1.0F);
            fogEvent.start = lO1I0l1O.f(0.0F);
            fogEvent.end = this.fogEnd.get().floatValue();
            fogEvent.shape = FogShape.SPHERE;
            fogEvent.modified = true;
         }
      }
   }

   private long resolveTime() {
      l1O0I1lO.fakeHandler();
      String mode = this.timeMode.get();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.l(entropy);
      }

      if (mode.equals(I0O1l0I1.b("R8O8bmTDvHo="))) return lO1I0l1O.l(1000);
      if (mode.equals(I0O1l0I1.b("R2VjZQ=="))) return lO1I0l1O.l(13000);
      if (mode.equals(I0O1l0I1.b("U2FiYWg="))) return lO1I0l1O.l(0);
      if (mode.equals(I0O1l0I1.b("R8O8biBEb8SfdW11"))) return lO1I0l1O.l(23000);
      if (mode.equals(I0O1l0I1.b("w5Z6ZWw="))) return (long)this.customTime.get().floatValue();
      return lO1I0l1O.l(6000);
   }
}
