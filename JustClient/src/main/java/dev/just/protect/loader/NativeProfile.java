package dev.just.protect.loader;

public class NativeProfile {
   private static String username = System.getProperty("justclient.username", "Yok");
   private static String role = System.getProperty("justclient.role", "Bilinmeyen rol");
   private static String expiryDate = System.getProperty("justclient.expiry", "Bilinmeyen tarih!");

   public NativeProfile(String name, String role, String expiry) {
      username = name;
      NativeProfile.role = role;
      expiryDate = expiry;
   }

   public static String getName() {
      return username;
   }

   public static String getRole() {
      return role;
   }

   public static String getExpiryDate() {
      return expiryDate;
   }
}
