#!/usr/bin/env python3
"""
Script to remap intermediary names to yarn names in Java source files.
This converts class_XXX, method_XXX, field_XXX to their proper yarn names.
"""

import os
import re
import sys

# Path to the yarn mappings file
MAPPINGS_FILE = r"C:\Users\stust\.gradle\caches\fabric-loom\1.21.4\net.fabricmc.yarn.1_21_4.1.21.4+build.8-v2\mappings.tiny"

# Source directory
SRC_DIR = r"C:\Users\stust\eclipse-workspace\ExonClient-Source\src\main\java"

def parse_mappings(mappings_file):
    """Parse the tiny mappings file and return dictionaries for classes, methods, and fields."""
    class_map = {}  # intermediary -> yarn
    method_map = {}  # intermediary -> yarn
    field_map = {}  # intermediary -> yarn

    current_class_intermediary = None
    current_class_named = None

    with open(mappings_file, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.rstrip('\n')
            if not line or line.startswith('tiny'):
                continue

            parts = line.split('\t')

            if line.startswith('c\t'):
                # Class mapping: c official intermediary named
                if len(parts) >= 4:
                    intermediary = parts[2]  # e.g., net/minecraft/class_310
                    named = parts[3]  # e.g., net/minecraft/client/MinecraftClient

                    # Extract simple class name
                    int_simple = intermediary.split('/')[-1]
                    named_simple = named.split('/')[-1]

                    class_map[int_simple] = named_simple
                    class_map[intermediary.replace('/', '.')] = named.replace('/', '.')

                    current_class_intermediary = int_simple
                    current_class_named = named_simple

            elif line.startswith('\tf\t'):
                # Field mapping: \tf descriptor official intermediary named
                if len(parts) >= 5:
                    intermediary = parts[3]
                    named = parts[4]
                    if intermediary.startswith('field_'):
                        field_map[intermediary] = named

            elif line.startswith('\tm\t'):
                # Method mapping: \tm descriptor official intermediary named
                if len(parts) >= 5:
                    intermediary = parts[3]
                    named = parts[4]
                    if intermediary.startswith('method_'):
                        method_map[intermediary] = named

    return class_map, method_map, field_map

def remap_file(filepath, class_map, method_map, field_map):
    """Remap a single Java source file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original = content

    # Replace class names (both in imports and in code)
    for intermediary, named in class_map.items():
        if 'class_' in intermediary:
            # For full package paths like net.minecraft.class_310
            content = content.replace(intermediary, named)

    # Replace method names
    for intermediary, named in method_map.items():
        # Replace method calls like .method_123(
        content = re.sub(r'\b' + re.escape(intermediary) + r'\b', named, content)

    # Replace field names
    for intermediary, named in field_map.items():
        content = re.sub(r'\b' + re.escape(intermediary) + r'\b', named, content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    print("Loading mappings...")
    class_map, method_map, field_map = parse_mappings(MAPPINGS_FILE)
    print(f"Loaded {len(class_map)} class mappings, {len(method_map)} method mappings, {len(field_map)} field mappings")

    # Find all Java files
    java_files = []
    for root, dirs, files in os.walk(SRC_DIR):
        for file in files:
            if file.endswith('.java'):
                java_files.append(os.path.join(root, file))

    print(f"Found {len(java_files)} Java files")

    # Remap each file
    changed = 0
    for filepath in java_files:
        if remap_file(filepath, class_map, method_map, field_map):
            print(f"Remapped: {filepath}")
            changed += 1

    print(f"\nDone! Changed {changed} files.")

if __name__ == '__main__':
    main()
