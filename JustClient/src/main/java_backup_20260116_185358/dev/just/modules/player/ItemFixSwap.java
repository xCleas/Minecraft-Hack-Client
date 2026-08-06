package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ItemFixSwap",
   keywords = {"NoSlotChange", "NoServerDesync", "SlotSabitleyici"},
   desc = "QW50aS1oaWxlIHNpc3RlbWluaW4gc2xvdHVudXp1IGRlxJ9pxZ90aXJtZXNpbmkgZW5nZWxsZXI=",
   type = Type.Player
)
public class ItemFixSwap extends Function {
   public ItemFixSwap() {
      this.addSettings(new Setting[0]);
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventPacket e && e.isReceivePacket() && e.getPacket() instanceof UpdateSelectedSlotC2SPacket packetHeldItemChange) {
         event.setCancel(true);
      }
   }
}
