package dev.just.manager.dragManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import dev.just.manager.IMinecraft;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.Function;
import dev.just.util.render.RenderUtil;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class Dragging {
   private static final int SNAP_DISTANCE = 10;
   private static final float FADE_SPEED = 0.01F;
   private static final float DRAG_SMOOTHNESS = 0.1F;
   @Expose
   @SerializedName("x")
   private float xPos;
   @Expose
   @SerializedName("y")
   private float yPos;
   public float initialXVal;
   public float initialYVal;
   private float startX;
   private float startY;
   private boolean dragging;
   private float width;
   private float height;
   private int defaultX;
   private int defaultY;
   @Expose
   @SerializedName("name")
   private final String name;
   private final Function function;
   private float borderAlpha = 0.0F;
   private float borderScale = 0.0F;
   private float guideAlpha = 0.0F;
   private float tipAlpha = 0.0F;
   public float targetX;
   public float targetY;
   private long lastTime = System.currentTimeMillis();

   public Dragging(Function function, String name, float initialXVal, float initialYVal) {
      this.function = function;
      this.name = name;
      this.xPos = initialXVal;
      this.yPos = initialYVal;
      this.initialXVal = initialXVal;
      this.initialYVal = initialYVal;
      this.targetX = initialXVal;
      this.targetY = initialYVal;
      this.defaultX = (int)initialXVal;
      this.defaultY = (int)initialYVal;
      DragManager.draggables.put(name, this);
   }

   public int getDefaultX() {
      return this.defaultX;
   }

   public int getDefaultY() {
      return this.defaultY;
   }

   public float getX() {
      return this.xPos;
   }

   public void setX(float x) {
      this.xPos = x;
      this.targetX = x;
   }

   public float getY() {
      return this.yPos;
   }

   public void setY(float y) {
      this.yPos = y;
      this.targetY = y;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public String getName() {
      return this.name;
   }

   public Function getModule() {
      return this.function;
   }

   public void onDraw(double mouseX, double mouseY, Window window) {
      long windowHandle = IMinecraft.mc.getWindow().getHandle();
      boolean isShiftDown = GLFW.glfwGetKey(windowHandle, 340) == 1;
      if (this.dragging) {
         float newX = (float)(mouseX - (double)this.startX);
         float newY = (float)(mouseY - (double)this.startY);
         float screenWidth = (float)window.getScaledWidth();
         float screenHeight = (float)window.getScaledHeight();
         float screenCenterX = screenWidth / 2.0F;
         float screenCenterY = screenHeight / 2.0F;
         if (isShiftDown) {
            if (Math.abs(newX + this.width / 2.0F - screenCenterX) <= 10.0F) {
               newX = screenCenterX - this.width / 2.0F;
            }

            if (Math.abs(newY + this.height / 2.0F - screenCenterY) <= 10.0F) {
               newY = screenCenterY - this.height / 2.0F;
            }

            for (Dragging other : DragManager.draggables.values()) {
               if (other != this) {
                  float otherCenterX = other.getX() + other.width / 2.0F;
                  float otherCenterY = other.getY() + other.height / 2.0F;
                  if (Math.abs(newX + this.width / 2.0F - otherCenterX) <= 10.0F) {
                     newX = otherCenterX - this.width / 2.0F;
                  }

                  if (Math.abs(newY + this.height / 2.0F - otherCenterY) <= 10.0F) {
                     newY = otherCenterY - this.height / 2.0F;
                  }
               }
            }
         }

         this.targetX = Math.max(0.0F, Math.min(newX, screenWidth - this.width));
         this.targetY = Math.max(0.0F, Math.min(newY, screenHeight - this.height));
      }

      this.xPos = this.xPos + (this.targetX - this.xPos) * 0.1F;
      this.yPos = this.yPos + (this.targetY - this.yPos) * 0.1F;
      this.borderAlpha = this.dragging ? Math.min(this.borderAlpha + 0.01F, 1.0F) : Math.max(this.borderAlpha - 0.01F, 0.0F);
      float targetExpand = this.dragging ? 4.0F : 0.0F;
      this.borderScale = this.borderScale + (targetExpand - this.borderScale) * 0.1F;
      if (this.borderAlpha > 0.0F) {
         int alphaColor = (int)(this.borderAlpha * 255.0F) << 24 | 16777215;
         float drawX = this.xPos - this.borderScale / 2.0F;
         float drawY = this.yPos - this.borderScale / 2.0F;
         float drawWidth = this.width + this.borderScale;
         float drawHeight = this.height + this.borderScale;
         RenderUtil.drawRoundedBorder(new MatrixStack(), drawX, drawY, drawWidth, drawHeight, 3.0F, 0.01F, alphaColor);
      }

      float targetTipAlpha = this.dragging ? 1.0F : 0.0F;
      this.tipAlpha = this.tipAlpha + (targetTipAlpha - this.tipAlpha) * 0.1F;
      this.tipAlpha = MathHelper.clamp(this.tipAlpha, 0.0F, 1.0F);
      if (this.tipAlpha > 0.0F) {
         int color = (int)(this.tipAlpha * 255.0F) << 24 | 16777215;
         MatrixStack matrix = new MatrixStack();
         String tip = "Shift - Otomatik Hizala";
         FontUtils.durman[12].centeredDraw(matrix, tip, this.xPos + this.width / 2.0F, this.yPos + this.height + 4.0F, color);
      }
   }

   public void renderGuides(Window window) {
      long now = System.currentTimeMillis();
      float delta = (float)(now - this.lastTime) / 16.666F;
      this.lastTime = now;
      float targetAlpha = this.dragging ? 1.0F : 0.0F;
      this.guideAlpha = this.guideAlpha + (targetAlpha - this.guideAlpha) * 1.2F * delta;
      this.guideAlpha = MathHelper.clamp(this.guideAlpha, 0.0F, 1.0F);
      if (!(this.guideAlpha <= 0.0F)) {
         float screenWidth = (float)window.getScaledWidth();
         float screenHeight = (float)window.getScaledHeight();
         float screenCenterX = screenWidth / 2.0F;
         float screenCenterY = screenHeight / 2.0F;
         int alphaColor = (int)(this.guideAlpha * 255.0F) << 24 | 16777215;
         RenderUtil.drawLine(screenCenterX, 0.0F, screenCenterX, screenHeight, alphaColor);
         RenderUtil.drawLine(0.0F, screenCenterY, screenWidth, screenCenterY, alphaColor);

         for (Dragging other : DragManager.draggables.values()) {
            if (other != this) {
               float otherCenterX = other.getX() + other.width / 2.0F;
               float otherCenterY = other.getY() + other.height / 2.0F;
               if (Math.abs(this.xPos + this.width / 2.0F - otherCenterX) < 10.0F) {
                  RenderUtil.drawLine(otherCenterX, 0.0F, otherCenterX, screenHeight, alphaColor);
               }

               if (Math.abs(this.yPos + this.height / 2.0F - otherCenterY) < 10.0F) {
                  RenderUtil.drawLine(0.0F, otherCenterY, screenWidth, otherCenterY, alphaColor);
               }
            }
         }
      }
   }

   public boolean onClick(double mouseX, double mouseY, int button) {
      if (!(IMinecraft.mc.currentScreen instanceof ChatScreen)) {
         return false;
      } else {
         float scaledMouseX = (float)mouseX;
         float scaledMouseY = (float)mouseY;
         if (button == 0
            && RenderUtil.isInRegion(
               (double)((int)scaledMouseX), (double)((int)scaledMouseY), (double)this.xPos, (double)this.yPos, (double)this.width, (double)this.height
            )) {
            this.dragging = true;
            this.startX = scaledMouseX - this.xPos;
            this.startY = scaledMouseY - this.yPos;
            return true;
         } else {
            return false;
         }
      }
   }

   public void onRelease(int button) {
      if (button == 0) {
         this.dragging = false;
      }
   }
}
