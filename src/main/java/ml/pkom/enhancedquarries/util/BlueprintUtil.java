package ml.pkom.enhancedquarries.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ml.pkom.easyapi.FileControl;
import ml.pkom.easyapi.config.JsonConfig;
import ml.pkom.enhancedquarries.easyapi.Compressor;
import ml.pkom.mcpitanlibarch.api.nbt.NbtTag;
import ml.pkom.mcpitanlibarch.api.util.BlockUtil;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.Deflater;

import static ml.pkom.enhancedquarries.Configs.configDir;

public class BlueprintUtil {

    // 相対座標
    public static BlockPos getMaxPos(Map<BlockPos, BlockState> blocks) {
        int x, y, z;
        x = y = z = 0;
        for (BlockPos pos : blocks.keySet()) {
            if (x < pos.getX()) x = pos.getX();
            if (y < pos.getY()) y = pos.getY();
            if (z < pos.getZ()) z = pos.getZ();
        }

        return new BlockPos(x, y, z);
    }

    public static BlockPos getMaxPos(ItemStack stack) {
        return getMaxPos(readNBt(stack));
    }

    public static BlockPos getMinPos(Map<BlockPos, BlockState> blocks) {
        int x, y, z;
        x = y = z = 0;
        for (BlockPos pos : blocks.keySet()) {
            if (x > pos.getX()) x = pos.getX();
            if (y > pos.getY()) y = pos.getY();
            if (z > pos.getZ()) z = pos.getZ();
        }

        return new BlockPos(x, y, z);
    }

    public static BlockPos getMinPos(ItemStack stack) {
        return getMinPos(readNBt(stack));
    }


    public static void writeNbt(ItemStack stack, Map<BlockPos, BlockState> blocks) {
        NbtCompound nbt = writeData(new NbtCompound(), blocks);
        stack.setSubNbt("blueprint", nbt);
    }

    public static Map<BlockPos, BlockState> readNBt(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("blueprint");
        return readData(nbt);
    }

    public static Map<BlockPos, BlockState> readNBt(ItemStack stack, Direction direction) {
        NbtCompound nbt = stack.getSubNbt("blueprint");
        return readData(nbt, direction);
    }

    public static NbtCompound writeData(NbtCompound nbt, Map<BlockPos, BlockState> blocks) {
        System.out.println("writedata");
        NbtList nbtList = new NbtList();

        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            NbtCompound blockNbt = new NbtCompound();
            NbtCompound posNbt = new NbtCompound();

            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());

            if (state.getProperties().contains(Properties.HORIZONTAL_FACING)) {
                blockNbt.putString("horizontal_facing", state.get(Properties.HORIZONTAL_FACING).name());
            }
            if (state.getProperties().contains(Properties.FACING)) {
                blockNbt.putString("facing", state.get(Properties.FACING).name());
            }
            if (state.getProperties().contains(Properties.HOPPER_FACING)) {
                blockNbt.putString("hopper_facing", state.get(Properties.HOPPER_FACING).name());
            }

            if (state.getProperties().contains(Properties.BLOCK_HALF)) {
                blockNbt.putString("block_half", state.get(Properties.BLOCK_HALF).name());
            }
            if (state.getProperties().contains(Properties.STAIR_SHAPE)) {
                blockNbt.putString("stair_shape", state.get(Properties.STAIR_SHAPE).name());
            }
            if (state.getProperties().contains(Properties.SLAB_TYPE)) {
                blockNbt.putString("slab_type", state.get(Properties.SLAB_TYPE).name());
            }
            if (state.getProperties().contains(Properties.BED_PART)) {
                blockNbt.putString("bed_part", state.get(Properties.BED_PART).name());
            }
            if (state.getProperties().contains(Properties.AXIS)) {
                blockNbt.putString("axis", state.get(Properties.AXIS).name());
            }
            if (state.getProperties().contains(Properties.HORIZONTAL_AXIS)) {
                blockNbt.putString("horizontal_axis", state.get(Properties.HORIZONTAL_AXIS).name());
            }
            if (state.getProperties().contains(Properties.CHEST_TYPE)) {
                blockNbt.putString("chest_type", state.get(Properties.CHEST_TYPE).name());
            }
            if (state.getProperties().contains(Properties.PISTON_TYPE)) {
                blockNbt.putString("piston_type", state.get(Properties.PISTON_TYPE).name());
            }
            if (state.getProperties().contains(Properties.DOOR_HINGE)) {
                blockNbt.putString("door_hinge", state.get(Properties.DOOR_HINGE).name());
            }
            if (state.getProperties().contains(Properties.DOUBLE_BLOCK_HALF)) {
                blockNbt.putString("double_block_half", state.get(Properties.DOUBLE_BLOCK_HALF).name());
            }

            blockNbt.put("pos", posNbt);
            blockNbt.putString("id", BlockUtil.toID(state.getBlock()).toString());

