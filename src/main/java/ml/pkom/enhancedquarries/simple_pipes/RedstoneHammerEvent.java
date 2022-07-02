package ml.pkom.enhancedquarries.simple_pipes;

//import alexiil.mc.mod.pipes.blocks.TilePipeItemWood;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Direction;

public class RedstoneHammerEvent {
    public RedstoneHammerEvent(ItemUsageContext context) {
        /*if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof TilePipeItemWood) {
            TilePipeItemWood pipe = (TilePipeItemWood) context.getWorld().getBlockEntity(context.getBlockPos());
            Direction dir = pipe.currentDirection();
            if (dir == null)
                return;
            pipe.tryExtract(dir, 1);
        }*/
    }
}
