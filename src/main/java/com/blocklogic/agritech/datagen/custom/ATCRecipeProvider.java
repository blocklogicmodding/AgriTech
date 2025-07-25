package com.blocklogic.agritech.datagen.custom;

import com.blocklogic.agritech.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ATCRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ATCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AGRITECH_PLANTER_BLOCK.get())
                .pattern("S S")
                .pattern("PDP")
                .pattern("LPL")
                .define('S', Items.OAK_SLAB)
                .define('P', Items.OAK_PLANKS)
                .define('L', ItemTags.OAK_LOGS)
                .define('D', Items.DIRT)
                .unlockedBy("has_oak_logs", has(ItemTags.OAK_LOGS))
                .save(recipeOutput);
    }
}
