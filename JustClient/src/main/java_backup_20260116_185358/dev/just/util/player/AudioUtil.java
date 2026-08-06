package dev.just.util.player;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import net.minecraft.util.Identifier;
import net.minecraft.resource.Resource;

public class AudioUtil implements IMinecraft {
   public static void playSound(String name) {
      Identifier id = Identifier.of("justclient", "sounds/" + name);
      mc.execute(() -> {
         try {
            Optional<Resource> resourceOpt = mc.getResourceManager().getResource(id);
            if (resourceOpt.isEmpty()) {
               System.err.println("Sound resource not found: " + id);
               return;
            }

            Resource resource = resourceOpt.get();

            try (InputStream in = resource.getInputStream()) {
               byte[] soundData = in.readAllBytes();
               float volume = (float)Manager.FUNCTION_MANAGER.clientSounds.volume.get().intValue() / 100.0F;
               new Thread(() -> playSoundFromBytes(soundData, volume)).start();
            }
         } catch (Exception var8) {
            System.err.println("Error loading sound: " + var8.getMessage());
         }
      });
   }

   private static void playSoundFromBytes(byte[] soundData, float volume) {
      try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundData))) {
         Clip clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         FloatControl gainControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
         float dB = (float)(20.0 * Math.log10((double)volume));
         gainControl.setValue(Math.min(Math.max(dB, gainControl.getMinimum()), gainControl.getMaximum()));
         clip.start();
         clip.addLineListener(ex -> {
            if (ex.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
               clip.close();
            }
         });
      } catch (Exception var8) {
         System.err.println("Error playing sound: " + var8.getMessage());
      }
   }
}
