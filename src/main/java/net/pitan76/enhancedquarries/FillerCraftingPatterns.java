package net.pitan76.enhancedquarries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FillerCraftingPatterns {

    public static ItemStack BRICKS = create(net.minecraft.item.Items.BRICKS);
    public static ItemStack SLAB_BRICKS = create(net.minecraft.item.Items.BRICK_SLAB);
    public static ItemStack GLASS = create(net.minecraft.item.Items.GLASS);
    public static ItemStack WATER_BUCKET = create(net.minecraft.item.Items.WATER_BUCKET);
    public static ItemStack EMPTY = ItemStackUtil.empty();

    private static final List<FillerCraftingPattern> patterns = new ArrayList<>();

    public static List<FillerCraftingPattern> getPatterns() {
        return patterns;
    }

    public static void addPattern(FillerCraftingPattern pattern) {
        getPatterns().add(pattern);
    }

    public static void removePattern(FillerCraftingPattern pattern) {
        getPatterns().remove(pattern);
    }

    public static FillerCraftingPattern getPattern(Item module) {
        for (FillerCraftingPattern pattern : getPatterns()) {
            if (pattern.getOutput().getItem() == module) {
                return pattern;
            }
        }
        return null;
    }

    public static void init() {
        addPattern(FillerCraftingPattern.createFillPattern(create(Items.fillerALL_FILL), BRICKS));
        addPattern(FillerCraftingPattern.createFillPattern(create(Items.fillerALL_REMOVE), GLASS));
        addPattern(FillerCraftingPattern.createDonutPattern(create(Items.fillerALL_DELETE), GLASS));
        addPattern(new FillerCraftingPattern(create(Items.fillerLEVELING), EMPTY, EMPTY, EMPTY, GLASS, GLASS, GLASS, BRICKS, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerWALL), BRICKS, EMPTY, BRICKS, BRICKS, EMPTY, BRICKS, BRICKS, EMPTY, BRICKS));
        addPattern(FillerCraftingPattern.createDonutPattern(create(Items.fillerBOX), BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerTORCH), BRICKS, EMPTY, BRICKS, EMPTY, BRICKS, EMPTY, BRICKS, EMPTY, BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerDELETE_FLUID), EMPTY, EMPTY, EMPTY, GLASS, GLASS, GLASS, WATER_BUCKET, WATER_BUCKET, WATER_BUCKET));
        addPattern(new FillerCraftingPattern(create(Items.fillerFLOOR_REPLACE), EMPTY, EMPTY, EMPTY, BRICKS, BRICKS, BRICKS, GLASS, GLASS, GLASS));
        addPattern(new FillerCraftingPattern(create(Items.fillerTOWER), EMPTY, BRICKS, BRICKS, EMPTY, BRICKS, BRICKS, EMPTY, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerVERTICAL_LAYER), SLAB_BRICKS, EMPTY, SLAB_BRICKS, SLAB_BRICKS, EMPTY, SLAB_BRICKS, SLAB_BRICKS, EMPTY, SLAB_BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerHORIZONTAL_LAYER), SLAB_BRICKS, SLAB_BRICKS, SLAB_BRICKS, EMPTY, EMPTY, EMPTY, SLAB_BRICKS, SLAB_BRICKS, SLAB_BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerSTAIRS), EMPTY, EMPTY, BRICKS, EMPTY, BRICKS, BRICKS, BRICKS, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerPYRAMID), EMPTY, EMPTY, EMPTY, EMPTY, BRICKS, EMPTY, BRICKS, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(create(Items.fillerCUT_STAIRS), EMPTY, EMPTY, GLASS, EMPTY, GLASS, GLASS, GLASS, GLASS, GLASS));
        addPattern(new FillerCraftingPattern(create(Items.fillerCUT_PYRAMID), EMPTY, EMPTY, EMPTY, EMPTY, GLASS, EMPTY, GLASS, GLASS, GLASS));
    }

    public static ItemStack create(Item item) {
        return ItemStackUtil.create(item);
    }
}
