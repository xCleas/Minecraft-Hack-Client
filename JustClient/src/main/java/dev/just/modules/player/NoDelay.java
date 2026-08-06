package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.mixin.iface.MinecraftClientAccessor;
import dev.just.mixin.iface.MixinLivingEntityAccessor;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

@FunctionAnnotation(
   name = "NoDelay",
   desc = "E\u015fya ve eylem bekleme s\u00fcrelerini kald\u0131r\u0131r",
   type = Type.Player
)
public class NoDelay extends Function {
   private final BooleanSetting jump = new BooleanSetting("Z\u0131plama", true);
   private final BooleanSetting xp = new BooleanSetting("Tecr\u00fcbe \u0130ksiri", true);
   private final BooleanSetting crystal = new BooleanSetting("Kristal", true);
   private final BooleanSetting place = new BooleanSetting("Sa\u011f T\u0131k (Blok)", false);

   private static final int FAKE_COOLDOWN = 999;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public NoDelay() {
      this.addSettings(new Setting[]{this.jump, this.xp, this.crystal, this.place});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  fakeModeInternal();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventUpdate)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               processDelaysInternal();
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
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

   private void processDelaysInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               handleJumpDelayInternal();
               _s = 1;
               break;

            case 1:
               handleItemDelayInternal();
               _s = 2;
               break;

            case 2:
               SemanticNoise.deadCode1();
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

   private void handleJumpDelayInternal() {
      l1O0I1lO.fakeHandler();
      if (!this.jump.get()) return;
      if (l1O0I1lO.opaqueTrue()) {
         ((MixinLivingEntityAccessor)mc.player).setLastJumpCooldown(lO1I0l1O.i(0));
      }
   }

   private void handleItemDelayInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 5);
      Item item = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               item = mc.player.getMainHandStack().getItem();
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  ((MinecraftClientAccessor)mc).setUseCooldown(lO1I0l1O.i(FAKE_COOLDOWN));
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (checkItemInternal(item)) {
                  if (l1O0I1lO.opaqueTrue()) {
                     ((MinecraftClientAccessor)mc).setUseCooldown(lO1I0l1O.i(0));
                  }
               }
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(item, entropy);
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

   private int checkItemFlag(Item item) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      boolean result = I1lO0l1I.or(
         checkBlockItemInternal(item),
         checkCrystalInternal(item),
         checkXpBottleInternal(item)
      );
      return lO1I0l1O.bool(result);
   }

   private boolean checkItemInternal(Item item) {
      return lO1I0l1O.unbool(checkItemFlag(item));
   }

   private boolean checkBlockItemInternal(Item item) {
      l1O0I1lO.fakeHandler();
      return I1lO0l1I.all(item instanceof BlockItem, this.place.get());
   }

   private boolean checkCrystalInternal(Item item) {
      l1O0I1lO.fakeHandler();
      return I1lO0l1I.all(item == Items.END_CRYSTAL, this.crystal.get());
   }

   private boolean checkXpBottleInternal(Item item) {
      l1O0I1lO.fakeHandler();
      return I1lO0l1I.all(item == Items.EXPERIENCE_BOTTLE, this.xp.get());
   }

   private void fakeModeInternal() {
      l1O0I1lO.fakeHandler();
      entropy ^= System.nanoTime();
      ((MinecraftClientAccessor)mc).setUseCooldown(FAKE_COOLDOWN);
      SemanticNoise.deadCode2();
   }
}
