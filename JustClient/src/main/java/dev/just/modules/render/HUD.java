package dev.just.modules.render;

import com.google.common.collect.Lists;
import dev.just.JustClient;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.render.EventRender2D;
import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.dragManager.Dragging;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.fontManager.RenderFonts;
import dev.just.manager.themeManager.StyleManager;
import dev.just.mixin.iface.ItemCooldownEntryAccessor;
import dev.just.mixin.iface.ItemCooldownManagerAccessor;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.animations.Animation;
import dev.just.util.animations.impl.EaseBackIn;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.player.ServerUtil;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.Item;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.world.GameMode;
import net.minecraft.text.Text;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Direction.AxisDirection;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.joml.Vector4i;

@FunctionAnnotation(
   name = "HUD",
   desc = "S3VsbGFuxLFjxLEgYXJhefO8esO8",
   type = Type.Render
)
public class HUD extends Function {
   public final MultiSetting setting = new MultiSetting(
      I0O1l0I1.b("RWxlbWVudGxlcg=="),
      Arrays.asList(I0O1l0I1.b("WsSxcmhIVUQ=")),
      new String[]{I0O1l0I1.b("WsSxcmhIVUQ=")}
   );
   private final ModeSetting hudColor = new ModeSetting(I0O1l0I1.b("SFVEIHJlbmdp"), "Normal", "Normal", I0O1l0I1.b("VGVtYXlhIGJhxJ9sxLE="));
   private final ModeSetting gradientType = new ModeSetting(() -> this.hudColor.is(I0O1l0I1.b("VGVtYXlhIGJhxJ9sxLE=")), I0O1l0I1.b("R3JhZHlhbiB0aXBp"), I0O1l0I1.b("U29sZGFuIHNhxJ9h"), I0O1l0I1.b("U29sZGFuIHNhxJ9h"), I0O1l0I1.b("U2HEn2RhbiBzb2xh"));
   private final SliderSetting customAlpha = new SliderSetting(I0O1l0I1.b("U2F5ZGFtbMSxaw=="), 120.0, 120.0, 255.0, 5.0);
   private final BooleanSetting visibleCrosshair = new BooleanSetting(
      I0O1l0I1.b("SGVkZWZlIGJha2Fya2VuIEhlZGVmIEh1ZCBnw7ZzdGVy"), false, I0O1l0I1.b("SGVkZWZlIGJha2Fya2VuIEhlZGVmIEh1ZCd1IGfDtnN0ZXJpcg=="), () -> this.setting.get(I0O1l0I1.b("SGVkZWYgSHVk"))
   );
   private final BooleanSetting blur = new BooleanSetting(I0O1l0I1.b("QnVsYW7EsWtsxLFr"), false, I0O1l0I1.b("VMO8bSBIVUQgZWxlbWVudGxlcmluZSBidWxhbsSxa2zEsWsgdXlndWxhcg=="));
   private final SliderSetting roundingSilaSanya = new SliderSetting(I0O1l0I1.b("S2FmYSB5dXZhcmxhbWE="), 2.0, 0.0, 12.0, 1.0);
   private static final Pattern NAME_PATTERN = Pattern.compile("^\\w{3,16}$");
   private static final Pattern PREFIX_MATCHES = Pattern.compile(".*(mod|mod|adm|adm|help|help|curat|curat|own|own|dev|supp|supp|staff|staff).*", 2);
   private static final Item[] TRACKED_ITEMS = new Item[]{
      Items.ENDER_PEARL,
      Items.CHORUS_FRUIT,
      Items.FIREWORK_ROCKET,
      Items.SHIELD,
      Items.GOLDEN_APPLE,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.TOTEM_OF_UNDYING,
      Items.SNOWBALL,
      Items.DRIED_KELP,
      Items.ENDER_EYE,
      Items.NETHERITE_SCRAP,
      Items.EXPERIENCE_BOTTLE,
      Items.PHANTOM_MEMBRANE
   };
   private static final Map<Item, String> ITEM_NAMES;
   public final Dragging watermarkDrag = JustClient.getInstance().createDrag(this, "JustHud", 10.0F, 10.0F);
   public final Dragging targethudDrag = JustClient.getInstance().createDrag(this, "Hedef Hud", 10.0F, 45.0F);
   public final Dragging keybindsDrag = JustClient.getInstance().createDrag(this, I0O1l0I1.b("VHXFn0F0YW1hbGFyxLFIVUQ="), 10.0F, 95.0F);
   public final Dragging stafflistDrag = JustClient.getInstance().createDrag(this, "PersonelListesiHUD", 10.0F, 128.0F);
   public final Dragging itemcooldownDrag = JustClient.getInstance().createDrag(this, I0O1l0I1.b("QmVrbGVtZVPDvHJlc2lIVUQ="), 10.0F, 165.0F);
   public final Dragging potionhudDrag = JustClient.getInstance().createDrag(this, I0O1l0I1.b("xLBrc2lySFVE"), 10.0F, 198.0F);
   public final Dragging coordinateshudDrag = JustClient.getInstance().createDrag(this, "KoordinatlarHUD", 10.0F, 198.0F);
   public final Dragging armorDrag = JustClient.getInstance().createDrag(this, I0O1l0I1.b("WsSxcmhIVUQ="), 478.0F, 468.0F);
   Animation tHudAnimation = new EaseBackIn(300, 1.0, 1.5F);
   private final Vector4f corner = new Vector4f(3.0F, 0.0F, 0.0F, 3.0F);
   LivingEntity target = null;
   float health = 0.0F;
   float health2 = 0.0F;
   int activeModules = 0;
   private float heightDynamic = 0.0F;
   private double scale = 0.0;
   private final List<HUD.StaffPlayer> staffPlayers = new ArrayList<>(32);
   private final Set<String> addedPlayers = new HashSet<>(64);
   private String serverAddressCache = "";
   private boolean isLocalServerCache = false;
   private float potionListHeightDynamic = 0.0F;
   private float cooldownListHeightDynamic = 0.0F;
   private int activeStaff = 0;
   private float hDynam = 0.0F;
   private float widthDynamic = 0.0F;
   private float nameWidth = 0.0F;
   private float lastHealth = 0.0F;
   private float lastAbsorption = 0.0F;
   private float keybindsHeightDynamic = 0.0F;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public HUD() {
      this.addSettings(
         new Setting[]{this.setting, this.hudColor, this.gradientType, this.customAlpha, this.blur, this.roundingSilaSanya}
      );
      this.state = true; // HUD varsayilan olarak acik
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (mc == null || mc.player == null || mc.world == null) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventRender2D eventRender2D) {
                  handleRenderInternal(eventRender2D);
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void handleRenderInternal(EventRender2D eventRender2D) {
      l1O0I1lO.fakeHandler();
      // Ana HUD (Just Client + Isim/FPS + Aktif Moduller)
      this.waterMark(eventRender2D);

      // Zirh HUD (ayara bagli)
      if (l1O0I1lO.opaqueTrue() && this.setting.get(I0O1l0I1.b("WsSxcmhIVUQ="))) {
         this.armor(eventRender2D);
      }
   }

   private void armor(EventRender2D eventRender2D) {
      float x = this.armorDrag.getX();
      float y = this.armorDrag.getY();
      int armorCount = 0;

      for (int i = 0; i < 4; i++) {
         if (!((ItemStack)mc.player.getInventory().armor.get(i)).isEmpty()) {
            armorCount++;
         }
      }

      int width = armorCount > 0 ? 20 * armorCount : 35;
      this.armorDrag.setWidth((float)width);
      this.armorDrag.setHeight(18.0F);
      float startX = x + (float)width - 20.0F;

      for (int ix = 0; ix < 4; ix++) {
         ItemStack itemStack = (ItemStack)mc.player.getInventory().armor.get(ix);
         if (!itemStack.isEmpty()) {
            eventRender2D.getDrawContext().getMatrices().push();
            eventRender2D.getDrawContext().getMatrices().translate(startX, y + 0.2F, 0.0F);
            eventRender2D.getDrawContext().getMatrices().scale(1.0F, 1.0F, 1.0F);
            eventRender2D.getDrawContext().drawItem(itemStack, 0, 0, 0);
            eventRender2D.getDrawContext().drawStackOverlay(mc.textRenderer, itemStack, 0, 0);
            eventRender2D.getDrawContext().getMatrices().pop();
            startX -= 20.0F;
         }
      }
   }

   private void updateStaffPlayers(MinecraftClient mc) {
      this.staffPlayers.clear();
      this.addedPlayers.clear();
      Map<String, PlayerListEntry> nameToEntry = new HashMap<>(mc.player.networkHandler.getPlayerList().size() + 4);

      for (PlayerListEntry e : mc.player.networkHandler.getPlayerList()) {
         if (e.getProfile() != null && e.getProfile().getName() != null) {
            nameToEntry.put(e.getProfile().getName().toLowerCase(Locale.ROOT), e);
         }
      }

      String ourName = mc.player.getName().getString();
      Scoreboard scoreboard = mc.world.getScoreboard();

      for (Team team : scoreboard.getTeams()) {
         Text prefixComponent = team.getPrefix();
         String prefix = prefixComponent.getString();
         String cleanPrefixLower = this.repairString(prefix).toLowerCase(Locale.ROOT);

         for (String member : team.getPlayerList()) {
            if (member != null && !member.equals(ourName) && !this.addedPlayers.contains(member) && NAME_PATTERN.matcher(member).matches()) {
               PlayerListEntry entry = nameToEntry.get(member.toLowerCase(Locale.ROOT));
               boolean isVanished = entry == null;
               if (!isVanished) {
                  if (PREFIX_MATCHES.matcher(cleanPrefixLower).matches() || Manager.STAFF_MANAGER.isStaff(member)) {
                     UUID uuid = entry.getProfile().getId();
                     this.staffPlayers.add(new HUD.StaffPlayer(member, prefixComponent, uuid));
                     this.addedPlayers.add(member);
                  }
               } else if (!prefix.isEmpty()) {
                  this.staffPlayers.add(new HUD.StaffPlayer(member, prefixComponent, null));
                  this.addedPlayers.add(member);
               }
            }
         }
      }

      if (!this.staffPlayers.isEmpty()) {
         this.staffPlayers.sort(Comparator.comparing(HUD.StaffPlayer::getName));
      }
   }

   private void potion(EventRender2D eventRender2D) {
      float posX = this.potionhudDrag.getX();
      float posY = this.potionhudDrag.getY();
      float time = (float)(System.currentTimeMillis() % 2000L) / 2000.0F;
      float pulse = (float)(Math.sin((double)time * Math.PI * 2.0) * 0.1F + 0.9F);
      int headerHeight = 18;
      int padding = 5;
      int lineHeight = 10;
      List<StatusEffectInstance> activeEffects = new ArrayList<>(mc.player.getStatusEffects());
      float maxWidth = 100.0F;
      List<Runnable> list = Lists.newArrayListWithCapacity(activeEffects.size());
      float maxDurationWidth = 0.0F;

      for (StatusEffectInstance eff : activeEffects) {
         String name = I18n.translate(((StatusEffect)eff.getEffectType().value()).getTranslationKey(), new Object[0]);
         int level = eff.getAmplifier() + 1;
         String levelStr = level > 1 ? " " + level : "";
         String displayName = name + levelStr;
         float nameWidth = FontUtils.durman[13].getWidth(displayName);
         String duration = this.formatDuration(eff);
         float durationWidth = FontUtils.durman[13].getWidth(duration);
         maxDurationWidth = Math.max(maxDurationWidth, durationWidth);
         float totalWidth = (float)(padding * 2 + 25) + nameWidth + (float)padding + durationWidth;
         if (totalWidth > maxWidth) {
            maxWidth = totalWidth;
         }
      }

      float listHeightTarget = (float)(activeEffects.size() * lineHeight);
      this.potionListHeightDynamic = MathUtil.fast(this.potionListHeightDynamic, listHeightTarget, 15.0F);
      float totalHeight = (float)headerHeight + this.potionListHeightDynamic;
      int alpha = this.customAlpha.get().intValue();
      if (alpha <= 240 && this.blur.get()) {
         RenderUtil.drawBlur(
            eventRender2D.getDrawContext().getMatrices(),
            posX,
            posY + (float)headerHeight - 1.0F,
            maxWidth,
            this.potionListHeightDynamic + 6.0F,
            new Vector4f(0.0F, 3.0F, 3.0F, 0.0F),
            12.0F,
            Color.white.getRGB()
         );
      }

      StyleManager theme = Manager.STYLE_MANAGER;
      Color upColor = new Color(theme.getFirstColor());
      Color downColor = new Color(theme.getSecondColor());
      if (this.hudColor.is("Normal")) {
         RenderUtil.drawRoundedRect(
            eventRender2D.getDrawContext().getMatrices(),
            posX,
            posY,
            maxWidth,
            (float)(headerHeight + 1),
            new Vector4f(3.0F, 0.0F, 0.0F, 3.0F),
            ColorUtil.hud_color
         );
      } else {
         int left = ColorUtil.gradient(10, 90, upColor.getRGB(), downColor.getRGB());
         int right = ColorUtil.gradient(10, 0, upColor.getRGB(), downColor.getRGB());
         int top = ColorUtil.gradient(10, 180, upColor.getRGB(), downColor.getRGB());
         int bottom = ColorUtil.gradient(10, 270, upColor.getRGB(), downColor.getRGB());
         boolean leftToRight = this.gradientType.is(I0O1l0I1.b("U29sZGFuIHNhxJ9h"));
         int c1 = leftToRight ? ColorUtil.hud_color : left;
         int c2 = leftToRight ? ColorUtil.hud_color : right;
         int c3 = leftToRight ? right : ColorUtil.hud_color;
         int c4 = leftToRight ? left : ColorUtil.hud_color;
         RenderUtil.rectRGB(eventRender2D.getDrawContext().getMatrices(), posX, posY, maxWidth, (float)(headerHeight + 1), this.corner, c1, c2, c3, c4);
      }

      RenderUtil.drawTexture(
         eventRender2D.getDrawContext().getMatrices(), "images/hud/potion.png", posX + maxWidth - 16.0F, posY + 4.5F, 11.0F, 11.0F, 0.0F, Color.white.getRGB()
      );
      FontUtils.durman[15].drawLeftAligned(eventRender2D.getDrawContext().getMatrices(), "Etkiler", posX + 10.0F, posY + 5.0F, -1);
      RenderUtil.drawRoundedRect(
         eventRender2D.getDrawContext().getMatrices(),
         posX,
         posY + (float)headerHeight - 1.0F,
         maxWidth,
         this.potionListHeightDynamic + 6.0F,
         new Vector4f(0.0F, 3.0F, 3.0F, 0.0F),
         new Color(22, 22, 22, alpha).getRGB()
      );
      Scissor.push();
      Scissor.setFromComponentCoordinates(
         (double)posX, (double)posY, (double)maxWidth, (double)((float)headerHeight + this.potionListHeightDynamic + (float)padding / 2.0F + 5.0F)
      );
      float yOffset = posY + (float)headerHeight + (float)padding - 1.0F;
      StatusEffectSpriteManager spriteManager = mc.getStatusEffectSpriteManager();

      for (StatusEffectInstance effx : activeEffects) {
         StatusEffect effect = (StatusEffect)effx.getEffectType().value();
         RegistryEntry<StatusEffect> holder = effx.getEffectType();
         String name = I18n.translate(effect.getTranslationKey(), new Object[0]);
         int level = effx.getAmplifier() + 1;
         String levelStr = level > 1 ? " " + level : "";
         String displayName = name + levelStr;
         String duration = this.formatDuration(effx);
         int ticksLeft = effx.getDuration();
         int colorAlpha;
         if (ticksLeft <= 60 && ticksLeft > 0) {
            colorAlpha = (int)(Math.sin((double)System.currentTimeMillis() / 200.0) * 80.0 + 128.0);
         } else {
            colorAlpha = 255;
         }

         int color = ColorUtil.rgba(255, 255, 255, colorAlpha);
         Sprite texture = spriteManager.getSprite(holder);
         float finalYOffset = yOffset;
         list.add(
            () -> eventRender2D.getDrawContext()
                  .drawSpriteStretched(RenderLayer::getGuiTextured, texture, (int)(posX + (float)padding - 1.5F), (int)finalYOffset - 1, 9, 9, -1)
         );
         RenderUtil.drawRoundedRect(
            eventRender2D.getDrawContext().getMatrices(),
            posX + (float)padding + 9.0F,
            finalYOffset - 1.0F,
            1.2F,
            9.0F,
            0.0F,
            new Color(255, 255, 255, 120).getRGB()
         );
         FontUtils.durman[13].drawLeftAligned(eventRender2D.getDrawContext().getMatrices(), displayName, posX + (float)padding + 14.0F, yOffset - 1.0F, color);
         RenderUtil.drawRoundedRect(
            eventRender2D.getDrawContext().getMatrices(),
            posX + maxWidth - maxDurationWidth - (float)padding - 7.0F,
            yOffset - 1.0F,
            8.0F + maxDurationWidth,
            10.0F,
            1.0F,
            ColorUtil.hud_color
         );
         FontUtils.durman[13]
            .drawLeftAligned(
               eventRender2D.getDrawContext().getMatrices(), duration, posX + maxWidth - maxDurationWidth - (float)padding - 3.0F, yOffset - 0.3F, color
            );
         yOffset += (float)lineHeight;
      }

      Scissor.unset();
      Scissor.pop();
      list.forEach(Runnable::run);
      this.potionhudDrag.setWidth(maxWidth);
      this.potionhudDrag.setHeight(totalHeight + 5.0F);
   }

   private String formatDuration(StatusEffectInstance eff) {
      if (!eff.isInfinite() && eff.getDuration() <= 18000) {
         String raw = StatusEffectUtil.getDurationText(eff, 1.0F, 20.0F).getString();
         return raw.replace("{", "").replace("}", "");
      } else {
         return "**:**";
      }
   }

   private void cooldown(EventRender2D eventRender2D) {
      float posX = this.itemcooldownDrag.getX();
      float posY = this.itemcooldownDrag.getY();
      int headerHeight = 18;
      int padding = 5;
      int lineHeight = 10;
      List<Item> activeItems = new ArrayList<>();
      float maxWidth = 100.0F;
      ItemCooldownManager manager = mc.player.getItemCooldownManager();
      ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)manager;

      for (Item item : TRACKED_ITEMS) {
         ItemStack stack = new ItemStack(item);
         if (manager.isCoolingDown(stack)) {
            activeItems.add(item);
            String itemName = ITEM_NAMES.getOrDefault(item, stack.getName().getString());
            Identifier id = manager.getGroup(stack);
            Object rawEntry = accessor.getEntries().get(id);
            float remainingSeconds = 0.0F;
            if (rawEntry instanceof ItemCooldownEntryAccessor entry) {
               int end = entry.getEndTick();
               int current = accessor.getTick();
               float remainingTicks = (float)end - ((float)current + mc.getRenderTickCounter().getTickDelta(true));
               remainingSeconds = Math.max(0.0F, remainingTicks / 20.0F);
            }

            String timeLeft = this.formatCooldownTime(remainingSeconds);
            float nameWidth = FontUtils.durman[13].getWidth(itemName);
            float timeWidth = FontUtils.durman[13].getWidth(timeLeft);
            float totalWidth = (float)(padding * 2 + 25) + nameWidth + (float)padding + timeWidth;
            if (totalWidth > maxWidth) {
               maxWidth = totalWidth;
            }
         }
      }

      float listHeightTarget = (float)(activeItems.size() * lineHeight);
      this.cooldownListHeightDynamic = MathUtil.fast(this.cooldownListHeightDynamic, listHeightTarget, 15.0F);
      float totalHeight = (float)headerHeight + this.cooldownListHeightDynamic;
      int alpha = this.customAlpha.get().intValue();
      if (alpha <= 240 && this.blur.get()) {
         RenderUtil.drawBlur(
            eventRender2D.getDrawContext().getMatrices(),
            posX,
            posY + (float)headerHeight - 1.0F,
            maxWidth,
            this.cooldownListHeightDynamic + 6.0F,
            new Vector4f(0.0F, 3.0F, 3.0F, 0.0F),
            12.0F,
            Color.white.getRGB()
         );
      }

      StyleManager theme = Manager.STYLE_MANAGER;
      Color upColor = new Color(theme.getFirstColor());
      Color downColor = new Color(theme.getSecondColor());
      if (this.hudColor.is("Normal")) {
         RenderUtil.drawRoundedRect(
            eventRender2D.getDrawContext().getMatrices(),
            posX,
            posY,
            maxWidth,
            (float)(headerHeight + 1),
            new Vector4f(3.0F, 0.0F, 0.0F, 3.0F),
            ColorUtil.hud_color
         );
      } else {
         int left = ColorUtil.gradient(10, 90, upColor.getRGB(), downColor.getRGB());
         int right = ColorUtil.gradient(10, 0, upColor.getRGB(), downColor.getRGB());
         int top = ColorUtil.gradient(10, 180, upColor.getRGB(), downColor.getRGB());
         int bottom = ColorUtil.gradient(10, 270, upColor.getRGB(), downColor.getRGB());
         boolean leftToRight = this.gradientType.is(I0O1l0I1.b("U29sZGFuIHNhxJ9h"));
         int c1 = leftToRight ? ColorUtil.hud_color : left;
         int c2 = leftToRight ? ColorUtil.hud_color : right;
         int c3 = leftToRight ? right : ColorUtil.hud_color;
         int c4 = leftToRight ? left : ColorUtil.hud_color;
         RenderUtil.rectRGB(eventRender2D.getDrawContext().getMatrices(), posX, posY, maxWidth, (float)(headerHeight + 1), this.corner, c1, c2, c3, c4);
      }

      RenderUtil.drawTexture(
         eventRender2D.getDrawContext().getMatrices(),
         "images/hud/cooldown.png",
         posX + maxWidth - 17.0F,
         posY + 4.5F,
         11.0F,
         11.0F,
         0.0F,
         Color.white.getRGB()
      );
      FontUtils.durman[15].drawLeftAligned(eventRender2D.getDrawContext().getMatrices(), I0O1l0I1.b("QmVrbGVtZSBTw7xyZWxlcmk="), posX + 10.0F, posY + 5.0F, -1);
      RenderUtil.drawRoundedRect(
         eventRender2D.getDrawContext().getMatrices(),
         posX,
         posY + (float)headerHeight - 1.0F,
         maxWidth,
         this.cooldownListHeightDynamic + 6.0F,
         new Vector4f(0.0F, 3.0F, 3.0F, 0.0F),
         new Color(22, 22, 22, alpha).getRGB()
      );
      Scissor.push();
      Scissor.setFromComponentCoordinates(
         (double)posX, (double)posY, (double)maxWidth, (double)((float)headerHeight + this.cooldownListHeightDynamic + (float)padding / 2.0F + 5.0F)
      );
      float yOffset = posY + (float)headerHeight + (float)padding - 1.0F;

      for (Item itemx : activeItems) {
         ItemStack stack = itemx.getDefaultStack();
         String itemNamex = ITEM_NAMES.getOrDefault(itemx, stack.getName().getString());
         Identifier idx = manager.getGroup(stack);
         Object rawEntryx = accessor.getEntries().get(idx);
         float remainingSecondsx = 0.0F;
         if (rawEntryx instanceof ItemCooldownEntryAccessor entry) {
            int end = entry.getEndTick();
            int current = accessor.getTick();
            float remainingTicks = (float)end - ((float)current + mc.getRenderTickCounter().getTickDelta(true));
            remainingSecondsx = Math.max(0.0F, remainingTicks / 20.0F);
         }

         String timeLeft = this.formatCooldownTime(remainingSecondsx);
         RenderAddon.renderItem(eventRender2D.getDrawContext(), stack, posX + (float)padding - 1.5F, yOffset - 1.0F, 0.6F, false);
         RenderUtil.drawRoundedRect(
            eventRender2D.getDrawContext().getMatrices(),
            posX + (float)padding + 10.0F,
            yOffset - 0.5F,
            1.2F,
            9.0F,
            0.0F,
            new Color(255, 255, 255, 120).getRGB()
         );
         FontUtils.durman[13].drawLeftAligned(eventRender2D.getDrawContext().getMatrices(), itemNamex, posX + (float)padding + 14.0F, yOffset - 0.3F, -1);
         float timeWidth = FontUtils.durman[13].getWidth(timeLeft);
         RenderUtil.drawRoundedRect(
            eventRender2D.getDrawContext().getMatrices(),
            posX + maxWidth - timeWidth - (float)padding - 5.0F,
            yOffset - 1.0F,
            6.0F + timeWidth,
            10.0F,
            1.0F,
            ColorUtil.hud_color
         );
         FontUtils.durman[13]
            .drawLeftAligned(eventRender2D.getDrawContext().getMatrices(), timeLeft, posX + maxWidth - timeWidth - (float)padding - 2.0F, yOffset - 0.3F, -1);
         yOffset += (float)lineHeight;
      }

      Scissor.unset();
      Scissor.pop();
      this.itemcooldownDrag.setWidth(maxWidth);
      this.itemcooldownDrag.setHeight(totalHeight + 5.0F);
   }

