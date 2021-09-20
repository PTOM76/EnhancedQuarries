package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.tile.Tiles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnhancedQuarries implements ModInitializer {

    public static final String MOD_ID = "enhanced_quarries";
    public static final String MOD_NAME = "Enhanced Quarries";
    public static final String VERSION = "1.1.0";
    public static EnhancedQuarries instance;
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup FILLER_PLUS_GROUP = FabricItemGroupBuilder.build(
            id("all"),
            () -> new ItemStack(Items.NORMAL_QUARRY));

    public void onInitialize() {
        instance = this;
        log(Level.INFO, "Initializing");

        Items.init();
        Blocks.init();
        Tiles.init();
        ScreenHandlers.init();
        FillerModules.init();
        FillerCraftingPatterns.init();
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static Identifier id(String id, boolean bool) {
        if (bool) return new Identifier(MOD_ID, id);
        return new Identifier(id);
    }

    public static EnhancedQuarries getInstance() {
        return instance;
    }
}
