package dev.just.mixin.display;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.world.GameMode;
import net.minecraft.scoreboard.Team;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Nullables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerListHud.class})
public class MixinPlayerListHud {
   private static final Comparator<PlayerListEntry> ENTRY_ORDERING = Comparator.<PlayerListEntry>comparingInt(entry -> entry.getGameMode() == GameMode.SPECTATOR ? 1 : 0)
      .thenComparing(entry -> (String)Nullables.mapOrElse(entry.getScoreboardTeam(), Team::getName, ""))
      .thenComparing(entry -> entry.getProfile().getName(), String::compareToIgnoreCase);

   @Inject(
      method = {"collectPlayerEntries"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void collectPlayerEntriesHook(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
      if (IMinecraft.mc.player != null && IMinecraft.mc.player.networkHandler != null && Manager.FUNCTION_MANAGER != null) {
         int limit = (Manager.FUNCTION_MANAGER.extraTab != null && Manager.FUNCTION_MANAGER.extraTab.state) ? 200 : 80;
         List<PlayerListEntry> list = new ArrayList<>(IMinecraft.mc.player.networkHandler.getListedPlayerListEntries());
         list.sort(ENTRY_ORDERING);
         if (list.size() > limit) {
            list = list.subList(0, limit);
         }

         cir.setReturnValue(list);
      }
   }
}
