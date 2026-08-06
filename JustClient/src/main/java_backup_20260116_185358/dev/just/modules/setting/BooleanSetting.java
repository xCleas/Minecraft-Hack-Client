package dev.just.modules.setting;

import dev.just.modules.Function;
import java.util.function.Supplier;

public class BooleanSetting extends Setting {
   private String name;
   private String desc;
   private boolean status;
   private Supplier<Boolean> visible;

   public BooleanSetting(String name, boolean status) {
      this.name = name;
      this.status = status;
      this.setVisible(() -> true);
   }

   public BooleanSetting(String name, boolean status, String desc) {
      this.name = name;
      this.status = status;
      this.desc = desc;
      this.setVisible(() -> true);
   }

   public BooleanSetting(String name, boolean status, Supplier<Boolean> visible) {
      this.name = name;
      this.status = status;
      this.setVisible(visible);
   }

   public BooleanSetting(String name, boolean status, String desc, Supplier<Boolean> visible) {
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
      if (this.status != value) {
         this.status = value;
         Function.autoSave();
      }
   }
}
