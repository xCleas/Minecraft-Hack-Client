package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.move.MoveUtil;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "RWHelper",
   desc = "UmVhbGx5V29ybGQgc3VudWN1c3UgacOnaW4geWFyZMSxbWPEsSBvemVsbGlrbGVy",
   type = Type.Misc
)
public class RWHelper extends Function {
   private final BindBooleanSetting dragonFly = new BindBooleanSetting(Strings.b("RWpkZXJoYQ=="), Strings.b("RWpkZXJoYSDDvHplcmluZGUgZGFoYSBoxLF6bMSxIHXDp21hbsSxesSxIHNhxJ9sYXI="), true);

   public RWHelper() {
      this.addSettings(new Setting[]{this.dragonFly});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventMotion) {
         if (this.dragonFly.get() && mc.player.getAbilities().flying) {
            MoveUtil.setSpeed(1.0F);
            float y = 0.0F;
            boolean noForward = mc.player.forwardSpeed == 0.0F && !mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed();
            if (mc.options.jumpKey.isPressed()) {
               y = noForward ? 0.5F : 0.25F;
            } else if (mc.options.sneakKey.isPressed()) {
               y = noForward ? -0.5F : -0.25F;
            }

            mc.player.setVelocity(mc.player.getVelocity().x, (double)y, mc.player.getVelocity().z);
         }
      }
   }
}
