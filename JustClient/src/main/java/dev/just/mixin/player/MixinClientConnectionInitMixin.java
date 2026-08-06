package dev.just.mixin.player;

import dev.just.manager.Manager;
import dev.just.manager.proxyManager.Proxy;
import dev.just.manager.proxyManager.ProxyManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import java.net.InetSocketAddress;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.network.ClientConnection$1"}
)
public class MixinClientConnectionInitMixin {
   @Inject(
      method = {"initChannel(Lio/netty/channel/Channel;)V"},
      at = {@At("HEAD")}
   )
   private void connect(Channel channel, CallbackInfo ci) {
      Proxy proxy = ProxyManager.proxy;
      if (ProxyManager.proxyEnabled && proxy != null) {
         ProxyManager.lastUsedProxy = proxy;
         if (proxy.type == Proxy.ProxyType.SOCKS5) {
            channel.pipeline()
               .addFirst(
                  new ChannelHandler[]{
                     new Socks5ProxyHandler(
                        new InetSocketAddress(proxy.getIp(), proxy.getPort()),
                        proxy.username.isEmpty() ? null : proxy.username,
                        proxy.password.isEmpty() ? null : proxy.password
                     )
                  }
               );
         } else if (proxy.type == Proxy.ProxyType.SOCKS4) {
            channel.pipeline()
               .addFirst(
                  new ChannelHandler[]{
                     new Socks4ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.username.isEmpty() ? null : proxy.username)
                  }
               );
         }
      } else {
         ProxyManager.lastUsedProxy = new Proxy();
      }

      if (ProxyManager.proxyMenuButton != null) {
         ProxyManager.proxyMenuButton.setMessage(Text.literal("Proxy: " + ProxyManager.getLastUsedProxyIp()));
      }
   }
}
