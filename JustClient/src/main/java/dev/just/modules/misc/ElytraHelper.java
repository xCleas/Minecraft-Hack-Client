package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.input.EventKey;
import dev.just.manager.ClientManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.InventoryUtil;
import net.minecraft.util.Formatting;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ElytraHelper",
   desc = "RWxpdHJhIGlsZSBoxLF6bMSxIGV0a2lsZcWfaW0gc2HEn2xhcg==",
   type = Type.Misc
)
public class ElytraHelper extends Function {
   private final BindSetting elytraKey = new BindSetting(I0O1l0I1.b("RWxpdHJhIFR1xZ91"), 0);
   private final BindSetting fireworkKey = new BindSetting(I0O1l0I1.b("SGF2YWkgRmnFn2VrIFR1xZ91"), 0);
   private final BooleanSetting autoTakeoff = new BooleanSetting(I0O1l0I1.b("T3RvbWF0aWsgS2Fsa8SxxZ8="), true);
   private int takeoffTicks = 0;
   private boolean waitingToGlide = false;

   public ElytraHelper() {
      this.addSettings(new Setting[]{this.elytraKey, this.fireworkKey, this.autoTakeoff});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate && mc.player != null && this.autoTakeoff.get()) {
         this.handleAutoTakeoff();
      }

      if (event instanceof EventKey e && mc.player != null) {
         ItemStack equipped = mc.player.getEquippedStack(EquipmentSlot.CHEST);
         if (e.key == this.elytraKey.getKey()) {
            int chestPlateSlot = this.findChestplate();
            int elytraSlot = this.findItemSlot(Items.ELYTRA);
            if (equipped.getItem() == Items.ELYTRA) {
               if (chestPlateSlot != -1) {
                  this.swapSlots(chestPlateSlot, 6);
                  ClientManager.message(I0O1l0I1.b("xZ51bmEgZ2XDp2lsZGk6IA==") + Formatting.AQUA + I0O1l0I1.b("WsSxcmg="));
               } else {
                  this.swapSlots(elytraSlot == -1 ? 6 : elytraSlot, 6);
                  ClientManager.message(Formatting.YELLOW + I0O1l0I1.b("RWxpdHJhIMOnxLFrYXLEsWxkxLEgKFrEsXJoIGJ1bHVuYW1hZMSxKQ=="));
               }
            } else if (elytraSlot != -1) {
               this.swapSlots(elytraSlot, 6);
               ClientManager.message(I0O1l0I1.b("xZ51bmEgZ2XDp2lsZGk6IA==") + Formatting.RED + "Elitra");
            } else {
               ClientManager.message(Formatting.RED + I0O1l0I1.b("RWxpdHJhIGJ1bHVuYW1hZMSxIQ=="));
            }
         }

         if (e.key == this.fireworkKey.getKey() && equipped.getItem() == Items.ELYTRA) {
            InventoryUtil.inventorySwapClick2(Items.FIREWORK_ROCKET, true, false);
         }
      }
   }

   private void handleAutoTakeoff() {
      ItemStack chest = mc.player.getEquippedStack(EquipmentSlot.CHEST);
      if (chest.getItem() != Items.ELYTRA) {
         this.waitingToGlide = false;
         this.takeoffTicks = 0;
      } else if (mc.player.isGliding()) {
         this.waitingToGlide = false;
         this.takeoffTicks = 0;
      } else if (mc.player.isOnGround() && !this.waitingToGlide) {
         mc.player.jump();
         this.waitingToGlide = true;
         this.takeoffTicks = 0;
      } else {
         if (this.waitingToGlide) {
            this.takeoffTicks++;
            if (this.takeoffTicks >= 2 && mc.player.getVelocity().y < -0.08 && !mc.player.isGliding()) {
               InventoryUtil.startFly();
               this.waitingToGlide = false;
               this.takeoffTicks = 0;
            }

            if (this.takeoffTicks > 10) {
               this.waitingToGlide = false;
               this.takeoffTicks = 0;
            }
         }
      }
   }

   private int findItemSlot(Item item) {
      for (int i = 0; i < mc.player.getInventory().size(); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   private int findChestplate() {
      Item[] chestplates = new Item[]{
         Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.LEATHER_CHESTPLATE
      };

      for (Item item : chestplates) {
         int slot = this.findItemSlot(item);
         if (slot != -1) {
            return slot;
         }
      }

      return -1;
   }

   private void swapSlots(int from, int armorSlot) {
      int slot = from < 9 ? from + 36 : from;
      mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.SWAP, mc.player);
      mc.interactionManager.clickSlot(0, armorSlot, 0, SlotActionType.SWAP, mc.player);
      mc.interactionManager.clickSlot(0, slot, 0, SlotActionType.SWAP, mc.player);
   }

   @Override
   protected void onEnable() {
      ClientManager.message(I0O1l0I1.b("RGXEn2nFn2ltbGVyaW4gZMO8emfDvG4gw6dhbMSxxZ9tYXPEsSBpw6dpbiBsw7x0ZmVuIFZpYUZhYnJpY1BsdXMgw7x6ZXJpbmRlbiBzw7xyw7xtw7wgMS4xNyBvbGFyYWsgYXlhcmxhecSxbi4="));
      super.onEnable();
   }
}
