package dev.just.screens.dropdown;

import dev.just.modules.setting.Setting;
import net.minecraft.client.gui.DrawContext;

public interface SettingRenderer<T extends Setting> {
   void render(DrawContext var1, T var2, int var3, int var4, int var5, int var6);

   boolean mouseClicked(T var1, double var2, double var4, int var6, int var7, int var8, int var9, int var10);

   default boolean mouseReleased(T setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      return false;
   }

   default boolean mouseScrolled(T setting, double mouseX, double mouseY, double scrollX, double scrollY, int x, int y, int width, int height) {
      return false;
   }

   default boolean keyPressed(T setting, int keyCode, int scanCode, int modifiers) {
      return false;
   }

   default boolean charTyped(T setting, char c, int modifiers) {
      return false;
   }

   default boolean keyReleased(T setting, int keyCode, int scanCode, int modifiers) {
      return false;
   }

   default void tick(T setting, float delta) {
   }

   int getHeight();
}
