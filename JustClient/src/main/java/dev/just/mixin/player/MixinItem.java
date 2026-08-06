package dev.just.mixin.player;

import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.modules.player.CustomCoolDown;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Item.class})
public class MixinItem {
   @Inject(
      method = {"use"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
      if (user instanceof ClientPlayerEntity && Manager.FUNCTION_MANAGER != null) {
         CustomCoolDown coolDown = Manager.FUNCTION_MANAGER.customCoolDown;
         if (coolDown != null && coolDown.state) {
            if (!coolDown.PVPonly.get() || ClientManager.playerIsPVP()) {
               ItemStack stack = user.getStackInHand(hand);
               if (coolDown.isItemEnabled(stack.getItem()) && coolDown.lastUseMap.containsKey(stack.getItem())) {
                  cir.setReturnValue(ActionResult.FAIL);
                  cir.cancel();
               }
            }
         }
      }
   }

   @Inject(
      method = {"finishUsing"},
      at = {@At("RETURN")}
   )
   public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
      if (user instanceof ClientPlayerEntity player && Manager.FUNCTION_MANAGER != null) {
         CustomCoolDown coolDown = Manager.FUNCTION_MANAGER.customCoolDown;
         if (coolDown != null && coolDown.state) {
            if (!coolDown.PVPonly.get() || ClientManager.playerIsPVP()) {
               if (coolDown.isItemEnabled(stack.getItem())) {
                  coolDown.setCooldown(stack.getItem());
                  int cooldownTicks = (int)(coolDown.getCooldownForItem(stack.getItem()) * 20.0F);
                  player.getItemCooldownManager().set(stack, cooldownTicks);
               }
            }
         }
      }
   }

   @Inject(
      method = {"getMaxUseTime"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
      if (user instanceof ClientPlayerEntity && Manager.FUNCTION_MANAGER != null) {
         CustomCoolDown coolDown = Manager.FUNCTION_MANAGER.customCoolDown;
         if (coolDown != null && coolDown.state) {
            if (!coolDown.PVPonly.get() || ClientManager.playerIsPVP()) {
               if (coolDown.isItemEnabled(stack.getItem()) && coolDown.lastUseMap.containsKey(stack.getItem())) {
                  cir.setReturnValue(0);
                  cir.cancel();
               }
            }
         }
      }
   }

   @Inject(
      method = {"isUsedOnRelease"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isUsedOnRelease(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
      if (Manager.FUNCTION_MANAGER == null) return;
      CustomCoolDown coolDown = Manager.FUNCTION_MANAGER.customCoolDown;
      if (coolDown != null && coolDown.state) {
         if (!coolDown.PVPonly.get() || ClientManager.playerIsPVP()) {
            if (coolDown.isItemEnabled(stack.getItem()) && coolDown.lastUseMap.containsKey(stack.getItem())) {
               cir.setReturnValue(false);
               cir.cancel();
            }
         }
      }
   }
}
