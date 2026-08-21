package net.pitan76.enhancedquarries.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.block.enums.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.state.property.Properties;
import net.pitan76.easyapi.FileControl;
import net.pitan76.easyapi.config.JsonConfig;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.util.BlockStateUtil;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.block.BlockUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

        return BlockPos.of(x, y, z);
    }

    public static BlockPos getMaxPos(ItemStack stack) {
        return getMaxPos(readNbt(stack));
    }

    public static BlockPos getMinPos(Map<BlockPos, BlockState> blocks) {
        int x, y, z;
        x = y = z = 0;
        for (BlockPos pos : blocks.keySet()) {
            if (x > pos.getX()) x = pos.getX();
            if (y > pos.getY()) y = pos.getY();
            if (z > pos.getZ()) z = pos.getZ();
        }

        return BlockPos.of(x, y, z);
    }

    public static BlockPos getMinPos(ItemStack stack) {
        return getMinPos(readNbt(stack));
    }


    public static void writeNbt(ItemStack stack, Map<BlockPos, BlockState> blocks) {
        NbtCompound nbt = writeData(NbtUtil.create(), blocks);
        CustomDataUtil.set(stack.toMinecraft(), "blueprint", nbt);
    }

    public static void writeNbt2(ItemStack stack, Map<net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState> blocks) {
        NbtCompound nbt = writeData2(NbtUtil.create(), blocks);
        CustomDataUtil.set(stack.toMinecraft(), "blueprint", nbt);
    }

    public static Map<BlockPos, BlockState> readNbt(ItemStack stack) {
        NbtCompound nbt = CustomDataUtil.get(stack.toMinecraft(), "blueprint");
        return readData(nbt);
    }

    public static Map<BlockPos, BlockState> readNbt(ItemStack stack, Direction direction) {
        NbtCompound nbt = CustomDataUtil.get(stack.toMinecraft(), "blueprint");
        return readData(nbt, direction);
    }

    public static Map<BlockPos, BlockState> readNbt(ItemStack stack, net.minecraft.util.math.Direction direction) {
        return readNbt(stack, Direction.of(direction));
    }

    public static NbtCompound writeData(NbtCompound nbt, Map<BlockPos, BlockState> blocks) {
        EnhancedQuarries.logIfDev("writedata");
        NbtList nbtList = NbtUtil.createNbtList();

        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            NbtCompound blockNbt = NbtUtil.create();
            NbtCompound posNbt = NbtUtil.create();

            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());

            if (state.contains(CompatProperties.HORIZONTAL_FACING)) {
                NbtUtil.putString(blockNbt, "horizontal_facing", state.get(CompatProperties.HORIZONTAL_FACING).getRaw().name());
            }
            if (state.contains(CompatProperties.FACING)) {
                NbtUtil.putString(blockNbt, "facing", state.get(CompatProperties.FACING).getRaw().name());
            }
            if (state.contains(CompatProperties.HOPPER_FACING)) {
                NbtUtil.putString(blockNbt, "hopper_facing", state.get(CompatProperties.HOPPER_FACING).getRaw().name());
            }

            if (state.contains(CompatProperties.BLOCK_HALF)) {
                NbtUtil.putString(blockNbt, "block_half", state.get(CompatProperties.BLOCK_HALF).name());
            }
            if (state.contains(CompatProperties.STAIR_SHAPE)) {
                NbtUtil.putString(blockNbt, "stair_shape", state.get(CompatProperties.STAIR_SHAPE).name());
            }
            if (state.contains(Properties.SLAB_TYPE)) {
                NbtUtil.putString(blockNbt, "slab_type", state.get(Properties.SLAB_TYPE).name());
            }
            if (state.contains(Properties.BED_PART)) {
                NbtUtil.putString(blockNbt, "bed_part", state.get(Properties.BED_PART).name());
            }
            if (state.contains(Properties.AXIS)) {
                NbtUtil.putString(blockNbt, "axis", state.get(Properties.AXIS).name());
            }
            if (state.contains(Properties.HORIZONTAL_AXIS)) {
                NbtUtil.putString(blockNbt, "horizontal_axis", state.get(Properties.HORIZONTAL_AXIS).name());
            }
            if (state.contains(Properties.CHEST_TYPE)) {
                NbtUtil.putString(blockNbt, "chest_type", state.get(Properties.CHEST_TYPE).name());
            }
            if (state.contains(Properties.PISTON_TYPE)) {
                NbtUtil.putString(blockNbt, "piston_type", state.get(Properties.PISTON_TYPE).name());
            }
            if (state.contains(Properties.DOOR_HINGE)) {
                NbtUtil.putString(blockNbt, "door_hinge", state.get(Properties.DOOR_HINGE).name());
            }
            if (state.contains(Properties.DOUBLE_BLOCK_HALF)) {
                NbtUtil.putString(blockNbt, "double_block_half", state.get(Properties.DOUBLE_BLOCK_HALF).name());
            }

            NbtUtil.put(blockNbt, "pos", posNbt);
            NbtUtil.putString(blockNbt, "id", BlockUtil.toIdAsString(state.getBlock().get()));

            nbtList.add(blockNbt);
        }
        NbtUtil.put(nbt, "blocks", nbtList);
        return nbt;
    }

    public static NbtCompound writeData2(NbtCompound nbt, Map<net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState> blocks) {
        Map<BlockPos, BlockState> blocks2 = new LinkedHashMap<>();
        for (Map.Entry<net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState> entry : blocks.entrySet()) {
            blocks2.put(BlockPos.of(entry.getKey()), BlockState.of(entry.getValue()));
        }

        return writeData(nbt, blocks2);
    }

    public static Map<BlockPos, BlockState> readData(NbtCompound nbt) {
        return readData(nbt, Direction.NORTH);
    }

    // WEST基準からの時計回りの回転回数 (WEST -> NORTH -> EAST -> SOUTH)
    public static int getRotationSteps(Direction direction) {
        if (Direction.NORTH.equals(direction)) return 1;
        if (Direction.EAST.equals(direction)) return 2;
        if (Direction.SOUTH.equals(direction)) return 3;
        return 0;
    }

    // (x, z) -> (-z, x) をsteps回
    public static BlockPos rotatePos(BlockPos pos, int steps) {
        int x = pos.getX();
        int z = pos.getZ();
        for (int i = 0; i < steps; i++) {
            int newX = -z;
            z = x;
            x = newX;
        }
        return BlockPos.of(x, pos.getY(), z);
    }

    public static Direction rotateDirection(Direction direction, int steps) {
        if (direction.isVertical()) return direction;

        Direction result = direction;
        for (int i = 0; i < steps; i++) {
            result = result.rotateYClockwise();
        }
        return result;
    }

    public static net.minecraft.util.math.Direction.Axis rotateAxis(net.minecraft.util.math.Direction.Axis axis, int steps) {
        if (steps % 2 == 0) return axis;
        if (axis == net.minecraft.util.math.Direction.Axis.X) return net.minecraft.util.math.Direction.Axis.Z;
        if (axis == net.minecraft.util.math.Direction.Axis.Z) return net.minecraft.util.math.Direction.Axis.X;
        return axis;
    }

    private static BlockState withRotatedDirection(BlockState state, net.pitan76.mcpitanlib.api.state.property.DirectionProperty property, String name, int steps) {
        Direction direction = getDirectionFromName(name);
        try {
            return state.with(property, rotateDirection(direction, steps));
        } catch (IllegalArgumentException | IllegalStateException ignore) {
            try {
                // そのプロパティに使えない値なら回転前の値で妥協する
                return state.with(property, direction);
            } catch (IllegalArgumentException | IllegalStateException ignore2) {
                return state;
            }
        }
    }

    public static Map<BlockPos, BlockState> readData(NbtCompound nbt, Direction direction) {
        Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
        int steps = getRotationSteps(direction);

        NbtList nbtList = NbtUtil.getNbtCompoundList(nbt, "blocks");
        for (NbtElement element : nbtList) {
            if (element instanceof NbtCompound) {
                NbtCompound blockNbt = (NbtCompound) element;

                Block block = BlockUtil.fromId(CompatIdentifier.of(NbtUtil.getString(blockNbt, "id")));
                if (block == null) continue;

                NbtCompound posNbt = NbtUtil.get(blockNbt, "pos");
                if (posNbt == null) continue;

                BlockState state = BlockState.of(BlockStateUtil.getDefaultState(block));
                BlockPos pos = rotatePos(BlockPos.of(NbtUtil.getInt(posNbt, "x"), NbtUtil.getInt(posNbt, "y"), NbtUtil.getInt(posNbt, "z")), steps);

                if (NbtUtil.has(blockNbt, "horizontal_facing"))
                    state = withRotatedDirection(state, CompatProperties.HORIZONTAL_FACING, NbtUtil.getString(blockNbt, "horizontal_facing"), steps);
                if (NbtUtil.has(blockNbt, "facing"))
                    state = withRotatedDirection(state, CompatProperties.FACING, NbtUtil.getString(blockNbt, "facing"), steps);
                if (NbtUtil.has(blockNbt, "hopper_facing"))
                    state = withRotatedDirection(state, CompatProperties.HOPPER_FACING, NbtUtil.getString(blockNbt, "hopper_facing"), steps);

                if (NbtUtil.has(blockNbt, "block_half")) {
                    try {
                        state = state.with(CompatProperties.BLOCK_HALF, BlockHalf.valueOf(NbtUtil.getString(blockNbt, "block_half").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "stair_shape")) {
                    try {
                        state = state.with(CompatProperties.STAIR_SHAPE, StairShape.valueOf(NbtUtil.getString(blockNbt, "stair_shape").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "slab_type")) {
                    try {
                        state = state.with(Properties.SLAB_TYPE, SlabType.valueOf(NbtUtil.getString(blockNbt, "slab_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "bed_part")) {
                    try {
                        if (NbtUtil.getString(blockNbt, "bed_part").equalsIgnoreCase("head")) continue;
                        state = state.with(Properties.BED_PART, BedPart.valueOf(NbtUtil.getString(blockNbt, "bed_part").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "axis")) {
                    try {
                        state = state.with(Properties.AXIS, rotateAxis(net.minecraft.util.math.Direction.Axis.valueOf(NbtUtil.getString(blockNbt, "axis").toUpperCase()), steps));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "horizontal_axis")) {
                    try {
                        state = state.with(Properties.HORIZONTAL_AXIS, rotateAxis(net.minecraft.util.math.Direction.Axis.valueOf(NbtUtil.getString(blockNbt, "horizontal_axis").toUpperCase()), steps));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "chest_type")) {
                    try {
                        state = state.with(Properties.CHEST_TYPE, ChestType.valueOf(NbtUtil.getString(blockNbt, "chest_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "piston_type")) {
                    try {
                        state = state.with(Properties.PISTON_TYPE, PistonType.valueOf(NbtUtil.getString(blockNbt, "piston_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "door_hinge")) {
                    try {
                        state = state.with(Properties.DOOR_HINGE, DoorHinge.valueOf(NbtUtil.getString(blockNbt, "door_hinge").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (NbtUtil.has(blockNbt, "double_block_half")) {
                    try {
                        if (NbtUtil.getString(blockNbt, "double_block_half").equalsIgnoreCase("upper")) continue;
                        state = state.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.valueOf(NbtUtil.getString(blockNbt, "double_block_half").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
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

    // ファイル名として使えない名前ならnullを返す
    public static String normalizeName(String name) {
        if (name == null) return null;

        String normalized = name.trim();
        if (normalized.isEmpty()) return null;
        if (normalized.equals(".") || normalized.equals("..")) return null;

        for (char c : normalized.toCharArray()) {
            if (c < 0x20) return null;
            if ("/\\:*?\"<>|".indexOf(c) != -1) return null;
        }

        return normalized;
    }

    public static File getBlueprintFile(String name) {
        String normalized = normalizeName(name);
        if (normalized == null) return null;

        return new File(new File(Config.configDir, "blueprint"), normalized + ".json");
    }

    // WEST基準
    public static boolean save(ItemStack stack, String name) {
        File file = getBlueprintFile(name);
        if (file == null) return false;

        JsonConfig config = new JsonConfig();

        NbtCompound nbt = CustomDataUtil.get(stack.toMinecraft(), "blueprint");

        if (nbt == null) return false;
        if (!NbtUtil.has(nbt, "blocks")) return false;

        NbtList nbtList = NbtUtil.getNbtCompoundList(nbt, "blocks");
        for (NbtElement element : nbtList) {
            if (element instanceof NbtCompound) {
                NbtCompound blockNbt = (NbtCompound) element;
                NbtCompound posNbt = NbtUtil.get(blockNbt, "pos");

                Map<String, Object> data = new LinkedHashMap<>();

                for (String key : NbtUtil.getKeys(blockNbt)) {
                    if (Objects.equals(key, "pos")) continue;
                    data.put(key, NbtUtil.get(blockNbt, key));
                }

                config.set(NbtUtil.getInt(posNbt, "x") + "," + NbtUtil.getInt(posNbt, "y") + "," + NbtUtil.getInt(posNbt, "z"), data);
            }
        }

        String json = config.toJson(false);

        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) return false;

        FileControl.fileWriteContents(file, json);

        return true;
    }

    public static boolean load(ItemStack stack, String name) {
        File file = getBlueprintFile(name);
        if (file == null || !file.exists()) return false;

        String json = FileControl.fileReadContents(file);
        if (json == null || json.isEmpty()) return false;

        Gson gson = new Gson();
        Type jsonMap = new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType();

        Map<String, Object> map = gson.fromJson(json, jsonMap);
        if (map == null || map.isEmpty()) return false;

        NbtCompound nbt = NbtUtil.create();

        NbtList nbtList = NbtUtil.createNbtList();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] keys = key.split(",");
            if (keys.length != 3) continue;
            BlockPos pos = BlockPos.of(Integer.parseInt(keys[0]), Integer.parseInt(keys[1]), Integer.parseInt(keys[2]));

            if (!(entry.getValue() instanceof Map)) continue;
            Map<String, Object> data = (Map<String, Object>) entry.getValue();

            NbtCompound blockNbt = NbtUtil.create();
            NbtCompound posNbt = NbtUtil.create();

            for (Map.Entry<String, Object> entry1 : data.entrySet()) {
                Object value = entry1.getValue();
                if (value instanceof Map) {
                    Map<String, Object> mapInValue = (Map<String, Object>) value;
                    for (Map.Entry<String, Object> value2 : mapInValue.entrySet()) {
                        if (Objects.equals(value2.getKey(), "value")) {
                            value = value2.getValue();
                            break;
                        }
                    }
                }
                if (value instanceof String)
                    NbtUtil.putString(blockNbt, entry1.getKey(), (String) value);
                if (value instanceof Integer)
                    NbtUtil.putInt(blockNbt, entry1.getKey(), (int) value);
                if (value instanceof Short)
                    NbtUtil.putShort(blockNbt, entry1.getKey(), (short) value);
                if (value instanceof Long)
                    NbtUtil.putLong(blockNbt, entry1.getKey(), (long) value);
                if (value instanceof Double)
                    NbtUtil.putDouble(blockNbt, entry1.getKey(), (double) value);
                if (value instanceof Float)
                    NbtUtil.putFloat(blockNbt, entry1.getKey(), (float) value);
                if (value instanceof Boolean)
                    NbtUtil.putBoolean(blockNbt, entry1.getKey(), (boolean) value);
                if (value instanceof Byte)
                    NbtUtil.putByte(blockNbt, entry1.getKey(), (byte) value);
                if (value instanceof NbtCompound)
                    NbtUtil.put(blockNbt, entry1.getKey(), (NbtCompound) value);
            }

            NbtUtil.putInt(posNbt, "x", pos.getX());
            NbtUtil.putInt(posNbt, "y", pos.getY());
            NbtUtil.putInt(posNbt, "z", pos.getZ());
            NbtUtil.put(blockNbt, "pos", posNbt);

            nbtList.add(blockNbt);
        }

        if (nbtList.isEmpty()) return false;

        NbtUtil.put(nbt, "blocks", nbtList);
        CustomDataUtil.set(stack.toMinecraft(), "blueprint", nbt);
        return true;
    }

}
