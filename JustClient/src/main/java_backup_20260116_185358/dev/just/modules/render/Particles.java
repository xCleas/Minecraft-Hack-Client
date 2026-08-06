package dev.just.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.player.EventAttack;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.providers.ResourceProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.VertexFormat;
import org.joml.Matrix4f;

@FunctionAnnotation(
   name = "Particles",
   desc = "SGF2YWRhIHXDp3XFn2FuIGVzdGV0aWsgcGFyw6dhY8Sxa2xhciBla2xlcg==",
   type = Type.Render
)
public class Particles extends Function {
   private final MultiSetting types = new MultiSetting(Strings.b("VGlwbGVy"), Arrays.asList(Strings.b("SGFzYXI="), Strings.b("RMO8bnlh")), new String[]{Strings.b("SGFzYXI="), Strings.b("RMO8bnlh")});
   private final ModeSetting mode = new ModeSetting(
      Strings.b("RG9rdQ=="), Strings.b("WcSxbGTEsXo="), Strings.b("VGHDpw=="), Strings.b("RG9sYXI="), Strings.b("QXRlxZ8gQsO2Y2XEn2k="), Strings.b("S2FscA=="), Strings.b("xZ5pbcWfZWs="), Strings.b("w4dpemhp"), Strings.b("Tm9rdGE="), Strings.b("Um9tYg=="), Strings.b("S2FyIFRhbmVzaQ=="), Strings.b("S8SxdsSxbGPEsW0="), Strings.b("WcSxbGTEsXo=")
   );
   private final SliderSetting countDamage = new SliderSetting(Strings.b("VnVydcWfIFBhcsOnYWPEsWsgU2F5xLFzxLE="), 20.0, 5.0, 50.0, 1.0, () -> this.types.get(Strings.b("SGFzYXI=")));
   private final SliderSetting countWorld = new SliderSetting(Strings.b("RMO8bnlhIFBhcsOnYWPEsWsgU2F5xLFzxLE="), 12.0, 2.0, 15.0, 1.0, () -> this.types.get(Strings.b("RMO8bnlh")));
   private final SliderSetting sizeDamage = new SliderSetting(Strings.b("VnVydcWfIFBhcsOnYWPEsWsgQm95dXR1"), 0.3F, 0.1F, 0.6F, 0.1F, () -> this.types.get(Strings.b("SGFzYXI=")));
   private final SliderSetting sizeWorld = new SliderSetting(Strings.b("RMO8bnlhIFBhcsOnYWPEsWsgQm95dXR1"), 1.1F, 0.1F, 1.2F, 0.1F, () -> this.types.get(Strings.b("RMO8bnlh")));
   private final SliderSetting sila = new SliderSetting(Strings.b("WWF5xLFsbWEgR8O8Y8O8"), 0.2F, 0.1F, 0.5, 0.1F);
   private final SliderSetting time = new SliderSetting(Strings.b("WWHFn2FtIFPDvHJlc2k="), 4000.0, 500.0, 8000.0, 100.0);
   private final SliderSetting speedMultiplier = new SliderSetting(Strings.b("SMSxeg=="), 1.2F, 0.1F, 3.0, 0.1F);
   private final BooleanSetting randomRotation = new BooleanSetting(Strings.b("UmFzdGdlbGUgRMO2bsO8xZ8="), true);
   private final ArrayList<Particles.World> worldParticles = new ArrayList<>();
   private final ArrayList<Particles.Damage> damageParticles = new ArrayList<>();
   private static final Map<String, Identifier> TEXTURES = new HashMap<>();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Particles() {
      this.addSettings(
         new Setting[]{
            this.types,
            this.mode,
            this.countDamage,
            this.countWorld,
            this.sizeDamage,
            this.sizeWorld,
            this.sila,
            this.time,
            this.speedMultiplier,
            this.randomRotation
         }
      );
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 6;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 7);
               if (_s == 0) _s = 1;
               break;

            case 1:
               handleWorldParticles(event);
               _s = 2;
               break;

