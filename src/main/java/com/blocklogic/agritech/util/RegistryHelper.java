package com.blocklogic.agritech.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

/**
 * Helper class for registry-related operations
 */
public class RegistryHelper {
    private static final Logger LOGGER = LogUtils.getLogger();
    /**
     * Gets the registry ID for an item
     *
     * @param item The item
     * @return The registry ID as a string
     */
    public static String getItemId(Item item) {
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(item);
        return registryName.toString();
    }

    /**
     * Gets the registry ID for an item stack
     *
     * @param stack The item stack
     * @return The registry ID as a string
     */
    public static String getItemId(ItemStack stack) {
        return getItemId(stack.getItem());
    }

    /**
     * Gets the registry ID for a block
     *
     * @param block The block
     * @return The registry ID as a string
     */
    public static String getBlockId(Block block) {
        ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(block);
        return registryName.toString();
    }

    /**
     * Gets an item from its registry ID
     *
     * @param id The registry ID
     * @return The item, or null if not found
     */
    public static Item getItem(String id) {
        try {
            ResourceLocation resourceLocation = ResourceLocation.parse(id);
            if (BuiltInRegistries.ITEM.containsKey(resourceLocation)) {
                return BuiltInRegistries.ITEM.get(resourceLocation);
            }
        } catch (Exception e){
            LOGGER.error("Invalid item ID in config: {}", id, e);
        }
        return null;
    }

    /**
     * Gets a block from its registry ID
     *
     * @param id The registry ID
     * @return The block, or null if not found
     */
    public static Block getBlock(String id) {
        try {
            ResourceLocation resourceLocation = ResourceLocation.parse(id);
            if (BuiltInRegistries.BLOCK.containsKey(resourceLocation)) {
                return BuiltInRegistries.BLOCK.get(resourceLocation);
            }
        } catch (Exception e) {
            LOGGER.error("Invalid block ID in config: {}", id, e);
        }
        return null;
    }

}