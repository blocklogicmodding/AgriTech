package com.blocklogic.agritech.command;

import com.blocklogic.agritech.Config;
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
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("reload")
                                .then(Commands.literal("crops")
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
                                .then(Commands.literal("config")
                                        .executes(context -> {
                                            try {
                                                Config.loadConfig();
                                                context.getSource().sendSuccess(() ->
                                                        Component.literal("AgriTech main config reloaded successfully!"), true);
                                                return 1;
                                            } catch (Exception e) {
                                                context.getSource().sendFailure(
                                                        Component.literal("Failed to reload AgriTech main config: " + e.getMessage()));
                                                return 0;
                                            }
                                        })
                                )
                                .executes(context -> {
                                    try {
                                        Config.loadConfig();
                                        AgritechCropConfig.loadConfig();
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("All AgriTech configs reloaded successfully!"), true);
                                        return 1;
                                    } catch (Exception e) {
                                        context.getSource().sendFailure(
                                                Component.literal("Failed to reload AgriTech configs: " + e.getMessage()));
                                        return 0;
                                    }
                                })
                        )
        );
    }
}