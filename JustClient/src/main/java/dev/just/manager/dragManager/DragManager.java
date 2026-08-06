package dev.just.manager.dragManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.just.manager.IMinecraft;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import net.minecraft.client.MinecraftClient;
import dev.just.protect.runtime.I0O1l0I1;

public class DragManager implements IMinecraft {
   public static LinkedHashMap<String, Dragging> draggables = new LinkedHashMap<>();
   public final File DRAG_DATA = new File(MinecraftClient.getInstance().runDirectory, "\\files\\drag.ew");
   private final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

   public void init() {
      if (!this.DRAG_DATA.exists()) {
         System.out.println(I0O1l0I1.b("U8O8csO8a2xlbmViaWxpciDDtsSfZSBwb3ppc3lvbiBkb3N5YXPEsSBidWx1bmFtYWTEsS4gS2F5xLF0IHNvbnJhc8SxIHllbmkgYmlyIGRvc3lhIG9sdcWfdHVydWxhY2FrLg=="));
      } else {
         try {
            Dragging[] loadedDrags = (Dragging[])this.GSON.fromJson(Files.readString(this.DRAG_DATA.toPath()), Dragging[].class);
            if (loadedDrags != null) {
               for (Dragging dragging : loadedDrags) {
                  if (dragging != null) {
                     Dragging currentDrag = draggables.get(dragging.getName());
                     if (currentDrag != null) {
                        currentDrag.setX(dragging.getX());
                        currentDrag.setY(dragging.getY());
                        draggables.put(dragging.getName(), currentDrag);
                     } else {
                        draggables.put(dragging.getName(), dragging);
                     }
                  }
               }

               System.out.println(I0O1l0I1.b("U8O8csO8a2xlbmViaWxpciDDtsSfZWxlcmluIHBvemlzeW9ubGFyxLEgZG9zeWFkYW4gecO8a2xlbmRpLg=="));
            } else {
               System.out.println(I0O1l0I1.b("RG9zeWFkYWtpIHZlcmlsZXIgYm/FnyB2ZXlhIGhhdGFsxLEu"));
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }
   }

   public void save() {
      if (!this.DRAG_DATA.exists()) {
         this.DRAG_DATA.getParentFile().mkdirs();
      }

      try (FileWriter writer = new FileWriter(this.DRAG_DATA)) {
         writer.write(this.GSON.toJson(draggables.values()));
         System.out.println(I0O1l0I1.b("U8O8csO8a2xlbmViaWxpciDDtsSfZSBwb3ppc3lvbmxhcsSxIGJhxZ9hcsSxeWxhIGtheWRlZGlsZGk6IA==") + this.DRAG_DATA.getAbsolutePath());
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   public void reset() {
      float off = 10.0F;

      for (Dragging dragging : draggables.values()) {
         float newX = (float)(dragging.getDefaultX() + 10);
         float newY = (float)dragging.getDefaultY() + off;
         dragging.setX(newX);
         dragging.setY(newY);
         dragging.targetX = newX;
         dragging.targetY = newY;
         off += dragging.getHeight() + 15.0F;
      }

      this.save();
   }
}