            nbtList.add(blockNbt);
        }
        nbt.put("blocks", nbtList);
        return nbt;
    }


    public static Map<BlockPos, BlockState> readData(NbtCompound nbt) {
        return readData(nbt, Direction.NORTH);
    }

    public static Map<BlockPos, BlockState> readData(NbtCompound nbt, Direction direction) {
        Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();

        NbtList nbtList = nbt.getList("blocks", NbtType.COMPOUND);
        for (NbtElement element : nbtList) {
            if (element instanceof NbtCompound) {
                NbtCompound blockNbt = (NbtCompound) element;

                Block block = BlockUtil.fromId(new Identifier(blockNbt.getString("id")));
                NbtCompound posNbt = blockNbt.getCompound("pos");
                BlockState state = block.getDefaultState();
                BlockPos pos = new BlockPos(posNbt.getInt("x"), posNbt.getInt("y"), posNbt.getInt("z"));

                if (direction == Direction.NORTH)
                    pos = new BlockPos(- posNbt.getInt("z"), posNbt.getInt("y"), posNbt.getInt("x"));
                if (direction == Direction.SOUTH)
                    pos = new BlockPos( posNbt.getInt("z"), posNbt.getInt("y"), - posNbt.getInt("x"));
                if (direction == Direction.EAST)
                    pos = new BlockPos(- posNbt.getInt("x"), posNbt.getInt("y"), - posNbt.getInt("z"));
                if (direction == Direction.WEST)
                    pos = new BlockPos(posNbt.getInt("x"), posNbt.getInt("y"), posNbt.getInt("z"));

                if (blockNbt.contains("horizontal_facing")) {
                    try {
                        if (direction == Direction.WEST)
                            state = state.with(Properties.HORIZONTAL_FACING, getDirectionFromName(blockNbt.getString("horizontal_facing")));
                        if (direction == Direction.NORTH)
                            state = state.with(Properties.HORIZONTAL_FACING, getDirectionFromName(blockNbt.getString("horizontal_facing")).rotateYClockwise());
                        if (direction == Direction.EAST)
                            state = state.with(Properties.HORIZONTAL_FACING, getDirectionFromName(blockNbt.getString("horizontal_facing")).rotateYClockwise().rotateYClockwise());
                        if (direction == Direction.SOUTH)
                            state = state.with(Properties.HORIZONTAL_FACING, getDirectionFromName(blockNbt.getString("horizontal_facing")).rotateYCounterclockwise());
                    } catch (IllegalStateException ignore) {
                        state = state.with(Properties.HORIZONTAL_FACING, getDirectionFromName(blockNbt.getString("horizontal_facing")));
                    }
                }
                if (blockNbt.contains("facing")) {
                    try {
                        if (direction == Direction.WEST)
                            state = state.with(Properties.FACING, getDirectionFromName(blockNbt.getString("facing")));
                        if (direction == Direction.NORTH)
                            state = state.with(Properties.FACING, getDirectionFromName(blockNbt.getString("facing")).rotateYClockwise());
                        if (direction == Direction.EAST)
                            state = state.with(Properties.FACING, getDirectionFromName(blockNbt.getString("facing")).rotateYClockwise().rotateYClockwise());
                        if (direction == Direction.SOUTH)
                            state = state.with(Properties.FACING, getDirectionFromName(blockNbt.getString("facing")).rotateYCounterclockwise());
                    } catch (IllegalStateException ignore) {
                        state = state.with(Properties.FACING, getDirectionFromName(blockNbt.getString("facing")));
                    }
                }
                if (blockNbt.contains("hopper_facing")) {
                    try {
                        if (direction == Direction.WEST)
                            state = state.with(Properties.HOPPER_FACING, getDirectionFromName(blockNbt.getString("hopper_facing")));
                        if (direction == Direction.NORTH)
                            state = state.with(Properties.HOPPER_FACING, getDirectionFromName(blockNbt.getString("hopper_facing")).rotateYClockwise());
                        if (direction == Direction.EAST)
                            state = state.with(Properties.HOPPER_FACING, getDirectionFromName(blockNbt.getString("hopper_facing")).rotateYClockwise().rotateYClockwise());
                        if (direction == Direction.SOUTH)
                            state = state.with(Properties.HOPPER_FACING, getDirectionFromName(blockNbt.getString("hopper_facing")).rotateYCounterclockwise());
                    } catch (IllegalStateException ignore) {
                        state = state.with(Properties.HOPPER_FACING, getDirectionFromName(blockNbt.getString("hopper_facing")));
                    }
                }

                if (blockNbt.contains("block_half")) {
                    try {
                        state = state.with(Properties.BLOCK_HALF, BlockHalf.valueOf(blockNbt.getString("block_half").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("stair_shape")) {
                    try {
                        state = state.with(Properties.STAIR_SHAPE, StairShape.valueOf(blockNbt.getString("stair_shape").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("slab_type")) {
                    try {
                        state = state.with(Properties.SLAB_TYPE, SlabType.valueOf(blockNbt.getString("slab_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("bed_part")) {
                    try {
                        if (blockNbt.getString("bed_part").equalsIgnoreCase("head")) continue;
                        state = state.with(Properties.BED_PART, BedPart.valueOf(blockNbt.getString("bed_part").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("axis")) {
                    try {
                        state = state.with(Properties.AXIS, Direction.Axis.valueOf(blockNbt.getString("axis").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("horizontal_axis")) {
                    try {
                        state = state.with(Properties.HORIZONTAL_AXIS, Direction.Axis.valueOf(blockNbt.getString("horizontal_axis").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("chest_type")) {
                    try {
                        state = state.with(Properties.CHEST_TYPE, ChestType.valueOf(blockNbt.getString("chest_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("piston_type")) {
                    try {
                        state = state.with(Properties.PISTON_TYPE, PistonType.valueOf(blockNbt.getString("piston_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("door_hinge")) {
                    try {
                        state = state.with(Properties.DOOR_HINGE, DoorHinge.valueOf(blockNbt.getString("door_hinge").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.contains("double_block_half")) {
                    try {
                        if (blockNbt.getString("double_block_half").equalsIgnoreCase("upper")) continue;
                        state = state.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.valueOf(blockNbt.getString("double_block_half").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (state.getBlock() instanceof PillarBlock) {
                    if (direction == Direction.WEST)
                        PillarBlock.changeRotation(state, BlockRotation.CLOCKWISE_90);
                    if (direction == Direction.NORTH)
                        for (int i = 0; i < 2; i++)
                            PillarBlock.changeRotation(state, BlockRotation.CLOCKWISE_90);
                    if (direction == Direction.EAST)
                        for (int i = 0; i < 3; i++)
                            PillarBlock.changeRotation(state, BlockRotation.CLOCKWISE_90);
                    if (direction == Direction.SOUTH)
                        for (int i = 0; i < 4; i++)
                            PillarBlock.changeRotation(state, BlockRotation.CLOCKWISE_90);
                }

                blocks.put(pos, state);
            }
        }

        return blocks;
    }

    public static Direction getDirectionFromName(String name) {
        if (name.equalsIgnoreCase("up")) return Direction.UP;
        if (name.equalsIgnoreCase("down")) return Direction.DOWN;
        if (name.equalsIgnoreCase("north")) return Direction.NORTH;
        if (name.equalsIgnoreCase("south")) return Direction.SOUTH;
        if (name.equalsIgnoreCase("east")) return Direction.EAST;
        if (name.equalsIgnoreCase("west")) return Direction.WEST;

        return Direction.NORTH;
    }

    // WEST基準
    public static boolean save(ItemStack stack, String name) {
        JsonConfig config = new JsonConfig();

        NbtCompound nbt = stack.getSubNbt("blueprint");

        if (nbt == null) return false;
        if (!nbt.contains("blocks")) return false;

        NbtList nbtList = nbt.getList("blocks", NbtType.COMPOUND);
        for (NbtElement element : nbtList) {
            if (element instanceof NbtCompound) {
                NbtCompound blockNbt = (NbtCompound) element;
                NbtCompound posNbt = blockNbt.getCompound("pos");

                Map<String, Object> data = new LinkedHashMap<>();

                for (String key : blockNbt.getKeys()) {
                    data.put(key, blockNbt.get(key));
                }

                config.set(posNbt.getInt("x") + "," + posNbt.getInt("y") + "," + posNbt.getInt("z"), data);
            }
        }

        String json = config.toJson(false);

        String compressed = Compressor.compress(json);
        File dir = new File(configDir, "blueprint");
        if (!dir.exists()) dir.mkdirs();

        FileControl.fileWriteContents(new File(dir, name + ".ebp"), compressed);

        return true;
    }

    public static boolean load(ItemStack stack, String name) {

        File dir = new File(configDir, "blueprint");

        if (!new File(dir, name + ".ebp").exists()) return false;

        String compressed = FileControl.fileReadContents(new File(dir, name + ".ebp"));
        String json = Compressor.decompress(compressed);
        JsonConfig config = new JsonConfig();

        Gson gson = new Gson();
        Type jsonMap = new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType();

        config.configMap = gson.fromJson(json, jsonMap);

        NbtCompound nbt = NbtTag.create();

        NbtList nbtList = new NbtList();

        for (Map.Entry<String, Object> entry : config.configMap.entrySet()) {
            String key = entry.getKey();
            String[] keys = key.split(",");
            if (keys.length != 3) continue;
            BlockPos pos = new BlockPos(Integer.parseInt(keys[0]), Integer.parseInt(keys[1]), Integer.parseInt(keys[2]));

            if (!(entry.getValue() instanceof Map)) continue;
            Map<String, Object> data = (Map<String, Object>) entry.getValue();

            NbtCompound blockNbt = new NbtCompound();
            NbtCompound posNbt = new NbtCompound();

            for (Map.Entry<String, Object> entry1 : data.entrySet()) {
                blockNbt.put(entry1.getKey(), (NbtCompound) entry1.getValue());

            }

            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());
            blockNbt.put("pos", posNbt);

            nbtList.add(blockNbt);
        }

        nbt.put("blocks", nbtList);
        stack.setSubNbt("blueprint", nbt);

        return true;
    }

}
