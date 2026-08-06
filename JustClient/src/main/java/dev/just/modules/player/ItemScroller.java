package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ItemScroller",
   desc = "SMSxemzEsSBlxZ95YSB0YcWfxLFtYS9rYXlkxLFybWE=",
   type = Type.Player
)
public class ItemScroller extends Function {
   public SliderSetting scroll = new SliderSetting(I0O1l0I1.b("R2VjaWttZQ=="), 100.0, 1.0, 100.0, 1.0);

   public ItemScroller() {
      this.addSettings(new Setting[]{this.scroll});
   }

   @Override
   public void onEvent(Event event) {
   }
}
