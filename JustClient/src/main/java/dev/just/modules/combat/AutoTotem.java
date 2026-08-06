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
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
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
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoTotem",
   desc = "QmVsaXJsaSBiaXIgc2HEn2zEsWsgZGXEn2VyaW5kZSB0b3RlbWkgb3RvbWF0aWsgb2xhcmFrIGVsaW5pemUgYWzEsXI=",
   type = Type.Combat
)
public class AutoTotem extends Function {
   private final MultiSetting mode = new MultiSetting(
      I0O1l0I1.b("T3RvbWF0aWsgQWw="),
      Arrays.asList(I0O1l0I1.b("S3Jpc3RhbA=="), I0O1l0I1.b("VG9wdXpsdSBPeXVuY3U=")),
      new String[]{I0O1l0I1.b("S3Jpc3RhbA=="), I0O1l0I1.b("VG9wdXpsdSBPeXVuY3U="), I0O1l0I1.b("WWFrxLFuIENyZWVwZXI="), I0O1l0I1.b("T2JzaWR5ZW4="), I0O1l0I1.b("w4dhcGE="), I0O1l0I1.b("RMO8xZ9tZQ=="), I0O1l0I1.b("VmFnb24=")}
   );
   private final SliderSetting HPElytra = new SliderSetting(I0O1l0I1.b("RWx5dHJhIEhQIEJvbnVzdQ=="), 5.0, 2.0, 6.0, 1.0);
   private final BooleanSetting back = new BooleanSetting(I0O1l0I1.b("RcWfeWF5xLEgR2VyaSBWZXI="), true);
   private final BooleanSetting noBallSwitch = new BooleanSetting(I0O1l0I1.b("S8O8cmUgVmFyc2EgQWxtYQ=="), false);
   private final BooleanSetting saveEnchantedtotem = new BooleanSetting(I0O1l0I1.b("QsO8ecO8bMO8IFRvdGVtaSBTYWtsYQ=="), true);
   private final BooleanSetting absorptionCheck = new BooleanSetting(I0O1l0I1.b("KyBBbHTEsW4gS2FscGxlcg=="), false);
   public final SliderSetting hp = new SliderSetting(I0O1l0I1.b("U2HEn2zEsWs="), 4.5, 2.0, 20.0, 0.1F);
   private final SliderSetting crystalDistance = new SliderSetting(I0O1l0I1.b("S3Jpc3RhbCBNZW56aWxp"), 4.0, 2.0, 6.0, 1.0, () -> this.mode.get(I0O1l0I1.b("S3Jpc3RhbA==")));
   private final SliderSetting anchorDistance = new SliderSetting(I0O1l0I1.b("w4dhcGEgTWVuemls"), 4.0, 2.0, 6.0, 1.0, () -> this.mode.get(I0O1l0I1.b("w4dhcGE=")));
   private final SliderSetting minecartDistance = new SliderSetting(I0O1l0I1.b("VmFnb24gTWVuemls"), 4.0, 2.0, 8.0, 1.0, () -> this.mode.get(I0O1l0I1.b("VmFnb24=")));
   private final SliderSetting obsidianDistance = new SliderSetting(I0O1l0I1.b("T2JzaWR5ZW4gTWVuemls"), 4.0, 2.0, 8.0, 1.0, () -> this.mode.get(I0O1l0I1.b("T2JzaWR5ZW4=")));

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
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, FAKE_STATE);
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entropy, _s);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 6);
      Entity e = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= e.hashCode();
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(e, entropy);
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
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(99);
      }
      boolean result = I1lO0l1I.all(
         this.mode.get(I0O1l0I1.b("S3Jpc3RhbA==")),
         e instanceof EndCrystalEntity,
         mc.player != null,
         e.distanceTo(mc.player) <= this.crystalDistance.get().floatValue()
      );
      return lO1I0l1O.bool(result);
   }

   private boolean checkCrystalSpawn(Entity e) {
      return lO1I0l1O.unbool(checkCrystalSpawnFlag(e));
   }

   private int checkMinecartSpawnFlag(Entity e) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(99);
      }
      boolean result = I1lO0l1I.all(
         this.mode.get(I0O1l0I1.b("VmFnb24=")),
         e instanceof TntMinecartEntity,
         mc.player != null,
         e.distanceTo(mc.player) <= this.minecartDistance.get().floatValue()
      );
      return lO1I0l1O.bool(result);
   }

   private boolean checkMinecartSpawn(Entity e) {
      return lO1I0l1O.unbool(checkMinecartSpawnFlag(e));
   }

   private void handleUpdateEvent() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 7);
      int slot = lO1I0l1O.i(-1);
      ItemStack offhand = null;
      int hasTotemFlag = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               slot = getTotemSlotInternal();
               _s = 1;
               break;

            case 1:
               offhand = mc.player.getOffHandStack();
               hasTotemFlag = lO1I0l1O.bool(offhand.getItem() == Items.TOTEM_OF_UNDYING);
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.bits(slot);
                  l1O0I1lO.fakeBranch(offhand, entropy);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 6);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (I1lO0l1I.lessThan(slot, lO1I0l1O.i(0))) {
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
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= slot;
               }
               if (I1lO0l1I.not(lO1I0l1O.unbool(hasTotemFlag))) {
                  swapTotem(slot);
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(offhand, slot);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
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
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(0);
      }

      if (!this.saveEnchantedtotem.get()) return lO1I0l1O.i(0);
      if (offhand.getItem() != Items.TOTEM_OF_UNDYING) return lO1I0l1O.i(0);
      if (!offhand.hasEnchantments()) return lO1I0l1O.i(0);

      ItemStack candidate = mc.player.getInventory().getStack(slot);
      boolean shouldSwap = I1lO0l1I.and(
         candidate.getItem() == Items.TOTEM_OF_UNDYING,
         I1lO0l1I.not(candidate.hasEnchantments())
      );

      if (l1O0I1lO.opaqueTrue() && shouldSwap) {
         InventoryUtil.swapSlotsUniversal(slot, lO1I0l1O.i(40), false, true);
         this.item = slot;
         return lO1I0l1O.i(1);
      }
      return lO1I0l1O.i(0);
   }

   private boolean shouldSwapEnchanted(ItemStack offhand, int slot) {
      return lO1I0l1O.unbool(shouldSwapEnchantedFlag(offhand, slot));
   }

   private void swapTotem(int slot) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         InventoryUtil.swapSlotsUniversal(slot, lO1I0l1O.i(40), false, true);
         if (I1lO0l1I.equals(this.item, lO1I0l1O.i(-1))) {
            this.item = slot;
         }
      }
   }

   private void handleNoNeedTotem() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return;
      }
      if (I1lO0l1I.and(I1lO0l1I.not(I1lO0l1I.equals(this.item, lO1I0l1O.i(-1))), this.back.get())) {
         InventoryUtil.swapSlotsUniversal(this.item, lO1I0l1O.i(40), false, true);
         this.item = lO1I0l1O.i(-1);
      }
   }

   private void forceTotemInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x79DF, 5);
      int slot = lO1I0l1O.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               slot = getTotemSlotInternal();
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.lessThan(slot, lO1I0l1O.i(0))) {
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               ItemStack offhand = mc.player.getOffHandStack();
               if (offhand.getItem() != Items.TOTEM_OF_UNDYING) {
                  InventoryUtil.swapSlotsUniversal(slot, lO1I0l1O.i(40), false, true);
                  this.item = slot;
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x5F37, 6);
      ItemStack offhand = null;
      int result = lO1I0l1O.i(-1);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.i(entropy % 36);
                  _s = 5;
                  break;
               }
               result = findTotemWithEnchantLogic(offhand);
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(offhand, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
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
      l1O0I1lO.fakeHandler();

      if (offhand.getItem() == Items.TOTEM_OF_UNDYING && offhand.hasEnchantments()) {
         int normalTotem = findTotemInternal(false);
         return normalTotem != lO1I0l1O.i(-1) ? normalTotem : lO1I0l1O.i(-1);
      }

      int normalTotem = findTotemInternal(false);
      if (normalTotem != lO1I0l1O.i(-1)) {
         return normalTotem;
      }

      int enchantedTotem = findTotemInternal(true);
      return enchantedTotem != lO1I0l1O.i(-1) ? enchantedTotem : lO1I0l1O.i(-1);
   }

   private int findTotemInternal(boolean enchanted) {
      l1O0I1lO.fakeHandler();

      for (int i = 0; i < mc.player.getInventory().size(); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
            boolean hasEnchant = stack.hasEnchantments();
            if (I1lO0l1I.equals(lO1I0l1O.bool(enchanted), lO1I0l1O.bool(hasEnchant))) {
               return i;
            }
         }
         if (l1O0I1lO.opaqueFalse()) {
            entropy ^= i;
         }
      }
      return lO1I0l1O.i(-1);
   }

   private boolean evaluateCondition() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x3A7F, 7);
      int result = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (checkHealthCondition()) {
                  result = lO1I0l1O.i(1);
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
                  result = lO1I0l1O.i(1);
                  _s = 6;
                  break;
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.i(entropy % 2);
               }
               _s = 6;
               break;

            case 4:
               if (I1lO0l1I.or(checkHPElytraInternal(), checkFallInternal())) {
                  result = lO1I0l1O.i(1);
               }
               _s = 6;
               break;

            case 5:
               l1O0I1lO.fakeBranch(result, entropy);
               _s = 6;
               break;

            case 6:
               return lO1I0l1O.unbool(result);

            default:
               _s = 6;
               break;
         }
      }
   }

   private boolean checkHealthCondition() {
      l1O0I1lO.fakeHandler();
      float absorption = I1lO0l1I.and(this.absorptionCheck.get(), mc.player.hasStatusEffect(StatusEffects.ABSORPTION))
         ? mc.player.getAbsorptionAmount() : lO1I0l1O.f(0.0F);

      if (l1O0I1lO.opaqueFalse()) {
         return absorption > lO1I0l1O.f(100.0F);
      }

      return mc.player.getHealth() + absorption <= this.hp.get().floatValue();
   }

   private boolean checkDangerConditions() {
      l1O0I1lO.fakeHandler();
      return I1lO0l1I.any(
         crystalInternal(),
         anchorInternal(),
         macePlayerInternal(),
         creeperInternal(),
         obsidianInternal()
      );
   }

   private boolean checkFallInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return entropy % lO1I0l1O.i(2) == 0;
      }
      if (!this.mode.get(I0O1l0I1.b("RMO8xZ9tZQ=="))) return false;
      if (mc.player.isGliding()) return false;
      return mc.player.fallDistance > lO1I0l1O.f(10.0F);
   }

   private boolean checkHPElytraInternal() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return false;
      }
      return I1lO0l1I.and(
         ((ItemStack)mc.player.getInventory().armor.get(lO1I0l1O.i(2))).getItem() == Items.ELYTRA,
         mc.player.getHealth() <= this.hp.get().floatValue() + this.HPElytra.get().floatValue()
      );
   }

   private boolean isBallInternal() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(this.mode.get(I0O1l0I1.b("w4dhcGE=")), mc.player.fallDistance > lO1I0l1O.f(5.0F))) {
         return false;
      }
      return I1lO0l1I.and(this.noBallSwitch.get(), mc.player.getOffHandStack().getItem() instanceof PlayerHeadItem);
   }

   private boolean anchorInternal() {
      l1O0I1lO.fakeHandler();
      if (!this.mode.get(I0O1l0I1.b("w4dhcGE="))) return false;
      return InventoryUtil.TotemUtil.getBlock(this.anchorDistance.get().floatValue(), Blocks.RESPAWN_ANCHOR) != null;
   }

   private boolean obsidianInternal() {
      l1O0I1lO.fakeHandler();
      if (!this.mode.get(I0O1l0I1.b("T2JzaWR5ZW4="))) return false;
      return InventoryUtil.TotemUtil.getBlock(this.obsidianDistance.get().floatValue(), Blocks.OBSIDIAN) != null;
   }

   private boolean creeperInternal() {
      l1O0I1lO.fakeHandler();
      if (!this.mode.get(I0O1l0I1.b("WWFrxLFuIENyZWVwZXI="))) return false;

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (entity instanceof CreeperEntity creeper) {
            if (I1lO0l1I.and(
               mc.player.distanceTo(creeper) < lO1I0l1O.f(5.0F),
               creeper.getClientFuseTime(lO1I0l1O.f(0.0F)) > lO1I0l1O.f(0.0F)
            )) {
               return true;
            }
         }
         if (l1O0I1lO.opaqueFalse()) {
            entropy ^= entity.hashCode();
         }
      }
      return false;
   }

   private boolean macePlayerInternal() {
      l1O0I1lO.fakeHandler();
      if (!this.mode.get(I0O1l0I1.b("VG9wdXpsdSBPeXVuY3U="))) return false;

      for (PlayerEntity player : Manager.SYNC_MANAGER.getPlayers()) {
         if (player != mc.player) {
            boolean hasMace = player.getMainHandStack().getItem() == Items.MACE;
            double dy = player.getY() - mc.player.getY();
            double yVel = player.getVelocity().y;
            double distance = (double)player.distanceTo(mc.player);

            boolean isAbove = dy > lO1I0l1O.d(1.5);
            boolean isInAir = I1lO0l1I.all(
               I1lO0l1I.not(player.isOnGround()),
               I1lO0l1I.not(player.isTouchingWater()),
               I1lO0l1I.not(player.isClimbing())
            );
            boolean fallingOrInAir = I1lO0l1I.and(
               I1lO0l1I.or(yVel < lO1I0l1O.d(-0.1), yVel > lO1I0l1O.d(0.1)),
               isInAir
            );

            if (I1lO0l1I.all(hasMace, isAbove, fallingOrInAir, distance < lO1I0l1O.d(24.0))) {
               return true;
            }
         }
         if (l1O0I1lO.opaqueFalse()) {
            entropy ^= player.hashCode();
         }
      }
      return false;
   }

   private boolean crystalInternal() {
      l1O0I1lO.fakeHandler();

      boolean checkCrystal = this.mode.get(I0O1l0I1.b("S3Jpc3RhbA=="));
      boolean checkMinecart = this.mode.get(I0O1l0I1.b("VmFnb24="));

      if (I1lO0l1I.and(I1lO0l1I.not(checkCrystal), I1lO0l1I.not(checkMinecart))) {
         return false;
      }

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (I1lO0l1I.and(checkCrystal, entity instanceof EndCrystalEntity)) {
            if (mc.player.distanceTo(entity) < this.crystalDistance.get().floatValue()) {
               return true;
            }
         }
         if (I1lO0l1I.and(checkMinecart, entity instanceof TntMinecartEntity)) {
            if (mc.player.distanceTo(entity) < this.minecartDistance.get().floatValue()) {
               return true;
            }
         }
         if (l1O0I1lO.opaqueFalse()) {
            entropy ^= entity.hashCode();
         }
      }
      return false;
   }

   private void reload() {
      l1O0I1lO.fakeHandler();
      this.item = lO1I0l1O.i(-1);
   }

   @Override
   protected void onEnable() {
      l1O0I1lO.fakeHandler();
      reload();
      entropy = (int) System.nanoTime();
      super.onEnable();
   }

   @Override
   protected void onDisable() {
      l1O0I1lO.fakeHandler();
      reload();
      super.onDisable();
   }
}
