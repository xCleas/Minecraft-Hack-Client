package dev.just.modules.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import dev.just.protect.runtime.I0O1l0I1;

public class BindSetting extends Setting {
   private static final List<BindSetting> allBindings = new ArrayList<>();
   private Supplier<Boolean> visible;
   private int key;
   private String name;
   private boolean binding;

   public BindSetting(String name, int defaultKey) {
      this.name = name != null ? name : I0O1l0I1.b("VHXFnw==");
      this.key = defaultKey;
      this.binding = false;
      allBindings.add(this);
      this.setVisible(() -> true);
   }

   public BindSetting(String name, int defaultKey, Supplier<Boolean> visible) {
      this.name = name != null ? name : I0O1l0I1.b("VHXFnw==");
      this.key = defaultKey;
      this.binding = false;
      allBindings.add(this);
      this.setVisible(visible);
   }

   @Override
   public boolean isVisible() {
      return this.visible != null && this.visible.get();
   }

   @Override
   public void setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public boolean isBinding() {
      return this.binding;
   }

   public void setBinding(boolean binding) {
      this.binding = binding;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
