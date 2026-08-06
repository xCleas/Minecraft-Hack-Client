package dev.just.events.impl;

import dev.just.events.Event;
import net.minecraft.network.packet.Packet;

public class EventPacket extends Event {
   private Packet packet;
   private final EventPacket.PacketType packetType;

   public EventPacket(Packet packet, EventPacket.PacketType packetType) {
      this.packet = packet;
      this.packetType = packetType;
   }

   public Packet getPacket() {
      return this.packet;
   }

   public void setPacket(Packet packet) {
      this.packet = packet;
   }

   public boolean isReceivePacket() {
      return this.packetType == EventPacket.PacketType.RECEIVE;
   }

   public boolean isSendPacket() {
      return this.packetType == EventPacket.PacketType.SEND;
   }

   public static enum PacketType {
      SEND,
      RECEIVE;
   }
}
