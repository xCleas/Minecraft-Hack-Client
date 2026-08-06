package dev.just.screens.dropdown;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.fontManager.RenderFonts;
import dev.just.modules.Function;
import dev.just.modules.Type;
import dev.just.modules.setting.*;
import dev.just.screens.dropdown.impl.*;
import dev.just.screens.dropdown.search.SearchState;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.util.*;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.Screen;
import org.joml.Vector4f;

public class ClickGUI extends Screen implements IMinecraft {
   // Renkler
   private static final int PRIMARY = new Color(255, 140, 50).getRGB();
   private static final int SECONDARY = new Color(255, 90, 30).getRGB();
   private static final int BG_DARK = new Color(18, 18, 24).getRGB();
   private static final int BG_PANEL = new Color(24, 24, 32).getRGB();
   private static final int BG_ITEM = new Color(32, 32, 42).getRGB();
   private static final int BG_ITEM_HOVER = new Color(42, 42, 55).getRGB();
   private static final int TEXT_WHITE = new Color(235, 235, 240).getRGB();
   private static final int TEXT_GRAY = new Color(140, 140, 155).getRGB();

   // Panel boyutlari
   private static final int PANEL_W = 130;
   private static final int PANEL_H = 300;
   private static final int PANEL_GAP = 6;
   private static final int ITEM_H = 22;

   // State
   private final Set<Type> categories = EnumSet.of(Type.Combat, Type.Move, Type.Render, Type.Player, Type.Misc);
   private static final Map<Type, Float> scrolls = new HashMap<>();
   private final Map<Function, Float> expandAnim = new HashMap<>();
   private final SearchState search = new SearchState();
   private boolean binding = false;
   private Function bindTarget = null;
   private SliderSetting dragSlider = null;
   private int dragSliderX = 0;
   private int dragSliderW = 0;
   private float openAnim = 0;
   private boolean closing = false;

   // Renderers
   private final BooleanSettingRenderer boolRender = new BooleanSettingRenderer();
   private final BindBooleanSettingRenderer bindBoolRender = new BindBooleanSettingRenderer();
   private final BindSettingRenderer bindRender = new BindSettingRenderer();
   private final ModeSettingRenderer modeRender = new ModeSettingRenderer();
   private final MultiSettingRenderer multiRender = new MultiSettingRenderer();
   private final SliderSettingRenderer sliderRender = new SliderSettingRenderer();
   private final TextSettingRenderer textRender = new TextSettingRenderer();

   public ClickGUI() {
      super(Text.literal("ClickGUI"));
      categories.forEach(c -> scrolls.putIfAbsent(c, 0f));
   }

   public void init() {
      super.init();
      closing = false;
      openAnim = 0;
   }

   public void render(DrawContext ctx, int mx, int my, float delta) {
      // Animasyon
      openAnim = closing ? Math.max(0, openAnim - 0.08f) : Math.min(1, openAnim + 0.08f);
      if (closing && openAnim <= 0) {
         super.close();
         return;
      }

      // Arka plan
      int bgAlpha = (int)(openAnim * 120);
      ctx.fill(0, 0, width, height, new Color(0, 0, 0, bgAlpha).getRGB());

      // Scale animasyon
      float scale = 0.9f + openAnim * 0.1f;
      ctx.getMatrices().push();
      ctx.getMatrices().translate(width / 2f, height / 2f, 0);
      ctx.getMatrices().scale(scale, scale, 1);
      ctx.getMatrices().translate(-width / 2f, -height / 2f, 0);

      // Panel pozisyonlari
      int totalW = categories.size() * (PANEL_W + PANEL_GAP) - PANEL_GAP;
      int startX = (width - totalW) / 2;
      int startY = (height - PANEL_H) / 2;

      // Panelleri ciz
      int idx = 0;
      for (Type cat : categories) {
         int px = startX + idx * (PANEL_W + PANEL_GAP);
         renderPanel(ctx, px, startY, cat, mx, my);
         idx++;
      }

      // Arama kutusu
      renderSearch(ctx, mx, my);

      // Alt bilgi
      RenderFonts small = FontUtils.durman[11];
      small.drawLeftAligned(ctx.getMatrices(), "Just Client", 8, height - 16, TEXT_GRAY);

      ctx.getMatrices().pop();
   }

