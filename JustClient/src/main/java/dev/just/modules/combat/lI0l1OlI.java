package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventPacket;
import dev.just.events.impl.input.EventKeyBoard;
import dev.just.events.impl.move.EventMotion;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.color.ColorUtil;
import dev.just.util.move.MoveUtil;
import dev.just.util.player.InventoryUtil;
import dev.just.util.player.TimerUtil;
import dev.just.util.render.RenderUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.joml.Vector2f;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "CrystalAura",
   type = Type.Combat,
   desc = "QWvEsWxsxLEga3Jpc3RhbCB5ZXJsZcWfdGlybWUgdmUgcGF0bGF0bWEgbW9kw7xsw7w="
)
public class lI0l1OlI extends Function {
   private final MultiSetting options = new MultiSetting(
      I0O1l0I1.b("QXlhcmxhcg=="),
      List.of(I0O1l0I1.b("U2VsZi1EYW1hZ2UgRW5nZWxsZQ=="), I0O1l0I1.b("SGFyZWtldCBEw7x6ZWx0bWU="), I0O1l0I1.b("RMO8xZ9lbiBPeXVuY3U="), I0O1l0I1.b("QmxvayBWdXJndWxhbWE=")),
      new String[]{I0O1l0I1.b("U2VsZi1EYW1hZ2UgRW5nZWxsZQ=="), I0O1l0I1.b("SGFyZWtldCBEw7x6ZWx0bWU="), I0O1l0I1.b("RMO8xZ9lbiBPeXVuY3U="), I0O1l0I1.b("QmxvayBWdXJndWxhbWE=")}
   );
   private final ModeSetting distanceMode = new ModeSetting(I0O1l0I1.b("TWVuemxsIFRpcGk="), I0O1l0I1.b("w5Z6ZWw="), I0O1l0I1.b("w5Z6ZWw="), "Grim");
   private final SliderSetting customDistance = new SliderSetting(I0O1l0I1.b("TWVuemls"), 5.0, 2.5, 6.0, 0.05F, () -> this.distanceMode.is(I0O1l0I1.b("w5Z6ZWw=")));
   private final SliderSetting breakDelay = new SliderSetting(I0O1l0I1.b("R2VjaWttZQ=="), 100.0, 0.0, 500.0, 1.0);
   public final BooleanSetting offHandCrystal = new BooleanSetting(I0O1l0I1.b("U29sIEVsZSBLcmlzdGFsIEFs"), true);
   private final BooleanSetting renderBlock = new BooleanSetting(I0O1l0I1.b("QmxvayBSZW5kZXI="), true);
   private final BooleanSetting rgCheck = new BooleanSetting(I0O1l0I1.b("QsO2bGdlIEtvbnRyb2zDvA=="), true, I0O1l0I1.b("Q2xhaW1saSBiw7ZsZ2VsZXJpIGtvbnRyb2wgZWRlcg=="));
   private final BooleanSetting twoPlace = new BooleanSetting(I0O1l0I1.b("w4dva2x1IFllcmxlxZ90aXJtZQ=="), true);
   private BlockPos closestObsidian = null;
   public EndCrystalEntity closestCrystal = null;
   private Vec3d obsidianVec = null;
   private final TimerUtil attackTimer = new TimerUtil();
   private final TimerUtil placeTimer = new TimerUtil();
   public final Vector2f rotate = new Vector2f(0.0F, 0.0F);
   private int originalSlot = -1;
   private boolean regionBlocked = false;
   private Vec3d regionBlockPos = null;
   private static final double REGION_BLOCK_RADIUS = 12.0;
   private static final double MIN_DISTANCE_BETWEEN_CRYSTALS = 2.5;
   private final List<BlockPos> multiPlaceTargets = new ArrayList<>();
   private Item originalOffhandItem = null;

   public lI0l1OlI() {
      this.addSettings(
         new Setting[]{
            this.distanceMode, this.options, this.customDistance, this.breakDelay, this.offHandCrystal, this.renderBlock, this.rgCheck, this.twoPlace
         }
      );
   }

