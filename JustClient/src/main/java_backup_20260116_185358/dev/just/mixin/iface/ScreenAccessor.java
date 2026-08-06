package dev.just.mixin.iface;

import java.util.List;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.Selectable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Screen.class})
public interface ScreenAccessor {
   @Accessor
   List<Drawable> getDrawables();

   @Accessor
   List<Element> getChildren();

   @Accessor
   List<Selectable> getSelectables();
}
