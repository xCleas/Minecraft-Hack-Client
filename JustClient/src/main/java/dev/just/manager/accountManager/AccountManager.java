package dev.just.manager.accountManager;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public class AccountManager implements IMinecraft {
   private final File file = new File(MinecraftClient.getInstance().runDirectory, "files/alts.ew");
   private final File lastAltFile = new File(MinecraftClient.getInstance().runDirectory, "files/lastAlt.ew");
   private final List<String> accounts = new ArrayList<>();
   private String lastSelectedAccount;

   public void init() {
      this.accounts.clear();
      if (!this.file.exists()) {
         try {
            this.file.getParentFile().mkdirs();
            this.file.createNewFile();
         } catch (IOException var5) {
            var5.printStackTrace();
            return;
         }
      }

      String line;
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8))) {
         while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
               this.accounts.add(line.trim());
            }
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      this.loadLastAlt();
   }

   public void saveAccounts() {
      try {
         if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.file.createNewFile();
         }

         try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8))) {
            for (String account : this.accounts) {
               writer.write(account);
               writer.newLine();
            }
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   public void addAccount(String account) {
      if (account != null && !account.trim().isEmpty() && !this.accounts.contains(account)) {
         this.accounts.add(account.trim());
         this.saveAccounts();
      }
   }

   public void removeAccount(String account) {
      if (this.accounts.remove(account)) {
         this.saveAccounts();
      }
   }

   public void clearAll() {
      this.accounts.clear();
      this.saveAccounts();
   }

   public List<String> getAccounts() {
      return new ArrayList<>(this.accounts);
   }

   public void setLastSelectedAccount(String account) {
      this.lastSelectedAccount = account;
      this.saveLastAlt();
   }

   public String getLastSelectedAccount() {
      return this.lastSelectedAccount;
   }

   public void saveLastAlt() {
      if (this.lastSelectedAccount != null) {
         try {
            if (!this.lastAltFile.exists()) {
               this.lastAltFile.getParentFile().mkdirs();
               this.lastAltFile.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.lastAltFile), StandardCharsets.UTF_8))) {
               writer.write(this.lastSelectedAccount);
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }
   }

   public void loadLastAlt() {
      if (this.lastAltFile.exists()) {
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.lastAltFile), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
               this.lastSelectedAccount = line.trim();
               String selected = this.lastSelectedAccount;
               ClientManager.loginAccount(selected);
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }
   }
}
