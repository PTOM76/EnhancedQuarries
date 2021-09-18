package ml.pkom.enhancedquarries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FillerCraftingPatterns {

    public static ItemStack BRICKS = create(net.minecraft.item.Items.BRICKS);
    public static ItemStack GLASS = create(net.minecraft.item.Items.GLASS);

    public static List<FillerCraftingPattern> patterns = new ArrayList<>();
    public static void init() {
        patterns.add(FillerCraftingPattern.createFillPattern(create(Items.fillerALL_FILL), BRICKS));
        patterns.add(FillerCraftingPattern.createFillPattern(create(Items.fillerALL_REMOVE), GLASS));
        patterns.add(FillerCraftingPattern.createDonutPattern(create(Items.fillerALL_DELETE), GLASS));
    }

    public static ItemStack create(Item item) {
        return new ItemStack(item);
    }
}
