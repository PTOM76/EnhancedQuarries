package net.pitan76.enhancedquarries.item;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseOnBlockEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.BaseBlock;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;

public class WrenchItem extends ExtendItem {
    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (e.isClient()) return e.success();
        Player player = e.getPlayer();
        World world = e.getWorld();
        BlockPos pos = e.getBlockPos();
        BlockState state = world.getBlockState(pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (player.isSneaking()) {
            if (!(blockEntity instanceof BaseEnergyTile)) return super.onRightClickOnBlock(e);
            BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
            energyTile.keepNbtOnDrop = true;

            ItemStack stack = new ItemStack(state.getBlock());
            NbtCompound nbt = new NbtCompound();
            energyTile.writeNbtOverride(nbt);
            stack.setSubNbt("BlockEntityTag", nbt);

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);

            world.breakBlock(pos, false);

        } else {
            if (!(state.getBlock() instanceof BaseBlock)) return super.onRightClickOnBlock(e);

            world.setBlockState(pos, state.rotate(BlockRotation.CLOCKWISE_90));
            return e.success();
        }

        return super.onRightClickOnBlock(e);
    }
}
