package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.input.EventKey;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.InventoryUtil;
import dev.just.util.player.TimerUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "HWHelper",
   desc = "SG9sbHlXb3JsZCDDtnplbCBlxZ95YWxhcsSxeWxhIGjEsXpsxLEgZXRraWxlxZ9pbSBzYcSfbGFy",
   type = Type.Misc
)
public class HWHelper extends Function {
   private final BindSetting trapka = new BindSetting(I0O1l0I1.b("VHJhcGthIFR1xZ91"), 0);
   private final BindSetting trapkaBax = new BindSetting(I0O1l0I1.b("UGF0bGF5xLFjxLEgVHJhcGthIFR1xZ91"), 0);
   private final BindSetting stan = new BindSetting(I0O1l0I1.b("U3RhbiBUdcWfdQ=="), 0);
   private final BindSetting snow = new BindSetting(I0O1l0I1.b("S2FyIFRvcHUgVHXFn3U="), 0);
   private final BindSetting babax = new BindSetting(I0O1l0I1.b("UGF0bGF5xLFjxLEgxZ5leSBUdcWfdQ=="), 0);
   private final BooleanSetting bypass = new BooleanSetting("Bypass", true, I0O1l0I1.b("RcWfeWEgZGXEn2nFn2ltaSBzxLFyYXPEsW5kYSBzaXppIHlhdmHFn2xhdGFyYWsgYW50aWNoZWF0J2kgYXRsYXTEsXI="));
   private final BooleanSetting inventoryUse = new BooleanSetting(I0O1l0I1.b("RW52YW50ZXJkZW4gS3VsbGFu"), true);
   private final TimerUtil timer = new TimerUtil();
   private boolean bypassActive = false;
   private boolean awaitingSwap = false;
   private int hotbarSlot = -1;
   private int invSlot = -1;
   private final Map<BindSetting, Item> binds = new LinkedHashMap<>();

   public HWHelper() {
      this.addSettings(new Setting[]{this.trapka, this.trapkaBax, this.stan, this.snow, this.babax, this.bypass, this.inventoryUse});
      this.binds.put(this.trapka, Items.POPPED_CHORUS_FRUIT);
      this.binds.put(this.trapkaBax, Items.PRISMARINE_SHARD);
      this.binds.put(this.stan, Items.NETHER_STAR);
      this.binds.put(this.snow, Items.SNOWBALL);
      this.binds.put(this.babax, Items.FIRE_CHARGE);
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventKey eventKey) {
         this.handleKey(eventKey.key);
      }

      if (event instanceof EventUpdate) {
         this.handleBypass();
      }
   }

   private void handleKey(int pressedKey) {
      for (Entry<BindSetting, Item> entry : this.binds.entrySet()) {
         if (pressedKey == entry.getKey().getKey()) {
            int[] slots = this.findSlots(entry.getValue());
            if (this.bypass.get()) {
               this.timer.reset();
               this.bypassActive = true;
               this.awaitingSwap = true;
               this.hotbarSlot = slots[0];
               this.invSlot = slots[1];
            } else {
               InventoryUtil.use(slots[0], slots[1], this.inventoryUse.get());
            }

            return;
         }
      }
   }

   private void handleBypass() {
      if (this.bypassActive) {
         this.setMovementKeys(false);
         if (this.awaitingSwap && this.timer.hasTimeElapsed(90L)) {
            this.awaitingSwap = false;
            if (this.hotbarSlot != -1 || this.invSlot != -1) {
               InventoryUtil.use(this.hotbarSlot, this.invSlot, this.inventoryUse.get());
            }
         }

         if (this.timer.hasTimeElapsed(150L)) {
            this.bypassActive = false;
            this.awaitingSwap = false;
            this.setMovementKeys(true);
         }
      }
   }

   private int[] findSlots(Item item) {
      if (mc.player == null) {
         return new int[]{-1, -1};
      } else {
         int hotbarSlot = -1;
         int inventorySlot = -1;

         for (int i = 0; i < mc.player.getInventory().size(); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
               if (i < 9) {
                  hotbarSlot = i;
               } else {
                  inventorySlot = i;
               }

               if (hotbarSlot != -1 && inventorySlot != -1) {
                  break;
               }
            }
         }

         return new int[]{hotbarSlot, inventorySlot};
      }
   }

   private void setMovementKeys(boolean restore) {
      KeyBinding[] keys = new KeyBinding[]{
         mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.sprintKey
      };

      for (KeyBinding key : keys) {
         if (restore) {
            this.updateKeyBinding(key);
         } else {
            key.setPressed(false);
         }
      }
   }

   private void updateKeyBinding(KeyBinding keyMapping) {
      keyMapping.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), keyMapping.getDefaultKey().getCode()));
   }
}
