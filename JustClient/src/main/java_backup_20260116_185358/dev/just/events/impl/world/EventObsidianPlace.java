package dev.just.events.impl.world;

import dev.just.events.Event;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class EventObsidianPlace extends Event {
   private final Block block;
   private final BlockPos pos;

   public Block getBlock() {
      return this.block;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   @Override
   public String toString() {
      return "EventObsidianPlace(block=" + this.getBlock() + ", pos=" + this.getPos() + ")";
   }

   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EventObsidianPlace other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         Object this$block = this.getBlock();
         Object other$block = other.getBlock();
         if (this$block == null ? other$block == null : this$block.equals(other$block)) {
            Object this$pos = this.getPos();
            Object other$pos = other.getPos();
            return this$pos == null ? other$pos == null : this$pos.equals(other$pos);
         } else {
            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof EventObsidianPlace;
   }

   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      Object $block = this.getBlock();
      result = result * 59 + ($block == null ? 43 : $block.hashCode());
      Object $pos = this.getPos();
      return result * 59 + ($pos == null ? 43 : $pos.hashCode());
   }

   public EventObsidianPlace(Block block, BlockPos pos) {
      this.block = block;
      this.pos = pos;
   }
}
