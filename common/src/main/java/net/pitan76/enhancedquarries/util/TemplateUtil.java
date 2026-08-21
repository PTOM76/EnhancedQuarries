package net.pitan76.enhancedquarries.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.pitan76.easyapi.FileControl;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// テンプレートはブロックの種類を記憶せず、形（座標）だけを保持する
public class TemplateUtil {

    public static final String KEY = "template";

    public static boolean has(ItemStack stack) {
        return CustomDataUtil.get(stack.toMinecraft(), KEY) != null;
    }

    public static BlockPos getMaxPos(Collection<BlockPos> positions) {
        int x, y, z;
        x = y = z = 0;
        for (BlockPos pos : positions) {
            if (x < pos.getX()) x = pos.getX();
            if (y < pos.getY()) y = pos.getY();
            if (z < pos.getZ()) z = pos.getZ();
        }

        return BlockPos.of(x, y, z);
    }

    public static BlockPos getMinPos(Collection<BlockPos> positions) {
        int x, y, z;
        x = y = z = 0;
        for (BlockPos pos : positions) {
            if (x > pos.getX()) x = pos.getX();
            if (y > pos.getY()) y = pos.getY();
            if (z > pos.getZ()) z = pos.getZ();
        }

        return BlockPos.of(x, y, z);
    }

    public static void writeNbt(ItemStack stack, Collection<BlockPos> positions) {
        CustomDataUtil.set(stack.toMinecraft(), KEY, writeData(NbtUtil.create(), positions));
    }

    public static Set<BlockPos> readNbt(ItemStack stack, Direction direction) {
        return readData(CustomDataUtil.get(stack.toMinecraft(), KEY), direction);
    }

    public static Set<BlockPos> readNbt(ItemStack stack, net.minecraft.util.math.Direction direction) {
        return readNbt(stack, Direction.of(direction));
    }

    public static NbtCompound writeData(NbtCompound nbt, Collection<BlockPos> positions) {
        NbtList nbtList = NbtUtil.createNbtList();

        for (BlockPos pos : positions) {
            NbtCompound posNbt = NbtUtil.create();
            NbtUtil.putInt(posNbt, "x", pos.getX());
            NbtUtil.putInt(posNbt, "y", pos.getY());
            NbtUtil.putInt(posNbt, "z", pos.getZ());

            nbtList.add(posNbt);
        }

        NbtUtil.put(nbt, "positions", nbtList);
        return nbt;
    }

    public static Set<BlockPos> readData(NbtCompound nbt, Direction direction) {
        Set<BlockPos> positions = new LinkedHashSet<>();
        if (nbt == null) return positions;

        int steps = BlueprintUtil.getRotationSteps(direction);

        for (NbtElement element : NbtUtil.getNbtCompoundList(nbt, "positions")) {
            if (!(element instanceof NbtCompound)) continue;
            NbtCompound posNbt = (NbtCompound) element;

            positions.add(BlueprintUtil.rotatePos(BlockPos.of(NbtUtil.getInt(posNbt, "x"),
                    NbtUtil.getInt(posNbt, "y"), NbtUtil.getInt(posNbt, "z")), steps));
        }

        return positions;
    }

    public static File getTemplateFile(String name) {
        String normalized = BlueprintUtil.normalizeName(name);
        if (normalized == null) return null;

        return new File(new File(Config.configDir, "template"), normalized + ".json");
    }

    public static boolean save(ItemStack stack, String name) {
        File file = getTemplateFile(name);
        if (file == null) return false;

        NbtCompound nbt = CustomDataUtil.get(stack.toMinecraft(), KEY);
        if (nbt == null) return false;

        List<String> positions = new ArrayList<>();
        for (NbtElement element : NbtUtil.getNbtCompoundList(nbt, "positions")) {
            if (!(element instanceof NbtCompound)) continue;
            NbtCompound posNbt = (NbtCompound) element;

            positions.add(NbtUtil.getInt(posNbt, "x") + "," + NbtUtil.getInt(posNbt, "y") + "," + NbtUtil.getInt(posNbt, "z"));
        }
        if (positions.isEmpty()) return false;

        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) return false;

        FileControl.fileWriteContents(file, new Gson().toJson(positions));
        return true;
    }

    public static boolean load(ItemStack stack, String name) {
        File file = getTemplateFile(name);
        if (file == null || !file.exists()) return false;

        String json = FileControl.fileReadContents(file);
        if (json == null || json.isEmpty()) return false;

        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();

        List<String> positions = new Gson().fromJson(json, listType);
        if (positions == null || positions.isEmpty()) return false;

        Set<BlockPos> result = new LinkedHashSet<>();
        for (String position : positions) {
            String[] keys = position.split(",");
            if (keys.length != 3) continue;

            try {
                result.add(BlockPos.of(Integer.parseInt(keys[0]), Integer.parseInt(keys[1]), Integer.parseInt(keys[2])));
            } catch (NumberFormatException ignore) {}
        }
        if (result.isEmpty()) return false;

        writeNbt(stack, result);
        return true;
    }
}
