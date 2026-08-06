package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.util.player.TimerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "GuiWalk",
   keywords = {"InventoryMove", "GuiMove"},
   type = Type.Player,
   desc = "RW52YW50ZXIgYcOnxLFra2VuIGhhcmVrZXQgZXRtZW5pemkgc2HEn2xhcg=="
)
public class GuiWalk extends Function {
   public final ModeSetting bypass = new ModeSetting(Strings.b("QnlwYXNzIFTDvHLDvA=="), Strings.b("Tm9ybWFs"), Strings.b("Tm9ybWFs"), "FunTime");
   private final Queue<ClickSlotC2SPacket> packetQueue = new ConcurrentLinkedQueue<>();
   private boolean wasInventoryOpen = false;
   private final TimerUtil timer = new TimerUtil();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public GuiWalk() {
      this.addSettings(new Setting[]{this.bypass});
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
               processGuiWalkInternal();
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, FAKE_STATE);
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

   private void processGuiWalkInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 7);
      List<KeyBinding> keyBindings = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               keyBindings = new ArrayList<>(
                  Arrays.asList(mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey)
               );
               _s = 1;
               break;

            case 1:
               if (LogicSplit.or(mc.currentScreen instanceof ChatScreen, mc.currentScreen instanceof SignEditScreen)) {
                  disableAllKeys(keyBindings);
                  _s = 6;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (LogicSplit.all(
                  this.bypass.is("FunTime"),
                  LogicSplit.not(this.packetQueue.isEmpty()),
                  LogicSplit.not(this.timer.hasTimeElapsed(NumberGuard.l(100)))
               )) {
                  disableAllKeys(keyBindings);
                  _s = 6;
                  break;
               }
               _s = 3;
               break;

            case 3:
               keyBindings.forEach(this::updateKeyBindingInternal);
               handleBypassLogic();
               _s = 6;
               break;

            case 4:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= keyBindings.size();
               }
               _s = 6;
               break;

            case 5:
               FlowObfuscator.fakeBranch(keyBindings, entropy);
               _s = 6;
               break;

            case 6:
               return;

            default:
               _s = 6;
               break;
         }
      }
   }

   private void disableAllKeys(List<KeyBinding> keyBindings) {
      FlowObfuscator.fakeHandler();
      for (KeyBinding keyBinding : keyBindings) {
         keyBinding.setPressed(false);
         if (FlowObfuscator.opaqueFalse()) {
            entropy ^= keyBinding.hashCode();
         }
      }
   }

   private void handleBypassLogic() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               boolean isInventoryOpen = mc.currentScreen instanceof InventoryScreen;
               _s = 1;
               break;

            case 1:
               boolean isInvOpen = mc.currentScreen instanceof InventoryScreen;
               if (this.bypass.is("FunTime")) {
                  handleFunTimeBypass(isInvOpen);
               } else {
                  this.packetQueue.clear();
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private void handleFunTimeBypass(boolean isInventoryOpen) {
      FlowObfuscator.fakeHandler();
      if (isInventoryOpen) {
         this.wasInventoryOpen = true;
      } else if (this.wasInventoryOpen) {
         sendQueuedPacketsInternal();
         this.wasInventoryOpen = false;
         this.timer.reset();
      }
   }

   private void updateKeyBindingInternal(KeyBinding keyBinding) {
      FlowObfuscator.fakeHandler();
      long handle = mc.getWindow().getHandle();
      int code = keyBinding.getDefaultKey().getCode();
      if (FlowObfuscator.opaqueTrue()) {
         keyBinding.setPressed(InputUtil.isKeyPressed(handle, code));
      }
   }

   public void queuePacket(ClickSlotC2SPacket packet) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (this.bypass.is("FunTime")) {
                  this.packetQueue.add(packet);
               } else if (mc.getNetworkHandler() != null) {
                  mc.getNetworkHandler().sendPacket(packet);
               }
               _s = 4;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= packet.hashCode();
               }
               _s = 4;
               break;

            case 2:
               FlowObfuscator.fakeBranch(packet, entropy);
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

   private void sendQueuedPacketsInternal() {
      FlowObfuscator.fakeHandler();
      new Thread(() -> {
         try {
            Thread.sleep(NumberGuard.l(80));
            while (!this.packetQueue.isEmpty()) {
               ClickSlotC2SPacket packet = this.packetQueue.poll();
               if (LogicSplit.and(packet != null, mc.getNetworkHandler() != null)) {
                  mc.getNetworkHandler().sendPacket(packet);
               }
            }
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }).start();
   }
}
