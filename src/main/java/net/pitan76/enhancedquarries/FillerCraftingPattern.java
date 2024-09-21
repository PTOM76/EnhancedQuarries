package net.pitan76.enhancedquarries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class FillerCraftingPattern {
    public ItemStack input1 = ItemStackUtil.empty();
    public ItemStack input2 = ItemStackUtil.empty();
    public ItemStack input3 = ItemStackUtil.empty();
    public ItemStack input4 = ItemStackUtil.empty();
    public ItemStack input5 = ItemStackUtil.empty();
    public ItemStack input6 = ItemStackUtil.empty();
    public ItemStack input7 = ItemStackUtil.empty();
    public ItemStack input8 = ItemStackUtil.empty();
    public ItemStack input9 = ItemStackUtil.empty();
    public ItemStack output = ItemStackUtil.empty();

    @Override
    public String toString() {
        return "FillerCraftingPattern{" +
                "input1=" + input1 +
                ", input2=" + input2 +
                ", input3=" + input3 +
                ", input4=" + input4 +
                ", input5=" + input5 +
                ", input6=" + input6 +
                ", input7=" + input7 +
                ", input8=" + input8 +
                ", input9=" + input9 +
                ", output=" + output +
                '}';
    }

    public FillerCraftingPattern(ItemStack output, ItemStack... inputs) {
        this.output = output;
        if (inputs.length > 0) input1 = inputs[0];
        if (inputs.length > 1) input2 = inputs[1];
        if (inputs.length > 2) input3 = inputs[2];
        if (inputs.length > 3) input4 = inputs[3];
        if (inputs.length > 4) input5 = inputs[4];
        if (inputs.length > 5) input6 = inputs[5];
        if (inputs.length > 6) input7 = inputs[6];
        if (inputs.length > 7) input8 = inputs[7];
        if (inputs.length > 8) input9 = inputs[8];
    }

    public ItemStack getInput(int i) {
        if (i == 1) return input1.copy();
        if (i == 2) return input2.copy();
        if (i == 3) return input3.copy();
        if (i == 4) return input4.copy();
        if (i == 5) return input5.copy();
        if (i == 6) return input6.copy();
        if (i == 7) return input7.copy();
        if (i == 8) return input8.copy();
        if (i == 9) return input9.copy();
        return ItemStackUtil.empty();
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public Item getOutputAsItem() {
        return getOutput().getItem();
    }

    public Item getInputAsItem(int i) {
        return getInput(i).getItem();
    }

    public static FillerCraftingPattern createFillPattern(ItemStack output, ItemStack input) {
        return new FillerCraftingPattern(output, input, input, input, input, input, input, input, input, input);
    }

    public static FillerCraftingPattern createDonutPattern(ItemStack output, ItemStack input) {
        return new FillerCraftingPattern(output, input, input, input, input, ItemStackUtil.empty(), input, input, input, input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillerCraftingPattern pattern = (FillerCraftingPattern) o;
        return Objects.equals(input1, pattern.input1) && Objects.equals(input2, pattern.input2) && Objects.equals(input3, pattern.input3) && Objects.equals(input4, pattern.input4) && Objects.equals(input5, pattern.input5) && Objects.equals(input6, pattern.input6) && Objects.equals(input7, pattern.input7) && Objects.equals(input8, pattern.input8) && Objects.equals(input9, pattern.input9) && Objects.equals(output, pattern.output);
    }

    public boolean inputEquals(FillerCraftingPattern pattern) {
        if (pattern == this) return true;

        return (pattern.input1.getItem().equals(input1.getItem())) && (pattern.input2.getItem().equals(input2.getItem())) && (pattern.input3.getItem().equals(input3.getItem())) && (pattern.input4.getItem().equals(input4.getItem())) && (pattern.input5.getItem().equals(input5.getItem())) && (pattern.input6.getItem().equals(input6.getItem())) && (pattern.input7.getItem().equals(input7.getItem())) && (pattern.input8.getItem().equals(input8.getItem())) && (pattern.input9.getItem().equals(input9.getItem()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(input1, input2, input3, input4, input5, input6, input7, input8, input9, output);
    }
}
