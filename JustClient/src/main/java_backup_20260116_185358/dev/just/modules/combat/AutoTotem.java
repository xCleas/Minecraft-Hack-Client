package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventEntitySpawn;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.util.player.InventoryUtil;
import java.util.Arrays;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PlayerHeadItem;
import net.minecraft.block.Blocks;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "AutoTotem",
   desc = "QmVsaXJsaSBiaXIgc2HEn2zEsWsgZGXEn2VyaW5kZSB0b3RlbWkgb3RvbWF0aWsgb2xhcmFrIGVsaW5pemUgYWzEsXI=",
   type = Type.Combat
)
public class AutoTotem extends Function {
   private final MultiSetting mode = new MultiSetting(
      Strings.b("T3RvbWF0aWsgQWw="),
      Arrays.asList(Strings.b("S3Jpc3RhbA=="), Strings.b("VG9wdXpsdSBPeXVuY3U=")),
      new String[]{Strings.b("S3Jpc3RhbA=="), Strings.b("VG9wdXpsdSBPeXVuY3U="), Strings.b("WWFrxLFuIENyZWVwZXI="), Strings.b("T2JzaWR5ZW4="), Strings.b("w4dhcGE="), Strings.b("RMO8xZ9tZQ=="), Strings.b("VmFnb24=")}
   );
   private final SliderSetting HPElytra = new SliderSetting(Strings.b("RWx5dHJhIEhQIEJvbnVzdQ=="), 5.0, 2.0, 6.0, 1.0);
   private final BooleanSetting back = new BooleanSetting(Strings.b("RcWfeWF5xLEgR2VyaSBWZXI="), true);
   private final BooleanSetting noBallSwitch = new BooleanSetting(Strings.b("S8O8cmUgVmFyc2EgQWxtYQ=="), false);
   private final BooleanSetting saveEnchantedtotem = new BooleanSetting(Strings.b("QsO8ecO8bMO8IFRvdGVtaSBTYWtsYQ=="), true);
   private final BooleanSetting absorptionCheck = new BooleanSetting(Strings.b("KyBBbHTEsW4gS2FscGxlcg=="), false);
   public final SliderSetting hp = new SliderSetting(Strings.b("U2HEn2zEsWs="), 4.5, 2.0, 20.0, 0.1F);
   private final SliderSetting crystalDistance = new SliderSetting(Strings.b("S3Jpc3RhbCBNZW56aWxp"), 4.0, 2.0, 6.0, 1.0, () -> this.mode.get(Strings.b("S3Jpc3RhbA==")));
   private final SliderSetting anchorDistance = new SliderSetting(Strings.b("w4dhcGEgTWVuemls"), 4.0, 2.0, 6.0, 1.0, () -> this.mode.get(Strings.b("w4dhcGE=")));
   private final SliderSetting minecartDistance = new SliderSetting(Strings.b("VmFnb24gTWVuemls"), 4.0, 2.0, 8.0, 1.0, () -> this.mode.get(Strings.b("VmFnb24=")));
   private final SliderSetting obsidianDistance = new SliderSetting(Strings.b("T2JzaWR5ZW4gTWVuemls"), 4.0, 2.0, 8.0, 1.0, () -> this.mode.get(Strings.b("T2JzaWR5ZW4=")));

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();
   private int item = -1;