            case 2:
               handleDamageParticles(event);
               _s = 6;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
               _s = 6;
               break;

            case 5:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.l(entropy) ^ FAKE_STATE;
               }
               _s = 6;
               break;

            case 6:
               return;

            default:
               _s = 6;
               break;
         }
      }
   }

   private void handleWorldParticles(Event event) {
      FlowObfuscator.fakeHandler();
      if (!this.types.get(Strings.b("RMO8bnlh"))) return;

      if (event instanceof EventUpdate) {
         this.worldParticles.removeIf(Particles.World::tick);
         float con = this.countWorld.get().floatValue() * NumberGuard.f(100.0F);

         for (int j = this.worldParticles.size(); (float)j < con; j++) {
            boolean drop = false;
            if (FlowObfuscator.opaqueTrue()) {
               this.worldParticles
                  .add(
                     new Particles.World(
                        (float)(mc.player.getX() + (double)MathUtil.random(NumberGuard.f(-48.0F), NumberGuard.f(48.0F))),
                        (float)(mc.player.getY() + (double)MathUtil.random(NumberGuard.f(2.0F), NumberGuard.f(48.0F))),
                        (float)(mc.player.getZ() + (double)MathUtil.random(NumberGuard.f(-48.0F), NumberGuard.f(48.0F))),
                        drop ? 0.0F : MathUtil.random(NumberGuard.f(-0.4F), NumberGuard.f(0.4F)),
                        drop ? MathUtil.random(NumberGuard.f(-0.2F), NumberGuard.f(-0.05F)) : MathUtil.random(NumberGuard.f(-0.1F), NumberGuard.f(0.1F)),
                        drop ? 0.0F : MathUtil.random(NumberGuard.f(-0.4F), NumberGuard.f(0.4F))
                     )
                  );
            }
         }
      }

      if (event instanceof EventRender3D e) {
         e.getMatrixStack().push();
         RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder bufferBuilder = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         this.worldParticles.forEach(p -> p.render(bufferBuilder));
         RenderUtil.render3D.endBuilding(bufferBuilder);
         RenderSystem.depthMask(true);
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.disableDepthTest();
         RenderSystem.disableBlend();
         e.getMatrixStack().pop();
      }
   }

   private void handleDamageParticles(Event event) {
      FlowObfuscator.fakeHandler();
      if (!this.types.get(Strings.b("SGFzYXI="))) return;

      if (event instanceof EventUpdate) {
         this.damageParticles.removeIf(Particles.Damage::tick);
      } else if (event instanceof EventAttack eventAttack) {
         Entity target = eventAttack.getTarget();
         if (target == null) return;

         double x = target.getX();
         double y = target.getY() + NumberGuard.d(0.5);
         double z = target.getZ();
         float con = this.countDamage.get().floatValue();

         for (int i = 0; (float)i < con; i++) {
            float motionX = MathUtil.random(-this.sila.get().floatValue(), this.sila.get().floatValue()) * this.speedMultiplier.get().floatValue();
            float motionZ = MathUtil.random(-this.sila.get().floatValue(), this.sila.get().floatValue()) * this.speedMultiplier.get().floatValue();
            float motionY = MathUtil.random(NumberGuard.f(-0.05F), NumberGuard.f(-0.1F));
            motionY *= this.speedMultiplier.get().floatValue();
            float rotation = this.randomRotation.get() ? MathUtil.random(0.0F, NumberGuard.f(360.0F)) : 0.0F;
            int color = ColorUtil.getColorStyle((float)((int)(Math.random() * NumberGuard.d(360.0))));
            if (FlowObfuscator.opaqueTrue()) {
               this.damageParticles
                  .add(new Particles.Damage((float)x, (float)y, (float)z, motionX, motionY, motionZ, this.time.get().longValue(), color, rotation));
            }
         }
      } else if (event instanceof EventRender3D e) {
         MatrixStack matrixStack = e.getMatrixStack();
         matrixStack.push();
         RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder bufferBuilder = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         Identifier tex = TEXTURES.getOrDefault(this.mode.get(), TEXTURES.get(Strings.b("WcSxbGTEsXo=")));
         RenderSystem.setShaderTexture(0, tex);
         this.damageParticles.forEach(p -> p.render(bufferBuilder));
         RenderUtil.render3D.endBuilding(bufferBuilder);
         RenderSystem.depthMask(true);
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.disableDepthTest();
         RenderSystem.disableBlend();
         matrixStack.pop();
      }
   }

   static {
      TEXTURES.put(Strings.b("VGHDpw=="), ResourceProvider.crown);
      TEXTURES.put("Dolar", ResourceProvider.dollar);
      TEXTURES.put(Strings.b("QXRlxZ8gQsO2Y2XEn2k="), ResourceProvider.firefly);
      TEXTURES.put("Kalp", ResourceProvider.heart);
      TEXTURES.put(Strings.b("xZ5pbcWfZWs="), ResourceProvider.lightning);
      TEXTURES.put(Strings.b("w4dpemhp"), ResourceProvider.line);
      TEXTURES.put("Nokta", ResourceProvider.point);
      TEXTURES.put("Romb", ResourceProvider.rhombus);
      TEXTURES.put("Kar Tanesi", ResourceProvider.snowflake);
      TEXTURES.put(Strings.b("S8SxdsSxbGPEsW0="), ResourceProvider.spark);
      TEXTURES.put(Strings.b("WcSxbGTEsXo="), ResourceProvider.star);
   }

   public class Damage {
      private float prevposX;
      private float prevposY;
      private float prevposZ;
      private float posX;
      private float posY;
      private float posZ;
      private float motionX;
      private float motionY;
      private float motionZ;
      private final long createdTime;
      private final long maxAge;
      private int currentColor;
      private final float rotation;

      public Damage(float posX, float posY, float posZ, float motionX, float motionY, float motionZ, long maxAge, int color, float rotation) {
         this.posX = this.prevposX = posX;
         this.posY = this.prevposY = posY;
         this.posZ = this.prevposZ = posZ;
         this.motionX = motionX;
         this.motionY = motionY;
         this.motionZ = motionZ;
         this.createdTime = System.currentTimeMillis();
         this.maxAge = maxAge;
         this.currentColor = color;
         this.rotation = rotation;
      }

      public boolean tick() {
         long now = System.currentTimeMillis();
         if (now - this.createdTime > this.maxAge) {
            return true;
         } else {
            this.prevposX = this.posX;
            this.prevposY = this.posY;
            this.prevposZ = this.posZ;

            for (int i = 0; i < 4; i++) {
               this.tryMove(this.motionX / 4.0F, this.motionY / 4.0F, this.motionZ / 4.0F);
            }

            this.motionX *= 0.97F;
            this.motionY *= 0.97F;
            this.motionZ *= 0.97F;
            return false;
         }
      }

      private boolean isSolid(float x, float y, float z) {
         BlockPos pos = new BlockPos((int)Math.floor((double)x), (int)Math.floor((double)y), (int)Math.floor((double)z));
         return IMinecraft.mc.world != null && IMinecraft.mc.world.getBlockState(pos).isFullCube(IMinecraft.mc.world, pos);
      }

      private void tryMove(float dx, float dy, float dz) {
         float newX = this.posX + dx;
         float newY = this.posY + dy;
         float newZ = this.posZ + dz;
         if (!this.isSolid(newX, this.posY, this.posZ)) {
            this.posX = newX;
         } else {
            this.motionX *= -0.4F;
         }

         if (!this.isSolid(this.posX, newY, this.posZ)) {
            this.posY = newY;
         } else {
            this.motionY *= -0.4F;
         }

         if (!this.isSolid(this.posX, this.posY, newZ)) {
            this.posZ = newZ;
         } else {
            this.motionZ *= -0.4F;
         }
      }

      public void render(BufferBuilder bufferBuilder) {
         Camera camera = IMinecraft.mc.gameRenderer.getCamera();
         Vec3d pos = RenderUtil.interpolatePos(this.prevposX, this.prevposY, this.prevposZ, this.posX, this.posY, this.posZ);
         MatrixStack matrices = new MatrixStack();
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
         matrices.translate(pos.x, pos.y, pos.z);
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotation(this.rotation));
         float alpha = Math.max(0.0F, 1.0F - (float)(System.currentTimeMillis() - this.createdTime) / (float)this.maxAge);
         int colorWithAlpha = RenderUtil.injectAlpha(this.currentColor, (int)(255.0F * alpha));
         Matrix4f matrix = matrices.peek().getPositionMatrix();
         float s = Particles.this.sizeDamage.get().floatValue();
         bufferBuilder.vertex(matrix, 0.0F, -s, 0.0F).texture(0.0F, 1.0F).color(colorWithAlpha);
         bufferBuilder.vertex(matrix, -s, -s, 0.0F).texture(1.0F, 1.0F).color(colorWithAlpha);
         bufferBuilder.vertex(matrix, -s, 0.0F, 0.0F).texture(1.0F, 0.0F).color(colorWithAlpha);
         bufferBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F).color(colorWithAlpha);
      }
   }

   public class World {
      protected float prevposX;
      protected float prevposY;
      protected float prevposZ;
      protected float posX;
      protected float posY;
      protected float posZ;
      protected float motionX;
      protected float motionY;
      protected float motionZ;
      protected int age;
      protected int maxAge;

      public World(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
         this.posX = posX;
         this.posY = posY;
         this.posZ = posZ;
         this.prevposX = posX;
         this.prevposY = posY;
         this.prevposZ = posZ;
         this.motionX = motionX;
         this.motionY = motionY;
         this.motionZ = motionZ;
         this.age = (int)MathUtil.random(100.0F, 300.0F);
         this.maxAge = this.age;
      }

      public boolean tick() {
         if (IMinecraft.mc.player.squaredDistanceTo((double)this.posX, (double)this.posY, (double)this.posZ) > 4096.0) {
            this.age -= 8;
         } else {
            this.age--;
         }

         if (this.age < 0) {
            return true;
         } else {
            this.prevposX = this.posX;
            this.prevposY = this.posY;
            this.prevposZ = this.posZ;
            this.posX = this.posX + this.motionX;
            this.posY = this.posY + this.motionY;
            this.posZ = this.posZ + this.motionZ;
            this.motionX *= 0.9F;
            this.motionY *= 0.9F;
            this.motionZ *= 0.9F;
            this.motionY -= 0.001F;
            return false;
         }
      }

      public void render(BufferBuilder bufferBuilder) {
         Identifier tex = Particles.TEXTURES.getOrDefault(Particles.this.mode.get(), Particles.TEXTURES.get(Strings.b("WcSxbGTEsXo=")));
         RenderSystem.setShaderTexture(0, tex);
         Camera camera = IMinecraft.mc.gameRenderer.getCamera();
         int color1 = ColorUtil.getColorStyle((float)(this.age * 2));
         Vec3d pos = RenderUtil.interpolatePos(this.prevposX, this.prevposY, this.prevposZ, this.posX, this.posY, this.posZ);
         MatrixStack matrices = new MatrixStack();
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
         matrices.translate(pos.x, pos.y, pos.z);
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
         Matrix4f matrix1 = matrices.peek().getPositionMatrix();
         float size = Particles.this.sizeWorld.get().floatValue();
         bufferBuilder.vertex(matrix1, 0.0F, -size, 0.0F)
            .texture(0.0F, 1.0F)
            .color(RenderUtil.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))));
         bufferBuilder.vertex(matrix1, -size, -size, 0.0F)
            .texture(1.0F, 1.0F)
            .color(RenderUtil.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))));
         bufferBuilder.vertex(matrix1, -size, 0.0F, 0.0F)
            .texture(1.0F, 0.0F)
            .color(RenderUtil.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))));
         bufferBuilder.vertex(matrix1, 0.0F, 0.0F, 0.0F)
            .texture(0.0F, 0.0F)
            .color(RenderUtil.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))));
      }
   }
}
