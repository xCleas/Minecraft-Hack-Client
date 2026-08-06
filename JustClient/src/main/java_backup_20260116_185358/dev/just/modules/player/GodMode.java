package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.util.player.TimerUtil;
import net.minecraft.util.Formatting;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "GodMode",
   type = Type.Player,
   desc = "QmVsaXJsaSBzdW51Y3VsYXJkYSDDtmzDvG1zw7x6bMO8ayBleHBsb2l0aW5pIGFrdGlmIGVkZXI="
)
public class GodMode extends Function {
   TimerUtil timerUtil = new TimerUtil();
   private long lastClickTime = 0L;
   private boolean firstClick = true;

   public GodMode() {
      this.addSettings(new Setting[0]);
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventPacket eventPacket && eventPacket.getPacket() instanceof GameMessageS2CPacket packet) {
         String message = packet.content().getString();
         if (message.contains("PVP modundayken isinlanamazsiniz")
            || message.contains(Strings.b("UFZQIG1vZHVuZGF5a2VuIMSxxZ/EsW5sYW5hbWF6c8SxbsSxeg=="))
            || message.contains("Farm warpina basariyla isinlandiniz!")
            || message.contains(Strings.b("RmFybSB3YXJwxLFuYSBiYcWfYXLEsXlsYSDEscWfxLFubGFuZMSxbsSxeiE="))) {
            eventPacket.setCancel(true);
         }
      }

      if (event instanceof EventUpdate) {
         if (this.timerUtil.hasTimeElapsed(8000L)) {
            ClientManager.message(
               Formatting.WHITE + Strings.b("R29kTW9kZSBldGtpbmtlbg==") + Formatting.RED + Strings.b("IEjEsMOHQsSwUiA=") + Formatting.WHITE + Strings.b("ZcWfeWEgZGXEn2nFn3Rpcm1lIChzd2FwKSBrdWxsYW7EsWxhbWF6IQ==")
            );
         }

         this.timerUtil.reset();
         if (mc.currentScreen instanceof DeathScreen) {
            this.toggle();
         }
      }

      if (event instanceof EventUpdate && ClientManager.playerIsPVP()) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.lastClickTime >= 35L) {
            this.lastClickTime = currentTime;
            MinecraftClient.getInstance().execute(() -> mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 1, 1, SlotActionType.PICKUP, mc.player));
         }
      }
   }

   @Override
   public void onEnable() {
      this.firstClick = true;
      this.lastClickTime = System.currentTimeMillis();
      if (!ClientManager.playerIsPVP()) {
         mc.player.networkHandler.sendCommand("warp");
         new Thread(
               () -> {
                  try {
                     Thread.sleep(500L);
                     MinecraftClient.getInstance()
                        .execute(() -> mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 21, 1, SlotActionType.PICKUP, mc.player));
                  } catch (InterruptedException var1) {
                     var1.printStackTrace();
                  }
               }
            )
            .start();
         new Thread(() -> {
            try {
               Thread.sleep(700L);
               MinecraftClient.getInstance().execute(() -> {
                  mc.currentScreen = null;
                  mc.mouse.lockCursor();
               });
            } catch (InterruptedException var1) {
               var1.printStackTrace();
            }
         }).start();
      }
   }
}