   private void renderPanel(DrawContext ctx, int x, int y, Type cat, int mx, int my) {
      RenderFonts titleFont = FontUtils.sf_bold[14];
      RenderFonts itemFont = FontUtils.sf_medium[12];

      // Panel arka plan
      RenderUtil.drawRoundedRect(ctx.getMatrices(), x, y, PANEL_W, PANEL_H, 8, BG_PANEL);

      // Baslik
      int accent = ColorUtil.interpolateColor(PRIMARY, SECONDARY, (float)cat.ordinal() / 4f);
      RenderUtil.drawRoundedRect(ctx.getMatrices(), x, y, 4, PANEL_H, new Vector4f(8, 0, 0, 8), accent);
      titleFont.drawLeftAligned(ctx.getMatrices(), cat.displayName, x + 14, y + 8, accent);

      // Ayirici
      RenderUtil.drawRoundedRect(ctx.getMatrices(), x + 10, y + 26, PANEL_W - 20, 1, 0, new Color(50, 50, 60).getRGB());

      // Scroll
      float scroll = scrolls.getOrDefault(cat, 0f);
      int contentY = y + 32;
      int contentH = PANEL_H - 40;

      Scissor.push();
      Scissor.setFromComponentCoordinates(x, contentY, PANEL_W, contentH);

      float curY = contentY - scroll;
      for (Function f : Manager.FUNCTION_MANAGER.getFunctions(cat)) {
         if (!matchSearch(f)) continue;

         // Expand animasyon
         float expand = expandAnim.getOrDefault(f, f.expanded ? 1f : 0f);
         float targetExpand = f.expanded ? 1f : 0f;
         expand = MathUtil.lerp(expand, targetExpand, 12f);
         expandAnim.put(f, expand);

         int settingsH = (int)(calcSettingsHeight(f) * expand);
         int totalH = ITEM_H + settingsH;

         // Modul arka plan
         boolean hovered = mx >= x + 4 && mx <= x + PANEL_W - 4 && my >= curY && my < curY + ITEM_H && my >= contentY && my < contentY + contentH;
         int itemBg = hovered ? BG_ITEM_HOVER : BG_ITEM;
         if (f.state) {
            itemBg = ColorUtil.blendColorsInt(itemBg, accent, 0.15f);
         }
         RenderUtil.drawRoundedRect(ctx.getMatrices(), x + 4, curY, PANEL_W - 8, totalH, 4, itemBg);

         // Modul adi
         String name = binding && bindTarget == f ? "[...]" : f.name;
         int nameColor = f.state ? accent : TEXT_WHITE;
         itemFont.drawLeftAligned(ctx.getMatrices(), name, x + 12, curY + 6, nameColor);

         // Ok isareti (ayarlar varsa)
         if (!f.getSettings().isEmpty()) {
            String arrow = f.expanded ? "v" : ">";
            itemFont.drawLeftAligned(ctx.getMatrices(), arrow, x + PANEL_W - 18, curY + 6, TEXT_GRAY);
         }

         // Ayarlar
         if (settingsH > 0 && expand > 0.01f) {
            float setY = curY + ITEM_H + 2;
            for (Setting s : f.getSettings()) {
               if (!s.isVisible()) continue;
               int sh = getSettingHeight(s, PANEL_W - 24);
               if (sh > 0) {
                  renderSetting(ctx, s, x + 12, (int)setY, PANEL_W - 24, sh, mx, my);
                  setY += sh + 2;
               }
            }
         }

         curY += totalH + 2;
      }

      Scissor.pop();

      // Max scroll hesapla
      float maxScroll = Math.max(0, curY + scroll - contentY - contentH);
      scrolls.put(cat, MathHelper.clamp(scroll, 0, maxScroll));
   }

   private void renderSetting(DrawContext ctx, Setting s, int x, int y, int w, int h, int mx, int my) {
      if (s instanceof BooleanSetting bs) {
         boolRender.render(ctx, bs, x, y, w, h);
      } else if (s instanceof BindBooleanSetting bbs) {
         bindBoolRender.render(ctx, bbs, x, y, w, h);
      } else if (s instanceof BindSetting bs) {
         bindRender.render(ctx, bs, x, y, w, h);
      } else if (s instanceof ModeSetting ms) {
         modeRender.render(ctx, ms, x, y, w, h);
      } else if (s instanceof MultiSetting ms) {
         multiRender.render(ctx, ms, x, y, w, h);
      } else if (s instanceof SliderSetting ss) {
         sliderRender.render(ctx, ss, x, y, w, h);
      } else if (s instanceof TextSetting ts) {
         textRender.render(ctx, ts, x, y, w, h);
      }
   }

