package dev.just.events;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.Function;

public class Event implements IMinecraft {
   public boolean isCancel;

   public boolean isCancel() {
      return this.isCancel;
   }

   public void setCancel(boolean cancel) {
      this.isCancel = cancel;
   }

   public static void call(Event event) {
      if (Manager.SYNC_MANAGER != null && Manager.FUNCTION_MANAGER != null) {
         if (mc.player != null && mc.world != null && !event.isCancel()) {
            if (!ClientManager.legitMode) {
               for (Function module : Manager.FUNCTION_MANAGER.getFunctions()) {
                  if (module != null && module.isState()) {
                     module.onEvent(event);
                  }
               }

               Manager.SYNC_MANAGER.onEvent(event);
            }
         }
      }
   }
}
