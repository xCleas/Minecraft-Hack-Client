package dev.just.modules.misc;

import dev.just.com.discord.DiscordIPC;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "DiscordRPC",
   desc = "RGlzY29yZCdkYSBveXVuIGFrdGl2aXRlc2luaSBnw7ZzdGVyaXI=",
   type = Type.Misc
)
public class DiscordRCP extends Function {
   private DiscordIPC ipc;
   private volatile boolean started = false;
   private Thread updateThread;
   private long startTime;

   public DiscordRCP() {
      super();
      this.state = true;
   }

   @Override
   public void toggle() {
      if (!this.state) {
         this.state = true;
         this.onEnable();
      }
   }

   @Override
   protected void onEnable() {
      this.startRpc();
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         if (!this.state) {
            this.state = true;
         }
         this.startRpc();
      }
   }

   public synchronized void startRpc() {
      if (this.started) {
         return;
      }

      this.started = true;
      this.startTime = System.currentTimeMillis() / 1000L;

      new Thread(() -> {
         try {
            this.ipc = new DiscordIPC(1456037947039285372L);

            if (this.ipc.connect()) {
               System.out.println(I0O1l0I1.b("W0p1c3RDbGllbnRdIERpc2NvcmQgUlBDIGJhxJ9sYW5kxLEh"));

               // İlk güncelleme
               updatePresence();

               // Güncelleme döngüsü
               this.updateThread = new Thread(() -> {
                  try {
                     while (!Thread.currentThread().isInterrupted() && this.started) {
                        Thread.sleep(5000L);
                        if (this.ipc != null && this.ipc.isConnected()) {
                           updatePresence();
                        }
                     }
                  } catch (InterruptedException e) {
                     Thread.currentThread().interrupt();
                  }
               }, "JustClient-RPC-Update");
               this.updateThread.setDaemon(true);
               this.updateThread.start();

            } else {
               System.err.println(I0O1l0I1.b("W0p1c3RDbGllbnRdIERpc2NvcmQgYnVsdW5hbWFkxLE="));
               this.started = false;
            }
         } catch (Exception e) {
            System.err.println(I0O1l0I1.b("W0p1c3RDbGllbnRdIERpc2NvcmQgUlBDIGJhxZ9sYXTEsWxhbWFkxLE6IA==") + e.getMessage());
            this.started = false;
         }
      }, "JustClient-RPC-Init").start();
   }

   private void updatePresence() {
      if (this.ipc == null || !this.ipc.isConnected()) {
         return;
      }

      try {
         String username = "Unknown";
         try {
            if (Manager.USER_PROFILE != null) {
               username = Manager.USER_PROFILE.getName();
            }
         } catch (Exception ignored) {}

         this.ipc.updatePresence(
            I0O1l0I1.b("SnVzdCBDbGllbnQgT3ludXlvcg=="),           // details
            I0O1l0I1.b("S3VsbGFuxLFjxLE6IA==") + username,        // state
            "icon",                          // largeImageKey
            "Just Client v1.0",              // largeImageText
            this.startTime,                  // startTimestamp
            "Discord",                       // buttonLabel
            "https://discord.gg/kyr6ESA3jn"  // buttonUrl
         );
      } catch (Exception e) {
         // Sessizce devam et
      }
   }

   @Override
   protected void onDisable() {
      this.state = true;
      this.startRpc();
   }

   @Override
   public void load(com.google.gson.JsonObject object) {
      if (object != null) {
         if (object.has("keyIndex")) {
            this.setBindCode(object.get("keyIndex").getAsInt());
         }
      }
      this.state = true;
   }
}