   private void staffList(EventRender2D render2D) {
      float posX = this.stafflistDrag.getX();
      float posY = this.stafflistDrag.getY();
      RenderFonts fontBig = FontUtils.durman[15];
      RenderFonts fontSmall = FontUtils.durman[13];
      int headerHeight = 18;
      int padding = 4;
      int offset = 10;
      float width = Math.max(this.nameWidth + 60.0F, 100.0F);
      int index = 0;
      this.nameWidth = 0.0F;
      this.hDynam = MathUtil.fast(this.hDynam, (float)(this.activeStaff * offset), 15.0F);
      this.widthDynamic = MathUtil.fast(this.widthDynamic, width, 8.0F);
      int alpha = this.customAlpha.get().intValue();
      if (alpha <= 240 && this.blur.get()) {
         RenderUtil.drawBlur(
            render2D.getDrawContext().getMatrices(),
            posX,
            posY + (float)headerHeight - 1.0F,
            this.widthDynamic,
            this.hDynam + 6.0F,
            new Vector4f(0.0F, 3.0F, 3.0F, 0.0F),
            12.0F,
            Color.white.getRGB()
         );
      }

      StyleManager theme = Manager.STYLE_MANAGER;
      Color upColor = new Color(theme.getFirstColor());
      Color downColor = new Color(theme.getSecondColor());
      if (this.hudColor.is("Normal")) {
         RenderUtil.drawRoundedRect(
            render2D.getDrawContext().getMatrices(), posX, posY, this.widthDynamic, (float)(headerHeight + 1), this.corner, ColorUtil.hud_color
         );
      } else {
         int left = ColorUtil.gradient(10, 90, upColor.getRGB(), downColor.getRGB());
         int right = ColorUtil.gradient(10, 0, upColor.getRGB(), downColor.getRGB());
         int top = ColorUtil.gradient(10, 180, upColor.getRGB(), downColor.getRGB());
         int bottom = ColorUtil.gradient(10, 270, upColor.getRGB(), downColor.getRGB());
         boolean leftToRight = this.gradientType.is(I0O1l0I1.b("U29sZGFuIHNhxJ9h"));
         int c1 = leftToRight ? ColorUtil.hud_color : left;
         int c2 = leftToRight ? ColorUtil.hud_color : right;
         int c3 = leftToRight ? right : ColorUtil.hud_color;
         int c4 = leftToRight ? left : ColorUtil.hud_color;
         RenderUtil.rectRGB(render2D.getDrawContext().getMatrices(), posX, posY, this.widthDynamic, (float)(headerHeight + 1), this.corner, c1, c2, c3, c4);
      }

      RenderUtil.drawTexture(
         render2D.getDrawContext().getMatrices(),
         "images/hud/staff.png",
         posX + this.widthDynamic - (float)headerHeight,
         posY + 4.0F,
         12.0F,
         12.0F,
         0.0F,
         Color.white.getRGB()
      );
      fontBig.drawLeftAligned(render2D.getDrawContext().getMatrices(), "Personel Listesi", posX + 10.0F, posY + 5.0F, -1);
      RenderUtil.drawRoundedRect(
         render2D.getDrawContext().getMatrices(),
         posX,
         posY + (float)headerHeight - 1.0F,
         this.widthDynamic,
         this.hDynam + 6.0F,
         new Vector4f(0.0F, 3.0F, 3.0F, 0.0F),
         new Color(22, 22, 22, alpha).getRGB()
      );
      if (!this.staffPlayers.isEmpty()) {
         Scissor.push();
         Scissor.setFromComponentCoordinates(
            (double)posX, (double)posY, (double)this.widthDynamic, (double)((float)headerHeight + this.hDynam + (float)padding / 2.0F + 5.0F)
         );
         Map<String, PlayerListEntry> playerInfoMap = new HashMap<>();

         for (PlayerListEntry info : mc.getNetworkHandler().getPlayerList()) {
            playerInfoMap.put(info.getProfile().getName(), info);
         }

         for (HUD.StaffPlayer staff : this.staffPlayers) {
            String staffname = staff.getName();
            String status = staff.getStatus().getString();
            float statusWidth = fontSmall.getWidth(status);
            float currentWidth = fontSmall.getWidth(staffname);
            if (currentWidth > this.nameWidth) {
               this.nameWidth = currentWidth;
            }

            float baseY = posY + (float)headerHeight + (float)padding + (float)(index * offset);
            PlayerListEntry playerInfo = playerInfoMap.get(staffname);
            if (playerInfo != null) {
               if (staff.getStatus() != HUD.StaffPlayer.Status.VANISHED && staff.getStatus() != HUD.StaffPlayer.Status.SPEC) {
                  RenderAddon.drawStaffHead(
                     render2D.getDrawContext().getMatrices(), playerInfo.getSkinTextures().texture(), posX + (float)padding, baseY - 1.0F, 9.0F, 3.0F
                  );
               }
            } else {
               RenderUtil.drawTexture(
                  render2D.getDrawContext().getMatrices(),
                  "images/hud/staffvanish.png",
                  posX + (float)padding,
                  baseY - 1.0F,
                  9.0F,
                  9.0F,
                  3.0F,
                  Color.white.getRGB()
               );
            }

            RenderUtil.drawRoundedRect(
               render2D.getDrawContext().getMatrices(), posX + (float)padding + 11.0F, baseY - 0.8F, 1.2F, 9.0F, 0.0F, new Color(255, 255, 255, 120).getRGB()
            );
            fontSmall.drawLeftAligned(render2D.getDrawContext().getMatrices(), staffname, posX + (float)padding + 16.0F, baseY - 0.5F, Color.WHITE.getRGB());
            fontSmall.drawLeftAligned(
               render2D.getDrawContext().getMatrices(),
               status,
               posX + this.widthDynamic - statusWidth - 4.0F,
               baseY - 1.0F,
               this.getStatusColor(staff.getStatus())
            );
            index++;
         }

         Scissor.unset();
         Scissor.pop();
      }

      this.activeStaff = index;
      this.stafflistDrag.setWidth(this.widthDynamic);
      this.stafflistDrag.setHeight(this.hDynam + (float)headerHeight + (float)padding + 1.0F);
   }

