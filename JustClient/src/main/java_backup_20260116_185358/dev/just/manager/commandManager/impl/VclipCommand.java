package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.commandManager.Command;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.just.protect.runtime.Strings;

public class VclipCommand extends Command {
   public VclipCommand() {
      super("vclip");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("up").executes(context -> {
         double offset = this.findFreeSpace(true);
         this.teleport(offset);
         return 1;
      }));
      builder.then(literal("down").executes(context -> {
         double offset = this.findFreeSpace(false);
         this.teleport(offset);
         return 1;
      }));
      builder.then(arg("offset", DoubleArgumentType.doubleArg()).executes(context -> {
         double offset = DoubleArgumentType.getDouble(context, "offset");
         this.teleport(offset);
         return 1;
      }));
   }

   private void teleport(double offset) {
      if (offset == 0.0) {
         ClientManager.message(Formatting.RED + Strings.b("SGF0YSEg") + Formatting.WHITE + Strings.b("VXlndW4gYm/FnyBhbGFuIGJ1bHVuYW1hZMSxLg=="));
      } else {
         double startY = mc.player.getY();
         double endY = startY + offset;

         for (int i = 0; i < 19; i++) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), startY, mc.player.getZ(), false, true));
         }

         for (int i = 0; i < 19; i++) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), endY, mc.player.getZ(), false, true));
         }

         mc.player.setPosition(mc.player.getX(), endY, mc.player.getZ());
         ClientManager.message(
            Formatting.GREEN
               + Strings.b("QmHFn2FyxLFsxLEhIA==")
               + Formatting.WHITE
               + (offset > 0.0 ? Strings.b("WXVrYXLEsXlh") : Strings.b("QcWfYcSfxLF5YQ=="))
               + " "
               + Formatting.GOLD
               + (int)Math.abs(offset)
               + Formatting.WHITE
               + Strings.b("IGJsb2sgxLHFn8SxbmxhbmTEsW4u")
         );
      }
   }

   private double findFreeSpace(boolean up) {
      BlockPos start = mc.player.getBlockPos();
      int step = up ? 1 : -1;

      for (int i = 1; i < 256; i++) {
         BlockPos checkPos = start.add(0, i * step, 0);
         BlockPos checkPosAbove = checkPos.up();
         if (mc.world.isAir(checkPos) && mc.world.isAir(checkPosAbove)) {
            return (double)(i * step);
         }
      }

      return 0.0;
   }
}
