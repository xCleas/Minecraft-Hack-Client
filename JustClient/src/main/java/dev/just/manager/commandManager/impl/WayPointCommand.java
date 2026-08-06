package dev.just.manager.commandManager.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.manager.fontManager.FontUtils;
import dev.just.util.render.RenderUtil;
import dev.just.util.vector.VectorUtil;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3d;

public class WayPointCommand extends Command {
   private final Path FILE = Paths.get(mc.runDirectory.getAbsolutePath(), "files\\modules", "waypoints.ew");
   private final Gson GSON = new Gson();
   private final Type TYPE = (new TypeToken<Map<String, BlockPos>>() {
   }).getType();
   private static Map<String, BlockPos> waypoints = new ConcurrentHashMap<>();

   public WayPointCommand() {
      super("way");
      this.load();
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         literal("add")
            .then(
               ((RequiredArgumentBuilder)arg("name", StringArgumentType.word()).executes(ctx -> {
                     BlockPos pos = mc.player.getBlockPos();
                     String name = StringArgumentType.getString(ctx, "name");
                     waypoints.put(name, pos);
                     this.save();
                     ClientManager.message(I0O1l0I1.b("wqdhQmHFn2FyxLFsxLEhIMKnZg==") + name + I0O1l0I1.b("IMKnYW5va3Rhc8SxIGtheWRlZGlsZGk6IA==") + pos.toShortString());
                     return 1;
                  }))
                  .then(
                     arg("x", IntegerArgumentType.integer())
                        .then(arg("y", IntegerArgumentType.integer()).then(arg("z", IntegerArgumentType.integer()).executes(ctx -> {
                           String name = StringArgumentType.getString(ctx, "name");
                           int x = IntegerArgumentType.getInteger(ctx, "x");
                           int y = IntegerArgumentType.getInteger(ctx, "y");
                           int z = IntegerArgumentType.getInteger(ctx, "z");
                           BlockPos pos = new BlockPos(x, y, z);
                           waypoints.put(name, pos);
                           this.save();
                           ClientManager.message(I0O1l0I1.b("wqdhQmHFn2FyxLFsxLEhIMKnZg==") + name + I0O1l0I1.b("IMKnYW5va3Rhc8SxIGtheWRlZGlsZGk6IA==") + pos.toShortString());
                           return 1;
                        })))
                  )
            )
      );
      builder.then(literal("remove").then(arg("name", StringArgumentType.word()).suggests((ctx, suggestionsBuilder) -> {
         waypoints.keySet().forEach(suggestionsBuilder::suggest);
         return suggestionsBuilder.buildFuture();
      }).executes(ctx -> {
         String name = StringArgumentType.getString(ctx, "name");
         if (waypoints.remove(name) != null) {
            this.save();
            ClientManager.message(I0O1l0I1.b("wqdhQmHFn2FyxLFsxLEhIMKnZg==") + name + I0O1l0I1.b("IMKnYW5va3Rhc8SxIHNpbGluZGku"));
         } else {
            ClientManager.message(I0O1l0I1.b("wqdjSGF0YSEgwqdmQnUgaXNpbWRlIG5va3RhIGJ1bHVuYW1hZMSxLg=="));
         }

         return 1;
      })));
      builder.then(literal("list").executes(ctx -> {
         if (waypoints.isEmpty()) {
            ClientManager.message(I0O1l0I1.b("wqc3S2F5xLF0bMSxIG5va3RhIHlvay4="));
         } else {
            ClientManager.message("§aNokta Listesi:");
            waypoints.forEach((n, pos) -> ClientManager.message("§f" + n + " §7→ " + pos.toShortString()));
         }

         return 1;
      }));
   }

   public static void render(MatrixStack matrices) {
      if (!waypoints.isEmpty()) {
         Camera camera = mc.gameRenderer.getCamera();
         Vec3d camPos = camera.getPos();
         int sw = mc.getWindow().getScaledWidth();
         int sh = mc.getWindow().getScaledHeight();
         waypoints.forEach(
            (name, pos) -> {
               Vector3d sp = VectorUtil.toScreen((double)pos.getX() + 0.5, (double)pos.getY() + 1.5, (double)pos.getZ() + 0.5);
               if (!(sp.z() <= 0.0) && !(sp.x() < 0.0) && !(sp.x() > (double)sw) && !(sp.y() < 0.0) && !(sp.y() > (double)sh)) {
                  int dist = (int)camPos.distanceTo(Vec3d.of(pos));
                  String text = name + " [" + dist + "m]";
                  float tw = FontUtils.durman[15].getWidth(text);
                  float th = FontUtils.durman[15].getHeight();
                  float px = 4.0F;
                  float py = 1.5F;
                  RenderUtil.drawRoundedRect(
                     matrices,
                     (float)sp.x() - tw / 2.0F - px,
                     (float)sp.y() - th / 2.0F - py,
                     tw + px * 2.0F,
                     th + py * 2.0F,
                     1.5F,
                     new Color(30, 30, 30, 150).getRGB()
                  );
                  FontUtils.durman[15].centeredDraw(matrices, text, (float)sp.x(), (float)sp.y() - th / 2.0F - 0.5F, Color.WHITE.getRGB());
               }
            }
         );
      }
   }

   private void save() {
      try {
         Files.createDirectories(this.FILE.getParent());
         Files.writeString(
            this.FILE, this.GSON.toJson(waypoints, this.TYPE), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
         );
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   private void load() {
      if (Files.exists(this.FILE)) {
         try {
            String json = Files.readString(this.FILE, StandardCharsets.UTF_8);
            waypoints = (Map<String, BlockPos>)this.GSON.fromJson(json, this.TYPE);
            if (waypoints == null) {
               waypoints = new ConcurrentHashMap<>();
            }
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }
}