   private double getEffectiveDistance() {
      return this.distanceMode.is("Grim") ? 3.6 : (double)this.customDistance.get().intValue();
   }

   public boolean check() {
      return (this.closestObsidian != null || this.closestCrystal != null) && this.rotate != null && this.options.get(I0O1l0I1.b("SGFyZWtldCBEw7x6ZWx0bWU="));
   }

   @Override
   public void onDisable() {
      this.reset();
      super.onDisable();
   }

   @Override
   public void onEvent(Event event) {
      if (this.rgCheck.get() && event instanceof EventPacket eventPacket && eventPacket.getPacket() instanceof GameMessageS2CPacket packet) {
         String message = packet.content().getString();
         if (message.contains("can't place that block here")) {
            this.regionBlocked = true;
            this.regionBlockPos = mc.player.getPos();
         }
      }

      if (event instanceof EventRender3D && this.renderBlock.get() && this.obsidianVec != null) {
         BlockPos pos = BlockPos.ofFloored(this.obsidianVec);
         RenderUtil.render3D.drawHoleOutline(new Box(pos), ColorUtil.getColorStyle(360.0F), 2.0F);
         if (!this.multiPlaceTargets.isEmpty()) {
            for (BlockPos p : this.multiPlaceTargets) {
               RenderUtil.render3D.drawHoleOutline(new Box(p), ColorUtil.getColorStyle(220.0F), 1.0F);
            }
         }
      }

      if (event instanceof EventKeyBoard input && this.check()) {
         MoveUtil.fixMovement(input, this.rotate.x);
      }

      if (event instanceof EventMotion motion) {
         if (this.offHandCrystal.get()) {
            float currentHp = mc.player.getHealth();
            float minHp = Manager.FUNCTION_MANAGER.autoTotem.hp.get().floatValue();
            if (currentHp > minHp) {
               InventoryUtil.moveToOffhand(Items.END_CRYSTAL);
            }
         }

         this.handleCrystalLogic(motion);
      }
   }

   private void handleCrystalLogic(EventMotion motion) {
      if (this.regionBlocked && this.regionBlockPos != null) {
         if (mc.player.getPos().distanceTo(this.regionBlockPos) < 12.0) {
            return;
         }

         this.regionBlocked = false;
         this.regionBlockPos = null;
      }

      double maxDist = this.getEffectiveDistance();
      this.closestCrystal = this.findNearestCrystal(maxDist);
      if (this.closestCrystal != null) {
         this.breakCrystal(this.closestCrystal, motion);
      } else {
         if (this.originalSlot == -1) {
            this.originalSlot = mc.player.getInventory().selectedSlot;
         }

         int crystalSlot = InventoryUtil.getItem(Items.END_CRYSTAL.getClass(), true);
         if (crystalSlot == -1 && !this.offHandCrystal.get()) {
            this.restoreOriginalSlot();
         } else {
            this.closestObsidian = null;
            double bestDamage = 0.0;

            for (Entity e : Manager.SYNC_MANAGER.getEntities()) {
               if (e instanceof LivingEntity) {
                  LivingEntity target = (LivingEntity)e;
                  if (e != mc.player && e.isAlive()) {
                     BlockPos pos = this.findBestCrystalPosition(target, maxDist);
                     if (pos != null) {
                        double damage = this.calculateCrystalDamage(pos, e);
                        if ((!this.options.get(I0O1l0I1.b("U2VsZi1EYW1hZ2UgRW5nZWxsZQ==")) || !(this.calculateCrystalDamage(pos, mc.player) > 6.0)) && damage > bestDamage) {
                           bestDamage = damage;
                           this.closestObsidian = pos;
                        }
                     }
                  }
               }
            }

            this.multiPlaceTargets.clear();
            if (this.closestObsidian != null) {
               this.obsidianVec = Vec3d.ofCenter(this.closestObsidian);
               this.aimAt(this.obsidianVec, motion);
               this.multiPlaceTargets.add(this.closestObsidian);
               if (this.twoPlace.get()) {
                  List<BlockPos> candidates = new ArrayList<>();
                  BlockPos[] nearby = new BlockPos[]{
                     this.closestObsidian.north(),
                     this.closestObsidian.south(),
                     this.closestObsidian.east(),
                     this.closestObsidian.west(),
                     this.closestObsidian.north().east(),
                     this.closestObsidian.north().west(),
                     this.closestObsidian.south().east(),
                     this.closestObsidian.south().west()
                  };

                  for (BlockPos pos : nearby) {
                     if (this.canPlaceCrystal(pos, maxDist)) {
                        boolean tooClose = false;

                        for (BlockPos existing : this.multiPlaceTargets) {
                           if (existing.getSquaredDistance(pos) < 6.25) {
                              tooClose = true;
                              break;
                           }
                        }

                        if (!tooClose) {
                           candidates.add(pos);
                        }
                     }
                  }

                  Collections.shuffle(candidates);

                  for (BlockPos posx : candidates) {
                     if (this.multiPlaceTargets.size() >= 3) {
                        break;
                     }

                     this.multiPlaceTargets.add(posx);
                  }
               }
            }

            if (!this.multiPlaceTargets.isEmpty() && this.placeTimer.hasTimeElapsed((long)this.breakDelay.get().doubleValue())) {
               if (!this.offHandCrystal.get()) {
                  mc.player.getInventory().selectedSlot = crystalSlot;
               }

               for (BlockPos posx : this.multiPlaceTargets) {
                  this.tryPlaceCrystal(posx, motion);
               }

               this.multiPlaceTargets.clear();
               this.placeTimer.reset();
            }
         }
      }
   }