   private void targethud(EventRender2D render2D) {
      float x = this.targethudDrag.getX();
      float y = this.targethudDrag.getY();
      this.target = this.getTarget(this.target);
      this.scale = this.tHudAnimation.getOutput();
      if (this.scale != 0.0 && this.target != null) {
         float rawHealth = MathHelper.clamp(ServerUtil.getHealth(this.target), 0.0F, 1.0F);
         float rawAbsorption = MathHelper.clamp(this.target.getAbsorptionAmount(), 0.0F, 7.1F);
         this.lastHealth = MathUtil.fast(this.lastHealth, rawHealth, 8.0F);
         this.lastAbsorption = MathUtil.fast(this.lastAbsorption, rawAbsorption, 8.0F);
         String healthDisplay = String.format(Locale.ENGLISH, "%.0f", this.lastHealth * 20.0F);
         render2D.getMatrixStack().push();
         RenderAddon.sizeAnimation(render2D.getMatrixStack(), (double)(x + 60.0F), (double)(y + 17.5F), this.scale);
         RenderUtil.drawRoundedRect(render2D.getDrawContext().getMatrices(), x, y, 120.0F, 35.0F, 3.0F, ColorUtil.hud_color);
         String displayName = Manager.FUNCTION_MANAGER.nameProtect.getProtectedName(this.target.getName().getString());
         if (displayName.length() > 12) {
            displayName = displayName.substring(0, 12) + "...";
         }

         FontUtils.durman[15].drawLeftAligned(render2D.getDrawContext().getMatrices(), displayName, x + 35.0F, y + 5.0F, -1);
         RenderAddon.drawHead(render2D.getDrawContext().getMatrices(), this.target, x + 4.0F, y + 3.5F, 28.0F, this.roundingSilaSanya.get().floatValue());
         RenderUtil.drawRoundedRect(render2D.getDrawContext().getMatrices(), x + 34.2F, y + 26.0F, 82.0F, 5.0F, 0.0F, new Color(44, 41, 42, 255).getRGB());
         StyleManager theme = Manager.STYLE_MANAGER;
         Color upColor = new Color(theme.getFirstColor());
         Color downColor = new Color(theme.getSecondColor());
         Vector4i vec = new Vector4i(
            ColorUtil.gradient(5, 90, upColor.getRGB(), downColor.getRGB()),
            ColorUtil.gradient(5, 0, upColor.getRGB(), downColor.getRGB()),
            ColorUtil.gradient(5, 180, upColor.getRGB(), downColor.getRGB()),
            ColorUtil.gradient(5, 270, upColor.getRGB(), downColor.getRGB())
         );
         RenderUtil.rectRGB(render2D.getDrawContext().getMatrices(), x + 34.2F, y + 26.0F, 82.0F * this.lastHealth, 5.0F, 0.0F, vec.w, vec.x, vec.y, vec.z);
         RenderUtil.drawRoundedRect(
            render2D.getDrawContext().getMatrices(), x + 34.2F, y + 26.0F, 4.0F * this.lastAbsorption, 5.0F, 0.0F, Color.YELLOW.getRGB()
         );
         FontUtils.sf_bold[13].centeredDraw(render2D.getDrawContext().getMatrices(), healthDisplay + " HP", x + 105.0F, y + 16.0F, -1);
         List<ItemStack> stacks = new ArrayList<>();
         stacks.add(this.target.getMainHandStack());
         stacks.add(this.target.getOffHandStack());
         stacks.removeIf(i -> i.getItem() instanceof AirBlockItem || i.isEmpty());
         float renderOffset = 0.0F;

         for (ItemStack stack : stacks) {
            render2D.getDrawContext().getMatrices().push();
            render2D.getDrawContext().getMatrices().translate(x + renderOffset + 35.0F, y + 16.0F, 0.0F);
            render2D.getDrawContext().getMatrices().scale(0.5F, 0.5F, 1.0F);
            render2D.getDrawContext().drawItem(stack, 0, 0, 7, 0);
            render2D.getDrawContext().drawStackOverlay(mc.textRenderer, stack, 0, 0);
            render2D.getDrawContext().getMatrices().pop();
            renderOffset += 15.0F;
         }

         render2D.getMatrixStack().pop();
         this.targethudDrag.setWidth(120.0F);
         this.targethudDrag.setHeight(35.0F);
      }
   }

