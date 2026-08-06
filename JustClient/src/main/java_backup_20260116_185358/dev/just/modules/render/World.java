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
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.client.render.FogShape;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "World",
   desc = "R8O8bsO8biBzYWF0aW5pLCBoYXZhIGR1cnVtdW51IHZlIHNpc2kgZGXEn2nFn3Rpcm1lbml6aSBzYcSfbGFy",
   type = Type.Render
)
public class World extends Function {
   private final BooleanSetting timeBox = new BooleanSetting(Strings.b("WmFtYW7EsSBEZcSfacWfdGly"), true);
   private final ModeSetting timeMode = new ModeSetting(this.timeBox::get, Strings.b("R8O8bsO8biBTYWF0aQ=="), Strings.b("R8O8bmTDvHo="), Strings.b("R8O8bmTDvHo="), Strings.b("R2VjZQ=="), Strings.b("U2FiYWg="), Strings.b("R8O8biBEb8SfdW11"), Strings.b("w5Z6ZWw="));
   private final SliderSetting customTime = new SliderSetting(Strings.b("w5Z6ZWwgWmFtYW4="), 6000.0, 0.0, 24000.0, 100.0, () -> this.timeBox.get() && this.timeMode.is(Strings.b("w5Z6ZWw=")));
   private final BooleanSetting weatherBox = new BooleanSetting(Strings.b("SGF2YSBEdXJ1bXUgRGXEn2nFn3Rpcg=="), true);
   private final ModeSetting weatherMode = new ModeSetting(this.weatherBox::get, Strings.b("SGF2YSBEdXJ1bXU="), Strings.b("QcOnxLFr"), Strings.b("QcOnxLFr"), Strings.b("WWHEn211cmx1"), Strings.b("RsSxcnTEsW5hbMSx"));
   public final BooleanSetting fog = new BooleanSetting(Strings.b("U2lz"), false);
   public final SliderSetting fogEnd = new SliderSetting(Strings.b("U2lzIE1lc2FmZXNp"), 200.0, 0.0, 500.0, 1.0, this.fog::get);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public World() {
      this.addSettings(new Setting[]{this.timeBox, this.timeMode, this.customTime, this.weatherBox, this.weatherMode, this.fog, this.fogEnd});
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

   private void handlePacketEventInternal(Event event) {
      FlowObfuscator.fakeHandler();
      if (event instanceof EventPacket packet) {
         if (LogicSplit.and(this.timeBox.get(), packet.getPacket() instanceof WorldTimeUpdateS2CPacket)) {
            if (FlowObfuscator.opaqueTrue()) {
               packet.setCancel(true);
            }
         }
      }
   }

   private void handleUpdateEventInternal(Event event) {
      FlowObfuscator.fakeHandler();
      if (event instanceof EventUpdate) {
         if (this.timeBox.get()) {
            if (FlowObfuscator.opaqueTrue()) {
               mc.world.setTime(this.resolveTime(), this.resolveTime(), false);
            }
         }

         if (this.weatherBox.get()) {
            applyWeatherInternal();
         }
      }
   }

   private void applyWeatherInternal() {
      FlowObfuscator.fakeHandler();
      String mode = this.weatherMode.get();
      if (FlowObfuscator.opaqueTrue()) {
         if (mode.equals(Strings.b("QcOnxLFr"))) {
               mc.world.setRainGradient(NumberGuard.f(0.0F));
               mc.world.setThunderGradient(NumberGuard.f(0.0F));
         } else if (mode.equals(Strings.b("WWHEn211cmx1"))) {
               mc.world.setRainGradient(NumberGuard.f(1.0F));
               mc.world.setThunderGradient(NumberGuard.f(0.0F));
         } else if (mode.equals(Strings.b("RsSxcnTEsW5hbMSx"))) {
               mc.world.setRainGradient(NumberGuard.f(1.0F));
               mc.world.setThunderGradient(NumberGuard.f(1.0F));
         }
      }
   }

   private void handleFogEventInternal(Event event) {
      FlowObfuscator.fakeHandler();
      if (event instanceof EventFog fogEvent && this.fog.get()) {
         if (FlowObfuscator.opaqueTrue()) {
            int themeColor = ColorUtil.gradient(NumberGuard.i(15), NumberGuard.i(360), Manager.STYLE_MANAGER.getFirstColor(), Manager.STYLE_MANAGER.getSecondColor());
            fogEvent.r = (float)(themeColor >> NumberGuard.i(16) & 0xFF) / NumberGuard.f(255.0F);
            fogEvent.g = (float)(themeColor >> NumberGuard.i(8) & 0xFF) / NumberGuard.f(255.0F);
            fogEvent.b = (float)(themeColor & 0xFF) / NumberGuard.f(255.0F);
            fogEvent.alpha = NumberGuard.f(1.0F);
            fogEvent.start = NumberGuard.f(0.0F);
            fogEvent.end = this.fogEnd.get().floatValue();
            fogEvent.shape = FogShape.SPHERE;
            fogEvent.modified = true;
         }
      }
   }

   private long resolveTime() {
      FlowObfuscator.fakeHandler();
      String mode = this.timeMode.get();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.l(entropy);
      }

      if (mode.equals(Strings.b("R8O8bmTDvHo="))) return NumberGuard.l(1000);
      if (mode.equals(Strings.b("R2VjZQ=="))) return NumberGuard.l(13000);
      if (mode.equals(Strings.b("U2FiYWg="))) return NumberGuard.l(0);
      if (mode.equals(Strings.b("R8O8biBEb8SfdW11"))) return NumberGuard.l(23000);
      if (mode.equals(Strings.b("w5Z6ZWw="))) return (long)this.customTime.get().floatValue();
      return NumberGuard.l(6000);
   }
}
