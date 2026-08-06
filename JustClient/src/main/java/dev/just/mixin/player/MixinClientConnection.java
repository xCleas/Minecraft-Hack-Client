package dev.just.mixin.player;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.manager.IMinecraft;
import dev.just.util.move.NetworkUtils;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientConnection.class})
public class MixinClientConnection implements IMinecraft {
   @Inject(
      method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPacketReceived(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
      EventPacket event = new EventPacket(packet, EventPacket.PacketType.RECEIVE);
      Event.call(event);
      if (event.isCancel()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"send(Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPacketSend(Packet<?> packet, CallbackInfo ci) {
      if (!NetworkUtils.isSendingSilent()) {
         EventPacket event = new EventPacket(packet, EventPacket.PacketType.SEND);
         Event.call(event);
         if (event.isCancel()) {
            ci.cancel();
         }
      }
   }
}
