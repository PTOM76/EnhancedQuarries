package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.command.EnhancedQuarriesCommand;
import net.pitan76.enhancedquarries.item.quarrymodule.ModuleItems;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.command.CommandRegistry;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.item.CreativeTabBuilder;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.transfer.energy.v1.EnergyLookup;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.midohra.registry.MidohraRegistryV2;
import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.transfer.energy.v1.EnergyLookup;

public class EnhancedQuarries extends CommonModInitializer {

    public static final String MOD_ID = "enhanced_quarries";
    public static final String MOD_NAME = "Enhanced Quarries";
    public static EnhancedQuarries instance;

    public static final CreativeTabBuilder ENHANCED_QUARRIES_GROUP = CreativeTabBuilder.create(_id("all")).setIconM(() -> Items.NORMAL_QUARRY.createStack());

    public static CompatRegistryV2 registry;
    public static MidohraRegistryV2 registry2;

    @Override
    public void init() {
        instance = this;
        registry = super.registry;
        registry2 = MidohraRegistryV2.of(registry);

        registry.registerItemGroup(ENHANCED_QUARRIES_GROUP);
        Blocks.init();
        Items.init();
        Tiles.init();
        ScreenHandlers.init();
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

    public static void postInit() {
        ModuleItems.init();
        FillerModules.init();
        FillerCraftingPatterns.init();
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
        if (!EnergyLookup.ENERGY.isSupported()) return;

        for (RegistryResult<BlockEntityType<?>> tileType : Tiles.getEnergyTileTypes()) {
            BlockEntityType<?> type = tileType.getOrNull();
            if (type == null) continue;

            EnergyLookup.ENERGY.registerForBlockEntity((blockEntity, dir) -> {
                if (!(blockEntity instanceof BaseEnergyTile)) return null;

                return ((BaseEnergyTile) blockEntity).getEnergyStorage();
            }, type);
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
