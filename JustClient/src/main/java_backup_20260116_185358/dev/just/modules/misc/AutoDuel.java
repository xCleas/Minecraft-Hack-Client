package dev.just.modules.misc;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.misc.autoduel.Counter;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.modules.setting.TextSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "AutoDuel",
   type = Type.Misc,
   desc = "T3RvbWF0aWsgb2xhcmFrIGTDvGVsbG8gaXN0ZcSfaSBnw7ZuZGVyaXI="
)
public class AutoDuel extends Function {
   private final Pattern pattern = Pattern.compile("^\\w{3,16}$");
   private final ModeSetting mode = new ModeSetting(
      Strings.b("TW9k"), Strings.b("S8O8cmVsZXI="), Strings.b("S8O8cmVsZXI="), Strings.b("S2Fsa2Fu"), Strings.b("RGlrZW5sZXIgMw=="), "Netherite", Strings.b("SGlsZWxpIENlbm5ldA=="), Strings.b("WWF5"), Strings.b("S2xhc2lr"), Strings.b("VG90ZW1sZXI="), "NoDebuff"
   );
   private SliderSetting slowTime = new SliderSetting(Strings.b("R8O2bmRlcmltIEjEsXrEsQ=="), 500.0, 300.0, 1000.0, 100.0);
   private final BooleanSetting babki = new BooleanSetting(Strings.b("UGFyYXPEsW5hIE95bmE="), false);
   private TextSetting money = new TextSetting(Strings.b("TWlrdGFy"), "10000", () -> this.babki.get());
   private double lastPosX;
   private double lastPosY;
   private double lastPosZ;
   private final List<String> sent = Lists.newArrayList();
   private final Counter counter = Counter.create();
   private final Counter counter2 = Counter.create();
   private final Counter counterChoice = Counter.create();
   private final Counter counterTo = Counter.create();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public AutoDuel() {
      this.addSettings(new Setting[]{this.mode, this.slowTime, this.babki, this.money});
   }

   @Override
   public void onEnable() {
      FlowObfuscator.fakeHandler();
      this.counter.reset();
      this.counter2.reset();
      this.counterChoice.reset();
      this.counterTo.reset();
      this.sent.clear();
      entropy = System.nanoTime();
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  this.updateLogic();
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventPacket eventPacket) {
                  this.packetLogic(eventPacket);
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void updateLogic() {
      List<String> players = this.getOnlinePlayers();
      double distance = Math.sqrt(
         Math.pow(this.lastPosX - mc.player.getX(), 2.0)
            + Math.pow(this.lastPosY - mc.player.getY(), 2.0)
            + Math.pow(this.lastPosZ - mc.player.getZ(), 2.0)
      );
      if (distance > 500.0) {
         this.toggle();
      }

      this.lastPosX = mc.player.getX();
      this.lastPosY = mc.player.getY();
      this.lastPosZ = mc.player.getZ();
      if (this.counter2.hasReached(800L * (long)players.size())) {
         this.sent.clear();
         this.counter2.reset();
      }

      for (String player : players) {
         if (!this.sent.contains(player) && !player.equals(mc.player.getGameProfile().getName()) && this.counter.hasReached(this.slowTime.get().longValue())) {
            if (this.babki.get()) {
               mc.player.networkHandler.sendCommand("duel " + player + " " + this.money.getValue());
            } else {
               mc.player.networkHandler.sendCommand("duel " + player);
            }

            this.sent.add(player);
            this.counter.reset();
         }
      }

      if (mc.currentScreen != null) {
         ScreenHandler var8 = mc.player.currentScreenHandler;
         if (var8 instanceof ScreenHandler) {
            String title = mc.currentScreen.getTitle().getString();
            if (title.contains("Kit Secimi") || title.contains("Kit Selection")) {
               int slotID = -1;
               if (this.counterChoice.hasReached(150L)) {
                  if (this.mode.is(Strings.b("S2Fsa2Fu"))) {
                     slotID = 0;
                  }

                  if (this.mode.is(Strings.b("RGlrZW5sZXIgMw=="))) {
                     slotID = 1;
                  }

                  if (this.mode.is(Strings.b("WWF5"))) {
                     slotID = 2;
                  }

                  if (this.mode.is(Strings.b("VG90ZW1sZXI="))) {
                     slotID = 3;
                  }

                  if (this.mode.is("NoDebuff")) {
                     slotID = 4;
                  }

                  if (this.mode.is(Strings.b("S8O8cmVsZXI="))) {
                     slotID = 5;
                  }

                  if (this.mode.is(Strings.b("S2xhc2lr"))) {
                     slotID = 6;
                  }

                  if (this.mode.is(Strings.b("SGlsZWxpIENlbm5ldA=="))) {
                     slotID = 7;
                  }

                  if (this.mode.is("Netherite")) {
                     slotID = 8;
                  }

                  if (slotID >= 0) {
                     mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slotID, 0, SlotActionType.QUICK_MOVE, mc.player);
                  }

                  this.counterChoice.reset();
               }
            } else if ((title.contains(Strings.b("RMO8ZWxsbyBBeWFybGFyxLE=")) || title.contains("Duel Ayarlari")) && this.counterTo.hasReached(150L)) {
               mc.interactionManager.clickSlot(var8.syncId, 0, 0, SlotActionType.QUICK_MOVE, mc.player);
               this.counterTo.reset();
            }
         }
      }
   }

   private void packetLogic(EventPacket event) {
      if (event.isReceivePacket() && event.getPacket() instanceof GameMessageS2CPacket chat) {
         String text = chat.content().toString().toLowerCase();
         if (text.contains(Strings.b("YmHFn2zEsXlvcg==")) && text.contains(Strings.b("c2FuaXll")) || text.contains(Strings.b("a29tdXQga3VsbGFubWFrIHlhc2FrdMSxcg=="))) {
            this.toggle();
         }
      }
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
