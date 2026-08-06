package dev.just.manager.macroManager;

public class Macro {
   private String message;
   private int key;

   public Macro(String message, int key) {
      this.message = message;
      this.key = key;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
   }
}
