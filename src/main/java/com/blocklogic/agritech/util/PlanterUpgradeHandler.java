package com.blocklogic.agritech.util;

import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class PlanterUpgradeHandler {

    public static boolean canUpgrade(ItemStack upgradeItem, Block currentBlock) {
        if (upgradeItem.getItem() != ModItems.HOPPING_UPGRADE.get()) {
            return false;
        }

        return currentBlock == ModBlocks.AGRITECH_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.ACACIA_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.BAMBOO_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.BIRCH_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.CHERRY_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.CRIMSON_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.DARK_OAK_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.JUNGLE_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.MANGROVE_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.SPRUCE_PLANTER_BLOCK.get() ||
                currentBlock == ModBlocks.WARPED_PLANTER_BLOCK.get();
    }

    public static Block getHoppingVariant(Block basicPlanter) {
        if (basicPlanter == ModBlocks.AGRITECH_PLANTER_BLOCK.get()) {
            return ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.ACACIA_PLANTER_BLOCK.get()) {
            return ModBlocks.ACACIA_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.BAMBOO_PLANTER_BLOCK.get()) {
            return ModBlocks.BAMBOO_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.BIRCH_PLANTER_BLOCK.get()) {
            return ModBlocks.BIRCH_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.CHERRY_PLANTER_BLOCK.get()) {
            return ModBlocks.CHERRY_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.CRIMSON_PLANTER_BLOCK.get()) {
            return ModBlocks.CRIMSON_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.DARK_OAK_PLANTER_BLOCK.get()) {
            return ModBlocks.DARK_OAK_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.JUNGLE_PLANTER_BLOCK.get()) {
            return ModBlocks.JUNGLE_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.MANGROVE_PLANTER_BLOCK.get()) {
            return ModBlocks.MANGROVE_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.SPRUCE_PLANTER_BLOCK.get()) {
            return ModBlocks.SPRUCE_HOPPING_PLANTER_BLOCK.get();
        } else if (basicPlanter == ModBlocks.WARPED_PLANTER_BLOCK.get()) {
            return ModBlocks.WARPED_HOPPING_PLANTER_BLOCK.get();
        }
        return null;
    }

    public static boolean performUpgrade(Level level, BlockPos pos, BlockState currentState, Player player, InteractionHand hand) {
        ItemStack hopperItem = player.getItemInHand(hand);
        Block currentBlock = currentState.getBlock();

        if (!canUpgrade(hopperItem, currentBlock)) {
            return false;
        }

        Block targetBlock = getHoppingVariant(currentBlock);
        if (targetBlock == null) {
            return false;
        }

        BlockEntity currentEntity = level.getBlockEntity(pos);
        List<ItemStack> inventoryItems = new ArrayList<>();

        if (currentEntity instanceof AgritechPlanterBlockEntity planter) {
            inventoryItems = extractInventory(planter.inventory);

            for (int i = 0; i < planter.inventory.getSlots(); i++) {
                planter.inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }

        BlockState newState = targetBlock.defaultBlockState();
        level.setBlock(pos, newState, 3);

        BlockEntity newEntity = level.getBlockEntity(pos);
        if (newEntity instanceof AgritechPlanterBlockEntity newPlanter) {
            restoreInventory(newPlanter.inventory, inventoryItems);
            newEntity.setChanged();
        }

        if (!player.isCreative()) {
            hopperItem.shrink(1);
        }

        level.playSound(null, pos, SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS, 0.5F, 1.2F);

        return true;
    }

    private static List<ItemStack> extractInventory(ItemStackHandler inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            items.add(stack.copy());
        }
        return items;
    }

    private static void restoreInventory(ItemStackHandler newInventory, List<ItemStack> items) {
        int slotsToFill = Math.min(items.size(), newInventory.getSlots());
        for (int i = 0; i < slotsToFill; i++) {
            newInventory.setStackInSlot(i, items.get(i));
        }
    }
}