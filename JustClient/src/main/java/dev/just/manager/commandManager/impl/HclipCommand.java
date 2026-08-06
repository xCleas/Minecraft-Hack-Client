package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.I0O1l0I1;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class HclipCommand extends Command {
   public HclipCommand() {
      super("hclip");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(arg("offset", DoubleArgumentType.doubleArg()).executes(context -> {
         double offset = DoubleArgumentType.getDouble(context, "offset");
         this.teleport(offset);
         return 1;
      }));
   }

   private void teleport(double offset) {
      if (offset == 0.0) {
         ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.WHITE + "Mesafe 0 olamaz.");
      } else {
         Vec3d look = mc.player.getRotationVec(1.0F).normalize();
         double x = mc.player.getX() + look.x * offset;
         double z = mc.player.getZ() + look.z * offset;
         double y = mc.player.getY();

         for (int i = 0; i < 19; i++) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), y, mc.player.getZ(), false, true));
         }

         for (int i = 0; i < 19; i++) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, true));
         }

         mc.player.setPosition(x, y, z);
         ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.WHITE + I0O1l0I1.b("xLBsZXJpIA==") + Formatting.GOLD + (int)offset + Formatting.WHITE + I0O1l0I1.b("IGJsb2sgxLHFn8SxbmxhbmTEsW4u"));
      }
   }
}
