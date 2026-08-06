package dev.just.util.render.providers;

import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public final class ResourceProvider {
   public static final ShaderProgramKey TEXTURE_SHADER_KEY = new ShaderProgramKey(getShaderIdentifier("texture"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
   public static final ShaderProgramKey RECTANGLE_SHADER_KEY = new ShaderProgramKey(getShaderIdentifier("rectangle"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public static final ShaderProgramKey BLUR_SHADER_KEY = new ShaderProgramKey(getShaderIdentifier("blur"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public static final ShaderProgramKey RECTANGLE_BORDER_SHADER_KEY = new ShaderProgramKey(getShaderIdentifier("border"), VertexFormats.POSITION_COLOR, Defines.EMPTY);
   public static final ShaderProgramKey GLASS_SHADER_KEY = new ShaderProgramKey(getGlass("data"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
   public static final Identifier firefly = Identifier.of("justclient", "images/particles/firefly.png");
   public static final Identifier bloom = Identifier.of("justclient", "images/particles/bloom.png");
   public static final Identifier snowflake = Identifier.of("justclient", "images/particles/snowflake.png");
   public static final Identifier dollar = Identifier.of("justclient", "images/particles/dollar.png");
   public static final Identifier heart = Identifier.of("justclient", "images/particles/heart.png");
   public static final Identifier star = Identifier.of("justclient", "images/particles/star.png");
   public static final Identifier spark = Identifier.of("justclient", "images/particles/spark.png");
   public static final Identifier crown = Identifier.of("justclient", "images/particles/crown.png");
   public static final Identifier lightning = Identifier.of("justclient", "images/particles/lightning.png");
   public static final Identifier line = Identifier.of("justclient", "images/particles/line.png");
   public static final Identifier point = Identifier.of("justclient", "images/particles/point.png");
   public static final Identifier rhombus = Identifier.of("justclient", "images/particles/rhombus.png");
   public static final Identifier marker = Identifier.of("justclient", "images/targetesp/target.png");
   public static final Identifier marker2 = Identifier.of("justclient", "images/targetesp/target2.png");
   public static final Identifier CUSTOM_CAPE = Identifier.of("justclient", "cape/cape.png");
   public static final Identifier CUSTOM_ELYTRA = Identifier.of("justclient", "cape/elytra.png");
   public static final Identifier container = Identifier.of("justclient", "images/hud/container.png");
   public static final Identifier color_image = Identifier.of("justclient", "images/gui/pick.png");

   private static Identifier getGlass(String name) {
      return Identifier.of("justclient", "core/glass/" + name);
   }

   private static Identifier getShaderIdentifier(String name) {
      return Identifier.of("justclient", "core/" + name);
   }
}
