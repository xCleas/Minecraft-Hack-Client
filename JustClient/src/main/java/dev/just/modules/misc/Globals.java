package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.apiManager.ClientAPI;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.I0O1l0I1;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.PlayerEntity;

@FunctionAnnotation(
   name = "Globals",
   type = Type.Misc,
   desc = "YAKINDA"
)
public class Globals extends Function {
   private ClientAPI clientAPI;
   private final int port = 13599;
   private String playerName;
   public final Map<UUID, Boolean> isClientUserCache = new ConcurrentHashMap<>();

   public Globals() {
      this.addSettings(new Setting[0]);
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.clientAPI = new ClientAPI("1.4.3", 13599);
      this.isClientUserCache.clear();
      if (mc.player != null) {
         this.playerName = mc.player.getGameProfile().getName();
         this.clientAPI.addPlayer(this.playerName);
         ClientManager.message("[Globals] ClientAPI aktif edildi");
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.clear();
      ClientManager.message(I0O1l0I1.b("W0dsb2JhbHNdIENsaWVudEFQSSBrYXBhdMSxbGTEsQ=="));
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         if (mc.player != null && this.clientAPI != null) {
            if (mc.player.age % 30 == 0) {
               Set<UUID> active = ConcurrentHashMap.newKeySet();

               for (PlayerEntity player : Manager.SYNC_MANAGER.getPlayers()) {
                  if (player != null) {
                     UUID uuid = player.getUuid();
                     active.add(uuid);
                     String name = player.getGameProfile().getName();
                     this.clientAPI.isClientUserAsync(name, result -> {
                        if (result) {
                           this.isClientUserCache.put(uuid, true);
                        } else {
                           this.isClientUserCache.remove(uuid);
                        }
                     });
                  }
               }

               this.isClientUserCache.keySet().removeIf(uuidx -> !active.contains(uuidx));
            }
         }
      }
   }

   public void clear() {
      this.removePlayer();
      if (this.clientAPI != null) {
         this.clientAPI.shutdown();
      }

      this.clientAPI = null;
      this.isClientUserCache.clear();
   }

   private void removePlayer() {
      if (this.clientAPI != null && this.playerName != null) {
         this.clientAPI.removePlayer(this.playerName);
         this.playerName = null;
      }
   }
}
