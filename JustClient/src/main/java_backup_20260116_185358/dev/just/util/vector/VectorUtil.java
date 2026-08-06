package dev.just.util.vector;

import dev.just.manager.IMinecraft;
import dev.just.mixin.iface.GameRendererAccessor;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2f;
import net.minecraft.client.util.math.MatrixStack.Entry;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class VectorUtil implements IMinecraft {
   private static final float MIN_DISTANCE_SQ = 1.0F;
   public static Matrix4f previousProjectionMatrix = new Matrix4f();
   public static Entry lastWorldSpaceMatrix = new MatrixStack().peek();

   public static Vector3d toScreen(Vector3d vec) {
      return toScreen(vec.x, vec.y, vec.z);
   }

   public static Vector3d toScreen(double x, double y, double z) {
      if (lastWorldSpaceMatrix != null && previousProjectionMatrix != null) {
         Vector3f vector3f = new Vector3f((float)x, (float)y, (float)z);
         int[] viewport = new int[4];
         GL11.glGetIntegerv(2978, viewport);
         Vector4f vector4f = new Vector4f(vector3f.x, vector3f.y, vector3f.z, 1.0F).mul(lastWorldSpaceMatrix.getPositionMatrix());
         if (vector4f.z > 0.0F) {
            return new Vector3d(-1.0, -1.0, -1.0);
         } else {
            Vector3f target = new Vector3f();
            new Matrix4f(previousProjectionMatrix).project(vector4f.x, vector4f.y, vector4f.z, viewport, target);
            double scale = mc.getWindow().getScaleFactor();
            return new Vector3d((double)target.x / scale, (double)((float)mc.getWindow().getHeight() - target.y) / scale, (double)target.z);
         }
      } else {
         return new Vector3d(-1.0, -1.0, -1.0);
      }
   }

   public static Vector2f project(double x, double y, double z) {
      Camera camera = mc.getEntityRenderDispatcher().camera;
      if (camera == null) {
         return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
      } else {
         Vec3d camPos = camera.getPos();
         Vector3f pos = new Vector3f((float)(x - camPos.x), (float)(y - camPos.y), (float)(z - camPos.z));
         Quaternionf rotation = new Quaternionf(mc.getEntityRenderDispatcher().getRotation()).conjugate();
         pos.rotate(rotation);
         if ((Boolean)mc.options.getBobView().getValue() && mc.getCameraEntity() instanceof PlayerEntity player) {
            applyViewBobbing(player, pos);
         }

         double fov = (double)((GameRendererAccessor)mc.gameRenderer).invokeGetFov(camera, mc.getRenderTickCounter().getTickDelta(true), true);
         return calculateScreenPosition(pos, fov);
      }
   }

   private static void applyViewBobbing(PlayerEntity player, Vector3f pos) {
      float delta = mc.getRenderTickCounter().getTickDelta(true);
      float speed = MathHelper.lerp(delta, player.prevStrideDistance, player.strideDistance);
      float bob = player.upwardSpeed - player.sidewaysSpeed;
      if (bob != 0.0F) {
         float roll = MathHelper.sin((float)((double)bob * Math.PI)) * speed * 3.0F;
         pos.rotateZ(roll * (float) (Math.PI / 180.0));
         float pitch = Math.abs(MathHelper.cos((float)((double)bob * Math.PI - 0.2F)) * speed) * 5.0F;
         pos.rotateX(pitch * (float) (Math.PI / 180.0));
      }

      pos.add(
         MathHelper.sin((float)((double)bob * Math.PI)) * speed * 0.5F,
         -Math.abs(MathHelper.cos((float)((double)bob * Math.PI)) * speed),
         0.0F
      );
   }

   private static Vector2f calculateScreenPosition(Vector3f pos, double fov) {
      if (pos.z >= 0.0F) {
         return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
      } else {
         Window w = mc.getWindow();
         float halfW = (float)w.getScaledWidth() * 0.5F;
         float halfH = (float)w.getScaledHeight() * 0.5F;
         float factor = (float)((double)halfH / Math.tan(Math.toRadians(fov) * 0.5)) / -pos.z;
         return new Vector2f(halfW + pos.x * factor, halfH - pos.y * factor);
      }
   }

   public static Vec3d getInterpolatedPos(Entity entity, float tickDelta) {
      return new Vec3d(
         MathHelper.lerp((double)tickDelta, entity.prevX, entity.getX()),
         MathHelper.lerp((double)tickDelta, entity.prevY, entity.getY()),
         MathHelper.lerp((double)tickDelta, entity.prevZ, entity.getZ())
      );
   }

   public static Vec3d getInterpolatedPos(Vec3d prev, Vec3d pos, float tickDelta) {
      return new Vec3d(
         MathHelper.lerp((double)tickDelta, prev.x, pos.getX()),
         MathHelper.lerp((double)tickDelta, prev.y, pos.getY()),
         MathHelper.lerp((double)tickDelta, prev.z, pos.getZ())
      );
   }
}
