package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.I1lO0l1I;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.registry.entry.RegistryEntry;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "AutoTool",
   desc = "QmxvayBrxLFybWFrIGnDp2luIGVsaW5pemUgZW4gaXlpIGFsZXRpIGFsxLFy",
   type = Type.Player
)
public class AutoTool extends Function {
   public static int itemIndex;
   private boolean swap;
   private long swapDelay;
   private final List<Integer> lastItem = new ArrayList<>();

   // Fake constants
   private static final int FAKE_SLOT = 99;
   private static final float FAKE_SPEED = 100.0f;
   private volatile long entropy = System.nanoTime();

   @Override
   public void onEvent(Event event) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         fakeMode();
         return;
      }

      if (!(event instanceof EventUpdate)) return;
      if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;

      processBlockTarget(result);
   }

   private void processBlockTarget(BlockHitResult result) {
      l1O0I1lO.fakeHandler();

      if (result == null) return;

      BlockPos pos = result.getBlockPos();
      if (pos == null) return;
      if (mc.world.getBlockState(pos).isAir()) return;

      handleToolSwap(pos);
   }

   private void handleToolSwap(BlockPos pos) {
      l1O0I1lO.fakeHandler();

      int toolSlot = getBest(pos);

      if (shouldSwapTool(toolSlot)) {
         performSwap(toolSlot);
      } else if (shouldRestoreSlot()) {
         restoreSlot();
      }

      SemanticNoise.deadCode1();
   }

   private boolean shouldSwapTool(int toolSlot) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entropy > 0;
      }

      return I1lO0l1I.all(
         toolSlot != lO1I0l1O.i(-1),
         mc.options.attackKey.isPressed()
      );
   }

   private void performSwap(int toolSlot) {
      l1O0I1lO.fakeHandler();

      this.lastItem.add(mc.player.getInventory().selectedSlot);

      if (l1O0I1lO.opaqueTrue()) {
         mc.player.getInventory().selectedSlot = toolSlot;
         itemIndex = toolSlot;
      }

      this.swap = true;
      this.swapDelay = System.currentTimeMillis();
   }

   private boolean shouldRestoreSlot() {
      l1O0I1lO.fakeHandler();

      long delay = lO1I0l1O.i(200);

      return I1lO0l1I.all(
         this.swap,
         !this.lastItem.isEmpty(),
         System.currentTimeMillis() >= this.swapDelay + delay
      );
   }

   private void restoreSlot() {
      l1O0I1lO.fakeHandler();

      int slot = this.lastItem.get(lO1I0l1O.i(0));

      if (l1O0I1lO.opaqueTrue()) {
         mc.player.getInventory().selectedSlot = slot;
         itemIndex = slot;
      }

      this.lastItem.clear();
      this.swap = false;
   }

   public static int getBest(BlockPos pos) {
      l1O0I1lO.fakeHandler();

      if (pos == null) return lO1I0l1O.i(-1);

      int index = lO1I0l1O.i(-1);
      float currentFastest = lO1I0l1O.f(1.0F);

      for (int i = 0; i < lO1I0l1O.i(9); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);

         if (isValidTool(stack)) {
            float speed = calculateToolSpeed(stack, pos);
            if (speed > currentFastest) {
               currentFastest = speed;
               index = i;
            }
         }
      }

      if (l1O0I1lO.opaqueFalse()) {
         return FAKE_SLOT;
      }

      return index;
   }

   private static boolean isValidTool(ItemStack stack) {
      l1O0I1lO.fakeHandler();

      if (stack == ItemStack.EMPTY) return false;

      int durability = stack.getMaxDamage() - stack.getDamage();
      return durability > lO1I0l1O.i(10);
   }

   private static float calculateToolSpeed(ItemStack stack, BlockPos pos) {
      l1O0I1lO.fakeHandler();

      float digSpeed = (float)EnchantmentHelper.getLevel(
         (RegistryEntry)mc.world
            .getRegistryManager()
            .getOrThrow(Enchantments.EFFICIENCY.getRegistryRef())
            .getEntry(Enchantments.EFFICIENCY.getValue())
            .get(),
         stack
      );

      float destroySpeed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos));

      if (mc.world.getBlockState(pos).getBlock() instanceof AirBlock) {
         return lO1I0l1O.f(0.0F);
      }

      if (l1O0I1lO.opaqueFalse()) {
         return FAKE_SPEED;
      }

      return digSpeed + destroySpeed;
   }

   private void fakeMode() {
      // Never runs
      entropy ^= System.nanoTime();
      itemIndex = FAKE_SLOT;
      SemanticNoise.deadCode2();
   }
}
