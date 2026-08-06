package dev.just.modules.setting;

import dev.just.modules.Function;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting {
   public boolean expanded;
   private List<String> modes;
   private String name;
   private String selected;
   private Supplier<Boolean> visible;
   public int modeOffset;

   public void resetModeOffset() {
      this.modeOffset = 0;
   }

   public void incrementModeOffset(int value) {
      this.modeOffset += value;
   }

   public int getModeOffset() {
      return this.expanded ? this.modeOffset : 0;
   }

   public ModeSetting(String name, String selected, String... modes) {
      this.name = name;
      this.selected = selected;
      this.modes = Arrays.asList(modes);
      this.setVisible(() -> true);
   }

   public ModeSetting(Supplier<Boolean> visible, String name, String selected, String... modes) {
      this.name = name;
      this.selected = selected;
      this.modes = Arrays.asList(modes);
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

   public String get() {
      return this.selected;
   }

   public boolean is(String name) {
      return this.selected.contains(name);
   }

   public void set(String in) {
      if (!this.selected.equals(in)) {
         this.selected = in;
         Function.autoSave();
      }
   }

   public int getIndex() {
      return this.modes.indexOf(this.selected);
   }

   public int getIndex(String mode) {
      return this.modes.indexOf(mode);
   }

   public List<String> getModes() {
      return this.modes;
   }
}
