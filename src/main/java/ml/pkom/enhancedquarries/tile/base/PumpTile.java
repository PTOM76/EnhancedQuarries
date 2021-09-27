package ml.pkom.enhancedquarries.tile.base;

import alexiil.mc.lib.attributes.CombinableAttribute;
import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.fluid.world.FluidWorldUtil;
import ml.pkom.enhancedquarries.block.base.Pump;
import ml.pkom.enhancedquarries.event.BlockStatePos;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

import java.util.HashSet;
import java.util.Set;

public class PumpTile extends PowerAcceptorBlockEntity {

    public FluidVolume stored = FluidVolumeUtil.EMPTY;

    public FluidVolume getStoredFluid() {
        return stored;
    }

    public void setStoredFluid(FluidVolume stored) {
        this.stored = stored;
    }

    // デフォルトコスト
    private long defaultEnergyCost = 30;

    // ブロック1回設置分に対するエネルギーのコスト
    public long getEnergyCost() {
        return defaultEnergyCost;
    }

    //public PumpTile(BlockEntityType<?> type) {
    //    super(type);
    //}

    public PumpTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        //this(type);
    }

    @Override
    public long getBaseMaxPower() {
        return 5000;
    }

    @Override
    public long getBaseMaxOutput() {
        return 0;
    }

    @Override
    public long getBaseMaxInput() {
        return 500;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        setStoredFluid(FluidVolume.fromTag(tag.getCompound("fluid")));
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt = super.writeNbt(nbt);
        nbt.put("fluid", getStoredFluid().toTag());
        return nbt;
    }

    private double defaultBasicSpeed = 5;

    // 基準の速度
    public double getBasicSpeed() {
        return defaultBasicSpeed;
    }

    public double defaultSettingCoolTime = 250;

    // クールダウンの基準
    public double getSettingCoolTime() {
        return defaultSettingCoolTime;
    }

    public double coolTime = getSettingCoolTime();

    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        // 1.--
        if (world == null || world.isClient())
        {
            return;
        }
        // ----
        //BlockState state = getCachedState();
        if (!(state.getBlock() instanceof Pump)) return;
        //Pump pump = (Pump) state.getBlock();

        // レッドストーン受信で無効
        if (world.isReceivingRedstonePower(getPos())) {
            if (isActive()) {
                 Pump.setActive(false, world, getPos());
            }
            return;
        }
        if (getEnergy() > getEuPerTick(getEnergyCost())) {
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

    public <T> T getNeighbourAttribute(CombinableAttribute<T> attr, Direction dir) {
        return attr.get(getWorld(), getPos().offset(dir), SearchOptions.inDirection(dir));
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
        if (!getStoredFluid().isEmpty()) {
            FluidVolume original = getStoredFluid().copy();
            FluidInsertable insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, Direction.UP);
            setStoredFluid(insertable.attemptInsertion(getStoredFluid(), Simulation.ACTION));
            if (getStoredFluid().equals(original)) {
                insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, Direction.NORTH);
                setStoredFluid(insertable.attemptInsertion(getStoredFluid(), Simulation.ACTION));
            }
            if (getStoredFluid().equals(original)) {
                insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, Direction.SOUTH);
                setStoredFluid(insertable.attemptInsertion(getStoredFluid(), Simulation.ACTION));
            }
            if (getStoredFluid().equals(original)) {
                insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, Direction.WEST);
                setStoredFluid(insertable.attemptInsertion(getStoredFluid(), Simulation.ACTION));
            }
            if (getStoredFluid().equals(original)) {
                insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, Direction.EAST);
                setStoredFluid(insertable.attemptInsertion(getStoredFluid(), Simulation.ACTION));
            }
            if (!getStoredFluid().isEmpty())
                return false;
        }
        if (world.getFluidState(getPos().down()).isEmpty() && world.getFluidState(getPos().down(2)).isEmpty() && world.getFluidState(getPos().down(3)).isEmpty()) return false;
        BlockStatePos statePos = getFarFluid();
        try {
            FluidVolume drained = FluidWorldUtil.drain(world, statePos.getBlockPos(), Simulation.ACTION);
            setStoredFluid(drained);
            return !getStoredFluid().isEmpty();
        } catch (NullPointerException e) {
            return false;
        }

    }

    public void coolTimeBonus() {
        if (getBaseMaxPower() / 1.125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 5;
        }
        if (getBaseMaxPower() / 1.25 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 3;
        }
        if (getBaseMaxPower() / 3 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 2;
        }
        if (getBaseMaxPower() / 5 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 7 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 10 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 12 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 15 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 16 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 20 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 30 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 40 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
    }


}