   private void renderSearch(DrawContext ctx, int mx, int my) {
      int sw = 200;
      int sh = 24;
      int sx = (width - sw) / 2;
      int sy = (height + PANEL_H) / 2 + 12;

      RenderUtil.drawRoundedRect(ctx.getMatrices(), sx, sy, sw, sh, 6, BG_PANEL);

      RenderFonts font = FontUtils.sf_medium[13];
      String text = search.text.isEmpty() && !search.focused ? "Modul ara..." : search.text;
      int color = search.text.isEmpty() && !search.focused ? TEXT_GRAY : TEXT_WHITE;

      if (search.focused && System.currentTimeMillis() % 1000 < 500) {
         text = text + "|";
      }

      font.drawLeftAligned(ctx.getMatrices(), text, sx + 10, sy + 6, color);
   }

   private int calcSettingsHeight(Function f) {
      int h = 0;
      for (Setting s : f.getSettings()) {
         if (s.isVisible()) {
            h += getSettingHeight(s, PANEL_W - 24) + 2;
         }
      }
      return h;
   }

   private int getSettingHeight(Setting s, int w) {
      if (!s.isVisible()) return 0;
      if (s instanceof BooleanSetting) return boolRender.getHeight();
      if (s instanceof BindBooleanSetting) return bindBoolRender.getHeight();
      if (s instanceof BindSetting) return bindRender.getHeight();
      if (s instanceof ModeSetting ms) return modeRender.getHeight(ms, w);
      if (s instanceof MultiSetting ms) return multiRender.getHeight(ms, w);
      if (s instanceof SliderSetting) return sliderRender.getHeight();
      if (s instanceof TextSetting) return textRender.getHeight();
      return 0;
   }

   private boolean matchSearch(Function f) {
      if (search.text.isEmpty()) return true;
      String q = search.text.toLowerCase();
      return f.name.toLowerCase().contains(q) || f.keywords.toLowerCase().contains(q);
   }

   public boolean mouseClicked(double mx, double my, int btn) {
      // Arama kutusu
      int sw = 200, sh = 24;
      int sx = (width - sw) / 2;
      int sy = (height + PANEL_H) / 2 + 12;
      if (mx >= sx && mx <= sx + sw && my >= sy && my <= sy + sh) {
         search.focused = true;
         return true;
      }
      search.focused = false;

      // Binding
      if (binding && bindTarget != null) {
         bindTarget.setBindCode(-(btn + 2));
         binding = false;
         bindTarget = null;
         return true;
      }

      // Panel tiklama
      int totalW = categories.size() * (PANEL_W + PANEL_GAP) - PANEL_GAP;
      int startX = (width - totalW) / 2;
      int startY = (height - PANEL_H) / 2;

      int idx = 0;
      for (Type cat : categories) {
         int px = startX + idx * (PANEL_W + PANEL_GAP);
         if (handlePanelClick(px, startY, cat, mx, my, btn)) return true;
         idx++;
      }

      return super.mouseClicked(mx, my, btn);
   }

   private boolean handlePanelClick(int x, int y, Type cat, double mx, double my, int btn) {
      if (mx < x || mx > x + PANEL_W || my < y || my > y + PANEL_H) return false;

      float scroll = scrolls.getOrDefault(cat, 0f);
      int contentY = y + 32;
      float curY = contentY - scroll;

      for (Function f : Manager.FUNCTION_MANAGER.getFunctions(cat)) {
         if (!matchSearch(f)) continue;

         float expand = expandAnim.getOrDefault(f, f.expanded ? 1f : 0f);
         int settingsH = (int)(calcSettingsHeight(f) * expand);
         int totalH = ITEM_H + settingsH;

         // Modul tiklama
         if (mx >= x + 4 && mx <= x + PANEL_W - 4 && my >= curY && my < curY + ITEM_H) {
            if (btn == 0) {
               f.toggle();
               return true;
            } else if (btn == 1) {
               f.expanded = !f.expanded;
               return true;
            } else if (btn == 2) {
               binding = true;
               bindTarget = f;
               return true;
            }
         }

         // Ayar tiklama
         if (settingsH > 0 && expand > 0.5f) {
            float setY = curY + ITEM_H + 2;
            for (Setting s : f.getSettings()) {
               if (!s.isVisible()) continue;
               int sh = getSettingHeight(s, PANEL_W - 24);
               if (sh > 0) {
                  if (mx >= x + 12 && mx <= x + PANEL_W - 12 && my >= setY && my < setY + sh) {
                     if (handleSettingClick(s, mx, my, btn, x + 12, (int)setY, PANEL_W - 24, sh)) {
                        return true;
                     }
                  }
                  setY += sh + 2;
               }
            }
         }

         curY += totalH + 2;
      }
      return false;
   }

