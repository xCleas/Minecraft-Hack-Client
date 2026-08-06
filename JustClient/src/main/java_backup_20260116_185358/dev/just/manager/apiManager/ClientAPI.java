package dev.just.manager.apiManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import dev.just.protect.runtime.Strings;

public class ClientAPI {
   private final String host;
   private final int port;
   private final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();
   private final ExecutorService executor = Executors.newCachedThreadPool();
   private static final String SECRET_KEY = "k0tikBolomotik";

   public ClientAPI(String host, int port) {
      this.host = host;
      this.port = port;
   }

   public void isClientUserAsync(String nick, Consumer<Boolean> callback) {
      if (this.cache.containsKey(nick)) {
         callback.accept(this.cache.get(nick));
      } else {
         this.executor.submit(() -> {
            boolean result = this.isClientUser(nick);
            this.cache.put(nick, result);
            callback.accept(result);
         });
      }
   }

   public boolean isClientUser(String nick) {
      try {
         boolean var7;
         try (
            Socket socket = new Socket(this.host, this.port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
         ) {
            String hash = this.sha256("k0tikBolomotik" + nick);
            out.write("sorgula:" + hash + ":" + nick + "\n");
            out.flush();
            String response = in.readLine();
            var7 = "true".equalsIgnoreCase(response);
         }

         return var7;
      } catch (IOException var14) {
         return false;
      }
   }

   public void addPlayer(String nick) {
      this.executor.submit(() -> this.sendCommand("ekle", nick));
   }

   public void removePlayer(String nick) {
      this.executor.submit(() -> this.sendCommand("sil", nick));
   }

   private void sendCommand(String cmd, String nick) {
      try (
         Socket socket = new Socket(this.host, this.port);
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
      ) {
         String hash = this.sha256("k0tikBolomotik" + nick);
         out.write(cmd + ":" + hash + ":" + nick + "\n");
         out.flush();
      } catch (IOException var11) {
      }
   }

   private String sha256(String input) {
      try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
         StringBuilder sb = new StringBuilder();

         for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
         }

         return sb.toString();
      } catch (NoSuchAlgorithmException var9) {
         throw new RuntimeException(Strings.b("xZ5pZnJlbGVtZSBhbGdvcml0bWFzxLEgYnVsdW5hbWFkxLEh"), var9);
      }
   }

   public void shutdown() {
      this.executor.shutdownNow();
   }
}
