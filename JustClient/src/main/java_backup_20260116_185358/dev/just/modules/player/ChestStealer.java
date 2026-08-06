package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.player.TimerUtil;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import java.util.List;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ChestStealer",
   desc = "U2FuZMSxa3Rha2kgZcWfeWFsYXLEsSBvdG9tYXRpayBvbGFyYWsgdG9wbGFy",
   type = Type.Player
)
public class ChestStealer extends Function {
   private final ModeSetting mode = new ModeSetting(Strings.b("TW9k"), Strings.b("Tm9ybWFs"), Strings.b("Tm9ybWFs"), Strings.b("QWvEsWxsxLE="));
   private final SliderSetting stealDelay = new SliderSetting(Strings.b("R2VjaWttZQ=="), 120.0, 0.0, 1000.0, 1.0);
   private final TimerUtil timer = new TimerUtil();
   private static final List<String> BLOCKED_TITLES = List.of(Strings.b("QcOnxLFrIEFydMSxcm1h"), "Warp", Strings.b("ScWfxLFubGFubWE="), Strings.b("TWVuw7w="), Strings.b("S2l0IFNlw6dpbWk="), Strings.b("S2FzYQ=="), Strings.b("TWHEn2F6YQ=="), "Market");

   // Fake constants
   private static final int FAKE_DELAY = 5000;
   private static final int FAKE_SIZE = 999;
   private volatile long entropy = System.nanoTime();

   public ChestStealer() {
      this.addSettings(new Setting[]{this.mode, this.stealDelay});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_DELAY;
                  fakeInstantMode();
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventUpdate)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (!(mc.currentScreen instanceof GenericContainerScreen container)) {
                  _s = 5;
                  break;
               }
               processContainer((GenericContainerScreen) mc.currentScreen);
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
               SemanticNoise.deadCode1();
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

   private void processContainer(GenericContainerScreen container) {
      FlowObfuscator.fakeHandler();

      if (isBlockedTitle(container)) return;

      GenericContainerScreenHandler handler = (GenericContainerScreenHandler)container.getScreenHandler();
      int chestSize = computeChestSize(handler);
      boolean instant = isInstantMode();

      stealItems(handler, chestSize, instant);
   }

   private boolean isBlockedTitle(GenericContainerScreen container) {
      FlowObfuscator.fakeHandler();

      String title = container.getTitle().getString().toLowerCase();

      if (FlowObfuscator.opaqueFalse()) {
         entropy ^= title.hashCode();
         return false;
      }

      for (String blocked : BLOCKED_TITLES) {
         if (title.contains(blocked.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   private int computeChestSize(GenericContainerScreenHandler handler) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(FAKE_SIZE);
      }

      return handler.getRows() * NumberGuard.i(9);
   }

   private boolean isInstantMode() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return this.stealDelay.get().floatValue() == NumberGuard.f(0.0F);
   }

   private void stealItems(GenericContainerScreenHandler handler, int chestSize, boolean instant) {
      FlowObfuscator.fakeHandler();

      for (int i = 0; i < chestSize; i++) {
         if (shouldStealItem(handler, i, instant)) {
            performSteal(handler, i, instant);
            if (!instant) break;
         }
      }

      SemanticNoise.deadCode1();
   }

   private boolean shouldStealItem(GenericContainerScreenHandler handler, int slot, boolean instant) {
      FlowObfuscator.fakeHandler();

      ItemStack stack = handler.getSlot(slot).getStack();

      if (stack.isEmpty()) return false;
      if (stack.getItem() == Items.AIR) return false;

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      if (this.mode.is(Strings.b("QWvEsWxsxLE="))) {
         if (!Manager.CHESTSTEALER_MANAGER.isAllowed(stack.getItem())) {
            return false;
         }
      }

      if (!instant && !this.timer.hasTimeElapsed(getStealDelay())) {
         return false;
      }

      return true;
   }

   private long getStealDelay() {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(FAKE_DELAY);
      }

      return this.stealDelay.get().longValue();
   }

   private void performSteal(GenericContainerScreenHandler handler, int slot, boolean instant) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         click(handler.syncId, slot);
      }

      if (!instant) {
         this.timer.reset();
      }
   }

   private void click(int id, int slot) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueTrue()) {
         mc.interactionManager.clickSlot(id, slot, NumberGuard.i(0), SlotActionType.QUICK_MOVE, mc.player);
      }
   }

   private void fakeInstantMode() {
      // Never runs
      entropy ^= System.nanoTime();
      SemanticNoise.deadCode2();
   }
}
