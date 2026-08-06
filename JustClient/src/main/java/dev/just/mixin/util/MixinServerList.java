package dev.just.mixin.util;

import com.llamalad7.mixinextras.sugar.Local;
import dev.just.manager.ClientManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtElement;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerList.class})
public class MixinServerList {
   @Unique
   private static final List<ServerInfo> serverTop = List.of(new ServerInfo("Just Network", "play.justnw.com.tr", ServerInfo.ServerType.OTHER));
   @Shadow
   @Final
   private List<ServerInfo> servers;
   @Shadow
   @Final
   private List<ServerInfo> hiddenServers;

   @Inject(
      method = {"loadFile"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/option/ServerList;hiddenServers:Ljava/util/List;",
         ordinal = 0
      )}
   )
   private void loadFileHook(CallbackInfo ci) {
      if (ClientManager.legitMode) {
         this.servers.removeIf(si -> this.serverTop.stream().anyMatch(topx -> topx.address.equals(si.address)));
      } else {
         for (ServerInfo top : this.serverTop) {
            boolean exists = this.servers.stream().anyMatch(si -> si.address.equals(top.address));
            if (!exists) {
               this.servers.add(top);
            }
         }
      }

      this.removeDuplicates();
   }

   @Unique
   private void removeDuplicates() {
      this.removeDuplicatesFromList(this.servers);
      this.removeDuplicatesFromList(this.hiddenServers);
   }

   @Unique
   private void removeDuplicatesFromList(List<ServerInfo> list) {
      Set<String> seen = new HashSet<>();
      list.removeIf(si -> !seen.add(si.address));
   }

   @Redirect(
      method = {"saveFile"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/nbt/NbtList;add(Ljava/lang/Object;)Z"
      )
   )
   private boolean saveFileHook(NbtList instance, Object o, @Local(ordinal = 0) ServerInfo info) {
      // serverTop'taki sunucuları kaydetme (her başlangıçta zaten ekleniyor)
      // Diğer tüm sunucuları KAYDET
      boolean isTopServer = this.serverTop.stream().anyMatch(top -> top.address.equals(info.address));
      if (!ClientManager.legitMode && isTopServer) {
         return true; // serverTop sunucusu, kaydetme
      }
      return instance.add((NbtElement)o); // Diğer sunucuları kaydet
   }
}
