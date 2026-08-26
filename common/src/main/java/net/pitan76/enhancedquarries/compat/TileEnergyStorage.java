package net.pitan76.enhancedquarries.compat;

import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.transfer.energy.v1.IMutableEnergyStorage;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

public class TileEnergyStorage implements IMutableEnergyStorage {

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
        if (internal >= Long.MAX_VALUE) return Long.MAX_VALUE;

        return (long) (internal / getConversionRate());
    }

    public long toInternal(long external) {
        if (external >= Long.MAX_VALUE) return Long.MAX_VALUE;

        return (long) (external * getConversionRate());
    }

    @Override
    public long getEnergyStored() {
        return toExternal(tile.getEnergy());
    }

    @Override
    public void setEnergyStored(long energy) {
        tile.setEnergy(Math.max(0, Math.min(toInternal(energy), tile.getMaxEnergy())));
    }

    @Override
    public long getCapacityEnergy() {
        return toExternal(tile.getMaxEnergy());
    }

    @Override
    public long getMaxInputEnergy() {
        if (!canInsertEnergy()) return 0;

        return toExternal(tile.getMaxInput());
    }

    @Override
    public long getMaxOutputEnergy() {
        if (!canExtractEnergy()) return 0;

        return toExternal(tile.getMaxOutput());
    }

    @Override
    public long insertEnergy(long amount, boolean simulate) {
        if (amount <= 0 || !canInsertEnergy()) return 0;

        long internal = Math.min(toInternal(amount), Math.min(tile.getMaxInput(), tile.getUsableCapacity()));
        if (internal <= 0) return 0;
        if (!simulate) tile.setEnergy(tile.getEnergy() + internal);

        return toExternal(internal);
    }

    @Override
    public long extractEnergy(long amount, boolean simulate) {
        if (amount <= 0 || !canExtractEnergy()) return 0;

        long internal = Math.min(toInternal(amount), Math.min(tile.getMaxOutput(), tile.getEnergy()));
        if (internal <= 0) return 0;
        if (!simulate) tile.setEnergy(tile.getEnergy() - internal);

        return toExternal(internal);
    }

    @Override
    public boolean canInsertEnergy() {
        return tile.canInsertEnergy();
    }

    @Override
    public boolean canExtractEnergy() {
        return tile.canExtractEnergy();
    }
}
