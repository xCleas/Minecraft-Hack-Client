import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Script to remap intermediary names to yarn names in Java source files.
 * This converts class_XXX, method_XXX, field_XXX to their proper yarn names.
 */
public class Remap {
    // Path to the yarn mappings file
    static final String MAPPINGS_FILE = "C:\\Users\\stust\\.gradle\\caches\\fabric-loom\\1.21.4\\net.fabricmc.yarn.1_21_4.1.21.4+build.8-v2\\mappings.tiny";

    // Source directory
    static final String SRC_DIR = "C:\\Users\\stust\\eclipse-workspace\\ExonClient-Source\\src\\main\\java";

    static Map<String, String> classMap = new HashMap<>();
    static Map<String, String> methodMap = new HashMap<>();
    static Map<String, String> fieldMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Loading mappings...");
        parseMappings();
        System.out.println("Loaded " + classMap.size() + " class mappings, " +
                          methodMap.size() + " method mappings, " +
                          fieldMap.size() + " field mappings");

        // Find all Java files
        List<Path> javaFiles = new ArrayList<>();
        Files.walk(Paths.get(SRC_DIR))
             .filter(p -> p.toString().endsWith(".java"))
             .forEach(javaFiles::add);

        System.out.println("Found " + javaFiles.size() + " Java files");

        // Remap each file
        int changed = 0;
        for (Path filepath : javaFiles) {
            if (remapFile(filepath)) {
                System.out.println("Remapped: " + filepath);
                changed++;
            }
        }

        System.out.println("\nDone! Changed " + changed + " files.");
    }

    static void parseMappings() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(MAPPINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("tiny")) continue;

                String[] parts = line.split("\t");

                if (line.startsWith("c\t") && parts.length >= 4) {
                    // Class mapping: c official intermediary named
                    String intermediary = parts[2]; // e.g., net/minecraft/class_310
                    String named = parts[3]; // e.g., net/minecraft/client/MinecraftClient

                    // Extract simple class name
                    String intSimple = intermediary.substring(intermediary.lastIndexOf('/') + 1);
                    String namedSimple = named.substring(named.lastIndexOf('/') + 1);

                    classMap.put(intSimple, namedSimple);
                    classMap.put(intermediary.replace('/', '.'), named.replace('/', '.'));

                } else if (line.startsWith("\tf\t") && parts.length >= 5) {
                    // Field mapping
                    String intermediary = parts[3];
                    String named = parts[4];
                    if (intermediary.startsWith("field_")) {
                        fieldMap.put(intermediary, named);
                    }

                } else if (line.startsWith("\tm\t") && parts.length >= 5) {
                    // Method mapping
                    String intermediary = parts[3];
                    String named = parts[4];
                    if (intermediary.startsWith("method_")) {
                        methodMap.put(intermediary, named);
                    }
                }
            }
        }
    }

    static boolean remapFile(Path filepath) throws Exception {
        String content = Files.readString(filepath);
        String original = content;

        // Replace class names (both in imports and in code)
        for (Map.Entry<String, String> entry : classMap.entrySet()) {
            String intermediary = entry.getKey();
            String named = entry.getValue();
            if (intermediary.contains("class_")) {
                content = content.replace(intermediary, named);
            }
        }

        // Replace method names
        for (Map.Entry<String, String> entry : methodMap.entrySet()) {
            String intermediary = entry.getKey();
            String named = entry.getValue();
            content = content.replaceAll("\\b" + Pattern.quote(intermediary) + "\\b", named);
        }

        // Replace field names
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            String intermediary = entry.getKey();
            String named = entry.getValue();
            content = content.replaceAll("\\b" + Pattern.quote(intermediary) + "\\b", named);
        }

        if (!content.equals(original)) {
            Files.writeString(filepath, content);
            return true;
        }
        return false;
    }
}
