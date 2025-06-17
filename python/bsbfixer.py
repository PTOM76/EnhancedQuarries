import os
import re

FOLDER_PATH = '../src/main/java'

def convert_compatible_block_settings(content):
    pattern = re.compile(
        r'public\s+static\s+CompatibleBlockSettings\s+(\w+)\s*=\s*CompatibleBlockSettings\s*\.\s*of\((.*?)\)\s*\.\s*requiresTool\(\)\s*\.\s*strength\(\s*(.*?),\s*(.*?)\s*\);',
        re.DOTALL
    )

    def replacement(match):
        variable_name = match.group(1)
        material = match.group(2)
        strength1 = match.group(3)
        strength2 = match.group(4)
        return (
            f'public static BlockSettingsBuilder {variable_name} = new BlockSettingsBuilder()\n'
            f'        .material({material})\n'
            f'        .requiresTool()\n'
            f'        .strength({strength1}, {strength2});'
        )

    return pattern.sub(replacement, content)

def update_class_constructors(content, id_map):
    pattern = re.compile(
        r'public\s+(\w+)\(\s*CompatibleBlockSettings\s+settings\s*\)\s*{\s*super\(defaultSettings\);\s*}',
        re.DOTALL
    )

    def replacement(match):
        class_name = match.group(1)
        return f'public {class_name}(CompatibleBlockSettings settings) {{\n' \
               f'    super(settings);\n' \
               f'}}'

    content = pattern.sub(replacement, content)

    pattern = re.compile(
        r'public\s+(\w+)\(\s*\)\s*{\s*super\(defaultSettings\);\s*}',
        re.DOTALL
    )

    def replacement(match):
        class_name = match.group(1)
        if class_name in id_map:
            id_value = id_map[class_name]
        else:
            id_value = "normal_builder"
        return f'public {class_name}() {{\n' \
               f'    super(defaultSettings.build(EnhancedQuarries._id("{id_value}")));\n' \
               f'}}'

    content = pattern.sub(replacement, content)

    pattern = re.compile(
        r'public\s+(\w+)\(\s*CompatIdentifier\s+id\s*\)\s*{\s*super\(defaultSettings\.build\(id\)\);\s*}',
        re.DOTALL
    )

    def replacement(match):
        class_name = match.group(1)
        return f'public {class_name}(CompatIdentifier id) {{\n' \
               f'    super(defaultSettings.build(id));\n' \
               f'}}'

    return pattern.sub(replacement, content)

def extract_ids_from_items(content):
    pattern = re.compile(r'registry\.registerItem\(EnhancedQuarries\._id\("(\w+)"\), \(\) -> (\w+)\);')
    return {v: k for k, v in pattern.findall(content)}

# Extract IDs from Items.java
items_file_path = os.path.join(FOLDER_PATH, 'net/pitan76/enhancedquarries/Items.java')
with open(items_file_path, 'r', encoding='utf-8') as file:
    items_content = file.read()
id_map = extract_ids_from_items(items_content)

for root, dirs, files in os.walk(FOLDER_PATH):
    for filename in files:
        if filename.endswith('.java'):
            filepath = os.path.join(root, filename)

            with open(filepath, 'r', encoding='utf-8') as file:
                content = file.read()

            content2 = content

            # Convert CompatibleBlockSettings to BlockSettingsBuilder
            content = convert_compatible_block_settings(content)

            # Update class constructors
            content = update_class_constructors(content, id_map)

            # Write if content is changed
            if content != content2:
                with open(filepath, 'w', encoding='utf-8') as file:
                    file.write(content)

                print(f'Updated CompatibleBlockSettings and class constructors in {filepath}')