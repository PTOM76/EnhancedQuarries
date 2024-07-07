package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Pump;
import net.pitan76.enhancedquarries.event.BlockStatePos;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.extra.transfer.util.FluidStorageUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

import java.util.HashSet;
import java.util.Set;

// reference: Kibe Utilities's tank
public class PumpTile extends BaseEnergyTile {
    private SingleFluidStorage storedFluid = FluidStorageUtil.withFixedCapacity(FluidConstants.BUCKET * 4, this::markDirty);

    public SingleFluidStorage getStoredFluid() {
        return storedFluid;
    }

    public void setStoredFluid(SingleFluidStorage storedFluid) {
        this.storedFluid = storedFluid;
    }

    // ブロック1回設置分に対するエネルギーのコスト
    public long getEnergyCost() {
        return 30;
    }

    public PumpTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    @Override
    public long getMaxEnergy() {
        return 5000;
    }

    @Override
    public long getMaxOutput() {
        return 0;
    }

    @Override
    public long getMaxInput() {
        return 500;
    }

    public void readNbt(ReadNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        if (nbt.contains("variant")) {
            FluidStorageUtil.readNbt(storedFluid, args);
        }
    }

    public void writeNbt(WriteNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        if(!storedFluid.isResourceBlank() && !FluidStorageUtil.isEmpty(storedFluid)) {
            FluidStorageUtil.writeNbt(storedFluid, args);
        }
    }

    // 基準の速度
    public double getBasicSpeed() {
        return 5;
    }

    // クールダウンの基準
    public double getSettingCoolTime() {
        return 250;
    }

    public double coolTime = getSettingCoolTime();

    public void tick(World world, BlockPos pos, BlockState state, BaseEnergyTile blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (world.isClient()) return;

        if (!(state.getBlock() instanceof Pump)) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, getPos())) {
            if (isActive())
                 Pump.setActive(false, world, getPos());
            return;
        }
        if (getEnergy() > getEnergyCost()) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                if (tryPump(world)) {
                    useEnergy(getEnergyCost());
                }
            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!Pump.isActive(state)) {
                Pump.setActive(true, world, getPos());
            }
        } else if (Pump.isActive(state)) {
            Pump.setActive(false, world, getPos());
        }
    }

    public BlockStatePos getFarFluid() {
        for (BlockStatePos statePos : sphereAround(getPos(), 30)) {
            if (statePos.getBlockState().getFluidState().getFluid() != null && statePos.getBlockState().getFluidState().isStill()) {
                //if (statePos.getBlockState().getFluidState().getFluid().equals(fluid))
                return statePos;
            }
        }
        return null;
    }

    public Set<BlockStatePos> sphereAround(BlockPos pos, int radius) {
        Set<BlockStatePos> sphere = new HashSet<>();
        BlockStatePos center = new BlockStatePos(getWorld().getBlockState(pos), pos, getWorld());
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    BlockPos procPos = center.getBlockPos().add(x, y, z);
                    if (getWorld().getBlockState(procPos).getBlock() instanceof AirBlock) continue;
                    if (getWorld().getFluidState(procPos).isEmpty()) continue;
                    if (getWorld().getFluidState(procPos).getFluid() == null) continue;
                    if (!getWorld().getFluidState(procPos).isStill()) continue;
                    BlockStatePos b = new BlockStatePos(getWorld().getBlockState(procPos), procPos, getWorld());
                    if (center.getBlockPos().getManhattanDistance(b.getBlockPos()) <= radius) {
                        if (b.getBlockPos().equals(getPos().down())) continue;
                        sphere.add(b);
                        return sphere;
                    }
                }

            }
        }
        if (sphere.isEmpty() && !getWorld().getFluidState(getPos().down()).isEmpty()) {
            sphere.add(new BlockStatePos(getWorld().getBlockState(getPos().down()), getPos().down(), getWorld()));
        }
        return sphere;
    }

    public boolean tryPump(World world) {
        //EnhancedQuarries.log(Level.INFO, getStored().amount().asInt(1) + "");
        if (storedFluid.getAmount() >= storedFluid.getCapacity()) {
            return false;
        }
        if (world.getFluidState(getPos().down()).isEmpty() && world.getFluidState(getPos().down(2)).isEmpty() && world.getFluidState(getPos().down(3)).isEmpty())
            return false;
        BlockStatePos statePos = getFarFluid();
        try {
            BlockState state = statePos.getBlockState();
            world.removeBlock(statePos.getBlockPos(), false);
            FluidState fluidState = state.getFluidState();
            try (Transaction transaction = Transaction.openOuter()) {
                storedFluid.insert(FluidVariant.of(fluidState.getFluid()), FluidConstants.BUCKET, transaction);
                transaction.commit();
            }
            return !FluidStorageUtil.isEmpty(storedFluid);
        } catch (NullPointerException e) {
            return false;
        }

    }

    public void coolTimeBonus() {
        if (getMaxEnergy() / 1.125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 5;
        }
        if (getMaxEnergy() / 1.25 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 3;
        }
        if (getMaxEnergy() / 3 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 2;
        }
        if (getMaxEnergy() / 5 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 7 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 10 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 12 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 15 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 16 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 20 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 30 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 40 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
    }
}
