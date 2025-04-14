package com.blocklogic.agritech.compat.jei;

import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.util.RegistryHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;

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

    /**
     * Factory method to create a recipe from seed and soil IDs
     */
    public static PlanterRecipe create(String seedId, String soilId) {
        Ingredient seedIngredient = Ingredient.of(RegistryHelper.getItem(seedId));

        Ingredient soilIngredient = Ingredient.of(RegistryHelper.getBlock(soilId).asItem());

        List<ItemStack> outputs = new ArrayList<>();
        List<AgritechCropConfig.DropInfo> drops = AgritechCropConfig.getCropDrops(seedId);
        LOGGER.info("Drops for Seed ID {}: {}", seedId, drops);

        for (AgritechCropConfig.DropInfo dropInfo : drops) {
            LOGGER.info("Processing Drop Info - Item: {}, Min: {}, Max: {}, Chance: {}",
                    dropInfo.item, dropInfo.minCount, dropInfo.maxCount, dropInfo.chance);
            if (dropInfo.chance > 0) {
                ItemStack outputStack = new ItemStack(
                        RegistryHelper.getItem(dropInfo.item),
                        (dropInfo.minCount + dropInfo.maxCount) / 2
                );
                LOGGER.info("Created Output Stack: {}", outputStack);
                outputs.add(outputStack);
            }
        }

        return new PlanterRecipe(seedIngredient, soilIngredient, outputs);
    }
}