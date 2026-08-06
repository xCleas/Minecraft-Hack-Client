package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.mixin.iface.ClientWorldAccessor;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.player.TimerUtil;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.SemanticNoise;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.component.DataComponentTypes;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoPotion",
   keywords = {"AutoBuff"},
   type = Type.Combat,
   desc = "RWtzaWsgb2xhbiBpa3Npcmxlcmkgb3RvbWF0aWsgb2xhcmFrIGF5YcSfxLFuxLF6xLFuIGFsdMSxbmEgZsSxcmxhdMSxcg=="
)
public class AutoPotion extends Function {
   private final BooleanSetting autoOff = new BooleanSetting(I0O1l0I1.b("T3RvbWF0aWsgS2FwYXRtYQ=="), false);
   public MultiSetting potions = new MultiSetting(I0O1l0I1.b("xLBrc2lybGVy"), Arrays.asList(I0O1l0I1.b("R8O8w6c="), I0O1l0I1.b("SMSxeg=="), I0O1l0I1.b("QXRlxZ8gRGlyZW5jaQ==")), new String[]{I0O1l0I1.b("R8O8w6c="), I0O1l0I1.b("SMSxeg=="), I0O1l0I1.b("QXRlxZ8gRGlyZW5jaQ==")});
   public final TimerUtil timer = new TimerUtil();
   private boolean spoofed = false;
   public boolean isActivePotion;
   private float rotprev;
   private int selectedSlot = -1;
   private final float pose = 90.0F;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private static final long FAKE_DELAY = 9999L;
   private volatile long entropy = System.nanoTime();

   public AutoPotion() {
      this.addSettings(new Setting[]{this.potions, this.autoOff});
   }