   private void breakCrystal(Entity crystal, EventMotion motion) {
      if (crystal != null) {
         this.aimAt(crystal.getPos().add(0.0, 0.5, 0.0), motion);
         if (this.attackTimer.hasTimeElapsed((long)this.breakDelay.get().doubleValue())) {
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
            this.attackTimer.reset();
         }
      }
   }

   private BlockPos findBestCrystalPosition(LivingEntity target, double maxDist) {
      BlockPos base = target.getBlockPos();
      BlockPos[] positions = new BlockPos[]{
         base.north(),
         base.south(),
         base.east(),
         base.west(),
         base.north().east(),
         base.north().west(),
         base.south().east(),
         base.south().west(),
         base.up(),
         base.up().north(),
         base.up().south(),
         base.up().east(),
         base.up().west(),
         base.up().north().east(),
         base.up().north().west(),
         base.up().south().east(),
         base.up().south().west()
      };
      BlockPos bestPos = null;
      double bestDamage = 0.0;

      for (BlockPos airPos : positions) {
         BlockPos baseBlock = airPos.down();
         if (!baseBlock.equals(mc.player.getBlockPos().down()) && this.canPlaceCrystal(baseBlock, maxDist)) {
            double damage = this.calculateCrystalDamage(baseBlock, target);
            double selfDamage = this.calculateCrystalDamage(baseBlock, mc.player);
            if ((!this.options.get(I0O1l0I1.b("U2VsZi1EYW1hZ2UgRW5nZWxsZQ==")) || !(selfDamage > 6.0)) && damage > bestDamage) {
               bestDamage = damage;
               bestPos = baseBlock;
            }
         }
      }

      return bestPos;
   }

