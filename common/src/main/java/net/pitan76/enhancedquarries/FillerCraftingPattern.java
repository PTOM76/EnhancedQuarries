package net.pitan76.enhancedquarries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatItems;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

import java.util.Arrays;
import java.util.Objects;

public class FillerCraftingPattern {
    public Item[] inputs = {
            CompatItems.AIR, CompatItems.AIR, CompatItems.AIR,
            CompatItems.AIR, CompatItems.AIR, CompatItems.AIR,
            CompatItems.AIR, CompatItems.AIR, CompatItems.AIR
    };

    public Item output = CompatItems.AIR;

    @Override
    public String toString() {
        return "FillerCraftingPattern{" +
                "input[0]=" + inputs[0] +
                ", input[1]=" + inputs[1] +
                ", input[2]=" + inputs[2] +
                ", input[3]=" + inputs[3] +
                ", input[4]=" + inputs[4] +
                ", input[5]=" + inputs[5] +
                ", input[6]=" + inputs[6] +
                ", input[7]=" + inputs[7] +
                ", input[8]=" + inputs[8] +
                ", output=" + output +
                '}';
    }

    @Deprecated
    public FillerCraftingPattern(ItemStack output, ItemStack... inputs) {
        this(ItemStackUtil.getItem(output), Arrays.stream(inputs).map(ItemStackUtil::getItem).toArray(Item[]::new));
    }
    
    public FillerCraftingPattern(Item output, Item... inputs) {
        this.output = output;
        if (inputs.length > 0) this.inputs[0] = inputs[0];
        if (inputs.length > 1) this.inputs[1] = inputs[1];
        if (inputs.length > 2) this.inputs[2] = inputs[2];
        if (inputs.length > 3) this.inputs[3] = inputs[3];
        if (inputs.length > 4) this.inputs[4] = inputs[4];
        if (inputs.length > 5) this.inputs[5] = inputs[5];
        if (inputs.length > 6) this.inputs[6] = inputs[6];
        if (inputs.length > 7) this.inputs[7] = inputs[7];
        if (inputs.length > 8) this.inputs[8] = inputs[8];
        
    }

    @Deprecated
    public ItemStack getInput(int i) {
        return getInputItem(i) == CompatItems.AIR ? ItemStackUtil.empty() : new ItemStack(getInputItem(i));
    }

    public Item getInputItem(int i) {
        if (i < 1 || i > 9) return CompatItems.AIR;
        return inputs[i - 1];
    }

    public ItemStack getOutput() {
        return ItemStackUtil.create(output);
    }

    public Item getOutputItem() {
        return output;
    }

    public Item getOutputAsItem() {
        return getOutput().getItem();
    }

    public Item getInputAsItem(int i) {
        return getInput(i).getItem();
    }

    @Deprecated
    public static FillerCraftingPattern createFillPattern(ItemStack output, ItemStack input) {
        return new FillerCraftingPattern(output, input, input, input, input, input, input, input, input, input);
    }

    @Deprecated
    public static FillerCraftingPattern createDonutPattern(ItemStack output, ItemStack input) {
        return new FillerCraftingPattern(output, input, input, input, input, ItemStackUtil.empty(), input, input, input, input);
    }

    public static FillerCraftingPattern createFillPattern(FillerModule fillerALLFill, Item input) {
        return new FillerCraftingPattern(fillerALLFill, input);
    }

    public static FillerCraftingPattern createDonutPattern(FillerModule fillerALLFill, Item input) {
        return new FillerCraftingPattern(fillerALLFill, input, input, input, input, CompatItems.AIR, input, input, input, input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillerCraftingPattern pattern = (FillerCraftingPattern) o;
        return Objects.equals(output, pattern.output) && inputEquals(pattern);
    }

    public boolean inputEquals(FillerCraftingPattern pattern) {
        if (pattern == this) return true;

        return (pattern.getInputItem(1).equals(getInputItem(1))) && (pattern.getInputItem(2).equals(getInputItem(2))) && (pattern.getInputItem(3).equals(getInputItem(3))) && (pattern.getInputItem(4).equals(getInputItem(4))) && (pattern.getInputItem(5).equals(getInputItem(5))) && (pattern.getInputItem(6).equals(getInputItem(6))) && (pattern.getInputItem(7).equals(getInputItem(7))) && (pattern.getInputItem(8).equals(getInputItem(8))) && (pattern.getInputItem(9).equals(getInputItem(9)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(output) + Arrays.hashCode(inputs);
    }
}