   private void waterMark(EventRender2D render2D) {
      // Sabit pozisyon (suruklenemez)
      float x = 10.0F;
      float y = 10.0F;
      String userName = mc.getGameProfile().getName();
      int fps = ClientManager.getFps();
      String ping = ClientManager.getPing();

      MatrixStack matrices = render2D.getDrawContext().getMatrices();
      RenderFonts titleFont = FontUtils.sf_bold[18];
      RenderFonts infoFont = FontUtils.sf_bold[15];
      RenderFonts moduleFont = FontUtils.sf_medium[14];
      int alpha = this.customAlpha.get().intValue();

      int accentColor = new Color(255, 140, 50).getRGB();
      int bgColor = new Color(20, 20, 28, alpha).getRGB();
      int textWhite = new Color(235, 235, 240).getRGB();
      int textGray = new Color(160, 160, 170).getRGB();

      // Box 1: Just Client by Lxrich
      String clientName = "Just Client";
      String byText = " by Lxrich";
      float box1Width = titleFont.getWidth(clientName + byText) + 24.0F;
      float box1Height = 28.0F;

      RenderUtil.drawRoundedRect(matrices, x, y, box1Width, box1Height, 6.0F, bgColor);
      RenderUtil.drawRoundedRect(matrices, x, y, 4.0F, box1Height, new Vector4f(6.0F, 0.0F, 0.0F, 6.0F), accentColor);
      titleFont.drawLeftAligned(matrices, clientName, x + 12.0F, y + 6.0F, accentColor);
      titleFont.drawLeftAligned(matrices, byText, x + 12.0F + titleFont.getWidth(clientName), y + 6.0F, textGray);

      // Box 2: Isim | FPS | Ping (yaninda)
      String infoText = userName + " | " + fps + " FPS | " + ping + " ms";
      float box2Width = infoFont.getWidth(infoText) + 20.0F;
      float box2X = x + box1Width + 6.0F;

      RenderUtil.drawRoundedRect(matrices, box2X, y, box2Width, box1Height, 6.0F, bgColor);
      infoFont.drawLeftAligned(matrices, userName, box2X + 10.0F, y + 7.0F, textWhite);
      infoFont.drawLeftAligned(matrices, " | " + fps + " FPS | " + ping + " ms", box2X + 10.0F + infoFont.getWidth(userName), y + 7.0F, textGray);

      // Aktif moduller (Just Client kutusunun altinda)
      float modulesY = y + box1Height + 4.0F;
      float modulesWidth = Math.max(box1Width, 120.0F);

      List<String[]> activeModules = new ArrayList<>();
      for (Function f : Manager.FUNCTION_MANAGER.getFunctions()) {
         if (f.state && f != this && !(f instanceof dev.just.modules.render.ClickGUI)) {
            String bindText = "";
            if (f.bind != 0) {
               bindText = " [" + this.getShortKey(ClientManager.getKey(f.bind)) + "]";
            }
            activeModules.add(new String[]{f.name, bindText});
            float itemW = moduleFont.getWidth(f.name + bindText) + 20.0F;
            if (itemW > modulesWidth) modulesWidth = itemW;
         }
      }

      if (!activeModules.isEmpty()) {
         float modulesHeight = activeModules.size() * 16.0F + 12.0F;
         RenderUtil.drawRoundedRect(matrices, x, modulesY, modulesWidth, modulesHeight, 6.0F, bgColor);
         RenderUtil.drawRoundedRect(matrices, x, modulesY, 4.0F, modulesHeight, new Vector4f(6.0F, 0.0F, 0.0F, 6.0F), accentColor);

         float itemY = modulesY + 6.0F;
         for (String[] module : activeModules) {
            moduleFont.drawLeftAligned(matrices, module[0], x + 12.0F, itemY, textWhite);
            if (!module[1].isEmpty()) {
               moduleFont.drawLeftAligned(matrices, module[1], x + 12.0F + moduleFont.getWidth(module[0]), itemY, accentColor);
            }
            itemY += 16.0F;
         }

         this.watermarkDrag.setHeight(box1Height + 4.0F + modulesHeight);
      } else {
         this.watermarkDrag.setHeight(box1Height);
      }

      this.watermarkDrag.setWidth(box1Width + 6.0F + box2Width);
   }

