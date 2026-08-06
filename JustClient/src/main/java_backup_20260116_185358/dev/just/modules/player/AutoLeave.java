package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.modules.setting.TextSetting;
import dev.just.protect.runtime.Strings;
import net.minecraft.text.Text;

@FunctionAnnotation(
   name = "AutoLeave",
   desc = "VGVobGlrZSBhbsSxbmRhIHN1bnVjdWRhbiBrYcOnYXIu",
   type = Type.Player
)
public class AutoLeave extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("VGV0aWtsZXlpY2k="), Strings.b("WWFrxLFuIE95dW5jdQ=="), Strings.b("WWFrxLFuIE95dW5jdQ=="), Strings.b("RMO8xZ/DvGsgQ2Fu"));
   private final SliderSetting heal = new SliderSetting(Strings.b("Q2FuIFPEsW7EsXLEsQ=="), 6.0, 1.0, 20.0, 1.0, () -> this.mode.is(Strings.b("RMO8xZ/DvGsgQ2Fu")));
   private final SliderSetting radius = new SliderSetting(Strings.b("TWVzYWZl"), 60.0, 20.0, 150.0, 1.0, () -> this.mode.is(Strings.b("WWFrxLFuIE95dW5jdQ==")));
   private final ModeSetting run = new ModeSetting(Strings.b("RXlsZW0="), Strings.b("U3VudWN1ZGFuIMOHxLFr"), Strings.b("U3VudWN1ZGFuIMOHxLFr"), "/lobby", "/home");
   private final TextSetting homeName = new TextSetting(Strings.b("RXYgQWTEsQ=="), "home", () -> this.run.is("/home"));
   private final BooleanSetting pvpNoLeave = new BooleanSetting(Strings.b("UHZQIFPEsXJhc8SxbmRhIMOHxLFrbWE="), true, Strings.b("UHZQIG1vZHVuZGF5a2VuIChjb21iYXQgbG9nKSBjZXphIHllbWVtZWsgacOnaW4gw6fEsWvEscWfxLEgZW5nZWxsZXIu"));
   private boolean triggered = false;

   public AutoLeave() {
      this.addSettings(new Setting[]{this.mode, this.heal, this.radius, this.run, this.homeName, this.pvpNoLeave});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         if (mc.player != null && mc.world != null) {
            if (!this.pvpNoLeave.get() || !ClientManager.playerIsPVP()) {
               boolean shouldTrigger = false;
               if (this.mode.is(Strings.b("RMO8xZ/DvGsgQ2Fu")) && mc.player.getHealth() <= this.heal.get().floatValue()) {
                  shouldTrigger = true;
               } else if (this.mode.is(Strings.b("WWFrxLFuIE95dW5jdQ=="))) {
                  shouldTrigger = Manager.SYNC_MANAGER
                     .getPlayers()
                     .stream()
                     .anyMatch(
                        other -> other != mc.player
                              && !Manager.FRIEND_MANAGER.isFriend(other.getName().getString())
                              && mc.player.distanceTo(other) <= this.radius.get().floatValue()
                     );
               }

               if (shouldTrigger && !this.triggered) {
                  this.executeAction();
                  this.triggered = true;
               } else if (!shouldTrigger) {
                  this.triggered = false;
               }
            }
         }
      }
   }

   private void executeAction() {
      if (mc.player != null) {
         if (this.run.is(Strings.b("U3VudWN1ZGFuIMOHxLFr"))) {
            mc.player.networkHandler.getConnection().disconnect(Text.literal(Strings.b("wqdjW0F1dG9MZWF2ZV0gVGVobGlrZSBBbGfEsWxhbmTEsSE=")));
         } else if (this.run.is("/lobby")) {
            mc.player.networkHandler.sendChatCommand("lobby");
         } else if (this.run.is("/home")) {
            mc.player.networkHandler.sendChatCommand("home " + this.homeName.getValue());
         }

         this.state = false;
      }
   }
}
