package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventNoSlow;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.SemanticNoise;
import net.minecraft.util.Hand;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "NoSlow",
   desc = "RcWfeWEga3VsbGFuxLFya2VuIHlhdmHFn2xhbWFuxLF6xLEgZW5nZWxsZXI=",
   type = Type.Move
)
public class NoSlow extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("TW9k"), "Grim", "Grim", "ReallyWorld", "LonyGrief");
   private int ticks;

   private static final int MODE_GRIM = 0x4A ^ 0x4A;
   private static final int MODE_RW = 0x4B ^ 0x4A;
   private static final int MODE_LG = 0x48 ^ 0x4A;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public NoSlow() {
      this.addSettings(new Setting[]{this.mode});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               int modeId = resolveModeInternal();
               processEventInternal(event, modeId);
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entropy, _s);
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

   private int resolveModeInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(99);
      }
      if (this.mode.is("Grim")) return MODE_GRIM;
      if (this.mode.is("ReallyWorld")) return MODE_RW;
      if (this.mode.is("LonyGrief")) return MODE_LG;
      return NumberGuard.i(-1);
   }

   private void processEventInternal(Event event, int modeId) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (event instanceof EventUpdate) {
                  handleUpdateInternal(modeId);
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventNoSlow eventNoSlow) {
                  handleNoSlowInternal(eventNoSlow, modeId);
               }
               _s = 4;
               break;

            case 2:
               SemanticNoise.deadCode2();
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void handleUpdateInternal(int modeId) {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!LogicSplit.equals(modeId, MODE_RW)) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (mc.player.isGliding()) {
                  _s = 4;
                  break;
               }
               if (FlowObfuscator.opaqueFalse()) {
                  ticks = NumberGuard.i(999);
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (mc.player.isUsingItem()) {
                  ticks++;
               } else {
                  ticks = NumberGuard.i(0);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void handleNoSlowInternal(EventNoSlow eventNoSlow, int modeId) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (LogicSplit.equals(modeId, MODE_GRIM)) {
                  handleGrimInternal(eventNoSlow);
               } else if (LogicSplit.equals(modeId, MODE_RW)) {
                  handleReallyWorldInternal(eventNoSlow);
               } else if (LogicSplit.equals(modeId, MODE_LG)) {
                  handleLonyGriefInternal(eventNoSlow);
               }
               _s = 4;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= modeId;
               }
               _s = 4;
               break;

            case 2:
               FlowObfuscator.fakeBranch(modeId, entropy);
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void handleGrimInternal(EventNoSlow eventNoSlow) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         eventNoSlow.setCancel(true);
      }
   }

   private void handleReallyWorldInternal(EventNoSlow eventNoSlow) {
      FlowObfuscator.fakeHandler();
      int t1 = NumberGuard.i(1);
      int t2 = NumberGuard.i(2);
      if (LogicSplit.or(ticks == t1, ticks == t2)) {
         eventNoSlow.setCancel(true);
      }
      if (ticks >= t2) {
         ticks = NumberGuard.i(0);
      }
      if (ticks == 0) {
         eventNoSlow.setCancel(false);
      }
   }

   private void handleLonyGriefInternal(EventNoSlow eventNoSlow) {
      FlowObfuscator.fakeHandler();
      Hand active = mc.player.getActiveHand();
      if (active == null) return;
      if (FlowObfuscator.opaqueFalse()) {
         return;
      }
      Hand opposite = active == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(opposite, NumberGuard.i(0), mc.player.getYaw(), mc.player.getPitch()));
         eventNoSlow.setCancel(true);
      }
   }
}
