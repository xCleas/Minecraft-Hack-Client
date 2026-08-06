package dev.just.manager.fontManager;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.manager.IMinecraft;
import dev.just.util.render.RenderUtil;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.render.VertexFormat;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class RenderFonts implements Closeable, IMinecraft {
   private static final ExecutorService ASYNC_WORKER = Executors.newSingleThreadExecutor();
   private static final float SCALE_FACTOR = 0.5F;
   private static final float INV_SCALE_FACTOR = 2.0F;
   private static final int COLOR_MASK_ALPHA = -16777216;
   private static final int COLOR_MASK_RED = 16711680;
   private static final int COLOR_MASK_GREEN = 65280;
   private static final int COLOR_MASK_BLUE = 255;
   private static final int COLOR_SHIFT_ALPHA = 24;
   private static final int COLOR_SHIFT_RED = 16;
   private static final int COLOR_SHIFT_GREEN = 8;
   private static final int COLOR_SHIFT_BLUE = 0;
   private static final float COLOR_DIVISOR = 255.0F;
   private static final float INV_COLOR_DIVISOR = 0.003921569F;
   private final Map<Identifier, ObjectList<RenderFonts.DrawEntry>> GLYPH_PAGE_CACHE = new ConcurrentHashMap<>();
   private final List<RenderFonts.GlyphMap> maps = new ArrayList<>();
   private final Char2ObjectMap<RenderFonts.Glyph> allGlyphs = new Char2ObjectArrayMap();
   private final int charsPerPage;
   private final int padding;
   private final String prebakeGlyphs;
   private final float originalSize;
   private final FontRenderContext sharedFontRenderContext;
   private float cachedMaxHeight = 0.0F;
   private boolean heightDirty = true;
   private final Map<String, Float> widthCache = new ConcurrentHashMap<>();
   private static final int MAX_WIDTH_CACHE_SIZE = 1000;
   private Font font;
   private Future<Void> prebakeGlyphsFuture;
   private static final Char2ObjectMap<float[]> COLOR_CODES_FLOAT = new Char2ObjectArrayMap();
   private static final ThreadLocal<StringBuilder> STRING_BUILDER_POOL = ThreadLocal.withInitial(StringBuilder::new);
   private static final ThreadLocal<char[]> CHAR_ARRAY_BUFFER = ThreadLocal.withInitial(() -> new char[1024]);

   public RenderFonts(Font font, float sizePx, int charsPerPage, int padding, String prebakeGlyphs) {
      this.originalSize = sizePx;
      this.charsPerPage = charsPerPage;
      this.padding = padding;
      this.prebakeGlyphs = prebakeGlyphs;
      BufferedImage tempImage = new BufferedImage(1, 1, 2);
      Graphics2D g = tempImage.createGraphics();
      this.sharedFontRenderContext = g.getFontRenderContext();
      g.dispose();
      this.initializeFont(font, sizePx);
   }

   public RenderFonts(Font font, float sizePx) {
      this(font, sizePx, 256, 5, null);
   }

   private void initializeFont(Font baseFont, float sizePx) {
      this.font = baseFont.deriveFont(sizePx);
      if (this.prebakeGlyphs != null && !this.prebakeGlyphs.isEmpty()) {
         this.prebakeGlyphsFuture = ASYNC_WORKER.submit(() -> {
            for (char c : this.prebakeGlyphs.toCharArray()) {
               if (Thread.interrupted()) {
                  break;
               }

               this.locateGlyph(c);
            }

            return null;
         });
      }
   }

   public void drawLeftAligned(MatrixStack ms, String text, float x, float y, int color) {
      this.render(ms, text, x, y, color, false);
   }

   public void drawRightAligned(MatrixStack ms, String text, float x, float y, int color) {
      float width = this.getWidth(text);
      this.render(ms, text, x - width, y, color, false);
   }

   public void centeredDraw(MatrixStack ms, String text, float x, float y, int color) {
      float width = this.getWidth(text);
      this.render(ms, text, x - width * 0.5F, y, color, false);
   }

   public float getWidth(String text) {
      if (text != null && !text.isEmpty()) {
         Float cached = this.widthCache.get(text);
         if (cached != null) {
            return cached;
         } else {
            float width = 0.0F;
            char[] chars = this.getCharArray(text);
            int len = text.length();

            for (int i = 0; i < len; i++) {
               char c = chars[i];
               if ((c == 167 || c == '&') && i + 1 < len) {
                  i++;
               } else {
                  RenderFonts.Glyph glyph = this.locateGlyph(c);
                  if (glyph != null) {
                     width += (float)glyph.width() * 0.5F;
                  }
               }
            }

            if (this.widthCache.size() < 1000) {
               this.widthCache.put(text, width);
            }

            return width;
         }
      } else {
         return 0.0F;
      }
   }

   public float getHeight() {
      if (this.heightDirty) {
         float max = 0.0F;
         ObjectIterator var2 = this.allGlyphs.values().iterator();

         while (var2.hasNext()) {
            RenderFonts.Glyph glyph = (RenderFonts.Glyph)var2.next();
            if (glyph != null) {
               float h = (float)glyph.height();
               if (h > max) {
                  max = h;
               }
            }
         }

         this.cachedMaxHeight = max * 0.5F;
         this.heightDirty = false;
      }

      return this.cachedMaxHeight;
   }

   public void drawClipped(MatrixStack ms, String text, float maxWidth, float x, float y, int color) {
      if (text != null && !text.isEmpty()) {
         StringBuilder clipped = STRING_BUILDER_POOL.get();
         clipped.setLength(0);
         float width = 0.0F;
         char[] chars = this.getCharArray(text);
         int len = text.length();

         for (int i = 0; i < len; i++) {
            char c = chars[i];
            if ((c == 167 || c == '&') && i + 1 < len) {
               i++;
            } else {
               RenderFonts.Glyph glyph = this.locateGlyph(c);
               if (glyph != null) {
                  float glyphWidth = (float)glyph.width() * 0.5F;
                  if (width + glyphWidth > maxWidth) {
                     break;
                  }

                  clipped.append(c);
                  width += glyphWidth;
               }
            }
         }

         this.render(ms, clipped.toString(), x, y, color, false);
      }
   }

   public List<String> splitTextToLines(String text, float maxWidth) {
      List<String> lines = new ArrayList<>();
      if (text != null && !text.isEmpty()) {
         StringBuilder currentLine = new StringBuilder();
         String[] words = text.split(" ");

         for (String word : words) {
            if (currentLine.length() > 0) {
               String testLine = currentLine + " " + word;
               if (this.getWidth(testLine) <= maxWidth) {
                  currentLine.append(" ").append(word);
               } else {
                  lines.add(currentLine.toString());
                  currentLine = new StringBuilder(word);
               }
            } else if (this.getWidth(word) <= maxWidth) {
               currentLine.append(word);
            } else {
               StringBuilder part = new StringBuilder();

               for (char c : word.toCharArray()) {
                  if (this.getWidth(part.toString() + c) <= maxWidth) {
                     part.append(c);
                  } else {
                     lines.add(part.toString());
                     part = new StringBuilder(c + "");
                  }
               }

               currentLine = part;
            }
         }

         if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
         }

         return lines;
      } else {
         return lines;
      }
   }

   private void render(MatrixStack ms, String text, float x, float y, int color, boolean isGradient) {
      if (text != null && !text.isEmpty()) {
         if (this.prebakeGlyphsFuture != null && !this.prebakeGlyphsFuture.isDone()) {
            try {
               this.prebakeGlyphsFuture.get();
            } catch (Exception var20) {
            }
         }

         float a = (float)(color >>> 24 & 0xFF) * 0.003921569F;
         float r = (float)(color >>> 16 & 0xFF) * 0.003921569F;
         float g = (float)(color >>> 8 & 0xFF) * 0.003921569F;
         float b = (float)(color & 0xFF) * 0.003921569F;
         RenderSystem.setShaderColor(r, g, b, a);
         ms.push();
         ms.translate(x, y, 0.0F);
         ms.scale(0.5F, 0.5F, 1.0F);
         RenderUtil.enableRender();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         Matrix4f matrix4f = ms.peek().getPositionMatrix();
         Map<Identifier, List<RenderFonts.DrawEntry>> localCache = new Object2ObjectOpenHashMap();
         this.GLYPH_PAGE_CACHE.forEach((key, value) -> localCache.put(key, new ObjectArrayList(value)));
         this.GLYPH_PAGE_CACHE.clear();
         float cursorX = 0.0F;
         char[] chars = this.getCharArray(text);
         int len = text.length();

         for (int i = 0; i < len; i++) {
            char c = chars[i];
            if (!isGradient && (c == 167 || c == '&') && i + 1 < len) {
               char code = Character.toLowerCase(chars[++i]);
               float[] col = (float[])COLOR_CODES_FLOAT.get(code);
               if (col != null) {
                  r = col[0];
                  g = col[1];
                  b = col[2];
               }
            } else {
               RenderFonts.Glyph glyph = this.locateGlyph(c);
               if (glyph != null) {
                  Identifier tex = glyph.owner().bindToTexture;
                  localCache.computeIfAbsent(tex, k -> new ObjectArrayList()).add(new RenderFonts.DrawEntry(cursorX, 0.0F, r, g, b, glyph));
                  cursorX += (float)glyph.width();
               }
            }
         }

         Tessellator tessellator = IMinecraft.tessellator();
         localCache.forEach((identifier, entries) -> {
            RenderSystem.setShaderTexture(0, identifier);
            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (RenderFonts.DrawEntry entry : entries) {
               RenderFonts.Glyph g2 = entry.toDraw;
               RenderFonts.GlyphMap gm = g2.owner();
               float w = (float)g2.width();
               float h = (float)g2.height();
               float u1 = (float)g2.u() * gm.invWidth;
               float v1 = (float)g2.v() * gm.invHeight;
               float u2 = ((float)g2.u() + w) * gm.invWidth;
               float v2 = ((float)g2.v() + h) * gm.invHeight;
               bufferBuilder.vertex(matrix4f, entry.atX, entry.atY + h, 0.0F).texture(u1, v2).color(entry.r, entry.g, entry.b, a);
               bufferBuilder.vertex(matrix4f, entry.atX + w, entry.atY + h, 0.0F).texture(u2, v2).color(entry.r, entry.g, entry.b, a);
               bufferBuilder.vertex(matrix4f, entry.atX + w, entry.atY, 0.0F).texture(u2, v1).color(entry.r, entry.g, entry.b, a);
               bufferBuilder.vertex(matrix4f, entry.atX, entry.atY, 0.0F).texture(u1, v1).color(entry.r, entry.g, entry.b, a);
            }

            RenderUtil.render3D.endBuilding(bufferBuilder);
         });
         ms.pop();
         RenderUtil.disableRender();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public void renderGradientText(MatrixStack matrixStack, String text, float x, float y, int color1, int color2) {
      this.renderGradientInternal(matrixStack, text, x, y, color1, color2, 0.0F, false);
   }

   public void renderAnimatedGradientText(MatrixStack matrixStack, String text, float x, float y, int color1, int color2, float time) {
      this.renderGradientInternal(matrixStack, text, x, y, color1, color2, time, true);
   }

   private void renderGradientInternal(MatrixStack matrixStack, String text, float x, float y, int color1, int color2, float time, boolean animated) {
      if (text != null && !text.isEmpty()) {
         int length = text.length();
         if (length != 0) {
            float rStart = (float)(color1 >>> 16 & 0xFF) * 0.003921569F;
            float gStart = (float)(color1 >>> 8 & 0xFF) * 0.003921569F;
            float bStart = (float)(color1 & 0xFF) * 0.003921569F;
            float aStart = (float)(color1 >>> 24 & 0xFF) * 0.003921569F;
            float rEnd = (float)(color2 >>> 16 & 0xFF) * 0.003921569F;
            float gEnd = (float)(color2 >>> 8 & 0xFF) * 0.003921569F;
            float bEnd = (float)(color2 & 0xFF) * 0.003921569F;
            float aEnd = (float)(color2 >>> 24 & 0xFF) * 0.003921569F;
            if (this.prebakeGlyphsFuture != null && !this.prebakeGlyphsFuture.isDone()) {
               try {
                  this.prebakeGlyphsFuture.get();
               } catch (Exception var31) {
               }
            }

            matrixStack.push();
            matrixStack.translate(x, y, 0.0F);
            matrixStack.scale(0.5F, 0.5F, 1.0F);
            RenderUtil.enableRender();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            Map<Identifier, List<RenderFonts.GradientDrawEntry>> textureMap = new Object2ObjectOpenHashMap();
            float cursorX = 0.0F;
            float invLength = length == 1 ? 0.0F : 1.0F / (float)(length - 1);
            char[] chars = this.getCharArray(text);

            for (int i = 0; i < length; i++) {
               char c = chars[i];
               float t = (float)i * invLength;
               if (animated) {
                  float tRaw = t + time % 1.0F;
                  t = tRaw % 1.0F;
                  t = t < 0.5F ? t * 2.0F : (1.0F - t) * 2.0F;
               }

               float r = rStart + (rEnd - rStart) * t;
               float g = gStart + (gEnd - gStart) * t;
               float b = bStart + (bEnd - bStart) * t;
               float a = aStart + (aEnd - aStart) * t;
               RenderFonts.Glyph glyph = this.locateGlyph(c);
               if (glyph != null) {
                  textureMap.computeIfAbsent(glyph.owner().bindToTexture, k -> new ObjectArrayList())
                     .add(new RenderFonts.GradientDrawEntry(cursorX, r, g, b, a, glyph));
                  cursorX += (float)glyph.width();
               }
            }

            Tessellator tessellator = IMinecraft.tessellator();
            textureMap.forEach((texture, entries) -> {
               RenderSystem.setShaderTexture(0, texture);
               BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

               for (RenderFonts.GradientDrawEntry entry : entries) {
                  RenderFonts.Glyph glyphx = entry.glyph;
                  float w = (float)glyphx.width();
                  float h = (float)glyphx.height();
                  float u1 = (float)glyphx.u() * glyphx.owner().invWidth;
                  float v1 = (float)glyphx.v() * glyphx.owner().invHeight;
                  float u2 = ((float)glyphx.u() + w) * glyphx.owner().invWidth;
                  float v2 = ((float)glyphx.v() + h) * glyphx.owner().invHeight;
                  bufferBuilder.vertex(matrix4f, entry.x, h, 0.0F).texture(u1, v2).color(entry.r, entry.g, entry.b, entry.a);
                  bufferBuilder.vertex(matrix4f, entry.x + w, h, 0.0F).texture(u2, v2).color(entry.r, entry.g, entry.b, entry.a);
                  bufferBuilder.vertex(matrix4f, entry.x + w, 0.0F, 0.0F).texture(u2, v1).color(entry.r, entry.g, entry.b, entry.a);
                  bufferBuilder.vertex(matrix4f, entry.x, 0.0F, 0.0F).texture(u1, v1).color(entry.r, entry.g, entry.b, entry.a);
               }

               RenderUtil.render3D.endBuilding(bufferBuilder);
            });
            matrixStack.pop();
            RenderUtil.disableRender();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }
   }

   @Nullable
   private RenderFonts.Glyph locateGlyph(char glyphChar) {
      RenderFonts.Glyph existing = (RenderFonts.Glyph)this.allGlyphs.get(glyphChar);
      if (existing != null) {
         return existing;
      } else {
         RenderFonts.Glyph newGlyph = this.createGlyph(glyphChar);
         if (newGlyph != null) {
            this.allGlyphs.put(glyphChar, newGlyph);
            this.heightDirty = true;
         }

         return newGlyph;
      }
   }

   @Nullable
   private RenderFonts.Glyph createGlyph(char glyphChar) {
      for (RenderFonts.GlyphMap map : this.maps) {
         if (map.contains(glyphChar)) {
            return map.getGlyph(glyphChar);
         }
      }

      int base = this.charsPerPage * (glyphChar / this.charsPerPage);
      String id = generateRandomId(16);
      RenderFonts.GlyphMap newMap = new RenderFonts.GlyphMap(
         (char)base, (char)(base + this.charsPerPage), this.font, Identifier.of("font", "temp/" + id), this.padding, this.sharedFontRenderContext
      );
      this.maps.add(newMap);
      return newMap.getGlyph(glyphChar);
   }

   private char[] getCharArray(String text) {
      char[] buffer = CHAR_ARRAY_BUFFER.get();
      int len = text.length();
      if (len > buffer.length) {
         buffer = new char[len];
         CHAR_ARRAY_BUFFER.set(buffer);
      }

      text.getChars(0, len, buffer, 0);
      return buffer;
   }

   private static String generateRandomId(int length) {
      Random random = new Random();
      StringBuilder sb = new StringBuilder(length);

      for (int i = 0; i < length; i++) {
         sb.append((char)(97 + random.nextInt(26)));
      }

      return sb.toString();
   }

   @Override
   public void close() {
      try {
         if (this.prebakeGlyphsFuture != null && !this.prebakeGlyphsFuture.isDone() && !this.prebakeGlyphsFuture.isCancelled()) {
            this.prebakeGlyphsFuture.cancel(true);
            this.prebakeGlyphsFuture.get();
            this.prebakeGlyphsFuture = null;
         }

         this.maps.forEach(RenderFonts.GlyphMap::destroy);
         this.maps.clear();
         this.allGlyphs.clear();
         this.GLYPH_PAGE_CACHE.clear();
         this.widthCache.clear();
      } catch (Exception var2) {
      }
   }

   static {
      COLOR_CODES_FLOAT.put('0', new float[]{0.0F, 0.0F, 0.0F});
      COLOR_CODES_FLOAT.put('1', new float[]{0.0F, 0.0F, 0.6666667F});
      COLOR_CODES_FLOAT.put('2', new float[]{0.0F, 0.6666667F, 0.0F});
      COLOR_CODES_FLOAT.put('3', new float[]{0.0F, 0.6666667F, 0.6666667F});
      COLOR_CODES_FLOAT.put('4', new float[]{0.6666667F, 0.0F, 0.0F});
      COLOR_CODES_FLOAT.put('5', new float[]{0.6666667F, 0.0F, 0.6666667F});
      COLOR_CODES_FLOAT.put('6', new float[]{1.0F, 0.6666667F, 0.0F});
      COLOR_CODES_FLOAT.put('7', new float[]{0.6666667F, 0.6666667F, 0.6666667F});
      COLOR_CODES_FLOAT.put('8', new float[]{0.33333334F, 0.33333334F, 0.33333334F});
      COLOR_CODES_FLOAT.put('9', new float[]{0.33333334F, 0.33333334F, 1.0F});
      COLOR_CODES_FLOAT.put('a', new float[]{0.33333334F, 1.0F, 0.33333334F});
      COLOR_CODES_FLOAT.put('b', new float[]{0.33333334F, 1.0F, 1.0F});
      COLOR_CODES_FLOAT.put('c', new float[]{1.0F, 0.33333334F, 0.33333334F});
      COLOR_CODES_FLOAT.put('d', new float[]{1.0F, 0.33333334F, 1.0F});
      COLOR_CODES_FLOAT.put('e', new float[]{1.0F, 1.0F, 0.33333334F});
      COLOR_CODES_FLOAT.put('f', new float[]{1.0F, 1.0F, 1.0F});
   }

   private static record CharMetrics(char character, int width, int height) {
   }

   private static record DrawEntry(float atX, float atY, float r, float g, float b, RenderFonts.Glyph toDraw) {
   }

   private static record Glyph(int u, int v, int width, int height, char value, RenderFonts.GlyphMap owner) {
   }

   private class GlyphMap {
      private final Char2ObjectMap<RenderFonts.Glyph> glyphs = new Char2ObjectArrayMap();
      private final Font font;
      private final Identifier bindToTexture;
      private final char fromIncl;
      private final char toExcl;
      private final int pixelPadding;
      private final FontRenderContext fontRenderContext;
      private int width;
      private int height;
      private float invWidth;
      private float invHeight;
      private boolean generated = false;

      GlyphMap(char from, char to, Font font, Identifier id, int padding, FontRenderContext frc) {
         this.fromIncl = from;
         this.toExcl = to;
         this.font = font;
         this.bindToTexture = id;
         this.pixelPadding = padding;
         this.fontRenderContext = frc;
      }

      RenderFonts.Glyph getGlyph(char c) {
         if (!this.generated) {
            this.generate();
         }

         return (RenderFonts.Glyph)this.glyphs.get(c);
      }

      boolean contains(char c) {
         return c >= this.fromIncl && c < this.toExcl;
      }

      void destroy() {
         IMinecraft.mc.getTextureManager().destroyTexture(this.bindToTexture);
         this.glyphs.clear();
         this.width = -1;
         this.height = -1;
         this.generated = false;
      }

      private void generate() {
         if (!this.generated) {
            int range = this.toExcl - this.fromIncl;
            int charsPerRow = (int)Math.ceil(Math.sqrt((double)range));
            int charsPerCol = (int)Math.ceil((double)range / (double)charsPerRow);
            int maxCharWidth = 0;
            int maxCharHeight = 0;
            RenderFonts.CharMetrics[] metrics = new RenderFonts.CharMetrics[range];

            for (int i = 0; i < range; i++) {
               char c = (char)(this.fromIncl + i);
               Rectangle2D bounds = this.font.getStringBounds(String.valueOf(c), this.fontRenderContext);
               int w = (int)Math.ceil(bounds.getWidth());
               int h = (int)Math.ceil(bounds.getHeight());
               maxCharWidth = Math.max(maxCharWidth, w);
               maxCharHeight = Math.max(maxCharHeight, h);
               metrics[i] = new RenderFonts.CharMetrics(c, w, h);
            }

            this.width = Math.max((maxCharWidth + this.pixelPadding) * charsPerRow + this.pixelPadding, 1);
            this.height = Math.max((maxCharHeight + this.pixelPadding) * charsPerCol + this.pixelPadding, 1);
            this.invWidth = 1.0F / (float)this.width;
            this.invHeight = 1.0F / (float)this.height;
            BufferedImage img = new BufferedImage(this.width, this.height, 2);
            Graphics2D g2d = img.createGraphics();
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, this.width, this.height);
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.setColor(Color.WHITE);
            g2d.setFont(this.font);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g2d.getFontMetrics();
            int baseAscent = fm.getAscent();

            for (int i = 0; i < metrics.length; i++) {
               RenderFonts.CharMetrics cm = metrics[i];
               int row = i / charsPerRow;
               int col = i % charsPerRow;
               int x = col * (maxCharWidth + this.pixelPadding) + this.pixelPadding;
               int y = row * (maxCharHeight + this.pixelPadding) + this.pixelPadding + baseAscent;
               RenderFonts.Glyph glyph = new RenderFonts.Glyph(x, y - baseAscent, cm.width, cm.height, cm.character, this);
               this.glyphs.put(cm.character, glyph);
               g2d.drawString(String.valueOf(cm.character), x, y);
            }

            g2d.dispose();
            this.registerBufferedImageTexture(this.bindToTexture, img);
            this.generated = true;
         }
      }

      private void registerBufferedImageTexture(Identifier id, BufferedImage img) {
         try {
            NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, img.getWidth(), img.getHeight(), false);

            try {
               int[] pixels = new int[img.getWidth() * img.getHeight()];
               img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

               for (int i = 0; i < pixels.length; i++) {
                  int argb = pixels[i];
                  int a = argb >>> 24;
                  int r = argb >>> 16 & 0xFF;
                  int g = argb >>> 8 & 0xFF;
                  int b = argb & 0xFF;
                  int abgr = a << 24 | b << 16 | g << 8 | r;
                  nativeImage.setColorArgb(i % img.getWidth(), i / img.getWidth(), abgr);
               }

               NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
               texture.upload();
               IMinecraft.mc.getTextureManager().registerTexture(id, texture);
            } catch (Throwable var13) {
               try {
                  nativeImage.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }

               throw var13;
            }

            nativeImage.close();
         } catch (Throwable var14) {
         }
      }
   }

   private static record GradientDrawEntry(float x, float r, float g, float b, float a, RenderFonts.Glyph glyph) {
   }
}
