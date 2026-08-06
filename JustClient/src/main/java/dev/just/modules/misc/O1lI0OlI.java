package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.TextSetting;
import dev.just.util.player.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PlayerHeadItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "TPLoot",
   desc = "WWVyZGVraSBkZcSfZXJsaSBlxZ95YWxhcmEgcGFrZXRsZSDEscWfxLFubGFuYXJhayB0b3BsYXI=",
   type = Type.Misc
)
public class O1lI0OlI extends Function {
   private final ModeSetting lootEnd = new ModeSetting(I0O1l0I1.b("VG9wbGFkxLFrdGFuIFNvbnJh"), I0O1l0I1.b("SGnDp2Jpcmk="), I0O1l0I1.b("SGnDp2Jpcmk="), "/hub", "/spawn", "home", I0O1l0I1.b("w5Z6ZWw="));
   private final TextSetting text = new TextSetting(I0O1l0I1.b("S29tdXQ="), "/home home", () -> this.lootEnd.is("home"));
   private final TextSetting custom = new TextSetting(I0O1l0I1.b("S29tdXQ="), "/test", () -> this.lootEnd.is(I0O1l0I1.b("w5Z6ZWw=")));
   private final TimerUtil timerUtil = new TimerUtil();
   private boolean check;

   public O1lI0OlI() {
      this.addSettings(new Setting[]{this.lootEnd, this.text, this.custom});
   }

   private boolean isValuable(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         Item item = stack.getItem();
         return item instanceof SwordItem
            || item instanceof PlayerHeadItem
            || item instanceof ArmorItem
            || item == Items.TOTEM_OF_UNDYING
            || item == Items.ENDER_PEARL
            || item == Items.END_CRYSTAL
            || item == Items.FIREWORK_ROCKET
            || item == Items.ELYTRA
            || item == Items.GOLDEN_APPLE
            || item == Items.ENCHANTED_GOLDEN_APPLE
            || item == Items.CLAY_BALL
            || item == Items.MAGMA_CREAM
            || item == Items.CRYING_OBSIDIAN;
      } else {
         return false;
      }
   }

   private boolean isHotbar() {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (this.isValuable(stack)) {
            return true;
         }
      }

      return false;
   }

   private boolean isInv() {
      for (int i = 9; i < 36; i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (this.isValuable(stack)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
            if (entity instanceof ItemEntity) {
               ItemEntity itemEntity = (ItemEntity)entity;
               ItemStack stack = itemEntity.getStack();
               if (this.isValuable(stack)) {
                  double itemY = itemEntity.getY();
                  BlockPos blockBelow = itemEntity.getBlockPos().down();
                  if (!mc.world.getBlockState(blockBelow).isAir() && itemY - (double)blockBelow.getY() <= 1.0) {
                     double x = itemEntity.getX() + 0.5;
                     double y = itemEntity.getY();
                     double z = itemEntity.getZ() + 0.5;
                     mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, mc.player.getYaw(), mc.player.getPitch(), false, true));
                     this.check = true;
                  }
               }
            }
         }

         if (this.check && this.timerUtil.hasTimeElapsed(100L) && (this.isHotbar() || this.isInv())) {
            String var15 = this.lootEnd.get();
            switch (var15) {
               case "/hub":
                  mc.player.networkHandler.sendChatMessage("/hub");
                  break;
               case "/spawn":
                  mc.player.networkHandler.sendChatMessage("/spawn");
                  break;
               case "home":
                  mc.player.networkHandler.sendChatMessage(this.text.getValue());
                  break;
               default:
                  if (var15.equals(I0O1l0I1.b("w5Z6ZWw="))) mc.player.networkHandler.sendChatMessage(this.custom.getValue());
            }

            this.check = false;
            this.timerUtil.reset();
         }
      }
   }
}
