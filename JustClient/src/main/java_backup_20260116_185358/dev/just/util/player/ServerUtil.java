package dev.just.util.player;

import dev.just.manager.IMinecraft;
import java.util.Locale;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreHolder;

public class ServerUtil implements IMinecraft {
   public static void selectCompass() {
      int slot = InventoryUtil.getHotBarSlot(Items.COMPASS);
      if (slot != -1) {
         mc.player.getInventory().selectedSlot = slot;
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
      }
   }

   public static float getHealth(LivingEntity target) {
      if (mc.getCurrentServerEntry() == null) {
         return target.getHealth() / target.getMaxHealth();
      } else {
         String serverAddress = mc.getCurrentServerEntry().address.toLowerCase(Locale.ROOT);
         boolean isLocal = mc.isConnectedToLocalServer();
         if (isLocal || serverAddress.isEmpty()) {
            return target.getHealth() / target.getMaxHealth();
         } else if (target instanceof MobEntity) {
            return target.getHealth() / target.getMaxHealth();
         } else {
            if (serverAddress.contains("reallyworld")
               || serverAddress.contains("playrw")
               || serverAddress.contains("saturn-x")
               || serverAddress.contains("skytime")
               || serverAddress.contains("space-times")) {
               Scoreboard scoreboard = target.getWorld().getScoreboard();
               ScoreboardObjective scoreObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
               if (scoreObjective != null) {
                  try {
                     int hp = scoreboard.getOrCreateScore(ScoreHolder.fromName(target.getNameForScoreboard()), scoreObjective).getScore();
                     if (hp >= 0 && (float)hp <= target.getMaxHealth()) {
                        return (float)hp / target.getMaxHealth();
                     }
                  } catch (NumberFormatException var6) {
                  }
               }
            }

            return target.getHealth() / target.getMaxHealth();
         }
      }
   }

   public static boolean isConnected(String ip) {
      if (mc.getCurrentServerEntry() == null) {
         return false;
      } else {
         String serverAddress = mc.getCurrentServerEntry().address;
         return serverAddress != null && serverAddress.contains(ip);
      }
   }
}
