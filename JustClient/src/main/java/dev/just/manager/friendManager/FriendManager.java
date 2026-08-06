package dev.just.manager.friendManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.MinecraftClient;
import dev.just.protect.runtime.I0O1l0I1;

public final class FriendManager {
   public final List<Friend> friends = new CopyOnWriteArrayList<>();
   private final Path FRIENDS_FILE = Paths.get(Objects.requireNonNull(MinecraftClient.getInstance().runDirectory).getAbsolutePath(), "files", "friends.ew");

   public void init() {
      try {
         Files.createDirectories(this.FRIENDS_FILE.getParent());
         if (Files.notExists(this.FRIENDS_FILE)) {
            Files.createFile(this.FRIENDS_FILE);
         } else {
            this.readFriends();
         }
      } catch (IOException var2) {
         System.err.println(I0O1l0I1.b("RnJpZW5kTWFuYWdlciBiYcWfbGF0bWEgaGF0YXPEsTogIA==") + var2.getMessage());
      }
   }

   public void addFriend(String name) {
      if (name != null && !name.isBlank() && !this.isFriend(name)) {
         this.friends.add(new Friend(name));
         this.updateFile();
      }
   }

   public boolean isFriend(String name) {
      return name != null && this.friends.stream().anyMatch(f -> f.getName().equalsIgnoreCase(name));
   }

   public void removeFriend(String name) {
      if (name != null) {
         this.friends.removeIf(f -> f.getName().equalsIgnoreCase(name));
         this.updateFile();
      }
   }

   public void clearFriends() {
      this.friends.clear();
      this.updateFile();
   }

   public List<Friend> getFriends() {
      return List.copyOf(this.friends);
   }

   private void updateFile() {
      try {
         Files.write(this.FRIENDS_FILE, this.friends.stream().map(Friend::getName).toList(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
      } catch (IOException var2) {
         System.err.println(I0O1l0I1.b("QXJrYWRhxZ8gZG9zeWFzxLEgZ8O8bmNlbGxlbmVtZWRpOiA=") + var2.getMessage());
      }
   }

   private void readFriends() {
      try {
         Files.lines(this.FRIENDS_FILE, StandardCharsets.UTF_8).map(String::trim).filter(s -> !s.isEmpty()).forEach(name -> this.friends.add(new Friend(name)));
      } catch (IOException var2) {
         System.err.println(I0O1l0I1.b("QXJrYWRhxZ8gZG9zeWFzxLEgb2t1bmFtYWTEsTogIA==") + var2.getMessage());
      }
   }
}