   public AutoTotem() {
      this.addSettings(
         new Setting[]{
            this.mode, this.hp, this.HPElytra, this.back, this.noBallSwitch,
            this.saveEnchantedtotem, this.absorptionCheck, this.crystalDistance,
            this.anchorDistance, this.minecartDistance, this.obsidianDistance
         }
      );
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventEntitySpawn spawnEvent) {
                  handleSpawnEvent(spawnEvent);
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventUpdate) {
                  handleUpdateEvent();
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, FAKE_STATE);
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entropy, _s);
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

   private void handleSpawnEvent(EventEntitySpawn spawnEvent) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 6);
      Entity e = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               e = spawnEvent.getEntity();
               _s = 1;
               break;

            case 1:
               if (checkCrystalSpawn(e)) {
                  forceTotemInternal();
               }
               _s = 2;
               break;

            case 2:
               if (checkMinecartSpawn(e)) {
                  forceTotemInternal();
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= e.hashCode();
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(e, entropy);
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

   private int checkCrystalSpawnFlag(Entity e) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(99);
      }
      boolean result = LogicSplit.all(
         this.mode.get(Strings.b("S3Jpc3RhbA==")),
         e instanceof EndCrystalEntity,
         mc.player != null,
         e.distanceTo(mc.player) <= this.crystalDistance.get().floatValue()
      );
      return NumberGuard.bool(result);
   }

   private boolean checkCrystalSpawn(Entity e) {
      return NumberGuard.unbool(checkCrystalSpawnFlag(e));
   }

   private int checkMinecartSpawnFlag(Entity e) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(99);
      }
      boolean result = LogicSplit.all(
         this.mode.get(Strings.b("VmFnb24=")),
         e instanceof TntMinecartEntity,
         mc.player != null,
         e.distanceTo(mc.player) <= this.minecartDistance.get().floatValue()
      );
      return NumberGuard.bool(result);
   }

   private boolean checkMinecartSpawn(Entity e) {
      return NumberGuard.unbool(checkMinecartSpawnFlag(e));
   }

   private void handleUpdateEvent() {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 7);
      int slot = NumberGuard.i(-1);
      ItemStack offhand = null;
      int hasTotemFlag = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               slot = getTotemSlotInternal();
               _s = 1;
               break;

            case 1:
               offhand = mc.player.getOffHandStack();
               hasTotemFlag = NumberGuard.bool(offhand.getItem() == Items.TOTEM_OF_UNDYING);
               _s = 2;
               break;

            case 2:
               if (evaluateCondition()) {
                  _s = 3;
               } else {
                  _s = 4;
               }
               break;

            case 3:
               handleNeedTotem(slot, offhand, hasTotemFlag);
               _s = 6;
               break;

            case 4:
               handleNoNeedTotem();
               _s = 6;
               break;

            case 5:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.bits(slot);
                  FlowObfuscator.fakeBranch(offhand, entropy);
               }
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

   private void handleNeedTotem(int slot, ItemStack offhand, int hasTotemFlag) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 6);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (LogicSplit.lessThan(slot, NumberGuard.i(0))) {
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (shouldSwapEnchanted(offhand, slot)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= slot;
               }
               if (LogicSplit.not(NumberGuard.unbool(hasTotemFlag))) {
                  swapTotem(slot);
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(offhand, slot);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
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

   private int shouldSwapEnchantedFlag(ItemStack offhand, int slot) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.i(0);
      }

      if (!this.saveEnchantedtotem.get()) return NumberGuard.i(0);
      if (offhand.getItem() != Items.TOTEM_OF_UNDYING) return NumberGuard.i(0);
      if (!offhand.hasEnchantments()) return NumberGuard.i(0);

      ItemStack candidate = mc.player.getInventory().getStack(slot);
      boolean shouldSwap = LogicSplit.and(
         candidate.getItem() == Items.TOTEM_OF_UNDYING,
         LogicSplit.not(candidate.hasEnchantments())
      );

      if (FlowObfuscator.opaqueTrue() && shouldSwap) {
         InventoryUtil.swapSlotsUniversal(slot, NumberGuard.i(40), false, true);
         this.item = slot;
         return NumberGuard.i(1);
      }
      return NumberGuard.i(0);
   }

   private boolean shouldSwapEnchanted(ItemStack offhand, int slot) {
      return NumberGuard.unbool(shouldSwapEnchantedFlag(offhand, slot));
   }

   private void swapTotem(int slot) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         InventoryUtil.swapSlotsUniversal(slot, NumberGuard.i(40), false, true);
         if (LogicSplit.equals(this.item, NumberGuard.i(-1))) {
            this.item = slot;
         }
      }
   }

   private void handleNoNeedTotem() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return;
      }
      if (LogicSplit.and(LogicSplit.not(LogicSplit.equals(this.item, NumberGuard.i(-1))), this.back.get())) {
         InventoryUtil.swapSlotsUniversal(this.item, NumberGuard.i(40), false, true);
         this.item = NumberGuard.i(-1);
      }
   }

   private void forceTotemInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x79DF, 5);
      int slot = NumberGuard.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               slot = getTotemSlotInternal();
               _s = 1;
               break;

            case 1:
               if (LogicSplit.lessThan(slot, NumberGuard.i(0))) {
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               ItemStack offhand = mc.player.getOffHandStack();
               if (offhand.getItem() != Items.TOTEM_OF_UNDYING) {
                  InventoryUtil.swapSlotsUniversal(slot, NumberGuard.i(40), false, true);
                  this.item = slot;
               }
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= slot;
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

   private int getTotemSlotInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x5F37, 6);
      ItemStack offhand = null;
      int result = NumberGuard.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               offhand = mc.player.getOffHandStack();
               _s = 1;
               break;

            case 1:
               if (!this.saveEnchantedtotem.get()) {
                  result = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.i(entropy % 36);
                  _s = 5;
                  break;
               }
               result = findTotemWithEnchantLogic(offhand);
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(offhand, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
               _s = 5;
               break;

            case 5:
               return result;

            default:
               _s = 5;
               break;
         }
      }
   }

   private int findTotemWithEnchantLogic(ItemStack offhand) {
      FlowObfuscator.fakeHandler();

      if (offhand.getItem() == Items.TOTEM_OF_UNDYING && offhand.hasEnchantments()) {
         int normalTotem = findTotemInternal(false);
         return normalTotem != NumberGuard.i(-1) ? normalTotem : NumberGuard.i(-1);
      }

      int normalTotem = findTotemInternal(false);
      if (normalTotem != NumberGuard.i(-1)) {
         return normalTotem;
      }

      int enchantedTotem = findTotemInternal(true);
      return enchantedTotem != NumberGuard.i(-1) ? enchantedTotem : NumberGuard.i(-1);
   }

   private int findTotemInternal(boolean enchanted) {
      FlowObfuscator.fakeHandler();

      for (int i = 0; i < mc.player.getInventory().size(); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
            boolean hasEnchant = stack.hasEnchantments();
            if (LogicSplit.equals(NumberGuard.bool(enchanted), NumberGuard.bool(hasEnchant))) {
               return i;
            }
         }
         if (FlowObfuscator.opaqueFalse()) {
            entropy ^= i;
         }
      }
      return NumberGuard.i(-1);
   }

   private boolean evaluateCondition() {
      int _s = ControlFlow.next(hashCode() ^ 0x3A7F, 7);
      int result = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (checkHealthCondition()) {
                  result = NumberGuard.i(1);
                  _s = 6;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (isBallInternal()) {
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (checkDangerConditions()) {
                  result = NumberGuard.i(1);
                  _s = 6;
                  break;
               }
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.i(entropy % 2);
               }
               _s = 6;
               break;

            case 4:
               if (LogicSplit.or(checkHPElytraInternal(), checkFallInternal())) {
                  result = NumberGuard.i(1);
               }
               _s = 6;
               break;

            case 5:
               FlowObfuscator.fakeBranch(result, entropy);
               _s = 6;
               break;

            case 6:
               return NumberGuard.unbool(result);

            default:
               _s = 6;
               break;
         }
      }
   }

   private boolean checkHealthCondition() {
      FlowObfuscator.fakeHandler();
      float absorption = LogicSplit.and(this.absorptionCheck.get(), mc.player.hasStatusEffect(StatusEffects.ABSORPTION))
         ? mc.player.getAbsorptionAmount() : NumberGuard.f(0.0F);

      if (FlowObfuscator.opaqueFalse()) {
         return absorption > NumberGuard.f(100.0F);
      }

      return mc.player.getHealth() + absorption <= this.hp.get().floatValue();
   }

   private boolean checkDangerConditions() {
      FlowObfuscator.fakeHandler();
      return LogicSplit.any(
         crystalInternal(),
         anchorInternal(),
         macePlayerInternal(),
         creeperInternal(),
         obsidianInternal()
      );
   }

   private boolean checkFallInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return entropy % NumberGuard.i(2) == 0;
      }
      if (!this.mode.get(Strings.b("RMO8xZ9tZQ=="))) return false;
      if (mc.player.isGliding()) return false;
      return mc.player.fallDistance > NumberGuard.f(10.0F);
   }

   private boolean checkHPElytraInternal() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return false;
      }
      return LogicSplit.and(
         ((ItemStack)mc.player.getInventory().armor.get(NumberGuard.i(2))).getItem() == Items.ELYTRA,
         mc.player.getHealth() <= this.hp.get().floatValue() + this.HPElytra.get().floatValue()
      );
   }

   private boolean isBallInternal() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.and(this.mode.get(Strings.b("w4dhcGE=")), mc.player.fallDistance > NumberGuard.f(5.0F))) {
         return false;
      }
      return LogicSplit.and(this.noBallSwitch.get(), mc.player.getOffHandStack().getItem() instanceof PlayerHeadItem);
   }

   private boolean anchorInternal() {
      FlowObfuscator.fakeHandler();
      if (!this.mode.get(Strings.b("w4dhcGE="))) return false;
      return InventoryUtil.TotemUtil.getBlock(this.anchorDistance.get().floatValue(), Blocks.RESPAWN_ANCHOR) != null;
   }

   private boolean obsidianInternal() {
      FlowObfuscator.fakeHandler();
      if (!this.mode.get(Strings.b("T2JzaWR5ZW4="))) return false;
      return InventoryUtil.TotemUtil.getBlock(this.obsidianDistance.get().floatValue(), Blocks.OBSIDIAN) != null;
   }

   private boolean creeperInternal() {
      FlowObfuscator.fakeHandler();
      if (!this.mode.get(Strings.b("WWFrxLFuIENyZWVwZXI="))) return false;

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (entity instanceof CreeperEntity creeper) {
            if (LogicSplit.and(
               mc.player.distanceTo(creeper) < NumberGuard.f(5.0F),
               creeper.getClientFuseTime(NumberGuard.f(0.0F)) > NumberGuard.f(0.0F)
            )) {
               return true;
            }
         }
         if (FlowObfuscator.opaqueFalse()) {
            entropy ^= entity.hashCode();
         }
      }
      return false;
   }

   private boolean macePlayerInternal() {
      FlowObfuscator.fakeHandler();
      if (!this.mode.get(Strings.b("VG9wdXpsdSBPeXVuY3U="))) return false;

      for (PlayerEntity player : Manager.SYNC_MANAGER.getPlayers()) {
         if (player != mc.player) {
            boolean hasMace = player.getMainHandStack().getItem() == Items.MACE;
            double dy = player.getY() - mc.player.getY();
            double yVel = player.getVelocity().y;
            double distance = (double)player.distanceTo(mc.player);

            boolean isAbove = dy > NumberGuard.d(1.5);
            boolean isInAir = LogicSplit.all(
               LogicSplit.not(player.isOnGround()),
               LogicSplit.not(player.isTouchingWater()),
               LogicSplit.not(player.isClimbing())
            );
            boolean fallingOrInAir = LogicSplit.and(
               LogicSplit.or(yVel < NumberGuard.d(-0.1), yVel > NumberGuard.d(0.1)),
               isInAir
            );

            if (LogicSplit.all(hasMace, isAbove, fallingOrInAir, distance < NumberGuard.d(24.0))) {
               return true;
            }
         }
         if (FlowObfuscator.opaqueFalse()) {
            entropy ^= player.hashCode();
         }
      }
      return false;
   }

   private boolean crystalInternal() {
      FlowObfuscator.fakeHandler();

      boolean checkCrystal = this.mode.get(Strings.b("S3Jpc3RhbA=="));
      boolean checkMinecart = this.mode.get(Strings.b("VmFnb24="));

      if (LogicSplit.and(LogicSplit.not(checkCrystal), LogicSplit.not(checkMinecart))) {
         return false;
      }

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (LogicSplit.and(checkCrystal, entity instanceof EndCrystalEntity)) {
            if (mc.player.distanceTo(entity) < this.crystalDistance.get().floatValue()) {
               return true;
            }
         }
         if (LogicSplit.and(checkMinecart, entity instanceof TntMinecartEntity)) {
            if (mc.player.distanceTo(entity) < this.minecartDistance.get().floatValue()) {
               return true;
            }
         }
         if (FlowObfuscator.opaqueFalse()) {
            entropy ^= entity.hashCode();
         }
      }
      return false;
   }

   private void reload() {
      FlowObfuscator.fakeHandler();
      this.item = NumberGuard.i(-1);
   }

   @Override
   protected void onEnable() {
      FlowObfuscator.fakeHandler();
      reload();
      entropy = (int) System.nanoTime();
      super.onEnable();
   }

   @Override
   protected void onDisable() {
      FlowObfuscator.fakeHandler();
      reload();
      super.onDisable();
   }
}
