package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import java.awt.Color;
import java.util.Arrays;

@FunctionAnnotation(
   name = "ClickGUI",
   desc = "GUI Yonetimi",
   type = Type.Render,
   key = 344
)
public class ClickGUI extends Function {
   public final ModeSetting colorGUI = new ModeSetting("Tema", "Acik-Siyah", "Acik-Siyah", "Koyu");
   public final SliderSetting alpha = new SliderSetting("Saydamlik", 200.0, 120.0, 255.0, 1.0);
   public final BooleanSetting blur = new BooleanSetting("Bulaniklastirma", true);
   public final MultiSetting blurSetting = new MultiSetting(
      () -> this.blur.get(), "Elementler", Arrays.asList("Paneller"), new String[]{"Aciklamalar", "Paneller", "Tema olustur"}
   );
   public final BooleanSetting strike = new BooleanSetting("Modul Kenarligi", true);
   public final BooleanSetting filling = new BooleanSetting("Modul Dolgusu", true);
   public final SliderSetting rounding = new SliderSetting("Yuvarlama", 4.0, 0.0, 6.0, 1.0, () -> this.strike.get() || this.filling.get());
   public final SliderSetting alphaModules = new SliderSetting("Modul Saydamligi", 20.0, 10.0, 40.0, 1.0, () -> this.strike.get() || this.filling.get());

   public ClickGUI() {
      this.addSettings(new Setting[]{this.colorGUI, this.alpha, this.blur, this.blurSetting, this.strike, this.filling, this.rounding, this.alphaModules});
   }

   public Color getGuiColor() {
      String var1 = this.colorGUI.get();
      switch (var1) {
         case "Koyu":
            return new Color(0, 0, 0, this.alpha.get().intValue());
         case "Acik-Siyah":
         default:
            return new Color(17, 15, 28, this.alpha.get().intValue());
      }
   }

   @Override
   public int getBindCode() {
      return this.getClass().getAnnotation(FunctionAnnotation.class).key();
   }

   @Override
   public void onEvent(Event event) {
   }

   @Override
   public void onEnable() {
      this.setState(false);
      super.onEnable();
   }
}