   private boolean handleSettingClick(Setting s, double mx, double my, int btn, int x, int y, int w, int h) {
      if (s instanceof BooleanSetting bs) {
         return boolRender.mouseClicked(bs, mx, my, btn, x, y, w, h);
      } else if (s instanceof BindBooleanSetting bbs) {
         return bindBoolRender.mouseClicked(bbs, mx, my, btn, x, y, w, h);
      } else if (s instanceof BindSetting bs) {
         return bindRender.mouseClicked(bs, mx, my, btn, x, y, w, h);
      } else if (s instanceof ModeSetting ms) {
         return modeRender.mouseClicked(ms, mx, my, btn, x, y, w, h);
      } else if (s instanceof MultiSetting ms) {
         return multiRender.mouseClicked(ms, mx, my, btn, x, y, w, h);
      } else if (s instanceof SliderSetting ss) {
         if (sliderRender.mouseClicked(ss, mx, my, btn, x, y, w, h)) {
            dragSlider = ss;
            dragSliderX = x;
            dragSliderW = w;
            return true;
         }
      } else if (s instanceof TextSetting ts) {
         return textRender.mouseClicked(ts, mx, my, btn, x, y, w, h);
      }
      return false;
   }

   public boolean mouseScrolled(double mx, double my, double dx, double dy) {
      int totalW = categories.size() * (PANEL_W + PANEL_GAP) - PANEL_GAP;
      int startX = (width - totalW) / 2;
      int startY = (height - PANEL_H) / 2;

      int idx = 0;
      for (Type cat : categories) {
         int px = startX + idx * (PANEL_W + PANEL_GAP);
         if (mx >= px && mx <= px + PANEL_W && my >= startY && my <= startY + PANEL_H) {
            float scroll = scrolls.getOrDefault(cat, 0f);
            scrolls.put(cat, scroll - (float)dy * 15);
            return true;
         }
         idx++;
      }
      return super.mouseScrolled(mx, my, dx, dy);
   }

   public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
      if (dragSlider != null && btn == 0) {
         sliderRender.mouseDragged(dragSlider, mx, dragSliderX, dragSliderW);
         return true;
      }
      return super.mouseDragged(mx, my, btn, dx, dy);
   }

   public boolean mouseReleased(double mx, double my, int btn) {
      if (dragSlider != null) {
         sliderRender.mouseReleased(dragSlider);
         dragSlider = null;
         return true;
      }
      return super.mouseReleased(mx, my, btn);
   }

   public boolean keyPressed(int key, int scan, int mod) {
      if (key == 256) {
         close();
         return true;
      }

      // Binding
      if (binding && bindTarget != null) {
         bindTarget.setBindCode(key == 261 ? 0 : key);
         binding = false;
         bindTarget = null;
         return true;
      }

      // Arama
      if (search.focused) {
         if (key == 259 && !search.text.isEmpty()) {
            search.text = search.text.substring(0, search.text.length() - 1);
            return true;
         }
         if (key == 257 || key == 256) {
            search.focused = false;
            return true;
         }
      }

      // Setting key handling
      for (Type cat : categories) {
         for (Function f : Manager.FUNCTION_MANAGER.getFunctions(cat)) {
            if (f.expanded) {
               for (Setting s : f.getSettings()) {
                  if (s instanceof BindBooleanSetting bbs && bbs.isListeningForBind()) {
                     bbs.setKey(key == 261 ? 0 : key);
                     bbs.setListeningForBind(false);
                     return true;
                  }
                  if (s instanceof BindSetting bs && bs.isBinding()) {
                     bs.setKey(key == 261 ? -1 : key);
                     bs.setBinding(false);
                     return true;
                  }
                  if (s instanceof TextSetting ts && ts.isFocused()) {
                     if (textRender.keyPressed(ts, key, scan, mod)) return true;
                  }
               }
            }
         }
      }

      return super.keyPressed(key, scan, mod);
   }

   public boolean charTyped(char c, int mod) {
      if (search.focused) {
         if (search.text.length() < 20) {
            search.text += c;
         }
         return true;
      }

      for (Type cat : categories) {
         for (Function f : Manager.FUNCTION_MANAGER.getFunctions(cat)) {
            if (f.expanded) {
               for (Setting s : f.getSettings()) {
                  if (s instanceof TextSetting ts && ts.isFocused()) {
                     if (textRender.charTyped(ts, c, mod)) return true;
                  }
               }
            }
         }
      }
      return super.charTyped(c, mod);
   }

   public void close() {
      closing = true;
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext ctx, int mx, int my, float delta) {}
}
