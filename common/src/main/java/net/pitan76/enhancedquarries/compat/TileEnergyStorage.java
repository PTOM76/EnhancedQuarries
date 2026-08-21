package net.pitan76.enhancedquarries.compat;

import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.transfer.energy.v1.IEnergyStorage;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

public class TileEnergyStorage implements IEnergyStorage {

    private final BaseEnergyTile tile;

    public TileEnergyStorage(BaseEnergyTile tile) {
        this.tile = tile;
    }

    public BaseEnergyTile getTile() {
        return tile;
    }

    public static double getConversionRate() {
        String key = PlatformUtil.isFabric() ? "reborn_energy_conversion_rate" : "fe_conversion_rate";
        double rate = Config.config.getDoubleOrDefault(key, 1.0);

        return rate <= 0 ? 1.0 : rate;
    }

    public long toExternal(long internal) {
        return (long) (internal / getConversionRate());
    }

    public long toInternal(long external) {
        return (long) (external * getConversionRate());
    }

    @Override
    public long getAmount() {
        return toExternal(tile.getEnergy());
    }

    @Override
    public long getCapacity() {
        return toExternal(tile.getMaxEnergy());
    }

    @Override
    public long insert(long maxAmount, boolean simulate) {
        if (!supportsInsertion() || maxAmount <= 0) return 0;

        long amount = Math.min(toInternal(maxAmount), tile.getUsableCapacity());
        if (amount <= 0) return 0;
        if (simulate) return toExternal(amount);

        return toExternal(tile.insertEnergy(amount));
    }

    @Override
    public long extract(long maxAmount, boolean simulate) {
        if (!supportsExtraction() || maxAmount <= 0) return 0;

        long amount = Math.min(toInternal(maxAmount), tile.getEnergy());
        if (amount <= 0) return 0;
        if (simulate) return toExternal(amount);

        return toExternal(tile.extractEnergy(amount));
    }

    @Override
    public boolean supportsInsertion() {
        return tile.canInsertEnergy();
    }

    @Override
    public boolean supportsExtraction() {
        return tile.canExtractEnergy();
    }
}
