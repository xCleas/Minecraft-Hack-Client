package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.render.EventRender2D;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.providers.ResourceProvider;
import dev.just.util.vector.EntityPosition;
import dev.just.util.vector.VectorUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Formatting;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PlayerHeadItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.client.font.TextRenderer;
import org.joml.Vector3d;

@FunctionAnnotation(
   name = "NameTags",
   desc = "T3l1bmN1bGFyxLFuIHZlIGXFn3lhbGFyxLFuIMO8emVyaW5kZSBnZWxpxZ9tacWfIGJpbGdpIGV0aWtldGxlcmkgZ8O2c3Rlcmly",
   type = Type.Render
)
public class NameTags extends Function {
   public final MultiSetting tags = new MultiSetting(Strings.b("VmFybMSxa2xhcg=="), Arrays.asList(Strings.b("T3l1bmN1bGFy")), new String[]{Strings.b("T3l1bmN1bGFy"), Strings.b("WWVyZGVraSBFxZ95YWxhcg==")});
   private final BooleanSetting armorRender = new BooleanSetting(Strings.b("RcWfeWFsYXLEsSBHw7ZzdGVy"), true, () -> this.tags.get(Strings.b("T3l1bmN1bGFy")));
   private final BooleanSetting effectRender = new BooleanSetting(Strings.b("RWZla3RsZXJpIEfDtnN0ZXI="), true, () -> this.tags.get(Strings.b("T3l1bmN1bGFy")));
   private final BooleanSetting enchantRender = new BooleanSetting(Strings.b("QsO8ecO8bGVyaSBHw7ZzdGVy"), true, () -> this.tags.get(Strings.b("T3l1bmN1bGFy")));
   private final BooleanSetting sphereRender = new BooleanSetting(Strings.b("w5Z6ZWwgxLBzaW1sZXJpIEfDtnN0ZXI="), true, () -> this.tags.get(Strings.b("T3l1bmN1bGFy")));
   private final BooleanSetting shulkerCheck = new BooleanSetting(Strings.b("U2h1bGtlciDEsGNlcmnEn2luaSBHw7ZzdGVy"), true, () -> this.tags.get(Strings.b("WWVyZGVraSBFxZ95YWxhcg==")));

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();
   private static final int BG_COLOR = new Color(30, 30, 30, 150).getRGB();
   private static final Set<String> IMPORTANT_ENCHANTS;
   static {
      Set<String> tmp = new java.util.HashSet<>();
      tmp.add("Protection");
      tmp.add("Koruma");
      tmp.add("Unbreaking");
      tmp.add(Strings.b("S8SxcsSxbG1hemzEsWs="));
      tmp.add("Looting");
      tmp.add("Ganimet");
      tmp.add("Fortune");
      tmp.add("Servet");
      tmp.add("Efficiency");
      tmp.add("Verimlilik");
      tmp.add("Power");
      tmp.add(Strings.b("R8O8w6c="));
      tmp.add("Feather Falling");
      tmp.add(Strings.b("VMO8ecO8bXPDvA=="));
      tmp.add("Thorns");
      tmp.add("Dikenler");
      tmp.add("Silk Touch");
      tmp.add(Strings.b("xLBwZWtzaQ=="));
      tmp.add("Respiration");
      tmp.add(Strings.b("U29sdW5nYcOn"));
      tmp.add("Mending");
      tmp.add("Tamir");
      tmp.add("Knockback");
      tmp.add("Savurma");
      tmp.add("Curse of Vanishing");
      tmp.add("Kaybolma Laneti");
      IMPORTANT_ENCHANTS = java.util.Collections.unmodifiableSet(tmp);
   }

   public NameTags() {
      this.addSettings(new Setting[]{this.tags, this.armorRender, this.effectRender, this.enchantRender, this.sphereRender, this.shulkerCheck});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventRender2D)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handleRenderInternal((EventRender2D) event);
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
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

