package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.render.EventRender3D;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.util.move.NetworkUtils;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.Box;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Blink",
   desc = "U3VudWN1eWEgZ8O2bmRlcmlsZW4gcGFrZXRsZXJpIGdlY2lrdGlyZXJlayDEscWfxLFubGFubWEgZXRraXNpIHlhcmF0xLFy",
   type = Type.Move
)
public class Blink extends Function {
   private final SliderSetting maxTicks = new SliderSetting(I0O1l0I1.b("TWFrcy4gVGlr"), 20.0, 1.0, 50.0, 1.0);
   private final CopyOnWriteArrayList<Packet<?>> packetBuffer = new CopyOnWriteArrayList<>();
   private Box playerBoundingBox;
   private int currentTick = 0;

   private static final int FAKE_BUFFER_SIZE = 1000;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Blink() {
      this.addSettings(new Setting[]{this.maxTicks});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakeInstantTeleportInternal();
                  _s = 6;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 7);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!validatePlayerInternal()) {
                  this.toggle();
                  _s = 6;
                  break;
               }
               _s = 2;
               break;

            case 2:
               processEventInternal(event);
               _s = 6;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entropy, _s);
               _s = 6;
               break;

            case 5:
               l1O0I1lO.fakeHandler();
               _s = 6;
               break;

            case 6:
               return;

            default:
               _s = 6;
               break;
         }
      }
   }

   private int validatePlayerFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(I1lO0l1I.all(mc.player != null, mc.world != null));
   }

   private boolean validatePlayerInternal() {
      return lO1I0l1O.unbool(validatePlayerFlag());
   }

   private void processEventInternal(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (event instanceof EventPacket packetEvent) {
                  handlePacketInternal(packetEvent);
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  handleUpdateInternal();
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventRender3D) {
                  handleRenderInternal();
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
               }
               _s = 5;
               break;

            case 4:
               SemanticNoise.deadCode1();
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void handlePacketInternal(EventPacket packetEvent) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      Packet<?> packet = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               packet = packetEvent.getPacket();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= packet.hashCode();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.and(packetEvent.isSendPacket(), I1lO0l1I.not(packet instanceof KeepAliveC2SPacket))) {
                  bufferPacketInternal(packet);
                  if (l1O0I1lO.opaqueTrue()) {
                     packetEvent.setCancel(true);
                  }
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(packet, entropy);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void bufferPacketInternal(Packet<?> packet) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         if (packetBuffer.size() > FAKE_BUFFER_SIZE) return;
      }
      if (l1O0I1lO.opaqueTrue()) {
         packetBuffer.add(packet);
      }
   }

   private void handleUpdateInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               currentTick++;
               _s = 1;
               break;

            case 1:
               int maxTickValue = getMaxTicksInternal();
               if (currentTick >= maxTickValue) {
                  flushPacketsInternal();
                  currentTick = lO1I0l1O.i(0);
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= currentTick;
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeBranch(currentTick, entropy);
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private int getMaxTicksInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(FAKE_BUFFER_SIZE);
      }
      return this.maxTicks.get().intValue();
   }

   private void handleRenderInternal() {
      l1O0I1lO.fakeHandler();
      if (playerBoundingBox != null) {
         if (l1O0I1lO.opaqueTrue()) {
            RenderUtil.render3D.drawHoleOutline(playerBoundingBox, Color.WHITE.getRGB(), lO1I0l1O.f(2.0F));
         }
      }
   }

   private void flushPacketsInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (I1lO0l1I.or(mc.player == null, mc.world == null)) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (packetBuffer.isEmpty()) {
                  _s = 4;
                  break;
               }
               if (l1O0I1lO.opaqueFalse()) {
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               for (Packet<?> packet : packetBuffer) {
                  NetworkUtils.sendSilentPacket(packet);
               }
               packetBuffer.clear();
               playerBoundingBox = mc.player.getBoundingBox();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(packetBuffer, entropy);
               }
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void fakeInstantTeleportInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= System.nanoTime();
      packetBuffer.clear();
      SemanticNoise.deadCode1();
   }

   @Override
   public void onDisable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               flushPacketsInternal();
               _s = 1;
               break;

            case 1:
               playerBoundingBox = null;
               currentTick = lO1I0l1O.i(0);
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }
}
