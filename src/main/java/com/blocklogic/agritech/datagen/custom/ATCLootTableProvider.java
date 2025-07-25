package com.blocklogic.agritech.datagen.custom;

import com.blocklogic.agritech.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ATCLootTableProvider extends BlockLootSubProvider {
    public ATCLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.AGRITECH_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.ACACIA_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.BAMBOO_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.BIRCH_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.CHERRY_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.CRIMSON_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.DARK_OAK_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.JUNGLE_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.MANGROVE_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.SPRUCE_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.WARPED_PLANTER_BLOCK.get());

        dropSelf(ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.ACACIA_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.BAMBOO_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.BIRCH_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.CHERRY_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.CRIMSON_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.DARK_OAK_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.JUNGLE_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.MANGROVE_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.SPRUCE_HOPPING_PLANTER_BLOCK.get());
        dropSelf(ModBlocks.WARPED_HOPPING_PLANTER_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
