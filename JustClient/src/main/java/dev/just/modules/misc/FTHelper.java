package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.input.EventKey;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.InventoryUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "FTHelper",
   desc = "RnVuVGltZSDDtnplbCBlxZ95YWxhcsSxeWxhIGjEsXpsxLEgZXRraWxlxZ9pbSBzYcSfbGFy",
   type = Type.Misc
)
public class FTHelper extends Function {
   private final BindSetting trapka = new BindSetting(I0O1l0I1.b("VHJhcGthIFR1xZ91"), 0);
   private final BindSetting disorientation = new BindSetting(I0O1l0I1.b("RGV6b3J5YW50YXN5b24gVHXFn3U="), 0);
   private final BindSetting plast = new BindSetting(I0O1l0I1.b("UGxhc3QgVHXFn3U="), 0);
   private final BindSetting godaura = new BindSetting(I0O1l0I1.b("VGFucsSxIEF1cmFzxLEgVHXFn3U="), 0);
   private final BooleanSetting inventoryUse = new BooleanSetting(I0O1l0I1.b("RW52YW50ZXJkZW4gS3VsbGFu"), true);
   private final Map<BindSetting, Item> binds = new LinkedHashMap<>();

   public FTHelper() {
      this.addSettings(new Setting[]{this.trapka, this.disorientation, this.plast, this.godaura, this.inventoryUse});
      this.binds.put(this.trapka, Items.NETHERITE_SCRAP);
      this.binds.put(this.disorientation, Items.ENDER_EYE);
      this.binds.put(this.plast, Items.DRIED_KELP);
      this.binds.put(this.godaura, Items.PHANTOM_MEMBRANE);
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventKey eventKey && mc.player != null) {
         int pressedKey = eventKey.key;

         for (Entry<BindSetting, Item> entry : this.binds.entrySet()) {
            if (pressedKey == entry.getKey().getKey()) {
               this.useItem(entry.getValue());
               return;
            }
         }

         return;
      }
   }

   private void useItem(Item item) {
      int[] slots = this.findSlots(item);
      InventoryUtil.use(slots[0], slots[1], this.inventoryUse.get());
   }

   private int[] findSlots(Item item) {
      if (mc.player == null) {
         return new int[]{-1, -1};
      } else {
         PlayerInventory inv = mc.player.getInventory();
         int size = inv.size();
         int hotbarSlot = -1;
         int inventorySlot = -1;

         for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
               if (i < 9) {
                  if (hotbarSlot == -1) {
                     hotbarSlot = i;
                  }

                  if (inventorySlot == -1) {
                     inventorySlot = i + 36;
                  }
               } else if (inventorySlot == -1) {
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
}
