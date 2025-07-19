package com.blocklogic.agritech.compat.jei;

import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.util.RegistryHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PlanterRecipe implements IRecipeCategoryExtension {
    private final Ingredient seedIngredient;
    private final Ingredient soilIngredient;
    private final List<ItemStack> outputs;

    public PlanterRecipe(Ingredient seedIngredient, Ingredient soilIngredient, List<ItemStack> outputs) {
        this.seedIngredient = seedIngredient;
        this.soilIngredient = soilIngredient;
        this.outputs = outputs;
    }

    public Ingredient getSeedIngredient() {
        return seedIngredient;
    }

    public Ingredient getSoilIngredient() {
        return soilIngredient;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public static PlanterRecipe create(String seedId, String soilId) {
        net.minecraft.world.item.Item seedItem = RegistryHelper.getItem(seedId);
        if (seedItem == null) {
            com.mojang.logging.LogUtils.getLogger().error("Failed to create planter recipe: Seed item not found for ID: {}", seedId);
            throw new IllegalArgumentException("Seed item not found for ID: " + seedId);
        }

        net.minecraft.world.level.block.Block soilBlock = RegistryHelper.getBlock(soilId);
        if (soilBlock == null) {
            com.mojang.logging.LogUtils.getLogger().error("Failed to create planter recipe: Soil block not found for ID: {}", soilId);
            throw new IllegalArgumentException("Soil block not found for ID: " + soilId);
        }

        Ingredient seedIngredient = Ingredient.of(seedItem);
        Ingredient soilIngredient = Ingredient.of(soilBlock.asItem());

        List<ItemStack> outputs = new ArrayList<>();
        List<AgritechCropConfig.DropInfo> drops = AgritechCropConfig.getCropDrops(seedId);

        for (AgritechCropConfig.DropInfo dropInfo : drops) {
            if (dropInfo.chance > 0) {
                net.minecraft.world.item.Item dropItem = RegistryHelper.getItem(dropInfo.item);
                if (dropItem != null) {
                    ItemStack outputStack = new ItemStack(
                            dropItem,
                            (dropInfo.minCount + dropInfo.maxCount) / 2
                    );
                    outputs.add(outputStack);
                } else {
                    com.mojang.logging.LogUtils.getLogger().error("Drop item not found for ID: {} in recipe for seed {}", dropInfo.item, seedId);
                    throw new IllegalArgumentException("Drop item not found for ID: " + dropInfo.item + " in recipe for seed " + seedId);
                }
            }
        }

        return new PlanterRecipe(seedIngredient, soilIngredient, outputs);
    }
}