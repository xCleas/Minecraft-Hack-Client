package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.util.move.NetworkUtils;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.network.packet.Packet;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

@FunctionAnnotation(
   name = "Phase",
   desc = "ReallyWorld sunucusunda bloklarin icinden gecmenizi saglar",
   type = Type.Move
)
public class Phase extends Function {
   private final List<Packet<?>> bufferedPackets = new CopyOnWriteArrayList<>();
   private boolean semiPacketSent;
   private boolean skipReleaseOnDisable;

   private static final double FAKE_OFFSET = 10000.0;
   private static final int FAKE_ITERATIONS = 100;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakePhaseModeInternal();
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
               dispatchEventInternal(event);
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

   private void dispatchEventInternal(Event event) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (event instanceof EventPacket ep) {
                  handlePacketEventInternal(ep);
               }
               _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  handleUpdateEventInternal();
               }
               _s = 4;
               break;

            case 2:
               SemanticNoise.deadCode1();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(event, entropy);
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

   private void handlePacketEventInternal(EventPacket ep) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (!ep.isSendPacket()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               Packet<?> packet = ep.getPacket();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= packet.hashCode();
                  _s = 4;
                  break;
               }
               if (packet instanceof PlayerMoveC2SPacket) {
                  bufferPacketInternal(packet);
                  ep.setCancel(true);
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(ep, entropy);
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
      if (l1O0I1lO.opaqueTrue()) {
         this.bufferedPackets.add(packet);
      }
   }

   private void handleUpdateEventInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               resetYVelocityInternal();
               _s = 1;
               break;

            case 1:
               BlockCheckResult result = analyzeBlocksInternal();
               processBlockAnalysisInternal(result);
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

   private void resetYVelocityInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.setVelocity(mc.player.getVelocity().x, lO1I0l1O.d(0.0), mc.player.getVelocity().z);
      }
   }

   private BlockCheckResult analyzeBlocksInternal() {
      l1O0I1lO.fakeHandler();
      Box box = mc.player.getBoundingBox().expand(lO1I0l1O.d(0.001));
      int minX = MathHelper.floor(box.minX);
      int minY = MathHelper.floor(box.minY);
      int minZ = MathHelper.floor(box.minZ);
      int maxX = MathHelper.floor(box.maxX);
      int maxY = MathHelper.floor(box.maxY);
      int maxZ = MathHelper.floor(box.maxZ);

      long totalStates = lO1I0l1O.l(0);
      long solidStates = lO1I0l1O.l(0);

      for (int x = minX; x <= maxX; x++) {
         for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
               BlockPos pos = new BlockPos(x, y, z);
               BlockState state = mc.world.getBlockState(pos);
               totalStates++;
               if (state.isSolid()) {
                  solidStates++;
               }
            }
         }
      }

      if (l1O0I1lO.opaqueFalse()) {
         return new BlockCheckResult(FAKE_ITERATIONS, FAKE_ITERATIONS);
      }

      return new BlockCheckResult(totalStates, solidStates);
   }

   private void processBlockAnalysisInternal(BlockCheckResult result) {
      l1O0I1lO.fakeHandler();
      boolean noSolidInAABB = result.solidStates == lO1I0l1O.l(0);
      boolean semiInsideBlock = I1lO0l1I.all(result.solidStates > lO1I0l1O.l(0), result.solidStates < result.totalStates);

      if (I1lO0l1I.and(I1lO0l1I.not(this.semiPacketSent), semiInsideBlock)) {
         sendSemiPacketsInternal();
         return;
      }

      if (I1lO0l1I.all(this.semiPacketSent, noSolidInAABB)) {
         exitPhaseInternal();
      }
   }

   private void sendSemiPacketsInternal() {
      l1O0I1lO.fakeHandler();
      double x = mc.player.getX();
      double y = mc.player.getY();
      double z = mc.player.getZ();
      float yaw = mc.player.getYaw();
      float pitch = mc.player.getPitch();
      boolean onGround = mc.player.isOnGround();
      int iterations = lO1I0l1O.i(2);

      if (l1O0I1lO.opaqueTrue()) {
         for (int i = 0; i < iterations; i++) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, onGround, false));
         }
      }
      this.semiPacketSent = true;
   }

   private void exitPhaseInternal() {
      l1O0I1lO.fakeHandler();
      this.skipReleaseOnDisable = true;
      this.toggle();
   }

   private void fakePhaseModeInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= System.nanoTime();
      bufferedPackets.clear();
      SemanticNoise.deadCode2();
   }

   @Override
   public void onDisable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (I1lO0l1I.and(I1lO0l1I.not(this.skipReleaseOnDisable), this.semiPacketSent)) {
                  sendReleasePacketsInternal();
               }
               _s = 1;
               break;

            case 1:
               flushBufferedPacketsInternal();
               _s = 2;
               break;

            case 2:
               super.onDisable();
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private void sendReleasePacketsInternal() {
      l1O0I1lO.fakeHandler();
      double x = mc.player.getX();
      double y = mc.player.getY();
      double z = mc.player.getZ();
      float yaw = mc.player.getYaw();
      float pitch = mc.player.getPitch();
      double offset = lO1I0l1O.d(5000.0);

      if (l1O0I1lO.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x - offset, y, z - offset, yaw, pitch, false, false));
         mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, mc.player.isOnGround(), false));
      }
   }

   private void flushBufferedPacketsInternal() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.or(mc.player == null, mc.player.networkHandler == null)) return;
      if (this.bufferedPackets.isEmpty()) return;

      if (l1O0I1lO.opaqueTrue()) {
         for (Packet<?> packet : this.bufferedPackets) {
            NetworkUtils.sendSilentPacket(packet);
         }
         this.bufferedPackets.clear();
      }
   }

   @Override
   public void onEnable() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               this.bufferedPackets.clear();
               this.semiPacketSent = false;
               this.skipReleaseOnDisable = false;
               _s = 1;
               break;

            case 1:
               entropy = System.nanoTime();
               super.onEnable();
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(semiPacketSent, entropy);
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

   private static class BlockCheckResult {
      final long totalStates;
      final long solidStates;

      BlockCheckResult(long total, long solid) {
         this.totalStates = total;
         this.solidStates = solid;
      }
   }
}
