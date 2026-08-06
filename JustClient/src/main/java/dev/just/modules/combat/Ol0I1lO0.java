package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;

@FunctionAnnotation(name = "Reach", desc = "Vurus menzilinizi artirir.", type = Type.Combat)
public class Ol0I1lO0 extends Function {

    public final SliderSetting reach = new SliderSetting("Menzil", 3.5, 3.0, 6.0, 0.1F);

    public Ol0I1lO0() {
        this.addSettings(new Setting[]{this.reach});
    }

    @Override
    public void onEvent(Event event) {
        // Reach is handled via mixin - this module just holds the setting
    }

    public double getReach() {
        return this.state ? this.reach.get().doubleValue() : 3.0;
    }
}
