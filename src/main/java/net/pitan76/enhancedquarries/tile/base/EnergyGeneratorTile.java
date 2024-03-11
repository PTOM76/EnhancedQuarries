package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.block.base.EnergyGenerator;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EnergyGeneratorTile extends BaseEnergyTile implements IInventory, SidedInventory, ExtendedScreenHandlerFactory {
    public EnergyGeneratorTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public EnergyGeneratorTile(TileCreateEvent event) {
        this(Tiles.ENERGY_GENERATOR_TILE.getOrNull(), event);
    }

    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public int burnTime = 0;
    public boolean burning = false;
    private long lastEnergy = 0;

    @Override
    public long getMaxEnergy() {
        return 10000;
    }

    @Override
    public long getMaxOutput() {
        return 32;
    }

    @Override
    public long getMaxInput() {
        return 0;
    }

    public long getGenerateEnergy() {
        return 10;
    }

    public boolean isBurning() {
        return burning;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, BaseEnergyTile blockEntity) {
        super.tick(world, pos, state, blockEntity);
        if (world == null) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, getPos())) {
            if (isActive())
                EnergyGenerator.setActive(false, world, getPos());
            return;
        }

        if (isActive() != isBurning())
            EnergyGenerator.setActive(isBurning(), world, getPos());

        // 燃焼時間が0の場合
        if (burnTime == 0 && !world.isClient()) {
            burnTime = getBurnTimeFrom(getItems().get(0));
            if (burnTime > 0) {
                ItemStack stack = getItems().get(0);
                // レシピリマインダーがある場合
                if (stack.getItem().hasRecipeRemainder()) {
                    ItemStack remainder = new ItemStack(stack.getItem().getRecipeRemainder(), 1);
                    if (stack.getCount() == 1)
                        // 最大スタック数が1の場合はスタックを置き換える
                        getItems().set(0, remainder);
                    else {
                        // 最大スタック数が1より大きい場合は1つ減らしてドロップ
                        stack.decrement(1);
                        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), remainder);
                    }
                } else {
                    stack.decrement(1);
                }
            }
        }

        // エネルギー容量がある場合
        if (0 < getUsableCapacity()) {
            if (burnTime > 0) {
                burning = true;
                --burnTime;
                insertEnergy(getGenerateEnergy());
            } else {
                burning = false;
            }
        } else if (isActive()) {
            burning = false;
        }

        outputEnergy(this, world, pos);

        if (lastEnergy != getEnergy() && !world.isClient()) {
            for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                if (player.networkHandler != null && player.currentScreenHandler instanceof EnergyGeneratorScreenHandler && ((EnergyGeneratorScreenHandler) player.currentScreenHandler).tile == this ) {
                    PacketByteBuf buf = PacketByteUtil.create();
                    PacketByteUtil.writeLong(buf, getEnergy());
                    ServerNetworking.send(player, EnhancedQuarries.id("energy_generator"), buf);
                }
            }
            lastEnergy = getEnergy();
        }
    }

    public static void outputEnergy(EnergyGeneratorTile blockEntity, World world, BlockPos pos) {
        if (blockEntity.getEnergy() <= 0) return;
        for (Direction direction : Direction.values()) {
            BlockPos targetPos = pos.offset(direction);
            BlockEntity dirTile = world.getBlockEntity(targetPos);
            if (!(dirTile instanceof BaseEnergyTile)) continue;

            BaseEnergyTile targetEntity = (BaseEnergyTile)dirTile;
            long output = getOutputEnergy(blockEntity, targetEntity);
            if (output > 0) {
                blockEntity.useEnergy(output);
                targetEntity.insertEnergy(output);
            }
        }
    }

    private static long getOutputEnergy(EnergyGeneratorTile blockEntity, BaseEnergyTile targetEntity) {
        long maxInput = targetEntity.getMaxInput();
        long usableCapacity = targetEntity.getUsableCapacity();
        long output = blockEntity.getMaxOutput();
        if (usableCapacity < output)
            output = usableCapacity;

        if (output > blockEntity.getEnergy())
            output = blockEntity.getEnergy();

        if (output > maxInput)
            output = maxInput;

        return output;
    }

    public static int getBurnTimeFrom(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return 0;

        Map<Item, Integer> burnMap = AbstractFurnaceBlockEntity.createFuelTimeMap();
        return burnMap.containsKey(stack.getItem()) ? burnMap.get(stack.getItem()) / 4 : 0;
    }

    @Override
    public void readNbtOverride(NbtCompound nbt) {
        super.readNbtOverride(nbt);
        if (nbt.contains("BurnTime"))
            burnTime = nbt.getInt("BurnTime");
        if (nbt.contains("Burning"))
            burning = nbt.getBoolean("Burning");
        if (nbt.contains("Items"))
            InventoryUtil.readNbt(getWorld(), nbt, invItems);
    }

    @Override
    public void writeNbtOverride(NbtCompound nbt) {
        super.writeNbtOverride(nbt);
        nbt.putInt("BurnTime", burnTime);
        nbt.putBoolean("Burning", burning);
        InventoryUtil.writeNbt(getWorld(), nbt, invItems);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return invItems;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir != Direction.DOWN;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN;
    }

    @Override
    public Text getDisplayName()  {
        return TextUtil.translatable("screen.enhanced_quarries.energy_generator.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new EnergyGeneratorScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        PacketByteUtil.writeLong(buf, getEnergy());
        PacketByteUtil.writeLong(buf, getMaxEnergy());
    }
}
