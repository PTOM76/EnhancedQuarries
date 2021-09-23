package ml.pkom.enhancedquarries.block.base;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.fluid.impl.EmptyFluidExtractable;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class Pump extends BaseBlock implements BlockEntityProvider, AttributeProvider {
    public static FabricBlockSettings defaultSettings = FabricBlockSettings
            .of(Material.METAL)
            .requiresTool()
            .breakByTool(FabricToolTags.PICKAXES, 0)
            .strength(2, 8);

    // Custom Setting
    public Pump(Settings settings) {
        super(settings);
    }

    // Default Setting
    public Pump() {
        this(defaultSettings);
    }

    // 1.17.1へのポート用
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(new TileCreateEvent(pos, state));
    }

    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
        to.offer(EmptyFluidExtractable.SUPPLIER);
    }

    public BlockEntity createBlockEntity(BlockView world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public abstract BlockEntity createBlockEntity(TileCreateEvent event);


}