   private void handleRenderInternal(EventRender2D e) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue() && this.tags.get(Strings.b("T3l1bmN1bGFy"))) {
         this.renderPlayers(e);
      }

      if (FlowObfuscator.opaqueTrue() && this.tags.get(Strings.b("WWVyZGVraSBFxZ95YWxhcg=="))) {
         this.renderItems(e);
      }
   }

   private void renderPlayers(EventRender2D e) {
      int screenW = mc.getWindow().getScaledWidth();
      int screenH = mc.getWindow().getScaledHeight();
      float tickDelta = e.getDeltatick().getTickDelta(true);
      MatrixStack matrixStack = e.getMatrixStack();

      for (PlayerEntity player : Manager.SYNC_MANAGER.getPlayers()) {
         if (player != null && (!(player instanceof ClientPlayerEntity) || !mc.options.getPerspective().isFirstPerson())) {
            Vector3d vec = VectorUtil.toScreen(EntityPosition.get(player, 2.0F, tickDelta));
            if (!(vec.z < 0.0) && !(vec.x < 0.0) && !(vec.x > (double)screenW) && !(vec.y < 0.0) && !(vec.y > (double)screenH)) {
               boolean isClientUser = Manager.FUNCTION_MANAGER.globals.isClientUserCache.containsKey(player.getUuid());
               String friendPrefix = Manager.FRIEND_MANAGER.isFriend(player.getName().getString())
                  ? Formatting.GRAY + "[" + Formatting.GREEN + "F" + Formatting.GRAY + "] "
                  : "";
               float health = player.getHealth() + player.getAbsorptionAmount();
               String hpText = Formatting.GRAY
                  + " ["
                  + (health < 300.0F ? Formatting.RED.toString() + (int)health : Formatting.RED + "Unknown")
                  + Formatting.GRAY
                  + "]"
                  + Formatting.RESET;
               String name = Manager.FUNCTION_MANAGER.nameProtect.getProtectedName(player.getGameProfile().getName());
               Text prefix = (Text)(player.getScoreboardTeam() != null ? player.getScoreboardTeam().getPrefix() : Text.literal(""));
               Text itemText = null;
               if (this.sphereRender.get()) {
                  ItemStack offHand = player.getOffHandStack();
                  if (!offHand.isEmpty() && (offHand.getItem() == Items.TOTEM_OF_UNDYING || offHand.getItem() instanceof PlayerHeadItem)) {
                     Text customName = offHand.getCustomName();
                     if (customName != null) {
                        itemText = customName;
                     }
                  }
               }

               float iconWidth = isClientUser ? 12.0F : 0.0F;
               float friendWidth = (float)mc.textRenderer.getWidth(Text.literal(friendPrefix)) * 0.7F;
               float prefixWidth = (float)mc.textRenderer.getWidth(prefix) * 0.7F;
               float nameHpWidth = FontUtils.durman[13].getWidth(name + hpText);
               float itemWidth = itemText != null ? (float)mc.textRenderer.getWidth(itemText) * 0.7F + 3.0F : 0.0F;
               float totalWidth = iconWidth + friendWidth + prefixWidth + nameHpWidth + itemWidth;
               float x = (float)vec.x - (totalWidth + 8.0F) / 2.0F;
               float y = (float)vec.y - 14.0F - 1.0F;
               RenderUtil.drawRoundedRect(matrixStack, x, y, totalWidth + 8.0F, 12.0F, 1.5F, BG_COLOR);
               MatrixStack matrices = e.getDrawContext().getMatrices();
               matrices.push();
               matrices.translate(x + 4.0F, y + 3.2F, 0.0F);
               matrices.scale(0.7F, 0.7F, 1.0F);
               int dx = 0;
               if (isClientUser) {
                  RenderUtil.drawTexture(matrices, "images/hud/tags.png", (float)dx, -2.0F, 12.0F, 12.0F, 0.0F, Color.white.getRGB());
                  dx = (int)((float)dx + iconWidth / 0.7F + 2.0F);
               }

               if (!friendPrefix.isEmpty()) {
                  mc.textRenderer
                     .draw(
                        Text.literal(friendPrefix),
                        (float)dx,
                        0.0F,
                        -1,
                        false,
                        matrices.peek().getPositionMatrix(),
                        mc.getBufferBuilders().getEntityVertexConsumers(),
                        TextLayerType.NORMAL,
                        0,
                        15728880
                     );
                  dx = (int)((float)dx + friendWidth / 0.7F);
               }

               mc.textRenderer
                  .draw(
                     prefix,
                     (float)dx,
                     0.0F,
                     -1,
                     false,
                     matrices.peek().getPositionMatrix(),
                     mc.getBufferBuilders().getEntityVertexConsumers(),
                     TextLayerType.NORMAL,
                     0,
                     15728880
                  );
               dx = (int)((float)dx + prefixWidth / 0.7F);
               matrices.pop();
               FontUtils.durman[13].drawLeftAligned(matrixStack, name + hpText, x + 4.0F + iconWidth + friendWidth + prefixWidth, y + 1.8F, -1);
               if (itemText != null) {
                  matrices.push();
                  matrices.translate(x + 4.0F + iconWidth + friendWidth + prefixWidth + nameHpWidth + 3.0F, y + 3.5F, 0.0F);
                  matrices.scale(0.7F, 0.7F, 1.0F);
                  mc.textRenderer
                     .draw(
                        itemText,
                        0.0F,
                        0.0F,
                        -1,
                        false,
                        matrices.peek().getPositionMatrix(),
                        mc.getBufferBuilders().getEntityVertexConsumers(),
                        TextLayerType.NORMAL,
                        0,
                        15728880
                     );
                  matrices.pop();
               }

               if (this.effectRender.get()) {
                  this.renderEffect(e, player);
               }

               if (this.armorRender.get()) {
                  this.renderPlayerItems(e, x + 5.0F, y, player);
               }
            }
         }
      }
   }

   private void renderEffect(EventRender2D e, PlayerEntity player) {
      Vector3d footPos = VectorUtil.toScreen(EntityPosition.get(player, 0.0F, e.getDeltatick().getTickDelta(true)));
      if (!(footPos.z < 0.0)) {
         int offsetY = 5;

         for (StatusEffectInstance effect : player.getStatusEffects()) {
            String name = I18n.translate(((StatusEffect)effect.getEffectType().value()).getName().getString(), new Object[0]);
            int lvl = effect.getAmplifier() + 1;
            int sec = effect.getDuration() / 20;
            String text = Formatting.WHITE
               + name
               + (lvl > 1 ? " " + lvl : "")
               + Formatting.WHITE
               + " | "
               + sec / 60
               + ":"
               + String.format("%02d", sec % 60);
            FontUtils.durman[14]
               .centeredDraw(e.getDrawContext().getMatrices(), text, (float)footPos.x, (float)footPos.y + (float)offsetY, Color.white.getRGB());
            offsetY += 9;
         }
      }
   }

   private void renderPlayerItems(EventRender2D e, float x, float y, PlayerEntity player) {
      List<ItemStack> stacks = new ArrayList<>(6);
      stacks.add(player.getMainHandStack());
      player.getArmorItems().forEach(stacks::add);
      stacks.add(player.getOffHandStack());
      stacks.removeIf(i -> i.isEmpty() || i.getItem() instanceof AirBlockItem);
      float offset = 0.0F;

      for (ItemStack stack : stacks) {
         RenderAddon.renderItem(e.getDrawContext(), stack, x + offset - 3.0F, y - 18.0F, 0.8F, true);
         if (this.enchantRender.get() && !stack.getEnchantments().isEmpty()) {
            List<Entry<RegistryEntry<Enchantment>>> enchantments = new ArrayList<>(stack.getEnchantments().getEnchantmentEntries());
            enchantments.removeIf(entryx -> {
               Text name = Enchantment.getName((RegistryEntry)entryx.getKey(), entryx.getIntValue());
               String full = name.getString();
               return IMPORTANT_ENCHANTS.stream().noneMatch(full::contains);
            });
            if (!enchantments.isEmpty()) {
               int totalHeight = enchantments.size() * 8;
               int startY = (int)(y - 18.0F - (float)totalHeight);
               MatrixStack matrices = e.getMatrixStack();

               for (Entry<RegistryEntry<Enchantment>> entry : enchantments) {
                  RegistryEntry<Enchantment> regEntry = (RegistryEntry<Enchantment>)entry.getKey();
                  int level = entry.getIntValue();
                  Text enchantText = Enchantment.getName(regEntry, level);
                  String display = this.getShortName(enchantText, level);
                  matrices.push();
                  matrices.translate(x + offset, (float)startY, 0.0F);
                  matrices.scale(0.7F, 0.7F, 1.0F);
                  FontUtils.durman[14].drawLeftAligned(e.getDrawContext().getMatrices(), display, 0.0F, 0.0F, Color.white.getRGB());
                  matrices.pop();
                  startY += 8;
               }
            }
         }

         offset += 15.0F;
      }
   }

   private String getShortName(Text description, int level) {
      String full = description.getString();
      String[] words = full.split(" ");
      String shortName;
      if (words.length == 1) {
         shortName = words[0].substring(0, Math.min(2, words[0].length())).toUpperCase();
      } else {
         shortName = "";

         for (String w : words) {
            if (!w.isEmpty()) {
               shortName = shortName + w.charAt(0);
            }
         }

         shortName = shortName.toUpperCase();
      }

      return shortName + " " + level;
   }

   private void renderItems(EventRender2D e) {
      float tickDelta = e.getDeltatick().getTickDelta(true);
      int screenW = mc.getWindow().getScaledWidth();
      int screenH = mc.getWindow().getScaledHeight();

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)entity;
            ItemStack stack = itemEntity.getStack();
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
               BlockItem bi = (BlockItem)item;
               if (bi.getBlock() instanceof ShulkerBoxBlock) {
                  Vector3d vec = VectorUtil.toScreen(EntityPosition.get(entity, 0.6F, tickDelta));
                  if (vec.z < 0.0 || vec.x < 0.0 || vec.x > (double)screenW || vec.y < 0.0 || vec.y > (double)screenH) {
                     continue;
                  }

                  int offsetX = (int)vec.x;
                  int offsetY = (int)vec.y;
                  this.renderShulkerToolTip(e.getDrawContext(), offsetX, offsetY, stack);
               }
            }

            Vector3d vec = VectorUtil.toScreen(EntityPosition.get(entity, 0.6F, tickDelta));
            if (!(vec.z < 0.0) && !(vec.x < 0.0) && !(vec.x > (double)screenW) && !(vec.y < 0.0) && !(vec.y > (double)screenH)) {
               String name = itemEntity.getName().getString();
               int count = stack.getCount();
               if (count > 1) {
                  name = name + " [x" + Formatting.RED + count + Formatting.WHITE + "]";
               }

               float width = FontUtils.sf_bold[15].getWidth(name);
               float height = FontUtils.sf_bold[15].getHeight();
               float x = (float)vec.x - width / 2.0F - 3.0F;
               float y = (float)vec.y;
               RenderUtil.drawRoundedRect(e.getMatrixStack(), x, y, width + 6.0F, height + 2.0F, 1.5F, BG_COLOR);
               FontUtils.sf_bold[15].centeredDraw(e.getDrawContext().getMatrices(), name, (float)vec.x, y + 0.3F, Color.WHITE.getRGB());
            }
         }
      }
   }

   public boolean renderShulkerToolTip(DrawContext context, int offsetX, int offsetY, ItemStack stack) {
      ContainerComponent compoundTag = (ContainerComponent)stack.get(DataComponentTypes.CONTAINER);
      if (compoundTag != null && !compoundTag.copyFirstStack().isEmpty()) {
         this.draw(context, compoundTag.stream().toList(), offsetX, offsetY);
         return true;
      } else {
         return false;
      }
   }

   private void draw(DrawContext context, List<ItemStack> itemStacks, int offsetX, int offsetY) {
      int columns = 9;
      int rows = 3;
      float itemSize = 16.0F;
      float spacing = 0.1F;
      int paddingX = 8;
      int paddingY = 7;
      offsetX += 8;
      offsetY -= 75;
      int bgWidth = 160;
      int bgHeight = 62;
      RenderUtil.drawTexture(
         context.getMatrices(), ResourceProvider.container, (float)(offsetX - 8), (float)(offsetY - 7), (float)bgWidth, (float)bgHeight, 0.0F, -1
      );

      for (int index = 0; index < itemStacks.size(); index++) {
         int row = index / 9;
         int col = index % 9;
         float x = (float)offsetX + (float)col * 16.1F;
         float y = (float)offsetY + (float)row * 16.1F;
         RenderAddon.renderItem(context, itemStacks.get(index), x, y, 0.85F, true);
      }
   }
}
