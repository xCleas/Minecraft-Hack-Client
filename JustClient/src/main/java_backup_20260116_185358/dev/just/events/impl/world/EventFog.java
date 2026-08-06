package dev.just.events.impl.world;

import dev.just.events.Event;
import net.minecraft.client.render.FogShape;

public class EventFog extends Event {
   public boolean modified = false;
   public float r;
   public float g;
   public float b;
   public float alpha;
   public float start;
   public float end;
   public FogShape shape;
}
