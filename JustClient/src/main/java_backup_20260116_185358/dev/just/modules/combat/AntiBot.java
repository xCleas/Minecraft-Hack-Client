package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@FunctionAnnotation(
   name = "AntiBot",
   desc = "Anti-Cheat botlar\u0131n\u0131 engeller",
   type = Type.Combat
)
public class AntiBot extends Function {
   private final BooleanSetting removeWorld = new BooleanSetting("D\u00fcnyadan Sil", false);
   private final List<Entity> bots = new ArrayList<>();

   private static final int C_FOOD = 0x14 ^ 0x00;
   private static final int C_ZERO = 0x4A ^ 0x4A;
   private static final int C_ONE = 0x4B ^ 0x4A;
   private static final int C_TWO = 0x48 ^ 0x4A;
   private static final int C_THREE = 0x49 ^ 0x4A;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public AntiBot() {
      this.addSettings(new Setting[]{this.removeWorld});
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
               if (event instanceof EventUpdate) {
                  processEntitiesInternal();
               }
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, FAKE_STATE);
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entropy, _s);
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

   private void processEntitiesInternal() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               _s = 1;
               break;

            case 1:
               for (PlayerEntity entity : Manager.SYNC_MANAGER.getPlayers()) {
                  if (FlowObfuscator.opaqueTrue() && entity != mc.player) {
                     evaluateEntityInternal(entity);
                  }
                  if (FlowObfuscator.opaqueFalse()) {
                     entropy ^= entity.hashCode();
                  }
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(bots, entropy);
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

   private void evaluateEntityInternal(PlayerEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 6);
      int result = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               result = runChecksInternal(entity);
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.i(entropy % 10);
               }
               _s = 2;
               break;

            case 2:
               if (FlowObfuscator.opaqueTrue()) {
                  if (LogicSplit.greaterThan(result, C_ZERO)) {
                     markAsBotInternal(entity);
                  } else {
                     this.bots.remove(entity);
                  }
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= result;
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entity, result);
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

   private void markAsBotInternal(PlayerEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (this.bots.contains(entity)) {
                  _s = 4;
                  break;
               }
               _s = 1;
               break;

            case 1:
               this.bots.add(entity);
               _s = 2;
               break;

            case 2:
               if (FlowObfuscator.opaqueTrue() && this.removeWorld.get()) {
                  mc.world.removeEntity(entity.getId(), Entity.RemovalReason.KILLED);
               }
               _s = 4;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= entity.hashCode();
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

   private int runChecksInternal(PlayerEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x79DF, 7);
      int score = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               score = NumberGuard.c(score, checkArmorInternal(entity));
               _s = 1;
               break;

            case 1:
               score = NumberGuard.c(score, checkItemsInternal(entity));
               _s = 2;
               break;

            case 2:
               score = NumberGuard.c(score, checkStatusInternal(entity));
               _s = 3;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  score = NumberGuard.i(999);
                  _s = 6;
                  break;
               }
               _s = 4;
               break;

            case 4:
               int threshold = NumberGuard.i(7);
               int result = LogicSplit.greaterThan(score, threshold - 1) ? C_ONE : C_ZERO;
               _s = 6;
               return result;

            case 5:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(score, entropy);
               }
               _s = 6;
               break;

            case 6:
               return score;

            default:
               _s = 6;
               break;
         }
      }
   }

   private int checkArmorInternal(PlayerEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x5F37, 6);
      int score = C_ZERO;
      ItemStack[] armor = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               armor = getArmorPiecesInternal(entity);
               _s = 1;
               break;

            case 1:
               boolean full = true;
               for (ItemStack piece : armor) {
                  if (piece.isEmpty()) full = false;
               }
               if (full) score = NumberGuard.c(score, C_ONE);
               _s = 2;
               break;

            case 2:
               boolean ench = true;
               for (ItemStack piece : armor) {
                  if (LogicSplit.and(!piece.isEmpty(), !piece.isEnchantable())) ench = false;
               }
               if (ench) score = NumberGuard.c(score, C_ONE);
               _s = 3;
               break;

            case 3:
               if (FlowObfuscator.opaqueTrue()) {
                  if (isValidArmorSetInternal(armor)) score = NumberGuard.c(score, C_ONE);
               }
               _s = 4;
               break;

            case 4:
               boolean notDamaged = true;
               for (ItemStack piece : armor) {
                  if (piece.isDamaged()) notDamaged = false;
               }
               if (notDamaged) score = NumberGuard.c(score, C_ONE);
               _s = 5;
               break;

            case 5:
               return score;

            default:
               _s = 5;
               break;
         }
      }
   }

   private ItemStack[] getArmorPiecesInternal(PlayerEntity entity) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return new ItemStack[NumberGuard.i(4)];
      }
      return new ItemStack[] {
         entity.getInventory().armor.get(C_ZERO),
         entity.getInventory().armor.get(C_ONE),
         entity.getInventory().armor.get(C_TWO),
         entity.getInventory().armor.get(C_THREE)
      };
   }

   private int isValidArmorSetFlag(ItemStack[] armor) {
      FlowObfuscator.fakeHandler();

      ItemStack boots = armor[C_ZERO];
      ItemStack legs = armor[C_ONE];
      ItemStack chest = armor[C_TWO];
      ItemStack helm = armor[C_THREE];

      boolean leather = LogicSplit.any(
         boots.getItem() == Items.LEATHER_BOOTS,
         legs.getItem() == Items.LEATHER_LEGGINGS,
         chest.getItem() == Items.LEATHER_CHESTPLATE,
         helm.getItem() == Items.LEATHER_HELMET
      );

      boolean iron = LogicSplit.any(
         boots.getItem() == Items.IRON_BOOTS,
         legs.getItem() == Items.IRON_LEGGINGS,
         chest.getItem() == Items.IRON_CHESTPLATE,
         helm.getItem() == Items.IRON_HELMET
      );

      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(boots.getItem() == Items.DIAMOND_BOOTS);
      }

      return NumberGuard.bool(LogicSplit.or(leather, iron));
   }

   private boolean isValidArmorSetInternal(ItemStack[] armor) {
      return NumberGuard.unbool(isValidArmorSetFlag(armor));
   }

   private int checkItemsInternal(PlayerEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x3A7F, 5);
      int score = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueTrue()) {
                  if (entity.getOffHandStack().isEmpty()) score = NumberGuard.c(score, C_ONE);
                  if (!entity.getMainHandStack().isEmpty()) score = NumberGuard.c(score, C_ONE);
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  score = NumberGuard.c(score, entity.getMainHandStack().getCount());
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entity, score);
               _s = 4;
               break;

            case 4:
               return score;

            default:
               _s = 4;
               break;
         }
      }
   }

   private int checkStatusInternal(PlayerEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x2E4B, 5);
      int result = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               int level = entity.getHungerManager().getFoodLevel();
               _s = 1;
               break;

            case 1:
               int level2 = entity.getHungerManager().getFoodLevel();
               if (FlowObfuscator.opaqueTrue()) {
                  result = LogicSplit.equals(level2, NumberGuard.i(C_FOOD)) ? C_ONE : C_ZERO;
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.i(entropy % 5);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entity, result);
               _s = 4;
               break;

            case 4:
               return result;

            default:
               _s = 4;
               break;
         }
      }
   }

   public boolean check(LivingEntity entity) {
      int _s = ControlFlow.next(hashCode() ^ 0x1F7A, 5);
      int result = NumberGuard.i(0);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueTrue()) {
                  result = NumberGuard.bool(LogicSplit.and(
                     entity instanceof PlayerEntity,
                     this.bots.contains(entity)
                  ));
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  result = NumberGuard.bool(entropy % 2 == 0);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeBranch(entity, result);
               _s = 4;
               break;

            case 4:
               return NumberGuard.unbool(result);

            default:
               _s = 4;
               break;
         }
      }
   }

   @Override
   protected void onEnable() {
      FlowObfuscator.fakeHandler();
      super.onEnable();
      this.bots.clear();
      entropy = (int) System.nanoTime();
   }

   @Override
   protected void onDisable() {
      FlowObfuscator.fakeHandler();
      super.onDisable();
      this.bots.clear();
   }
}
