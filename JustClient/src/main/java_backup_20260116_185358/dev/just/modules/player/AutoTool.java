package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.LogicSplit;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.registry.entry.RegistryEntry;
import dev.just.protect.runtime.Strings;

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
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         fakeMode();
         return;
      }

      if (!(event instanceof EventUpdate)) return;
      if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;

      processBlockTarget(result);
   }

   private void processBlockTarget(BlockHitResult result) {
      FlowObfuscator.fakeHandler();

      if (result == null) return;

      BlockPos pos = result.getBlockPos();
      if (pos == null) return;
      if (mc.world.getBlockState(pos).isAir()) return;

      handleToolSwap(pos);
   }

   private void handleToolSwap(BlockPos pos) {
      FlowObfuscator.fakeHandler();

      int toolSlot = getBest(pos);

      if (shouldSwapTool(toolSlot)) {
         performSwap(toolSlot);
      } else if (shouldRestoreSlot()) {
         restoreSlot();
      }

      SemanticNoise.deadCode1();
   }

   private boolean shouldSwapTool(int toolSlot) {
      FlowObfuscator.fakeHandler();

      if (FlowObfuscator.opaqueFalse()) {
         return entropy > 0;
      }

      return LogicSplit.all(
         toolSlot != NumberGuard.i(-1),
         mc.options.attackKey.isPressed()
      );
   }

   private void performSwap(int toolSlot) {
      FlowObfuscator.fakeHandler();

      this.lastItem.add(mc.player.getInventory().selectedSlot);

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.getInventory().selectedSlot = toolSlot;
         itemIndex = toolSlot;
      }

      this.swap = true;
      this.swapDelay = System.currentTimeMillis();
   }

   private boolean shouldRestoreSlot() {
      FlowObfuscator.fakeHandler();

      long delay = NumberGuard.i(200);

      return LogicSplit.all(
         this.swap,
         !this.lastItem.isEmpty(),
         System.currentTimeMillis() >= this.swapDelay + delay
      );
   }

   private void restoreSlot() {
      FlowObfuscator.fakeHandler();

      int slot = this.lastItem.get(NumberGuard.i(0));

      if (FlowObfuscator.opaqueTrue()) {
         mc.player.getInventory().selectedSlot = slot;
         itemIndex = slot;
      }

      this.lastItem.clear();
      this.swap = false;
   }

   public static int getBest(BlockPos pos) {
      FlowObfuscator.fakeHandler();

      if (pos == null) return NumberGuard.i(-1);

      int index = NumberGuard.i(-1);
      float currentFastest = NumberGuard.f(1.0F);

      for (int i = 0; i < NumberGuard.i(9); i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);

         if (isValidTool(stack)) {
            float speed = calculateToolSpeed(stack, pos);
            if (speed > currentFastest) {
               currentFastest = speed;
               index = i;
            }
         }
      }

      if (FlowObfuscator.opaqueFalse()) {
         return FAKE_SLOT;
      }

      return index;
   }

   private static boolean isValidTool(ItemStack stack) {
      FlowObfuscator.fakeHandler();

      if (stack == ItemStack.EMPTY) return false;

      int durability = stack.getMaxDamage() - stack.getDamage();
      return durability > NumberGuard.i(10);
   }

   private static float calculateToolSpeed(ItemStack stack, BlockPos pos) {
      FlowObfuscator.fakeHandler();

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
         return NumberGuard.f(0.0F);
      }

      if (FlowObfuscator.opaqueFalse()) {
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
