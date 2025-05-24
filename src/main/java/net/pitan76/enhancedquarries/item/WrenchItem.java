package net.pitan76.enhancedquarries.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.BaseBlock;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.BlockRotations;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

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
        BlockState state = e.getBlockState();
        Block block = BlockStateUtil.getBlock(state);
        BlockEntity blockEntity = e.getBlockEntity();

        if (player.isSneaking()) {
            if (!(blockEntity instanceof BaseEnergyTile)) return super.onRightClickOnBlock(e);
            BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
            energyTile.keepNbtOnDrop = true;

            ItemStack stack = ItemStackUtil.create(block);

            BlockEntityDataUtil.writeCompatBlockEntityNbtToStack(stack, energyTile);
            ItemEntityUtil.createWithSpawn(world, stack, PosUtil.x(pos) + 0.5D, PosUtil.y(pos) + 0.5D, PosUtil.z(pos) + 0.5D);
            WorldUtil.breakBlock(world, pos, false);

        } else {
            if (!(block instanceof BaseBlock)) return super.onRightClickOnBlock(e);

            WorldUtil.setBlockState(world, pos, BlockStateUtil.rotate(state, BlockRotations.CLOCKWISE_90));
            return e.success();
        }

        return super.onRightClickOnBlock(e);
    }
}
