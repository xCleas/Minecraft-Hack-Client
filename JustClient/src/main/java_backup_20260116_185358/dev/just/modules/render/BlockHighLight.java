package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.render.EventRender3D;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.Render3DUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "BlockHighLight",
   desc = "QmFrdMSxxJ/EsW7EsXogYmxvxJ91IHZ1cmd1bGF5YXJhayBiZWxpcmdpbmxlxZ90aXJpcg==",
   type = Type.Render
)
public class BlockHighLight extends Function {
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

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
               if (!(event instanceof EventRender3D)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handleRenderInternal((EventRender3D) event);
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

   private void handleRenderInternal(EventRender3D e) {
      FlowObfuscator.fakeHandler();
      if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;
      if (result.getType() != HitResult.Type.BLOCK) return;

      BlockPos pos = result.getBlockPos();
      if (FlowObfuscator.opaqueTrue() && pos != null) {
         Render3DUtil.drawShapeAlternative(
            pos, mc.world.getBlockState(pos).getOutlineShape(mc.world, pos), ColorUtil.getColorStyle(NumberGuard.f(360.0F)), NumberGuard.f(2.0F), true, true
         );
      }
   }
}
