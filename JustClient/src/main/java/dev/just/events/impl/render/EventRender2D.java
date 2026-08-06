package dev.just.events.impl.render;

import dev.just.events.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.RenderTickCounter;

public class EventRender2D extends Event {
   private DrawContext drawContext;
   private MatrixStack matrixStack;
   private RenderTickCounter deltatick;

   public EventRender2D(DrawContext drawContext, MatrixStack matrixStack, RenderTickCounter deltatick) {
      this.drawContext = drawContext;
      this.matrixStack = matrixStack;
      this.deltatick = deltatick;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public DrawContext getDrawContext() {
      return this.drawContext;
   }

   public RenderTickCounter getDeltatick() {
      return this.deltatick;
   }
}
