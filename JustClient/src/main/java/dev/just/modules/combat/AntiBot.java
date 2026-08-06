package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
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
               if (event instanceof EventUpdate) {
                  processEntitiesInternal();
               }
               _s = 5;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, FAKE_STATE);
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               l1O0I1lO.fakeBranch(entropy, _s);
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

   private void processEntitiesInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               _s = 1;
               break;

            case 1:
               for (PlayerEntity entity : Manager.SYNC_MANAGER.getPlayers()) {
                  if (l1O0I1lO.opaqueTrue() && entity != mc.player) {
                     evaluateEntityInternal(entity);
                  }
                  if (l1O0I1lO.opaqueFalse()) {
                     entropy ^= entity.hashCode();
                  }
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(bots, entropy);
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

   private void evaluateEntityInternal(PlayerEntity entity) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 6);
      int result = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               result = runChecksInternal(entity);
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.i(entropy % 10);
               }
               _s = 2;
               break;

            case 2:
               if (l1O0I1lO.opaqueTrue()) {
                  if (I1lO0l1I.greaterThan(result, C_ZERO)) {
                     markAsBotInternal(entity);
                  } else {
                     this.bots.remove(entity);
                  }
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= result;
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entity, result);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (l1O0I1lO.opaqueTrue() && this.removeWorld.get()) {
                  mc.world.removeEntity(entity.getId(), Entity.RemovalReason.KILLED);
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x79DF, 7);
      int score = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               score = lO1I0l1O.c(score, checkArmorInternal(entity));
               _s = 1;
               break;

            case 1:
               score = lO1I0l1O.c(score, checkItemsInternal(entity));
               _s = 2;
               break;

            case 2:
               score = lO1I0l1O.c(score, checkStatusInternal(entity));
               _s = 3;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  score = lO1I0l1O.i(999);
                  _s = 6;
                  break;
               }
               _s = 4;
               break;

            case 4:
               int threshold = lO1I0l1O.i(7);
               int result = I1lO0l1I.greaterThan(score, threshold - 1) ? C_ONE : C_ZERO;
               _s = 6;
               return result;

            case 5:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(score, entropy);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x5F37, 6);
      int score = C_ZERO;
      ItemStack[] armor = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               armor = getArmorPiecesInternal(entity);
               _s = 1;
               break;

            case 1:
               boolean full = true;
               for (ItemStack piece : armor) {
                  if (piece.isEmpty()) full = false;
               }
               if (full) score = lO1I0l1O.c(score, C_ONE);
               _s = 2;
               break;

            case 2:
               boolean ench = true;
               for (ItemStack piece : armor) {
                  if (I1lO0l1I.and(!piece.isEmpty(), !piece.isEnchantable())) ench = false;
               }
               if (ench) score = lO1I0l1O.c(score, C_ONE);
               _s = 3;
               break;

            case 3:
               if (l1O0I1lO.opaqueTrue()) {
                  if (isValidArmorSetInternal(armor)) score = lO1I0l1O.c(score, C_ONE);
               }
               _s = 4;
               break;

            case 4:
               boolean notDamaged = true;
               for (ItemStack piece : armor) {
                  if (piece.isDamaged()) notDamaged = false;
               }
               if (notDamaged) score = lO1I0l1O.c(score, C_ONE);
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
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return new ItemStack[lO1I0l1O.i(4)];
      }
      return new ItemStack[] {
         entity.getInventory().armor.get(C_ZERO),
         entity.getInventory().armor.get(C_ONE),
         entity.getInventory().armor.get(C_TWO),
         entity.getInventory().armor.get(C_THREE)
      };
   }

   private int isValidArmorSetFlag(ItemStack[] armor) {
      l1O0I1lO.fakeHandler();

      ItemStack boots = armor[C_ZERO];
      ItemStack legs = armor[C_ONE];
      ItemStack chest = armor[C_TWO];
      ItemStack helm = armor[C_THREE];

      boolean leather = I1lO0l1I.any(
         boots.getItem() == Items.LEATHER_BOOTS,
         legs.getItem() == Items.LEATHER_LEGGINGS,
         chest.getItem() == Items.LEATHER_CHESTPLATE,
         helm.getItem() == Items.LEATHER_HELMET
      );

      boolean iron = I1lO0l1I.any(
         boots.getItem() == Items.IRON_BOOTS,
         legs.getItem() == Items.IRON_LEGGINGS,
         chest.getItem() == Items.IRON_CHESTPLATE,
         helm.getItem() == Items.IRON_HELMET
      );

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(boots.getItem() == Items.DIAMOND_BOOTS);
      }

      return lO1I0l1O.bool(I1lO0l1I.or(leather, iron));
   }

   private boolean isValidArmorSetInternal(ItemStack[] armor) {
      return lO1I0l1O.unbool(isValidArmorSetFlag(armor));
   }

   private int checkItemsInternal(PlayerEntity entity) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x3A7F, 5);
      int score = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueTrue()) {
                  if (entity.getOffHandStack().isEmpty()) score = lO1I0l1O.c(score, C_ONE);
                  if (!entity.getMainHandStack().isEmpty()) score = lO1I0l1O.c(score, C_ONE);
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  score = lO1I0l1O.c(score, entity.getMainHandStack().getCount());
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeBranch(entity, score);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x2E4B, 5);
      int result = C_ZERO;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               int level = entity.getHungerManager().getFoodLevel();
               _s = 1;
               break;

            case 1:
               int level2 = entity.getHungerManager().getFoodLevel();
               if (l1O0I1lO.opaqueTrue()) {
                  result = I1lO0l1I.equals(level2, lO1I0l1O.i(C_FOOD)) ? C_ONE : C_ZERO;
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.i(entropy % 5);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeBranch(entity, result);
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x1F7A, 5);
      int result = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueTrue()) {
                  result = lO1I0l1O.bool(I1lO0l1I.and(
                     entity instanceof PlayerEntity,
                     this.bots.contains(entity)
                  ));
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.bool(entropy % 2 == 0);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeBranch(entity, result);
               _s = 4;
               break;

            case 4:
               return lO1I0l1O.unbool(result);

            default:
               _s = 4;
               break;
         }
      }
   }

   @Override
   protected void onEnable() {
      l1O0I1lO.fakeHandler();
      super.onEnable();
      this.bots.clear();
      entropy = (int) System.nanoTime();
   }

   @Override
   protected void onDisable() {
      l1O0I1lO.fakeHandler();
      super.onDisable();
      this.bots.clear();
   }
}
