package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.render.EventRender3D;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "BlockESP",
   desc = "U2FuZMSxaywgZsSxcsSxbiwgc3Bhd25lciB2ZSBkacSfZXIgYmxva2xhcsSxbiB5ZXJpbmkgZ8O2c3Rlcmly",
   type = Type.Render
)
public class BlockESP extends Function {
   private final SliderSetting radius = new SliderSetting(I0O1l0I1.b("WWFyxLHDp2Fw"), 20.0, 1.0, 30.0, 1.0);
   private final BooleanSetting chest = new BooleanSetting(I0O1l0I1.b("U2FuZMSxa2xhcg=="), true);
   private final BooleanSetting furnace = new BooleanSetting(I0O1l0I1.b("RsSxcsSxbmxhcg=="), true);
   private final BooleanSetting spawner = new BooleanSetting(I0O1l0I1.b("U3Bhd25lcmxhcg=="), true);
   private final BooleanSetting brewingStand = new BooleanSetting(I0O1l0I1.b("xLBrc2lyIFN0YW5kbGFyxLE="), true);
   private final BooleanSetting enderChest = new BooleanSetting(I0O1l0I1.b("RW5kZXIgU2FuZMSxa2xhcsSx"), true);
   private final BooleanSetting detectorRail = new BooleanSetting(I0O1l0I1.b("RGVkZWt0w7ZyIFJheWxhcg=="), true);
   private final Map<String, Color> customBlocks = new HashMap<>();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public BlockESP() {
      this.addSettings(new Setting[]{this.radius, this.chest, this.furnace, this.spawner, this.brewingStand, this.enderChest, this.detectorRail});
   }

   public void addCustomBlock(String blockId, Color color) {
      Block block = (Block)Registries.BLOCK.get(Identifier.of(blockId));
      if (block != Blocks.AIR) {
         this.customBlocks.put(blockId, color);
      }
   }

   public void removeCustomBlock(String blockId) {
      this.customBlocks.remove(blockId);
   }

   public Map<String, Color> getCustomBlocks() {
      return this.customBlocks;
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
               if (!(event instanceof EventRender3D)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handleRenderInternal();
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

   private void handleRenderInternal() {
      l1O0I1lO.fakeHandler();
      int r = this.radius.get().intValue();
      BlockPos playerPos = mc.player.getBlockPos();

      for (int x = playerPos.getX() - r; x <= playerPos.getX() + r; x++) {
         for (int y = playerPos.getY() - r; y <= playerPos.getY() + r; y++) {
            for (int z = playerPos.getZ() - r; z <= playerPos.getZ() + r; z++) {
               BlockPos pos = new BlockPos(x, y, z);
               BlockState state = mc.world.getBlockState(pos);
               Box box = new Box(pos).contract(lO1I0l1O.d(0.01));
               if (!this.chest.get() || state.getBlock() != Blocks.CHEST && state.getBlock() != Blocks.TRAPPED_CHEST) {
                  if (!this.furnace.get()
                     || state.getBlock() != Blocks.FURNACE
                        && state.getBlock() != Blocks.BLAST_FURNACE
                        && state.getBlock() != Blocks.SMOKER) {
                     if (l1O0I1lO.opaqueTrue() && this.spawner.get() && state.getBlock() == Blocks.SPAWNER) {
                        RenderUtil.render3D.drawHoleOutline(box, new Color(255, 0, 255, 150).getRGB(), lO1I0l1O.f(2.0F));
                     } else if (this.brewingStand.get() && state.getBlock() == Blocks.BREWING_STAND) {
                        RenderUtil.render3D.drawHoleOutline(box, new Color(0, 191, 255, 150).getRGB(), lO1I0l1O.f(2.0F));
                     } else if (this.enderChest.get() && state.getBlock() == Blocks.ENDER_CHEST) {
                        RenderUtil.render3D.drawHoleOutline(box, new Color(75, 0, 130, 150).getRGB(), lO1I0l1O.f(2.0F));
                     } else if (this.detectorRail.get() && state.getBlock() == Blocks.DETECTOR_RAIL) {
                        RenderUtil.render3D.drawHoleOutline(box, new Color(255, 165, 0, 150).getRGB(), lO1I0l1O.f(2.0F));
                     } else {
                        String id = Registries.BLOCK.getId(state.getBlock()).toString();
                        Color c = this.customBlocks.get(id);
                        if (c != null) {
                           RenderUtil.render3D.drawHoleOutline(box, c.getRGB(), lO1I0l1O.f(2.0F));
                        }
                     }
                  } else {
                     RenderUtil.render3D.drawHoleOutline(box, new Color(128, 128, 128, 150).getRGB(), lO1I0l1O.f(2.0F));
                  }
               } else {
                  RenderUtil.render3D.drawHoleOutline(box, new Color(139, 69, 19, 150).getRGB(), lO1I0l1O.f(2.0F));
               }
            }
         }
      }
   }
}
