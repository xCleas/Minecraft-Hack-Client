package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKey;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.InventoryUtil;
import dev.just.util.player.TimerUtil;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.SemanticNoise;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoSwap",
   type = Type.Combat,
   desc = "RcWfeWFsYXIgYXJhc8SxbmRhIHRlayB0dcWfbGEgaMSxemzEsSBnZcOnacWfIHlhcG1hbsSxesSxIHNhxJ9sYXI="
)
public class AutoSwap extends Function {
   private final BindSetting itemSwapKey = new BindSetting(I0O1l0I1.b("RGXEn2nFn2ltIFR1xZ91"), 0);
   private final ModeSetting firstItem = new ModeSetting(I0O1l0I1.b("QmlyaW5jaSBFxZ95YQ=="), I0O1l0I1.b("S2Fsa2Fu"), I0O1l0I1.b("S2Fsa2Fu"), I0O1l0I1.b("RWxtYQ=="), "Totem", I0O1l0I1.b("S8O8cmU="), I0O1l0I1.b("SGF2YWkgRmnFn2Vr"));
   private final ModeSetting secondItem = new ModeSetting(I0O1l0I1.b("xLBraW5jaSBFxZ95YQ=="), I0O1l0I1.b("S2Fsa2Fu"), I0O1l0I1.b("S2Fsa2Fu"), I0O1l0I1.b("RWxtYQ=="), "Totem", I0O1l0I1.b("S8O8cmU="), I0O1l0I1.b("SGF2YWkgRmnFn2Vr"));
   private final BooleanSetting swapSwordWithAxe = new BooleanSetting(I0O1l0I1.b("S8SxbMSxw6cgdmUgQmFsdGEgRGXEn2nFn3Rpcg=="), false);
   private final BooleanSetting funTimeAndHolyWorldBypass = new BooleanSetting(I0O1l0I1.b("U3VudWN1IEJ5cGFzcyAoRlQvSFcp"), false);
   private final TimerUtil timer = new TimerUtil();
   private boolean bypassActive = false;
   private boolean awaitingSwap = false;
   private int pendingSlot = -1;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public AutoSwap() {
      this.addSettings(new Setting[]{this.itemSwapKey, this.firstItem, this.secondItem, this.swapSwordWithAxe, this.funTimeAndHolyWorldBypass});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventKey eventKey && eventKey.key == this.itemSwapKey.getKey()) {
                  handleKeyEventInternal();
               }
               _s = 2;
               break;

            case 2:
               if (this.bypassActive) {
                  handleBypassInternal();
               }
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

   private void handleKeyEventInternal() {
      l1O0I1lO.fakeHandler();
      Item itemA = this.getItem(this.firstItem.getIndex());
      Item itemB = this.getItem(this.secondItem.getIndex());
      if (I1lO0l1I.or(itemA == null, itemB == null)) {
         return;
      }

      int inventorySlot = this.findItemInInventory(mc.player.getOffHandStack().getItem() == itemA ? itemB : itemA);
      if (inventorySlot == lO1I0l1O.i(-1)) {
         return;
      }

      if (this.funTimeAndHolyWorldBypass.get()) {
         this.timer.reset();
         this.bypassActive = true;
         this.awaitingSwap = true;
         this.pendingSlot = inventorySlot;
      } else {
         if (l1O0I1lO.opaqueTrue()) {
            mc.interactionManager.clickSlot(lO1I0l1O.i(0), inventorySlot < lO1I0l1O.i(9) ? inventorySlot + lO1I0l1O.i(36) : inventorySlot, lO1I0l1O.i(40), SlotActionType.SWAP, mc.player);
         }
         if (this.swapSwordWithAxe.get()) {
            this.handleWeaponSwap();
         }
      }
   }

   private void handleBypassInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               mc.options.forwardKey.setPressed(false);
               mc.options.backKey.setPressed(false);
               mc.options.leftKey.setPressed(false);
               mc.options.rightKey.setPressed(false);
               mc.options.sprintKey.setPressed(false);
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.and(this.awaitingSwap, this.timer.hasTimeElapsed(lO1I0l1O.l(90)))) {
                  this.awaitingSwap = false;
                  if (this.pendingSlot != lO1I0l1O.i(-1)) {
                     if (l1O0I1lO.opaqueTrue()) {
                        mc.interactionManager.clickSlot(lO1I0l1O.i(0), this.pendingSlot < lO1I0l1O.i(9) ? this.pendingSlot + lO1I0l1O.i(36) : this.pendingSlot, lO1I0l1O.i(40), SlotActionType.SWAP, mc.player);
                     }
                     if (this.swapSwordWithAxe.get()) {
                        this.handleWeaponSwap();
                     }
                     this.pendingSlot = lO1I0l1O.i(-1);
                  }
               }
               _s = 2;
               break;

            case 2:
               if (this.timer.hasTimeElapsed(lO1I0l1O.l(150))) {
                  this.bypassActive = false;
                  this.awaitingSwap = false;
                  this.pendingSlot = lO1I0l1O.i(-1);
                  this.updateKeyBinding(mc.options.forwardKey);
                  this.updateKeyBinding(mc.options.backKey);
                  this.updateKeyBinding(mc.options.leftKey);
                  this.updateKeyBinding(mc.options.rightKey);
                  this.updateKeyBinding(mc.options.sprintKey);
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(bypassActive, entropy);
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

   private int findItemInInventory(Item item) {
      for (int i = 0; i < mc.player.getInventory().size(); i++) {
         ItemStack itemStack = mc.player.getInventory().getStack(i);
         if (!itemStack.isEmpty() && itemStack.getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   private Item getItem(int index) {
      if (index == 0) {
         return Items.SHIELD;
      } else if (index == 1) {
         return Items.GOLDEN_APPLE;
      } else if (index == 2) {
         return Items.TOTEM_OF_UNDYING;
      } else if (index == 3) {
         return Items.PLAYER_HEAD;
      } else {
         return index == 4 ? Items.FIREWORK_ROCKET : null;
      }
   }

   private void handleWeaponSwap() {
      int swordSlot = InventoryUtil.getItem(SwordItem.class, true);
      if (swordSlot == -1) {
         swordSlot = InventoryUtil.getItem(SwordItem.class, false);
      }

      int axeSlot = InventoryUtil.getItem(AxeItem.class, true);
      if (axeSlot == -1) {
         axeSlot = InventoryUtil.getItem(AxeItem.class, false);
      }

      if (swordSlot != -1 && axeSlot != -1) {
         InventoryUtil.swapSlots(swordSlot, axeSlot);
      }
   }

   private void updateKeyBinding(KeyBinding keyMapping) {
      keyMapping.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), keyMapping.getDefaultKey().getCode()));
   }
}
