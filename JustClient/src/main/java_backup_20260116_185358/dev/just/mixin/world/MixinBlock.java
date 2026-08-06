package dev.just.mixin.world;

import dev.just.events.Event;
import dev.just.events.impl.world.EventObsidianPlace;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Block.class})
public class MixinBlock {
   @Inject(
      method = {"onPlaced"},
      at = {@At("HEAD")}
   )
   private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
      if (state.getBlock() == Blocks.OBSIDIAN) {
         Event.call(new EventObsidianPlace(state.getBlock(), pos));
      }
   }
}
