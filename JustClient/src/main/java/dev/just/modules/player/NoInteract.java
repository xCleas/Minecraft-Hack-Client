package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "NoInteract",
   desc = "U2HEnyB0xLFrIGlsZSBrb250ZXluZXIvYmxvayBhw6dtYW7EsXrEsSBlbmdlbGxlcg==",
   type = Type.Player
)
public class NoInteract extends Function {
   public final BooleanSetting onlyAura = new BooleanSetting(I0O1l0I1.b("U2FkZWNlIEF0dGFja0F1cmEgaWxl"), false);

   public NoInteract() {
      this.addSettings(new Setting[]{this.onlyAura});
   }

   @Override
   public void onEvent(Event event) {
   }
}
