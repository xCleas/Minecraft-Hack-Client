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
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import dev.just.util.move.NetworkUtils;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.Box;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Blink",
   desc = "U3VudWN1eWEgZ8O2bmRlcmlsZW4gcGFrZXRsZXJpIGdlY2lrdGlyZXJlayDEscWfxLFubGFubWEgZXRraXNpIHlhcmF0xLFy",
   type = Type.Move
)
public class Blink extends Function {
   private final SliderSetting maxTicks = new SliderSetting(Strings.b("TWFrcy4gVGlr"), 20.0, 1.0, 50.0, 1.0);
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
      int _s = ControlFlow.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  fakeInstantTeleportInternal();
                  _s = 6;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 7);
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
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entropy, _s);
               _s = 6;
               break;

            case 5:
               FlowObfuscator.fakeHandler();
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
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > NumberGuard.l(0));
      }
      return NumberGuard.bool(LogicSplit.all(mc.player != null, mc.world != null));
   }

   private boolean validatePlayerInternal() {
      return NumberGuard.unbool(validatePlayerFlag());
   }

   private void processEventInternal(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 6);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
               if (FlowObfuscator.opaqueFalse()) {
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
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);
      Packet<?> packet = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               packet = packetEvent.getPacket();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= packet.hashCode();
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (LogicSplit.and(packetEvent.isSendPacket(), LogicSplit.not(packet instanceof KeepAliveC2SPacket))) {
                  bufferPacketInternal(packet);
                  if (FlowObfuscator.opaqueTrue()) {
                     packetEvent.setCancel(true);
                  }
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(packet, entropy);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
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
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         if (packetBuffer.size() > FAKE_BUFFER_SIZE) return;
      }
      if (FlowObfuscator.opaqueTrue()) {
         packetBuffer.add(packet);
      }
   }

   private void handleUpdateInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               currentTick++;
               _s = 1;
               break;

            case 1:
               int maxTickValue = getMaxTicksInternal();
               if (currentTick >= maxTickValue) {
                  flushPacketsInternal();
                  currentTick = NumberGuard.i(0);
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= currentTick;
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeBranch(currentTick, entropy);
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
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(FAKE_BUFFER_SIZE);
      }
      return this.maxTicks.get().intValue();
   }

   private void handleRenderInternal() {
      FlowObfuscator.fakeHandler();
      if (playerBoundingBox != null) {
         if (FlowObfuscator.opaqueTrue()) {
            RenderUtil.render3D.drawHoleOutline(playerBoundingBox, Color.WHITE.getRGB(), NumberGuard.f(2.0F));
         }
      }
   }

   private void flushPacketsInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (LogicSplit.or(mc.player == null, mc.world == null)) {
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
               if (FlowObfuscator.opaqueFalse()) {
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(packetBuffer, entropy);
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
      FlowObfuscator.fakeHandler();
      entropy ^= System.nanoTime();
      packetBuffer.clear();
      SemanticNoise.deadCode1();
   }

   @Override
   public void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               flushPacketsInternal();
               _s = 1;
               break;

            case 1:
               playerBoundingBox = null;
               currentTick = NumberGuard.i(0);
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
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
