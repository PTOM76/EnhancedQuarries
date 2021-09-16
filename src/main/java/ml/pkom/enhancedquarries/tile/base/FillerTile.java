package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.block.Frame;
import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.mixin.MachineBaseBlockEntityAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;

import java.util.List;

public class FillerTile extends PowerAcceptorBlockEntity implements InventoryProvider, BuiltScreenHandlerProvider {// implements IInventory {
    // Container
    public RebornInventory<FillerTile> inventory = new RebornInventory<>(27, "FillerTile", 64, this);

    public RebornInventory<FillerTile> getInventory() {
        return inventory;
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("filler").player(player.inventory).inventory().hotbar().addInventory()
                .blockEntity(this)
                .outputSlot(6, 55, 66)
                .outputSlot(7, 75, 66)
                .outputSlot(8, 95, 66)
                .outputSlot(9, 115, 66)
                .outputSlot(10, 135, 66)
                .energySlot(11, 8, 72)
                .syncEnergyValue()
                .addInventory()
                .create(this, syncID);
    }
    // ----

    // TR
    // デフォルトコスト
    private double defaultEnergyCost = 30;
    private double defaultPlaceFrameEnergyCost = 40;
    private double defaultReplaceFluidEnergyCost = 120;

    // ブロック1回設置分に対するエネルギーのコスト
    public double getEnergyCost() {
        return defaultEnergyCost;
    }

    // エネルギーの容量
    public double getBaseMaxPower() {
        return 5000;
    }

    // エネルギーの最大出力(不要なので0)
    public double getBaseMaxOutput() {
        return 0;
    }

    // エネルギーの最大入力
    public double getBaseMaxInput() {
        return 500;
    }

    // エネルギーの生産をするか？→false
    public boolean canProvideEnergy(final Direction direction) { return false; }

    // ----

    // NBT

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putDouble("coolTime", coolTime);

        if (pos1 != null) {
            tag.putInt("rangePos1X", getPos1().getX());
            tag.putInt("rangePos1Y", getPos1().getY());
            tag.putInt("rangePos1Z", getPos1().getZ());
        }
        if (pos2 != null) {
            tag.putInt("rangePos2X", getPos2().getX());
            tag.putInt("rangePos2Y", getPos2().getY());
            tag.putInt("rangePos2Z", getPos2().getZ());
        }
        return super.writeNbt(tag);
    }

    public void fromTag(BlockState blockState, NbtCompound tag) {
        super.fromTag(blockState, tag);
        if (tag.contains("coolTime")) coolTime = tag.getDouble("coolTime");
        if (tag.contains("rangePos1X")
                && tag.contains("rangePos1Y")
                && tag.contains("rangePos1Z")
                && tag.contains("rangePos2X")
                && tag.contains("rangePos2Y")
                && tag.contains("rangePos2Z")) {
            setPos1(new BlockPos(tag.getInt("rangePos1X"), tag.getInt("rangePos1Y"), tag.getInt("rangePos1Z")));
            setPos2(new BlockPos(tag.getInt("rangePos2X"), tag.getInt("rangePos2Y"), tag.getInt("rangePos2Z")));
        }

    }

    // ----

    private double defaultBasicSpeed = 5;

    // 基準の速度
    public double getBasicSpeed() {
        return defaultBasicSpeed;
    }

    // TR用のTick
    public void TRTick() {
        super.tick();
    }

    public double defaultSettingCoolTime = 300;

    // クールダウンの基準
    public double getSettingCoolTime() {
        return defaultSettingCoolTime;
    }

    public double coolTime = getSettingCoolTime();

    public void tick() {
        // 1.--
        super.tick();
        if (getWorld() == null || getWorld().isClient())
        {
            return;
        }
        // ----
        BlockState state = getWorld().getBlockState(getPos());
        Filler filler = (Filler) state.getBlock();

        // レッドストーン受信で無効
        if (getWorld().isReceivingRedstonePower(getPos())) {
            if (isActive()) {
                filler.setActive(false, getWorld(), getPos());
            }
            return;
        }

        if (getEnergy() > getEuPerTick(getEnergyCost())) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                //if (tryQuarrying()) {
                //    useEnergy(getEnergyCost());
                //}
            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) {
                filler.setActive(true, getWorld(), getPos());
            }
        } else if (isActive()) {
            filler.setActive(false, getWorld(), getPos());
        }
    }

    // クールダウンのエネルギー量による追加ボーナス
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

    public BlockPos getRangePos1() {
        BlockPos blockPos = null;
        // default
        if (getFacing().equals(Direction.NORTH)) {
            blockPos = new BlockPos(getPos().getX() - 5, getPos().getY(), getPos().getZ() + 11);
        }
        if (getFacing().equals(Direction.SOUTH)) {
            blockPos = new BlockPos(getPos().getX() - 5, getPos().getY(), getPos().getZ() - 1);
        }
        if (getFacing().equals(Direction.WEST)) {
            blockPos = new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ() + 5);
        }
        if (getFacing().equals(Direction.EAST)) {
            blockPos = new BlockPos(getPos().getX() - 11, getPos().getY(), getPos().getZ() + 5);
        }
        return blockPos;
    }

    public BlockPos getRangePos2() {
        BlockPos blockPos = null;
        // default
        if (getFacing().equals(Direction.NORTH)) {
            blockPos = new BlockPos(getPos().getX() + 6, getPos().getY() + 5, getPos().getZ());
        }
        if (getFacing().equals(Direction.SOUTH)) {
            blockPos = new BlockPos(getPos().getX() + 6, getPos().getY() + 5, getPos().getZ() - 12);
        }
        if (getFacing().equals(Direction.WEST)) {
            blockPos = new BlockPos(getPos().getX() + 12, getPos().getY() + 5, getPos().getZ() - 6);
        }
        if (getFacing().equals(Direction.EAST)) {
            blockPos = new BlockPos(getPos().getX(), getPos().getY() + 5, getPos().getZ() - 6);
        }
        return blockPos;
    }

    // マーカーによる範囲指定を許可するか？
    public boolean canSetPosByMarker() {
        return true;
    }

    private BlockPos pos1 = null;
    private BlockPos pos2 = null;

    public BlockPos getPos1() {
        return pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }

    public FillerTile(BlockEntityType<?> type) {
        super(type);
    }


    public void init() {
        /*
        int index = 0;
        SlotConfiguration.SlotIO slotIO = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.OUTPUT);
        SlotConfiguration slotConfig = new SlotConfiguration(getInventory());
        for (;index < getInventory().size();index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, slotIO, index));
            markDirty();
        }
        ((MachineBaseBlockEntityAccessor) this).setSlotConfiguration(slotConfig);
        //FillerPlus.log(Level.INFO, "north output: " + slotConfig.getSlotDetails(0).getSideDetail(Direction.NORTH).getSlotIO().getIoConfig().name());

         */
    }
}
