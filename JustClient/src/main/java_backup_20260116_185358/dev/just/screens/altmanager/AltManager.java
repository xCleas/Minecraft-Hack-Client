package dev.just.screens.altmanager;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import dev.just.protect.runtime.Strings;

public class AltManager extends Screen implements IMinecraft {
   private final Screen parent;
   private boolean isTyping = false;
   private final StringBuilder inputText = new StringBuilder();
   private final List<String> accounts = Manager.ACCOUNT_MANAGER.getAccounts();
   private float scrollOffset = 0.0F;
   private float targetScrollOffset = 0.0F;
   private float hoverAnimationInput = 0.0F;
   private float[] hoverAnimations1;
   private float[] hoverAnimations2;
   private int selectedAccountIndex = -1;
   private static final float SCALE = 1.5F;
   private float createHoverAnim = 0.0F;
   private float clearHoverAnim = 0.0F;
   private float randomHoverAnim = 0.0F;
   private float createScale = 1.0F;
   private float clearScale = 1.0F;
   private float randomScale = 1.0F;
   private final String title = Strings.b("SGVzYXAgWcO2bmV0aWNpc2k=");
   private int shakeTime = 0;
   private float shakeOffsetY = 0.0F;
   private boolean showConfirmDialog = false;

   public AltManager(Screen parent) {
      super(Text.of(Strings.b("SGVzYXAgWcO2bmV0aWNpc2k=")));
      this.parent = parent;
   }

   protected void init() {
      super.init();
   }

