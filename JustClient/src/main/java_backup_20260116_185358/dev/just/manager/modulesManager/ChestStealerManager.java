package dev.just.manager.modulesManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;

public class ChestStealerManager {
   private final File file;
   private final Set<Item> whitelist = new HashSet<>();

   public ChestStealerManager() {
      this.file = new File(new File(Objects.requireNonNull(MinecraftClient.getInstance().runDirectory), "files/modules"), "cheststealer.ew");
      this.load();
      if (this.whitelist.isEmpty()) {
         this.addDefaultItems();
         this.save();
      }
   }

   private void addDefaultItems() {
      this.addItem("minecraft:totem_of_undying");
      this.addItem("minecraft:player_head");
   }

   public boolean addItem(String name) {
      Identifier id = Identifier.tryParse(name);
      if (id != null && Registries.ITEM.containsId(id)) {
         Item item = (Item)Registries.ITEM.get(id);
         if (this.whitelist.add(item)) {
            this.save();
            return true;
         }
      }

      return false;
   }

   public boolean removeItem(String name) {
      Identifier id = Identifier.tryParse(name);
      if (id != null && Registries.ITEM.containsId(id)) {
         Item item = (Item)Registries.ITEM.get(id);
         if (this.whitelist.remove(item)) {
            this.save();
            return true;
         }
      }

      return false;
   }

   public boolean isAllowed(Item item) {
      return this.whitelist.contains(item);
   }

   public Set<Item> getWhitelist() {
      return this.whitelist;
   }

   private void save() {
      try {
         this.file.getParentFile().mkdirs();

         try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file))) {
            for (Item item : this.whitelist) {
               Identifier id = Registries.ITEM.getId(item);
               writer.write(id.toString());
               writer.newLine();
            }
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }
   }

   private void load() {
      this.whitelist.clear();
      if (this.file.exists()) {
         String line;
         try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            while ((line = reader.readLine()) != null) {
               Identifier id = Identifier.tryParse(line.trim());
               if (id != null && Registries.ITEM.containsId(id)) {
                  this.whitelist.add((Item)Registries.ITEM.get(id));
               }
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }
   }
}
