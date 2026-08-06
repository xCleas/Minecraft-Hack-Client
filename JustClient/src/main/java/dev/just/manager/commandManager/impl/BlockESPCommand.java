package dev.just.manager.commandManager.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.Command;
import dev.just.protect.runtime.I0O1l0I1;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.util.Formatting;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;

public class BlockESPCommand extends Command {
   private static final Map<String, Color> COLOR_NAMES = Map.ofEntries(
      Map.entry("white", Color.WHITE),
      Map.entry("black", Color.BLACK),
      Map.entry("red", Color.RED),
      Map.entry("green", Color.GREEN),
      Map.entry("blue", Color.BLUE),
      Map.entry("yellow", Color.YELLOW),
      Map.entry("cyan", Color.CYAN),
      Map.entry("magenta", Color.MAGENTA),
      Map.entry("gray", Color.GRAY),
      Map.entry("darkgray", Color.DARK_GRAY),
      Map.entry("lightgray", Color.LIGHT_GRAY),
      Map.entry("orange", Color.ORANGE),
      Map.entry("pink", Color.PINK),
      Map.entry("белый", Color.WHITE),
      Map.entry("чёрный", Color.BLACK),
      Map.entry("черный", Color.BLACK),
      Map.entry("красный", Color.RED),
      Map.entry("зелёный", Color.GREEN),
      Map.entry("зеленый", Color.GREEN),
      Map.entry("синий", Color.BLUE),
      Map.entry("жёлтый", Color.YELLOW),
      Map.entry("желтый", Color.YELLOW),
      Map.entry("голубой", Color.CYAN),
      Map.entry("пурпурный", Color.MAGENTA),
      Map.entry("серый", Color.GRAY),
      Map.entry("тёмно-серый", Color.DARK_GRAY),
      Map.entry("темно-серый", Color.DARK_GRAY),
      Map.entry("светло-серый", Color.LIGHT_GRAY),
      Map.entry("оранжевый", Color.ORANGE),
      Map.entry("розовый", Color.PINK)
   );

   public BlockESPCommand() {
      super("blockesp");
   }

   @Override
   public void execute(LiteralArgumentBuilder<CommandSource> root) {
      root.then(
         literal("add")
            .then(
               arg("block", StringArgumentType.word())
                  .suggests(this::suggestBlocks)
                  .then(arg("color", StringArgumentType.word()).suggests(this::suggestColors).executes(ctx -> this.addBlock(ctx)))
            )
      );
      root.then(literal("remove").then(arg("block", StringArgumentType.word()).suggests(this::suggestBlocks).executes(ctx -> this.removeBlock(ctx))));
   }

   private int addBlock(CommandContext<CommandSource> ctx) {
      String blockName = StringArgumentType.getString(ctx, "block");
      String colorStr = StringArgumentType.getString(ctx, "color").toLowerCase();
      if (!blockName.contains(":")) {
         blockName = "minecraft:" + blockName;
      }

      Color color = COLOR_NAMES.get(colorStr);
      if (color == null) {
         try {
            if (!colorStr.startsWith("#")) {
               colorStr = "#" + colorStr;
            }

            color = Color.decode(colorStr);
         } catch (NumberFormatException var6) {
            ClientManager.message(Formatting.RED + I0O1l0I1.b("SGF0YSEg") + Formatting.WHITE + I0O1l0I1.b("R2XDp2Vyc2l6IHJlbmsuIFJlbmsgYWTEsSB2ZXlhIEhFWCBrb2R1IGt1bGxhbiAow7ZybjogZmYwMGZmKQ=="));
            return 1;
         }
      }

      color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);
      Manager.FUNCTION_MANAGER.blockESP.addCustomBlock(blockName, color);
      ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.WHITE + blockName + I0O1l0I1.b("IGJsb8SfdSA=") + colorStr + I0O1l0I1.b("IHJlbmdpeWxlIGVrbGVuZGku"));
      return 1;
   }

   private int removeBlock(CommandContext<CommandSource> ctx) {
      String blockName = StringArgumentType.getString(ctx, "block");
      if (!blockName.contains(":")) {
         blockName = "minecraft:" + blockName;
      }

      Manager.FUNCTION_MANAGER.blockESP.removeCustomBlock(blockName);
      ClientManager.message(Formatting.GREEN + I0O1l0I1.b("QmHFn2FyxLFsxLEhIA==") + Formatting.WHITE + blockName + I0O1l0I1.b("IGJsb8SfdSBsaXN0ZWRlbiBzaWxpbmRpLg=="));
      return 1;
   }

   private CompletableFuture<Suggestions> suggestColors(CommandContext<CommandSource> ctx, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(COLOR_NAMES.keySet(), builder);
   }

   private CompletableFuture<Suggestions> suggestBlocks(CommandContext<CommandSource> ctx, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(Registries.BLOCK.stream().map(b -> Registries.BLOCK.getId(b).getPath()).toList(), builder);
   }
}
