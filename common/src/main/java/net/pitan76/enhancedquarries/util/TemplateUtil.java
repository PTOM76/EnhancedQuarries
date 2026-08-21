package net.pitan76.enhancedquarries.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.pitan76.easyapi.FileControl;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.nbt.NbtCompound;
import net.pitan76.mcpitanlib.midohra.nbt.NbtElement;
import net.pitan76.mcpitanlib.midohra.nbt.NbtList;
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
        return getData(stack) != null;
    }

    public static NbtCompound getData(ItemStack stack) {
        return stack.getCustomNbt(KEY);
    }

    public static void setData(ItemStack stack, NbtCompound nbt) {
        stack.putCustomNbt(KEY, nbt);
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
        setData(stack, writeData(NbtCompound.of(), positions));
    }

    public static Set<BlockPos> readNbt(ItemStack stack, Direction direction) {
        return readData(getData(stack), direction);
    }

    public static Set<BlockPos> readNbt(ItemStack stack, net.minecraft.util.math.Direction direction) {
        return readNbt(stack, Direction.of(direction));
    }

    public static NbtCompound writeData(NbtCompound nbt, Collection<BlockPos> positions) {
        NbtList nbtList = NbtList.of();

        for (BlockPos pos : positions) {
            NbtCompound posNbt = NbtCompound.of();
            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());

            nbtList.add(posNbt);
        }

        nbt.put("positions", nbtList);
        return nbt;
    }

    public static Set<BlockPos> readData(NbtCompound nbt, Direction direction) {
        Set<BlockPos> positions = new LinkedHashSet<>();
        if (nbt == null) return positions;

        int steps = BlueprintUtil.getRotationSteps(direction);

        for (NbtElement element : nbt.getNbtCompoundList("positions")) {
            if (!element.isNbtCompound()) continue;
            NbtCompound posNbt = element.asNbtCompound();

            positions.add(BlueprintUtil.rotatePos(BlockPos.of(posNbt.getInt("x"),
                    posNbt.getInt("y"), posNbt.getInt("z")), steps));
        }

        return positions;
    }

    public static List<String> getTemplateNames() {
        return BlueprintUtil.listNames(new File(Config.configDir, "template"));
    }

    public static File getTemplateFile(String name) {
        String normalized = BlueprintUtil.normalizeName(name);
        if (normalized == null) return null;

        return new File(new File(Config.configDir, "template"), normalized + ".json");
    }

    public static boolean save(ItemStack stack, String name) {
        File file = getTemplateFile(name);
        if (file == null) return false;

        NbtCompound nbt = getData(stack);
        if (nbt == null) return false;

        List<String> positions = new ArrayList<>();
        for (NbtElement element : nbt.getNbtCompoundList("positions")) {
            if (!element.isNbtCompound()) continue;
            NbtCompound posNbt = element.asNbtCompound();

            positions.add(posNbt.getInt("x") + "," + posNbt.getInt("y") + "," + posNbt.getInt("z"));
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
