package dev.just.events.impl.render;

import dev.just.events.Event;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.RenderTickCounter;

public class EventRender3D extends Event {
   private RenderTickCounter deltatick;
   private MatrixStack matrixStack;

   public EventRender3D(MatrixStack matrixStack, RenderTickCounter deltatick) {
      this.matrixStack = matrixStack;
      this.deltatick = deltatick;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public RenderTickCounter getDeltatick() {
      return this.deltatick;
   }
}