   private void coordinates(EventRender2D render2D) {
      int screenWidth = mc.getWindow().getScaledWidth();
      RenderFonts font = FontUtils.sf_bold[17];
      float x = this.coordinateshudDrag.getX();
      float y = this.coordinateshudDrag.getY();
      String coords = String.format(
         "X: %d, Y: %d, Z: %d", (int)mc.player.getX(), (int)mc.player.getY(), (int)mc.player.getZ()
      );
      String tpsText = "TPS: ";

      try {
         Object tpsValue = ClientManager.getTPS();
         if (tpsValue != null) {
            tpsText = tpsText + tpsValue.toString();
         } else {
            tpsText = tpsText + "N/A";
         }
      } catch (Exception var10) {
         tpsText = tpsText + "N/A";
      }

      int textWidth = (int)font.getWidth(coords);
      boolean isLeftSide = x < (float)(screenWidth / 2);
      if (isLeftSide) {
         font.drawLeftAligned(render2D.getDrawContext().getMatrices(), coords, x, y + 12.0F, Color.white.getRGB());
         font.drawLeftAligned(render2D.getDrawContext().getMatrices(), tpsText, x, y, Color.white.getRGB());
         this.coordinateshudDrag.setWidth((float)textWidth);
      } else {
         font.drawRightAligned(render2D.getDrawContext().getMatrices(), coords, x + (float)textWidth, y + 12.0F, Color.white.getRGB());
         font.drawRightAligned(render2D.getDrawContext().getMatrices(), tpsText, x + (float)textWidth, y, Color.white.getRGB());
         this.coordinateshudDrag.setWidth((float)textWidth);
      }

      this.coordinateshudDrag.setHeight(24.0F);
   }

