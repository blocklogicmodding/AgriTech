package com.blocklogic.agritech.command;

import com.blocklogic.agritech.config.AgritechCropConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AgritechCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("agritech")
                        .requires(source -> source.hasPermission(2)) // Requires operator level 2
                        .then(Commands.literal("reload")
                                .executes(context -> {
                                    try {
                                        AgritechCropConfig.loadConfig();
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("AgriTech crop config reloaded successfully!"), true);
                                        return 1;
                                    } catch (Exception e) {
                                        context.getSource().sendFailure(
                                                Component.literal("Failed to reload AgriTech crop config: " + e.getMessage()));
                                        return 0;
                                    }
                                })
                        )
        );
    }
}