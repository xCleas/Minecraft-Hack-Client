package dev.just.manager;

import com.google.common.collect.ImmutableList;
import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.events.impl.render.EventPlayerRender;
import dev.just.mixin.iface.ClientPlayerEntityAccessor;
import dev.just.modules.combat.rotation.RotationController;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;

public class SyncManager implements IMinecraft {
   private volatile List<Entity> cachedEntities = Collections.emptyList();
   private volatile List<AbstractClientPlayerEntity> cachedPlayers = Collections.emptyList();
   private volatile List<ItemStack> cachedItems = Collections.emptyList();
   private float visualBodyYaw;
   private float visualHeadYaw;
   private float visualHeadPitch;
   private float visualPrevBodyYaw;
   private float visualPrevHeadYaw;
   private float visualPrevHeadPitch;
   public float tps = 20.0F;
   private long lastTime = System.nanoTime();
   private final float NANOS_IN_SECOND = 1.0E9F;

   public List<Entity> getEntities() {
      return this.cachedEntities;
   }

   public List<ItemStack> getItems() {
      return this.cachedItems;
   }

   public List<AbstractClientPlayerEntity> getPlayers() {
      return this.cachedPlayers;
   }

   public float getBodyYaw() {
      ClientPlayerEntity player = mc.player;
      if (player == null) {
         return this.visualBodyYaw;
      } else {
         double dx = player.getX() - player.prevX;
         double dz = player.getZ() - player.prevZ;
         float offset = this.visualBodyYaw;
         if (dx * dx + dz * dz > 0.0025F) {
            offset = (float)(MathHelper.atan2(dz, dx) * (180.0 / Math.PI) - 90.0);
         }

         ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)player;
         float lastYaw = accessor.getLastYaw();
         if (player.handSwingProgress > 0.0F) {
            offset = lastYaw;
         }

         float deltaBodyYaw = MathHelper.clamp(
            MathHelper.wrapDegrees(lastYaw - (this.visualBodyYaw + MathHelper.wrapDegrees(offset - this.visualBodyYaw) * 0.3F)), -45.0F, 75.0F
         );
         return (deltaBodyYaw > 50.0F ? deltaBodyYaw * 0.2F : 0.0F) + lastYaw - deltaBodyYaw;
      }
   }

   public void onEvent(Event event) {
      if (mc.world != null && mc.player != null) {
         Objects.requireNonNull(event);
         switch (event) {
            case EventUpdate ignored:
               Manager.ROTATION.onUpdate();
               this.cachedEntities = ImmutableList.copyOf(mc.world.getEntities());
               this.cachedPlayers = ImmutableList.copyOf(mc.world.getPlayers());
               this.cachedItems = ImmutableList.copyOf(mc.player.getAllArmorItems());
               break;
            case EventMotion em:
               Manager.ROTATION.onUpdate();
               RotationController rc = Manager.ROTATION;
               rc.updateIfFree(em.getYaw(), em.getPitch());
               if (rc.isControlling()) {
                  em.setYaw(rc.getYaw());
                  em.setPitch(rc.getPitch());
               }

               this.visualPrevHeadYaw = this.visualHeadYaw;
               this.visualPrevHeadPitch = this.visualHeadPitch;
               this.visualHeadYaw = em.getYaw();
               this.visualHeadPitch = em.getPitch();
               this.visualPrevBodyYaw = this.visualBodyYaw;
               this.visualBodyYaw = this.getBodyYaw();
               break;
            case EventPlayerRender epr:
               epr.setPrevYaw(this.visualPrevHeadYaw);
               epr.setPrevPitch(this.visualPrevHeadPitch);
               epr.setPrevBodyYaw(this.visualPrevBodyYaw);
               epr.setYaw(this.visualHeadYaw);
               epr.setPitch(this.visualHeadPitch);
               epr.setBodyYaw(this.visualBodyYaw);
               break;
            case EventPacket ep:
               if (ep.getPacket() instanceof WorldTimeUpdateS2CPacket) {
                  long now = System.nanoTime();
                  long delay = now - this.lastTime;
                  this.lastTime = now;
                  if (delay <= 0L) {
                     return;
                  }

                  float currentTPS = 20.0F * (1.0E9F / (float)delay);
                  currentTPS = MathHelper.clamp(currentTPS, 0.0F, 20.0F);
                  this.tps = this.tps + (currentTPS - this.tps) * 0.05F;
               }
               break;
            default:
               break;
         }
      }
   }
}
