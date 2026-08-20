package net.pitan76.enhancedquarries.forge.compat;

import net.minecraftforge.energy.IEnergyStorage;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;

/**
 * BaseEnergyTile を Forge Energy (FE) として公開するアダプタ。
 */
public class FEEnergyStorage implements IEnergyStorage, net.pitan76.enhancedquarries.compat.IEnergyStorage {

    public static double getConversionRate() {
        double rate = Config.config.getDouble("fe_conversion_rate");
        return rate <= 0 ? 1.0 : rate;
    }

    private final BaseEnergyTile tile;

    public FEEnergyStorage(BaseEnergyTile tile) {
        this.tile = tile;
    }

    public BaseEnergyTile getTile() {
        return tile;
    }

    private static int toInt(long value) {
        if (value > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (value < 0) return 0;
        return (int) value;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive() || maxReceive <= 0) return 0;

        double rate = getConversionRate();
        long internal = (long) (maxReceive * rate);
        long acceptable = Math.min(internal, tile.getUsableCapacity());
        acceptable = Math.min(acceptable, (long) (tile.getMaxInput() * rate));
        if (acceptable <= 0) return 0;

        if (simulate) return toInt((long) (acceptable / rate));
        return toInt((long) (tile.insertEnergy(acceptable) / rate));
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract() || maxExtract <= 0) return 0;

        double rate = getConversionRate();
        long internal = (long) (maxExtract * rate);
        long available = Math.min(internal, tile.getEnergy());
        available = Math.min(available, (long) (tile.getMaxOutput() * rate));
        if (available <= 0) return 0;

        if (simulate) return toInt((long) (available / rate));
        return toInt((long) (tile.extractEnergy(available) / rate));
    }

    @Override
    public int getEnergyStored() {
        return toInt((long) (tile.getEnergy() / getConversionRate()));
    }

    @Override
    public int getMaxEnergyStored() {
        return toInt((long) (tile.getMaxEnergy() / getConversionRate()));
    }

    @Override
    public boolean canExtract() {
        return tile.canExtractEnergy() && tile.getMaxOutput() > 0;
    }

    @Override
    public boolean canReceive() {
        return tile.canInsertEnergy() && tile.getMaxInput() > 0;
    }
}
