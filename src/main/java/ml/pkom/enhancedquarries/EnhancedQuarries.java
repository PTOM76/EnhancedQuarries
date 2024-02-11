package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.compat.RebornEnergyRegister;
import ml.pkom.enhancedquarries.screen.LibraryScreenHandler;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.item.CreativeTabBuilder;
import ml.pkom.mcpitanlibarch.api.network.PacketByteUtil;
import ml.pkom.mcpitanlibarch.api.network.ServerNetworking;
import ml.pkom.mcpitanlibarch.api.registry.ArchRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnhancedQuarries implements ModInitializer {

    public static final String MOD_ID = "enhanced_quarries";
    public static final String MOD_NAME = "Enhanced Quarries";
    public static EnhancedQuarries instance;
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup ENHANCED_QUARRIES_GROUP = CreativeTabBuilder.create(id("all")).setIcon(() -> new ItemStack(Items.NORMAL_QUARRY)).build();

    public static ArchRegistry registry = ArchRegistry.createRegistry(MOD_ID);

    public void onInitialize() {
        instance = this;
        log(Level.INFO, "Initializing");

        registry.registerItemGroup(id("all"), () -> ENHANCED_QUARRIES_GROUP);
        Items.init();
        Blocks.init();
        Tiles.init();
        ScreenHandlers.init();
        FillerModules.init();
        FillerCraftingPatterns.init();
        Config.init();

        ServerNetworking.registerReceiver(id("blueprint_name"), ((server, p, buf) -> {
            String text = PacketByteUtil.readString(buf);
            Player player = new Player(p);
            if (!(player.getCurrentScreenHandler() instanceof LibraryScreenHandler)) return;
            LibraryScreenHandler screenHandler = (LibraryScreenHandler) player.getCurrentScreenHandler();
            screenHandler.setBlueprintName(text);
        }));

        registerEnergyStorage();

        registry.allRegister();
    }

    //@SuppressWarnings("UnstableApiUsage")
    // まだ未使用
    /*
    public static void registerFluidStorage() {
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, dir) -> {
            if (!(blockEntity instanceof QuarryTile)) return null;
            QuarryTile quarryTile = (QuarryTile) blockEntity;
            return quarryTile.getFluidStorage();

        }, Tiles.NORMAL_QUARRY_TILE.getOrNull());

    }
     */

    public static void registerEnergyStorage() {
        if (FabricLoader.getInstance().isModLoaded("team_reborn_energy")) {
            RebornEnergyRegister.init();
        }
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
