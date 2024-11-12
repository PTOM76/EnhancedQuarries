import os
import re

FOLDER_PATH = '../src/main/java'

def extract_ids_from_init(content):
    pattern = re.compile(r'registry\.registerItem\(\w+\._id\("(\w+)"\), \(\) -> (\w+)\);')
    return {v: k for k, v in pattern.findall(content)}

def update_compatible_item_settings(content, id_map):
    pattern = re.compile(r'public static Item (\w+) = .*?CompatibleItemSettings\.of\(\)')
    matches = list(pattern.finditer(content))

    for match in reversed(matches):
        item_name = match.group(1)
        if item_name in id_map:
            new_call = f'CompatibleItemSettings.of(_id("{id_map[item_name]}"))'
            content = content[:match.start()] + content[match.start():match.end()].replace('CompatibleItemSettings.of()', new_call) + content[match.end():]
    return content

for root, dirs, files in os.walk(FOLDER_PATH):
    for filename in files:
        if filename.endswith('.java'):
            filepath = os.path.join(root, filename)

            with open(filepath, 'r', encoding='utf-8') as file:
                content = file.read()

            if 'public static void init()' in content:
                id_map = extract_ids_from_init(content)
            else:
                id_map = {}

            content2 = content

            # Update CompatibleItemSettings.of() calls
            content = update_compatible_item_settings(content, id_map)

            # Write if content is changed
            if content != content2:
                with open(filepath, 'w', encoding='utf-8') as file:
                    file.write(content)

                print(f'Updated CompatibleItemSettings.of() calls in {filepath}')