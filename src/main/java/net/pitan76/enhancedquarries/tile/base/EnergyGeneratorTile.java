package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
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
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.VanillaStyleSidedInventory;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.args.AvailableSlotsArgs;
import net.pitan76.mcpitanlib.api.gui.v2.ExtendedScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import net.pitan76.mcpitanlib.core.registry.FuelRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyGeneratorTile extends BaseEnergyTile implements IInventory, VanillaStyleSidedInventory, ExtendedScreenHandlerFactory {
    public EnergyGeneratorTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    public EnergyGeneratorTile(TileCreateEvent e) {
        this(Tiles.ENERGY_GENERATOR_TILE.getOrNull(), e);
    }

    public ItemStackList invItems = ItemStackList.ofSize(1, ItemStackUtil.empty());

    public int burnTime = 0;
    public int maxBurnTime = 0;
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
            burnTime = getBurnTimeFrom(world, getItems().get(0));
            maxBurnTime = burnTime;
            if (burnTime > 0) {
                ItemStack stack = getItems().get(0);
                // レシピリマインダーがある場合
                if (ItemUtil.hasRecipeRemainder(stack.getItem())) {
                    ItemStack remainder = ItemStackUtil.create(ItemUtil.getRecipeRemainder(stack.getItem()), 1);
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
                PacketByteUtil.writeInt(buf, burnTime);
                PacketByteUtil.writeInt(buf, maxBurnTime);
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

    public static int getBurnTimeFrom(World world, @NotNull ItemStack stack) {
        if (stack.isEmpty()) return 0;

        //Map<Item, Integer> burnMap = AbstractFurnaceBlockEntity.createFuelTimeMap();
        ItemStack copy = ItemStackUtil.copyWithCount(stack, 1);
        try {
            return FuelRegistry.get(world, copy) / 4;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        NbtCompound nbt = args.getNbt();
        if (NbtUtil.has(nbt, "BurnTime"))
            burnTime = NbtUtil.getInt(nbt, "BurnTime");
        if (NbtUtil.has(nbt, "MaxBurnTime"))
            maxBurnTime = NbtUtil.getInt(nbt, "MaxBurnTime");
        if (NbtUtil.has(nbt, "Burning"))
            burning = NbtUtil.getBoolean(nbt, "Burning");
        if (NbtUtil.has(nbt, "Items"))
            InventoryUtil.readNbt(args, invItems);
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        super.writeNbt(args);
        NbtCompound nbt = args.getNbt();
        NbtUtil.putInt(nbt, "BurnTime", burnTime);
        NbtUtil.putInt(nbt, "MaxBurnTime", maxBurnTime);
        NbtUtil.putBoolean(nbt, "Burning", burning);
        InventoryUtil.writeNbt(args, invItems);
    }

    @Override
    public ItemStackList getItems() {
        return invItems;
    }

    @Override
    public int[] getAvailableSlots(AvailableSlotsArgs args) {
        return new int[]{0};
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
        PacketByteUtil.writeInt(args.getBuf(), burnTime);
        PacketByteUtil.writeInt(args.getBuf(), maxBurnTime);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new EnergyGeneratorScreenHandler(e, this);
    }
}
