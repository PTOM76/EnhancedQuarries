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
        patterns.add(new FillerCraftingPattern(create(Items.fillerLEVELING), ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, GLASS, GLASS, GLASS, BRICKS, BRICKS, BRICKS));
        patterns.add(new FillerCraftingPattern(create(Items.fillerWALL), BRICKS, ItemStack.EMPTY, BRICKS, BRICKS, ItemStack.EMPTY, BRICKS, BRICKS, ItemStack.EMPTY, BRICKS));
        patterns.add(new FillerCraftingPattern(create(Items.fillerBOX), BRICKS, BRICKS, BRICKS, BRICKS, ItemStack.EMPTY, BRICKS, BRICKS, BRICKS, BRICKS));
        patterns.add(new FillerCraftingPattern(create(Items.fillerTORCH), BRICKS, ItemStack.EMPTY, BRICKS, ItemStack.EMPTY, BRICKS, ItemStack.EMPTY, BRICKS, ItemStack.EMPTY, BRICKS));
    }

    public static ItemStack create(Item item) {
        return new ItemStack(item);
    }
}
