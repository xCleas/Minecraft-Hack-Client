package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.I0O1l0I1;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@FunctionAnnotation(
   name = "Velocity",
   keywords = {"AKB", "AntiKnockBack", "R2VyaSBUZXBtZQ=="},
   type = Type.Combat,
   desc = "QWzEsW5hbiBnZXJpIHRlcG1leWkgdmUgc2F2cnVsbWF5xLEgdGFtYW1lbiBpcHRhbCBlZGVy"
)
public class lI0O1I0l extends Function {
   public ModeSetting mode = new ModeSetting("Mod", I0O1l0I1.b("xLBwdGFs"), I0O1l0I1.b("xLBwdGFs"), "Grim");

   public lI0O1I0l() {
      this.addSettings(new Setting[]{this.mode});
   }

   @Override
   public void onEvent(Event event) {
      if (mc.player == null) return;
      if (!(event instanceof EventPacket)) return;

      EventPacket eventPacket = (EventPacket) event;
      if (eventPacket == null || eventPacket.getPacket() == null) return;

      String currentMode = this.mode.get();
      if (currentMode == null) return;

      // İptal modu
      if (currentMode.equals("\u0130ptal") || currentMode.equals("İptal")) {
         handleCancel(eventPacket);
      }
   }

   private void handleCancel(EventPacket eventPacket) {
      if (eventPacket == null) return;
      if (!(eventPacket.getPacket() instanceof EntityVelocityUpdateS2CPacket)) return;

      EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket) eventPacket.getPacket();
      if (packet == null || mc.player == null) return;

      if (packet.getEntityId() == mc.player.getId()) {
         eventPacket.setCancel(true);
      }
   }
}
