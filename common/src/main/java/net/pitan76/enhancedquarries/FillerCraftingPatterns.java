package net.pitan76.enhancedquarries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.item.CompatItems;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

public class FillerCraftingPatterns {

    public static Item BRICKS = net.minecraft.item.Items.BRICKS;
    public static Item SLAB_BRICKS = net.minecraft.item.Items.BRICK_SLAB;
    public static Item GLASS = CompatItems.GLASS;
    public static Item WATER_BUCKET = net.minecraft.item.Items.WATER_BUCKET;
    public static Item EMPTY = CompatItems.AIR;

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
        addPattern(FillerCraftingPattern.createFillPattern(Items.fillerALL_FILL.getICompat(), BRICKS));
        addPattern(FillerCraftingPattern.createFillPattern(Items.fillerALL_REMOVE.getICompat(), GLASS));
        addPattern(FillerCraftingPattern.createDonutPattern(Items.fillerALL_DELETE.getICompat(), GLASS));
        addPattern(new FillerCraftingPattern(Items.fillerLEVELING.getICompat(), EMPTY, EMPTY, EMPTY, GLASS, GLASS, GLASS, BRICKS, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerWALL.getICompat(), BRICKS, EMPTY, BRICKS, BRICKS, EMPTY, BRICKS, BRICKS, EMPTY, BRICKS));
        addPattern(FillerCraftingPattern.createDonutPattern(Items.fillerBOX.getICompat(), BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerTORCH.getICompat(), BRICKS, EMPTY, BRICKS, EMPTY, BRICKS, EMPTY, BRICKS, EMPTY, BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerDELETE_FLUID.getICompat(), EMPTY, EMPTY, EMPTY, GLASS, GLASS, GLASS, WATER_BUCKET, WATER_BUCKET, WATER_BUCKET));
        addPattern(new FillerCraftingPattern(Items.fillerFLOOR_REPLACE.getICompat(), EMPTY, EMPTY, EMPTY, BRICKS, BRICKS, BRICKS, GLASS, GLASS, GLASS));
        addPattern(new FillerCraftingPattern(Items.fillerTOWER.getICompat(), EMPTY, BRICKS, BRICKS, EMPTY, BRICKS, BRICKS, EMPTY, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerVERTICAL_LAYER.getICompat(), SLAB_BRICKS, EMPTY, SLAB_BRICKS, SLAB_BRICKS, EMPTY, SLAB_BRICKS, SLAB_BRICKS, EMPTY, SLAB_BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerHORIZONTAL_LAYER.getICompat(), SLAB_BRICKS, SLAB_BRICKS, SLAB_BRICKS, EMPTY, EMPTY, EMPTY, SLAB_BRICKS, SLAB_BRICKS, SLAB_BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerSTAIRS.getICompat(), EMPTY, EMPTY, BRICKS, EMPTY, BRICKS, BRICKS, BRICKS, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerPYRAMID.getICompat(), EMPTY, EMPTY, EMPTY, EMPTY, BRICKS, EMPTY, BRICKS, BRICKS, BRICKS));
        addPattern(new FillerCraftingPattern(Items.fillerCUT_STAIRS.getICompat(), EMPTY, EMPTY, GLASS, EMPTY, GLASS, GLASS, GLASS, GLASS, GLASS));
        addPattern(new FillerCraftingPattern(Items.fillerCUT_PYRAMID.getICompat(), EMPTY, EMPTY, EMPTY, EMPTY, GLASS, EMPTY, GLASS, GLASS, GLASS));
    }

    public static ItemStack create(Item item) {
        return ItemStackUtil.create(item);
    }
}
