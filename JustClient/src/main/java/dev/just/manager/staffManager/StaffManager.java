package dev.just.manager.staffManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public class StaffManager {
   private static final List<String> staffNames = new ArrayList<>();
   private final File file = new File(MinecraftClient.getInstance().runDirectory, "\\files\\staff.ew");

   public void init() throws Exception {
      if (!this.file.exists()) {
         this.file.createNewFile();
      } else {
         this.readStaffs();
      }
   }

   public void addStaff(String name) {
      staffNames.add(name);
      this.updateFile();
   }

   public boolean isStaff(String staffName) {
      return staffNames.stream().anyMatch(staff -> staff.equals(staffName));
   }

   public void removeStaff(String name) {
      staffNames.removeIf(friend -> friend.equalsIgnoreCase(name));
   }

   public void clearStaffs() {
      staffNames.clear();
      this.updateFile();
   }

   public List<String> getStaffNames() {
      return staffNames;
   }

   public void updateFile() {
      try {
         StringBuilder builder = new StringBuilder();
         staffNames.forEach(friend -> builder.append(friend).append("\n"));
         Files.write(this.file.toPath(), builder.toString().getBytes());
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public void reload() {
      staffNames.clear();
      this.readStaffs();
   }

   private void readStaffs() {
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(this.file.getAbsolutePath()))));

         String line;
         while ((line = reader.readLine()) != null) {
            staffNames.add(line);
         }

         reader.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
