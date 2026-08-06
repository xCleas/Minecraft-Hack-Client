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
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import java.util.List;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ChestStealer",
   desc = "U2FuZMSxa3Rha2kgZcWfeWFsYXLEsSBvdG9tYXRpayBvbGFyYWsgdG9wbGFy",
   type = Type.Player
)
public class ChestStealer extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("TW9k"), I0O1l0I1.b("Tm9ybWFs"), I0O1l0I1.b("Tm9ybWFs"), I0O1l0I1.b("QWvEsWxsxLE="));
   private final SliderSetting stealDelay = new SliderSetting(I0O1l0I1.b("R2VjaWttZQ=="), 120.0, 0.0, 1000.0, 1.0);
   private final TimerUtil timer = new TimerUtil();
   private static final List<String> BLOCKED_TITLES = List.of(I0O1l0I1.b("QcOnxLFrIEFydMSxcm1h"), "Warp", I0O1l0I1.b("ScWfxLFubGFubWE="), I0O1l0I1.b("TWVuw7w="), I0O1l0I1.b("S2l0IFNlw6dpbWk="), I0O1l0I1.b("S2FzYQ=="), I0O1l0I1.b("TWHEn2F6YQ=="), "Market");

   // Fake constants
   private static final int FAKE_DELAY = 5000;
   private static final int FAKE_SIZE = 999;
   private volatile long entropy = System.nanoTime();

   public ChestStealer() {
      this.addSettings(new Setting[]{this.mode, this.stealDelay});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_DELAY;
                  fakeInstantMode();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
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
      l1O0I1lO.fakeHandler();

      if (isBlockedTitle(container)) return;

      GenericContainerScreenHandler handler = (GenericContainerScreenHandler)container.getScreenHandler();
      int chestSize = computeChestSize(handler);
      boolean instant = isInstantMode();

      stealItems(handler, chestSize, instant);
   }

   private boolean isBlockedTitle(GenericContainerScreen container) {
      l1O0I1lO.fakeHandler();

      String title = container.getTitle().getString().toLowerCase();

      if (l1O0I1lO.opaqueFalse()) {
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
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(FAKE_SIZE);
      }

      return handler.getRows() * lO1I0l1O.i(9);
   }

   private boolean isInstantMode() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return this.stealDelay.get().floatValue() == lO1I0l1O.f(0.0F);
   }

   private void stealItems(GenericContainerScreenHandler handler, int chestSize, boolean instant) {
      l1O0I1lO.fakeHandler();

      for (int i = 0; i < chestSize; i++) {
         if (shouldStealItem(handler, i, instant)) {
            performSteal(handler, i, instant);
            if (!instant) break;
         }
      }

      SemanticNoise.deadCode1();
   }

   private boolean shouldStealItem(GenericContainerScreenHandler handler, int slot, boolean instant) {
      l1O0I1lO.fakeHandler();

      ItemStack stack = handler.getSlot(slot).getStack();

      if (stack.isEmpty()) return false;
      if (stack.getItem() == Items.AIR) return false;

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      if (this.mode.is(I0O1l0I1.b("QWvEsWxsxLE="))) {
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
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(FAKE_DELAY);
      }

      return this.stealDelay.get().longValue();
   }

   private void performSteal(GenericContainerScreenHandler handler, int slot, boolean instant) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         click(handler.syncId, slot);
      }

      if (!instant) {
         this.timer.reset();
      }
   }

   private void click(int id, int slot) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         mc.interactionManager.clickSlot(id, slot, lO1I0l1O.i(0), SlotActionType.QUICK_MOVE, mc.player);
      }
   }

   private void fakeInstantMode() {
      // Never runs
      entropy ^= System.nanoTime();
      SemanticNoise.deadCode2();
   }
}
