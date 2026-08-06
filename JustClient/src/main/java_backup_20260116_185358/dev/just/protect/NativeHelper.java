package dev.just.protect;

import dev.just.manager.Manager;

public class NativeHelper {
   public static void setProfile() {
      Manager.USER_PROFILE = new UserProfile("JustPlayer", "Kullanıcı", "19.12.2025");
   }
}
