package net.pitan76.enhancedquarries.compat;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import team.reborn.energy.api.EnergyStorage;

public class TREnergyStorage extends SnapshotParticipant<Long> implements EnergyStorage, IEnergyStorage {

    public static final double CONVERSION_RATE = Config.config.getDouble("reborn_energy_conversion_rate");

    private final BaseEnergyTile tile;

    public TREnergyStorage(BaseEnergyTile tile) {
        this.tile = tile;
    }

    public BaseEnergyTile getTile() {
        return tile;
    }

    public long getUsableCapacity() {
        return (long) (tile.getUsableCapacity() / CONVERSION_RATE);
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (maxAmount < getUsableCapacity()) {
            updateSnapshots(transaction);
            return (long) (tile.insertEnergy((long) (maxAmount * CONVERSION_RATE)) / CONVERSION_RATE);
        }
        if (maxAmount > 0) {
            updateSnapshots(transaction);
            return (long) (tile.insertEnergy((long) (getUsableCapacity() * CONVERSION_RATE)) / CONVERSION_RATE);
        }

        return 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (maxAmount < getAmount()) {
            updateSnapshots(transaction);
            return (long) (tile.extractEnergy((long) (maxAmount * CONVERSION_RATE)) / CONVERSION_RATE);
        }
        if (getAmount() > 0) {
            updateSnapshots(transaction);
            return (long) (tile.extractEnergy(tile.getEnergy()) / CONVERSION_RATE);
        }
        return 0;
    }

    @Override
    public long getAmount() {
        return (long) (tile.getEnergy() / CONVERSION_RATE);
    }

    @Override
    public long getCapacity() {
        return (long) (tile.getMaxEnergy() / CONVERSION_RATE);
    }

    @Override
    protected Long createSnapshot() {
        return tile.getEnergy();
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        tile.setEnergy(snapshot);
    }
}