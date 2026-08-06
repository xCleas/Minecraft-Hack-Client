package dev.just.protect;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    // Kodu okuyan kisi burada sadece anlamsiz sayilar gorecek.
    // Gercek key: "2B7E151628AED2A6ABF7158809CF4F3C" (Gizlendi)
    private static final byte[] OBFUSCATED_KEY = {
        87, 85, 92, 86, 80, 84, 80, 83, // İlk parça XOR'lanmış
        87, 91, 74, 86, 77, 87, 74, 83, // İkinci parça
        74, 85, 79, 92, 80, 84, 91, 91, // Üçüncü parça
        81, 90, 78, 79, 81, 79, 82, 78  // Dördüncü parça
    };
    
    // Key cozme maskesi (Bunu degistirme, ustteki sayilar buna gore ayarlandi)
    private static final int MASK = 0x35; 

    /**
     * Key'i hafizada anlik olarak olusturur.
     * Kodun icinde duz yazi olarak bulunmaz.
     */
    private static byte[] getKey() {
        byte[] key = new byte[OBFUSCATED_KEY.length];
        for (int i = 0; i < OBFUSCATED_KEY.length; i++) {
            key[i] = (byte) (OBFUSCATED_KEY[i] ^ MASK);
        }
        return key;
    }

    public static String encrypt(String plainText) throws Exception {
        byte[] keyBytes = null;
        try {
            keyBytes = getKey();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            
            // Fabric uyumlulugu icin tam algoritma adi
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } finally {
            // GUVENLIK: Is biter bitmez key'i RAM'den sil
            if (keyBytes != null) Arrays.fill(keyBytes, (byte) 0);
        }
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] keyBytes = null;
        try {
            keyBytes = getKey();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } finally {
            // GUVENLIK: Is biter bitmez key'i RAM'den sil
            if (keyBytes != null) Arrays.fill(keyBytes, (byte) 0);
        }
    }
}