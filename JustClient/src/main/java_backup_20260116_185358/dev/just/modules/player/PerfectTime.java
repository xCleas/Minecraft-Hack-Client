package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "PerfectTime",
   desc = "TcSxenJhayB2ZXlhIGFyYmFsZXQgdGFtIGdlcmlsZGnEn2luZGUgb3RvbWF0aWsgb2xhcmFrIGbEsXJsYXTEsXI=",
   type = Type.Player
)
public class PerfectTime extends Function {
   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate && mc.player != null && mc.player.isUsingItem()) {
         ItemStack stack = mc.player.getMainHandStack();
         Item item = stack.getItem();
         int useTime = stack.getMaxUseTime(mc.player) - mc.player.getItemUseTimeLeft();
         if (item instanceof TridentItem && useTime >= 10) {
            this.releaseUse();
         } else if (item instanceof CrossbowItem && useTime >= stack.getMaxUseTime(mc.player) - 1) {
            this.releaseUse();
         }
      }
   }

   private void releaseUse() {
      mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
      mc.player.stopUsingItem();
   }
}
