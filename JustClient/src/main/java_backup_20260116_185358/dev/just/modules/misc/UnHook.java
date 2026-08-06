package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BindSetting;
import dev.just.modules.setting.Setting;
import dev.just.screens.unhook.UnHookScreen;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "UnHook",
   keywords = {"SelfDestruct"},
   desc = "QUFDIGtvbnRyb2xkZW4gZ2XDp21layBpw6dpbiBoaWxleWkgZGV2cmUgZMSxxZ/EsSBixLFyYWvEsXI=",
   type = Type.Misc
)
public class UnHook extends Function {
   public static BindSetting unHookKey = new BindSetting(Strings.b("R2VyaSBkw7Zuw7zFnyB0dcWfdQ=="), 260);
   public static final List<Function> functionsToBack = new CopyOnWriteArrayList<>();

   public UnHook() {
      this.addSettings(new Setting[]{unHookKey});
   }

   @Override
   public void onEvent(Event event) {
   }

   @Override
   protected void onEnable() {
      mc.setScreen(new UnHookScreen());
      super.onEnable();
   }

   public void onUnhook() {
      functionsToBack.clear();

      for (int i = 0; i < Manager.FUNCTION_MANAGER.getFunctions().size(); i++) {
         Function function = Manager.FUNCTION_MANAGER.getFunctions().get(i);
         if (function.state && function != this) {
            functionsToBack.add(function);
            function.setState(false);
         }
      }

      File folder = new File("C:\\JustClient");
      if (folder.exists()) {
         try {
            Path folderPathObj = folder.toPath();
            DosFileAttributeView attributes = Files.getFileAttributeView(folderPathObj, DosFileAttributeView.class);
            attributes.setHidden(true);
         } catch (IOException var4) {
         }
      }

      this.toggle();
   }
}
