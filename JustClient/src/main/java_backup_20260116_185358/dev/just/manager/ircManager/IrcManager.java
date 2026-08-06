package dev.just.manager.ircManager;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextColor;

public class IrcManager {
   private final String cheatName = "ExosWare";
   private final String secretKey = "levinAntiKotopishka";
   private volatile Socket socket;
   private volatile BufferedReader in;
   private volatile BufferedWriter out;
   public volatile String nickname;
   public static final List<String> MESSAGES = new CopyOnWriteArrayList<>();
   private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "IRC-Scheduler");
      t.setDaemon(true);
      return t;
   });
   private final ExecutorService readerService = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "IRC-Reader");
      t.setDaemon(true);
      return t;
   });
   private final ExecutorService writerService = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "IRC-Writer");
      t.setDaemon(true);
      return t;
   });
   private final Object connectLock = new Object();
   private volatile boolean connecting = false;
   private static final int CONNECT_TIMEOUT_MS = 5000;
   private static final long RETRY_DELAY_MS = 15000L;
   private final Set<String> ignoredNicks = ConcurrentHashMap.newKeySet();
   private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

   public void connect(String username) {
      this.nickname = username;
      this.scheduleConnect(0L);
      this.startWriter();
   }

   private void scheduleConnect(long delayMillis) {
      this.scheduler.schedule(this::tryConnect, delayMillis, TimeUnit.MILLISECONDS);
   }

   private void tryConnect() {
      synchronized (this.connectLock) {
         if (this.connecting) {
            return;
         }

         this.connecting = true;
      }

      try {
         this.closeConnection();
         Socket s = new Socket();
         s.connect(new InetSocketAddress("11.1.1", 3025), 5000);
         s.setTcpNoDelay(true);
         s.setKeepAlive(true);
         BufferedReader newIn = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
         BufferedWriter newOut = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
         String nonce = newIn.readLine();
         if (nonce == null || nonce.isEmpty()) {
            throw new IOException("Nonce alinamadi");
         }

         String hmac = this.hmacSha256("levinAntiKotopishka", nonce + this.nickname);
         newOut.write("ExosWare:" + this.nickname + ":" + hmac + "\n");
         newOut.flush();
         this.socket = s;
         this.in = newIn;
         this.out = newOut;
         this.startReading();
         this.logToClient("Baglanti kuruldu");
      } catch (IOException var18) {
         this.logToClient("Baglanti kurulamadi. 15 saniye sonra tekrar denenecek...");
         this.scheduleConnect(15000L);
      } finally {
         synchronized (this.connectLock) {
            this.connecting = false;
         }
      }
   }

   private void startReading() {
      this.readerService.submit(() -> {
         try {
            String line;
            try {
               while (!Thread.currentThread().isInterrupted() && this.in != null && (line = this.in.readLine()) != null) {
                  this.messageClient(line);
               }
            } catch (IOException var5) {
            }
         } finally {
            this.reconnect();
         }
      });
   }

   private void reconnect() {
      this.closeConnection();
      this.scheduleConnect(15000L);
   }

   private void closeConnection() {
      this.closeQuietly(this.in);
      this.closeQuietly(this.out);
      if (this.socket != null) {
         try {
            this.socket.close();
         } catch (IOException var2) {
         }
      }

      this.in = null;
      this.out = null;
      this.socket = null;
   }

   public void messageHost(String msg) {
      if (msg != null && !msg.trim().isEmpty()) {
         this.messageQueue.offer(msg);
         this.messageClient("[IRC • Just] » Sen: " + msg);
      }
   }

   public static void addMessage(String msg) {
      MESSAGES.add(msg);
      if (MESSAGES.size() > 200) {
         MESSAGES.remove(0);
      }
   }

   private void startWriter() {
      this.writerService.submit(() -> {
         while (!Thread.currentThread().isInterrupted()) {
            try {
               String msg = this.messageQueue.take();
               BufferedWriter writer = this.out;
               if (writer != null) {
                  writer.write(msg);
                  writer.newLine();
                  writer.flush();
               } else {
                  this.messageQueue.offer(msg);
                  Thread.sleep(1000L);
               }
            } catch (InterruptedException var3) {
               break;
            } catch (IOException var4) {
               this.reconnect();
            }
         }
      });
   }

   public void messageClient(String string) {
      if (IMinecraft.mc != null && IMinecraft.mc.player != null && IMinecraft.mc.world != null && IMinecraft.mc.inGameHud != null) {
         String[] parts = string.split(":", 2);
         if (parts.length <= 1 || !this.isIgnored(parts[0].trim())) {
            IMinecraft.mc.inGameHud.getChatHud().addMessage(this.applyGradient(string));
            addMessage(string);
         }
      }
   }

   private Text applyGradient(String string) {
      MutableText component = Text.empty();
      int splitIndex = string.indexOf(" » ");
      String prefix = splitIndex != -1 ? string.substring(0, splitIndex) : string;
      String rest = splitIndex != -1 ? string.substring(splitIndex) : "";
      int length = Math.max(prefix.length(), 1);

      for (int i = 0; i < length; i++) {
         float ratio = (float)i / (float)(length - 1);
         int rgb = this.blendColors(8421504, 16777215, ratio);
         component.append(
            Text.literal(String.valueOf(prefix.charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)))
         );
      }

      if (!rest.isEmpty()) {
         component.append(Text.literal(rest).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(16777215))));
      }

      return component;
   }

   private int blendColors(int c1, int c2, float ratio) {
      ratio = Math.max(0.0F, Math.min(1.0F, ratio));
      int r = (int)((float)(c1 >> 16 & 0xFF) * (1.0F - ratio) + (float)(c2 >> 16 & 0xFF) * ratio);
      int g = (int)((float)(c1 >> 8 & 0xFF) * (1.0F - ratio) + (float)(c2 >> 8 & 0xFF) * ratio);
      int b = (int)((float)(c1 & 0xFF) * (1.0F - ratio) + (float)(c2 & 0xFF) * ratio);
      return r << 16 | g << 8 | b;
   }

   public void ignoreNick(String nick) {
      if (nick != null) {
         this.ignoredNicks.add(nick.toLowerCase());
      }
   }

   public void unignoreNick(String nick) {
      if (nick != null) {
         this.ignoredNicks.remove(nick.toLowerCase());
      }
   }

   public boolean isIgnored(String nick) {
      return nick != null && this.ignoredNicks.contains(nick.toLowerCase());
   }

   public Set<String> getIgnoredNicks() {
      return Collections.unmodifiableSet(this.ignoredNicks);
   }

   private void logToClient(String msg) {
      System.out.println(msg);
      if (Manager.FUNCTION_MANAGER.irc.state) {
         this.messageClient(msg);
      }
   }

   private void closeQuietly(Closeable c) {
      try {
         if (c != null) {
            c.close();
         }
      } catch (IOException var3) {
      }
   }

   private String hmacSha256(String key, String data) {
      try {
         Mac mac = Mac.getInstance("HmacSHA256");
         mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
         byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
         StringBuilder sb = new StringBuilder();

         for (byte b : hash) {
            sb.append(String.format("%02x", b));
         }

         return sb.toString();
      } catch (Exception var10) {
         return "";
      }
   }

   public void shutdown() {
      CompletableFuture.runAsync(() -> {
         try {
            this.readerService.shutdownNow();
         } catch (Exception var4) {
         }

         try {
            this.writerService.shutdownNow();
         } catch (Exception var3) {
         }

         try {
            this.scheduler.shutdownNow();
         } catch (Exception var2) {
         }

         this.closeConnection();
      });
   }
}
