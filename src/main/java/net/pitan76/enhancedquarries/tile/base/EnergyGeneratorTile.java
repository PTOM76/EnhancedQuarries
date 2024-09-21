package net.pitan76.enhancedquarries.tile.base;

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
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.container.factory.ExtraDataArgs;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.*;
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

    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(1, ItemStackUtil.empty());

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
    public void tick(TileTickEvent<BaseEnergyTile> e) {
        super.tick(e);
        World world = e.world;
        BlockPos pos = e.pos;
        if (world == null) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, pos)) {
            if (isActive())
                EnergyGenerator.setActive(false, world, pos);
            
            return;
        }

        if (isActive() != isBurning())
            EnergyGenerator.setActive(isBurning(), world, pos);

        // 燃焼時間が0の場合
        if (burnTime == 0 && !WorldUtil.isClient(world)) {
            burnTime = getBurnTimeFrom(getItems().get(0));
            if (burnTime > 0) {
                ItemStack stack = getItems().get(0);
                // レシピリマインダーがある場合
                if (stack.getItem().hasRecipeRemainder()) {
                    ItemStack remainder = ItemStackUtil.create(stack.getItem().getRecipeRemainder(), 1);
                    if (stack.getCount() == 1)
                        // 最大スタック数が1の場合はスタックを置き換える
                        getItems().set(0, remainder);
                    else {
                        // 最大スタック数が1より大きい場合は1つ減らしてドロップ
                        ItemStackUtil.decrementCount(stack, 1);
                        ItemScattererUtil.spawn(world, pos, remainder);
                    }
                } else {
                    ItemStackUtil.decrementCount(stack, 1);
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

        if (lastEnergy == getEnergy() || WorldUtil.isClient(world)) return;
        
        for (Player player : WorldUtil.getPlayers(world)) {
            if (player.hasNetworkHandler() && player.getCurrentScreenHandler() instanceof EnergyGeneratorScreenHandler
                    && ((EnergyGeneratorScreenHandler) player.getCurrentScreenHandler()).tile == this) {
                PacketByteBuf buf = PacketByteUtil.create();
                PacketByteUtil.writeLong(buf, getEnergy());
                ServerNetworking.send(player, EnhancedQuarries._id("energy_generator_sync"), buf);
            }
        }
        lastEnergy = getEnergy();
    }

    public static void outputEnergy(EnergyGeneratorTile blockEntity, World world, BlockPos pos) {
        if (blockEntity.getEnergy() <= 0) return;
        for (Direction direction : Direction.values()) {
            BlockPos targetPos = pos.offset(direction);
            BlockEntity dirTile = WorldUtil.getBlockEntity(world, targetPos);
            if (!(dirTile instanceof BaseEnergyTile)) continue;

            BaseEnergyTile targetEntity = (BaseEnergyTile) dirTile;
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
    public void readNbt(ReadNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        if (NbtUtil.has(nbt, "BurnTime"))
            burnTime = NbtUtil.getInt(nbt, "BurnTime");
        if (NbtUtil.has(nbt, "Burning"))
            burning = NbtUtil.getBoolean(nbt, "Burning");
        if (NbtUtil.has(nbt, "Items"))
            InventoryUtil.readNbt(args, invItems);
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        NbtUtil.putInt(nbt, "BurnTime", burnTime);
        NbtUtil.putBoolean(nbt, "Burning", burning);
        InventoryUtil.writeNbt(args, invItems);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return invItems;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{0};
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
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhanced_quarries.energy_generator.title");
    }

    @Override
    public void writeExtraData(ExtraDataArgs args) {
        if (!args.hasBuf()) return;
        
        PacketByteUtil.writeLong(args.getBuf(), getEnergy());
        PacketByteUtil.writeLong(args.getBuf(), getMaxEnergy());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new EnergyGeneratorScreenHandler(syncId, playerInventory, this);
    }
}
