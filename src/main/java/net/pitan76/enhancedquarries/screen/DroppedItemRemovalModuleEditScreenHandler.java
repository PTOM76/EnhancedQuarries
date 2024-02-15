package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import org.jetbrains.annotations.Nullable;

public class DroppedItemRemovalModuleEditScreenHandler extends SimpleScreenHandler {
    public static NamedScreenHandlerFactory factory = new NamedScreenHandlerFactory() {
        @Override
        public Text getDisplayName() {
            return TextUtil.translatable("screen.enhancedquarries.dropped_item_removal_module_edit.title");
        }

        @Nullable
        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
            return new DroppedItemRemovalModuleEditScreenHandler(syncId, playerInventory);
        }
    };

    public DroppedItemRemovalModuleEditScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(ScreenHandlers.DROPPED_ITEM_REMOVAL_MODULE_EDIT_SCREEN_HANDLER_TYPE, syncId, playerInventory);
    }

    public DroppedItemRemovalModuleEditScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory) {
        super(type, syncId);

    }
}
