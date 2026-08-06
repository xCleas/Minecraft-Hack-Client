package dev.just.modules.player;

import com.mojang.authlib.GameProfile;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.player.EventAttack;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.combat.I1lO0Il1;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.modules.setting.TextSetting;
import dev.just.util.player.TimerUtil;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.network.PlayerListEntry;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoMessage",
   type = Type.Player,
   desc = "T3RvbWF0aWsgb2xhcmFrIHNvaGJldGUgbWVzYWogZ8O2bmRlcmlyLg=="
)
public class AutoMessage extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("R8O2bmRlcmltIMWeZWtsaQ=="), I0O1l0I1.b("w5Zsw7xtZGVuIFNvbnJh"), I0O1l0I1.b("w5Zsw7xtZGVuIFNvbnJh"), I0O1l0I1.b("U2FsZMSxcsSxIFPEsXJhc8SxbmRh"), I0O1l0I1.b("WmFtYW4gQXlhcmzEsQ=="));
   private final SliderSetting timer = new SliderSetting(
      I0O1l0I1.b("R2VjaWttZSAoTVMp"), 5000.0, 0.0, 35000.0, 1000.0, () -> this.mode.is(I0O1l0I1.b("WmFtYW4gQXlhcmzEsQ==")) || this.mode.is(I0O1l0I1.b("U2FsZMSxcsSxIFPEsXJhc8SxbmRh"))
   );
   private final TextSetting text = new TextSetting(I0O1l0I1.b("TWVzYWo="), I0O1l0I1.b("R8O8bGUgZ8O8bGUgJXRhcmdldCUh"));
   private final TimerUtil delayTimer = new TimerUtil();
   private LivingEntity lastTarget;
   private boolean waitingForDeath = false;
   private final Pattern pattern = Pattern.compile(".*");

   public AutoMessage() {
      this.addSettings(new Setting[]{this.mode, this.timer, this.text});
   }

   @Override
   public void onEvent(Event event) {
      if (mc.player != null && mc.world != null) {
         I1lO0Il1 aura = Manager.FUNCTION_MANAGER.attackAura;
         if (event instanceof EventAttack attackEvent && attackEvent.getTarget() instanceof LivingEntity entity && this.mode.is(I0O1l0I1.b("w5Zsw7xtZGVuIFNvbnJh"))) {
            this.lastTarget = entity;
            this.waitingForDeath = true;
         }

         if (event instanceof EventUpdate) {
            if (this.mode.is(I0O1l0I1.b("WmFtYW4gQXlhcmzEsQ=="))) {
               if (this.delayTimer.hasTimeElapsed(this.timer.get().longValue())) {
                  this.sendMessage(this.replaceTarget(this.text.getValue(), null));
                  this.delayTimer.reset();
               }

               return;
            }

            if (this.mode.is(I0O1l0I1.b("U2FsZMSxcsSxIFPEsXJhc8SxbmRh"))) {
               if (aura != null && aura.target != null && this.delayTimer.hasTimeElapsed(this.timer.get().longValue())) {
                  this.sendMessage(this.replaceTarget(this.text.getValue(), aura.target));
                  this.delayTimer.reset();
               }

               return;
            }

            if (this.mode.is(I0O1l0I1.b("w5Zsw7xtZGVuIFNvbnJh")) && this.waitingForDeath && this.lastTarget != null) {
               boolean dead = this.lastTarget.isDead() || this.lastTarget.getHealth() <= 0.0F;
               boolean unloaded = mc.world.getEntityById(this.lastTarget.getId()) == null;
               if (dead || unloaded) {
                  this.sendMessage(this.replaceTarget(this.text.getValue(), this.lastTarget));
                  this.waitingForDeath = false;
                  this.lastTarget = null;
                  this.delayTimer.reset();
               }
            }
         }
      }
   }

   private void sendMessage(String msg) {
      if (msg != null && !msg.trim().isEmpty()) {
         mc.player.networkHandler.sendChatMessage(msg);
      }
   }

   private String replaceTarget(String msg, LivingEntity target) {
      return msg.replace("%target%", target != null ? target.getName().getString() : "oyuncu");
   }

   private List<String> getOnlinePlayers() {
      return mc.player
         .networkHandler
         .getPlayerList()
         .stream()
         .map(PlayerListEntry::getProfile)
         .<String>map(GameProfile::getName)
         .filter(profileName -> this.pattern.matcher(profileName).matches())
         .collect(Collectors.toList());
   }
}