   private int isEatingFoodFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(I1lO0l1I.all(
         mc.player.isUsingItem(),
         I1lO0l1I.not(mc.player.getActiveItem().isOf(Items.SHIELD)),
         I1lO0l1I.not(mc.player.getActiveItem().isOf(Items.BOW)),
         I1lO0l1I.not(mc.player.getActiveItem().isOf(Items.TRIDENT))
      ));
   }

   private boolean isEatingFoodInternal() {
      return lO1I0l1O.unbool(isEatingFoodFlag());
   }

   private int findPotionSlotInternal(AutoPotion.PotionType type) {
      l1O0I1lO.fakeHandler();
      for (int i = 0; i < lO1I0l1O.i(9); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() == Items.SPLASH_POTION) {
            Optional<PotionContentsComponent> potionComponent = Optional.ofNullable((PotionContentsComponent)stack.getComponents().get(DataComponentTypes.POTION_CONTENTS));
            if (potionComponent.isPresent()) {
               for (StatusEffectInstance effect : potionComponent.get().getEffects()) {
                  if (effect.getEffectType() == type.effect) {
                     return i;
                  }
               }
            }
         }
      }
      return lO1I0l1O.i(-1);
   }

   private int hasEffectFlag(RegistryEntry<StatusEffect> effect) {
      l1O0I1lO.fakeHandler();
      return lO1I0l1O.bool(mc.player.hasStatusEffect(effect));
   }

   private boolean hasEffectInternal(RegistryEntry<StatusEffect> effect) {
      return lO1I0l1O.unbool(hasEffectFlag(effect));
   }

   private int canBuffTypeFlag(AutoPotion.PotionType type) {
      l1O0I1lO.fakeHandler();
      if (hasEffectInternal(type.effect)) {
         return lO1I0l1O.bool(false);
      }
      return lO1I0l1O.bool(I1lO0l1I.and(type.isEnabled(this), findPotionSlotInternal(type) != lO1I0l1O.i(-1)));
   }

   private boolean canBuffTypeInternal(AutoPotion.PotionType type) {
      return lO1I0l1O.unbool(canBuffTypeFlag(type));
   }

   private int canBuffFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > FAKE_DELAY);
      }
      return lO1I0l1O.bool(I1lO0l1I.all(
         I1lO0l1I.not(isEatingFoodInternal()),
         I1lO0l1I.or(canBuffTypeInternal(PotionType.STRENGTH), canBuffTypeInternal(PotionType.SPEED), canBuffTypeInternal(PotionType.FIRE_RESISTANCE)),
         mc.player.isOnGround(),
         this.timer.hasTimeElapsed(lO1I0l1O.l(500))
      ));
   }

   private boolean canBuffInternal() {
      return lO1I0l1O.unbool(canBuffFlag());
   }

   private int isActiveFlag() {
      l1O0I1lO.fakeHandler();
      return lO1I0l1O.bool(I1lO0l1I.or(
         this.isActivePotion,
         canBuffTypeInternal(PotionType.STRENGTH),
         canBuffTypeInternal(PotionType.SPEED),
         canBuffTypeInternal(PotionType.FIRE_RESISTANCE)
      ));
   }

   private boolean isActiveInternal() {
      return lO1I0l1O.unbool(isActiveFlag());
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventMotion eventAfterRotate) {
                  handleMotionInternal(eventAfterRotate);
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventUpdate) {
                  handleUpdateInternal();
               }
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

   private void handleMotionInternal(EventMotion eventAfterRotate) {
      l1O0I1lO.fakeHandler();
      if (shouldThrowInternal()) {
         this.rotprev = mc.player.getPitch();
         eventAfterRotate.setPitch(lO1I0l1O.f(90.0F));
         this.spoofed = true;
         this.isActivePotion = true;
      }
   }

   private void handleUpdateInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (I1lO0l1I.and(this.isActivePotion, I1lO0l1I.not(shouldThrowInternal()))) {
                  this.isActivePotion = false;
                  if (this.autoOff.get()) {
                     this.toggle();
                  }
               }
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.and(shouldThrowInternal(), this.spoofed)) {
                  executeThrowSequenceInternal();
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(spoofed, entropy);
               }
               _s = 4;
               break;

            case 3:
               SemanticNoise.deadCode1();
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

   private void executeThrowSequenceInternal() {
      l1O0I1lO.fakeHandler();
      throwPotionInternal(PotionType.STRENGTH);
      throwPotionInternal(PotionType.SPEED);
      throwPotionInternal(PotionType.FIRE_RESISTANCE);
      if (l1O0I1lO.opaqueTrue()) {
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
         mc.player.setPitch(this.rotprev);
      }
      this.timer.reset();
      this.spoofed = false;
      this.isActivePotion = false;
      if (this.autoOff.get()) {
         this.toggle();
      }
   }

   private int shouldThrowFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > lO1I0l1O.l(0));
      }
      return lO1I0l1O.bool(I1lO0l1I.all(
         isActiveInternal(),
         canBuffInternal(),
         mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock() != Blocks.AIR
      ));
   }

   private boolean shouldThrowInternal() {
      return lO1I0l1O.unbool(shouldThrowFlag());
   }

   private void throwPotionInternal(AutoPotion.PotionType type) {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(type.isEnabled(this), I1lO0l1I.not(hasEffectInternal(type.effect)))) {
         int slot = findPotionSlotInternal(type);
         if (slot != lO1I0l1O.i(-1)) {
            this.selectedSlot = mc.player.getInventory().selectedSlot;
            if (l1O0I1lO.opaqueTrue()) {
               mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            }
            float yaw = mc.player.getYaw();
            float pitch = mc.player.getPitch();
            this.sendSequencedPacketInternal(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id, yaw, pitch));
         }
      }
   }

   private void sendSequencedPacketInternal(SequencedPacketCreator packetCreator) {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.or(mc.player.networkHandler == null, mc.world == null)) return;

      PendingUpdateManager pendingUpdateManager = ((ClientWorldAccessor)mc.world).getPendingUpdateManager().incrementSequence();
      try {
         int sequence = pendingUpdateManager.getSequence();
         if (l1O0I1lO.opaqueTrue()) {
            mc.player.networkHandler.sendPacket(packetCreator.predict(sequence));
         }
      } catch (Throwable var6) {
         if (pendingUpdateManager != null) {
            try {
               pendingUpdateManager.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }
         throw var6;
      }
      if (pendingUpdateManager != null) {
         pendingUpdateManager.close();
      }
   }

   private static enum PotionType {
      STRENGTH(5, StatusEffects.STRENGTH, I0O1l0I1.b("R8O8w6c=")),
      SPEED(1, StatusEffects.SPEED, I0O1l0I1.b("SMSxeg==")),
      FIRE_RESISTANCE(12, StatusEffects.FIRE_RESISTANCE, I0O1l0I1.b("QXRlxZ8gRGlyZW5jaQ=="));

      final int id;
      final RegistryEntry<StatusEffect> effect;
      final String settingName;

      private PotionType(int id, RegistryEntry<StatusEffect> effect, String settingName) {
         this.id = id;
         this.effect = effect;
         this.settingName = settingName;
      }

      public boolean isEnabled(AutoPotion module) {
         return module.potions.get(this.settingName);
      }
   }
}
