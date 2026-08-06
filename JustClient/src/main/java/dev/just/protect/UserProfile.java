package dev.just.protect;

public class UserProfile {
   private final String name;
   private final String role;
   private final String expiry;

   public UserProfile(String name, String role, String expiry) {
      this.name = name;
      this.role = role;
      this.expiry = expiry;
   }

   public String getName() {
      return this.name;
   }

   public String getExpiry() {
      return this.expiry;
   }

   public String getRole() {
      return this.role;
   }
}
