package net.pitan76.enhancedquarries.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.BaseBlock;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.BlockEntityDataUtil;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;

public class WrenchItem extends CompatItem {
    public WrenchItem(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (e.isClient()) return e.success();
        Player player = e.getPlayer();
        World world = e.getWorld();
        BlockPos pos = e.getBlockPos();
        BlockState state = WorldUtil.getBlockState(world, pos);
        BlockEntity blockEntity = e.getBlockEntity();
        if (player.isSneaking()) {
            if (!(blockEntity instanceof BaseEnergyTile)) return super.onRightClickOnBlock(e);
            BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
            energyTile.keepNbtOnDrop = true;

            ItemStack stack = ItemStackUtil.create(state.getBlock());

            BlockEntityDataUtil.writeCompatBlockEntityNbtToStack(stack, energyTile);

            ItemEntity itemEntity = ItemEntityUtil.create(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
            ItemEntityUtil.setToDefaultPickupDelay(itemEntity);
            WorldUtil.spawnEntity(world, itemEntity);

            WorldUtil.breakBlock(world, pos, false);

        } else {
            if (!(state.getBlock() instanceof BaseBlock)) return super.onRightClickOnBlock(e);

            WorldUtil.setBlockState(world, pos, state.rotate(BlockRotation.CLOCKWISE_90));
            return e.success();
        }

        return super.onRightClickOnBlock(e);
    }
}
