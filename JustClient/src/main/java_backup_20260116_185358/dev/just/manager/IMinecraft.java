package dev.just.manager;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

public interface IMinecraft {
   MinecraftClient mc = MinecraftClient.getInstance();

   static RenderTickCounter tickCounter() {
      return IMinecraft.Holder.tickCounter;
   }

   static Tessellator tessellator() {
      return IMinecraft.Holder.tessellator;
   }

   static MinecraftClient getMc() {
      return IMinecraft.Holder.minecraftClient;
   }

   public static class Holder {
      private static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
      private static final RenderTickCounter tickCounter = IMinecraft.mc.getRenderTickCounter();
      private static final Tessellator tessellator = Tessellator.getInstance();
   }
}
