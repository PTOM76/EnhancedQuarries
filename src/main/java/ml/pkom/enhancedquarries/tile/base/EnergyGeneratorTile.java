package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.block.base.EnergyGenerator;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.mcpitanlibarch.api.gui.inventory.IInventory;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.mcpitanlibarch.api.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EnergyGeneratorTile extends BaseEnergyTile implements IInventory, SidedInventory, NamedScreenHandlerFactory {
    public EnergyGeneratorTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public EnergyGeneratorTile(TileCreateEvent event) {
        this(Tiles.ENERGY_GENERATOR_TILE.getOrNull(), event);
    }

    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public int burnTime;
    public boolean burning;

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
        if (world == null || world.isClient()) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, getPos())) {
            if (isActive())
                EnergyGenerator.setActive(false, world, getPos());
            return;
        }

        if (isActive() != isBurning())
            EnergyGenerator.setActive(isBurning(), world, getPos());

        // 燃焼時間が0の場合
        if (burnTime == 0) {
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
            Inventories.readNbt(nbt, invItems);
    }

    @Override
    public void writeNbtOverride(NbtCompound nbt) {
        super.writeNbtOverride(nbt);
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
        return null;
    }
}
