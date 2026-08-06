package dev.just.util.player;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class InventoryUtil implements IMinecraft {
   public static void moveItemTest(int from, int to, boolean air) {
      if (from != to) {
         testPick(from, 0);
         testPick(to, 0);
         if (air) {
            testPick(from, 0);
         }
      }
   }

   public static void testPick(int slot, int button) {
      mc.interactionManager.clickSlot(0, slot, button, SlotActionType.PICKUP, mc.player);
   }

   public static void moveToOffhand(Item item) {
      if (mc.player != null && mc.interactionManager != null) {
         PlayerInventory inventory = mc.player.getInventory();
         if (((ItemStack)inventory.offHand.get(0)).getItem() != item) {
            for (int i = 0; i < 9; i++) {
               if (inventory.getStack(i).getItem() == item) {
                  mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i + 36, 40, SlotActionType.SWAP, mc.player);
                  return;
               }
            }

            for (int ix = 9; ix < 36; ix++) {
               if (inventory.getStack(ix).getItem() == item) {
                  mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, ix, 40, SlotActionType.SWAP, mc.player);
                  return;
               }
            }
         }
      }
   }

   public static int getItem(Class<?> itemClass, boolean hotBarOnly) {
      PlayerInventory inventory = mc.player.getInventory();
      int startSlot = hotBarOnly ? 0 : 9;
      int endSlot = hotBarOnly ? 9 : inventory.size();

      for (int i = startSlot; i < endSlot; i++) {
         if (i < inventory.size()) {
            ItemStack itemStack = inventory.getStack(i);
            if (!itemStack.isEmpty() && itemClass.isInstance(itemStack.getItem())) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int getPearls() {
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getStack(i).getItem().asItem() instanceof EnderPearlItem) {
            return i;
         }
      }

      return -1;
   }

   public static void swapSlotsUniversal(int slot1, int slot2, boolean cursor, boolean conversion) {
      if (cursor) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot1, 0, SlotActionType.PICKUP, mc.player);
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot2, 0, SlotActionType.PICKUP, mc.player);
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot1, 0, SlotActionType.PICKUP, mc.player);
      } else if (slot1 < 9 && conversion) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot1 + 36, slot2, SlotActionType.SWAP, mc.player);
      } else {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot1, slot2, SlotActionType.SWAP, mc.player);
      }
   }

   public static void swapSlots(int slot1, int slot2) {
      if (slot1 < 9 && slot2 < 9) {
         mc.interactionManager.clickSlot(0, slot1 + 36, slot2, SlotActionType.SWAP, mc.player);
      } else if (slot1 < 9) {
         mc.interactionManager.clickSlot(0, slot2, slot1, SlotActionType.SWAP, mc.player);
      } else if (slot2 < 9) {
         mc.interactionManager.clickSlot(0, slot1, slot2, SlotActionType.SWAP, mc.player);
      }
   }

   public static SearchInvResult getAxe() {
      if (mc.player == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for (int b1 = 9; b1 < 45; b1++) {
            ItemStack itemStack = mc.player.getInventory().getStack(b1 >= 36 ? b1 - 36 : b1);
            if (itemStack != null && itemStack.getItem() instanceof AxeItem axe) {
               slot = b1;
            }
         }

         if (slot >= 36) {
            slot -= 36;
         }

         return slot == -1 ? SearchInvResult.notFound() : new SearchInvResult(slot, true, mc.player.getInventory().getStack(slot));
      }
   }

   public static void moveItem(int from, int to, boolean air) {
      if (from != to) {
         pickupItem(from, 0);
         pickupItem(to, 0);
         if (air) {
            pickupItem(from, 0);
         }
      }
   }

   public static void pickupItem(int slot, int button) {
      mc.interactionManager.clickSlot(0, slot, button, SlotActionType.PICKUP, mc.player);
   }

   public static int getItemSlot(Item input) {
      for (ItemStack stack : Manager.SYNC_MANAGER.getItems()) {
         if (stack.getItem() == input) {
            return -2;
         }
      }

      int slot = -1;

      for (int i = 0; i < 36; i++) {
         ItemStack s = mc.player.getInventory().getStack(i);
         if (s.getItem() == input) {
            slot = i;
            break;
         }
      }

      if (slot < 9 && slot != -1) {
         slot += 36;
      }

      return slot;
   }

   public static int getHotBarSlot(Item input) {
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getStack(i).getItem() == input) {
            return i;
         }
      }

      return -1;
   }

   public static boolean doesHotbarHaveItem(Item item) {
      for (int i = 0; i < 9; i++) {
         mc.player.getInventory().getStack(i);
         if (mc.player.getInventory().getStack(i).getItem() == item) {
            return true;
         }
      }

      return false;
   }

   public static int getItemIndex(Item item) {
      for (int i = 0; i < 45; i++) {
         if (mc.player.getInventory().getStack(i).getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   public static void inventorySwapClick2(Item item, boolean useFromInventory, boolean rotation) {
      int currentSlot = mc.player.getInventory().selectedSlot;
      if (mc.player.isUsingItem()
         && !mc.player.getActiveItem().isOf(Items.SHIELD)
         && mc.player.getActiveHand() == Hand.MAIN_HAND) {
         for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
               swapSlotsUniversal(i, 40, false, true);
               if (rotation && Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
                  mc.player
                     .networkHandler
                     .sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround(), mc.player.horizontalCollision));
               }

               mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.OFF_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
               swapSlotsUniversal(i, 40, false, true);
               return;
            }
         }
      } else {
         for (int ix = 0; ix < 9; ix++) {
            if (mc.player.getInventory().getStack(ix).getItem() == item) {
               if (ix != currentSlot) {
                  mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(ix));
               }

               if (rotation && Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
                  mc.player
                     .networkHandler
                     .sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround(), mc.player.horizontalCollision));
               }

               mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
               if (ix != currentSlot) {
                  mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
               }

               return;
            }
         }

         if (useFromInventory) {
            for (int ixx = 9; ixx < 36; ixx++) {
               if (mc.player.getInventory().getStack(ixx).getItem() == item) {
                  int nextSlot = (currentSlot + 1) % 9;
                  swapSlotsUniversal(ixx, nextSlot, false, true);
                  mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(nextSlot));
                  if (rotation && Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
                     mc.player
                        .networkHandler
                        .sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround(), mc.player.horizontalCollision));
                  }

                  mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                  mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                  swapSlotsUniversal(ixx, nextSlot, false, true);
                  return;
               }
            }
         }
      }
   }

   public static void windowClick(int conteinerId, int slot, int mouse, SlotActionType type, PlayerEntity player) {
      mc.interactionManager.clickSlot(conteinerId, slot, mouse, type, player);
   }

   public static void startFly() {
      mc.player.startGliding();
      mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
   }

   public static void use(int hotbarSlot, int inventorySlot, boolean useFromInventory) {
      int currentItem = mc.player.getInventory().selectedSlot;
      if (hotbarSlot != -1) {
         mc.player.getInventory().selectedSlot = hotbarSlot;
         mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
         mc.player.getInventory().selectedSlot = currentItem;
      } else if (useFromInventory && inventorySlot != -1) {
         windowClick(0, inventorySlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
         mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
         windowClick(0, inventorySlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
         mc.player.getInventory().updateItems();
      }
   }

   public static class TotemUtil {
      public static BlockPos getBlock(float distance, Block block) {
         return getSphere(getPlayerPosLocal(), distance, 6, false, true, 0)
            .stream()
            .filter(position -> IMinecraft.mc.world.getBlockState(position).getBlock() == block)
            .min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(IMinecraft.mc.player, blockPos)))
            .orElse(null);
      }

      public static BlockPos getBlock(float distance) {
         return getSphere(getPlayerPosLocal(), distance, 6, false, true, 0)
            .stream()
            .filter(position -> IMinecraft.mc.world.getBlockState(position).getBlock() != Blocks.AIR)
            .min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(IMinecraft.mc.player, blockPos)))
            .orElse(null);
      }

      public static List<BlockPos> getSphere(BlockPos blockPos, float n, int n2, boolean b, boolean b2, int n3) {
         ArrayList<BlockPos> list = new ArrayList<>();
         int x = blockPos.getX();
         int y = blockPos.getY();
         int z = blockPos.getZ();

         for (int n4 = x - (int)n; (float)n4 <= (float)x + n; n4++) {
            for (int n5 = z - (int)n; (float)n5 <= (float)z + n; n5++) {
               for (int n6 = b2 ? y - (int)n : y; (float)n6 < (b2 ? (float)y + n : (float)(y + n2)); n6++) {
                  double n7 = (double)((x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? (y - n6) * (y - n6) : 0));
                  if (n7 < (double)(n * n) && (!b || n7 >= (double)((n - 1.0F) * (n - 1.0F)))) {
                     list.add(new BlockPos(n4, n6 + n3, n5));
                  }
               }
            }
         }

         return list;
      }

      public static BlockPos getPlayerPosLocal() {
         return IMinecraft.mc.player == null
            ? (BlockPos)BlockPos.ZERO
            : new BlockPos(
               (int)Math.floor(IMinecraft.mc.player.getX()),
               (int)Math.floor(IMinecraft.mc.player.getY()),
               (int)Math.floor(IMinecraft.mc.player.getZ())
            );
      }

      public static double getDistanceOfEntityToBlock(Entity entity, BlockPos blockPos) {
         return getDistance(
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            (double)blockPos.getX(),
            (double)blockPos.getY(),
            (double)blockPos.getZ()
         );
      }

      public static double getDistance(double n, double n2, double n3, double n4, double n5, double n6) {
         double n7 = n - n4;
         double n8 = n2 - n5;
         double n9 = n3 - n6;
         return (double)MathHelper.sqrt((float)(n7 * n7 + n8 * n8 + n9 * n9));
      }
   }
}
