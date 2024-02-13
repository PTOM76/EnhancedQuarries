package net.pitan76.enhancedquarries.cmd;

import net.pitan76.enhancedquarries.Config;
import ml.pkom.mcpitanlibarch.api.command.CommandSettings;
import ml.pkom.mcpitanlibarch.api.command.ConfigCommand;
import ml.pkom.mcpitanlibarch.api.command.LiteralCommand;
import ml.pkom.mcpitanlibarch.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class EnhancedQuarriesCommand extends LiteralCommand {

    @Override
    public void init(CommandSettings settings) {
        addArgumentCommand("config", new ConfigCommand(Config.config, Config.getConfigFile(), "[Enhanced Quarries]"));

        addArgumentCommand("reload", new LiteralCommand() {
            @Override
            public void init(CommandSettings settings) {
                settings.permissionLevel(2);
            }

            @Override
            public void execute(ServerCommandEvent event) {
                event.sendSuccess(TextUtil.literal("[Enhanced Quarries] Reloading..."), false);
                if (Config.reload()) {
                    event.sendSuccess(TextUtil.literal("[Enhanced Quarries] Reloaded!"), false);
                } else {
                    event.sendFailure(TextUtil.literal("[Enhanced Quarries] Failed to reload!"));
                }
            }
        });
    }

    @Override
    public void execute(ServerCommandEvent event) {
        event.sendSuccess(TextUtil.literal("[Enhanced Quarries]"
                + "\n- /enhancedquarries reload...Reload config"
                + "\n- /enhancedquarries config set [Key] [Value]...Set config"
                + "\n- /enhancedquarries config get [Key]...Get config"
                + "\n- /enhancedquarries config list...List config"
        ), false);
    }
}
