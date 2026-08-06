package dev.just.events.impl.render;

import dev.just.events.Event;
import net.minecraft.util.Hand;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.math.MatrixStack;

public class EventHeldItemRenderer extends Event {
   private final Hand hand;
   private final ItemStack item;
   private final float ep;
   private final MatrixStack stack;

   public EventHeldItemRenderer(Hand hand, ItemStack item, float equipProgress, MatrixStack stack) {
      this.hand = hand;
      this.item = item;
      this.ep = equipProgress;
      this.stack = stack;
   }

   public Hand getHand() {
      return this.hand;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public float getEp() {
      return this.ep;
   }

   public MatrixStack getStack() {
      return this.stack;
   }
}