   private void keybindHud(EventRender2D render2D) {
      float posX = this.keybindsDrag.getX();
      float posY = this.keybindsDrag.getY();
      int padding = 6;
      int lineHeight = 14;
      MatrixStack matrices = render2D.getDrawContext().getMatrices();
      RenderFonts font = FontUtils.sf_medium[12];
      int alpha = this.customAlpha.get().intValue();
      float maxWidth = 100.0F;

      int accentColor = new Color(255, 140, 50).getRGB();
      int bgColor = new Color(20, 20, 28, alpha).getRGB();

      List<String[]> activeItems = new ArrayList<>();

      for (Function f : Manager.FUNCTION_MANAGER.getFunctions()) {
         if (f.bind != 0 && f.state) {
            String bindKey = this.getShortKey(ClientManager.getKey(f.bind));
            float itemWidth = font.getWidth(f.name) + font.getWidth("[" + bindKey + "]") + 20.0F;
            if (itemWidth > maxWidth) maxWidth = itemWidth;
            activeItems.add(new String[]{f.name, bindKey});
         }

         for (Setting setting : f.getSettings()) {
            if (setting instanceof BindBooleanSetting) {
               BindBooleanSetting bindSetting = (BindBooleanSetting)setting;
               if (bindSetting.isVisible() && bindSetting.getBindKey() != 0 && bindSetting.get()) {
                  String bindKey = this.getShortKey(ClientManager.getKey(bindSetting.getBindKey()));
                  float itemWidth = font.getWidth(bindSetting.getName()) + font.getWidth("[" + bindKey + "]") + 20.0F;
                  if (itemWidth > maxWidth) maxWidth = itemWidth;
                  activeItems.add(new String[]{bindSetting.getName(), bindKey});
               }
            }
         }
      }

      if (activeItems.isEmpty()) {
         this.keybindsDrag.setWidth(0);
         this.keybindsDrag.setHeight(0);
         this.activeModules = 0;
         return;
      }

      float totalHeight = activeItems.size() * lineHeight + padding * 2 + 18;
      float width = maxWidth + padding * 2;

      RenderUtil.drawRoundedRect(matrices, posX, posY, width, totalHeight, 5.0F, bgColor);
      RenderUtil.drawRoundedRect(matrices, posX, posY, 3.0F, totalHeight, new Vector4f(5.0F, 0.0F, 0.0F, 5.0F), accentColor);

      font.drawLeftAligned(matrices, "Aktif Moduller", posX + 10.0F, posY + 5.0F, accentColor);
      RenderUtil.drawRoundedRect(matrices, posX + 8.0F, posY + 17.0F, width - 16.0F, 1.0F, 0.0F, new Color(50, 50, 60).getRGB());

      float yOffset = posY + 22.0F;
      for (String[] item : activeItems) {
         String name = item[0];
         String bindKey = "[" + item[1] + "]";
         font.drawLeftAligned(matrices, name, posX + padding + 4.0F, yOffset, new Color(210, 210, 220).getRGB());
         font.drawLeftAligned(matrices, bindKey, posX + width - font.getWidth(bindKey) - padding, yOffset, accentColor);
         yOffset += lineHeight;
      }

      this.activeModules = activeItems.size();
      this.keybindsDrag.setWidth(width);
      this.keybindsDrag.setHeight(totalHeight);
   }

