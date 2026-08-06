package dev.just.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
   public static List<String> getAllKeys() {
      List<String> keys = new ArrayList<>();

      for (int i = 1; i <= 8; i++) {
         keys.add("MOUSE" + i);
      }

      for (int key = 32; key <= 348; key++) {
         String name = keyMappings(key);
         if (!name.equals("NONE") && !keys.contains(name)) {
            keys.add(name);
         }
      }

      for (int i = 0; i <= 9; i++) {
         keys.add("NUM" + i);
      }

      keys.add("NUM.");
      keys.add("NUM/");
      keys.add("NUM*");
      keys.add("NUM-");
      keys.add("NUM+");
      keys.add("NUMENTER");
      keys.add("NUM=");
      keys.add("LSHIFT");
      keys.add("LCONTROL");
      keys.add("LALT");
      keys.add("LSUPER");
      keys.add("RSHIFT");
      keys.add("RCONTROL");
      keys.add("RALT");
      keys.add("RSUPER");
      keys.add("MENU");

      for (int i = 1; i <= 12; i++) {
         keys.add("F" + i);
      }

      return keys;
   }

   public static String keyMappings(int keyCode) {
      if (keyCode < -1) {
         int mouseButton = -keyCode - 2;

         return switch (mouseButton) {
            case 0 -> "MOUSE1";
            case 1 -> "MOUSE2";
            case 2 -> "MOUSE3";
            case 3 -> "MOUSE4";
            case 4 -> "MOUSE5";
            case 5 -> "MOUSE6";
            case 6 -> "MOUSE7";
            case 7 -> "MOUSE8";
            default -> "NONE";
         };
      } else {
         return switch (keyCode) {
            case 32 -> "SPACE";
            default -> "NONE";
            case 39 -> "'";
            case 44 -> ",";
            case 45 -> "-";
            case 46 -> ".";
            case 47 -> "/";
            case 48 -> "0";
            case 49 -> "1";
            case 50 -> "2";
            case 51 -> "3";
            case 52 -> "4";
            case 53 -> "5";
            case 54 -> "6";
            case 55 -> "7";
            case 56 -> "8";
            case 57 -> "9";
            case 59 -> ";";
            case 61 -> "=";
            case 65 -> "A";
            case 66 -> "B";
            case 67 -> "C";
            case 68 -> "D";
            case 69 -> "E";
            case 70 -> "F";
            case 71 -> "G";
            case 72 -> "H";
            case 73 -> "I";
            case 74 -> "J";
            case 75 -> "K";
            case 76 -> "L";
            case 77 -> "M";
            case 78 -> "N";
            case 79 -> "O";
            case 80 -> "P";
            case 81 -> "Q";
            case 82 -> "R";
            case 83 -> "S";
            case 84 -> "T";
            case 85 -> "U";
            case 86 -> "V";
            case 87 -> "W";
            case 88 -> "X";
            case 89 -> "Y";
            case 90 -> "Z";
            case 91 -> "[";
            case 92 -> "\\";
            case 93 -> "]";
            case 96 -> "`";
            case 256 -> "ESCAPE";
            case 257 -> "ENTER";
            case 258 -> "TAB";
            case 259 -> "BACKSPACE";
            case 260 -> "INSERT";
            case 261 -> "DELETE";
            case 262 -> "RIGHT";
            case 263 -> "LEFT";
            case 264 -> "DOWN";
            case 265 -> "UP";
            case 266 -> "PAGEUP";
            case 267 -> "PAGEDOWN";
            case 268 -> "HOME";
            case 269 -> "END";
            case 280 -> "CAPSLOCK";
            case 281 -> "SCROLLLOCK";
            case 282 -> "NUMLOCK";
            case 283 -> "PRINTSCREEN";
            case 284 -> "PAUSE";
            case 290 -> "F1";
            case 291 -> "F2";
            case 292 -> "F3";
            case 293 -> "F4";
            case 294 -> "F5";
            case 295 -> "F6";
            case 296 -> "F7";
            case 297 -> "F8";
            case 298 -> "F9";
            case 299 -> "F10";
            case 300 -> "F11";
            case 301 -> "F12";
            case 320 -> "NUM0";
            case 321 -> "NUM1";
            case 322 -> "NUM2";
            case 323 -> "NUM3";
            case 324 -> "NUM4";
            case 325 -> "NUM5";
            case 326 -> "NUM6";
            case 327 -> "NUM7";
            case 328 -> "NUM8";
            case 329 -> "NUM9";
            case 330 -> "NUM.";
            case 331 -> "NUM/";
            case 332 -> "NUM*";
            case 333 -> "NUM-";
            case 334 -> "NUM+";
            case 335 -> "NUMENTER";
            case 336 -> "NUM=";
            case 340 -> "LSHIFT";
            case 341 -> "LCONTROL";
            case 342 -> "LALT";
            case 343 -> "LSUPER";
            case 344 -> "RSHIFT";
            case 345 -> "RCONTROL";
            case 346 -> "RALT";
            case 347 -> "RSUPER";
            case 348 -> "MENU";
         };
      }
   }

   public static int keyCode(String name) {
      if (name == null || name.isEmpty()) return -1;

      // Mouse buttons
      if (name.startsWith("MOUSE")) {
         try {
            int button = Integer.parseInt(name.substring(5));
            return -(button + 2);
         } catch (NumberFormatException e) {
            return -1;
         }
      }

      // Key mappings (reverse of keyMappings method)
      return switch (name) {
         case "SPACE" -> 32;
         case "'" -> 39;
         case "," -> 44;
         case "-" -> 45;
         case "." -> 46;
         case "/" -> 47;
         case "0" -> 48;
         case "1" -> 49;
         case "2" -> 50;
         case "3" -> 51;
         case "4" -> 52;
         case "5" -> 53;
         case "6" -> 54;
         case "7" -> 55;
         case "8" -> 56;
         case "9" -> 57;
         case ";" -> 59;
         case "=" -> 61;
         case "A" -> 65;
         case "B" -> 66;
         case "C" -> 67;
         case "D" -> 68;
         case "E" -> 69;
         case "F" -> 70;
         case "G" -> 71;
         case "H" -> 72;
         case "I" -> 73;
         case "J" -> 74;
         case "K" -> 75;
         case "L" -> 76;
         case "M" -> 77;
         case "N" -> 78;
         case "O" -> 79;
         case "P" -> 80;
         case "Q" -> 81;
         case "R" -> 82;
         case "S" -> 83;
         case "T" -> 84;
         case "U" -> 85;
         case "V" -> 86;
         case "W" -> 87;
         case "X" -> 88;
         case "Y" -> 89;
         case "Z" -> 90;
         case "[" -> 91;
         case "\\" -> 92;
         case "]" -> 93;
         case "`" -> 96;
         case "ESCAPE" -> 256;
         case "ENTER" -> 257;
         case "TAB" -> 258;
         case "BACKSPACE" -> 259;
         case "INSERT" -> 260;
         case "DELETE" -> 261;
         case "RIGHT" -> 262;
         case "LEFT" -> 263;
         case "DOWN" -> 264;
         case "UP" -> 265;
         case "PAGEUP" -> 266;
         case "PAGEDOWN" -> 267;
         case "HOME" -> 268;
         case "END" -> 269;
         case "CAPSLOCK" -> 280;
         case "SCROLLLOCK" -> 281;
         case "NUMLOCK" -> 282;
         case "PRINTSCREEN" -> 283;
         case "PAUSE" -> 284;
         case "F1" -> 290;
         case "F2" -> 291;
         case "F3" -> 292;
         case "F4" -> 293;
         case "F5" -> 294;
         case "F6" -> 295;
         case "F7" -> 296;
         case "F8" -> 297;
         case "F9" -> 298;
         case "F10" -> 299;
         case "F11" -> 300;
         case "F12" -> 301;
         case "NUM0" -> 320;
         case "NUM1" -> 321;
         case "NUM2" -> 322;
         case "NUM3" -> 323;
         case "NUM4" -> 324;
         case "NUM5" -> 325;
         case "NUM6" -> 326;
         case "NUM7" -> 327;
         case "NUM8" -> 328;
         case "NUM9" -> 329;
         case "NUM." -> 330;
         case "NUM/" -> 331;
         case "NUM*" -> 332;
         case "NUM-" -> 333;
         case "NUM+" -> 334;
         case "NUMENTER" -> 335;
         case "NUM=" -> 336;
         case "LSHIFT" -> 340;
         case "LCONTROL" -> 341;
         case "LALT" -> 342;
         case "LSUPER" -> 343;
         case "RSHIFT" -> 344;
         case "RCONTROL" -> 345;
         case "RALT" -> 346;
         case "RSUPER" -> 347;
         case "MENU" -> 348;
         default -> {
            // Try reflection for unknown keys
            try {
               Field field = GLFW.class.getDeclaredField("GLFW_KEY_" + name);
               yield field.getInt(null);
            } catch (Exception e) {
               yield -1;
            }
         }
      };
   }
}