   public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
      this.scrollOffset = MathUtil.lerp(this.scrollOffset, this.targetScrollOffset, 8.0F);
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), -1.0F, -1.0F, (float)(this.width + 2), (float)(this.height + 2), 4.0F, new Color(27, 27, 27, 255).getRGB()
      );
      if (this.shakeTime > 0) {
         this.shakeTime--;
         this.shakeOffsetY = (float)(Math.sin((double)this.shakeTime * 0.5) * 3.0);
      } else {
         this.shakeOffsetY = 0.0F;
      }

      int titleWidth = (int)FontUtils.sf_bold[48].getWidth(Strings.b("SGVzYXAgWcO2bmV0aWNpc2k="));
      float titleX = (float)(this.width - titleWidth) / 2.0F;
      float titleBaseY = (float)this.height / 7.0F;
      float titleY = titleBaseY + this.shakeOffsetY;
      float time = (float)(System.currentTimeMillis() % 4000L) / 1500.0F;
      FontUtils.sf_bold[48]
         .renderAnimatedGradientText(
            drawContext.getMatrices(), Strings.b("SGVzYXAgWcO2bmV0aWNpc2k="), titleX, titleY, ColorUtil.getColorStyle(30.0F), ColorUtil.getColorStyle(260.0F), time
         );
      int centerX = this.width / 2;
      int centerY = this.height / 2;
      int inputWidth = 330;
      int inputHeight = 25;
      int inputX = centerX - 165;
      int inputY = centerY - 138;
      boolean isHoveredInput = RenderUtil.isInRegion(mouseX, mouseY, inputX, inputY, inputWidth, inputHeight);
      this.hoverAnimationInput = MathUtil.lerp(this.hoverAnimationInput, isHoveredInput ? 1.0F : 0.0F, 10.0F);
      int nameColor = ColorUtil.interpolateColor(ColorUtil.rgba(180, 180, 180, 255), ColorUtil.rgba(230, 230, 230, 255), this.hoverAnimationInput);
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), (float)inputX, (float)inputY, (float)inputWidth, (float)inputHeight, 4.0F, ColorUtil.rgba(35, 35, 35, 255)
      );
      if (!this.isTyping) {
         StringBuilder placeholder = new StringBuilder(Strings.b("S3VsbGFuxLFjxLEgYWTEsSBnaXJpbg=="));

         for (int i = 0; (long)i < System.currentTimeMillis() / 500L % 4L; i++) {
            placeholder.append(".");
         }

         FontUtils.durman[21]
            .drawLeftAligned(
               drawContext.getMatrices(), placeholder.toString(), (float)(inputX + 6), (float)inputY + (float)inputHeight / 2.0F - 7.0F, nameColor
            );
      } else {
         StringBuilder builder = new StringBuilder(this.inputText);
         builder.append(System.currentTimeMillis() / 500L % 2L == 0L ? "_" : "");
         FontUtils.durman[21]
            .drawLeftAligned(drawContext.getMatrices(), builder.toString(), (float)(inputX + 6), (float)inputY + (float)inputHeight / 2.0F - 7.0F, nameColor);
      }

      int listY = centerY - 105;
      int listWidth = 330;
      int listHeight = 210;
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), (float)inputX, (float)listY, (float)listWidth, (float)listHeight, 4.0F, new Color(35, 35, 35, 255).getRGB()
      );
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)inputX, (double)listY, (double)listWidth, (double)listHeight);
      if (this.hoverAnimations1 == null || this.hoverAnimations1.length != this.accounts.size()) {
         this.hoverAnimations1 = new float[this.accounts.size()];
      }

      if (this.hoverAnimations2 == null || this.hoverAnimations2.length != this.accounts.size()) {
         this.hoverAnimations2 = new float[this.accounts.size()];
      }

      float startY = (float)(listY + 5);
      float itemHeight = 52.5F;

      for (int i = 0; i < this.accounts.size(); i++) {
         float y = startY - this.scrollOffset + (float)i * itemHeight;
         int entryX = centerX - 157;
         int entryWidth = 210;
         int entryHeight = 45;
         RenderUtil.drawRoundedRect(
            drawContext.getMatrices(), (float)entryX, y, (float)(entryWidth + 10), (float)entryHeight, 4.0F, ColorUtil.rgba(23, 23, 23, 255)
         );
         int bgColor = i == this.selectedAccountIndex ? ColorUtil.rgba(50, 50, 80, 255) : ColorUtil.rgba(23, 23, 23, 255);
         RenderUtil.drawRoundedBorder(drawContext.getMatrices(), (float)entryX, y, (float)(entryWidth + 10), (float)entryHeight, 4.0F, 0.3F, bgColor);
         FontUtils.durman[21]
            .drawLeftAligned(drawContext.getMatrices(), this.accounts.get(i), (float)(entryX + 10), y + 5.0F, ColorUtil.rgba(200, 200, 200, 255));
         FontUtils.durman[16]
            .drawLeftAligned(
               drawContext.getMatrices(),
               "Tarih " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
               (float)(entryX + 10),
               y + 30.0F,
               ColorUtil.rgba(140, 140, 140, 255)
            );
         int btnWidth = 90;
         int btnHeight = 19;
         int selectBtnX = entryX + entryWidth + 15;
         int selectBtnY = (int)y;
         boolean accountHovered1 = RenderUtil.isInRegion(mouseX, mouseY, selectBtnX, selectBtnY, btnWidth, btnHeight);
         this.hoverAnimations1[i] = MathUtil.lerp(this.hoverAnimations1[i], accountHovered1 ? 1.0F : 0.0F, 12.0F);
         int selectBgColor = ColorUtil.blendColorsInt(new Color(35, 35, 35).getRGB(), new Color(55, 55, 55).getRGB(), this.hoverAnimations1[i]);
         int outlineColor = new Color(60, 60, 60, 180).getRGB();
         RenderUtil.drawRoundedRect(
            drawContext.getMatrices(), (float)selectBtnX, (float)selectBtnY, (float)btnWidth, (float)(btnHeight + 2), 4.0F, selectBgColor
         );
         RenderUtil.drawRoundedBorder(
            drawContext.getMatrices(), (float)selectBtnX, (float)selectBtnY, (float)btnWidth, (float)(btnHeight + 2), 4.0F, 1.0F, outlineColor
         );
         FontUtils.sf_medium[20]
            .centeredDraw(
               drawContext.getMatrices(),
               Strings.b("U2XDpw=="),
               (float)selectBtnX + (float)btnWidth / 2.0F,
               (float)selectBtnY + (float)btnHeight / 2.0F - 6.0F,
               Color.WHITE.getRGB()
            );
         int deleteBtnY = selectBtnY + btnHeight + 4;
         boolean accountHovered2 = RenderUtil.isInRegion(mouseX, mouseY, selectBtnX, deleteBtnY, btnWidth, btnHeight);
         this.hoverAnimations2[i] = MathUtil.lerp(this.hoverAnimations2[i], accountHovered2 ? 1.0F : 0.0F, 12.0F);
         int deleteBgColor = ColorUtil.blendColorsInt(new Color(35, 35, 35).getRGB(), new Color(55, 55, 55).getRGB(), this.hoverAnimations2[i]);
         RenderUtil.drawRoundedRect(
            drawContext.getMatrices(), (float)selectBtnX, (float)deleteBtnY, (float)btnWidth, (float)(btnHeight + 2), 4.0F, deleteBgColor
         );
         RenderUtil.drawRoundedBorder(
            drawContext.getMatrices(), (float)selectBtnX, (float)deleteBtnY, (float)btnWidth, (float)(btnHeight + 2), 4.0F, 1.0F, outlineColor
         );
         FontUtils.sf_medium[20]
            .centeredDraw(
               drawContext.getMatrices(),
               "Sil",
               (float)selectBtnX + (float)btnWidth / 2.0F,
               (float)deleteBtnY + (float)btnHeight / 2.0F - 6.0F,
               Color.WHITE.getRGB()
            );
      }

      Scissor.unset();
      Scissor.pop();
      int buttonsY = listY + listHeight + 15;
      int buttonWidth = 105;
      int createX = centerX - buttonWidth - 60;
      int clearX = centerX - buttonWidth / 2;
      int randomX = centerX + buttonWidth + -45;
      float animSpeed = 0.04F;
      boolean isHoveredCreate = RenderUtil.isInRegion(mouseX, mouseY, createX, buttonsY, buttonWidth, inputHeight);
      if (isHoveredCreate) {
         this.createHoverAnim = Math.min(1.0F, this.createHoverAnim + animSpeed);
         this.createScale = Math.min(1.04F, this.createScale + animSpeed * 0.5F);
      } else {
         this.createHoverAnim = Math.max(0.0F, this.createHoverAnim - animSpeed);
         this.createScale = Math.max(1.0F, this.createScale - animSpeed * 0.5F);
      }

      int createBgColor = ColorUtil.blendColorsInt(new Color(35, 35, 35).getRGB(), new Color(55, 55, 55).getRGB(), this.createHoverAnim);
      drawContext.getMatrices().push();
      drawContext.getMatrices().translate((float)createX + (float)buttonWidth / 2.0F, (float)buttonsY + (float)inputHeight / 2.0F, 0.0F);
      drawContext.getMatrices().scale(this.createScale, this.createScale, 1.0F);
      drawContext.getMatrices().translate(-((float)createX + (float)buttonWidth / 2.0F), -((float)buttonsY + (float)inputHeight / 2.0F), 0.0F);
      RenderUtil.drawRoundedRect(drawContext.getMatrices(), (float)createX, (float)buttonsY, (float)buttonWidth, (float)inputHeight, 4.0F, createBgColor);
      RenderUtil.drawRoundedBorder(
         drawContext.getMatrices(), (float)createX, (float)buttonsY, (float)buttonWidth, (float)inputHeight, 4.0F, 1.0F, new Color(60, 60, 60, 180).getRGB()
      );
      FontUtils.sf_medium[20]
         .centeredDraw(
            drawContext.getMatrices(),
            Strings.b("RWtsZQ=="),
            (float)createX + (float)buttonWidth / 2.0F,
            (float)buttonsY + (float)inputHeight / 2.0F - 7.0F,
            Color.WHITE.getRGB()
         );
      drawContext.getMatrices().pop();
      boolean isHoveredClear = RenderUtil.isInRegion(mouseX, mouseY, clearX, buttonsY, buttonWidth, inputHeight);
      if (isHoveredClear) {
         this.clearHoverAnim = Math.min(1.0F, this.clearHoverAnim + animSpeed);
         this.clearScale = Math.min(1.04F, this.clearScale + animSpeed * 0.5F);
      } else {
         this.clearHoverAnim = Math.max(0.0F, this.clearHoverAnim - animSpeed);
         this.clearScale = Math.max(1.0F, this.clearScale - animSpeed * 0.5F);
      }

      int clearBgColor = ColorUtil.blendColorsInt(new Color(35, 35, 35).getRGB(), new Color(55, 55, 55).getRGB(), this.clearHoverAnim);
      drawContext.getMatrices().push();
      drawContext.getMatrices().translate((float)clearX + (float)buttonWidth / 2.0F, (float)buttonsY + (float)inputHeight / 2.0F, 0.0F);
      drawContext.getMatrices().scale(this.clearScale, this.clearScale, 1.0F);
      drawContext.getMatrices().translate(-((float)clearX + (float)buttonWidth / 2.0F), -((float)buttonsY + (float)inputHeight / 2.0F), 0.0F);
      RenderUtil.drawRoundedRect(drawContext.getMatrices(), (float)clearX, (float)buttonsY, (float)buttonWidth, (float)inputHeight, 4.0F, clearBgColor);
      RenderUtil.drawRoundedBorder(
         drawContext.getMatrices(), (float)clearX, (float)buttonsY, (float)buttonWidth, (float)inputHeight, 4.0F, 1.0F, new Color(60, 60, 60, 180).getRGB()
      );
      FontUtils.sf_medium[20]
         .centeredDraw(
            drawContext.getMatrices(),
            Strings.b("SGVwc2luaSBzaWw="),
            (float)clearX + (float)buttonWidth / 2.0F,
            (float)buttonsY + (float)inputHeight / 2.0F - 7.0F,
            Color.WHITE.getRGB()
         );
      drawContext.getMatrices().pop();
      boolean isHoveredRandom = RenderUtil.isInRegion(mouseX, mouseY, randomX, buttonsY, buttonWidth, inputHeight);
      if (isHoveredRandom) {
         this.randomHoverAnim = Math.min(1.0F, this.randomHoverAnim + animSpeed);
         this.randomScale = Math.min(1.04F, this.randomScale + animSpeed * 0.5F);
      } else {
         this.randomHoverAnim = Math.max(0.0F, this.randomHoverAnim - animSpeed);
         this.randomScale = Math.max(1.0F, this.randomScale - animSpeed * 0.5F);
      }

      int randomBgColor = ColorUtil.blendColorsInt(new Color(35, 35, 35).getRGB(), new Color(55, 55, 55).getRGB(), this.randomHoverAnim);
      drawContext.getMatrices().push();
      drawContext.getMatrices().translate((float)randomX + (float)buttonWidth / 2.0F, (float)buttonsY + (float)inputHeight / 2.0F, 0.0F);
      drawContext.getMatrices().scale(this.randomScale, this.randomScale, 1.0F);
      drawContext.getMatrices().translate(-((float)randomX + (float)buttonWidth / 2.0F), -((float)buttonsY + (float)inputHeight / 2.0F), 0.0F);
      RenderUtil.drawRoundedRect(drawContext.getMatrices(), (float)randomX, (float)buttonsY, (float)buttonWidth, (float)inputHeight, 4.0F, randomBgColor);
      RenderUtil.drawRoundedBorder(
         drawContext.getMatrices(), (float)randomX, (float)buttonsY, (float)buttonWidth, (float)inputHeight, 4.0F, 1.0F, new Color(60, 60, 60, 180).getRGB()
      );
      FontUtils.sf_medium[20]
         .centeredDraw(
            drawContext.getMatrices(),
            "Rastgele",
            (float)randomX + (float)buttonWidth / 2.0F,
            (float)buttonsY + (float)inputHeight / 2.0F - 7.0F,
            Color.WHITE.getRGB()
         );
      drawContext.getMatrices().pop();
      String accountName = mc.getSession().getUsername();
      FontUtils.sf_medium[18]
         .centeredDraw(drawContext.getMatrices(), Strings.b("U2XDp2lsaSBoZXNhcDog") + accountName, (float)centerX, (float)(buttonsY + inputHeight + 30), -1);
      FontUtils.sf_medium[18]
         .centeredDraw(drawContext.getMatrices(), Strings.b("SGVzYXAgc2F5xLFzxLE6IA==") + this.accounts.size(), (float)centerX, (float)(buttonsY + inputHeight + 60), -1);
      if (this.showConfirmDialog) {
         this.drawConfirmDialog(drawContext);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int centerX = this.width / 2;
      int centerY = this.height / 2;
      int inputWidth = 330;
      int inputHeight = 25;
      int inputX = centerX - 165;
      int inputY = centerY - 138;
      int buttonWidth = 105;
      int buttonsY = centerY - 105 + 210 + 15;
      int createX = centerX - buttonWidth - 60;
      int clearX = centerX - buttonWidth / 2;
      int randomX = centerX + buttonWidth + -45;
      if (RenderUtil.isInRegion(mouseX, mouseY, (double)inputX, (double)inputY, (double)inputWidth, (double)inputHeight) && !this.isTyping && button == 0) {
         this.isTyping = true;
         return true;
      } else if (!RenderUtil.isInRegion(mouseX, mouseY, (double)inputX, (double)inputY, (double)inputWidth, (double)inputHeight)
         && !RenderUtil.isInRegion(mouseX, mouseY, (double)createX, (double)buttonsY, (double)buttonWidth, (double)inputHeight)
         && !RenderUtil.isInRegion(mouseX, mouseY, (double)clearX, (double)buttonsY, (double)buttonWidth, (double)inputHeight)
         && !RenderUtil.isInRegion(mouseX, mouseY, (double)randomX, (double)buttonsY, (double)buttonWidth, (double)inputHeight)
         && this.isTyping
         && button == 0) {
         this.isTyping = false;
         return true;
      } else {
         int titleWidth = (int)FontUtils.sf_bold[48].getWidth(Strings.b("SGVzYXAgWcO2bmV0aWNpc2k="));
         float titleX = (float)(this.width - titleWidth) / 2.0F;
         float titleY = (float)this.height / 7.0F;
         if (mouseX >= (double)titleX && mouseX <= (double)(titleX + (float)titleWidth) && mouseY >= (double)titleY && mouseY <= (double)(titleY + 25.0F)) {
            this.shakeTime = 20;
            return true;
         } else if (RenderUtil.isInRegion(mouseX, mouseY, (double)createX, (double)buttonsY, (double)buttonWidth, (double)inputHeight)
            && this.isTyping
            && button == 0) {
            String newAccount = this.inputText.toString().trim();
            if (!newAccount.isEmpty() && this.accounts.stream().noneMatch(a -> a.equalsIgnoreCase(newAccount))) {
               this.isTyping = false;
               this.accounts.add(newAccount);
               Manager.ACCOUNT_MANAGER.addAccount(newAccount);
               this.inputText.setLength(0);
            }

            return true;
         } else if (this.showConfirmDialog) {
            int boxWidth = 300;
            int boxHeight = 130;
            int boxX = (this.width - boxWidth) / 2;
            int boxY = (this.height - boxHeight) / 2;
            int btnWidth = 90;
            int btnHeight = 28;
            int yesX = boxX + 35;
            int noX = boxX + boxWidth - 35 - btnWidth;
            int btnY = boxY + boxHeight - 50;
            if (RenderUtil.isInRegion(mouseX, mouseY, (double)yesX, (double)btnY, (double)btnWidth, (double)btnHeight)) {
               this.accounts.clear();
               Manager.ACCOUNT_MANAGER.clearAll();
               this.selectedAccountIndex = -1;
               this.showConfirmDialog = false;
               return true;
            } else if (RenderUtil.isInRegion(mouseX, mouseY, (double)noX, (double)btnY, (double)btnWidth, (double)btnHeight)) {
               this.showConfirmDialog = false;
               return true;
            } else {
               return true;
            }
         } else if (RenderUtil.isInRegion(mouseX, mouseY, (double)clearX, (double)buttonsY, (double)buttonWidth, 25.0) && button == 0) {
            this.showConfirmDialog = true;
            return true;
         } else if (RenderUtil.isInRegion(mouseX, mouseY, (double)randomX, (double)buttonsY, (double)buttonWidth, (double)inputHeight) && button == 0) {
            int maxNumber = 9999999;
            String prefix = "justclient_";
            String randomNumberStr = String.valueOf((int)(Math.random() * (double)(maxNumber + 1)));
            String randomName = prefix + randomNumberStr;
            if (randomName.length() > 16) {
               randomName = randomName.substring(0, 16);
            }

            if (!this.accounts.contains(randomName)) {
               this.accounts.add(randomName);
               Manager.ACCOUNT_MANAGER.addAccount(randomName);
            }

            ClientManager.loginAccount(randomName);
            this.selectedAccountIndex = this.accounts.indexOf(randomName);
            Manager.ACCOUNT_MANAGER.setLastSelectedAccount(randomName);
            return true;
         } else {
            int listY = centerY - 105;
            int listWidth = 330;
            int listHeight = 210;
            if (RenderUtil.isInRegion(mouseX, mouseY, (double)inputX, (double)listY, (double)listWidth, (double)listHeight)) {
               float startY = (float)(listY + 5);
               float itemHeight = 52.5F;
               int btnWidth = 90;
               int btnHeight = 19;
               int entryX = centerX - 157;
               int entryWidth = 210;
               int entryHeight = 45;

               for (int i = 0; i < this.accounts.size(); i++) {
                  float y = startY - this.scrollOffset + (float)i * itemHeight;
                  if (RenderUtil.isInRegion(mouseX, mouseY, (double)entryX, (double)((int)y), (double)(entryWidth + 10), (double)entryHeight) && button == 0) {
                     String selected = this.accounts.get(i);
                     ClientManager.loginAccount(selected);
                     this.selectedAccountIndex = i;
                     Manager.ACCOUNT_MANAGER.setLastSelectedAccount(selected);
                     return true;
                  }

                  int selectBtnX = entryX + entryWidth + 15;
                  int selectBtnY = (int)y;
                  if (RenderUtil.isInRegion(mouseX, mouseY, (double)selectBtnX, (double)selectBtnY, (double)btnWidth, (double)btnHeight) && button == 0) {
                     String selected = this.accounts.get(i);
                     ClientManager.loginAccount(selected);
                     this.selectedAccountIndex = i;
                     Manager.ACCOUNT_MANAGER.setLastSelectedAccount(selected);
                     return true;
                  }

                  int deleteBtnY = selectBtnY + btnHeight + 4;
                  if (RenderUtil.isInRegion(mouseX, mouseY, (double)selectBtnX, (double)deleteBtnY, (double)btnWidth, (double)btnHeight) && button == 0) {
                     if (this.selectedAccountIndex == i) {
                        this.selectedAccountIndex = -1;
                     }

                     Manager.ACCOUNT_MANAGER.removeAccount(this.accounts.get(i));
                     this.accounts.remove(i);
                     int maxOffset = Math.max(0, this.accounts.size() * 57 - 202);
                     this.targetScrollOffset = Math.max(0.0F, Math.min(this.targetScrollOffset, (float)maxOffset));
                     return true;
                  }
               }
            }

            return super.mouseClicked(mouseX, mouseY, button);
         }
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      int centerY = this.height / 2;
      int listY = centerY - 105;
      int listHeight = 210;
      if (mouseY >= (double)listY && mouseY <= (double)(listY + listHeight)) {
         this.targetScrollOffset = (float)((double)this.targetScrollOffset - scrollY * 45.0);
         int maxOffset = Math.max(0, this.accounts.size() * 54 - listHeight);
         this.targetScrollOffset = Math.max(0.0F, Math.min(this.targetScrollOffset, (float)maxOffset));
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.isTyping) {
         boolean ctrl = GLFW.glfwGetKey(mc.getWindow().getHandle(), 341) == 1 || GLFW.glfwGetKey(mc.getWindow().getHandle(), 345) == 1;
         if (ctrl && keyCode == 86) {
            String clipboard = GLFW.glfwGetClipboardString(mc.getWindow().getHandle());
            if (clipboard != null && !clipboard.isEmpty()) {
               String filtered = clipboard.replaceAll("[^\\w]", "");
               int maxLength = 16 - this.inputText.length();
               if (maxLength > 0) {
                  if (filtered.length() > maxLength) {
                     filtered = filtered.substring(0, maxLength);
                  }

                  this.inputText.append(filtered);
               }
            }

            return true;
         }

         if (keyCode == 257 || keyCode == 335) {
            String newAccount = this.inputText.toString().trim();
            if (!newAccount.isEmpty() && this.accounts.stream().noneMatch(a -> a.equalsIgnoreCase(newAccount))) {
               this.isTyping = false;
               this.accounts.add(newAccount);
               Manager.ACCOUNT_MANAGER.addAccount(newAccount);
               this.inputText.setLength(0);
            }

            return true;
         }

         if (keyCode == 259 && this.inputText.length() > 0) {
            this.inputText.deleteCharAt(this.inputText.length() - 1);
            return true;
         }
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean charTyped(char chr, int modifiers) {
      if (this.isTyping) {
         if (chr == '\n' || chr == '\r') {
            return false;
         }

         if (this.inputText.length() < 16 && (Character.isLetterOrDigit(chr) || chr == '_')) {
            this.inputText.append(chr);
            return true;
         }
      }

      return super.charTyped(chr, modifiers);
   }

   public void close() {
      if (this.parent != null) {
         mc.setScreen(this.parent);
      }

      super.close();
   }

   private void drawConfirmDialog(DrawContext drawContext) {
      int boxWidth = 300;
      int boxHeight = 130;
      int boxX = (this.width - boxWidth) / 2;
      int boxY = (this.height - boxHeight) / 2;
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), 0.0F, 0.0F, (float)this.width, (float)this.height, 0.0F, new Color(0, 0, 0, 120).getRGB()
      );
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), (float)boxX, (float)boxY, (float)boxWidth, (float)boxHeight, 6.0F, new Color(40, 40, 40, 240).getRGB()
      );
      FontUtils.sf_bold[22]
         .centeredDraw(
            drawContext.getMatrices(),
            Strings.b("VMO8bSBoZXNhcGxhcsSxIHNpbG1layBpc3RlZGnEn2luaXplIGVtaW4gbWlzaW5pej8="),
            (float)this.width / 2.0F,
            (float)(boxY + 30),
            Color.WHITE.getRGB()
         );
      int btnWidth = 90;
      int btnHeight = 28;
      int yesX = boxX + 35;
      int noX = boxX + boxWidth - 35 - btnWidth;
      int btnY = boxY + boxHeight - 50;
      RenderUtil.drawRoundedRect(drawContext.getMatrices(), (float)yesX, (float)btnY, (float)btnWidth, (float)btnHeight, 5.0F, new Color(60, 180, 75).getRGB());
      FontUtils.sf_medium[20]
         .centeredDraw(
            drawContext.getMatrices(), Strings.b("RXZldA=="), (float)yesX + (float)btnWidth / 2.0F, (float)btnY + (float)btnHeight / 2.0F - 6.0F, Color.WHITE.getRGB()
         );
      RenderUtil.drawRoundedRect(drawContext.getMatrices(), (float)noX, (float)btnY, (float)btnWidth, (float)btnHeight, 5.0F, new Color(200, 60, 60).getRGB());
      FontUtils.sf_medium[20]
         .centeredDraw(
            drawContext.getMatrices(), Strings.b("SGF5xLFy"), (float)noX + (float)btnWidth / 2.0F, (float)btnY + (float)btnHeight / 2.0F - 6.0F, Color.WHITE.getRGB()
         );
   }
}