   private String getShortKey(String key) {
      if (key == null) {
         return "";
      } else {
         String bindText = key.toUpperCase();
         return bindText.length() > 6 ? bindText.substring(0, 6) + "…" : bindText;
      }
   }

   public LivingEntity getTarget(LivingEntity nullTarget) {
      LivingEntity result = getTargetInternal(nullTarget);
      return result;
   }

   private LivingEntity getTargetInternal(LivingEntity nullTarget) {
      // AttackAura target
      Object auraTarget = Manager.FUNCTION_MANAGER.attackAura.target;
      if (auraTarget != null) {
         if (auraTarget instanceof LivingEntity) {
            this.tHudAnimation.setDirection(AxisDirection.POSITIVE);
            return (LivingEntity) auraTarget;
         }
      }

      // Crosshair target
      if (this.visibleCrosshair.get()) {
         Object crosshair = mc.crosshairTarget;
         if (crosshair != null && crosshair instanceof EntityHitResult) {
            Entity aimed = ((EntityHitResult) crosshair).getEntity();
            if (aimed != null && aimed instanceof LivingEntity) {
               this.tHudAnimation.setDirection(AxisDirection.POSITIVE);
               return (LivingEntity) aimed;
            }
         }
      }

      // Chat screen - show player
      if (mc.currentScreen instanceof ChatScreen) {
         this.tHudAnimation.setDirection(AxisDirection.POSITIVE);
         return mc.player;
      }

      // Default
      this.tHudAnimation.setDirection(AxisDirection.NEGATIVE);
      return nullTarget;
   }

