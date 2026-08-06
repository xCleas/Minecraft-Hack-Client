package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.commandManager.Command;
import dev.just.manager.fontManager.FontUtils;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector2f;

public class GpsCommand extends Command {
   private static boolean enabled;
   private static BlockPos GPS_POSITION;
   private static float smoothYaw = 0.0F;

   public GpsCommand() {
      super("gps");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      RequiredArgumentBuilder<CommandSource, Integer> xArg = arg("x", IntegerArgumentType.integer());
      RequiredArgumentBuilder<CommandSource, Integer> zArg = arg("z", IntegerArgumentType.integer());
      zArg.executes(context -> {
         int x = IntegerArgumentType.getInteger(context, "x");
         int z = IntegerArgumentType.getInteger(context, "z");
         setGps(x, z);
         return 1;
      });
      xArg.then(zArg);
      builder.then(xArg);
      builder.then(literal("set").then(arg("x", IntegerArgumentType.integer()).then(arg("z", IntegerArgumentType.integer()).executes(context -> {
         int x = IntegerArgumentType.getInteger(context, "x");
         int z = IntegerArgumentType.getInteger(context, "z");
         setGps(x, z);
         return 1;
      }))));
      builder.then(literal("off").executes(context -> {
         enabled = false;
         GPS_POSITION = null;
         ClientManager.message(Formatting.GRAY + I0O1l0I1.b("R1BTIGthcGF0xLFsZMSxIQ=="));
         return 1;
      }));
   }

   private static void setGps(int x, int z) {
      GPS_POSITION = new BlockPos(x, (int)mc.player.getY(), z);
      enabled = true;
      ClientManager.message(Formatting.GRAY + I0O1l0I1.b("R1BTIGF5YXJsYW5kxLE6IFg6IA==") + x + ", Y: " + (int)mc.player.getY() + ", Z: " + z);
   }

   private static int getDistance(BlockPos pos) {
      double dx = mc.player.getX() - (double)pos.getX();
      double dz = mc.player.getZ() - (double)pos.getZ();
      return (int)MathHelper.sqrt((float)(dx * dx + dz * dz));
   }

   private static float getYaw(Vector2f target) {
      double dx = (double)target.x - mc.player.getX();
      double dz = (double)target.y - mc.player.getZ();
      return (float)(-(Math.atan2(dx, dz) * (180.0 / Math.PI)));
   }

   public static void render(MatrixStack matrixStack) {
      if (enabled && GPS_POSITION != null && mc.player != null) {
         float screenWidth = (float)mc.getWindow().getScaledWidth() / 2.0F;
         float screenHeight = (float)mc.getWindow().getScaledHeight() / 2.0F;
         float targetYaw = getYaw(new Vector2f((float)GPS_POSITION.getX(), (float)GPS_POSITION.getZ())) - mc.player.getYaw();
         targetYaw = MathHelper.wrapDegrees(targetYaw);
         float maxStep = 4.0F;
         float delta = MathHelper.wrapDegrees(targetYaw - smoothYaw);
         smoothYaw = smoothYaw + MathHelper.clamp(delta, -maxStep, maxStep);
         int distance = getDistance(GPS_POSITION);
         if (distance < 1) {
            FontUtils.durman[15].centeredDraw(matrixStack, I0O1l0I1.b("SGVkZWZlIHVsYcWfdMSxbsSxeiE="), screenWidth, screenHeight - 105.0F, -1);
         } else {
            matrixStack.push();
            matrixStack.translate(screenWidth, screenHeight - 120.0F, 0.0F);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(smoothYaw));
            RenderUtil.drawTexture(matrixStack, "images/triangle2.png", -12.0F, -12.0F, 25.0F, 25.0F, 0.0F, Color.white.getRGB());
            matrixStack.pop();
            FontUtils.durman[15].centeredDraw(matrixStack, distance + "m", screenWidth, screenHeight - 105.0F, -1);
         }
      }
   }
}
