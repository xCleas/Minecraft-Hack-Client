package dev.just.events.impl.input;

import dev.just.events.Event;

public class EventKey extends Event {
   public int key;

   public EventKey(int key) {
      this.key = key;
   }
}
