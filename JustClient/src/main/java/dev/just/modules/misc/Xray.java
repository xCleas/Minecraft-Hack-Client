package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.events.impl.render.EventRender3D;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.block.BlockState;

@FunctionAnnotation(
   name = "Xray",
   desc = "Madenlerin yerini duvarlar\u0131n arkas\u0131ndan g\u00f6rmenizi sa\u011flar",
   type = Type.Misc
)
public class Xray extends Function {
   public static SliderSetting radius = new SliderSetting("Yar\u0131\u00e7ap", 20.0, 1.0, 30.0, 1.0);
   public static BooleanSetting ancient = new BooleanSetting("Netherite", true);
   public static BooleanSetting diamond = new BooleanSetting("Elmas", true);
   public static BooleanSetting emerald = new BooleanSetting("Z\u00fcmr\u00fct", true);
   public static BooleanSetting gold = new BooleanSetting("Alt\u0131n", true);
   public static BooleanSetting iron = new BooleanSetting("Demir", true);
   public static BooleanSetting coal = new BooleanSetting("K\u00f6m\u00fcr", true);
   public static BooleanSetting redstone = new BooleanSetting("K\u0131z\u0131lta\u015f", true);
   public static BooleanSetting lapise = new BooleanSetting("Lapis", true);

   private static final int C_GREEN = 0x00FF00;
   private static final int C_CYAN = 0x00FFFF50;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public Xray() {
      this.addSettings(new Setting[]{radius, ancient, diamond, emerald, gold, iron, coal, redstone, lapise});
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
               if (event instanceof EventRender3D) {
                  processBlocksInternal();
               }
               _s = 5;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, FAKE_STATE);
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               l1O0I1lO.fakeBranch(entropy, _s);
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

   private void processBlocksInternal() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);
      int iRange = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               float range = radius.get().floatValue();
               iRange = lO1I0l1O.i((int) range);
               _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  iRange = lO1I0l1O.i(999);
                  _s = 4;
                  break;
               }
               _s = 2;
               break;

            case 2:
               iterateBlocksInternal(iRange);
               _s = 4;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(iRange, entropy);
               }
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   private void iterateBlocksInternal(int range) {
      l1O0I1lO.fakeHandler();

      double px = mc.player.getX();
      double py = mc.player.getY();
      double pz = mc.player.getZ();

      for (int x = (int)(px - range); x <= px + range; x++) {
         for (int y = (int)(py - range); y <= py + range; y++) {
            for (int z = (int)(pz - range); z <= pz + range; z++) {
               processBlockInternal(x, y, z);
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= x ^ y ^ z;
               }
            }
         }
      }
   }

   private void processBlockInternal(int x, int y, int z) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 6);
      BlockPos pos = null;
      Block block = null;
      int color = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               pos = new BlockPos(x, y, z);
               _s = 1;
               break;

            case 1:
               BlockState state = mc.world.getBlockState(pos);
               block = state.getBlock();
               _s = 2;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  _s = 5;
                  break;
               }
               _s = 3;
               break;

            case 3:
               Box box = new Box(pos).contract(lO1I0l1O.d(0.01));
               color = getOreColorInternal(block);
               if (color != lO1I0l1O.i(0)) {
                  renderOreInternal(box, color);
               }
               _s = 5;
               break;

            case 4:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(pos, block);
               }
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

   private int getOreColorInternal(Block block) {
      int _s = O1lI0O1l.next(hashCode() ^ 0x9E37, 10);
      int result = lO1I0l1O.i(0);

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (I1lO0l1I.and(ancient.get(), checkOreInternal(block, Blocks.ANCIENT_DEBRIS, null))) {
                  result = Color.green.getRGB();
                  _s = 9;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (I1lO0l1I.and(diamond.get(), checkOreInternal(block, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE))) {
                  result = new Color(0, 255, 255, lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (I1lO0l1I.and(emerald.get(), checkOreInternal(block, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE))) {
                  result = new Color(0, lO1I0l1O.i(128), 0, lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 3;
               break;

            case 3:
               if (I1lO0l1I.and(gold.get(), checkOreInternal(block, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE))) {
                  result = new Color(255, 255, 0, lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 4;
               break;

            case 4:
               if (I1lO0l1I.and(iron.get(), checkOreInternal(block, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE))) {
                  result = new Color(lO1I0l1O.i(192), lO1I0l1O.i(192), lO1I0l1O.i(192), lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 5;
               break;

            case 5:
               if (I1lO0l1I.and(coal.get(), checkOreInternal(block, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE))) {
                  result = new Color(0, 0, 0, lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 6;
               break;

            case 6:
               if (I1lO0l1I.and(redstone.get(), checkOreInternal(block, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE))) {
                  result = new Color(255, 0, 0, lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 7;
               break;

            case 7:
               if (I1lO0l1I.and(lapise.get(), checkOreInternal(block, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE))) {
                  result = new Color(0, 0, 255, lO1I0l1O.i(80)).getRGB();
                  _s = 9;
                  break;
               }
               _s = 9;
               break;

            case 8:
               if (l1O0I1lO.opaqueFalse()) {
                  result = lO1I0l1O.i(0xFFFFFF);
               }
               _s = 9;
               break;

            case 9:
               return result;

            default:
               _s = 9;
               break;
         }
      }
   }

   private int checkOreFlag(Block block, Block ore1, Block ore2) {
      l1O0I1lO.fakeHandler();

      if (block == ore1) return lO1I0l1O.i(1);
      if (I1lO0l1I.and(ore2 != null, block == ore2)) return lO1I0l1O.i(1);

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(block == Blocks.BEDROCK);
      }

      return lO1I0l1O.i(0);
   }

   private boolean checkOreInternal(Block block, Block ore1, Block ore2) {
      return lO1I0l1O.unbool(checkOreFlag(block, ore1, ore2));
   }

   private void renderOreInternal(Box box, int color) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueTrue()) {
         RenderUtil.render3D.drawHoleOutline(box, color, lO1I0l1O.f(2.0F));
      }

      if (l1O0I1lO.opaqueFalse()) {
         entropy ^= color;
      }
   }
}
