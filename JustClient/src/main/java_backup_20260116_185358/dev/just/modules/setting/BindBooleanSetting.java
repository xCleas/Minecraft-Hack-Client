package dev.just.modules.setting;

import java.util.function.Supplier;

public class BindBooleanSetting extends Setting {
   public int dotState = 0;
   public long lastDotUpdate = 0L;
   public boolean expanded = false;
   private String name;
   private String desc;
   private boolean status;
   private Supplier<Boolean> visible;
   private int bindKey = 0;
   private boolean listeningForBind = false;

   public BindBooleanSetting(String name, boolean status) {
      this.name = name;
      this.status = status;
      this.setVisible(() -> true);
   }

   public BindBooleanSetting(String name, String desc, boolean status) {
      this.name = name;
      this.status = status;
      this.desc = desc;
      this.setVisible(() -> true);
   }

   public BindBooleanSetting(String name, boolean status, Supplier<Boolean> visible) {
      this.name = name;
      this.status = status;
      this.setVisible(visible);
   }

   public BindBooleanSetting(String name, boolean status, String desc, Supplier<Boolean> visible) {
      this.name = name;
      this.status = status;
      this.desc = desc;
      this.setVisible(visible);
   }

   @Override
   public boolean isVisible() {
      return this.visible.get();
   }

   @Override
   public void setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public String getDesc() {
      return this.desc;
   }

   public boolean get() {
      return this.status;
   }

   public void set(boolean value) {
      this.status = value;
   }

   public int getBindKey() {
      return this.bindKey;
   }

   public void setKey(int bindKey) {
      this.bindKey = bindKey;
   }

   public boolean isListeningForBind() {
      return this.listeningForBind;
   }

   public void setListeningForBind(boolean listeningForBind) {
      this.listeningForBind = listeningForBind;
   }

   public boolean onKeyPress(int keyCode, boolean isKeyDown) {
      int processedKey = keyCode >= 0 ? keyCode : -(100 + keyCode + 2);
      if (processedKey == this.bindKey && isKeyDown) {
         this.status = !this.status;
         return true;
      } else {
         return false;
      }
   }
}