   private boolean canPlaceCrystal(BlockPos baseBlock, double maxDist) {
      if (mc.world != null && mc.player != null) {
         if (mc.world.getBlockState(baseBlock).getBlock() != Blocks.OBSIDIAN
            && mc.world.getBlockState(baseBlock).getBlock() != Blocks.BEDROCK) {
            return false;
         } else {
            BlockPos air1 = baseBlock.up();
            BlockPos air2 = baseBlock.up(2);
            if (mc.world.getBlockState(air1).isAir() && mc.world.getBlockState(air2).isAir()) {
               if (mc.player.getPos().distanceTo(Vec3d.ofCenter(baseBlock)) > maxDist) {
                  return false;
               } else {
                  Box placeBox = new Box(
                     (double)air1.getX(),
                     (double)air1.getY(),
                     (double)air1.getZ(),
                     (double)(air1.getX() + 1),
                     (double)(air1.getY() + 1),
                     (double)(air1.getZ() + 1)
                  );

                  for (Entity e : mc.world.getOtherEntities(null, placeBox)) {
                     if (e instanceof EndCrystalEntity) {
                        return false;
                     }
                  }

                  return true;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void tryPlaceCrystal(BlockPos pos, EventMotion motion) {
      if (pos != null && mc.player != null && mc.interactionManager != null) {
         BlockHitResult hitResult = new BlockHitResult(Vec3d.of(pos).add(0.5, 1.0, 0.5), Direction.UP, pos, false);
         mc.interactionManager.interactBlock(mc.player, this.offHandCrystal.get() ? Hand.OFF_HAND : Hand.MAIN_HAND, hitResult);
         mc.player.swingHand(this.offHandCrystal.get() ? Hand.OFF_HAND : Hand.MAIN_HAND);
         EndCrystalEntity newCrystal = null;

         for (Entity e : Manager.SYNC_MANAGER.getEntities()) {
            if (e instanceof EndCrystalEntity && e.squaredDistanceTo(Vec3d.ofCenter(pos.up())) < 3.2) {
               newCrystal = (EndCrystalEntity)e;
               break;
            }
         }

         if (newCrystal != null) {
            this.breakCrystal(newCrystal, motion);
         }
      }
   }

   private EndCrystalEntity findNearestCrystal(double maxDist) {
      EndCrystalEntity closest = null;
      double minDist = Double.MAX_VALUE;
      Vec3d eyePos = mc.player.getEyePos();

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (entity instanceof EndCrystalEntity) {
            EndCrystalEntity crystal = (EndCrystalEntity)entity;
            if (crystal.isAlive()) {
               double dist = mc.player.squaredDistanceTo(crystal);
               if (dist <= maxDist * maxDist && dist < minDist) {
                  closest = crystal;
                  minDist = dist;
               }
            }
         }
      }

      return closest;
   }

   private double calculateCrystalDamage(BlockPos pos, Entity target) {
      if (target != null && mc.world != null) {
         double distance = target.squaredDistanceTo(Vec3d.ofCenter(pos));
         if (distance > 12.0) {
            return 0.0;
         } else {
            double exposure = 1.0 - distance / 12.0;
            double damage = exposure * 12.0;
            return Math.max(0.0, damage);
         }
      } else {
         return 0.0;
      }
   }

   private void aimAt(Vec3d vec, EventMotion motion) {
      float[] rot = rotations(vec);
      if (motion != null) {
         motion.setYaw(rot[0]);
         motion.setPitch(rot[1]);
      }

      this.rotate.set(rot[0], rot[1]);
   }

   private void restoreOriginalSlot() {
      if (this.originalSlot >= 0 && this.originalSlot <= 8) {
         mc.player.getInventory().selectedSlot = this.originalSlot;
         this.originalSlot = -1;
      }
   }

   public void reset() {
      this.restoreOriginalSlot();
      this.closestObsidian = null;
      this.closestCrystal = null;
      this.obsidianVec = null;
      this.multiPlaceTargets.clear();
      this.attackTimer.reset();
      this.placeTimer.reset();
      this.regionBlocked = false;
      this.regionBlockPos = null;
   }

   public static float[] rotations(Vec3d vec) {
      double dx = vec.x - mc.player.getX();
      double dy = vec.y - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double dz = vec.z - mc.player.getZ();
      double dist = Math.sqrt(dx * dx + dz * dz);
      float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
      float pitch = (float)(-Math.toDegrees(Math.atan2(dy, dist)));
      return new float[]{yaw, pitch};
   }
}
