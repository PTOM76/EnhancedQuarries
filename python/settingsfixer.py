import os
import re

FOLDER_PATH = '../src/main/java'

# Replacements r'(pattern)': r'(replacement)'
REPLACEMENTS = {
    r'CompatibleItemSettings\.of\(\)': r'CompatibleItemSettings.of(_id("default_id"))',
}

# Function to update CompatibleItemSettings.of() calls
def update_compatible_item_settings(content):
    pattern = re.compile(r'CompatibleItemSettings\.of\(\)')
    matches = pattern.findall(content)
    for match in matches:
        # Extract the item name from the previous line
        item_name_match = re.search(r'public static Item (\w+) =', content[:content.find(match)])
        if item_name_match:
            item_name = item_name_match.group(1).lower()
            new_call = f'CompatibleItemSettings.of(_id("{item_name}"))'
            content = content.replace(match, new_call, 1)
    return content

for root, dirs, files in os.walk(FOLDER_PATH):
    for filename in files:
        if filename.endswith('.java'):
            filepath = os.path.join(root, filename)

            with open(filepath, 'r', encoding='utf-8') as file:
                content = file.read()

            content2 = content

            # Update CompatibleItemSettings.of() calls
            content = update_compatible_item_settings(content)

            # Write if content is changed
            if content != content2:
                with open(filepath, 'w', encoding='utf-8') as file:
                    file.write(content)

                print(f'Updated CompatibleItemSettings.of() calls in {filepath}')