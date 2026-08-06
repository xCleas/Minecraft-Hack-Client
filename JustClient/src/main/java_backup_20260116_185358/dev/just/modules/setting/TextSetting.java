package dev.just.modules.setting;

import java.util.function.Supplier;

public class TextSetting extends Setting {
   private Supplier<Boolean> visible;
   private String value;
   public boolean isFocused = false;
   public int cursorPosition = 0;
   public boolean cursorVisible = false;
   public boolean hasText = false;

   public boolean isFocused() {
      return this.isFocused;
   }

   public void setFocused(boolean focused) {
      this.isFocused = focused;
   }

   public int getCursorPosition() {
      return this.cursorPosition;
   }

   public void setCursorPosition(int pos) {
      this.cursorPosition = pos;
   }

   public boolean isCursorVisible() {
      return this.cursorVisible;
   }

   public boolean hasText() {
      return this.hasText;
   }

   public void setHasText(boolean has) {
      this.hasText = has;
   }

   public TextSetting(String name, String value) {
      this.name = name;
      this.setValue(value);
      this.setVisible(() -> true);
   }

   public TextSetting(String name, String value, Supplier<Boolean> visible) {
      this.name = name;
      this.setValue(value);
      this.setVisible(visible);
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   @Override
   public boolean isVisible() {
      return this.visible.get();
   }

   @Override
   public void setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
   }
}
