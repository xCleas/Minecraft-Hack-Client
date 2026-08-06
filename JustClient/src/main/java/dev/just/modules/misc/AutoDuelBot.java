package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.player.TimerUtil;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoDuelBot",
   type = Type.Misc,
   desc = "RMO8ZWxsbyBkdXl1cnVzdSB5YXBhciB2ZSB0ZWtsaWZsZXJpIG90b21hdGlrIGthYnVsIGVkZXIu"
)
public class AutoDuelBot extends Function {
   private final ModeSetting chatMode = new ModeSetting(I0O1l0I1.b("U29oYmV0"), I0O1l0I1.b("WWVyZWw="), I0O1l0I1.b("WWVyZWw="), "Global");
   private final SliderSetting minMoney = new SliderSetting(I0O1l0I1.b("TWluIFBhcmE="), 1000.0, 1000.0, 1000000.0, 1000.0);
   private final SliderSetting maxMoney = new SliderSetting(I0O1l0I1.b("TWF4IFBhcmE="), 1000000.0, 1000.0, 1000000.0, 1000.0);
   private final SliderSetting messageDelay = new SliderSetting(I0O1l0I1.b("TWVzYWogR2VjaWttZXNp"), 5000.0, 3000.0, 30000.0, 1000.0);
   private final TimerUtil timerUtil = new TimerUtil();
   private String lastNick = null;
   private int lastBet = -1;

   public AutoDuelBot() {
      this.addSettings(new Setting[]{this.chatMode, this.minMoney, this.maxMoney, this.messageDelay});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         if (this.timerUtil.hasTimeElapsed(this.messageDelay.get().longValue())) {
            String msg = I0O1l0I1.b("SGVya2VzIGTDvGVsbG8gYXRzxLFuISBCYWhpczog") + this.minMoney.get().intValue() + I0O1l0I1.b("IGlsZSA=") + this.maxMoney.get().intValue() + I0O1l0I1.b("IGFyYXPEsS4=");
            if (this.chatMode.is("Global")) {
               msg = "! " + msg;
            }

            mc.player.networkHandler.sendChatMessage(msg);
            this.timerUtil.reset();
         }

         if (this.lastNick != null && this.lastBet >= 0) {
            if (this.lastBet >= this.minMoney.get().intValue() && this.lastBet <= this.maxMoney.get().intValue()) {
               mc.player.networkHandler.sendChatCommand("duel accept " + this.lastNick);
            }

            this.lastNick = null;
            this.lastBet = -1;
         }
      } else if (event instanceof EventPacket eventPacket && eventPacket.getPacket() instanceof GameMessageS2CPacket packet) {
         String text = packet.content().getString();
         if (text.contains("➝ Isim: ") || text.contains(I0O1l0I1.b("4p6dIMSwc2ltOiA=")) || text.contains("➝ Name: ")) {
            this.lastNick = text.split(": ")[1].trim();
         } else if (text.contains("➝ Bahis: ") || text.contains("➝ Bet: ")) {
            try {
               String betValue = text.split(": ")[1].replaceAll("[^0-9]", "").trim();
               this.lastBet = Integer.parseInt(betValue);
            } catch (Exception var6) {
            }
         }
      }
   }
}
