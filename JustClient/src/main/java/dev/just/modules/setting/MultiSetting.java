package dev.just.modules.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class MultiSetting extends Setting {
   private final String name;
   private final List<String> modes;
   private final Set<String> selected;
   private Supplier<Boolean> visible;
   public boolean expanded;
   public int modeOffset;

   public int getModeOffset() {
      return this.modeOffset;
   }

   public MultiSetting(String name, String[] modes) {
      this(name, Collections.emptySet(), modes);
   }

   public MultiSetting(String name, Collection<String> selected, String[] modes) {
      this(name, selected, Arrays.asList(modes));
   }

   public MultiSetting(Supplier<Boolean> visible, String name, Collection<String> selected, String[] modes) {
      this(name, selected, Arrays.asList(modes), visible);
   }

   public MultiSetting(String name, Collection<String> selected, List<String> modes) {
      this.name = name;
      this.modes = new ArrayList<>(modes);
      this.selected = new LinkedHashSet<>();
      this.setVisible(() -> true);
      this.setSelected(selected);
   }

   public MultiSetting(String name, Collection<String> selected, List<String> modes, Supplier<Boolean> visible) {
      this.name = name;
      this.modes = new ArrayList<>(modes);
      this.selected = new LinkedHashSet<>();
      this.setVisible(visible);
      this.setSelected(selected);
   }

   public void toggle(String mode) {
      if (this.modes.contains(mode)) {
         if (this.selected.contains(mode)) {
            this.selected.remove(mode);
         } else {
            this.selected.add(mode);
         }
      }
   }

   public void setSelected(Collection<String> modes) {
      this.selected.clear();
      modes.stream().filter(this.modes::contains).forEach(this.selected::add);
   }

   public void clearSelection() {
      this.selected.clear();
   }

   public boolean get(String name) {
      return this.selected.contains(name);
   }

   public boolean hasAnySelected() {
      return !this.selected.isEmpty();
   }

   public String getConfigValue() {
      return String.join(",", this.selected);
   }

   public void setConfigValue(String value) {
      this.setSelected(Arrays.asList(value.split(",")));
   }

   public List<String> getAvailableModes() {
      return new ArrayList<>(this.modes);
   }

   public Set<String> getSelectedModes() {
      return new LinkedHashSet<>(this.selected);
   }

   @Override
   public String getName() {
      return this.name;
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   @Override
   public boolean isVisible() {
      return this.visible.get();
   }

   @Override
   public void setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
   }

   public int getAllSelected() {
      return this.selected.size();
   }
}
