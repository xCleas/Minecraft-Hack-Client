package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ServerRPSpoff",
   desc = "U3VudWN1IGRva3UgcGFrZXRsZXJpbmkgaW5kaXJtZWRlbiBrYWJ1bCBlZGlsbWnFnyBnaWJpIGfDtnN0ZXJpcg==",
   type = Type.Misc
)
public class ServerRPSpoff extends Function {
   @Override
   public void onEvent(Event event) {
      if (event instanceof EventPacket packetEvent && packetEvent.getPacket() instanceof ResourcePackSendS2CPacket packet) {
         ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
         if (networkHandler != null) {
            networkHandler.sendPacket(new ResourcePackStatusC2SPacket(packet.id(), Status.ACCEPTED));
            networkHandler.sendPacket(new ResourcePackStatusC2SPacket(packet.id(), Status.SUCCESSFULLY_LOADED));
         }

         event.setCancel(true);
      }
   }
}
