package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;

@FunctionAnnotation(
   name = "IRC",
   desc = "YAKINDA",
   type = Type.Misc
)
public class IRC extends Function {
   @Override
   public void onEvent(Event event) {
   }

   @Override
   protected void onDisable() {
      Manager.IRC_MANAGER.shutdown();
      super.onDisable();
   }
}