   private String repairString(String input) {
      StringBuilder sb = new StringBuilder(input.length());

      for (char c : input.toCharArray()) {
         if (c >= '！' && c <= '～') {
            sb.append((char)(c - 'ﻠ'));
         } else {
            sb.append(c);
         }
      }

      return sb.toString();
   }

   @Override
   public void onDisable() {
      this.staffPlayers.clear();
      this.addedPlayers.clear();
   }

   private String processName(String original) {
      return original.length() <= 12 && !original.matches(".*\\d.*") ? original : original.substring(0, Math.min(9, original.length())) + "...";
   }

   private int getStatusColor(HUD.StaffPlayer.Status status) {
      switch (status) {
         case NEAR:
            return Color.ORANGE.getRGB();
         case SPEC:
            return Color.YELLOW.getRGB();
         case VANISHED:
            return Color.RED.getRGB();
         default:
            return Color.GREEN.getRGB();
      }
   }

   private String formatCooldownTime(float seconds) {
      int totalSeconds = (int)Math.floor((double)seconds);
      int minutes = totalSeconds / 60;
      int secs = totalSeconds % 60;
      if (minutes > 0) {
         return secs > 0 ? String.format("%dm %02ds", minutes, secs) : String.format("%dm", minutes);
      } else {
         return String.format("%ds", secs);
      }
   }

   static {
      Map<Item, String> tmp = new HashMap<>(16);
      tmp.put(Items.ENDER_PEARL, I0O1l0I1.b("RW5kZXIgxLBuY2lzaQ=="));
      tmp.put(Items.CHORUS_FRUIT, I0O1l0I1.b("S29ybyBNZXl2ZXNp"));
      tmp.put(Items.FIREWORK_ROCKET, I0O1l0I1.b("RmnFn2Vr"));
      tmp.put(Items.SHIELD, "Kalkan");
      tmp.put(Items.GOLDEN_APPLE, I0O1l0I1.b("QWx0xLFuIEVsbWE="));
      tmp.put(Items.ENCHANTED_GOLDEN_APPLE, I0O1l0I1.b("QsO8ecO8bMO8IEFsdMSxbiBFbG1h"));
      tmp.put(Items.TOTEM_OF_UNDYING, I0O1l0I1.b("w5Zsw7xtc8O8emzDvGsgVG90ZW1p"));
      tmp.put(Items.SNOWBALL, "Kartopu");
      tmp.put(Items.DRIED_KELP, I0O1l0I1.b("S3VydXR1bG11xZ8gWW9zdW4="));
      tmp.put(Items.ENDER_EYE, I0O1l0I1.b("RW5kZXIgR8O2esO8"));
      tmp.put(Items.NETHERITE_SCRAP, I0O1l0I1.b("TmV0aGVyaXRlIFBhcsOnYXPEsQ=="));
      tmp.put(Items.EXPERIENCE_BOTTLE, I0O1l0I1.b("VGVjcsO8YmUgxZ5pxZ9lc2k="));
      tmp.put(Items.PHANTOM_MEMBRANE, I0O1l0I1.b("SGF5YWxldCBaYXLEsQ=="));
      ITEM_NAMES = Collections.unmodifiableMap(tmp);
   }

   public class StaffPlayer {
      private final String name;
      private final Text prefix;
      private HUD.StaffPlayer.Status status;
      private final long joinTime;
      private GameMode gameMode;
      private boolean isOnPlayerList;
      private final UUID uuid;

      public StaffPlayer(String name, Text prefix, @Nullable UUID uuid) {
         this.name = name;
         this.prefix = prefix;
         this.uuid = uuid;
         this.joinTime = System.currentTimeMillis();
         this.updateStatus();
      }

      public void updateStatus() {
         if (IMinecraft.mc != null && IMinecraft.mc.world != null && IMinecraft.mc.getNetworkHandler() != null) {
            PlayerListEntry entry = null;
            if (this.uuid != null) {
               for (PlayerListEntry e : IMinecraft.mc.getNetworkHandler().getPlayerList()) {
                  if (this.uuid.equals(e.getProfile().getId())) {
                     entry = e;
                     break;
                  }
               }
            } else {
               for (PlayerListEntry ex : IMinecraft.mc.getNetworkHandler().getPlayerList()) {
                  if (ex.getProfile() != null && ex.getProfile().getName() != null && ex.getProfile().getName().equalsIgnoreCase(this.name)) {
                     entry = ex;
                     break;
                  }
               }
            }

            this.isOnPlayerList = entry != null;
            this.gameMode = entry != null ? entry.getGameMode() : null;
            boolean entityLoaded = false;
            if (entry != null) {
               PlayerEntity loaded = IMinecraft.mc.world.getPlayerByUuid(entry.getProfile().getId());
               entityLoaded = loaded != null;
            }

            if (!this.isOnPlayerList) {
               this.status = HUD.StaffPlayer.Status.VANISHED;
            } else if (this.gameMode == GameMode.SPECTATOR) {
               this.status = HUD.StaffPlayer.Status.SPEC;
            } else if (entityLoaded) {
               this.status = HUD.StaffPlayer.Status.NEAR;
            } else {
               this.status = HUD.StaffPlayer.Status.NONE;
            }
         } else {
            this.status = HUD.StaffPlayer.Status.VANISHED;
            this.isOnPlayerList = false;
            this.gameMode = null;
         }
      }

      public String getName() {
         return this.name;
      }

      public Text getPrefix() {
         return this.prefix;
      }

      public HUD.StaffPlayer.Status getStatus() {
         return this.status;
      }

      public long getJoinTime() {
         return this.joinTime;
      }

      public GameMode getGameMode() {
         return this.gameMode;
      }

      public boolean isOnPlayerList() {
         return this.isOnPlayerList;
      }

      public UUID getUuid() {
         return this.uuid;
      }

      public static enum Status {
         NONE("§2[ON]"),
         NEAR("§6[N]"),
         SPEC("§e[GM3]"),
         VANISHED("§c[V]");

         final String string;

         private Status(String string) {
            this.string = string;
         }

         public String getString() {
            return this.string;
         }
      }
   }
}
