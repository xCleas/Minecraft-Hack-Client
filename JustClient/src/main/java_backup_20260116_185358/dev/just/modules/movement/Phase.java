package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.util.move.NetworkUtils;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
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
      int _s = ControlFlow.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  fakePhaseModeInternal();
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
               dispatchEventInternal(event);
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

   private void dispatchEventInternal(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
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
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!ep.isSendPacket()) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               Packet<?> packet = ep.getPacket();
               if (FlowObfuscator.opaqueFalse()) {
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(ep, entropy);
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
      if (FlowObfuscator.opaqueTrue()) {
         this.bufferedPackets.add(packet);
      }
   }

   private void handleUpdateEventInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               resetYVelocityInternal();
               _s = 1;
               break;

            case 1:
               BlockCheckResult result = analyzeBlocksInternal();
               processBlockAnalysisInternal(result);
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

   private void resetYVelocityInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         mc.player.setVelocity(mc.player.getVelocity().x, NumberGuard.d(0.0), mc.player.getVelocity().z);
      }
   }

   private BlockCheckResult analyzeBlocksInternal() {
      FlowObfuscator.fakeHandler();
      Box box = mc.player.getBoundingBox().expand(NumberGuard.d(0.001));
      int minX = MathHelper.floor(box.minX);
      int minY = MathHelper.floor(box.minY);
      int minZ = MathHelper.floor(box.minZ);
      int maxX = MathHelper.floor(box.maxX);
      int maxY = MathHelper.floor(box.maxY);
      int maxZ = MathHelper.floor(box.maxZ);

      long totalStates = NumberGuard.l(0);
      long solidStates = NumberGuard.l(0);

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

      if (FlowObfuscator.opaqueFalse()) {
         return new BlockCheckResult(FAKE_ITERATIONS, FAKE_ITERATIONS);
      }

      return new BlockCheckResult(totalStates, solidStates);
   }

   private void processBlockAnalysisInternal(BlockCheckResult result) {
      FlowObfuscator.fakeHandler();
      boolean noSolidInAABB = result.solidStates == NumberGuard.l(0);
      boolean semiInsideBlock = LogicSplit.all(result.solidStates > NumberGuard.l(0), result.solidStates < result.totalStates);

      if (LogicSplit.and(LogicSplit.not(this.semiPacketSent), semiInsideBlock)) {
         sendSemiPacketsInternal();
         return;
      }

      if (LogicSplit.all(this.semiPacketSent, noSolidInAABB)) {
         exitPhaseInternal();
      }
   }

   private void sendSemiPacketsInternal() {
      FlowObfuscator.fakeHandler();
      double x = mc.player.getX();
      double y = mc.player.getY();
      double z = mc.player.getZ();
      float yaw = mc.player.getYaw();
      float pitch = mc.player.getPitch();
      boolean onGround = mc.player.isOnGround();
      int iterations = NumberGuard.i(2);

      if (FlowObfuscator.opaqueTrue()) {
         for (int i = 0; i < iterations; i++) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, onGround, false));
         }
      }
      this.semiPacketSent = true;
   }

   private void exitPhaseInternal() {
      FlowObfuscator.fakeHandler();
      this.skipReleaseOnDisable = true;
      this.toggle();
   }

   private void fakePhaseModeInternal() {
      FlowObfuscator.fakeHandler();
      entropy ^= System.nanoTime();
      bufferedPackets.clear();
      SemanticNoise.deadCode2();
   }

   @Override
   public void onDisable() {
      int _s = ControlFlow.next(hashCode() ^ 0x3C1A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (LogicSplit.and(LogicSplit.not(this.skipReleaseOnDisable), this.semiPacketSent)) {
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
               if (FlowObfuscator.opaqueFalse()) {
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
      FlowObfuscator.fakeHandler();
      double x = mc.player.getX();
      double y = mc.player.getY();
      double z = mc.player.getZ();
      float yaw = mc.player.getYaw();
      float pitch = mc.player.getPitch();
      double offset = NumberGuard.d(5000.0);

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x - offset, y, z - offset, yaw, pitch, false, false));
         mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, mc.player.isOnGround(), false));
      }
   }

   private void flushBufferedPacketsInternal() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.or(mc.player == null, mc.player.networkHandler == null)) return;
      if (this.bufferedPackets.isEmpty()) return;

      if (FlowObfuscator.opaqueTrue()) {
         for (Packet<?> packet : this.bufferedPackets) {
            NetworkUtils.sendSilentPacket(packet);
         }
         this.bufferedPackets.clear();
      }
   }

   @Override
   public void onEnable() {
      int _s = ControlFlow.next(hashCode() ^ 0x5D2F, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
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
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(semiPacketSent, entropy);
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

   private static class BlockCheckResult {
      final long totalStates;
      final long solidStates;

      BlockCheckResult(long total, long solid) {
         this.totalStates = total;
         this.solidStates = solid;
      }
   }
}
