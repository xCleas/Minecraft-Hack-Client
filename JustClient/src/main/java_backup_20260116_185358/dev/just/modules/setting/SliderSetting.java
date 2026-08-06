package dev.just.modules.setting;

import dev.just.modules.Function;
import java.util.function.Supplier;
import net.minecraft.util.math.MathHelper;

public class SliderSetting extends Setting {
   public boolean dragging;
   public float circleScale = 1.0F;
   private String name;
   private double value;
   private double min;
   private double max;
   private double increment;
   private Supplier<Boolean> visible;
   public double circlePos = -1.0;

   public SliderSetting(String name, double value, double min, double max, double increment) {
      this.name = name;
      this.value = value;
      this.min = min;
      this.max = max;
      this.increment = increment;
      this.setVisible(() -> true);
   }

   public SliderSetting(String name, double value, double min, double max, double increment, Supplier<Boolean> visible) {
      this.name = name;
      this.value = value;
      this.min = min;
      this.max = max;
      this.increment = increment;
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

   public Number get() {
      return MathHelper.clamp(this.value, this.getMin(), this.getMax());
   }

   public void set(double value) {
      double oldValue = this.value;
      this.value = MathHelper.clamp(value, this.getMin(), this.getMax());
      // Deger degistiyse ve dragging bittiyse kaydet
      if (oldValue != this.value && !this.dragging) {
         Function.autoSave();
      }
   }

   // Dragging bitince kaydet
   public void onDragEnd() {
      Function.autoSave();
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public double getIncrement() {
      return this.increment;
   }
}
