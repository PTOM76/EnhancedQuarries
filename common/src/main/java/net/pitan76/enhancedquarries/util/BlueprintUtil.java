package net.pitan76.enhancedquarries.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.block.enums.*;
import net.minecraft.state.property.Properties;
import net.pitan76.easyapi.FileControl;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.util.BlockStateUtil;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.block.BlockUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.nbt.NbtCompound;
import net.pitan76.mcpitanlib.midohra.nbt.NbtElement;
import net.pitan76.mcpitanlib.midohra.nbt.NbtList;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

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
        NbtCompound nbt = writeData(NbtCompound.of(), blocks);
        stack.putCustomNbt("blueprint", nbt);
    }

    public static void writeNbt2(ItemStack stack, Map<net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState> blocks) {
        NbtCompound nbt = writeData2(NbtCompound.of(), blocks);
        stack.putCustomNbt("blueprint", nbt);
    }

    public static Map<BlockPos, BlockState> readNbt(ItemStack stack) {
        NbtCompound nbt = stack.getCustomNbt("blueprint");
        return readData(nbt);
    }

    public static Map<BlockPos, BlockState> readNbt(ItemStack stack, Direction direction) {
        NbtCompound nbt = stack.getCustomNbt("blueprint");
        return readData(nbt, direction);
    }

    public static Map<BlockPos, BlockState> readNbt(ItemStack stack, net.minecraft.util.math.Direction direction) {
        return readNbt(stack, Direction.of(direction));
    }

    public static NbtCompound writeData(NbtCompound nbt, Map<BlockPos, BlockState> blocks) {
        EnhancedQuarries.logIfDev("writedata");
        NbtList nbtList = NbtList.of();

        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            NbtCompound blockNbt = NbtCompound.of();
            NbtCompound posNbt = NbtCompound.of();

            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());

            if (state.contains(CompatProperties.HORIZONTAL_FACING)) {
                blockNbt.putString("horizontal_facing", state.get(CompatProperties.HORIZONTAL_FACING).getRaw().name());
            }
            if (state.contains(CompatProperties.FACING)) {
                blockNbt.putString("facing", state.get(CompatProperties.FACING).getRaw().name());
            }
            if (state.contains(CompatProperties.HOPPER_FACING)) {
                blockNbt.putString("hopper_facing", state.get(CompatProperties.HOPPER_FACING).getRaw().name());
            }

            if (state.contains(CompatProperties.BLOCK_HALF)) {
                blockNbt.putString("block_half", state.get(CompatProperties.BLOCK_HALF).name());
            }
            if (state.contains(CompatProperties.STAIR_SHAPE)) {
                blockNbt.putString("stair_shape", state.get(CompatProperties.STAIR_SHAPE).name());
            }
            if (state.contains(CompatProperties.SLAB_TYPE)) {
                blockNbt.putString("slab_type", state.get(CompatProperties.SLAB_TYPE).name());
            }
            if (state.contains(Properties.BED_PART)) {
                blockNbt.putString("bed_part", state.get(Properties.BED_PART).name());
            }
            if (state.contains(CompatProperties.AXIS)) {
                blockNbt.putString("axis", state.get(CompatProperties.AXIS).name());
            }
            if (state.contains(CompatProperties.HORIZONTAL_AXIS)) {
                blockNbt.putString("horizontal_axis", state.get(CompatProperties.HORIZONTAL_AXIS).name());
            }
            if (state.contains(Properties.CHEST_TYPE)) {
                blockNbt.putString("chest_type", state.get(Properties.CHEST_TYPE).name());
            }
            if (state.contains(Properties.PISTON_TYPE)) {
                blockNbt.putString("piston_type", state.get(Properties.PISTON_TYPE).name());
            }
            if (state.contains(Properties.DOOR_HINGE)) {
                blockNbt.putString("door_hinge", state.get(Properties.DOOR_HINGE).name());
            }
            if (state.contains(Properties.DOUBLE_BLOCK_HALF)) {
                blockNbt.putString("double_block_half", state.get(Properties.DOUBLE_BLOCK_HALF).name());
            }

            blockNbt.put("pos", posNbt);
            blockNbt.putString("id", BlockUtil.toIdAsString(state.getBlock().get()));

            nbtList.add(blockNbt);
        }
        nbt.put("blocks", nbtList);
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

        NbtList nbtList = nbt.getNbtCompoundList("blocks");
        for (NbtElement element : nbtList) {
            if (element.isNbtCompound()) {
                NbtCompound blockNbt = element.asNbtCompound();

                Block block = BlockUtil.fromId(CompatIdentifier.of(blockNbt.getString("id")));
                if (block == null) continue;

                NbtCompound posNbt = blockNbt.getCompound("pos");
                if (posNbt == null) continue;

                BlockState state = BlockState.of(BlockStateUtil.getDefaultState(block));
                BlockPos pos = rotatePos(BlockPos.of(posNbt.getInt("x"), posNbt.getInt("y"), posNbt.getInt("z")), steps);

                if (blockNbt.has("horizontal_facing"))
                    state = withRotatedDirection(state, CompatProperties.HORIZONTAL_FACING, blockNbt.getString("horizontal_facing"), steps);
                if (blockNbt.has("facing"))
                    state = withRotatedDirection(state, CompatProperties.FACING, blockNbt.getString("facing"), steps);
                if (blockNbt.has("hopper_facing"))
                    state = withRotatedDirection(state, CompatProperties.HOPPER_FACING, blockNbt.getString("hopper_facing"), steps);

                if (blockNbt.has("block_half")) {
                    try {
                        state = state.with(CompatProperties.BLOCK_HALF, BlockHalf.valueOf(blockNbt.getString("block_half").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("stair_shape")) {
                    try {
                        state = state.with(CompatProperties.STAIR_SHAPE, StairShape.valueOf(blockNbt.getString("stair_shape").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("slab_type")) {
                    try {
                        state = state.with(Properties.SLAB_TYPE, SlabType.valueOf(blockNbt.getString("slab_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("bed_part")) {
                    try {
                        if (blockNbt.getString("bed_part").equalsIgnoreCase("head")) continue;
                        state = state.with(Properties.BED_PART, BedPart.valueOf(blockNbt.getString("bed_part").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("axis")) {
                    try {
                        state = state.with(Properties.AXIS, rotateAxis(net.minecraft.util.math.Direction.Axis.valueOf(blockNbt.getString("axis").toUpperCase()), steps));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("horizontal_axis")) {
                    try {
                        state = state.with(Properties.HORIZONTAL_AXIS, rotateAxis(net.minecraft.util.math.Direction.Axis.valueOf(blockNbt.getString("horizontal_axis").toUpperCase()), steps));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("chest_type")) {
                    try {
                        state = state.with(Properties.CHEST_TYPE, ChestType.valueOf(blockNbt.getString("chest_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("piston_type")) {
                    try {
                        state = state.with(Properties.PISTON_TYPE, PistonType.valueOf(blockNbt.getString("piston_type").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("door_hinge")) {
                    try {
                        state = state.with(Properties.DOOR_HINGE, DoorHinge.valueOf(blockNbt.getString("door_hinge").toUpperCase()));
                    } catch (IllegalArgumentException ignore) {}
                }
                if (blockNbt.has("double_block_half")) {
                    try {
                        if (blockNbt.getString("double_block_half").equalsIgnoreCase("upper")) continue;
                        state = state.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.valueOf(blockNbt.getString("double_block_half").toUpperCase()));
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

    public static List<String> getBlueprintNames() {
        return listNames(new File(Config.configDir, "blueprint"));
    }

    // ディレクトリ内の.jsonの拡張子を除いたファイル名を返す
    public static List<String> listNames(File dir) {
        List<String> names = new ArrayList<>();
        if (!dir.isDirectory()) return names;

        File[] files = dir.listFiles();
        if (files == null) return names;

        for (File file : files) {
            if (!file.isFile()) continue;

            String fileName = file.getName();
            if (!fileName.endsWith(".json")) continue;

            names.add(fileName.substring(0, fileName.length() - ".json".length()));
        }
        names.sort(String::compareToIgnoreCase);
        return names;
    }

    public static File getBlueprintFile(String name) {
        String normalized = normalizeName(name);
        if (normalized == null) return null;

        return new File(new File(Config.configDir, "blueprint"), normalized + ".json");
    }

    private static final Type BLOCK_MAP_TYPE = new TypeToken<LinkedHashMap<String, LinkedHashMap<String, String>>>() {}.getType();

    // WEST基準
    public static boolean save(ItemStack stack, String name) {
        File file = getBlueprintFile(name);
        if (file == null) return false;

        net.pitan76.mcpitanlib.midohra.nbt.NbtCompound nbt = stack.getCustomNbt("blueprint");
        if (nbt == null) return false;
        if (!nbt.has("blocks")) return false;

        Map<String, Map<String, String>> blocks = new LinkedHashMap<>();

        for (NbtElement element : nbt.getNbtCompoundList("blocks")) {
            if (!(element.isNbtCompound())) continue;
            NbtCompound blockNbt = element.asNbtCompound();

            if (!blockNbt.has("pos")) continue;
            NbtCompound posNbt = blockNbt.getCompound("pos");
            if (posNbt == null) continue;

            Map<String, String> data = new LinkedHashMap<>();
            for (String key : NbtUtil.getKeys(blockNbt.toMinecraft())) {
                if (key.equals("pos")) continue;
                data.put(key, blockNbt.getString(key));
            }

            blocks.put(posNbt.getInt("x") + "," + posNbt.getInt("y") + "," + posNbt.getInt("z"), data);
        }

        if (blocks.isEmpty()) return false;

        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) return false;

        return FileControl.fileWriteContents(file, new Gson().toJson(blocks));
    }

    public static boolean load(ItemStack stack, String name) {
        File file = getBlueprintFile(name);
        if (file == null || !file.exists()) return false;

        String json = FileControl.fileReadContents(file);
        if (json == null || json.isEmpty()) return false;

        Map<String, Map<String, String>> blocks;
        try {
            blocks = new Gson().fromJson(json, BLOCK_MAP_TYPE);
        } catch (JsonSyntaxException ignore) {
            return false;
        }
        if (blocks == null || blocks.isEmpty()) return false;

        NbtList nbtList = NbtList.of();

        for (Map.Entry<String, Map<String, String>> entry : blocks.entrySet()) {
            String[] keys = entry.getKey().split(",");
            if (keys.length != 3) continue;
            if (entry.getValue() == null) continue;

            NbtCompound blockNbt = NbtCompound.of();
            for (Map.Entry<String, String> data : entry.getValue().entrySet()) {
                if (data.getValue() == null) continue;
                blockNbt.putString(data.getKey(), data.getValue());
            }
            if (!blockNbt.has("id")) continue;

            NbtCompound posNbt = NbtCompound.of();
            try {
                posNbt.putInt("x", Integer.parseInt(keys[0]));
                posNbt.putInt("y", Integer.parseInt(keys[1]));
                posNbt.putInt("z", Integer.parseInt(keys[2]));
            } catch (NumberFormatException ignore) {
                continue;
            }

            blockNbt.put("pos", posNbt);
            nbtList.add(blockNbt);
        }

        if (nbtList.isEmpty()) return false;

        NbtCompound nbt = NbtCompound.of();
        nbt.put("blocks", nbtList);
        stack.putCustomNbt("blueprint", nbt);
        return true;
    }
}
