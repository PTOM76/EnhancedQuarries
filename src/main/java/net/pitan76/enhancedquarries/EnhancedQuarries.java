package net.pitan76.enhancedquarries;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.cmd.EnhancedQuarriesCommand;
import net.pitan76.enhancedquarries.compat.RebornEnergyRegister;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.command.CommandRegistry;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.item.CreativeTabBuilder;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.fabric.ExtendModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnhancedQuarries extends ExtendModInitializer {

    public static final String MOD_ID = "enhanced_quarries";
    public static final String MOD_NAME = "Enhanced Quarries";
    public static EnhancedQuarries instance;
    private static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeTabBuilder ENHANCED_QUARRIES_GROUP = CreativeTabBuilder.create(_id("all")).setIcon(() -> ItemStackUtil.create(Items.NORMAL_QUARRY));

    public static CompatRegistryV2 registry;

    @Override
    public void init() {
        instance = this;
        registry = super.registry;

        registry.registerItemGroup(ENHANCED_QUARRIES_GROUP);
        Items.init();
        Blocks.init();
        Tiles.init();
        ScreenHandlers.init();
        FillerModules.init();
        FillerCraftingPatterns.init();
        Config.init();

        ServerNetworking.registerReceiver(_id("blueprint_name"), (e -> {
            String text = PacketByteUtil.readString(e.getBuf());
            Player player = e.getPlayer();

            if (!(player.getCurrentScreenHandler() instanceof LibraryScreenHandler)) return;
            LibraryScreenHandler screenHandler = (LibraryScreenHandler) player.getCurrentScreenHandler();
            screenHandler.setBlueprintName(text);
        }));

        registerEnergyStorage();

        CommandRegistry.register("enhancedquarries", new EnhancedQuarriesCommand());
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
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

    public static void log(String message, boolean isDebug) {
        instance.logger.log(message, isDebug);
    }

    public static void logIfDev(String message) {
        instance.logger.infoIfDev(message);
    }

    public static CompatIdentifier _id(String id) {
        return CompatIdentifier.of(MOD_ID, id);
    }

    public static EnhancedQuarries getInstance() {
        return instance;
    }
}
