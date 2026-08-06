package dev.just.util.move;

import dev.just.manager.IMinecraft;
import net.minecraft.network.packet.Packet;

public final class NetworkUtils implements IMinecraft {
   private static boolean sendingSilent = false;

   public static void sendSilentPacket(Packet<?> packet) {
      try {
         sendingSilent = true;
         mc.player.networkHandler.sendPacket(packet);
      } finally {
         sendingSilent = false;
      }
   }

   public static void sendPacket(Packet<?> packet) {
      mc.player.networkHandler.sendPacket(packet);
   }

   public static boolean isSendingSilent() {
      return sendingSilent;
   }

   private NetworkUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
